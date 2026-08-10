package co.csedge.cubycare.data.repository

import co.csedge.cubycare.data.model.Vaccine
import java.util.UUID

object VaccineScheduleProvider {

    fun sanitizeVaccineName(name: String): String {
        return when {
            name.contains("dynamic_74") -> "BCG (Tuberculosis)"
            name.contains("dynamic_279") -> "OPV (Oral Polio - Birth Dose)"
            name.contains("dynamic_210") && !name.contains("Dose") -> "Hepatitis B (Hep B1)"
            name.contains("dynamic_138") -> "DTaP / DTP Vaccine"
            name.contains("dynamic_139") -> "DTaP Booster 1"
            name.contains("dynamic_140") -> "DTaP Booster 2"
            name.contains("dynamic_222") -> "IPV (Polio)"
            name.contains("dynamic_223") -> "IPV Booster 1"
            name.contains("dynamic_224") -> "IPV Booster 2"
            name.contains("dynamic_211") -> "Rotavirus Oral Vaccine"
            name.contains("dynamic_212") -> "Rotavirus Booster"
            name.contains("dynamic_346") -> "PCV (Pneumococcal)"
            name.contains("dynamic_287") -> "Hib Vaccine"
            name.contains("dynamic_288") -> "PCV Booster"
            name.contains("dynamic_232") -> "Influenza (Flu Vaccine)"
            name.contains("dynamic_259") -> "MMR (Measles, Mumps, Rubella)"
            name.contains("dynamic_260") -> "MMR Booster"
            name.contains("dynamic_209") -> "Hepatitis A"
            name.contains("dynamic_441") -> "Varicella (Chickenpox)"
            name.contains("dynamic_424") -> "Typhoid Conjugate (TCV)"
            name.startsWith("dynamic_") -> "Childhood Vaccine"
            else -> name
        }
    }

