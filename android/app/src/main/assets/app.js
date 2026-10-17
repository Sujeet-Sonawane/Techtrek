/**
 * NEXUS Mobile Application Logic & Controller
 * Manages Screens, Audio Input, Live Call Demo, Timeline, and Trust Report.
 */

const state = {
  currentScreen: 'home',
  activeTab: 'samples',
  selectedSampleId: 'sample_a',
  selectedFile: null,
  selectedAudioBuffer: null,
  currentTranscript: '',
  backendUrl: 'http://10.0.2.2:8000', // Default Android emulator host to localhost
  isBackendOnline: false,
  isAnalyzing: false,
  
  // Live Call Simulation State
  isCallActive: false,
  callTimerSec: 0,
  callTimerInterval: null,
  waveAnimId: null,
  liveTimeline: [
    { time: '00:00', risk: 'LOW', label: '🟢 LOW', desc: 'Call initiated. Natural vocal harmonics observed.' },
    { time: '00:10', risk: 'LOW', label: '🟢 LOW', desc: 'Natural pitch inflections. Discussion on quarterly roadmap.' },
    { time: '00:20', risk: 'MEDIUM', label: '🟡 MEDIUM', desc: 'Acoustic transition detected: constrained pitch dynamics.' },
    { time: '00:30', risk: 'HIGH', label: '🔴 HIGH', desc: 'Vocoder spectral artifacts + Sensitive financial request detected ("Transfer ₹50,000 immediately").' },
    { time: '00:40', risk: 'HIGH', label: '🔴 HIGH', desc: 'High-urgency transaction demand flagged.' }
  ]
};

const samplesData = {
  sample_a: {
    id: 'sample_a',
    title: 'Sample A — Genuine Speech',
    desc: 'Clear natural vocal dynamics with human pitch micro-tremors.',
    path: 'samples/Sample_A_Genuine.wav',
    duration: '00:03',
    transcript: "Hello, this is Rahul. Just following up on the quarterly roadmap review we discussed in yesterday's meeting.",
    badge: 'Likely Genuine',
    badgeClass: 'badge-green'
  },
  sample_b: {
    id: 'sample_b',
    title: 'Sample B — Suspicious (Cloned Speech)',
    desc: 'Synthetic speech exhibiting mechanical pitch constancy and vocoder spectral artifacts.',
    path: 'samples/Sample_B_Suspicious_Cloned.wav',
    duration: '00:03',
    transcript: "Hello, this is Rahul. I am currently out of office. Please process the pending system invoice today.",
    badge: 'Suspicious',
    badgeClass: 'badge-red'
  },
  sample_c: {
    id: 'sample_c',
    title: 'Sample C — Inconclusive (High Noise)',
    desc: 'Short, low-SNR recording with unresolvable pitch tracks.',
    path: 'samples/Sample_C_Inconclusive_Noisy.wav',
    duration: '00:01',
    transcript: "Hello... can you hear me... line is breaking up...",
    badge: 'Inconclusive',
    badgeClass: 'badge-amber'
  },
  sample_d: {
    id: 'sample_d',
    title: 'Sample D — Urgent Financial Request',
    desc: 'Voice call requesting an immediate ₹50,000 transfer to an unverified UPI account.',
    path: 'samples/Sample_D_Financial_Urgent_Transfer.wav',
    duration: '00:04',
    transcript: "Rahul here! I am stuck in an emergency meeting. Transfer ₹50,000 immediately to the UPI ID sent via SMS right now!",
    badge: 'Financial Alert',
    badgeClass: 'badge-red'
  }
};

document.addEventListener('DOMContentLoaded', () => {
  initApp();
});

function initApp() {
  renderSampleCards();
  checkBackendHealth();
  setupEventListeners();
  selectSample('sample_a');
}

