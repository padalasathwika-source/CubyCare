package co.csedge.cubycare.data.model

data class Medicine(
    val id: String = "",
    val name: String = "",
    val time: String = "", // E.g., "08:00 AM"
    val dose: String = "", // E.g., "5ml"
    val duration: String = "", // E.g., "5 days"
    val frequency: String = "", // E.g., "Twice a day"
    val flowchartInfo: String = "", // Body effect description
    val lastAdministeredTime: Long = 0L
)
