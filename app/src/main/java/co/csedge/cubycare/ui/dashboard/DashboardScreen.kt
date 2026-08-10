package co.csedge.cubycare.ui.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.csedge.cubycare.data.model.Child
import co.csedge.cubycare.ui.components.ChildProfileAvatar

import co.csedge.cubycare.utils.tr
import co.csedge.cubycare.utils.currentVitalsAgeRange

@Composable
fun TodayCard(
    title: String,
    subtitle: String,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconRes: Int? = null,
    vectorIcon: ImageVector? = null,
    iconTint: Color = Color.Unspecified
) {
    Card(
        modifier = modifier
            .height(130.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (iconRes != null) {
                    Image(
                        painter = painterResource(id = iconRes),
                        contentDescription = title,
                        modifier = Modifier.size(36.dp),
                        contentScale = ContentScale.Fit
                    )
                } else if (vectorIcon != null) {
                    Icon(
                        imageVector = vectorIcon,
                        contentDescription = title,
                        tint = if (iconTint != Color.Unspecified) iconTint else MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayDashboard(
    children: List<Child>,
    onVaccineClick: (String, String?) -> Unit,
    onFeedingClick: (String) -> Unit,
    onMedicineClick: (String, String?) -> Unit,
    onSleepClick: (String, String) -> Unit,
    onActivityClick: (String, String) -> Unit,
    onAppointmentClick: (String) -> Unit
) {
    val realChildren = remember(children) {
        children.filter { it.id != "default_general_baby" && !it.name.contains("General Baby") && it.dateOfBirthMillis != 0L }
    }

    var selectedChildId by rememberSaveable { mutableStateOf("all") }

    val selectedChildIndex = remember(selectedChildId, realChildren) {
        if (selectedChildId == "all") 0
        else {
            val idx = realChildren.indexOfFirst { it.id == selectedChildId }
            if (idx >= 0) idx + 1 else 0
        }
    }

    val activeChild = if (selectedChildIndex > 0 && selectedChildIndex <= realChildren.size) {
        realChildren[selectedChildIndex - 1]
    } else {
        null
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = tr("todays_schedule"),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (realChildren.isNotEmpty()) {
            if (realChildren.size > 1) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedChildId == "all",
                        onClick = { selectedChildId = "all" },
                        label = { Text("👶 All Children (${realChildren.size})", fontWeight = FontWeight.Bold) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = Color.White
                        )
                    )
                    realChildren.forEachIndexed { index, child ->
                        FilterChip(
                            selected = selectedChildId == child.id,
                            onClick = { selectedChildId = child.id },
                            label = { Text(child.name, fontWeight = FontWeight.SemiBold) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            val upcomingVaccines = if (activeChild != null) {
                activeChild.vaccines.filter { it.administeredDateMillis == null }
            } else {
                realChildren.flatMap { it.vaccines }.filter { it.administeredDateMillis == null }
            }
            val vaccineText = if (upcomingVaccines.isNotEmpty()) {
                if (activeChild != null) "Next: ${upcomingVaccines.first().name}" else "Next: ${upcomingVaccines.first().name}"
            } else "Vaccines up to date!"

            val foodText = if (activeChild != null) {
                if (activeChild.foodDiary.isNotEmpty()) "Last: ${activeChild.foodDiary.last().food}" else "Log ${activeChild.name}'s food"
            } else {
                val totalFoodLogs = realChildren.sumOf { it.foodDiary.size }
                if (totalFoodLogs > 0) "$totalFoodLogs food log(s)" else "Log food diary"
            }

            val activeMeds = if (activeChild != null) {
                activeChild.medicines.filter { it.dose.isNotBlank() }
            } else {
                realChildren.flatMap { it.medicines }.filter { it.dose.isNotBlank() }
            }
            val medText = if (activeMeds.isNotEmpty()) "${activeMeds.size} active medicine(s)" else "No active medication"

            val targetChildId = activeChild?.id ?: realChildren.firstOrNull()?.id ?: ""
            val targetAgeRange = activeChild?.currentVitalsAgeRange ?: realChildren.firstOrNull()?.currentVitalsAgeRange ?: "0-6 Months"
            val playActivities = co.csedge.cubycare.data.repository.CubyJoyProvider.getActivitiesForAge(targetAgeRange)
            val playText = if (playActivities.isNotEmpty()) {
                val firstAct = playActivities.first().name
                if (activeChild != null) "${activeChild.name}: $firstAct" else "Try: $firstAct"
            } else "Fun activities & games"

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Row 1: Vaccines & Feeding
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TodayCard(
                        title = tr("vaccines"),
                        subtitle = vaccineText,
                        color = Color(0xFFE3F2FD),
                        iconRes = co.csedge.cubycare.R.drawable.vaccine_block_icon,
                        onClick = { onVaccineClick(targetChildId, upcomingVaccines.firstOrNull()?.id) },
                        modifier = Modifier.weight(1f)
                    )

                    TodayCard(
                        title = tr("food_diary"),
                        subtitle = foodText,
                        color = Color(0xFFE4F2D8),
                        iconRes = co.csedge.cubycare.R.drawable.nutrition_food_diary,
                        onClick = { onFeedingClick(targetChildId) },
                        modifier = Modifier.weight(1f)
                    )
                }

                // Row 2: Medicines & Sleep
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TodayCard(
                        title = tr("medicines"),
                        subtitle = medText,
                        color = Color(0xFFE0F7FA),
                        iconRes = co.csedge.cubycare.R.drawable.medicine_tracker,
                        onClick = { onMedicineClick(targetChildId, null) },
                        modifier = Modifier.weight(1f)
                    )

                    TodayCard(
                        title = tr("sleep_naps"),
                        subtitle = if (activeChild != null) "Sleep (${activeChild.ageFormatted})" else "Recommended sleep",
                        color = Color(0xFFE8EAF6),
                        iconRes = co.csedge.cubycare.R.drawable.cuby_naps_icon,
                        onClick = { onSleepClick(targetChildId, targetAgeRange) },
                        modifier = Modifier.weight(1f)
                    )
                }

                // Row 3: Activities & Appointments
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TodayCard(
                        title = tr("play_joy"),
                        subtitle = playText,
                        color = Color(0xFFFFE0B2),
                        iconRes = co.csedge.cubycare.R.drawable.cuby_joy_icon,
                        onClick = { onActivityClick(targetChildId, targetAgeRange) },
                        modifier = Modifier.weight(1f)
                    )

                    TodayCard(
                        title = tr("appointments"),
                        subtitle = if (activeChild != null) "${activeChild.name}'s checkups" else "Doctor checkups",
                        color = Color(0xFFF3E5F5),
                        vectorIcon = Icons.Filled.Add,
                        iconTint = Color(0xFF7B1FA2),
                        onClick = { onAppointmentClick(targetChildId) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

            Spacer(modifier = Modifier.height(12.dp))
            val context = androidx.compose.ui.platform.LocalContext.current
            Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                // Option 1: Find Nearby Pediatricians & Children's Hospitals (Google Maps)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { co.csedge.cubycare.utils.HospitalUtils.openNearbyHospitals(context) },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Place,
                            contentDescription = null,
                            tint = Color(0xFF2E7D32),
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "1️⃣ Find Nearby Children's Hospitals & Clinics",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1B5E20)
                            )
                            Text(
                                text = "Open directly in Google Maps for live directions & locations",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF2E7D32)
                            )
                        }
                        Button(
                            onClick = { co.csedge.cubycare.utils.HospitalUtils.openNearbyHospitals(context) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text("Open Maps 🗺️", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }

                // Option 2 (Second Option): Malla Reddy Narayana Hospital Pediatrics & Neonatology
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            co.csedge.cubycare.utils.HospitalUtils.openPediatricsSpecialty(context)
                        },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEDE7F6)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = null,
                            tint = Color(0xFF6A1B9A),
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "2️⃣ Malla Reddy Narayana Hospital",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4A148C)
                            )
                            Text(
                                text = "Speciality: Pediatrics & Neonatology Department",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF6A1B9A)
                            )
                        }
                        OutlinedButton(
                            onClick = {
                                co.csedge.cubycare.utils.HospitalUtils.openPediatricsSpecialty(context)
                            },
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text("Visit Site", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    children: List<Child>,
    onAddChildClick: () -> Unit,
    onChildClick: (String) -> Unit,
    onDeleteChild: (String) -> Unit,
    onGlobalMedicineTrackerClick: () -> Unit,
    onVaccineClick: (childId: String, vaccineId: String?) -> Unit,
    onFeedingClick: (String) -> Unit,
    onMedicineClick: (childId: String, medicineId: String?) -> Unit,
    onSleepClick: (childId: String, ageRange: String) -> Unit,
    onActivityClick: (childId: String, ageRange: String) -> Unit,
    onAppointmentClick: (String) -> Unit,
    onAiChatClick: () -> Unit = {},
    bottomBar: @Composable () -> Unit = {}
) {
    var childToDelete by remember { mutableStateOf<Child?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = co.csedge.cubycare.R.drawable.app_logo),
                            contentDescription = "CubyCare Logo",
                            modifier = Modifier.size(36.dp).padding(end = 8.dp)
                        )
                        Text(
                            text = "CubyCare",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onAiChatClick) {
                        Image(
                            painter = painterResource(id = co.csedge.cubycare.R.drawable.cuby_ai_robot_mother_baby),
                            contentDescription = tr("nav_ai_chat"),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(10.dp))
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        bottomBar = bottomBar,
        floatingActionButton = {
            FloatingActionButton(onClick = onAddChildClick, containerColor = MaterialTheme.colorScheme.primary) {
                Icon(Icons.Filled.Add, contentDescription = "Add Child", tint = Color.White)
            }
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = co.csedge.cubycare.R.drawable.premium_nursery_bg),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                alpha = 0.65f
            )

            if (children.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tr("no_child"),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = tr("children_profiles"),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxWidth().heightIn(max = 300.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(children) { child ->
                            val isGeneralProfile = child.id == "default_general_baby" || child.name.contains("General Baby") || child.dateOfBirthMillis == 0L
                            Card(
                                modifier = Modifier
                                    .size(140.dp)
                                    .clickable { onChildClick(child.id) },
                                shape = RoundedCornerShape(24.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                            ) {
                                Box(modifier = Modifier.fillMaxSize()) {
                                    if (!isGeneralProfile) {
                                        IconButton(
                                            onClick = { childToDelete = child },
                                            modifier = Modifier.align(Alignment.TopEnd).size(36.dp).padding(4.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Delete,
                                                contentDescription = "Delete",
                                                tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f),
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                    Column(
                                        modifier = Modifier.fillMaxSize().padding(8.dp),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        ChildProfileAvatar(
                                            profileImageUri = child.profileImageUri,
                                            name = child.name,
                                            size = 56.dp
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = child.name,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = child.ageFormatted,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    if (children.isNotEmpty()) {
                        val firstChild = children.first()
                        val firstChildId = firstChild.id
                        Spacer(modifier = Modifier.height(24.dp))
                        TodayDashboard(
                            children = children,
                            onVaccineClick = { childId, vaccineId -> onVaccineClick(childId, vaccineId) },
                            onFeedingClick = { childId -> onFeedingClick(childId) },
                            onMedicineClick = { childId, medicineId -> onMedicineClick(childId, medicineId) },
                            onSleepClick = { childId, ageRange -> onSleepClick(childId, ageRange) },
                            onActivityClick = { childId, ageRange -> onActivityClick(childId, ageRange) },
                            onAppointmentClick = { onAppointmentClick(children.first().id) }
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // AI Assistant Banner Card (Placed at Bottom)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                            .clickable { onAiChatClick() },
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.95f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = co.csedge.cubycare.R.drawable.cuby_ai_robot_mother_baby),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = tr("ai_banner_title"),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Text(
                                    text = tr("ai_banner_subtitle"),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                )
                            }
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }

        if (childToDelete != null) {
            AlertDialog(
                onDismissRequest = { childToDelete = null },
                title = { Text("Delete Profile") },
                text = { Text("Are you sure you want to delete ${childToDelete?.name}'s profile?") },
                confirmButton = {
                    TextButton(onClick = {
                        childToDelete?.let { onDeleteChild(it.id) }
                        childToDelete = null
                    }) {
                        Text(tr("delete"), color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { childToDelete = null }) {
                        Text(tr("cancel"))
                    }
                }
            )
        }
    }
}