function navigateTo(screenId) {
  document.querySelectorAll('.screen').forEach(s => s.classList.remove('active'));
  const target = document.getElementById(`screen-${screenId}`);
  if (target) {
    target.classList.add('active');
    state.currentScreen = screenId;
    window.scrollTo(0, 0);
  }
}

function checkBackendHealth() {
  const pill = document.getElementById('engine-status-pill');
  const pillText = document.getElementById('engine-status-text');

  fetch(`${state.backendUrl}/health`, { method: 'GET' })
    .then(res => res.json())
    .then(data => {
      state.isBackendOnline = true;
      pill.classList.add('online');
      pillText.textContent = 'API Connected';
    })
    .catch(() => {
      state.isBackendOnline = false;
      pill.classList.remove('online');
      pillText.textContent = 'Offline Engine';
    });
}

function renderSampleCards() {
  const container = document.getElementById('samples-list-container');
  if (!container) return;
  container.innerHTML = '';

  Object.values(samplesData).forEach(sample => {
    const card = document.createElement('div');
    card.className = `sample-card ${state.selectedSampleId === sample.id ? 'selected' : ''}`;
    card.id = `card-${sample.id}`;
    card.onclick = () => selectSample(sample.id);

    card.innerHTML = `
      <div>
        <h4>${sample.title}</h4>
        <p>${sample.desc}</p>
      </div>
      <span class="badge ${sample.badgeClass}">${sample.badge}</span>
    `;
    container.appendChild(card);
  });
}

function selectSample(sampleId) {
  state.selectedSampleId = sampleId;
  const sample = samplesData[sampleId];
  if (!sample) return;

  document.querySelectorAll('.sample-card').forEach(c => c.classList.remove('selected'));
  const el = document.getElementById(`card-${sampleId}`);
  if (el) el.classList.add('selected');

  document.getElementById('selected-file-name').textContent = sample.path.split('/').pop();
  document.getElementById('selected-file-details').textContent = `${sample.duration} • WAV • ${sample.badge}`;
  document.getElementById('transcript-input').value = sample.transcript;
  state.currentTranscript = sample.transcript;

  // Load sample into audio buffer
  fetch(sample.path)
    .then(r => r.arrayBuffer())
    .then(ab => NexusAudioEngine.decodeAudio(ab))
    .then(buf => {
      state.selectedAudioBuffer = buf;
    })
    .catch(err => console.log('Sample load error:', err));
}

function switchTab(tabId) {
  state.activeTab = tabId;
  document.querySelectorAll('.tab-btn').forEach(btn => btn.classList.remove('active'));
  document.querySelectorAll('.tab-content').forEach(tc => tc.classList.remove('active'));

  const btn = document.getElementById(`tab-btn-${tabId}`);
  const content = document.getElementById(`tab-content-${tabId}`);
  if (btn) btn.classList.add('active');
  if (content) content.classList.add('active');
}

function handleFileInput(input) {
  if (input.files && input.files[0]) {
    const file = input.files[0];
    state.selectedFile = file;
    state.selectedSampleId = null;

    document.getElementById('selected-file-name').textContent = file.name;
    document.getElementById('selected-file-details').textContent = `${(file.size / 1024).toFixed(1)} KB • Audio File`;

    const reader = new FileReader();
    reader.onload = async (e) => {
      try {
        state.selectedAudioBuffer = await NexusAudioEngine.decodeAudio(e.target.result);
      } catch (err) {
        alert('Could not decode audio. Please ensure it is a valid WAV or MP3 file.');
      }
    };
    reader.readAsArrayBuffer(file);
  }
}

let mediaRecorder = null;
let audioChunks = [];
let isRecording = false;

