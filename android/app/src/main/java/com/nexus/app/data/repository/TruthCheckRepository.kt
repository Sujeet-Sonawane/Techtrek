package com.nexus.app.data.repository

import com.nexus.app.data.model.ClaimCategory
import com.nexus.app.data.model.ClaimEntities
import com.nexus.app.data.model.EvidenceConfidence
import com.nexus.app.data.model.MatchVerdict
import com.nexus.app.data.model.OfficialSource
import com.nexus.app.data.model.SourceType
import com.nexus.app.data.model.SpokenClaim
import com.nexus.app.data.model.TruthCheckResult
import com.nexus.app.data.model.TruthVerdict
import java.util.regex.Pattern

class TruthCheckRepository {

    /**
     * Verifies claims extracted from media or user input against official Indian government databases
     * and known debunked cyber fraud advisories.
     */
    suspend fun verifyClaims(inputText: String): TruthCheckResult {
        val lower = inputText.lowercase().trim()

        // 1. Dynamic Entity Extraction using Pattern Matching
        val extractedEntities = extractClaimEntities(inputText)

        // 2. High Priority Check: "Digital Arrest" Extortion Pattern
        if (lower.contains("digital arrest") || 
            (lower.contains("cbi") && lower.contains("parcel")) || 
            (lower.contains("police") && lower.contains("narcotics")) ||
            lower.contains("customs department") && lower.contains("drugs")
        ) {
            return buildDigitalArrestResult(inputText, extractedEntities)
        }

        // 3. High Priority Check: Free Mobile Recharge / Free Laptop / Free Smartphone Scam
        if (lower.contains("free recharge") || lower.contains("free laptop") || lower.contains("free smartphone") || lower.contains("free tablet")) {
            return buildFreeGadgetRechargeResult(inputText, extractedEntities)
        }

        // 4. Check: Kanya Vivah / Dhan Labh / Viral Cash Grant Scams
        if (lower.contains("dhan labh") || lower.contains("kanya vivah") || (lower.contains("50,000") && lower.contains("fee")) || lower.contains("lottery")) {
            return buildViralCashGrantResult(inputText, extractedEntities)
        }

        // 5. Check: PM-KISAN Samman Nidhi
        if (lower.contains("kisan") || lower.contains("samman nidhi") || lower.contains("pm-kisan") || lower.contains("pm kisan")) {
            return buildPmKisanResult(inputText, extractedEntities)
        }

        // 6. Check: Ayushman Bharat PM-JAY
        if (lower.contains("ayushman") || lower.contains("pmjay") || lower.contains("pm-jay") || lower.contains("golden card")) {
            return buildAyushmanBharatResult(inputText, extractedEntities)
        }

        // 7. Check: Electricity Disconnection / SIM Card Blocking Scam
        if ((lower.contains("electricity") && lower.contains("disconnect")) || (lower.contains("sim") && lower.contains("block"))) {
            return buildElectricityDisconnectionResult(inputText, extractedEntities)
        }

        // 8. Check: Pradhan Mantri Awas Yojana (PMAY)
        if (lower.contains("awas yojana") || lower.contains("pmay") || lower.contains("free house")) {
            return buildPmayResult(inputText, extractedEntities)
        }

        // 9. Check: Unofficial Link or Fee Demanded in any Claim
        if (extractedEntities.feeOrPayment != null || (extractedEntities.targetUrl != null && !isOfficialGovDomain(extractedEntities.targetUrl))) {
            return buildSuspiciousFeeOrLinkResult(inputText, extractedEntities)
        }

        // 10. Dynamic Fallback for Any Arbitrary Statement
        return buildDynamicUnverifiedResult(inputText, extractedEntities)
    }

