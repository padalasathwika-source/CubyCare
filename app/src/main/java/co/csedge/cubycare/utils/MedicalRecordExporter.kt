package co.csedge.cubycare.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import co.csedge.cubycare.data.model.Child
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object MedicalRecordExporter {

    fun exportChildCSV(context: Context, child: Child) {
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dobStr = if (child.dateOfBirthMillis > 0L) sdf.format(Date(child.dateOfBirthMillis)) else "N/A"
            val latestGrowth = child.growthLogs.lastOrNull()

            val csvContent = StringBuilder()
            csvContent.append("Field,Value\n")
            csvContent.append("Child ID,${child.id}\n")
            csvContent.append("Child Name,${child.name}\n")
            csvContent.append("Date of Birth,$dobStr\n")
            csvContent.append("Gender,${child.gender}\n")
            csvContent.append("Blood Group,${child.bloodGroup.ifBlank { "Not Specified" }}\n")
            csvContent.append("Birth Weight,${child.birthWeight.ifBlank { "N/A" }}\n")
            csvContent.append("Birth Length,${child.birthLength.ifBlank { "N/A" }}\n")
            csvContent.append("Head Circumference at Birth,${child.headCircumference.ifBlank { "N/A" }}\n")
            csvContent.append("Premature Born,${if (child.isPremature) "Yes (${child.prematureMonths})" else "No"}\n")
            csvContent.append("Genetic Issues/Disorders,${child.geneticIssues.ifBlank { "None" }}\n")
            csvContent.append("Allergies,${child.allergies.ifBlank { "None" }}\n")
            csvContent.append("Current Medical Conditions,${child.currentMedicalConditions.ifBlank { "None" }}\n")
            csvContent.append("Latest Weight (Kg),${latestGrowth?.weightKg ?: "N/A"}\n")
            csvContent.append("Latest Length (Cm),${latestGrowth?.lengthCm ?: "N/A"}\n")
            csvContent.append("Latest Head Circumference (Cm),${latestGrowth?.headCircumferenceCm ?: "N/A"}\n")

            val file = File(context.cacheDir, "CubyCare_${child.name.replace(" ", "_")}_Medical_Record.csv")
            file.writeText(csvContent.toString())

            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/csv"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, "CubyCare Medical Record - ${child.name}")
                putExtra(Intent.EXTRA_TEXT, "Attached is the CubyCare pediatric health summary for ${child.name}.")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            context.startActivity(Intent.createChooser(shareIntent, "Export Medical Record CSV via"))
        } catch (e: Exception) {
            Toast.makeText(context, "Error exporting CSV: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }

    fun exportChildMedicalReport(context: Context, child: Child) {
        try {
            val sdf = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
            val dobStr = if (child.dateOfBirthMillis > 0L) sdf.format(Date(child.dateOfBirthMillis)) else "N/A"
            val latestGrowth = child.growthLogs.lastOrNull()

            val reportText = """
                =================================================
                          CUBYCARE PEDIATRIC HEALTH REPORT
                =================================================
                Child Name: ${child.name}
                Date of Birth: $dobStr (${child.ageInMonths} months old)
                Gender: ${child.gender}
                Blood Group: ${child.bloodGroup.ifBlank { "Not Specified" }}
                
                MEASUREMENTS & GROWTH HISTORY:
                -------------------------------------------------
                • Birth Weight: ${child.birthWeight.ifBlank { "N/A" }}
                • Birth Length: ${child.birthLength.ifBlank { "N/A" }}
                • Birth Head Circumference: ${child.headCircumference.ifBlank { "N/A" }}
                • Latest Weight: ${latestGrowth?.weightKg?.let { "$it kg" } ?: "N/A"}
                • Latest Height: ${latestGrowth?.lengthCm?.let { "$it cm" } ?: "N/A"}
                • Latest Head Circumference: ${latestGrowth?.headCircumferenceCm?.let { "$it cm" } ?: "N/A"}
                
                MEDICAL PROFILE & SPECIAL CARE:
                -------------------------------------------------
                • Premature Birth: ${if (child.isPremature) "Yes (${child.prematureMonths})" else "No"}
                • Known Allergies: ${child.allergies.ifBlank { "None reported" }}
                • Genetic Issues / Disorders: ${child.geneticIssues.ifBlank { "None reported" }}
                • Current Medical Conditions: ${child.currentMedicalConditions.ifBlank { "None reported" }}
                
                =================================================
                Generated by CubyCare Pediatric Health Companion
                Date: ${sdf.format(Date())}
                =================================================
            """.trimIndent()

            val file = File(context.cacheDir, "CubyCare_${child.name.replace(" ", "_")}_Summary.txt")
            file.writeText(reportText)

            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, "CubyCare Pediatric Health Report - ${child.name}")
                putExtra(Intent.EXTRA_TEXT, reportText)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            context.startActivity(Intent.createChooser(shareIntent, "Share/Export Medical Summary Report"))
        } catch (e: Exception) {
            Toast.makeText(context, "Error sharing report: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }

    fun exportAllChildrenCSV(context: Context, children: List<Child>) {
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val csvContent = StringBuilder()
            csvContent.append("Child ID,Child Name,DOB,Gender,Blood Group,Birth Weight,Birth Length,Birth Head Circ,Premature,Genetic Issues,Allergies,Medical Conditions,Latest Weight (kg),Latest Height (cm),Latest Head Circ (cm)\n")

            children.forEach { child ->
                val dobStr = if (child.dateOfBirthMillis > 0L) sdf.format(Date(child.dateOfBirthMillis)) else "N/A"
                val latestGrowth = child.growthLogs.lastOrNull()
                csvContent.append("\"${child.id}\",\"${child.name}\",\"$dobStr\",\"${child.gender}\",\"${child.bloodGroup.ifBlank { "Not Specified" }}\",\"${child.birthWeight.ifBlank { "N/A" }}\",\"${child.birthLength.ifBlank { "N/A" }}\",\"${child.headCircumference.ifBlank { "N/A" }}\",\"${if (child.isPremature) "Yes (${child.prematureMonths})" else "No"}\",\"${child.geneticIssues.ifBlank { "None" }}\",\"${child.allergies.ifBlank { "None" }}\",\"${child.currentMedicalConditions.ifBlank { "None" }}\",\"${latestGrowth?.weightKg ?: "N/A"}\",\"${latestGrowth?.lengthCm ?: "N/A"}\",\"${latestGrowth?.headCircumferenceCm ?: "N/A"}\"\n")
            }

            val file = File(context.cacheDir, "CubyCare_All_Children_Medical_Records.csv")
            file.writeText(csvContent.toString())

            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/csv"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, "CubyCare All Children Medical Records CSV")
                putExtra(Intent.EXTRA_TEXT, "Attached is the CubyCare pediatric health summary CSV for all registered children.")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            context.startActivity(Intent.createChooser(shareIntent, "Export All Children CSV via"))
        } catch (e: Exception) {
            Toast.makeText(context, "Error exporting CSV: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }

    fun exportAllChildrenMedicalReport(context: Context, children: List<Child>) {
        try {
            val sdf = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
            val reportText = StringBuilder()
            reportText.append("=================================================\n")
            reportText.append("       CUBYCARE ALL CHILDREN MEDICAL REPORT      \n")
            reportText.append("=================================================\n\n")

            children.forEachIndexed { index, child ->
                val dobStr = if (child.dateOfBirthMillis > 0L) sdf.format(Date(child.dateOfBirthMillis)) else "N/A"
                val latestGrowth = child.growthLogs.lastOrNull()
                reportText.append("CHILD #${index + 1}: ${child.name.uppercase()}\n")
                reportText.append("-------------------------------------------------\n")
                reportText.append("• Date of Birth: $dobStr (${child.ageInMonths} months old)\n")
                reportText.append("• Gender: ${child.gender}\n")
                reportText.append("• Blood Group: ${child.bloodGroup.ifBlank { "Not Specified" }}\n")
                reportText.append("• Birth Weight: ${child.birthWeight.ifBlank { "N/A" }} | Length: ${child.birthLength.ifBlank { "N/A" }} | Head Circ: ${child.headCircumference.ifBlank { "N/A" }}\n")
                reportText.append("• Latest Weight: ${latestGrowth?.weightKg?.let { "$it kg" } ?: "N/A"} | Latest Height: ${latestGrowth?.lengthCm?.let { "$it cm" } ?: "N/A"}\n")
                reportText.append("• Premature Birth: ${if (child.isPremature) "Yes (${child.prematureMonths})" else "No"}\n")
                reportText.append("• Known Allergies: ${child.allergies.ifBlank { "None reported" }}\n")
                reportText.append("• Genetic Issues: ${child.geneticIssues.ifBlank { "None reported" }}\n")
                reportText.append("• Current Medical Conditions: ${child.currentMedicalConditions.ifBlank { "None reported" }}\n\n")
            }

            reportText.append("=================================================\n")
            reportText.append("Generated by CubyCare Pediatric Companion\n")
            reportText.append("Date: ${sdf.format(Date())}\n")
            reportText.append("=================================================\n")

            val file = File(context.cacheDir, "CubyCare_All_Children_Medical_Report.txt")
            file.writeText(reportText.toString())

            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, "CubyCare All Children Pediatric Medical Report")
                putExtra(Intent.EXTRA_TEXT, reportText.toString())
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            context.startActivity(Intent.createChooser(shareIntent, "Share All Children Medical Report"))
        } catch (e: Exception) {
            Toast.makeText(context, "Error sharing report: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }

    fun contactPediatricSupport(context: Context, childName: String = "My Child") {
        try {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf("support@cubycare.com"))
                putExtra(Intent.EXTRA_SUBJECT, "CubyCare Pediatric Support Inquiry - $childName")
                putExtra(Intent.EXTRA_TEXT, "Hello CubyCare Pediatric Medical Team,\n\nI need support regarding $childName.\n\nQuery Details:\n")
            }
            context.startActivity(Intent.createChooser(intent, "Send Email via"))
        } catch (e: Exception) {
            Toast.makeText(context, "No email app found: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }
}
