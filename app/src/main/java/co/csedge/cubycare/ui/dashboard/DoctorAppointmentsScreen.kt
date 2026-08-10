package co.csedge.cubycare.ui.dashboard

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.csedge.cubycare.data.model.Child
import co.csedge.cubycare.data.repository.AppointmentRepository
import co.csedge.cubycare.data.repository.BookedAppointment
import co.csedge.cubycare.utils.HospitalUtils
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorAppointmentsScreen(
    child: Child,
    onBack: () -> Unit,
    bottomBar: @Composable () -> Unit = {}
) {
    val context = LocalContext.current
    val repository = remember { AppointmentRepository(context) }
    var appointments by remember { mutableStateOf(repository.getAppointmentsForChild(child.id)) }

    // Top Tabs: 0 -> Doctor Appointments & Reminders, 1 -> Malla Reddy Narayana Hospital
    var selectedTab by remember { mutableIntStateOf(0) }

    // Reminder Form / Dialog state
    var showAddDialog by remember { mutableStateOf(false) }
    var doctorNameInput by remember { mutableStateOf("") }
    var hospitalNameInput by remember { mutableStateOf("Malla Reddy Narayana Hospital") }
    var dateInput by remember { mutableStateOf("") }
    var timeInput by remember { mutableStateOf("") }
    var notesInput by remember { mutableStateOf("") }

    // Date & Time Pickers
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            dateInput = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            val amPm = if (hourOfDay >= 12) "PM" else "AM"
            val hour12 = if (hourOfDay % 12 == 0) 12 else hourOfDay % 12
            timeInput = String.format("%02d:%02d %s", hour12, minute, amPm)
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        false
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Doctor Appointments", fontWeight = FontWeight.Bold)
                        Text("${child.name}'s Schedule & Reminders", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.DarkGray
                )
            )
        },
        bottomBar = bottomBar,
        floatingActionButton = {
            if (selectedTab == 0) {
                ExtendedFloatingActionButton(
                    onClick = { showAddDialog = true },
                    icon = { Icon(Icons.Filled.Add, contentDescription = "Set Reminder") },
                    text = { Text("Set Appointment Reminder") },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        containerColor = Color(0xFFF9F9F9)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("📅 Appointments & Reminders", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("🏥 Malla Reddy Hospital", fontWeight = FontWeight.Bold) }
                )
            }

            when (selectedTab) {
                // TAB 0: DOCTOR APPOINTMENTS & REMINDERS
                0 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // 1. Nearby Hospitals Google Maps Intent Launcher Card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Place,
                                    contentDescription = null,
                                    tint = Color(0xFF2E7D32),
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "📍 Nearby Children's Hospitals & Clinics",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1B5E20)
                                    )
                                    Text(
                                        text = "Open directly in Google Maps for live directions & ratings",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFF2E7D32)
                                    )
                                }
                                Button(
                                    onClick = { HospitalUtils.openNearbyHospitals(context) },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Open Maps", fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                        }

                        // 2. Scheduled Appointments & Reminders Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "⏰ Booked Appointments & Reminders",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.DarkGray
                            )
                            IconButton(onClick = { showAddDialog = true }) {
                                Icon(Icons.Filled.Add, contentDescription = "Add", tint = MaterialTheme.colorScheme.primary)
                            }
                        }

                        if (appointments.isEmpty()) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Notifications,
                                        contentDescription = null,
                                        tint = Color.LightGray,
                                        modifier = Modifier.size(48.dp)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "No doctor appointments set yet",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Gray
                                    )
                                    Text(
                                        text = "Tap 'Set Appointment Reminder' below to schedule a checkup reminder.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray
                                    )
                                }
                            }
                        } else {
                            appointments.forEach { appointment ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (appointment.isMallaReddyHospital) Color(0xFFF3E5F5) else Color.White
                                    ),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Filled.CheckCircle,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.primary,
                                                    modifier = Modifier.size(24.dp)
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = appointment.doctorName.ifBlank { "Doctor Appointment" },
                                                    style = MaterialTheme.typography.titleMedium,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                            IconButton(
                                                onClick = {
                                                    repository.deleteAppointment(child.id, appointment.id)
                                                    appointments = repository.getAppointmentsForChild(child.id)
                                                    Toast.makeText(context, "Reminder deleted", Toast.LENGTH_SHORT).show()
                                                }
                                            ) {
                                                Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = Color.Red.copy(alpha = 0.7f))
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "🏥 ${appointment.hospitalName}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.DarkGray
                                        )
                                        Text(
                                            text = "📅 ${appointment.appointmentDate} at ${appointment.appointmentTime}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.Bold
                                        )

                                        if (appointment.notes.isNotBlank()) {
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(
                                                text = "📝 Note: ${appointment.notes}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color.Gray
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(70.dp))
                    }
                }

                // TAB 1: MALLA REDDY HOSPITAL SPECIALTY
                1 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFEDE7F6)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Text(
                                    text = "🏥 Malla Reddy Narayana Multispeciality Hospital",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF4A148C)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Department of Pediatrics & Neonatology",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF7B1FA2)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "Specialized pediatric healthcare, Level-III NICU/PICU care, pediatric surgery, newborn care, vaccinations, and developmental consultations.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.DarkGray
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = { HospitalUtils.openPediatricsSpecialty(context) },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7B1FA2)),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Book Appointment at Malla Reddy Pediatrics 🌐", fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Reminder Putter Dialog
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("⏰ Set Doctor Appointment Reminder", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = doctorNameInput,
                        onValueChange = { doctorNameInput = it },
                        label = { Text("Doctor / Specialist Name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = hospitalNameInput,
                        onValueChange = { hospitalNameInput = it },
                        label = { Text("Hospital / Clinic Name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = { datePickerDialog.show() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(dateInput.ifBlank { "Select Date 📅" })
                        }
                        OutlinedButton(
                            onClick = { timePickerDialog.show() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(timeInput.ifBlank { "Select Time ⏰" })
                        }
                    }

                    OutlinedTextField(
                        value = notesInput,
                        onValueChange = { notesInput = it },
                        label = { Text("Notes / Purpose (e.g. Checkup, Vaccines)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (dateInput.isBlank() || timeInput.isBlank()) {
                            Toast.makeText(context, "Please select Date and Time", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        val newAppointment = BookedAppointment(
                            childId = child.id,
                            doctorName = doctorNameInput.ifBlank { "Pediatric Specialist" },
                            hospitalName = hospitalNameInput.ifBlank { "Malla Reddy Narayana Hospital" },
                            appointmentDate = dateInput,
                            appointmentTime = timeInput,
                            notes = notesInput,
                            isMallaReddyHospital = hospitalNameInput.contains("Malla Reddy", ignoreCase = true)
                        )
                        repository.saveAppointment(newAppointment)
                        appointments = repository.getAppointmentsForChild(child.id)
                        Toast.makeText(context, "Appointment reminder saved successfully! ⏰", Toast.LENGTH_SHORT).show()
                        showAddDialog = false
                    }
                ) {
                    Text("Save Reminder")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