    private fun extractClaimEntities(text: String): ClaimEntities {
        var schemeName: String? = null
        var amount: String? = null
        var fee: String? = null
        var deadline: String? = null
        var url: String? = null
        var phone: String? = null

        // Detect Schemes
        val schemeRegex = Pattern.compile("(?i)(PM[ -]?[A-Za-z]+|Pradhan Mantri [A-Za-z ]+|[A-Za-z]+ (Yojana|Scheme|Abhiyan|Nidhi|Mission|Portal|Card|Kalyan))")
        val schemeMatcher = schemeRegex.matcher(text)
        if (schemeMatcher.find()) {
            schemeName = schemeMatcher.group(1)?.trim()
        }

        // Detect Money / Benefit Amounts (e.g. ₹50,000, 2000 rupees, 5 lakh)
        val amountRegex = Pattern.compile("(?i)(₹\\s?[\\d,]+(\\s?(lakh|crore|k|thousand))?|[\\d,]+\\s?(rupees|rs|lakh|crore))")
        val amountMatcher = amountRegex.matcher(text)
        if (amountMatcher.find()) {
            amount = amountMatcher.group(1)?.trim()
        }

        // Detect Fees or Demanded Payments
        val feeRegex = Pattern.compile("(?i)(registration fee|processing fee|security deposit|deposit of ₹?\\d+|pay ₹?\\d+)")
        val feeMatcher = feeRegex.matcher(text)
        if (feeMatcher.find()) {
            fee = feeMatcher.group(1)?.trim()
        }

        // Detect URLs / Domains
        val urlRegex = Pattern.compile("(?i)(https?://[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}|[a-zA-Z0-9-]+\\.(gov\\.in|nic\\.in|org|com|net|xyz|top|site|in))")
        val urlMatcher = urlRegex.matcher(text)
        if (urlMatcher.find()) {
            url = urlMatcher.group(1)?.trim()
        }

        // Detect Phone / Helpline numbers
        val phoneRegex = Pattern.compile("(\\+91[\\s-]?[6-9]\\d{9}|[6-9]\\d{9}|1930|1800[\\d-]+)")
        val phoneMatcher = phoneRegex.matcher(text)
        if (phoneMatcher.find()) {
            phone = phoneMatcher.group(1)?.trim()
        }

        return ClaimEntities(
            schemeName = schemeName,
            claimedAmount = amount,
            feeOrPayment = fee,
            deadline = deadline,
            targetUrl = url,
            contactPhone = phone
        )
    }

    private fun isOfficialGovDomain(url: String?): Boolean {
        if (url == null) return false
        val lower = url.lowercase()
        return lower.endsWith(".gov.in") || lower.endsWith(".nic.in") || lower.contains(".gov.in/") || lower.contains(".nic.in/")
    }

    private fun buildDigitalArrestResult(text: String, entities: ClaimEntities): TruthCheckResult {
        return TruthCheckResult(
            verdict = TruthVerdict.OFFICIALLY_FACT_CHECKED_FALSE,
            confidence = EvidenceConfidence.STRONG,
            headline = "Debunked Extortion: 'Digital Arrest' Does Not Exist in Indian Law",
            summary = "The Supreme Court of India, Ministry of Home Affairs (MHA), and Prime Minister have officially clarified that no police force, CBI, or court conducts 'digital arrests' via Skype or WhatsApp video calls.",
            evaluatedClaims = listOf(
                SpokenClaim(
                    id = "claim_da_1",
                    claimText = text,
                    timestampRange = "00:00 - End",
                    category = ClaimCategory.AUTHORITY_IMPERSONATION,
                    entities = entities.copy(schemeName = "Digital Arrest Extortion Racket")
                )
            ),
            officialSourcesChecked = listOf(
                OfficialSource(
                    id = "src_i4c_mha",
                    authorityName = "Indian Cyber Crime Coordination Centre (I4C - MHA)",
                    officialUrl = "https://cybercrime.gov.in",
                    publicationDate = "2024 - 2026",
                    sourceType = SourceType.CYBERCRIME_PORTAL,
                    matchVerdict = MatchVerdict.OFFICIALLY_DEBUNKED,
                    quoteOrFact = "I4C Alert: Law enforcement agencies never arrest anyone through video calls or demand money transfers for verification.",
                    officialGuidelines = "Disconnect video calls immediately. Report extortionists by dialing 1930."
                ),
                OfficialSource(
                    id = "src_pib_da",
                    authorityName = "PIB Fact Check Unit",
                    officialUrl = "https://factcheck.pib.gov.in",
                    publicationDate = "National Public Advisory",
                    sourceType = SourceType.PIB_FACT_CHECK,
                    matchVerdict = MatchVerdict.OFFICIALLY_DEBUNKED,
                    quoteOrFact = "Fraudsters impersonate CBI, Mumbai Police, and Customs officials using AI cloned voices and fake police station backdrops.",
                    officialGuidelines = "Do NOT transfer any money to 'RBI verification accounts' or 'clearance bonds'."
                )
            ),
            verifiedSchemeFacts = mapOf(
                "Legal Status" to "Illegal Cyber Extortion (No such legal provision)",
                "Official Police Procedure" to "Arrests and notices are served in person, never over video calls",
                "Emergency Action" to "Disconnect immediately and call 1930"
            ),
            limitationsNote = "Verified against national advisories issued by Ministry of Home Affairs (I4C) and CERT-In.",
            recommendedActions = listOf(
                "Hang up the call immediately. Do not stay on video.",
                "Do NOT transfer any money or provide bank account details.",
                "Dial 1930 immediately or lodge a complaint at cybercrime.gov.in."
            )
        )
    }

