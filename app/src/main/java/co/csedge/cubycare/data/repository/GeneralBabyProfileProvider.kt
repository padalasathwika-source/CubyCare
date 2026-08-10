package co.csedge.cubycare.data.repository

import co.csedge.cubycare.data.model.*
import java.util.UUID

object GeneralBabyProfileProvider {

    fun createDefaultGeneralBaby(): Child {
        val birthMillis = System.currentTimeMillis() - (365L * 24 * 3600 * 1000)
        
        return Child(
            id = "default_general_baby",
            name = "Cuby General Baby 👶",
            dateOfBirthMillis = 0L, // General profile has no single fixed age assigned
            gender = "General (Boy/Girl)",
            birthWeight = "3.3 kg",
            birthLength = "50.5 cm",
            headCircumference = "34.5 cm",
            bloodGroup = "O+",
            geneticIssues = "None (Standard Baseline)",
            allergies = "Mild Lactose Sensitivity",
            currentMedicalConditions = "Healthy Pediatric Baseline",
            isPremature = false,
            prematureMonths = "",
            profileImageUri = "general_baby_avatar",
            growthLogs = generateAllAgesGrowthLogs(birthMillis),
            vaccines = generateAllAgesVaccines(birthMillis),
            milestones = generateAllAgesMilestones(),
            foodDiary = generateAllAgesFoodDiary(birthMillis),
            dietaryPreferences = DietaryPreferences(
                isVegetarian = true,
                regionalPreference = "All Regional Diets",
                foodAllergies = "Mild Lactose Sensitivity",
                pickyEatingNotes = "Prefers soft mashed purees, khichdi, and warm milk"
            ),
            healthLogs = generateAllAgesHealthLogs(birthMillis),
            medicines = generateAllAgesMedicines()
        )
    }

    private fun generateAllAgesGrowthLogs(birthMillis: Long): List<GrowthRecord> {
        val monthMillis = (30.44 * 24 * 3600 * 1000).toLong()
        val data = listOf(
            Triple(0, 3.3, 50.5 to 34.5),
            Triple(1, 4.4, 54.5 to 36.8),
            Triple(2, 5.4, 58.0 to 38.3),
            Triple(3, 6.2, 61.0 to 39.8),
            Triple(4, 6.8, 63.5 to 41.2),
            Triple(5, 7.3, 65.5 to 42.2),
            Triple(6, 7.8, 67.5 to 43.2),
            Triple(7, 8.2, 69.0 to 44.0),
            Triple(8, 8.5, 70.5 to 44.7),
            Triple(9, 8.8, 71.8 to 45.3),
            Triple(10, 9.1, 73.0 to 45.8),
            Triple(11, 9.4, 74.2 to 46.2),
            Triple(12, 9.7, 75.8 to 46.8),
            Triple(18, 11.0, 82.0 to 47.8),
            Triple(24, 12.2, 87.5 to 48.8),
            Triple(36, 14.3, 96.0 to 49.8),
            Triple(48, 16.2, 103.0 to 50.8),
            Triple(60, 18.2, 109.5 to 51.5)
        )

        return data.map { (m, wt, htHd) ->
            GrowthRecord(
                id = UUID.randomUUID().toString(),
                dateMillis = birthMillis + (m * monthMillis),
                weightKg = wt,
                lengthCm = htHd.first,
                headCircumferenceCm = htHd.second,
                isEstimated = false,
                monthIndex = m
            )
        }
    }

    private fun generateAllAgesVaccines(birthMillis: Long): List<Vaccine> {
        val baseVaccines = VaccineScheduleProvider.generateIAPSchedule(birthMillis)
        return baseVaccines.mapIndexed { idx, v ->
            if (v.recommendedAgeMonths <= 9) {
                v.copy(
                    administeredDateMillis = birthMillis + (v.recommendedAgeMonths * 30.44 * 24 * 3600 * 1000).toLong()
                )
            } else {
                v
            }
        }
    }

    private fun generateAllAgesMilestones(): List<DevelopmentalMilestone> {
        val baseMilestones = MilestoneProvider.generateDefaultMilestones()
        return baseMilestones.map { m ->
            if (m.ageMonths <= 9.0) {
                m.copy(isCompleted = true, completedDateMillis = System.currentTimeMillis() - 30L * 24 * 3600 * 1000)
            } else {
                m
            }
        }
    }

