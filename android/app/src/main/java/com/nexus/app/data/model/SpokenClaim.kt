package com.nexus.app.data.model

enum class ClaimCategory(val label: String) {
    GOVERNMENT_SCHEME("Government Scheme"),
    FINANCIAL_BENEFIT("Direct Financial Benefit"),
    ELIGIBILITY_CRITERIA("Eligibility Criteria"),
    DEADLINE_URGENCY("Urgency / Deadline"),
    APPLICATION_URL("Application Portal / URL"),
    PAYMENT_REQUEST("Payment / Processing Fee"),
    AUTHORITY_IMPERSONATION("Official Impersonation"),
    GENERAL_FACT("General Claim")
}

data class ClaimEntities(
    val schemeName: String? = null,
    val claimedAmount: String? = null,
    val eligibility: String? = null,
    val deadline: String? = null,
    val targetUrl: String? = null,
    val feeOrPayment: String? = null
)

data class SpokenClaim(
    val id: String,
    val claimText: String,
    val timestampRange: String,
    val category: ClaimCategory,
    val entities: ClaimEntities = ClaimEntities()
)