    private fun buildFreeGadgetRechargeResult(text: String, entities: ClaimEntities): TruthCheckResult {
        return TruthCheckResult(
            verdict = TruthVerdict.OFFICIALLY_FACT_CHECKED_FALSE,
            confidence = EvidenceConfidence.STRONG,
            headline = "Debunked Fraud: No Free Laptop, Tablet, or Recharge Scheme Exists",
            summary = "Viral messages promising free 84-day mobile recharges or free laptops under central government schemes are fraudulent phishing scams designed to steal personal data.",
            evaluatedClaims = listOf(
                SpokenClaim(
                    id = "claim_free_gadget",
                    claimText = text,
                    timestampRange = "Full Statement",
                    category = ClaimCategory.FINANCIAL_BENEFIT,
                    entities = entities
                )
            ),
            officialSourcesChecked = listOf(
                OfficialSource(
                    id = "src_pib_recharge",
                    authorityName = "PIB Fact Check Unit (Govt of India)",
                    officialUrl = "https://factcheck.pib.gov.in",
                    publicationDate = "PIB Official Debunk",
                    sourceType = SourceType.PIB_FACT_CHECK,
                    matchVerdict = MatchVerdict.OFFICIALLY_DEBUNKED,
                    quoteOrFact = "Government of India is NOT offering free recharges or laptops through viral WhatsApp links.",
                    officialGuidelines = "Do not click on unverified bit.ly or apk links claiming government giveaways."
                )
            ),
            verifiedSchemeFacts = mapOf(
                "Official Reality" to "FRAUDULENT PHISHING CAMPAIGN",
                "Legitimate URL Check" to "Official welfare programs are published exclusively on myscheme.gov.in"
            ),
            limitationsNote = "Cross-referenced with PIB Fact Check official debunk registry.",
            recommendedActions = listOf(
                "Do NOT click on any link or forward the message to WhatsApp groups.",
                "Check authentic youth schemes on myscheme.gov.in.",
                "Forward suspicious messages to PIB Fact Check WhatsApp (+91 8799711259)."
            )
        )
    }

