"""
NEXUS Audio Analysis Engine
Extracts acoustic bio-markers and spectral characteristics to detect synthetic/manipulated speech.
"""

import io
import wave
import numpy as np
from scipy import signal
from typing import Dict, Any, Tuple, List


def read_audio_data(file_bytes: bytes) -> Tuple[np.ndarray, int, float]:
    """
    Read WAV audio bytes into a floating-point numpy array (-1.0 to 1.0).
    Returns (samples_array, sample_rate, duration_seconds).
    """
    try:
        with io.BytesIO(file_bytes) as bio:
            with wave.open(bio, "rb") as wf:
                channels = wf.getnchannels()
                sample_width = wf.getsampwidth()
                sample_rate = wf.getframerate()
                n_frames = wf.getnframes()
                raw_data = wf.readframes(n_frames)

                if sample_width == 1:
                    data = np.frombuffer(raw_data, dtype=np.uint8).astype(np.float32)
                    data = (data - 128.0) / 128.0
                elif sample_width == 2:
                    data = np.frombuffer(raw_data, dtype=np.int16).astype(np.float32) / 32768.0
                elif sample_width == 4:
                    data = np.frombuffer(raw_data, dtype=np.int32).astype(np.float32) / 2147483648.0
                else:
                    raise ValueError(f"Unsupported sample width: {sample_width} bytes")

                # Convert multi-channel to mono
                if channels > 1:
                    data = data.reshape(-1, channels).mean(axis=1)

                duration = len(data) / float(sample_rate)
                return data, sample_rate, duration
    except Exception as e:
        # Fallback raw byte interpretation if standard wave fails (e.g. PCM stream)
        try:
            data = np.frombuffer(file_bytes, dtype=np.int16).astype(np.float32) / 32768.0
            sample_rate = 16000
            duration = len(data) / float(sample_rate)
            return data, sample_rate, duration
        except Exception:
            raise ValueError(f"Unable to parse audio stream: {str(e)}")


def validate_audio(samples: np.ndarray, sample_rate: int, duration: float) -> Tuple[bool, str]:
    """
    Validates audio suitability for forensic analysis.
    """
    if duration < 0.6:
        return False, "Audio is too short for reliable analysis (minimum 0.6s required)."

    # Check RMS energy / silence
    rms = np.sqrt(np.mean(samples ** 2))
    if rms < 0.003:
        return False, "Audio is silent or near-zero volume."

    # Check for excessive clipping
    clipping_ratio = np.mean(np.abs(samples) > 0.98)
    if clipping_ratio > 0.15:
        return False, "Audio is severely distorted/clipped for reliable spectral analysis."

    return True, "Audio quality valid."


