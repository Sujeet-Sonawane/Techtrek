"""
NEXUS Audio Benchmark Generator
Generates valid acoustic WAV files with distinct forensic signatures for testing and judge evaluation.
"""

import os
import wave
import struct
import math
import numpy as np


def generate_wav(filepath: str, duration: float, generator_fn, sample_rate: int = 16000):
    os.makedirs(os.path.dirname(filepath), exist_ok=True)
    num_samples = int(duration * sample_rate)
    samples = []
    
    for i in range(num_samples):
        t = float(i) / sample_rate
        val = generator_fn(t, duration)
        # Clamp to [-1.0, 1.0]
        val = max(-1.0, min(1.0, val))
        samples.append(int(val * 32767.0))
        
    with wave.open(filepath, "wb") as wf:
        wf.setnchannels(1)
        wf.setsampwidth(2)
        wf.setframerate(sample_rate)
        packed = struct.pack(f"<{len(samples)}h", *samples)
        wf.writeframes(packed)
    print(f"Generated: {filepath} ({duration}s, {len(samples)} samples)")


# Sample A: Genuine Human Speech Characteristics
# Rich dynamic pitch (140Hz - 220Hz with natural micro-tremors and natural envelope decay)
def genuine_audio_fn(t: float, total_dur: float) -> float:
    # Pitch modulation: natural sentence prosody curve
    prosody = math.sin(2 * math.pi * 0.8 * t) * 35.0
    f0 = 160.0 + prosody + (math.sin(2 * math.pi * 5.2 * t) * 4.5)  # Natural human vocal tremor
    
    # Formants (F1 ~ 500Hz, F2 ~ 1500Hz, F3 ~ 2500Hz)
    h1 = math.sin(2 * math.pi * f0 * t) * 0.45
    h2 = math.sin(2 * math.pi * 2 * f0 * t) * 0.25
    h3 = math.sin(2 * math.pi * 3 * f0 * t) * 0.15
    f_band = math.sin(2 * math.pi * 800 * t) * 0.08
    
    # Syllabic amplitude envelope (speech rhythm)
    env = 0.5 + 0.4 * math.sin(2 * math.pi * 2.5 * t)
    return (h1 + h2 + h3 + f_band) * env * 0.7


# Sample B: Synthetic / Cloned Speech
# Strictly constant F0 (flat robotic pitch, no micro-inflection, vocoder-like spectral flatness)
def synthetic_audio_fn(t: float, total_dur: float) -> float:
    f0 = 155.0  # Rigorously flat F0
    # Overly rigid harmonics without natural vocal tract damping
    carrier = 0.35 * math.sin(2 * math.pi * f0 * t)
    carrier += 0.25 * math.sin(2 * math.pi * 2 * f0 * t)
    carrier += 0.20 * math.sin(2 * math.pi * 3 * f0 * t)
    carrier += 0.15 * math.sin(2 * math.pi * 4 * f0 * t)
    carrier += 0.10 * math.sin(2 * math.pi * 5 * f0 * t)
    # High-frequency vocoder buzz
    buzz = 0.08 * math.sin(2 * math.pi * 3800 * t)
    return (carrier + buzz) * 0.65


# Sample C: Inconclusive / Low Quality / Short & Noisy
def noisy_audio_fn(t: float, total_dur: float) -> float:
    # Weak intermittent signal drowned in white noise
    noise = (np.random.rand() * 2.0 - 1.0) * 0.12
    weak_speech = math.sin(2 * math.pi * 180 * t) * 0.04
    return weak_speech + noise


# Sample D: Financial Urgency Request
def financial_request_audio_fn(t: float, total_dur: float) -> float:
    # Agitated speech pattern with intense pitch peaks
    agitation = math.sin(2 * math.pi * 1.5 * t) * 45.0
    f0 = 190.0 + agitation
    s1 = math.sin(2 * math.pi * f0 * t) * 0.5
    s2 = math.sin(2 * math.pi * 2 * f0 * t) * 0.3
    env = max(0.0, math.sin(2 * math.pi * 3.0 * t))
    return (s1 + s2) * env * 0.75


def generate_all_samples(output_dir: str):
    os.makedirs(output_dir, exist_ok=True)
    generate_wav(os.path.join(output_dir, "Sample_A_Genuine.wav"), 3.5, genuine_audio_fn)
    generate_wav(os.path.join(output_dir, "Sample_B_Suspicious_Cloned.wav"), 3.5, synthetic_audio_fn)
    generate_wav(os.path.join(output_dir, "Sample_C_Inconclusive_Noisy.wav"), 1.2, noisy_audio_fn)
    generate_wav(os.path.join(output_dir, "Sample_D_Financial_Urgent_Transfer.wav"), 4.0, financial_request_audio_fn)


if __name__ == "__main__":
    import sys
    out = sys.argv[1] if len(sys.argv) > 1 else "samples"
    generate_all_samples(out)