    fun generateIAPSchedule(dobMillis: Long): List<Vaccine> {
        val monthMillis = 30.44 * 24 * 60 * 60 * 1000L
        val schedule = mutableListOf<Vaccine>()

        fun add(
            name: String, 
            age: String, 
            months: Double, 
            dose: Int, 
            type: String = "MANDATORY", 
            reason: String
        ) {
            schedule.add(
                Vaccine(
                    id = UUID.randomUUID().toString(),
                    name = name,
                    recommendedAge = age,
                    recommendedAgeMonths = months.toInt(),
                    doseNumber = dose,
                    nextDueDateMillis = dobMillis + (months * monthMillis).toLong(),
                    type = type,
                    reason = reason
                )
            )
        }

        // At Birth
        add("BCG (Tuberculosis)", "At Birth", 0.0, 1, "MANDATORY", "Protects against Tuberculosis (TB), a serious bacterial infection affecting lungs and brain.")
        add("OPV (Oral Polio - Birth Dose)", "At Birth", 0.0, 0, "MANDATORY", "Oral Polio Vaccine providing initial gut mucosal immunity against Poliovirus.")
        add("Hepatitis B (Hep B1)", "At Birth", 0.0, 1, "MANDATORY", "Protects newborn baby against chronic Hepatitis B liver infection.")

        // 6 Weeks (1.5 Months)
        add("DTaP / DTP Dose 1", "6 Weeks", 1.5, 1, "MANDATORY", "Protects against Diphtheria, Tetanus (lockjaw), and Pertussis (Whooping Cough).")
        add("IPV Dose 1", "6 Weeks", 1.5, 1, "MANDATORY", "Inactivated Polio Vaccine for complete polio paralysis prevention.")
        add("Hepatitis B Dose 2", "6 Weeks", 1.5, 2, "MANDATORY", "Second dose to strengthen Hepatitis B liver immunity.")
        add("Rotavirus Dose 1", "6 Weeks", 1.5, 1, "MANDATORY", "Oral vaccine protecting against severe viral rotavirus diarrhea and dehydration.")
        add("PCV Dose 1", "6 Weeks", 1.5, 1, "MANDATORY", "Pneumococcal vaccine protecting against bacterial pneumonia, blood infection, and meningitis.")
        add("Hib Dose 1", "6 Weeks", 1.5, 1, "MANDATORY", "Haemophilus Influenzae type b vaccine preventing severe throat swelling and meningitis.")

        // 10 Weeks (2.5 Months)
        add("DTaP / DTP Dose 2", "10 Weeks", 2.5, 2, "MANDATORY", "Second dose for Diphtheria, Tetanus, and Whooping Cough protection.")
        add("IPV Dose 2", "10 Weeks", 2.5, 2, "MANDATORY", "Second dose for Poliovirus protection.")
        add("Rotavirus Dose 2", "10 Weeks", 2.5, 2, "MANDATORY", "Second oral dose for Rotavirus diarrhea prevention.")
        add("PCV Dose 2", "10 Weeks", 2.5, 2, "MANDATORY", "Second dose for Pneumococcal disease prevention.")
        add("Hib Dose 2", "10 Weeks", 2.5, 2, "MANDATORY", "Second dose for Hib bacterial infection prevention.")

        // 14 Weeks (3.5 Months)
        add("DTaP / DTP Dose 3", "14 Weeks", 3.5, 3, "MANDATORY", "Third primary dose for Diphtheria, Tetanus, and Whooping Cough.")
        add("IPV Dose 3", "14 Weeks", 3.5, 3, "MANDATORY", "Third dose for Polio protection.")
        add("Hepatitis B Dose 3", "14 Weeks", 3.5, 3, "MANDATORY", "Final primary Hepatitis B dose for long-term protection.")
        add("Rotavirus Dose 3", "14 Weeks", 3.5, 3, "MANDATORY", "Third oral dose for complete Rotavirus diarrhea protection.")
        add("PCV Dose 3", "14 Weeks", 3.5, 3, "MANDATORY", "Third primary dose for Pneumococcal disease prevention.")
        add("Hib Dose 3", "14 Weeks", 3.5, 3, "MANDATORY", "Third primary dose for Hib infection prevention.")

        // 6 Months
        add("Influenza (Flu Vaccine) Dose 1", "6 Months", 6.0, 1, "OPTIONAL", "Seasonal flu shot protecting against influenza virus strains.")

        // 9 Months
        add("MMR (Measles, Mumps, Rubella) Dose 1", "9 Months", 9.0, 1, "MANDATORY", "Protects against Measles, Mumps, and Rubella (German Measles).")

        // 12 Months
        add("Hepatitis A Dose 1", "12 Months", 12.0, 1, "MANDATORY", "Protects against Hepatitis A virus caused by contaminated food/water.")

        // 15 Months
        add("MMR Dose 2", "15 Months", 15.0, 2, "MANDATORY", "Second dose ensuring long-term Measles, Mumps, and Rubella immunity.")
        add("Varicella (Chickenpox) Dose 1", "15 Months", 15.0, 1, "OPTIONAL", "Protects against Chickenpox virus, itchy rash, and high fever.")
        add("PCV Booster", "15 Months", 15.0, 4, "MANDATORY", "Booster dose for Pneumococcal disease protection.")

        // 16 - 18 Months
        add("DTaP Booster 1", "16-18 Months", 17.0, 4, "MANDATORY", "First booster dose for Diphtheria, Tetanus, and Whooping Cough.")
        add("IPV Booster 1", "16-18 Months", 17.0, 4, "MANDATORY", "First booster dose for Polio protection.")
        add("Hib Booster", "16-18 Months", 17.0, 4, "MANDATORY", "Booster dose for Hib infection protection.")

        // 2 Years (24 Months)
        add("Typhoid Conjugate (TCV)", "2 Years", 24.0, 1, "OPTIONAL", "Protects against Typhoid fever (Salmonella Typhi infection).")

        // 4 - 5 Years (48-60 Months)
        add("DTaP Booster 2", "4-5 Years", 60.0, 5, "MANDATORY", "Second booster for Diphtheria, Tetanus, and Whooping Cough before school.")
        add("IPV Booster 2", "4-5 Years", 60.0, 5, "MANDATORY", "Second booster for Polio protection.")
        add("Varicella Dose 2", "4-5 Years", 60.0, 2, "OPTIONAL", "Second dose for complete Chickenpox protection.")
        add("MMR Dose 3", "4-5 Years", 60.0, 3, "MANDATORY", "Final booster for Measles, Mumps, and Rubella.")

        return schedule
    }
}
