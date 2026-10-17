package com.nexus.app.data.model

enum class SourceType(val displayName: String) {
    PIB_FACT_CHECK("PIB Fact Check (Government of India)"),
    MYSCHEME_PORTAL("myScheme National Portal"),
    MINISTRY_PORTAL("Official Ministry Portal (.gov.in / .nic.in)"),
    RESERVE_BANK_INDIA("Reserve Bank of India (RBI)"),
    CYBERCRIME_PORTAL("National Cybercrime Reporting Portal (1930)"),
    LAW_ENFORCEMENT("Official Police / Law Enforcement Advisory")
}

enum class MatchVerdict(val label: String) {
    CORROBORATED("Corroborated by Official Source"),
    CONTRADICTED("Contradicted by Official Records"),
    OFFICIALLY_DEBUNKED("Debunked as Fake by Fact Checkers"),
    UNVERIFIED_NOT_FOUND("No Corroborating Official Record Found")
}

data class OfficialSource(
    val id: String,
    val authorityName: String,
    val officialUrl: String,
    val publicationDate: String,
    val sourceType: SourceType,
    val matchVerdict: MatchVerdict,
    val quoteOrFact: String,
    val officialGuidelines: String
)