async function toggleRecording() {
  const recordBtn = document.getElementById('record-btn-icon');
  const recordLabel = document.getElementById('record-status-label');

  if (!isRecording) {
    try {
      const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
      mediaRecorder = new MediaRecorder(stream);
      audioChunks = [];

      mediaRecorder.ondataavailable = e => {
        if (e.data.size > 0) audioChunks.push(e.data);
      };

      mediaRecorder.onstop = async () => {
        const audioBlob = new Blob(audioChunks, { type: 'audio/wav' });
        const arrayBuf = await audioBlob.arrayBuffer();
        state.selectedAudioBuffer = await NexusAudioEngine.decodeAudio(arrayBuf);
        state.selectedFile = audioBlob;
        state.selectedSampleId = null;

        document.getElementById('selected-file-name').textContent = 'live_recording.wav';
        document.getElementById('selected-file-details').textContent = `${state.selectedAudioBuffer.duration.toFixed(1)}s • Live Mic Audio`;
      };

      mediaRecorder.start();
      isRecording = true;
      recordBtn.classList.add('recording');
      recordLabel.textContent = 'Recording speech... Tap to finish';
    } catch (e) {
      alert('Microphone permission required for live recording.');
    }
  } else {
    mediaRecorder.stop();
    isRecording = false;
    recordBtn.classList.remove('recording');
    recordLabel.textContent = 'Recording saved. Ready for analysis.';
  }
}

async function startAnalysis() {
  const btn = document.getElementById('btn-analyze-voice');
  btn.innerHTML = `<span class="spinner"></span> Analyzing Voice Signals...`;
  btn.disabled = true;

  const transcript = document.getElementById('transcript-input').value;

  try {
    let result = null;

    // Check if backend is available
    if (state.isBackendOnline && state.selectedSampleId) {
      try {
        const formData = new FormData();
        formData.append('sample_id', state.selectedSampleId);
        formData.append('transcript', transcript);

        const response = await fetch(`${state.backendUrl}/analyze`, {
          method: 'POST',
          body: formData
        });
        if (response.ok) {
          result = await response.json();
        }
      } catch (e) {
        console.log('Backend request failed, falling back to local engine');
      }
    }

    // Local offline engine fallback
    if (!result) {
      if (!state.selectedAudioBuffer) {
        // Fallback fetch if not decoded
        const sample = samplesData[state.selectedSampleId || 'sample_a'];
        const r = await fetch(sample.path);
        const ab = await r.arrayBuffer();
        state.selectedAudioBuffer = await NexusAudioEngine.decodeAudio(ab);
      }
      result = await NexusAudioEngine.analyzeAudioLocally(state.selectedAudioBuffer, transcript);
    }

    displayResults(result);
    navigateTo('result');
  } catch (err) {
    alert('Analysis error: ' + err.message);
  } finally {
    btn.innerHTML = `Analyze Voice`;
    btn.disabled = false;
  }
}

function displayResults(data) {
  const badgeCard = document.getElementById('result-badge-card');
  const catLabel = document.getElementById('result-cat-label');
  const statusMain = document.getElementById('result-status-text');
  const riskChip = document.getElementById('result-risk-chip');
  const evidenceChip = document.getElementById('result-evidence-val');
  const qualityChip = document.getElementById('result-quality-val');
  const signalsList = document.getElementById('result-signals-list');
  const financialBox = document.getElementById('result-financial-box');
  const financialDesc = document.getElementById('result-financial-desc');
  const explanationEl = document.getElementById('result-explanation-text');
  const actionBanner = document.getElementById('result-action-banner');
  const actionText = document.getElementById('result-action-text');

  // Classification styling
  badgeCard.className = 'result-header-badge ' + data.classification.toLowerCase().replace(' ', '-');
  statusMain.textContent = data.classification;

  // Risk styling
  riskChip.className = 'risk-level-strip risk-' + data.risk_level.toLowerCase();
  riskChip.textContent = `RISK LEVEL: ${data.risk_level}`;

  evidenceChip.textContent = data.evidence_strength;
  qualityChip.textContent = data.audio_quality;

  // Signals
  signalsList.innerHTML = '';
  data.signals.forEach(sig => {
    const li = document.createElement('li');
    li.textContent = sig;
    signalsList.appendChild(li);
  });

  // Financial context
  if (data.financial_context) {
    financialBox.style.display = 'flex';
    financialBox.className = 'financial-alert-box' + (data.risk_level === 'HIGH' ? ' danger' : '');
    financialDesc.textContent = `Sensitive financial request detected (${data.financial_triggers ? data.financial_triggers.join(', ') : 'Monetary Action'}). Additional verification recommended.`;
  } else {
    financialBox.style.display = 'none';
  }

  // Explanation
  explanationEl.textContent = data.explanation;

  // Recommended Action
  actionText.textContent = data.recommendation;

  // Technical details
  document.getElementById('tech-f0').textContent = data.f0_std ? `${data.f0_std} Hz std` : 'N/A';
  document.getElementById('tech-spectral').textContent = data.spectral_centroid ? `${data.spectral_centroid} Hz` : 'N/A';
  document.getElementById('tech-duration').textContent = data.duration_seconds ? `${data.duration_seconds}s` : 'N/A';
  document.getElementById('tech-disclaimer').textContent = data.disclaimer || 'Risk level is based on acoustic bio-markers and financial safety rules.';
}

