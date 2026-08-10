package co.csedge.cubycare.data.model

data class Child(
    val id: String = "",
    val name: String = "",
    val dateOfBirthMillis: Long = 0L,
    val gender: String = "",
    val birthWeight: String = "",
    val birthLength: String = "",
    val headCircumference: String = "",
    val bloodGroup: String = "",
    val geneticIssues: String = "",
    val allergies: String = "",
    val currentMedicalConditions: String = "",
    val isPremature: Boolean = false,
    val prematureMonths: String = "",
    val profileImageUri: String = "",
    val growthLogs: List<GrowthRecord> = emptyList(),
    val vaccines: List<Vaccine> = emptyList(),
    val milestones: List<DevelopmentalMilestone> = emptyList(),
    val foodDiary: List<FoodDiaryEntry> = emptyList(),
    val dietaryPreferences: DietaryPreferences = DietaryPreferences(),
    val healthLogs: List<HealthRecord> = emptyList(),
    val medicines: List<Medicine> = emptyList()
) {
    // Calculates age in months
    val ageInMonths: Int
        get() {
            if (dateOfBirthMillis == 0L) return 0
            val currentTime = System.currentTimeMillis()
            val diffInMillis = currentTime - dateOfBirthMillis
            val days = diffInMillis / (1000 * 60 * 60 * 24)
            return (days / 30.44).toInt()
        }

    // Calculates precise age formatted in Years, Months, and Days
    val ageFormatted: String
        get() {
            if (dateOfBirthMillis == 0L) return "All Ages"

            val birthCal = java.util.Calendar.getInstance().apply {
                timeInMillis = dateOfBirthMillis
            }
            val nowCal = java.util.Calendar.getInstance()

            var years = nowCal.get(java.util.Calendar.YEAR) - birthCal.get(java.util.Calendar.YEAR)
            var months = nowCal.get(java.util.Calendar.MONTH) - birthCal.get(java.util.Calendar.MONTH)
            var days = nowCal.get(java.util.Calendar.DAY_OF_MONTH) - birthCal.get(java.util.Calendar.DAY_OF_MONTH)

            if (days < 0) {
                months--
                val prevMonthCal = (nowCal.clone() as java.util.Calendar).apply {
                    add(java.util.Calendar.MONTH, -1)
                }
                days += prevMonthCal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH)
            }

            if (months < 0) {
                years--
                months += 12
            }

            if (years < 0) return "0 Days"

            val parts = mutableListOf<String>()
            if (years > 0) parts.add("$years ${if (years == 1) "Yr" else "Yrs"}")
            if (months > 0) parts.add("$months ${if (months == 1) "Mo" else "Mos"}")
            if (days > 0 || parts.isEmpty()) parts.add("$days ${if (days == 1) "Day" else "Days"}")

            return parts.joinToString(" ")
        }
}