def estimate_pitch_contour(samples: np.ndarray, sample_rate: int, frame_size_ms: int = 40, hop_size_ms: int = 20) -> np.ndarray:
    """
    Estimates fundamental frequency (F0) using autocorrelation across frames.
    """
    frame_len = int(sample_rate * frame_size_ms / 1000)
    hop_len = int(sample_rate * hop_size_ms / 1000)
    
    # Human pitch bounds (70 Hz to 450 Hz)
    min_lag = int(sample_rate / 450)
    max_lag = int(sample_rate / 70)

    f0_values = []
    
    for i in range(0, len(samples) - frame_len, hop_len):
        frame = samples[i:i + frame_len]
        # Skip low-energy frames (unvoiced/silence)
        if np.sqrt(np.mean(frame ** 2)) < 0.01:
            continue
            
        # Autocorrelation
        corr = signal.correlate(frame, frame, mode='full')
        corr = corr[len(corr)//2:]
        
        if len(corr) > max_lag:
            search_window = corr[min_lag:max_lag]
            if len(search_window) > 0:
                peak_idx = np.argmax(search_window) + min_lag
                # Peak threshold check
                if corr[peak_idx] > 0.3 * corr[0]:
                    f0 = sample_rate / peak_idx
                    f0_values.append(f0)

    return np.array(f0_values) if f0_values else np.array([])


def calculate_spectral_features(samples: np.ndarray, sample_rate: int) -> Dict[str, float]:
    """
    Computes spectral centroid, spectral rolloff, spectral flatness, and zero-crossing rate.
    """
    # FFT
    n_fft = 2048
    if len(samples) < n_fft:
        samples = np.pad(samples, (0, n_fft - len(samples)))
        
    freqs, times, spec = signal.spectrogram(samples, fs=sample_rate, nperseg=1024, noverlap=512)
    spec_mag = np.abs(spec)
    
    # Spectral Centroid (Center of mass of spectrum)
    freqs_col = freqs.reshape(-1, 1)
    centroid = np.sum(freqs_col * spec_mag, axis=0) / (np.sum(spec_mag, axis=0) + 1e-10)
    mean_centroid = float(np.mean(centroid))
    
    # Spectral Flatness (Wiener entropy: geometric mean / arithmetic mean)
    log_spec = np.log(spec_mag + 1e-10)
    geom_mean = np.exp(np.mean(log_spec, axis=0))
    arith_mean = np.mean(spec_mag, axis=0) + 1e-10
    flatness = float(np.mean(geom_mean / arith_mean))
    
    # Zero Crossing Rate
    zcr = np.mean(np.abs(np.diff(np.sign(samples)))) / 2.0
    
    return {
        "spectral_centroid": mean_centroid,
        "spectral_flatness": flatness,
        "zero_crossing_rate": float(zcr)
    }


def analyze_audio_forensics(file_bytes: bytes, filename: str = "audio.wav") -> Dict[str, Any]:
    """
    High-level forensic pipeline analyzing voice authenticity.
    """
    try:
        samples, sample_rate, duration = read_audio_data(file_bytes)
    except Exception as e:
        return {
            "valid": False,
            "error": f"Invalid audio format: {str(e)}",
            "classification": "INCONCLUSIVE",
            "evidence_strength": "Low",
            "audio_quality": "Poor",
            "signals": ["Unable to decode audio format"],
            "explanation": "The submitted audio could not be decoded. Please provide a standard WAV or MP3 recording."
        }

    is_valid, validation_msg = validate_audio(samples, sample_rate, duration)
    if not is_valid:
        return {
            "valid": False,
            "error": validation_msg,
            "classification": "INCONCLUSIVE",
            "evidence_strength": "Low",
            "audio_quality": "Poor",
            "signals": [validation_msg],
            "explanation": f"Analysis inconclusive: {validation_msg} Please try a clearer recording."
        }

    # Extract Pitch metrics
    f0_contour = estimate_pitch_contour(samples, sample_rate)
    f0_std = float(np.std(f0_contour)) if len(f0_contour) > 5 else 0.0
    f0_mean = float(np.mean(f0_contour)) if len(f0_contour) > 5 else 0.0

    # Extract Spectral features
    spectral = calculate_spectral_features(samples, sample_rate)
    
    # Signals detected
    detected_signals: List[str] = []
    suspicious_score = 0
    inconclusive_score = 0

    # 1. Pitch Variation (Natural speech has micro-inflections & pitch variations > 18 Hz)
    if len(f0_contour) < 5:
        detected_signals.append("Insufficient voiced pitch segments detected (high noise or unvoiced)")
        inconclusive_score += 4
    elif f0_std < 14.0:
        detected_signals.append("Reduced natural pitch variation (mechanical constancy)")
        suspicious_score += 3
    elif f0_std < 20.0:
        detected_signals.append("Constrained fundamental frequency trajectory")
        suspicious_score += 1
    else:
        detected_signals.append("Natural organic pitch variation observed")

    # 2. Spectral Flatness & Vocoder artifacts
    if spectral["spectral_flatness"] > 0.40 and inconclusive_score == 0:
        detected_signals.append("Elevated high-frequency spectral flatness (vocoder artifact)")
        suspicious_score += 2
    elif spectral["spectral_flatness"] < 0.08:
        detected_signals.append("Harmonic richness consistent with biological vocal tract")

    # 3. Spectral Centroid
    if spectral["spectral_centroid"] > 3200 and inconclusive_score == 0:
        detected_signals.append("Atypical high-frequency energy distribution")
        suspicious_score += 1

    # 4. Signal-to-Noise Ratio (SNR)
    rms = float(np.sqrt(np.mean(samples ** 2)))
    if rms < 0.02 or len(f0_contour) < 5:
        detected_signals.append("Low signal-to-noise ratio in recording")
        inconclusive_score += 3
        audio_quality = "Poor"
    elif rms > 0.08:
        audio_quality = "Good"
    else:
        audio_quality = "Standard"

    # Decision logic: Inconclusive check takes precedence if signal is degraded
    if inconclusive_score >= 3:
        classification = "INCONCLUSIVE"
        evidence_strength = "Low"
        explanation = "The recording exhibits low volume or high noise floor, preventing reliable authenticity determination. Try a clearer recording."
    elif suspicious_score >= 3:
        classification = "SUSPICIOUS"
        evidence_strength = "Moderate" if suspicious_score <= 4 else "Strong"
        explanation = "The recording contains characteristics that may be consistent with synthetic or manipulated speech, such as constrained pitch dynamics and vocoder spectral artifacts."
    else:
        classification = "LIKELY GENUINE"
        evidence_strength = "Moderate"
        explanation = "Acoustic characteristics display natural pitch inflections and spectral dynamics consistent with authentic human speech."

    return {
        "valid": True,
        "classification": classification,
        "evidence_strength": evidence_strength,
        "audio_quality": audio_quality,
        "duration_seconds": round(duration, 2),
        "f0_std": round(f0_std, 1),
        "f0_mean": round(f0_mean, 1),
        "spectral_centroid": round(spectral["spectral_centroid"], 1),
        "spectral_flatness": round(spectral["spectral_flatness"], 3),
        "signals": detected_signals,
        "explanation": explanation
    }
