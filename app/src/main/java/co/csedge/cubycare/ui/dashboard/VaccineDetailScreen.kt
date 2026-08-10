package co.csedge.cubycare.ui.dashboard

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import co.csedge.cubycare.data.model.Vaccine
import co.csedge.cubycare.data.repository.StorageRepository
import co.csedge.cubycare.utils.VaccineScanner
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import co.csedge.cubycare.utils.tr

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaccineDetailScreen(
    child: Child,
    vaccine: Vaccine,
    onBack: () -> Unit,
    onUpdateChild: (Child) -> Unit,
    onFlowchartClick: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val storageRepository = remember { StorageRepository() }
    
    var isUploading by remember { mutableStateOf(false) }
    var scannedDateText by remember { mutableStateOf<String?>(null) }
    var showVaccineCenterDialog by remember { mutableStateOf(false) }
    
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )
    
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    
    fun launchMapIntent(uriString: String) {
        val uri = Uri.parse(uriString)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.google.android.apps.maps")
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            val fallbackIntent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(fallbackIntent)
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true || 
                      permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        
        val query = "vaccination centers near me"
        var uriString = "geo:0,0?q=${Uri.encode(query)}"
        
        if (granted) {
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        uriString = "geo:${location.latitude},${location.longitude}?q=${Uri.encode(query)}"
                    }
                    launchMapIntent(uriString)
                }
            } catch (e: SecurityException) {
                launchMapIntent(uriString)
            }
        } else {
            launchMapIntent(uriString)
        }
    }
    
    val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    val isCompleted = vaccine.administeredDateMillis != null
    val isOverdue = !isCompleted && vaccine.nextDueDateMillis != null && vaccine.nextDueDateMillis < System.currentTimeMillis()
    val isDueSoon = !isCompleted && !isOverdue && vaccine.nextDueDateMillis != null && (vaccine.nextDueDateMillis - System.currentTimeMillis() < 30L * 24 * 60 * 60 * 1000)

    val statusColor = when {
        isCompleted -> MaterialTheme.colorScheme.primary
        isOverdue -> MaterialTheme.colorScheme.error
        isDueSoon -> Color(0xFFF57C00) // Orange
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    val statusText = when {
        isCompleted -> "COMPLETED"
        isOverdue -> "OVERDUE"
        isDueSoon -> "DUE SOON"
        else -> "UPCOMING"
    }

    // Image Picker for Certificate Upload
    val certPickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            scope.launch {
                isUploading = true
                val url = storageRepository.uploadVaccineCertificate(child.id, vaccine.id, it)
                val updatedList = child.vaccines.map { v ->
                    if (v.id == vaccine.id) v.copy(certificateUrl = url, administeredDateMillis = System.currentTimeMillis()) else v
                }
                onUpdateChild(child.copy(vaccines = updatedList))
                isUploading = false
            }
        }
    }

    // Image Picker for Smart Scan (OCR)
    val scanPickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            scope.launch {
                isUploading = true
                val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, it))
                } else {
                    MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                }
                val dateFound = VaccineScanner.scanForAdministeredDate(bitmap)
                
                if (dateFound != null) {
                    scannedDateText = "Scanned Date: ${formatter.format(Date(dateFound))}"
                    val updatedList = child.vaccines.map { v ->
                        if (v.id == vaccine.id) v.copy(administeredDateMillis = dateFound) else v
                    }
                    onUpdateChild(child.copy(vaccines = updatedList))
                } else {
                    scannedDateText = "No date detected. Please enter manually."
                }
                isUploading = false
            }
        }
    }

    val cleanName = remember(vaccine.name) { co.csedge.cubycare.data.repository.VaccineScheduleProvider.sanitizeVaccineName(vaccine.name) }
    val eduInfo = remember(cleanName) { co.csedge.cubycare.data.repository.VaccineKnowledgeProvider.getInfoForVaccine(cleanName) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(cleanName) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFFC0CB), // Baby Pink
                    titleContentColor = Color.DarkGray
                )
            )
        },
        containerColor = Color(0xFFFFC0CB) // Baby Pink
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Status Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = cleanName,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray,
                            modifier = Modifier.weight(1f)
                        )
                        if (isOverdue) {
                            Icon(Icons.Filled.Warning, contentDescription = "Overdue", tint = Color.Red, modifier = Modifier.size(32.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Shields against: ${eduInfo.diseasePrevented}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Surface(
                        color = statusColor.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = statusText,
                            color = statusColor,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Dose Number: ${vaccine.doseNumber}", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Recommended Age: ${tr(vaccine.recommendedAge)}", style = MaterialTheme.typography.bodyLarge)
                    
                    if (isCompleted) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Administered on: ${formatter.format(Date(vaccine.administeredDateMillis!!))}",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    } else if (vaccine.nextDueDateMillis != null) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.DateRange, contentDescription = null, tint = statusColor)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Due Date: ${formatter.format(Date(vaccine.nextDueDateMillis))}",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = statusColor
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Educational Section
            Button(
                onClick = onFlowchartClick,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFADD8E6)) // Light Blue
            ) {
                Icon(Icons.Filled.Info, contentDescription = null, tint = Color.DarkGray)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Why should it be taken? (How it works)", color = Color.DarkGray, fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            if (!isCompleted) {
                // Action Buttons
                Text("Log this Vaccine", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(16.dp))
                
                if (isUploading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Processing...")
                } else {
                    if (scannedDateText != null) {
                        Text(scannedDateText!!, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    
                    OutlinedButton(onClick = { scanPickerLauncher.launch("image/*") }, modifier = Modifier.fillMaxWidth()) {
                        Icon(Icons.Filled.Search, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Smart Scan Card (OCR)")
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedButton(onClick = { certPickerLauncher.launch("image/*") }, modifier = Modifier.fillMaxWidth()) {
                        Icon(Icons.Filled.Share, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Upload Certificate")
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            showDatePicker = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Filled.Edit, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Log Manually (Select Date)")
                    }
                }
            } else if (vaccine.certificateUrl != null) {
                OutlinedButton(onClick = { /* Could open URL */ }, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Filled.Star, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("View Attached Certificate")
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Map Intent
            Button(
                onClick = { showVaccineCenterDialog = true },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Icon(Icons.Filled.Place, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Find Nearby Vaccination Centers", fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }

        if (showVaccineCenterDialog) {
            AlertDialog(
                onDismissRequest = { showVaccineCenterDialog = false },
                title = { Text("Vaccination Centers", fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        Text("Government clinics generally operate:", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                        Text("Mon - Sat: 9:00 AM - 4:00 PM\n(Closed on Public Holidays)", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Private pediatric clinics operate:", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                        Text("Mon - Sat: 10:00 AM - 8:00 PM", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(Icons.Filled.Info, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Timings may vary. Click below to search your local area on Google Maps for accurate places and timings.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        val hasFine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        val hasCoarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        
                        if (hasFine || hasCoarse) {
                            val query = "vaccination centers near me"
                            var uriString = "geo:0,0?q=${Uri.encode(query)}"
                            try {
                                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                                    if (location != null) {
                                        uriString = "geo:${location.latitude},${location.longitude}?q=${Uri.encode(query)}"
                                    }
                                    launchMapIntent(uriString)
                                }
                            } catch (e: SecurityException) {
                                launchMapIntent(uriString)
                            }
                        } else {
                            locationPermissionLauncher.launch(
                                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                            )
                        }
                        showVaccineCenterDialog = false
                    }) {
                        Icon(Icons.Filled.Place, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Search on Map")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showVaccineCenterDialog = false }) { Text("Close") }
                }
            )
        }

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        val selectedDate = datePickerState.selectedDateMillis ?: System.currentTimeMillis()
                        val updatedList = child.vaccines.map { v ->
                            if (v.id == vaccine.id) v.copy(administeredDateMillis = selectedDate) else v
                        }
                        onUpdateChild(child.copy(vaccines = updatedList))
                        showDatePicker = false
                    }) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text(tr("cancel"))
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}
