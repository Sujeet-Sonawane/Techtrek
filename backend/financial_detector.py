"""
NEXUS Financial Context Detection Engine
Identifies sensitive financial triggers, urgency pressure, and transactional indicators.
"""

import re
from typing import Dict, Any, List, Tuple

FINANCIAL_PATTERNS = [
    # Indian & Global Currencies / Amounts
    (r"(?:₹|rs\.?|rupees|inr|bucks|\$|dollars?)\s*[\d,]+", "Currency Amount"),
    (r"\b\d+\s*(?:lakh|crore|thousand|k)\b", "Large Monetary Sum"),
    (r"\btransfer\b", "Funds Transfer"),
    (r"\bpayment\b", "Payment Request"),
    (r"\bsend\s+(?:money|funds|cash)\b", "Direct Money Request"),
    
    # Instant Payment Rails & Banking
    (r"\bupi\b", "UPI Rail"),
    (r"\bgpay\b|\bgoogle\s*pay\b", "Google Pay"),
    (r"\bphonepe\b", "PhonePe"),
    (r"\bpaytm\b", "Paytm"),
    (r"\bneft\b|\brtgs\b|\bimps\b", "Bank Clearing (NEFT/RTGS/IMPS)"),
    (r"\bbank\s*account\b|\baccount\s*number\b", "Bank Account Details"),
    (r"\bbeneficiary\b", "Beneficiary Addition"),
    
    # Sensitive Credentials (Red Flag)
    (r"\botp\b|\bone\s*time\s*password\b", "OTP (One-Time Password)"),
    (r"\bpin\b|\bupi\s*pin\b|\bsecurity\s*code\b", "Security PIN"),
    (r"\bpassword\b", "Account Password"),
    (r"\bcvv\b|\bcard\s*number\b|\bexpiry\b", "Card Information"),
    
    # Identity & KYC Compliance
    (r"\bkyc\b", "KYC Verification"),
    (r"\bpan\s*card\b|\bpan\b", "PAN Details"),
    (r"\baadhaar\b", "Aadhaar Information"),
    (r"\brefund\b|\bcashback\b", "Refund / Cashback Claim"),
    (r"\binvestment\b|\bprofit\b", "Investment Solicitation"),
]

URGENCY_PATTERNS = [
    (r"\bimmediately\b|\bright\s*now\b|\bat\s*once\b", "Immediate Action Demand"),
    (r"\burgent\b|\burgently\b", "Urgency Pressure"),
    (r"\bhurry\b|\bquickly\b|\bfast\b", "Time Constriction"),
    (r"\bwithin\s*\d+\s*(?:mins?|minutes?|hours?)\b", "Countdown Pressure"),
    (r"\baccount\s*(?:blocked|suspended|frozen)\b", "Account Threat Pressure"),
    (r"\bpolice\b|\bcustoms\b|\bcbi\b|\btax\s*department\b", "Authority Intimidation"),
]


def detect_financial_context(text: str) -> Dict[str, Any]:
    """
    Parses conversational text / speech transcript for financial activity and urgency.
    """
    if not text or not text.strip():
        return {
            "financial_context": False,
            "financial_triggers": [],
            "urgency_detected": False,
            "urgency_triggers": [],
            "summary": "No financial context detected."
        }

    lower_text = text.lower()
    financial_triggers: List[str] = []
    urgency_triggers: List[str] = []

    for pattern, label in FINANCIAL_PATTERNS:
        if re.search(pattern, lower_text):
            financial_triggers.append(label)

    for pattern, label in URGENCY_PATTERNS:
        if re.search(pattern, lower_text):
            urgency_triggers.append(label)

    has_financial = len(financial_triggers) > 0
    has_urgency = len(urgency_triggers) > 0

    if has_financial and has_urgency:
        summary = "Sensitive financial request with urgency cues detected. Extreme caution recommended."
    elif has_financial:
        summary = "This conversation appears to involve a financial action. Additional verification is recommended."
    else:
        summary = "Standard conversational content; no financial requests identified."

    return {
        "financial_context": has_financial,
        "financial_triggers": list(set(financial_triggers)),
        "urgency_detected": has_urgency,
        "urgency_triggers": list(set(urgency_triggers)),
        "summary": summary
    }