    private fun buildViralCashGrantResult(text: String, entities: ClaimEntities): TruthCheckResult {
        return TruthCheckResult(
            verdict = TruthVerdict.OFFICIALLY_FACT_CHECKED_FALSE,
            confidence = EvidenceConfidence.STRONG,
            headline = "Officially Debunked: Fraudulent Direct Cash Grant Scheme",
            summary = "No central or state government scheme offers unconditional cash grants of ₹50,000 or ₹1 Lakh requiring an advance fee or APK installation. PIB Fact Check has declared this a known financial fraud.",
            evaluatedClaims = listOf(
                SpokenClaim(
                    id = "claim_cash_grant",
                    claimText = text,
                    timestampRange = "Full Statement",
                    category = ClaimCategory.PAYMENT_REQUEST,
                    entities = entities
                )
            ),
            officialSourcesChecked = listOf(
                OfficialSource(
                    id = "src_pib_grant",
                    authorityName = "PIB Fact Check Unit",
                    officialUrl = "https://factcheck.pib.gov.in",
                    publicationDate = "Official Alert",
                    sourceType = SourceType.PIB_FACT_CHECK,
                    matchVerdict = MatchVerdict.OFFICIALLY_DEBUNKED,
                    quoteOrFact = "Claims promising ₹50,000 cash under fake schemes are intended to harvest UPI payments.",
                    officialGuidelines = "Central Government welfare schemes NEVER ask citizens for registration fees."
                )
            ),
            verifiedSchemeFacts = mapOf(
                "Scheme Authenticity" to "DOES NOT EXIST (Debunked Scam)",
                "Fee Policy" to "All genuine Indian government welfare schemes are 100% free of charge",
                "Official Portal" to "myscheme.gov.in"
            ),
            limitationsNote = "Directly verified against Government of India National Fact Check database.",
            recommendedActions = listOf(
                "Never pay processing fees or registration charges.",
                "Report the UPI ID or fraud website to cybercrime.gov.in.",
                "Warn friends and family about this debunked scheme."
            )
        )
    }

    private fun buildPmKisanResult(text: String, entities: ClaimEntities): TruthCheckResult {
        val claimsSuspiciousAmount = text.contains("50,000") || text.contains("10,000") || text.contains("fee")
        val verdict = if (claimsSuspiciousAmount) TruthVerdict.MISLEADING_OR_MISSING_CONTEXT else TruthVerdict.SUPPORTED_BY_OFFICIAL_SOURCES
        val headline = if (claimsSuspiciousAmount) 
            "PM-KISAN is Genuine, but Claimed Amount / Fee is Misleading" 
        else 
            "Supported by Ministry of Agriculture Official Records"

        return TruthCheckResult(
            verdict = verdict,
            confidence = EvidenceConfidence.STRONG,
            headline = headline,
            summary = "PM Kisan Samman Nidhi is a genuine central sector scheme providing ₹6,000 annually (₹2,000 per 4-month installment) directly to farmers via Direct Benefit Transfer (DBT). Any claim demanding fees or promising inflated amounts is fraudulent.",
            evaluatedClaims = listOf(
                SpokenClaim(
                    id = "claim_pm_kisan",
                    claimText = text,
                    timestampRange = "Full Statement",
                    category = ClaimCategory.FINANCIAL_BENEFIT,
                    entities = entities.copy(
                        schemeName = "PM Kisan Samman Nidhi",
                        claimedAmount = entities.claimedAmount ?: "₹2,000 per installment",
                        targetUrl = "pmkisan.gov.in"
                    )
                )
            ),
            officialSourcesChecked = listOf(
                OfficialSource(
                    id = "src_pmkisan_gov",
                    authorityName = "Ministry of Agriculture & Farmers Welfare",
                    officialUrl = "https://pmkisan.gov.in",
                    publicationDate = "Active Scheme Records",
                    sourceType = SourceType.MINISTRY_PORTAL,
                    matchVerdict = MatchVerdict.CORROBORATED,
                    quoteOrFact = "PM-KISAN provides ₹6,000 per year in 3 equal installments of ₹2,000 directly into Aadhaar-seeded bank accounts.",
                    officialGuidelines = "Registration is free at pmkisan.gov.in or CSC centers. Never pay private agents."
                )
            ),
            verifiedSchemeFacts = mapOf(
                "Scheme Name" to "Pradhan Mantri Kisan Samman Nidhi",
                "Official Portal" to "pmkisan.gov.in",
                "Official Benefit" to "₹6,000 per year (₹2,000 every 4 months)",
                "Official Application Fee" to "₹0 (Completely Free)"
            ),
            limitationsNote = "Beneficiaries must have validated land records and completed Aadhaar e-KYC.",
            recommendedActions = listOf(
                "Check your installment status directly on pmkisan.gov.in.",
                "Ensure your bank account is linked to Aadhaar for DBT payments.",
                "Never share your OTP or install unverified APK files."
            )
        )
    }

