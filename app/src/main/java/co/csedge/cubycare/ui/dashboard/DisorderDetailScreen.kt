package co.csedge.cubycare.ui.dashboard

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import co.csedge.cubycare.data.model.Child
import co.csedge.cubycare.data.repository.DisorderRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisorderDetailScreen(child: Child, onBack: () -> Unit) {
    val context = LocalContext.current
    val disorder = remember { DisorderRepository.getDisorderInfo(child.geneticIssues) }
    var showDoctorDialog by remember { mutableStateOf(false) }
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
        
        val query = "${disorder.pediatricianSpecialization} near me"
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(disorder.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFFF9C4), // Light Lemon
                    titleContentColor = Color.DarkGray
                )
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // General Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(disorder.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Duration: ${disorder.duration}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Curable: ${if (disorder.isCurable) "Yes" else "No"}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = if (disorder.isCurable) Color(0xFF4CAF50) else Color(0xFFE53935))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Reasons / Causes:", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(disorder.reasons, style = MaterialTheme.typography.bodyLarge)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Symptoms & Precautions
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)), // Light Green
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Main Symptoms", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(disorder.mainSymptoms, style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Precautions & Avoidance", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color(0xFFC62828)) // Red for avoidance
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(disorder.avoidance, style = MaterialTheme.typography.bodyLarge)
                }
            }

            // Medication & Disclaimer
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)), // Light Orange
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Warning, contentDescription = null, tint = Color(0xFFF57C00))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Medication Information", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color(0xFFF57C00))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(disorder.simpleMedication, style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "DISCLAIMER: This app does not provide complex medical prescriptions. Always consult your doctor before administering any medication.",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Suggestable Pediatrician
            Text("Suggested Specialist", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDoctorDialog = true },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)), // Light Blue
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Person, contentDescription = null, tint = Color(0xFF1976D2))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(disorder.pediatricianName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(disorder.pediatricianSpecialization, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Place, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(disorder.pediatricianLocation, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Flowchart
            Text("How it affects the body", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Spacer(modifier = Modifier.height(16.dp))
            
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                disorder.flowchartSteps.forEachIndexed { index, step ->
                    DisorderFlowchartStep(
                        stepNumber = (index + 1).toString(),
                        title = step.title,
                        description = step.description
                    )
                    if (index < disorder.flowchartSteps.size - 1) {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowDown,
                            contentDescription = "Next",
                            modifier = Modifier.padding(vertical = 8.dp).size(32.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }

        if (showDoctorDialog) {
            AlertDialog(
                onDismissRequest = { showDoctorDialog = false },
                title = { Text(disorder.pediatricianName, fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        Text(disorder.pediatricianSpecialization, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(Icons.Filled.Place, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Searching your local area for top-rated specialists.", style = MaterialTheme.typography.bodyMedium)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(Icons.Filled.Info, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Timings:\n${disorder.hospitalTimings}", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        val hasFine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        val hasCoarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        
                        if (hasFine || hasCoarse) {
                            val query = "${disorder.pediatricianSpecialization} near me"
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
                        showDoctorDialog = false
                    }) {
                        Icon(Icons.Filled.Place, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Find Nearby Specialists")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDoctorDialog = false }) { Text("Close") }
                }
            )
        }
    }
}

@Composable
fun DisorderFlowchartStep(stepNumber: String, title: String, description: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFCE4EC)), // Light Pink
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stepNumber,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray,
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray.copy(alpha = 0.8f)
                )
            }
        }
    }
}
