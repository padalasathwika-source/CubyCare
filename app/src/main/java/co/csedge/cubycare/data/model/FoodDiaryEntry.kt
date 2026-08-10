package co.csedge.cubycare.data.model

data class FoodDiaryEntry(
    val id: String = "",
    val dateMillis: Long = System.currentTimeMillis(),
    val timeStr: String = "",
    val food: String = "",
    val quantity: String = "",
    val response: String = "" // e.g. "Liked", "Disliked", "Neutral"
)