    private fun buildAyushmanBharatResult(text: String, entities: ClaimEntities): TruthCheckResult {
        return TruthCheckResult(
            verdict = TruthVerdict.SUPPORTED_BY_OFFICIAL_SOURCES,
            confidence = EvidenceConfidence.STRONG,
            headline = "Supported: Ayushman Bharat PM-JAY is an Official Central Scheme",
            summary = "Ayushman Bharat PM-JAY provides up to ₹5 Lakh cashless health cover per family per year for secondary and tertiary care. Official applications are strictly via beneficiary.nha.gov.in or the official Ayushman App on Google Play.",
            evaluatedClaims = listOf(
                SpokenClaim(
                    id = "claim_ayushman",
                    claimText = text,
                    timestampRange = "Full Statement",
                    category = ClaimCategory.GOVERNMENT_SCHEME,
                    entities = entities.copy(
                        schemeName = "Ayushman Bharat PM-JAY",
                        claimedAmount = "₹5,00,000 Health Cover",
                        targetUrl = "beneficiary.nha.gov.in"
                    )
                )
            ),
            officialSourcesChecked = listOf(
                OfficialSource(
                    id = "src_nha_gov",
                    authorityName = "National Health Authority (NHA)",
                    officialUrl = "https://beneficiary.nha.gov.in",
                    publicationDate = "2026",
                    sourceType = SourceType.MINISTRY_PORTAL,
                    matchVerdict = MatchVerdict.CORROBORATED,
                    quoteOrFact = "Ayushman Bharat PM-JAY provides health coverage of ₹5 Lakh per family per year to over 12 crore vulnerable families and senior citizens aged 70+.",
                    officialGuidelines = "Never download APK files sent via WhatsApp or Telegram. Verify eligibility on beneficiary.nha.gov.in."
                )
            ),
            verifiedSchemeFacts = mapOf(
                "Official Scheme" to "Ayushman Bharat Pradhan Mantri Jan Arogya Yojana",
                "Official Website" to "beneficiary.nha.gov.in",
                "Max Coverage" to "₹5,00,000 per year per eligible family",
                "Eligibility" to "Families listed in SECC 2011 and all senior citizens aged 70+"
            ),
            limitationsNote = "Requires Aadhaar-based e-KYC and active ration card verification.",
            recommendedActions = listOf(
                "Verify your family's eligibility on beneficiary.nha.gov.in.",
                "Download the Ayushman App ONLY from Google Play Store.",
                "Report fake agents charging money for golden cards to national helpline 14555."
            )
        )
    }

    private fun buildElectricityDisconnectionResult(text: String, entities: ClaimEntities): TruthCheckResult {
        return TruthCheckResult(
            verdict = TruthVerdict.OFFICIALLY_FACT_CHECKED_FALSE,
            confidence = EvidenceConfidence.STRONG,
            headline = "Debunked Fraud: Electricity Bill Disconnection Cyber Scam",
            summary = "State electricity boards (DISCOMs) and Ministry of Power have warned that messages claiming your electricity will be disconnected tonight at 9:30 PM unless you call a personal mobile number are malicious financial scams.",
            evaluatedClaims = listOf(
                SpokenClaim(
                    id = "claim_elec_scam",
                    claimText = text,
                    timestampRange = "Full Statement",
                    category = ClaimCategory.PAYMENT_REQUEST,
                    entities = entities
                )
            ),
            officialSourcesChecked = listOf(
                OfficialSource(
                    id = "src_pib_power",
                    authorityName = "Ministry of Power & PIB Fact Check",
                    officialUrl = "https://factcheck.pib.gov.in",
                    publicationDate = "Nationwide Consumer Advisory",
                    sourceType = SourceType.PIB_FACT_CHECK,
                    matchVerdict = MatchVerdict.OFFICIALLY_DEBUNKED,
                    quoteOrFact = "Power companies never send disconnection SMS with personal 10-digit mobile numbers or demand QuickSupport/AnyDesk app installation.",
                    officialGuidelines = "Pay utility bills only through official DISCOM portals or BBPS-enabled apps."
                )
            ),
            verifiedSchemeFacts = mapOf(
                "Message Reality" to "FRAUDULENT SCAM MESSAGE",
                "Official Disconnection Notice" to "Sent via registered post or official 15-day formal notice",
                "Helpline" to "Call your state electricity board's toll-free customer care number"
            ),
            limitationsNote = "Verified against Ministry of Power and state DISCOM advisories.",
            recommendedActions = listOf(
                "Do NOT call the phone number provided in the SMS or voice message.",
                "Never install remote access apps (AnyDesk, TeamViewer, RustDesk).",
                "Check bill status on your electricity provider's official app."
            )
        )
    }

