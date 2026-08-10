package co.csedge.cubycare.ui.dashboard

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import co.csedge.cubycare.data.model.Child
import co.csedge.cubycare.data.model.Medicine
import java.util.*
import co.csedge.cubycare.worker.MedicineAlarmScheduler

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedicineScreen(
    child: Child,
    onBack: () -> Unit,
    onUpdateChild: (Child) -> Unit,
    onMedicineAdded: (String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var timeStr by remember { mutableStateOf("") }
    var dose by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf("") }

    // Helper to generate automatic flowchart info based on name
    fun getAutomaticFlowchart(medName: String): String {
        val lowerName = medName.lowercase()
        return when {
            lowerName.contains("paracetamol") || lowerName.contains("crocin") || lowerName.contains("tylenol") || lowerName.contains("calpol") -> 
                "Inhibits Prostaglandins -> Lowers body temperature -> Relieves pain and fever"
            lowerName.contains("ibuprofen") || lowerName.contains("advil") || lowerName.contains("motrin") -> 
                "Reduces Inflammation -> Decreases swelling -> Relieves pain"
            lowerName.contains("amoxicillin") || lowerName.contains("antibiotic") -> 
                "Targets Bacteria -> Breaks down bacterial cell walls -> Clears infection"
            lowerName.contains("vitamin") || lowerName.contains("d3") -> 
                "Boosts Calcium Absorption -> Strengthens bones -> Promotes healthy growth"
            lowerName.contains("cough") || lowerName.contains("syrup") -> 
                "Soothes Throat -> Suppresses cough reflex -> Eases breathing"
            lowerName.contains("colic") || lowerName.contains("gas") -> 
                "Breaks down gas bubbles -> Relieves stomach pressure -> Soothes crying"
            else -> 
                "Medicine Administered -> Absorbed into bloodstream -> Targets affected area -> Provides symptom relief"
        }
    }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            val amPm = if (hourOfDay >= 12) "PM" else "AM"
            val formattedHour = if (hourOfDay % 12 == 0) 12 else hourOfDay % 12
            val formattedMinute = String.format("%02d", minute)
            timeStr = "$formattedHour:$formattedMinute $amPm"
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        false
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Medicine") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.DarkGray
                )
            )
        },
        containerColor = Color(0xFFF9F9F9)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Enter Medicine Details", style = MaterialTheme.typography.titleLarge)

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Medicine Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = timeStr,
                onValueChange = { },
                label = { Text("Time for Reminder") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    TextButton(onClick = { timePickerDialog.show() }) {
                        Text("Select")
                    }
                }
            )

            OutlinedTextField(
                value = dose,
                onValueChange = { dose = it },
                label = { Text("Dose Prescribed (e.g., 5ml, 1 tablet)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = duration,
                onValueChange = { duration = it },
                label = { Text("Duration (e.g., 5 days)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = frequency,
                onValueChange = { frequency = it },
                label = { Text("Frequency (e.g., Twice a day)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        val generatedFlowchart = getAutomaticFlowchart(name)
                        val medicine = Medicine(
                            id = UUID.randomUUID().toString(),
                            name = name,
                            time = timeStr,
                            dose = dose,
                            duration = duration,
                            frequency = frequency,
                            flowchartInfo = generatedFlowchart,
                            lastAdministeredTime = 0L
                        )
                        val updatedList = child.medicines + medicine
                        onUpdateChild(child.copy(medicines = updatedList))
                        
                        // Schedule Alarm if time is provided
                        if (timeStr.isNotBlank()) {
                            MedicineAlarmScheduler.scheduleAlarm(context, medicine)
                        }
                        
                        onMedicineAdded(medicine.id)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = name.isNotBlank()
            ) {
                Text("Save Medicine")
            }
        }
    }
}
