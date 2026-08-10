package co.csedge.cubycare.ui.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.csedge.cubycare.data.model.Child
import co.csedge.cubycare.data.model.GrowthRecord
import co.csedge.cubycare.ui.components.ChildProfileAvatar
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import java.text.SimpleDateFormat
import java.util.*
import co.csedge.cubycare.utils.tr

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditChildScreen(
    child: Child,
    onSaveChild: (Child) -> Unit,
    onBack: () -> Unit
) {
    val isGeneralProfile = child.id == "default_general_baby" || child.dateOfBirthMillis == 0L || child.name.contains("General Baby")

    if (isGeneralProfile) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("General Baby Profile") },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "ℹ️ Read-Only Profile",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Cuby General Baby 👶 is a default reference profile provided by the app for all age brackets and cannot be modified.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = onBack,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Go Back")
                        }
                    }
                }
            }
        }
        return
    }

    val sdf = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    var name by remember { mutableStateOf(child.name) }
    var profileImageUri by remember { mutableStateOf(child.profileImageUri) }
    var dobStr by remember {
        mutableStateOf(
            if (child.dateOfBirthMillis > 0L) sdf.format(Date(child.dateOfBirthMillis)) else ""
        )
    }
    var dobErrorMsg by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    val fiveYearsAgoMillis = remember {
        val cal = Calendar.getInstance()
        cal.add(Calendar.YEAR, -5)
        cal.timeInMillis
    }
    val todayMillis = remember { System.currentTimeMillis() }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = if (child.dateOfBirthMillis > 0L) child.dateOfBirthMillis else System.currentTimeMillis(),
        initialDisplayMode = DisplayMode.Picker,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis in fiveYearsAgoMillis..todayMillis
            }
            override fun isSelectableYear(docYear: Int): Boolean {
                val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                return docYear in (currentYear - 5)..currentYear
            }
        }
    )
    var gender by remember { mutableStateOf(child.gender) }
    var birthWeight by remember { mutableStateOf(child.birthWeight.replace("[^0-9.]".toRegex(), "")) }
    var birthWeightUnit by remember { mutableStateOf("kg") }
    var birthLength by remember { mutableStateOf(child.birthLength.replace("[^0-9.]".toRegex(), "")) }
    var headCircumference by remember { mutableStateOf(child.headCircumference.replace("[^0-9.]".toRegex(), "")) }
    var bloodGroup by remember { mutableStateOf(child.bloodGroup) }
    var geneticIssues by remember { mutableStateOf(child.geneticIssues) }
    var allergies by remember { mutableStateOf(child.allergies) }
    var currentMedicalConditions by remember { mutableStateOf(child.currentMedicalConditions) }
    var isPremature by remember { mutableStateOf(child.isPremature) }
    var prematureMonths by remember { mutableStateOf(child.prematureMonths) }

    // Pre-populate latest current growth measurements if present
    val latestRecord = child.growthLogs.lastOrNull { !it.isEstimated } ?: child.growthLogs.lastOrNull()
    var currentWeight by remember {
        mutableStateOf(
            if (latestRecord != null && latestRecord.weightKg > 0.0) "${latestRecord.weightKg}" else ""
        )
    }
    var currentWeightUnit by remember { mutableStateOf("kg") }
    var currentLength by remember {
        mutableStateOf(
            if (latestRecord != null && latestRecord.lengthCm > 0.0) "${latestRecord.lengthCm}" else ""
        )
    }
    var currentHeadCircumference by remember {
        mutableStateOf(
            if (latestRecord != null && latestRecord.headCircumferenceCm > 0.0) "${latestRecord.headCircumferenceCm}" else ""
        )
    }

    var isSaving by remember { mutableStateOf(false) }

    val photoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { profileImageUri = it.toString() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit ${child.name}'s Profile") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Avatar Picker
            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .clickable { photoLauncher.launch("image/*") }
            ) {
                ChildProfileAvatar(
                    profileImageUri = profileImageUri,
                    name = name,
                    size = 96.dp
                )
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "Change Photo",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            TextButton(onClick = { photoLauncher.launch("image/*") }) {
                Text("Change Photo", color = MaterialTheme.colorScheme.primary)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Card 1: Basic Info
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "1. Basic Information",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text(tr("child_name")) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = dobStr,
                        onValueChange = {},
                        readOnly = true,
                        enabled = false,
                        label = { Text(tr("dob")) },
                        placeholder = { Text("Tap to select from Calendar (Age ≤ 5 yrs)") },
                        trailingIcon = {
                            IconButton(onClick = { showDatePicker = true }) {
                                Icon(Icons.Filled.DateRange, contentDescription = "Select Date")
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledBorderColor = MaterialTheme.colorScheme.outline,
                            disabledLabelColor = MaterialTheme.colorScheme.primary,
                            disabledTrailingIconColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showDatePicker = true },
                        shape = RoundedCornerShape(12.dp)
                    )

                    if (dobErrorMsg.isNotBlank()) {
                        Text(
                            text = dobErrorMsg,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // Gender Options: Boy / Girl / Other
                    Column {
                        Text(
                            text = tr("gender") + " (Mandatory)",
                            style = MaterialTheme.typography.labelMedium,
                            color = if (gender.isBlank()) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Boy", "Girl", "Other").forEach { g ->
                                FilterChip(
                                    selected = gender == g,
                                    onClick = { gender = g },
                                    label = { Text(g, fontWeight = FontWeight.Bold) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }

                    // Blood Group Options
                    Column {
                        Text(
                            text = tr("blood_group"),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        Row(
                            modifier = Modifier.horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-").forEach { bg ->
                                FilterChip(
                                    selected = bloodGroup == bg,
                                    onClick = { bloodGroup = if (bloodGroup == bg) "" else bg },
                                    label = { Text(bg, fontWeight = FontWeight.Bold) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Card 2: Birth Measurements
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "2. Birth Measurements",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    // Birth Weight with Unit Options
                    Column {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(tr("birth_weight"), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                listOf("kg", "lbs", "g").forEach { unit ->
                                    FilterChip(
                                        selected = birthWeightUnit == unit,
                                        onClick = { birthWeightUnit = unit },
                                        label = { Text(unit, fontWeight = FontWeight.Bold) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                                            selectedLabelColor = Color.White
                                        )
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = birthWeight,
                            onValueChange = { input ->
                                if (input.isEmpty() || input.matches(Regex("^[0-9]*\\.?[0-9]*$"))) {
                                    birthWeight = input
                                }
                            },
                            placeholder = { Text("e.g. 3.2") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            trailingIcon = {
                                Text(birthWeightUnit, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(end = 12.dp))
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    OutlinedTextField(
                        value = birthLength,
                        onValueChange = { input ->
                            if (input.isEmpty() || input.matches(Regex("^[0-9]*\\.?[0-9]*$"))) {
                                birthLength = input
                            }
                        },
                        label = { Text(tr("birth_length")) },
                        placeholder = { Text("e.g. 50") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        trailingIcon = {
                            Text("cm", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(end = 12.dp))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = headCircumference,
                        onValueChange = { input ->
                            if (input.isEmpty() || input.matches(Regex("^[0-9]*\\.?[0-9]*$"))) {
                                headCircumference = input
                            }
                        },
                        label = { Text(tr("head_circ")) },
                        placeholder = { Text("e.g. 35") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        trailingIcon = {
                            Text("cm", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(end = 12.dp))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Card 3: Current Measurements (Today)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "3. Today's Current Measurements",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    // Current Weight with Unit Options
                    Column {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(tr("current_weight"), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                listOf("kg", "lbs", "g").forEach { unit ->
                                    FilterChip(
                                        selected = currentWeightUnit == unit,
                                        onClick = { currentWeightUnit = unit },
                                        label = { Text(unit, fontWeight = FontWeight.Bold) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                                            selectedLabelColor = Color.White
                                        )
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = currentWeight,
                            onValueChange = { input ->
                                if (input.isEmpty() || input.matches(Regex("^[0-9]*\\.?[0-9]*$"))) {
                                    currentWeight = input
                                }
                            },
                            placeholder = { Text("e.g. 6.5") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            trailingIcon = {
                                Text(currentWeightUnit, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(end = 12.dp))
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    OutlinedTextField(
                        value = currentLength,
                        onValueChange = { input ->
                            if (input.isEmpty() || input.matches(Regex("^[0-9]*\\.?[0-9]*$"))) {
                                currentLength = input
                            }
                        },
                        label = { Text(tr("current_length")) },
                        placeholder = { Text("e.g. 65") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        trailingIcon = {
                            Text("cm", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(end = 12.dp))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = currentHeadCircumference,
                        onValueChange = { input ->
                            if (input.isEmpty() || input.matches(Regex("^[0-9]*\\.?[0-9]*$"))) {
                                currentHeadCircumference = input
                            }
                        },
                        label = { Text("Current Head Circumference (Today)") },
                        placeholder = { Text("e.g. 42") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        trailingIcon = {
                            Text("cm", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(end = 12.dp))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Card 4: Medical History & Conditions
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "4. Medical History & Special Conditions",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    OutlinedTextField(
                        value = geneticIssues,
                        onValueChange = { geneticIssues = it },
                        label = { Text(tr("genetic_issues")) },
                        placeholder = { Text("Leave blank if none") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = allergies,
                        onValueChange = { allergies = it },
                        label = { Text(tr("allergies")) },
                        placeholder = { Text("Leave blank if none") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = currentMedicalConditions,
                        onValueChange = { currentMedicalConditions = it },
                        label = { Text(tr("medical_conditions")) },
                        placeholder = { Text("Leave blank if none") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Was $name born prematurely?", style = MaterialTheme.typography.bodyLarge)
                        Switch(
                            checked = isPremature,
                            onCheckedChange = { isPremature = it }
                        )
                    }

                    if (isPremature) {
                        OutlinedTextField(
                            value = prematureMonths,
                            onValueChange = { prematureMonths = it },
                            label = { Text("Premature Duration") },
                            placeholder = { Text("e.g. 2 months early") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    isSaving = true
                    var newDobMillis = child.dateOfBirthMillis
                    try {
                        if (dobStr.isNotBlank()) {
                            val parsedDate = sdf.parse(dobStr)
                            if (parsedDate != null) {
                                newDobMillis = parsedDate.time
                            }
                        }
                    } catch (e: Exception) {
                        // Keep previous dobMillis if parse fails
                    }

                    val rawBWeight = birthWeight.replace("[^0-9.]".toRegex(), "").toDoubleOrNull()
                    val bWeight = if (rawBWeight != null) {
                        when (birthWeightUnit) {
                            "lbs" -> rawBWeight * 0.45359237
                            "g" -> rawBWeight / 1000.0
                            else -> rawBWeight
                        }
                    } else null
                    val bLength = birthLength.replace("[^0-9.]".toRegex(), "").toDoubleOrNull()
                    val bHc = headCircumference.replace("[^0-9.]".toRegex(), "").toDoubleOrNull()

                    val rawCWeight = currentWeight.replace("[^0-9.]".toRegex(), "").toDoubleOrNull()
                    val cWeight = if (rawCWeight != null) {
                        when (currentWeightUnit) {
                            "lbs" -> rawCWeight * 0.45359237
                            "g" -> rawCWeight / 1000.0
                            else -> rawCWeight
                        }
                    } else null
                    val cLength = currentLength.replace("[^0-9.]".toRegex(), "").toDoubleOrNull()
                    val cHc = currentHeadCircumference.replace("[^0-9.]".toRegex(), "").toDoubleOrNull()

                    val growthLogs = child.growthLogs.toMutableList()

                    val days = (System.currentTimeMillis() - newDobMillis) / (1000 * 60 * 60 * 24)
                    val currentAgeMonths = (days / 30.44).toInt().coerceAtLeast(0)

                    if (bWeight != null || bLength != null || bHc != null) {
                        growthLogs.removeAll { it.monthIndex == 0 }
                        growthLogs.add(
                            0,
                            GrowthRecord(
                                id = System.currentTimeMillis().toString() + "_0",
                                dateMillis = newDobMillis,
                                weightKg = bWeight ?: 0.0,
                                lengthCm = bLength ?: 0.0,
                                headCircumferenceCm = bHc ?: 0.0,
                                isEstimated = false,
                                monthIndex = 0
                            )
                        )
                    }

                    if (cWeight != null || cLength != null || cHc != null) {
                        growthLogs.removeAll { it.monthIndex == currentAgeMonths }
                        growthLogs.add(
                            GrowthRecord(
                                id = System.currentTimeMillis().toString() + "_$currentAgeMonths",
                                dateMillis = System.currentTimeMillis(),
                                weightKg = cWeight ?: 0.0,
                                lengthCm = cLength ?: 0.0,
                                headCircumferenceCm = cHc ?: 0.0,
                                isEstimated = false,
                                monthIndex = currentAgeMonths
                            )
                        )
                    }

                    val updatedChild = child.copy(
                        name = name,
                        profileImageUri = profileImageUri,
                        dateOfBirthMillis = newDobMillis,
                        gender = gender,
                        birthWeight = birthWeight,
                        birthLength = birthLength,
                        headCircumference = headCircumference,
                        bloodGroup = bloodGroup,
                        geneticIssues = geneticIssues,
                        allergies = allergies,
                        currentMedicalConditions = currentMedicalConditions,
                        isPremature = isPremature,
                        prematureMonths = prematureMonths,
                        growthLogs = growthLogs
                    )
                    onSaveChild(updatedChild)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = !isSaving && name.isNotBlank()
            ) {
                if (isSaving) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Icon(Icons.Filled.Check, contentDescription = "Save Changes")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(tr("save_changes"), style = MaterialTheme.typography.titleMedium)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { selectedMillis ->
                            val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
                                timeInMillis = selectedMillis
                            }
                            val localCal = Calendar.getInstance().apply {
                                set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
                            }
                            val selectedDob = sdf.format(localCal.time)
                            val ageYears = (System.currentTimeMillis() - localCal.timeInMillis) / (1000L * 60 * 60 * 24 * 365.25)

                            if (ageYears <= 5.02 && localCal.timeInMillis <= System.currentTimeMillis()) {
                                dobStr = selectedDob
                                dobErrorMsg = ""
                            } else {
                                dobErrorMsg = "Child age must be 5 years or under."
                            }
                        }
                        showDatePicker = false
                    }) {
                        Text(tr("ok"))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text(tr("cancel"))
                    }
                }
            ) {
                DatePicker(
                    state = datePickerState,
                    title = {
                        Text(
                            text = "Select Birth Date (Age ≤ 5 Years)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(16.dp)
                        )
                    },
                    showModeToggle = false
                )
            }
        }
    }
}