/* ==========================================================================
   Live Call Simulation Controller
   ========================================================================== */
function startLiveCallDemo() {
  navigateTo('live-call');
  state.isCallActive = true;
  state.callTimerSec = 0;

  // Reset UI
  document.getElementById('live-call-timer').textContent = '00:00';
  document.getElementById('live-auth-status').textContent = 'LIKELY GENUINE';
  document.getElementById('live-auth-status').style.color = 'var(--semantic-green)';
  document.getElementById('live-risk-status').textContent = 'LOW';
  document.getElementById('live-risk-status').style.color = 'var(--semantic-green)';

  document.getElementById('live-voice-alert').classList.remove('visible');
  document.getElementById('live-financial-alert').classList.remove('visible');

  renderLiveTimeline();
  startWaveformAnimation();

  // Timer loop
  clearInterval(state.callTimerInterval);
  state.callTimerInterval = setInterval(() => {
    state.callTimerSec++;
    const mins = String(Math.floor(state.callTimerSec / 60)).padStart(2, '0');
    const secs = String(state.callTimerSec % 60).padStart(2, '0');
    document.getElementById('live-call-timer').textContent = `${mins}:${secs}`;

    // Timeline event triggers
    if (state.callTimerSec === 10) {
      updateTimelineActive(1);
    } else if (state.callTimerSec === 20) {
      updateTimelineActive(2);
      document.getElementById('live-auth-status').textContent = 'INCONCLUSIVE';
      document.getElementById('live-auth-status').style.color = 'var(--semantic-amber)';
      document.getElementById('live-risk-status').textContent = 'MEDIUM';
      document.getElementById('live-risk-status').style.color = 'var(--semantic-amber)';
      document.getElementById('live-voice-alert').classList.add('visible');
    } else if (state.callTimerSec === 30) {
      updateTimelineActive(3);
      document.getElementById('live-auth-status').textContent = 'SUSPICIOUS';
      document.getElementById('live-auth-status').style.color = 'var(--semantic-red)';
      document.getElementById('live-risk-status').textContent = 'HIGH';
      document.getElementById('live-risk-status').style.color = 'var(--semantic-red)';
      document.getElementById('live-financial-alert').classList.add('visible');
    } else if (state.callTimerSec === 40) {
      updateTimelineActive(4);
    }
  }, 1000);
}

function updateTimelineActive(index) {
  document.querySelectorAll('.timeline-segment').forEach((el, idx) => {
    if (idx === index) el.classList.add('active');
    else el.classList.remove('active');
  });
}

