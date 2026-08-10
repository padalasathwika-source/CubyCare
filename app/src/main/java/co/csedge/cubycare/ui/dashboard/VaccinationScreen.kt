package co.csedge.cubycare.ui.dashboard

import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import co.csedge.cubycare.data.model.Child
import co.csedge.cubycare.data.model.Vaccine
import co.csedge.cubycare.data.repository.StorageRepository
import co.csedge.cubycare.data.repository.VaccineScheduleProvider
import co.csedge.cubycare.utils.VaccineScanner
import co.csedge.cubycare.ui.components.ChildProfileAvatar
import co.csedge.cubycare.utils.HospitalUtils
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import co.csedge.cubycare.utils.tr

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaccinationScreen(
    child: Child,
    allChildren: List<Child> = emptyList(),
    onSelectChild: ((Child) -> Unit)? = null,
    onBack: () -> Unit,
    onUpdateChild: (Child) -> Unit,
    onVaccineClick: (String) -> Unit,
    bottomBar: @Composable () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val storageRepository = remember { StorageRepository() }

    var selectedTab by remember { mutableStateOf(0) }
    var filterType by remember { mutableStateOf("ALL") } // ALL, MANDATORY, OPTIONAL
    val tabs = listOf("Upcoming / Due", "Completed")

    // Pre-populate IAP vaccines if empty and sanitize names
    val vaccines = remember(child.vaccines) {
        val list = if (child.vaccines.isEmpty()) {
            val iapSchedule = VaccineScheduleProvider.generateIAPSchedule(child.dateOfBirthMillis)
            onUpdateChild(child.copy(vaccines = iapSchedule))
            iapSchedule
        } else {
            child.vaccines
        }
        list.map { v ->
            v.copy(name = VaccineScheduleProvider.sanitizeVaccineName(v.name))
        }
    }

    // Apply Filter
    val filteredVaccines = if (filterType == "ALL") vaccines else vaccines.filter { it.type == filterType }

    val upcoming = filteredVaccines.filter { it.administeredDateMillis == null }.sortedBy { it.nextDueDateMillis }
    val completed = filteredVaccines.filter { it.administeredDateMillis != null }.sortedByDescending { it.administeredDateMillis }

    var vaccineToMark by remember { mutableStateOf<Vaccine?>(null) }
    var isUploading by remember { mutableStateOf(false) }
    var scannedDateText by remember { mutableStateOf<String?>(null) }

    // Image Picker for Certificate Upload
    val certPickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            vaccineToMark?.let { vac ->
                scope.launch {
                    isUploading = true
                    val url = storageRepository.uploadVaccineCertificate(child.id, vac.id, it)
                    val updatedList = child.vaccines.map { 
                        if (it.id == vac.id) it.copy(certificateUrl = url, administeredDateMillis = System.currentTimeMillis()) else it
                    }
                    onUpdateChild(child.copy(vaccines = updatedList))
                    isUploading = false
                    vaccineToMark = null
                }
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
                    val format = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                    scannedDateText = "Scanned Date: ${format.format(Date(dateFound))}"
                    
                    // Mark it
                    vaccineToMark?.let { vac ->
                        val updatedList = child.vaccines.map { 
                            if (it.id == vac.id) it.copy(administeredDateMillis = dateFound) else it
                        }
                        onUpdateChild(child.copy(vaccines = updatedList))
                        vaccineToMark = null
                    }
                } else {
                    scannedDateText = "No date detected. Please enter manually."
                }
                isUploading = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${child.name}'s Vaccinations") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        bottomBar = bottomBar,
        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = co.csedge.cubycare.R.drawable.mother_baby_vaccine_bg),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                alpha = 0.20f
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Calendar Strip
                if (upcoming.isNotEmpty()) {
                    VaccineCalendarStrip(upcoming)
                }

                // Filters
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                    FilterChip(selected = filterType == "ALL", onClick = { filterType = "ALL" }, label = { Text("All") })
                    FilterChip(selected = filterType == "MANDATORY", onClick = { filterType = "MANDATORY" }, label = { Text("UIP/Mandatory") })
                    FilterChip(selected = filterType == "OPTIONAL", onClick = { filterType = "OPTIONAL" }, label = { Text("IAP Optional") })
                }

                TabRow(selectedTabIndex = selectedTab) {
                    tabs.forEachIndexed { index, title ->
                        Tab(selected = selectedTab == index, onClick = { selectedTab = index }, text = { Text(title) })
                    }
                }

                Box(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
                    if (selectedTab == 0) {
                        if (upcoming.isEmpty()) {
                            EmptyState("All caught up! No upcoming vaccines.")
                        } else {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                modifier = Modifier.fillMaxSize().padding(top = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                items(upcoming) { vaccine ->
                                    VaccineSquareBlock(vaccine, onClick = { onVaccineClick(vaccine.id) })
                                }
                            }
                        }
                    } else {
                        if (completed.isEmpty()) {
                            EmptyState("No vaccines have been marked as completed yet.")
                        } else {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                modifier = Modifier.fillMaxSize().padding(top = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                items(completed) { vaccine ->
                                    VaccineSquareBlock(vaccine, onClick = { onVaccineClick(vaccine.id) })
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VaccineCalendarStrip(upcoming: List<Vaccine>) {
    val formatter = SimpleDateFormat("MMM yyyy", Locale.getDefault())
    val grouped = upcoming.groupBy { 
        it.nextDueDateMillis?.let { ms -> formatter.format(Date(ms)) } ?: "Unknown"
    }

    Column(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surfaceVariant).padding(vertical = 12.dp)) {
        Text("Upcoming Calendar", modifier = Modifier.padding(horizontal = 16.dp), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(grouped.keys.toList()) { monthStr ->
                val count = grouped[monthStr]?.size ?: 0
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(monthStr, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                        Text("$count Vaccine(s)", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyState(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(message, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun VaccineSquareBlock(vaccine: Vaccine, onClick: () -> Unit) {
    val isOverdue = vaccine.nextDueDateMillis != null && vaccine.nextDueDateMillis < System.currentTimeMillis() && vaccine.administeredDateMillis == null
    val isCompleted = vaccine.administeredDateMillis != null

    val hue = Math.abs(vaccine.name.hashCode()) % 360f
    val color = Color.hsl(hue = hue, saturation = 0.4f, lightness = 0.90f)

    Card(
        modifier = Modifier
            .size(140.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
                Text(
                    text = tr(vaccine.name),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (isOverdue) {
                    Icon(Icons.Filled.Warning, contentDescription = "Overdue", tint = Color.Red, modifier = Modifier.size(24.dp))
                } else if (isCompleted) {
                    Icon(Icons.Filled.Check, contentDescription = "Completed", tint = Color(0xFF4CAF50), modifier = Modifier.size(24.dp))
                }
            }
        }
    }
}
