package co.csedge.cubycare.data.repository

import co.csedge.cubycare.data.model.Disorder
import co.csedge.cubycare.data.model.FlowchartStep

class DisorderRepository {
    companion object {
        fun getDisorderInfo(issuesString: String): Disorder {
            val lowercaseIssues = issuesString.lowercase()
            
            // Down Syndrome
            if (lowercaseIssues.contains("down") || lowercaseIssues.contains("trisomy")) {
                return Disorder(
                    name = "Down Syndrome",
                    duration = "Lifelong",
                    reasons = "An extra copy of chromosome 21 (Trisomy 21) alters development.",
                    isCurable = false,
                    simpleMedication = "No specific cure exists. Medications may be prescribed for associated conditions like heart defects or thyroid issues (e.g., Levothyroxine). Early intervention therapies (speech, physical) are highly recommended.",
                    pediatricianName = "Dr. Sarah Mitchell",
                    pediatricianSpecialization = "Pediatric Geneticist",
                    pediatricianLocation = "City Children's Hospital",
                    hospitalTimings = "Mon - Fri: 9:00 AM - 4:00 PM\nSat: 10:00 AM - 1:00 PM",
                    exactHospitalAddress = "123 Medical District Ave, Cityville",
                    mainSymptoms = "Distinct facial features (e.g., almond-shaped eyes, flattened face), poor muscle tone (hypotonia), delayed speech/language development, and mild to moderate cognitive impairment.",
                    avoidance = "Avoid sedentary lifestyles to prevent obesity. Monitor carefully for obstructive sleep apnea. Avoid strenuous neck activities if atlantoaxial instability is present. Prevent social isolation.",
                    flowchartSteps = listOf(
                        FlowchartStep("Cell Division Error", "During conception, an abnormal cell division results in an extra full or partial copy of chromosome 21."),
                        FlowchartStep("Altered Development", "The extra genetic material alters the course of development and causes physical traits associated with Down syndrome."),
                        FlowchartStep("Physical & Cognitive Impact", "This leads to mild to moderate intellectual disability and characteristic facial features."),
                        FlowchartStep("Associated Health Risks", "Increased risk of congenital heart defects, thyroid issues, and hearing/vision problems.")
                    )
                )
            }
            
            // Cystic Fibrosis
            if (lowercaseIssues.contains("cystic") || lowercaseIssues.contains("fibrosis")) {
                return Disorder(
                    name = "Cystic Fibrosis",
                    duration = "Lifelong",
                    reasons = "Mutations in the CFTR gene cause thick, sticky mucus buildup.",
                    isCurable = false,
                    simpleMedication = "Mucus-thinning drugs, bronchodilators, and CFTR modulators (e.g., Trikafta) may be used to manage symptoms.",
                    pediatricianName = "Dr. James Carter",
                    pediatricianSpecialization = "Pediatric Pulmonologist",
                    pediatricianLocation = "National Respiratory Center",
                    hospitalTimings = "Mon - Fri: 8:00 AM - 5:00 PM\nSat: Closed",
                    exactHospitalAddress = "456 Downtown Blvd, Metropolis",
                    mainSymptoms = "Persistent cough with thick mucus, frequent lung infections, wheezing, poor weight gain despite a good appetite, and frequent greasy, bulky stools.",
                    avoidance = "Avoid exposure to smoke, dust, and airborne infections. Avoid contact with others who have cold/flu symptoms. Avoid dehydration and ensure proper enzyme/calorie intake.",
                    flowchartSteps = listOf(
                        FlowchartStep("Gene Mutation", "A defect in the CFTR gene disrupts the normal movement of salt and water in and out of cells."),
                        FlowchartStep("Mucus Thickening", "The body produces thick, sticky mucus instead of thin, slippery mucus."),
                        FlowchartStep("Organ Blockage", "This mucus clogs the lungs and blocks the pancreas, preventing natural enzymes from helping the body break down food."),
                        FlowchartStep("Symptom Onset", "Leads to severe respiratory infections and poor weight gain.")
                    )
                )
            }

            // Fallback for any other / generic or multiple disorders
            val displayName = if (issuesString.contains(",")) "Genetic Disorders" else issuesString.trim().replaceFirstChar { it.uppercase() }
            return Disorder(
                name = displayName,
                duration = "Varies by specific condition",
                reasons = "Typically caused by mutations or changes in the DNA sequence.",
                isCurable = false,
                simpleMedication = "Treatment is highly specific to the disorder. Some may require daily medication, therapies, or dietary changes. Please consult your specialist.",
                pediatricianName = "Dr. Emily Chen",
                pediatricianSpecialization = "Pediatric Specialist",
                pediatricianLocation = "Regional Children's Healthcare Center",
                hospitalTimings = "Mon - Sun: 24/7 (Emergency)\nMon - Fri: 9:00 AM - 6:00 PM (OPD)",
                exactHospitalAddress = "789 Healthway Drive, Regional Hub",
                mainSymptoms = "Symptoms vary widely based on the specific disorder but may include developmental delays, physical anomalies, respiratory issues, or metabolic abnormalities.",
                avoidance = "Follow specialist guidelines carefully. Avoid known triggers if applicable. Do not miss scheduled screenings, vaccinations, and routine health check-ups.",
                flowchartSteps = listOf(
                    FlowchartStep("Genetic Variation", "A variation or mutation occurs in the DNA."),
                    FlowchartStep("Protein Alteration", "The mutation changes how specific proteins are made or function in the body."),
                    FlowchartStep("Cellular Impact", "Cells cannot perform their normal duties as intended."),
                    FlowchartStep("Clinical Symptoms", "This leads to the physical or developmental symptoms observed.")
                )
            )
        }
    }
}
