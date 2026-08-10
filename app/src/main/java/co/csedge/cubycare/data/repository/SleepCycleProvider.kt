package co.csedge.cubycare.data.repository

import co.csedge.cubycare.data.model.SleepCycleInfo

object SleepCycleProvider {

    val ageRanges = listOf(
        "0-6 Months",
        "6-12 Months",
        "1-5 Years"
    )

    fun getSleepInfoForAge(ageRange: String): SleepCycleInfo {
        return allSleepData.find { it.ageRange == ageRange } ?: SleepCycleInfo(
            ageRange = ageRange,
            sleepDuration = "Data not available",
            numberOfNaps = "Data not available",
            nightAwakenings = "Data not available",
            whenDisturbed = "Data not available",
            mildRemedies = "Data not available",
            severeSymptoms = "Data not available"
        )
    }

    private val allSleepData = listOf(
        SleepCycleInfo(
            ageRange = "0-6 Months",
            sleepDuration = "14 - 17 hours total",
            numberOfNaps = "3 - 4 naps per day",
            nightAwakenings = "2 - 3 times (typically for feeding)",
            whenDisturbed = "Increased fussiness, crying, difficulty feeding, and extreme daytime fatigue. A disturbed cycle at this age often stems from growth spurts or hunger.",
            mildRemedies = "Ensure a calm, dark sleep environment. Try gentle rocking, a warm bath before bed, swaddling, or white noise. Ensure the baby is well-fed before sleep.",
            severeSymptoms = "If the baby is inconsolable for hours, has a fever, shows signs of dehydration (fewer wet diapers), or has abnormal breathing patterns, consult a pediatrician immediately."
        ),
        SleepCycleInfo(
            ageRange = "6-12 Months",
            sleepDuration = "12 - 16 hours total",
            numberOfNaps = "2 naps per day",
            nightAwakenings = "0 - 1 time (starts sleeping through the night)",
            whenDisturbed = "Increased clinginess, separation anxiety, irritability, and hyperactivity during the day. May experience temporary sleep regressions due to teething or hitting new developmental milestones.",
            mildRemedies = "Stick to a consistent bedtime routine. Offer teething rings if teething is suspected. Provide a transitional object (like a safe blanket or stuffed animal). Gradually reduce nighttime feedings if approved by your doctor.",
            severeSymptoms = "If sleep disturbances are accompanied by severe night terrors, chronic snoring, pausing in breathing (sleep apnea), or extreme lethargy during the day, seek medical advice."
        ),
        SleepCycleInfo(
            ageRange = "1-5 Years",
            sleepDuration = "10 - 14 hours total",
            numberOfNaps = "1 nap (often drops by age 3-4)",
            nightAwakenings = "Rarely (unless sick or during regressions)",
            whenDisturbed = "Frequent tantrums, mood swings, difficulty concentrating, and resistance to bedtime. Overtired toddlers often become hyperactive rather than sleepy.",
            mildRemedies = "Enforce a strict wind-down period (no screens 1 hour before bed). Read calming stories. Avoid sugary snacks in the evening. Keep the room cool and use a dim nightlight to prevent fear of the dark.",
            severeSymptoms = "If the child routinely sleepwalks, has persistent severe night terrors, complains of physical pain keeping them awake, or shows significant developmental regression, consult a pediatric sleep specialist."
        )
    )
}