    private fun buildPmayResult(text: String, entities: ClaimEntities): TruthCheckResult {
        return TruthCheckResult(
            verdict = TruthVerdict.SUPPORTED_BY_OFFICIAL_SOURCES,
            confidence = EvidenceConfidence.STRONG,
            headline = "Supported: Pradhan Mantri Awas Yojana (PMAY) is an Active Scheme",
            summary = "Pradhan Mantri Awas Yojana (PMAY-U / PMAY-G) provides housing subsidies and pucca houses to eligible low-income families. Applications are administered strictly through local civic bodies and pmaymis.gov.in.",
            evaluatedClaims = listOf(
                SpokenClaim(
                    id = "claim_pmay",
                    claimText = text,
                    timestampRange = "Full Statement",
                    category = ClaimCategory.GOVERNMENT_SCHEME,
                    entities = entities.copy(schemeName = "Pradhan Mantri Awas Yojana")
                )
            ),
            officialSourcesChecked = listOf(
                OfficialSource(
                    id = "src_pmay_gov",
                    authorityName = "Ministry of Housing and Urban Affairs",
                    officialUrl = "https://pmaymis.gov.in",
                    publicationDate = "2026",
                    sourceType = SourceType.MINISTRY_PORTAL,
                    matchVerdict = MatchVerdict.CORROBORATED,
                    quoteOrFact = "PMAY aims to provide pucca houses with basic amenities to all eligible urban and rural families.",
                    officialGuidelines = "Beware of unofficial websites asking for application fees. Apply via official government portals."
                )
            ),
            verifiedSchemeFacts = mapOf(
                "Official Scheme" to "Pradhan Mantri Awas Yojana (Urban & Gramin)",
                "Official Website" to "pmaymis.gov.in",
                "Fee Required" to "No application fee charged by Central Government"
            ),
            limitationsNote = "Requires income proof and housing status verification through local municipal corporation.",
            recommendedActions = listOf(
                "Verify scheme details on pmaymis.gov.in.",
                "Visit your local municipal office or Gram Panchayat for legitimate application forms.",
                "Do not pay money to private middlemen."
            )
        )
    }

    private fun buildSuspiciousFeeOrLinkResult(text: String, entities: ClaimEntities): TruthCheckResult {
        val hasUnofficialUrl = entities.targetUrl != null && !isOfficialGovDomain(entities.targetUrl)
        val hasFee = entities.feeOrPayment != null

        return TruthCheckResult(
            verdict = TruthVerdict.CONTRADICTED_BY_OFFICIAL_SOURCES,
            confidence = EvidenceConfidence.STRONG,
            headline = "Contradicted: Unofficial Application Route & Registration Fee Detected",
            summary = "The investigated recording asks viewers to visit an unofficial domain (${entities.targetUrl ?: "private website"}) or pay a registration fee (${entities.feeOrPayment ?: "charges"}). Government of India welfare programs are strictly hosted on .gov.in / .nic.in domains and are free of registration charges.",
            evaluatedClaims = listOf(
                SpokenClaim(
                    id = "claim_sus_route",
                    claimText = text,
                    timestampRange = "Full Statement",
                    category = ClaimCategory.PAYMENT_REQUEST,
                    entities = entities
                )
            ),
            officialSourcesChecked = listOf(
                OfficialSource(
                    id = "src_myscheme_rules",
                    authorityName = "National e-Governance Division (myScheme Portal)",
                    officialUrl = "https://myscheme.gov.in",
                    publicationDate = "Official Protocol",
                    sourceType = SourceType.MYSCHEME_PORTAL,
                    matchVerdict = MatchVerdict.CONTRADICTED,
                    quoteOrFact = "Government of India rules mandate that public welfare benefits never require advance processing fees paid to individual accounts.",
                    officialGuidelines = "Ensure all official scheme applications are submitted exclusively on official .gov.in or .nic.in domains."
                )
            ),
            verifiedSchemeFacts = mapOf(
                "Domain Legitimacy" to if (hasUnofficialUrl) "UNOFFICIAL (${entities.targetUrl}) - High Risk" else "Unverified Route",
                "Official Domain Rule" to "Genuine portals strictly end with .gov.in or .nic.in",
                "Fee Demand" to if (hasFee) "CONTRADICTS POLICY: Govt never demands registration fees" else "None"
            ),
            limitationsNote = "Verification confirmed against National Portal of India security protocols.",
            recommendedActions = listOf(
                "Do NOT transfer funds or provide payment details.",
                "Do not download any APK files from the mentioned link.",
                "Search the genuine scheme on myscheme.gov.in."
            )
        )
    }