    private fun generateAllAgesFoodDiary(birthMillis: Long): List<FoodDiaryEntry> {
        val monthMillis = (30.44 * 24 * 3600 * 1000).toLong()
        return listOf(
            FoodDiaryEntry(
                id = UUID.randomUUID().toString(),
                dateMillis = birthMillis + (2 * monthMillis),
                timeStr = "08:00 AM",
                food = "Exclusive Breast Milk / Formula",
                quantity = "150 ml",
                response = "Liked"
            ),
            FoodDiaryEntry(
                id = UUID.randomUUID().toString(),
                dateMillis = birthMillis + (6 * monthMillis),
                timeStr = "10:30 AM",
                food = "Steamed Apple Puree & Soft Ragi Porridge",
                quantity = "1 Small Bowl",
                response = "Liked"
            ),
            FoodDiaryEntry(
                id = UUID.randomUUID().toString(),
                dateMillis = birthMillis + (9 * monthMillis),
                timeStr = "01:00 PM",
                food = "Yellow Dal Khichdi with Ghee",
                quantity = "1 Bowl",
                response = "Liked"
            ),
            FoodDiaryEntry(
                id = UUID.randomUUID().toString(),
                dateMillis = birthMillis + (12 * monthMillis),
                timeStr = "07:30 PM",
                food = "Soft Curd Rice & Boiled Egg Yolk",
                quantity = "1 Bowl",
                response = "Liked"
            ),
            FoodDiaryEntry(
                id = UUID.randomUUID().toString(),
                dateMillis = birthMillis + (24 * monthMillis),
                timeStr = "12:30 PM",
                food = "Mixed Vegetable Soup, Soft Chapati & Milk",
                quantity = "1 Meal Portion",
                response = "Liked"
            )
        )
    }

    private fun generateAllAgesHealthLogs(birthMillis: Long): List<HealthRecord> {
        return listOf(
            HealthRecord(
                id = UUID.randomUUID().toString(),
                dateMillis = System.currentTimeMillis() - (5 * 24 * 3600 * 1000L),
                temperature = "98.6 °F",
                cough = "None",
                cold = "None",
                vomiting = "None",
                diarrhea = "None",
                appetite = "Normal",
                urineFrequency = "6-8 wet diapers (Normal)",
                bowelMovements = "Soft, 1-2 per day",
                rash = "None",
                pain = "None",
                medicationGiven = "Vitamin D3 (1 ml)"
            )
        )
    }

    private fun generateAllAgesMedicines(): List<Medicine> {
        return listOf(
            Medicine(
                id = "1",
                name = "Paracetamol Oral Drops",
                time = "08:00 AM",
                dose = "2.5 ml",
                duration = "As needed",
                frequency = "Post-vaccination or fever > 100°F",
                flowchartInfo = "Reduces post-vaccination fever and mild discomfort",
                lastAdministeredTime = System.currentTimeMillis() - (15 * 24 * 3600 * 1000L)
            ),
            Medicine(
                id = "2",
                name = "Vitamin D3 Drops (400 IU)",
                time = "09:00 AM",
                dose = "1.0 ml",
                duration = "Daily for 1st Year",
                frequency = "Once Daily in Morning",
                flowchartInfo = "Essential for strong bone development and calcium absorption",
                lastAdministeredTime = System.currentTimeMillis() - (1 * 24 * 3600 * 1000L)
            ),
            Medicine(
                id = "3",
                name = "Pediatric Zinc & Multivitamin",
                time = "01:30 PM",
                dose = "5.0 ml",
                duration = "Continuous support",
                frequency = "Once Daily after lunch",
                flowchartInfo = "Supports immune system strength and appetite",
                lastAdministeredTime = System.currentTimeMillis() - (2 * 24 * 3600 * 1000L)
            ),
            Medicine(
                id = "4",
                name = "Saline Nasal Drops (Solivin)",
                time = "08:30 PM",
                dose = "2 Drops",
                duration = "3-5 Days during cold",
                frequency = "Twice Daily before sleep",
                flowchartInfo = "Clears nasal congestion for peaceful sleep",
                lastAdministeredTime = System.currentTimeMillis() - (10 * 24 * 3600 * 1000L)
            )
        )
    }
}
