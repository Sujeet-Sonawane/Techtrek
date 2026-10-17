package com.nexus.app.data.model

object DemoScenarios {

    val scenarios: List<InvestigationReport> = listOf(
        // Scenario 1: Deepfake PM Video + Fake Scheme (High Forensic Suspicion + Debunked Claim)
        InvestigationReport(
            id = "demo_pm_fake_scheme",
            title = "Viral Video: PM Announcing 'Diwali Dhan Labh ₹50,000' Grant",
            mediaMetadata = MediaMetadata(
                fileName = "pm_announcement_dhanlabh_viral.mp4",
                format = "MP4",
                isVideo = true,
                durationSeconds = 48.0,
                fileSizeBytes = 12_400_000,
                resolution = "1080x1920 (Vertical Reel)",
                sampleRateHz = 44100,
                channels = 2,
                bitRateKbps = 2100,
                codec = "H.264 / AAC"
            ),
            forensicAnalysis = ForensicAnalysis(
                verdict = ForensicVerdict.SUSPICIOUS_INDICATORS_DETECTED,
                confidence = EvidenceConfidence.STRONG,
                headline = "Acoustic and Lip-Sync Anomalies Consistent with Deepfake Synthesis",
                summary = "Spectral analysis detected artificial pitch flattening characteristic of voice cloning models. Lip sync timing deviates by +140ms from phonetic syllables at 00:14.",
                audioAuthenticity = AudioAuthenticity(
                    status = "Synthetic Speech Signals Detected",
                    confidence = EvidenceConfidence.STRONG,
                    pitchVarianceHz = 12.4f,
                    spectralCentroidHz = 1840f,
                    jitterPercent = 0.08f,
                    syntheticLikelihood = "High",
                    technicalNotes = "Vocal jitter is unnaturally low (<0.1%), indicating statistical acoustic generation rather than human biomechanical vocal tract vibration."
                ),
                audioIntegrity = AudioIntegrity(
                    clippingDetected = false,
                    suddenTransitionsCount = 3,
                    snrDb = 28.5f,
                    anomaliesFound = listOf(
                        "Sudden background ambient noise drop between 00:12 and 00:15",
                        "Phonetic transition discontinuity during Hindi vowel elongation"
                    )
                ),
                videoSync = VideoSync(
                    avOffsetMs = 140,
                    lipSyncAlignment = "Offset Detected",
                    visualArtifactsDetected = true,
                    visualNotes = "Mouth interior textures show blur and warping inconsistent with facial skin texture."
                ),
                timelineFindings = listOf(
                    TimestampedFinding(
                        timestampSeconds = 6.2f,
                        formattedTime = "00:06",
                        findingType = "Synthetic Pitch Glitch",
                        description = "Unnatural fundamental frequency jump during speech onset.",
                        severity = "Moderate",
                        possibleCauses = "Neural TTS vocoder artifact or sample rate interpolation."
                    ),
                    TimestampedFinding(
                        timestampSeconds = 14.5f,
                        formattedTime = "00:14",
                        findingType = "Lip Sync Asynchrony",
                        description = "Mouth closed while plosive consonants /b/ and /p/ are heard in audio.",
                        severity = "High",
                        possibleCauses = "Facial reenactment / video dubbing desynchronization."
                    ),
                    TimestampedFinding(
                        timestampSeconds = 32.0f,
                        formattedTime = "00:32",
                        findingType = "Ambient Audio Cut",
                        description = "Abrupt drop in room reverberation preceding website URL recitation.",
                        severity = "Moderate",
                        possibleCauses = "Audio track spliced from two distinct recording sessions."
                    )
                ),
                technicalProvenance = mapOf(
                    "Container" to "MPEG-4 Base Media v1",
                    "Encoder" to "Lavf59.27.100 (FFmpeg modified stream)",
                    "Audio Bitrate" to "128 kbps AAC",
                    "Metadata Tampering" to "Original camera EXIF missing; synthesized audio track multiplexed"
                ),
                explainabilityFactors = listOf(
                    ForensicExplainabilityFactor(
                        factorName = "Vocal Cord Micro-Tremor (Jitter)",
                        observedValue = "0.08% (Unnaturally Low)",
                        expectedNormalRange = "0.40% - 1.20%",
                        isSuspicious = true,
                        impactWeight = "+40% Suspicion",
                        diagnosticSummary = "Mathematical neural vocoder flattening eliminates involuntary human laryngeal tremor.",
                        technicalRationale = "Generated using ElevenLabs voice cloning pipeline with statistical variance smoothing."
                    ),
                    ForensicExplainabilityFactor(
                        factorName = "A/V Viseme Desynchronization",
                        observedValue = "+140 ms Offset",
                        expectedNormalRange = "< 45 ms Synchronized",
                        isSuspicious = true,
                        impactWeight = "+35% Suspicion",
                        diagnosticSummary = "Lip boundary motion lags audio consonant syllables /b/ and /p/.",
                        technicalRationale = "Wav2Lip face reenactment desynchronization during rapid speech."
                    ),
                    ForensicExplainabilityFactor(
                        factorName = "Audio Room Reverberation Splicing",
                        observedValue = "Phase Shift at 00:32",
                        expectedNormalRange = "Continuous Acoustic Phase",
                        isSuspicious = true,
                        impactWeight = "+25% Splicing",
                        diagnosticSummary = "Sudden drop in background noise floor immediately before URL mention.",
                        technicalRationale = "Two distinct audio clips spliced together."
                    )
                ),
                limitationsNote = "Audio and video analysis identifies technical anomalies; findings are probabilistic indicators rather than judicial proof."
            ),
            truthCheckResult = TruthCheckResult(
                verdict = TruthVerdict.OFFICIALLY_FACT_CHECKED_FALSE,
                confidence = EvidenceConfidence.STRONG,
                headline = "Officially Debunked as Fraudulent by PIB Fact Check Unit",
                summary = "No scheme titled 'PM Diwali Dhan Labh Yojana' exists in government gazettes. PIB Fact Check published an official debunk alert regarding this exact viral video.",
                evaluatedClaims = listOf(
                    SpokenClaim(
                        id = "c1",
                        claimText = "Every Indian family will receive ₹50,000 directly into their bank account before Diwali.",
                        timestampRange = "00:04 - 00:18",
                        category = ClaimCategory.FINANCIAL_BENEFIT,
                        entities = ClaimEntities(
                            schemeName = "PM Diwali Dhan Labh Yojana",
                            claimedAmount = "₹50,000",
                            eligibility = "All Indian families"
                        )
                    ),
                    SpokenClaim(
                        id = "c2",
                        claimText = "Register immediately on pm-dhanlabh-scheme.in and pay ₹499 registration fee.",
                        timestampRange = "00:28 - 00:44",
                        category = ClaimCategory.PAYMENT_REQUEST,
                        entities = ClaimEntities(
                            deadline = "Before Diwali midnight",
                            targetUrl = "pm-dhanlabh-scheme.in",
                            feeOrPayment = "₹499 registration fee"
                        )
                    )
                ),
                officialSourcesChecked = listOf(
                    OfficialSource(
                        id = "s1",
                        authorityName = "PIB Fact Check Unit (Govt of India)",
                        officialUrl = "https://factcheck.pib.gov.in",
                        publicationDate = "14 October 2026",
                        sourceType = SourceType.PIB_FACT_CHECK,
                        matchVerdict = MatchVerdict.OFFICIALLY_DEBUNKED,
                        quoteOrFact = "A viral video falsely claiming that PM Narendra Modi has launched 'PM Dhan Labh Yojana' offering ₹50,000 is FAKE. No such scheme has been approved by the Government.",
                        officialGuidelines = "Official Government of India portals always use .gov.in or .nic.in domains. Never pay any fee for central government welfare benefits."
                    ),
                    OfficialSource(
                        id = "s2",
                        authorityName = "myScheme National Welfare Portal",
                        officialUrl = "https://myscheme.gov.in",
                        publicationDate = "October 2026",
                        sourceType = SourceType.MYSCHEME_PORTAL,
                        matchVerdict = MatchVerdict.UNVERIFIED_NOT_FOUND,
                        quoteOrFact = "Zero matching schemes found for keyword 'Dhan Labh' across 1,400+ Central and State welfare programs.",
                        officialGuidelines = "Citizens should verify all Central schemes directly on myscheme.gov.in."
                    )
                ),
                verifiedSchemeFacts = mapOf(
                    "Official Existence" to "Does NOT exist (FRAUDULENT)",
                    "Legitimate Domain" to "None (Reported phishing portal)",
                    "Registration Fee Policy" to "Govt welfare schemes are strictly FREE of registration charge",
                    "Official Helpline" to "National Cyber Crime Helpline: 1930"
                ),
                limitationsNote = "Fact check cross-references real-time PIB and myScheme databases. Private phishing domains are flagged using National Cybercrime registry.",
                recommendedActions = listOf(
                    "DO NOT click or open 'pm-dhanlabh-scheme.in' — it is a known credential phishing site.",
                    "DO NOT transfer ₹499 or any amount via UPI/QR code.",
                    "Report this fraudulent video and UPI handle to the National Cybercrime Reporting Portal at cybercrime.gov.in or call 1930.",
                    "Warn family and social media groups: the video is an AI-generated deepfake."
                )
            ),
            transcript = "Namaskar mere pyare bhaiyo aur behno. Is Diwali par sabhi parivaron ke khate mein seedhe pachaas hazaar rupaye bheje ja rahe hain. turant website pm-dhanlabh-scheme.in par jaakar chaar sau ninyanwe rupaye ka registration karein aur labh paayein.",
            riskLevel = "CRITICAL RISK",
            primaryWarning = "CRITICAL FRAUD: Fabricated AI deepfake video promoting a financial phishing scam.",
            actionSteps = listOf(
                "Do not interact with the linked website or make payments.",
                "Verify authentic schemes exclusively at myscheme.gov.in.",
                "Report suspicious messages to 1930."
            )
        ),

        // Scenario 2: Authentic Official Video + Authentic Claim (Authentic Forensics + Supported Claim)
        InvestigationReport(
            id = "demo_pmkisan_real",
            title = "Official Release: PM-KISAN 17th Installment Direct Benefit Transfer",
            mediaMetadata = MediaMetadata(
                fileName = "pmkisan_17th_installment_official.mp4",
                format = "MP4",
                isVideo = true,
                durationSeconds = 62.0,
                fileSizeBytes = 28_900_000,
                resolution = "1920x1080 (Landscape HD)",
                sampleRateHz = 48000,
                channels = 2,
                bitRateKbps = 3800,
                codec = "H.264 / AAC"
            ),
            forensicAnalysis = ForensicAnalysis(
                verdict = ForensicVerdict.NO_SUSPICIOUS_INDICATORS_DETECTED,
                confidence = EvidenceConfidence.STRONG,
                headline = "Natural Biomechanical Speech and Consistent Acoustic Provenance",
                summary = "Spectral distribution displays normal human harmonic patterns, organic vocal jitter (0.84%), and natural room reverberation without splicing.",
                audioAuthenticity = AudioAuthenticity(
                    status = "Acoustic Signals Natural & Consistent",
                    confidence = EvidenceConfidence.STRONG,
                    pitchVarianceHz = 48.2f,
                    spectralCentroidHz = 1420f,
                    jitterPercent = 0.84f,
                    syntheticLikelihood = "Low",
                    technicalNotes = "Vocal cord biomechanics, micro-tremors, and respiratory pauses are consistent with live unmanipulated human speech."
                ),
                audioIntegrity = AudioIntegrity(
                    clippingDetected = false,
                    suddenTransitionsCount = 0,
                    snrDb = 34.2f,
                    anomaliesFound = emptyList()
                ),
                videoSync = VideoSync(
                    avOffsetMs = 8,
                    lipSyncAlignment = "Synchronized",
                    visualArtifactsDetected = false,
                    visualNotes = "Facial lighting, natural eye blink frequency, and dental boundary movements match spoken phonemes."
                ),
                timelineFindings = emptyList(),
                technicalProvenance = mapOf(
                    "Container" to "MP4 ISO/IEC 14496-14",
                    "Encoder" to "Sony Professional Broadcast Camcorder",
                    "Audio Channels" to "Stereo 48.0 kHz 24-bit PCM/AAC",
                    "Metadata Integrity" to "Continuous SMPTE timecode verified"
                ),
                limitationsNote = "Absence of detected anomalies does not guarantee unrecorded contextual edits, but indicates high acoustic integrity."
            ),
            truthCheckResult = TruthCheckResult(
                verdict = TruthVerdict.SUPPORTED_BY_OFFICIAL_SOURCES,
                confidence = EvidenceConfidence.STRONG,
                headline = "Fully Supported by Ministry of Agriculture & PIB Notifications",
                summary = "The PM-KISAN 17th installment release of ₹2,000 to eligible farmer accounts is verified across official gazette and DBT portals.",
                evaluatedClaims = listOf(
                    SpokenClaim(
                        id = "c1",
                        claimText = "Over 9.26 crore farmers are receiving the 17th installment of ₹2,000 through Direct Benefit Transfer.",
                        timestampRange = "00:08 - 00:26",
                        category = ClaimCategory.FINANCIAL_BENEFIT,
                        entities = ClaimEntities(
                            schemeName = "PM-KISAN (Pradhan Mantri Kisan Samman Nidhi)",
                            claimedAmount = "₹2,000 per eligible farmer",
                            eligibility = "Small and marginal landholder farmer families with active e-KYC"
                        )
                    ),
                    SpokenClaim(
                        id = "c2",
                        claimText = "Beneficiaries can verify their payment status on pmkisan.gov.in.",
                        timestampRange = "00:34 - 00:52",
                        category = ClaimCategory.APPLICATION_URL,
                        entities = ClaimEntities(
                            targetUrl = "pmkisan.gov.in",
                            feeOrPayment = "Free of cost (No fee required)"
                        )
                    )
                ),
                officialSourcesChecked = listOf(
                    OfficialSource(
                        id = "s1",
                        authorityName = "Ministry of Agriculture & Farmers Welfare",
                        officialUrl = "https://pmkisan.gov.in",
                        publicationDate = "June 2024 / Updated 2026",
                        sourceType = SourceType.MINISTRY_PORTAL,
                        matchVerdict = MatchVerdict.CORROBORATED,
                        quoteOrFact = "17th installment of PM-KISAN successfully released to 9.26+ crore farmers totaling over ₹20,000 crores via DBT.",
                        officialGuidelines = "Farmers must ensure e-KYC is completed and bank account is linked to Aadhaar. No middleman or agent fee is charged."
                    ),
                    OfficialSource(
                        id = "s2",
                        authorityName = "PIB Delhi (Press Information Bureau)",
                        officialUrl = "https://pib.gov.in/PressReleasePage.aspx?PRID=2026117",
                        publicationDate = "June 2024",
                        sourceType = SourceType.PIB_FACT_CHECK,
                        matchVerdict = MatchVerdict.CORROBORATED,
                        quoteOrFact = "Official press release verifies PM-KISAN installment transfer directly to beneficiary Aadhaar-seeded accounts.",
                        officialGuidelines = "Check status only on official portal pmkisan.gov.in or PM-KISAN mobile app."
                    )
                ),
                verifiedSchemeFacts = mapOf(
                    "Scheme Name" to "PM-KISAN Samman Nidhi",
                    "Official Portal" to "pmkisan.gov.in",
                    "Official Benefit" to "₹6,000 per year (in 3 equal installments of ₹2,000)",
                    "e-KYC Requirement" to "Mandatory via OTP or Biometric at CSC"
                ),
                limitationsNote = "Verification confirms the official scheme details. Individual eligibility depends on landholding records and e-KYC status.",
                recommendedActions = listOf(
                    "You may safely check your beneficiary status directly at pmkisan.gov.in.",
                    "Ensure your Aadhaar is linked with your bank account for DBT.",
                    "Never share OTP or banking passwords with anyone claiming to help with PM-KISAN."
                )
            ),
            transcript = "Kisan bhaiyo aur behno, aaj PM-KISAN samman nidhi ki satrahvi kist seedhe aapke bank khate mein transfor ki jaa rahi hai. Kripya pmkisan.gov.in par jakar apna status check karein.",
            riskLevel = "SAFE / VERIFIED",
            primaryWarning = "AUTHENTIC & VERIFIED: Both video authenticity and official scheme details are fully verified.",
            actionSteps = listOf(
                "Access services directly at official portal pmkisan.gov.in.",
                "Ensure e-KYC is updated."
            )
        ),

        // Scenario 3: Authentic Video + Outdated/Misleading Claim (Old 2020 speech shared as 2026)
        InvestigationReport(
            id = "demo_recycled_speech",
            title = "Recycled Video: Old Pandemic Relief Speech Circulated as New 2026 Cash Grant",
            mediaMetadata = MediaMetadata(
                fileName = "pm_relief_package_viral_repost.mp4",
                format = "MP4",
                isVideo = true,
                durationSeconds = 55.0,
                fileSizeBytes = 18_200_000,
                resolution = "1280x720",
                sampleRateHz = 44100,
                channels = 2,
                bitRateKbps = 2400,
                codec = "H.264 / AAC"
            ),
            forensicAnalysis = ForensicAnalysis(
                verdict = ForensicVerdict.NO_SUSPICIOUS_INDICATORS_DETECTED,
                confidence = EvidenceConfidence.STRONG,
                headline = "Original Unmanipulated Media (Archival Broadcast Recording)",
                summary = "Media analysis confirms the recording is an authentic historical broadcast. No speech synthesis, voice cloning, or visual face warping was detected.",
                audioAuthenticity = AudioAuthenticity(
                    status = "Authentic Speech Signals",
                    confidence = EvidenceConfidence.STRONG,
                    pitchVarianceHz = 42.1f,
                    spectralCentroidHz = 1510f,
                    jitterPercent = 0.78f,
                    syntheticLikelihood = "Low",
                    technicalNotes = "Natural acoustic harmonics and broadcast microphone response."
                ),
                audioIntegrity = AudioIntegrity(
                    clippingDetected = false,
                    suddenTransitionsCount = 0,
                    snrDb = 32.0f,
                    anomaliesFound = emptyList()
                ),
                videoSync = VideoSync(
                    avOffsetMs = 0,
                    lipSyncAlignment = "Synchronized",
                    visualArtifactsDetected = false,
                    visualNotes = "Original Doordarshan broadcast watermark and synchronized lip movements."
                ),
                timelineFindings = emptyList(),
                technicalProvenance = mapOf(
                    "Source" to "Doordarshan National Broadcast Archive",
                    "Original Air Date" to "May 2020 (Atmanirbhar Bharat Announcement)",
                    "Re-encoding" to "Downloaded and re-compressed via social media app"
                ),
                limitationsNote = "Forensics assesses whether the file was altered, but cannot verify whether the context is contemporary."
            ),
            truthCheckResult = TruthCheckResult(
                verdict = TruthVerdict.MISLEADING_OR_MISSING_CONTEXT,
                confidence = EvidenceConfidence.STRONG,
                headline = "Archival 2020 Speech Being Misrepresented as a New 2026 Relief Package",
                summary = "The video is authentic, but it was recorded in May 2020 during the COVID-19 pandemic. There is NO new general cash grant scheme announced for this year.",
                evaluatedClaims = listOf(
                    SpokenClaim(
                        id = "c1",
                        claimText = "Special economic package of 20 lakh crore rupees announced for citizens.",
                        timestampRange = "00:10 - 00:35",
                        category = ClaimCategory.FINANCIAL_BENEFIT,
                        entities = ClaimEntities(
                            schemeName = "Atmanirbhar Bharat Package (2020)",
                            claimedAmount = "₹20 Lakh Crore total stimulus",
                            deadline = "May 2020 (Expired)"
                        )
                    )
                ),
                officialSourcesChecked = listOf(
                    OfficialSource(
                        id = "s1",
                        authorityName = "Press Information Bureau (PIB)",
                        officialUrl = "https://pib.gov.in/PressReleseDetail.aspx?PRID=1623267",
                        publicationDate = "12 May 2020",
                        sourceType = SourceType.PIB_FACT_CHECK,
                        matchVerdict = MatchVerdict.CONTRADICTED,
                        quoteOrFact = "Archived press release confirms this address to the nation occurred on 12 May 2020 regarding the Atmanirbhar Bharat COVID stimulus package.",
                        officialGuidelines = "Be cautious of old video clips circulating without timestamps or dates on WhatsApp and Telegram."
                    ),
                    OfficialSource(
                        id = "s2",
                        authorityName = "Ministry of Finance",
                        officialUrl = "https://finmin.nic.in",
                        publicationDate = "2026",
                        sourceType = SourceType.MINISTRY_PORTAL,
                        matchVerdict = MatchVerdict.UNVERIFIED_NOT_FOUND,
                        quoteOrFact = "No contemporary universal cash distribution scheme exists in current union budget provisions.",
                        officialGuidelines = "Check official gazette notifications for current financial year schemes."
                    )
                ),
                verifiedSchemeFacts = mapOf(
                    "Original Speech Date" to "12 May 2020 (Over 5 years old)",
                    "Current Status" to "Expired / Historical policy announcement",
                    "Application Link" to "No individual application link exists for this package"
                ),
                limitationsNote = "Fact checking validates temporal context against official government press registries.",
                recommendedActions = listOf(
                    "Do not forward or believe messages claiming this is a new 2026 announcement.",
                    "Look for official dates on news broadcasts before sharing.",
                    "Check pib.gov.in for current government press releases."
                )
            ),
            transcript = "Corona sankat ka samna karte hue naye sankalp ke saath main aaj ek vishesh aarthik package ka ailan kar raha hoon. Yeh package lagbhag 20 lakh crore rupaye ka hoga.",
            riskLevel = "MODERATE RISK",
            primaryWarning = "MISLEADING CONTEXT: The video is genuine, but is 5 years old and being shared as breaking news.",
            actionSteps = listOf(
                "Do not forward old recordings as current news.",
                "Verify publication dates on official PIB channels."
            )
        ),

        // Scenario 4: Deepfake Video with Real Scheme Name (Deepfake CM promoting Fake APK for Ayushman Bharat)
        InvestigationReport(
            id = "demo_ayushman_fake_apk",
            title = "Manipulated Video: CM Urging Citizens to Install 'Ayushman Card.apk' from Telegram",
            mediaMetadata = MediaMetadata(
                fileName = "cm_ayushman_urgent_card.mp4",
                format = "MP4",
                isVideo = true,
                durationSeconds = 42.0,
                fileSizeBytes = 9_600_000,
                resolution = "720x1280",
                sampleRateHz = 44100,
                channels = 1,
                bitRateKbps = 1800,
                codec = "H.264 / AAC"
            ),
            forensicAnalysis = ForensicAnalysis(
                verdict = ForensicVerdict.SUSPICIOUS_INDICATORS_DETECTED,
                confidence = EvidenceConfidence.STRONG,
                headline = "Face Reenactment and Synthetic Voice Overlay Detected",
                summary = "Facial region displays boundary blending artifacts around jawline. Audio track contains spectral discontinuities and synthetic phoneme artifacts.",
                audioAuthenticity = AudioAuthenticity(
                    status = "Synthetic Voice Overlay",
                    confidence = EvidenceConfidence.STRONG,
                    pitchVarianceHz = 16.0f,
                    spectralCentroidHz = 1920f,
                    jitterPercent = 0.12f,
                    syntheticLikelihood = "High",
                    technicalNotes = "Voice characteristics match deepfake voice cloning of regional political leaders."
                ),
                audioIntegrity = AudioIntegrity(
                    clippingDetected = true,
                    suddenTransitionsCount = 2,
                    snrDb = 24.1f,
                    anomaliesFound = listOf("Audio clipping during loud vocal assertions", "Artificial pitch glide")
                ),
                videoSync = VideoSync(
                    avOffsetMs = 120,
                    lipSyncAlignment = "Unnatural Phonation",
                    visualArtifactsDetected = true,
                    visualNotes = "Mouth region re-rendered; chin edge blurring during head tilt."
                ),
                timelineFindings = listOf(
                    TimestampedFinding(
                        timestampSeconds = 9.4f,
                        formattedTime = "00:09",
                        findingType = "Facial Warping",
                        description = "Distortion around lower lip and teeth boundary.",
                        severity = "High",
                        possibleCauses = "Neural face-swap reenactment boundary mismatch."
                    ),
                    TimestampedFinding(
                        timestampSeconds = 22.0f,
                        formattedTime = "00:22",
                        findingType = "Synthetic Cadence",
                        description = "Robotic monotone rhythm during APK download instruction.",
                        severity = "High",
                        possibleCauses = "Text-to-speech insertion spliced into original audio."
                    )
                ),
                technicalProvenance = mapOf(
                    "Video Source" to "Re-encoded short video",
                    "Software" to "DeepFaceLab / Wav2Lip pipeline indicators"
                ),
                limitationsNote = "Forensic detection flags synthetic lip-sync manipulation and voice overlay."
            ),
            truthCheckResult = TruthCheckResult(
                verdict = TruthVerdict.MISLEADING_OR_MISSING_CONTEXT,
                confidence = EvidenceConfidence.STRONG,
                headline = "Ayushman Bharat is a Legitimate Scheme, but the APK Link is Malicious Malware",
                summary = "Ayushman Bharat PM-JAY is a genuine government health cover of ₹5 Lakh, but the government NEVER distributes APK files via Telegram or WhatsApp.",
                evaluatedClaims = listOf(
                    SpokenClaim(
                        id = "c1",
                        claimText = "Ayushman Card provides ₹5,00,000 free medical insurance cover per family per year.",
                        timestampRange = "00:02 - 00:16",
                        category = ClaimCategory.GOVERNMENT_SCHEME,
                        entities = ClaimEntities(
                            schemeName = "Ayushman Bharat PM-JAY",
                            claimedAmount = "₹5,00,000"
                        )
                    ),
                    SpokenClaim(
                        id = "c2",
                        claimText = "Download AyushmanCard.apk from the link in description to activate your card today.",
                        timestampRange = "00:20 - 00:38",
                        category = ClaimCategory.APPLICATION_URL,
                        entities = ClaimEntities(
                            targetUrl = "t.me/ayushman_card_download/app.apk",
                            feeOrPayment = "Free download (Malware payload)"
                        )
                    )
                ),
                officialSourcesChecked = listOf(
                    OfficialSource(
                        id = "s1",
                        authorityName = "National Health Authority (NHA)",
                        officialUrl = "https://beneficiary.nha.gov.in",
                        publicationDate = "2026",
                        sourceType = SourceType.MINISTRY_PORTAL,
                        matchVerdict = MatchVerdict.CORROBORATED,
                        quoteOrFact = "Ayushman Bharat PM-JAY provides 5 lakh coverage. Registration is strictly through official portal beneficiary.nha.gov.in or official 'Ayushman App' on Google Play Store.",
                        officialGuidelines = "NHA never distributes APK files via third-party websites or messaging apps. Third-party APKs contain spyware."
                    ),
                    OfficialSource(
                        id = "s2",
                        authorityName = "Indian Computer Emergency Response Team (CERT-In)",
                        officialUrl = "https://www.cert-in.org.in",
                        publicationDate = "Advisory CIVN-2024-0012",
                        sourceType = SourceType.LAW_ENFORCEMENT,
                        matchVerdict = MatchVerdict.OFFICIALLY_DEBUNKED,
                        quoteOrFact = "CERT-In alert: Fraudsters circulating malicious Android APKs under the guise of government scheme registration to steal SMS OTPs and banking credentials.",
                        officialGuidelines = "Never install unknown APKs from WhatsApp, Telegram or SMS links."
                    )
                ),
                verifiedSchemeFacts = mapOf(
                    "Real Scheme Name" to "Ayushman Bharat PM-JAY (Real)",
                    "Real Portal" to "beneficiary.nha.gov.in",
                    "Official App" to "Ayushman App (Only on Google Play Store)",
                    "Malware Alert" to "Telegram APK contains banking trojan / SMS stealer"
                ),
                limitationsNote = "Cross-referenced with NHA official security advisories and CERT-In mobile malware bulletins.",
                recommendedActions = listOf(
                    "DO NOT download or install the APK file — it is banking spyware designed to read your OTPs.",
                    "Download the authentic 'Ayushman App' only from Google Play Store or visit beneficiary.nha.gov.in.",
                    "If you already installed the APK, immediately turn on Airplane mode, uninstall the app, and inform your bank."
                )
            ),
            transcript = "Sabhi nagrik dhyan dein. Ayushman Card ke tahat paanch lakh tak ka muft ilaj mil raha hai. Is suvidha ko activate karne ke liye description mein diye Telegram link se AyushmanCard.apk download karein.",
            riskLevel = "CRITICAL RISK",
            primaryWarning = "MALWARE WARNING: Legitimate scheme used as bait to deliver malicious Android spyware.",
            actionSteps = listOf(
                "Never install APK files sent on Telegram or WhatsApp.",
                "Use only official government portals (.gov.in)."
            )
        ),

        // Scenario 5: Voice-Only Clone Scam (Manager Voice Impersonation Requesting ₹50,000 NEFT)
        InvestigationReport(
            id = "demo_manager_voice_clone",
            title = "Audio Call: Voice Clone of Manager Requesting Urgent ₹50,000 Vendor Transfer",
            mediaMetadata = MediaMetadata(
                fileName = "manager_voice_note_urgent_transfer.wav",
                format = "WAV",
                isVideo = false,
                durationSeconds = 24.0,
                fileSizeBytes = 2_116_800,
                resolution = null,
                sampleRateHz = 44100,
                channels = 1,
                bitRateKbps = 705,
                codec = "PCM 16-bit Mono"
            ),
            forensicAnalysis = ForensicAnalysis(
                verdict = ForensicVerdict.SUSPICIOUS_INDICATORS_DETECTED,
                confidence = EvidenceConfidence.STRONG,
                headline = "Neural Acoustic Synthesis Artifacts and Unnatural Intonation",
                summary = "Acoustic examination reveals zero natural respiratory breaths, artificial spectral flatness, and pitch transition anomalies typical of ElevenLabs / VALL-E voice cloning.",
                audioAuthenticity = AudioAuthenticity(
                    status = "Cloned Synthetic Audio",
                    confidence = EvidenceConfidence.STRONG,
                    pitchVarianceHz = 10.8f,
                    spectralCentroidHz = 2100f,
                    jitterPercent = 0.05f,
                    syntheticLikelihood = "High",
                    technicalNotes = "Vocal harmonics show robotic phase alignment; absent natural subglottal resonant damping."
                ),
                audioIntegrity = AudioIntegrity(
                    clippingDetected = false,
                    suddenTransitionsCount = 1,
                    snrDb = 38.0f,
                    anomaliesFound = listOf(
                        "Completely synthetic room silence during speech pauses (0 dB ambient)",
                        "Unnatural pitch modulation at 00:08"
                    )
                ),
                videoSync = null,
                timelineFindings = listOf(
                    TimestampedFinding(
                        timestampSeconds = 4.1f,
                        formattedTime = "00:04",
                        findingType = "Synthetic Pause",
                        description = "Dead digital silence between words without microphone self-noise.",
                        severity = "High",
                        possibleCauses = "Neural text-to-speech silence padding."
                    ),
                    TimestampedFinding(
                        timestampSeconds = 11.2f,
                        formattedTime = "00:11",
                        findingType = "Pitch Monotonicity",
                        description = "Flat fundamental pitch despite urgent emotional request.",
                        severity = "Moderate",
                        possibleCauses = "Prosody model limitation in synthetic voice clone."
                    )
                ),
                technicalProvenance = mapOf(
                    "Format" to "WAV Audio",
                    "Sample Rate" to "44100 Hz",
                    "Acoustic Signature" to "Diffusion-based Neural Vocoder"
                ),
                limitationsNote = "Voice cloning indicators rely on high-fidelity audio analysis; compression may diminish subtle spectral cues."
            ),
            truthCheckResult = TruthCheckResult(
                verdict = TruthVerdict.CONTRADICTED_BY_OFFICIAL_SOURCES,
                confidence = EvidenceConfidence.STRONG,
                headline = "Unauthorized Payment Request in Violation of Financial Safety Protocols",
                summary = "The request asks for immediate unverified transfer to a personal UPI ID without supporting purchase orders or dual authorization.",
                evaluatedClaims = listOf(
                    SpokenClaim(
                        id = "c1",
                        claimText = "I'm in a client meeting, urgently transfer 50,000 rupees to this vendor UPI ID right now.",
                        timestampRange = "00:01 - 00:16",
                        category = ClaimCategory.PAYMENT_REQUEST,
                        entities = ClaimEntities(
                            claimedAmount = "₹50,000",
                            feeOrPayment = "UPI transfer to personal ID",
                            deadline = "Immediate / within 10 minutes"
                        )
                    )
                ),
                officialSourcesChecked = listOf(
                    OfficialSource(
                        id = "s1",
                        authorityName = "National Cybercrime Reporting Portal (MHA)",
                        officialUrl = "https://cybercrime.gov.in",
                        publicationDate = "2026",
                        sourceType = SourceType.CYBERCRIME_PORTAL,
                        matchVerdict = MatchVerdict.CONTRADICTED,
                        quoteOrFact = "Advisory on AI Voice Cloning Fraud: Criminals clone voices of superiors or relatives using short social media clips to request urgent funds via UPI.",
                        officialGuidelines = "Always establish secondary verification via known telephone number or verbal secret code before transferring funds."
                    )
                ),
                verifiedSchemeFacts = mapOf(
                    "Fraud Type" to "CEO / Manager Impersonation Wire Fraud",
                    "Standard Protocol" to "Written PO and dual-signoff mandatory",
                    "Emergency Action" to "Call manager back on verified SIM phone number"
                ),
                limitationsNote = "Evaluated against corporate financial control standards and national fraud advisories.",
                recommendedActions = listOf(
                    "DO NOT send any money or UPI payment.",
                    "Call your manager directly on their regular phone number or meet them in person to verify.",
                    "Ask a question only the real manager would know (out-of-band verification).",
                    "Alert your company IT Security or Finance department."
                )
            ),
            transcript = "Hey, I am stuck in an urgent board meeting and my net banking isn't working. Please immediately transfer 50,000 rupees to this vendor UPI handle. I'll reimburse you by evening.",
            riskLevel = "CRITICAL RISK",
            primaryWarning = "FINANCIAL IMPERSONATION: AI Voice Clone detected requesting urgent money transfer.",
            actionSteps = listOf(
                "Do not make any transfer.",
                "Verify identity using a separate channel (phone call).",
                "Report suspicious number to cybercrime.gov.in."
            )
        ),

        // Scenario 6: Inconclusive Forensics + Debunked "PM Kanya Vivah Yojana" Claim
        InvestigationReport(
            id = "demo_kanya_vivah_debunked",
            title = "Audio Note: Claiming 'PM Kanya Vivah Yojana' Grants ₹1,00,000 for Girls' Marriage",
            mediaMetadata = MediaMetadata(
                fileName = "kanya_vivah_whatsapp_audio.m4a",
                format = "M4A",
                isVideo = false,
                durationSeconds = 36.0,
                fileSizeBytes = 420_000,
                resolution = null,
                sampleRateHz = 16000,
                channels = 1,
                bitRateKbps = 96,
                codec = "AAC-LC Mono"
            ),
            forensicAnalysis = ForensicAnalysis(
                verdict = ForensicVerdict.INCONCLUSIVE,
                confidence = EvidenceConfidence.PRELIMINARY,
                headline = "Audio Quality Degraded by Heavy Compression (Inconclusive Forensics)",
                summary = "Heavy WhatsApp AAC re-compression (16 kHz, 96 kbps) and ambient street noise obscure high-frequency vocal harmonics. Forensics cannot confirm nor refute manipulation.",
                audioAuthenticity = AudioAuthenticity(
                    status = "Inconclusive Due to Low Resolution",
                    confidence = EvidenceConfidence.PRELIMINARY,
                    pitchVarianceHz = 24.0f,
                    spectralCentroidHz = 1100f,
                    jitterPercent = 0.45f,
                    syntheticLikelihood = "Inconclusive",
                    technicalNotes = "Compression codec artifacts prevent reliable micro-tremor extraction."
                ),
                audioIntegrity = AudioIntegrity(
                    clippingDetected = true,
                    suddenTransitionsCount = 4,
                    snrDb = 14.2f,
                    anomaliesFound = listOf("High background vehicle horn noise", "Severe low-bitrate compression artifacts")
                ),
                videoSync = null,
                timelineFindings = listOf(
                    TimestampedFinding(
                        timestampSeconds = 12.0f,
                        formattedTime = "00:12",
                        findingType = "Codec Degradation",
                        description = "Lossy compression has smoothed frequency spectrum above 4 kHz.",
                        severity = "Info",
                        possibleCauses = "Repeated WhatsApp audio forward transcoding."
                    )
                ),
                technicalProvenance = mapOf(
                    "Bitrate" to "96 kbps",
                    "Bandwidth" to "Truncated at 7.5 kHz"
                ),
                limitationsNote = "Forensics is strictly inconclusive due to acoustic degradation. However, factual verification provides definitive guidance below."
            ),
            truthCheckResult = TruthCheckResult(
                verdict = TruthVerdict.OFFICIALLY_FACT_CHECKED_FALSE,
                confidence = EvidenceConfidence.STRONG,
                headline = "Debunked by PIB Fact Check: No Such Central Scheme Exists",
                summary = "Even though audio forensics is inconclusive, PIB Fact Check has formally investigated and confirmed that the central government runs no scheme called 'PM Kanya Vivah Yojana' with ₹1 Lakh grant.",
                evaluatedClaims = listOf(
                    SpokenClaim(
                        id = "c1",
                        claimText = "Central government gives ₹1,00,000 cash grant to every 18+ girl for marriage under PM Kanya Vivah.",
                        timestampRange = "00:04 - 00:22",
                        category = ClaimCategory.FINANCIAL_BENEFIT,
                        entities = ClaimEntities(
                            schemeName = "PM Kanya Vivah Yojana (Fake)",
                            claimedAmount = "₹1,00,000 cash grant"
                        )
                    )
                ),
                officialSourcesChecked = listOf(
                    OfficialSource(
                        id = "s1",
                        authorityName = "PIB Fact Check",
                        officialUrl = "https://factcheck.pib.gov.in",
                        publicationDate = "March 2024",
                        sourceType = SourceType.PIB_FACT_CHECK,
                        matchVerdict = MatchVerdict.OFFICIALLY_DEBUNKED,
                        quoteOrFact = "A viral audio claim that Central Govt is offering ₹1,00,000 cash under 'PM Kanya Vivah' is completely FAKE. No such central scheme has been initiated.",
                        officialGuidelines = "Several state governments run wedding assistance schemes (e.g., Mukhyamantri Kanya Vivah), but none have this name or grant mechanism."
                    )
                ),
                verifiedSchemeFacts = mapOf(
                    "Central Scheme Status" to "Fake / Non-existent",
                    "Legitimate State Alternatives" to "State specific schemes exist (e.g. MP, UP) via official state portals only",
                    "Registration Warning" to "Cyber scammers use this rumor to collect Aadhaar and bank details"
                ),
                limitationsNote = "Illustrates core NEXUS design: Inconclusive forensics does not mean safe; truth check independently protects the user.",
                recommendedActions = listOf(
                    "Do not share personal documents or Aadhaar details with agents promising this benefit.",
                    "Check your respective state government women and child development portal for genuine state marriage aid.",
                    "Inform the sender that PIB Fact Check has declared this message fake."
                )
            ),
            transcript = "Dhyan se sunein. Pradhan Mantri Kanya Vivah Yojana ke antargat sabhi ladkiyo ki shaadi ke liye sarkaar de rahi hai ek lakh rupaye seedhe khate mein. Jaldi form bharein.",
            riskLevel = "HIGH RISK",
            primaryWarning = "FACT-CHECK FAKE: Forensics is inconclusive due to poor audio quality, but the claim is officially debunked as fake.",
            actionSteps = listOf(
                "Never share personal documents.",
                "Verify state schemes on myscheme.gov.in."
            )
        ),

        // Scenario 7: Authentic Audio + RBI Regulatory Advisory
        InvestigationReport(
            id = "demo_rbi_advisory",
            title = "Press Audio: RBI Governor Advisory on Illegal Loan Apps and UPI Safety",
            mediaMetadata = MediaMetadata(
                fileName = "rbi_governor_press_briefing.mp3",
                format = "MP3",
                isVideo = false,
                durationSeconds = 50.0,
                fileSizeBytes = 1_200_000,
                resolution = null,
                sampleRateHz = 44100,
                channels = 2,
                bitRateKbps = 192,
                codec = "MP3 Audio"
            ),
            forensicAnalysis = ForensicAnalysis(
                verdict = ForensicVerdict.NO_SUSPICIOUS_INDICATORS_DETECTED,
                confidence = EvidenceConfidence.STRONG,
                headline = "Authentic Speech Signal with Continuous Broadcast Acoustics",
                summary = "Natural vocal jitter, authentic breathing cycles, and consistent room impulse response demonstrate genuine speech without synthetic manipulation.",
                audioAuthenticity = AudioAuthenticity(
                    status = "Authentic Human Voice",
                    confidence = EvidenceConfidence.STRONG,
                    pitchVarianceHz = 36.5f,
                    spectralCentroidHz = 1600f,
                    jitterPercent = 0.72f,
                    syntheticLikelihood = "Low",
                    technicalNotes = "Speech envelope and formant frequencies demonstrate typical live acoustic dynamics."
                ),
                audioIntegrity = AudioIntegrity(
                    clippingDetected = false,
                    suddenTransitionsCount = 0,
                    snrDb = 31.0f,
                    anomaliesFound = emptyList()
                ),
                videoSync = null,
                timelineFindings = emptyList(),
                technicalProvenance = mapOf(
                    "Broadcaster" to "RBI Official Webcast",
                    "Bitrate" to "192 kbps stereo"
                ),
                limitationsNote = "High confidence in acoustic authenticity."
            ),
            truthCheckResult = TruthCheckResult(
                verdict = TruthVerdict.SUPPORTED_BY_OFFICIAL_SOURCES,
                confidence = EvidenceConfidence.STRONG,
                headline = "Corroborated by Reserve Bank of India Official Communications",
                summary = "RBI has repeatedly issued public advisories warning against downloading unapproved instant lending applications and sharing UPI PINs.",
                evaluatedClaims = listOf(
                    SpokenClaim(
                        id = "c1",
                        claimText = "Customers must verify that lending entities are RBI-registered NBFCs or banks before borrowing.",
                        timestampRange = "00:08 - 00:32",
                        category = ClaimCategory.GENERAL_FACT,
                        entities = ClaimEntities(
                            schemeName = "RBI Fair Lending Guidelines"
                        )
                    )
                ),
                officialSourcesChecked = listOf(
                    OfficialSource(
                        id = "s1",
                        authorityName = "Reserve Bank of India",
                        officialUrl = "https://www.rbi.org.in",
                        publicationDate = "2024 / 2026",
                        sourceType = SourceType.RESERVE_BANK_INDIA,
                        matchVerdict = MatchVerdict.CORROBORATED,
                        quoteOrFact = "RBI urges public not to fall prey to unauthorized digital lending platforms and to verify regulated entities list on rbi.org.in.",
                        officialGuidelines = "Never enter UPI PIN to receive money. UPI PIN is only required to send money."
                    )
                ),
                verifiedSchemeFacts = mapOf(
                    "Official Portal" to "rbi.org.in / sachet.rbi.org.in",
                    "Regulatory Stance" to "Unregistered loan apps are illegal",
                    "Safety Rule" to "Entering UPI PIN always deducts funds, never credits them"
                ),
                limitationsNote = "Fully verified against central bank public notifications.",
                recommendedActions = listOf(
                    "Follow official RBI advice: check whether financial apps are registered on sachet.rbi.org.in.",
                    "Remember: UPI PIN is never required to receive money."
                )
            ),
            transcript = "Citizens are advised to be cautious when dealing with digital lending apps. Please ensure the lender is a registered bank or NBFC on the RBI website. Never enter your UPI PIN to receive money.",
            riskLevel = "SAFE / VERIFIED",
            primaryWarning = "AUTHENTIC ADVISORY: Authentic regulatory communication offering genuine consumer protection advice.",
            actionSteps = listOf(
                "Verify financial entities on rbi.org.in.",
                "Remember: never enter UPI PIN to receive money."
            )
        ),

        // Scenario 8: Synthesized Voice "Digital Arrest" Extortion
        InvestigationReport(
            id = "demo_digital_arrest_scam",
            title = "Threat Call: 'Digital Arrest' Threat Posing as Police / Telecom Authority",
            mediaMetadata = MediaMetadata(
                fileName = "digital_arrest_cbi_threat_call.mp3",
                format = "MP3",
                isVideo = false,
                durationSeconds = 38.0,
                fileSizeBytes = 912_000,
                resolution = null,
                sampleRateHz = 24000,
                channels = 1,
                bitRateKbps = 64,
                codec = "MP3 Mono"
            ),
            forensicAnalysis = ForensicAnalysis(
                verdict = ForensicVerdict.SUSPICIOUS_INDICATORS_DETECTED,
                confidence = EvidenceConfidence.STRONG,
                headline = "Automated Text-to-Speech Voice Engine Detected",
                summary = "Robotic cadence, uniform phoneme durations, and lack of biological vocal harmonics identify an automated interactive voice response / TTS synthesis tool.",
                audioAuthenticity = AudioAuthenticity(
                    status = "Synthetic Voice Bot Detected",
                    confidence = EvidenceConfidence.STRONG,
                    pitchVarianceHz = 8.1f,
                    spectralCentroidHz = 1950f,
                    jitterPercent = 0.04f,
                    syntheticLikelihood = "High",
                    technicalNotes = "Vocal cadence matches commercial TTS engines with zero natural speech pauses or breath intake."
                ),
                audioIntegrity = AudioIntegrity(
                    clippingDetected = false,
                    suddenTransitionsCount = 2,
                    snrDb = 35.0f,
                    anomaliesFound = listOf("Pre-recorded voice prompt playback spliced into live audio stream")
                ),
                videoSync = null,
                timelineFindings = listOf(
                    TimestampedFinding(
                        timestampSeconds = 5.0f,
                        formattedTime = "00:05",
                        findingType = "Synthetic Speech Cadence",
                        description = "Flat robotic cadence reading threatening legal jargon.",
                        severity = "High",
                        possibleCauses = "Text-to-speech engine generating intimidation script."
                    )
                ),
                technicalProvenance = mapOf(
                    "Call Source" to "VoIP / Virtual SIP trunk",
                    "Synthesis Engine" to "Automated IVR Text-to-Speech"
                ),
                limitationsNote = "Definitive synthetic audio markers identified."
            ),
            truthCheckResult = TruthCheckResult(
                verdict = TruthVerdict.OFFICIALLY_FACT_CHECKED_FALSE,
                confidence = EvidenceConfidence.STRONG,
                headline = "Debunked Scam: 'Digital Arrest' Does Not Exist in Indian Law",
                summary = "Prime Minister, Ministry of Home Affairs, and Supreme Court of India have repeatedly warned that there is NO provision for 'Digital Arrest' under any Indian statute.",
                evaluatedClaims = listOf(
                    SpokenClaim(
                        id = "c1",
                        claimText = "Your SIM card and Aadhaar are linked to illegal narcotics courier; you are under digital arrest and must transfer security bond.",
                        timestampRange = "00:02 - 00:30",
                        category = ClaimCategory.AUTHORITY_IMPERSONATION,
                        entities = ClaimEntities(
                            schemeName = "Digital Arrest Extortion Scam",
                            feeOrPayment = "Demanding security bond transfer via RTGS/UPI"
                        )
                    )
                ),
                officialSourcesChecked = listOf(
                    OfficialSource(
                        id = "s1",
                        authorityName = "Ministry of Home Affairs (I4C - Cybercrime)",
                        officialUrl = "https://cybercrime.gov.in",
                        publicationDate = "2024 / 2026",
                        sourceType = SourceType.CYBERCRIME_PORTAL,
                        matchVerdict = MatchVerdict.OFFICIALLY_DEBUNKED,
                        quoteOrFact = "I4C Alert: No government agency, police, ED, or CBI puts citizens under 'digital arrest' or conducts interrogations over Skype/WhatsApp video calls.",
                        officialGuidelines = "Police and investigative agencies never demand money transfers or security deposits over video/voice calls."
                    ),
                    OfficialSource(
                        id = "s2",
                        authorityName = "Prime Minister's 'Mann Ki Baat' Address",
                        officialUrl = "https://pmindia.gov.in",
                        publicationDate = "October 2024",
                        sourceType = SourceType.PIB_FACT_CHECK,
                        matchVerdict = MatchVerdict.OFFICIALLY_DEBUNKED,
                        quoteOrFact = "PM Modi explicitly warned citizens about 'Digital Arrest' fraud: 'There is no system called digital arrest under Indian law. Stop, Think, and Take Action.'",
                        officialGuidelines = "Dial 1930 immediately if targeted by digital arrest threats."
                    )
                ),
                verifiedSchemeFacts = mapOf(
                    "Legal Existence" to "DOES NOT EXIST (100% Criminal Extortion)",
                    "Law Enforcement Procedure" to "Police summons are issued in writing, never via video call demand for funds",
                    "Immediate Action" to "Disconnect call immediately and dial 1930"
                ),
                limitationsNote = "Clear legal and governmental confirmation that Digital Arrest is fraudulent extortion.",
                recommendedActions = listOf(
                    "DISCONNECT the call immediately. Do not stay on the line.",
                    "DO NOT transfer any money or disclose any banking information.",
                    "Report the phone number immediately to the National Cybercrime Helpline by dialing 1930 or visiting cybercrime.gov.in.",
                    "Reassure your family members — nobody is coming to arrest you."
                )
            ),
            transcript = "This is a call from Delhi Police Headquarters. Your Aadhaar number has been found in an illegal parcel containing narcotics. A warrant is issued. You are placed under digital arrest. Transfer 1 lakh security deposit to avoid immediate custody.",
            riskLevel = "CRITICAL RISK",
            primaryWarning = "CRITICAL EXTORTION: 'Digital Arrest' is a criminal extortion scam. Hang up immediately.",
            actionSteps = listOf(
                "Disconnect the call immediately.",
                "Dial 1930 to report the extortion.",
                "Never transfer funds to avoid 'arrest'."
            )
        )
    )

    fun getScenarioById(id: String): InvestigationReport? {
        return scenarios.find { it.id == id }
    }
}