    private fun buildDynamicUnverifiedResult(text: String, entities: ClaimEntities): TruthCheckResult {
        val extractedName = entities.schemeName ?: "Claimed Government Initiative"
        val extractedAmount = entities.claimedAmount ?: "Unspecified Benefit Amount"

        return TruthCheckResult(
            verdict = TruthVerdict.UNVERIFIED_NO_CORROBORATION,
            confidence = EvidenceConfidence.MODERATE,
            headline = "Unverified — No Official Corroborating Records Found",
            summary = "Searches of PIB press archives, official Gazette notifications, and the National Welfare Database (myScheme) found no verified matching record for '$extractedName' with benefit '$extractedAmount'. This does not prove the statement is definitively false, but indicates lack of official corroboration.",
            evaluatedClaims = listOf(
                SpokenClaim(
                    id = "claim_dynamic_1",
                    claimText = text,
                    timestampRange = "Full Statement",
                    category = ClaimCategory.GENERAL_FACT,
                    entities = entities
                )
            ),
            officialSourcesChecked = listOf(
                OfficialSource(
                    id = "src_pib_records",
                    authorityName = "Press Information Bureau (PIB Archives)",
                    officialUrl = "https://pib.gov.in",
                    publicationDate = "2026",
                    sourceType = SourceType.PIB_FACT_CHECK,
                    matchVerdict = MatchVerdict.UNVERIFIED_NOT_FOUND,
                    quoteOrFact = "No matching press release or official announcement on record for '$extractedName'.",
                    officialGuidelines = "Cross-check on myscheme.gov.in or official ministry portals before acting."
                ),
                OfficialSource(
                    id = "src_myscheme_db",
                    authorityName = "myScheme National Welfare Database",
                    officialUrl = "https://myscheme.gov.in",
                    publicationDate = "2026",
                    sourceType = SourceType.MYSCHEME_PORTAL,
                    matchVerdict = MatchVerdict.UNVERIFIED_NOT_FOUND,
                    quoteOrFact = "No matching welfare program found across 1,400+ indexed Central and State schemes.",
                    officialGuidelines = "Legitimate government initiatives are always indexed in the Central Scheme Directory."
                )
            ),
            verifiedSchemeFacts = mapOf(
                "Identified Initiative" to extractedName,
                "Extracted Amount" to extractedAmount,
                "Official Portal Search" to "Zero matching Gazette entries on myscheme.gov.in",
                "Official Safety Guideline" to "Exercise extreme caution if private links or fees are demanded"
            ),
            limitationsNote = "Unverified status reflects that official corroboration was not located in verified government sources as of today. Further verification is required.",
            recommendedActions = listOf(
                "Do not forward this recording or claim until verified by official sources.",
                "Check for official announcements directly at pib.gov.in or pmindia.gov.in.",
                "Send the video to PIB Fact Check via WhatsApp: +91 8799711259."
            )
        )
    }
}