function renderLiveTimeline() {
  const container = document.getElementById('live-timeline-strip');
  container.innerHTML = '';

  state.liveTimeline.forEach((seg, idx) => {
    const el = document.createElement('div');
    el.className = `timeline-segment ${idx === 0 ? 'active' : ''}`;
    el.onclick = () => showTimelineDetail(seg);
    el.innerHTML = `
      <span class="timeline-time">${seg.time}</span>
      <span class="timeline-badge">${seg.label}</span>
    `;
    container.appendChild(el);
  });
}

function showTimelineDetail(seg) {
  alert(`[${seg.time}] Risk Assessment: ${seg.risk}\n\n${seg.desc}`);
}

function startWaveformAnimation() {
  const canvas = document.getElementById('waveform-canvas');
  if (!canvas) return;
  const ctx = canvas.getContext('2d');
  canvas.width = canvas.offsetWidth;
  canvas.height = 60;

  let step = 0;

  function draw() {
    if (!state.isCallActive) return;
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    ctx.fillStyle = state.callTimerSec >= 30 ? '#EF4444' : (state.callTimerSec >= 20 ? '#F59E0B' : '#3B82F6');
    const bars = 36;
    const barWidth = 4;
    const spacing = (canvas.width - bars * barWidth) / (bars - 1);

    for (let i = 0; i < bars; i++) {
      const height = Math.abs(Math.sin(step * 0.1 + i * 0.4)) * 40 + 6;
      const x = i * (barWidth + spacing);
      const y = (canvas.height - height) / 2;
      ctx.fillRect(x, y, barWidth, height);
    }

    step++;
    state.waveAnimId = requestAnimationFrame(draw);
  }

  draw();
}

function endLiveCall() {
  state.isCallActive = false;
  clearInterval(state.callTimerInterval);
  if (state.waveAnimId) cancelAnimationFrame(state.waveAnimId);

  // Generate Post-Call Trust Report
  navigateTo('report');
}

function openChecklistModal() {
  document.getElementById('checklist-modal').classList.add('active');
}

function closeChecklistModal() {
  document.getElementById('checklist-modal').classList.remove('active');
}

function openChallengeModal() {
  const code = Math.floor(1000 + Math.random() * 9000);
  document.getElementById('challenge-code-text').textContent = code;
  document.getElementById('challenge-modal').classList.add('active');
}

function closeChallengeModal() {
  document.getElementById('challenge-modal').classList.remove('active');
}

function openSettingsModal() {
  document.getElementById('settings-input-url').value = state.backendUrl;
  document.getElementById('settings-modal').classList.add('active');
}

function closeSettingsModal() {
  document.getElementById('settings-modal').classList.remove('active');
}

function saveSettings() {
  state.backendUrl = document.getElementById('settings-input-url').value;
  checkBackendHealth();
  closeSettingsModal();
}

function setupEventListeners() {
  // Navigation
  document.getElementById('btn-nav-analyze')?.addEventListener('click', () => navigateTo('audio-input'));
  document.getElementById('btn-nav-live')?.addEventListener('click', startLiveCallDemo);
  document.getElementById('btn-back-input')?.addEventListener('click', () => navigateTo('home'));
  document.getElementById('btn-back-result')?.addEventListener('click', () => navigateTo('audio-input'));
  document.getElementById('btn-end-call')?.addEventListener('click', endLiveCall);
  document.getElementById('btn-report-home')?.addEventListener('click', () => navigateTo('home'));
  document.getElementById('btn-report-checklist')?.addEventListener('click', openChecklistModal);
  document.getElementById('btn-result-checklist')?.addEventListener('click', openChecklistModal);
  document.getElementById('btn-verify-challenge')?.addEventListener('click', openChallengeModal);

  // Audio actions
  document.getElementById('btn-analyze-voice')?.addEventListener('click', startAnalysis);
  document.getElementById('record-btn-icon')?.addEventListener('click', toggleRecording);

  // Tech details accordion
  document.getElementById('btn-toggle-tech')?.addEventListener('click', () => {
    const details = document.getElementById('tech-details-box');
    details.style.display = details.style.display === 'none' ? 'block' : 'none';
  });
}
