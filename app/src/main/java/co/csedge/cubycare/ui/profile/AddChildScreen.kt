package co.csedge.cubycare.ui.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import co.csedge.cubycare.data.model.Child
import co.csedge.cubycare.data.model.GrowthRecord
import co.csedge.cubycare.data.repository.VaccineScheduleProvider
import co.csedge.cubycare.data.repository.MilestoneProvider
import co.csedge.cubycare.ui.components.ChildProfileAvatar
import java.text.SimpleDateFormat
import java.util.*
import co.csedge.cubycare.utils.tr
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.unit.sp

@Composable
fun QuestionSlide(
    question: String,
    description: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    unitSuffix: String? = null,
    isNumericOnly: Boolean = false
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = question,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))
        OutlinedTextField(
            value = value,
            onValueChange = { input ->
                if (isNumericOnly) {
                    if (input.isEmpty() || input.matches(Regex("^[0-9]*\\.?[0-9]*$"))) {
                        onValueChange(input)
                    }
                } else {
                    onValueChange(input)
                }
            },
            placeholder = { Text(placeholder) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            trailingIcon = if (unitSuffix != null) {
                {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(
                            text = unitSuffix,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            } else null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )
    }
}

@Composable
fun MeasurementQuestionSlide(
    question: String,
    description: String,
    value: String,
    onValueChange: (String) -> Unit,
    selectedUnit: String,
    onUnitChange: (String) -> Unit,
    placeholder: String,
    availableUnits: List<String> = listOf("cm", "inches")
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = question,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Unit: ", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.width(4.dp))
            availableUnits.forEach { unit ->
                FilterChip(
                    selected = selectedUnit == unit,
                    onClick = { onUnitChange(unit) },
                    label = { Text(unit, fontWeight = FontWeight.Bold) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = value,
            onValueChange = { input ->
                if (input.isEmpty() || input.matches(Regex("^[0-9]*\\.?[0-9]*$"))) {
                    onValueChange(input)
                }
            },
            placeholder = { Text(placeholder) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            trailingIcon = {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(
                        text = selectedUnit,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )
    }
}

fun formatQuestion(str: String, childName: String, rawName: String): String {
    var res = str.replace("\$name", childName)
    res = res.replace("{name}", childName)
    if (rawName.isNotBlank() && rawName != childName) {
        res = res.replace(rawName, childName)
    }
    return res
}

@Composable
fun GenderSelectionSlide(
    childName: String,
    selectedGender: String,
    onGenderSelected: (String) -> Unit
) {
    val genders = listOf("Boy", "Girl", "Other")

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "What is $childName's gender?",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Please select an option (Mandatory)",
            style = MaterialTheme.typography.bodyMedium,
            color = if (selectedGender.isBlank()) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            genders.forEach { g ->
                val isSelected = selectedGender == g
                Card(
                    onClick = { onGenderSelected(g) },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = g,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                        RadioButton(
                            selected = isSelected,
                            onClick = { onGenderSelected(g) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BloodGroupSelectionSlide(
    childName: String,
    selectedBloodGroup: String,
    onBloodGroupSelected: (String) -> Unit
) {
    val bloodGroups = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "What is $childName's blood group?",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Select blood group",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
        ) {
            items(bloodGroups) { bg ->
                val isSelected = selectedBloodGroup == bg
                FilterChip(
                    selected = isSelected,
                    onClick = { onBloodGroupSelected(if (isSelected) "" else bg) },
                    label = {
                        Text(
                            text = bg,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 15.sp,
                            modifier = Modifier.padding(vertical = 4.dp),
                            textAlign = TextAlign.Center
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddChildScreen(
    onChildAdded: (Child) -> Unit,
    onBackClick: () -> Unit = {}
) {
    var name by remember { mutableStateOf("") }
    var profileImageUri by remember { mutableStateOf("") }
    var dobStr by remember { mutableStateOf("") }
    var dobErrorMsg by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    val fiveYearsAgoMillis = remember {
        val cal = Calendar.getInstance()
        cal.add(Calendar.YEAR, -5)
        cal.timeInMillis
    }
    val todayMillis = remember { System.currentTimeMillis() }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis(),
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
    var gender by remember { mutableStateOf("") }
    var birthWeight by remember { mutableStateOf("") }
    var birthWeightUnit by remember { mutableStateOf("kg") }
    var birthLength by remember { mutableStateOf("") }
    var birthLengthUnit by remember { mutableStateOf("cm") }
    var headCircumference by remember { mutableStateOf("") }
    var headCircumferenceUnit by remember { mutableStateOf("cm") }
    var bloodGroup by remember { mutableStateOf("") }
    var geneticIssues by remember { mutableStateOf("") }
    var allergies by remember { mutableStateOf("") }
    var currentMedicalConditions by remember { mutableStateOf("") }
    var isPremature by remember { mutableStateOf(false) }
    var prematureMonths by remember { mutableStateOf("") }

    var currentWeight by remember { mutableStateOf("") }
    var currentWeightUnit by remember { mutableStateOf("kg") }
    var currentLength by remember { mutableStateOf("") }
    var currentLengthUnit by remember { mutableStateOf("cm") }
    var currentHeadCircumference by remember { mutableStateOf("") }
    var currentHeadCircumferenceUnit by remember { mutableStateOf("cm") }

    var isSaving by remember { mutableStateOf(false) }

    var currentStep by remember { mutableIntStateOf(0) }
    val totalSteps = 14

    val photoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { profileImageUri = it.toString() }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent,
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    painter = painterResource(id = co.csedge.cubycare.R.drawable.cubs_bg),
                    contentScale = ContentScale.Crop
                )
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Progress Indicator
                LinearProgressIndicator(
                    progress = { (currentStep + 1).toFloat() / totalSteps },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .padding(bottom = 32.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Animated Slide Content
                AnimatedContent(
                    targetState = currentStep,
                    transitionSpec = {
                        if (targetState > initialState) {
                            (slideInHorizontally { width -> width } + fadeIn(tween(300))).togetherWith(
                                slideOutHorizontally { width -> -width } + fadeOut(tween(300))
                            )
                        } else {
                            (slideInHorizontally { width -> -width } + fadeIn(tween(300))).togetherWith(
                                slideOutHorizontally { width -> width } + fadeOut(tween(300))
                            )
                        }
                    }, label = "wizard_transition"
                ) { step ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            when (step) {
                                0 -> {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = "Child Profile & Name",
                                            style = MaterialTheme.typography.headlineSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary,
                                            textAlign = TextAlign.Center
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "Upload a profile photo or keep profile as name only.",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            textAlign = TextAlign.Center
                                        )
                                        Spacer(modifier = Modifier.height(20.dp))

                                        Box(
                                            contentAlignment = Alignment.BottomEnd,
                                            modifier = Modifier.clickable { photoLauncher.launch("image/*") }
                                        ) {
                                            ChildProfileAvatar(
                                                profileImageUri = profileImageUri,
                                                name = name,
                                                size = 100.dp
                                            )
                                            Surface(
                                                shape = CircleShape,
                                                color = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(32.dp)
                                            ) {
                                                Box(contentAlignment = Alignment.Center) {
                                                    Icon(
                                                        imageVector = Icons.Filled.AccountCircle,
                                                        contentDescription = "Upload Photo",
                                                        tint = Color.White,
                                                        modifier = Modifier.size(18.dp)
                                                    )

                                                }
                                            }
                                        }

                                        TextButton(onClick = { photoLauncher.launch("image/*") }) {
                                            Text(
                                                if (profileImageUri.isNotBlank()) tr("change_photo") else tr("upload_photo"),
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(16.dp))
                                        OutlinedTextField(
                                            value = name,
                                            onValueChange = { name = it },
                                            label = { Text("Baby's Name") },
                                            placeholder = { Text("e.g., Emma or Liam") },
                                            singleLine = true,
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                    }
                                }
                                1 -> {
                                    val childName = if (name.trim().isNotBlank()) name.trim() else "the baby"
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = formatQuestion(tr("addchild_q_dob"), childName, name.trim()),
                                            style = MaterialTheme.typography.headlineSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary,
                                            textAlign = TextAlign.Center
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "Select $childName's birth date from calendar (Mandatory - Age ≤ 5 yrs).",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            textAlign = TextAlign.Center
                                        )
                                        Spacer(modifier = Modifier.height(24.dp))

                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable { showDatePicker = true },
                                            shape = RoundedCornerShape(16.dp),
                                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)),
                                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(16.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Column {
                                                    Text(
                                                        text = tr("dob"),
                                                        style = MaterialTheme.typography.labelMedium,
                                                        color = MaterialTheme.colorScheme.primary
                                                    )
                                                    Spacer(modifier = Modifier.height(4.dp))
                                                    Text(
                                                        text = if (dobStr.isNotBlank()) dobStr else "Tap to select from Calendar",
                                                        style = MaterialTheme.typography.titleMedium,
                                                        fontWeight = if (dobStr.isNotBlank()) FontWeight.Bold else FontWeight.Normal,
                                                        color = if (dobStr.isNotBlank()) MaterialTheme.colorScheme.onSurface else Color.Gray
                                                    )
                                                }
                                                Icon(
                                                    imageVector = Icons.Filled.DateRange,
                                                    contentDescription = "Select Date from Calendar",
                                                    tint = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                        }

                                        if (dobErrorMsg.isNotBlank()) {
                                            Spacer(modifier = Modifier.height(12.dp))
                                            Text(
                                                text = dobErrorMsg,
                                                color = MaterialTheme.colorScheme.error,
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.SemiBold,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }
                                2 -> {
                                    val childName = if (name.trim().isNotBlank()) name.trim() else "the baby"
                                    GenderSelectionSlide(
                                        childName = childName,
                                        selectedGender = gender,
                                        onGenderSelected = { gender = it }
                                    )
                                }
                                3 -> {
                                    val childName = if (name.trim().isNotBlank()) name.trim() else "the baby"
                                    MeasurementQuestionSlide(
                                        question = "What was $childName's birth weight?",
                                        description = "Weight recorded at birth (Optional)",
                                        value = birthWeight,
                                        onValueChange = { birthWeight = it },
                                        selectedUnit = birthWeightUnit,
                                        onUnitChange = { birthWeightUnit = it },
                                        placeholder = if (birthWeightUnit == "kg") "e.g., 3.2" else if (birthWeightUnit == "lbs") "e.g., 7.0" else "e.g., 3200",
                                        availableUnits = listOf("kg", "lbs", "g")
                                    )
                                }
                                4 -> {
                                    val childName = if (name.trim().isNotBlank()) name.trim() else "the baby"
                                    MeasurementQuestionSlide(
                                        question = "What was $childName's birth length?",
                                        description = "Body length/height recorded at birth (Optional)",
                                        value = birthLength,
                                        onValueChange = { birthLength = it },
                                        selectedUnit = birthLengthUnit,
                                        onUnitChange = { birthLengthUnit = it },
                                        placeholder = if (birthLengthUnit == "cm") "e.g., 50" else "e.g., 19.7"
                                    )
                                }
                                5 -> {
                                    val childName = if (name.trim().isNotBlank()) name.trim() else "the baby"
                                    MeasurementQuestionSlide(
                                        question = "What was $childName's birth head circumference?",
                                        description = "Head circumference recorded at birth (Optional)",
                                        value = headCircumference,
                                        onValueChange = { headCircumference = it },
                                        selectedUnit = headCircumferenceUnit,
                                        onUnitChange = { headCircumferenceUnit = it },
                                        placeholder = if (headCircumferenceUnit == "cm") "e.g., 34" else "e.g., 13.4"
                                    )
                                }
                                6 -> {
                                    val childName = if (name.trim().isNotBlank()) name.trim() else "the baby"
                                    BloodGroupSelectionSlide(
                                        childName = childName,
                                        selectedBloodGroup = bloodGroup,
                                        onBloodGroupSelected = { bloodGroup = it }
                                    )
                                }
                                7 -> {
                                    val childName = if (name.trim().isNotBlank()) name.trim() else "the baby"
                                    QuestionSlide(
                                        question = "Does $childName have any genetic health conditions or family medical history?",
                                        description = "Mention any inherited genetic conditions or family medical history (Optional)",
                                        value = geneticIssues,
                                        onValueChange = { geneticIssues = it },
                                        placeholder = "e.g., Thalassemia, G6PD, Cystic Fibrosis, Diabetes..."
                                    )
                                }
                                8 -> {
                                    val childName = if (name.trim().isNotBlank()) name.trim() else "the baby"
                                    QuestionSlide(
                                        question = "Does $childName have any known allergies?",
                                        description = "Mention any food, medication, or environmental allergies (Optional)",
                                        value = allergies,
                                        onValueChange = { allergies = it },
                                        placeholder = "e.g., Peanuts, Cow's Milk, Penicillin, Dust, Eggs..."
                                    )
                                }
                                9 -> {
                                    val childName = if (name.trim().isNotBlank()) name.trim() else "the baby"
                                    QuestionSlide(
                                        question = "Does $childName have any current medical conditions?",
                                        description = "Mention any ongoing or diagnosed health conditions (Optional)",
                                        value = currentMedicalConditions,
                                        onValueChange = { currentMedicalConditions = it },
                                        placeholder = "e.g., Asthma, Eczema, Heart Condition, Reflux..."
                                    )
                                }
                                10 -> {
                                    val childName = if (name.trim().isNotBlank()) name.trim() else "the baby"
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = "Was $childName born prematurely?",
                                            style = MaterialTheme.typography.headlineSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary,
                                            textAlign = TextAlign.Center
                                        )
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text("No", style = MaterialTheme.typography.bodyLarge)
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Switch(
                                                checked = isPremature,
                                                onCheckedChange = { isPremature = it }
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text("Yes", style = MaterialTheme.typography.bodyLarge)
                                        }
                                        if (isPremature) {
                                            Spacer(modifier = Modifier.height(16.dp))
                                            OutlinedTextField(
                                                value = prematureMonths,
                                                onValueChange = { prematureMonths = it },
                                                label = { Text("Premature Duration") },
                                                placeholder = { Text("e.g., 2 months early") },
                                                singleLine = true,
                                                modifier = Modifier.fillMaxWidth(),
                                                shape = RoundedCornerShape(16.dp)
                                            )
                                        }
                                    }
                                }
                                11 -> {
                                    val childName = if (name.trim().isNotBlank()) name.trim() else "the baby"
                                    MeasurementQuestionSlide(
                                        question = "What is $childName's current weight?",
                                        description = "Today's weight (Optional)",
                                        value = currentWeight,
                                        onValueChange = { currentWeight = it },
                                        selectedUnit = currentWeightUnit,
                                        onUnitChange = { currentWeightUnit = it },
                                        placeholder = if (currentWeightUnit == "kg") "e.g., 6.5" else if (currentWeightUnit == "lbs") "e.g., 14.3" else "e.g., 6500",
                                        availableUnits = listOf("kg", "lbs", "g")
                                    )
                                }
                                12 -> {
                                    val childName = if (name.trim().isNotBlank()) name.trim() else "the baby"
                                    MeasurementQuestionSlide(
                                        question = "What is $childName's current length/height?",
                                        description = "Today's length (Optional)",
                                        value = currentLength,
                                        onValueChange = { currentLength = it },
                                        selectedUnit = currentLengthUnit,
                                        onUnitChange = { currentLengthUnit = it },
                                        placeholder = if (currentLengthUnit == "cm") "e.g., 65" else "e.g., 25.5"
                                    )
                                }
                                13 -> {
                                    val childName = if (name.trim().isNotBlank()) name.trim() else "the baby"
                                    MeasurementQuestionSlide(
                                        question = "What is $childName's current head circumference?",
                                        description = "Today's head circumference (Optional)",
                                        value = currentHeadCircumference,
                                        onValueChange = { currentHeadCircumference = it },
                                        selectedUnit = currentHeadCircumferenceUnit,
                                        onUnitChange = { currentHeadCircumferenceUnit = it },
                                        placeholder = if (currentHeadCircumferenceUnit == "cm") "e.g., 42" else "e.g., 16.5"
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Navigation Controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (currentStep > 0) {
                        OutlinedButton(
                            onClick = { currentStep-- },
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Previous")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Previous")
                        }
                    } else {
                        OutlinedButton(
                            onClick = onBackClick,
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Cancel")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(tr("cancel"))
                        }
                    }

                    if (currentStep < totalSteps - 1) {
                        Button(
                            onClick = {
                                if (currentStep == 0 && name.isBlank()) {
                                    return@Button
                                }
                                if (currentStep == 1) {
                                    if (dobStr.isBlank()) {
                                        dobErrorMsg = "Please tap above to select birth date from calendar."
                                        return@Button
                                    }
                                    val dobDate = try { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(dobStr) } catch(e: Exception) { null }
                                    if (dobDate != null) {
                                        val ageYears = (System.currentTimeMillis() - dobDate.time) / (1000L * 60 * 60 * 24 * 365.25)
                                        if (ageYears > 5.02) {
                                            dobErrorMsg = "Child age must be 5 years old or under."
                                            return@Button
                                        }
                                    }
                                }
                                if (currentStep == 2 && gender.isBlank()) {
                                    return@Button
                                }
                                currentStep++
                            },
                            shape = RoundedCornerShape(16.dp),
                            enabled = (currentStep != 0 || name.isNotBlank()) &&
                                      (currentStep != 1 || dobStr.isNotBlank()) &&
                                      (currentStep != 2 || gender.isNotBlank())
                        ) {
                            Text("Next")
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(Icons.Filled.ArrowForward, contentDescription = "Next")
                        }
                    } else {
                        Button(
                            onClick = {
                                isSaving = true
                                var dobMillis = System.currentTimeMillis()
                                try {
                                    if (dobStr.isNotBlank()) {
                                        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                        val date = sdf.parse(dobStr)
                                        if (date != null) {
                                            dobMillis = date.time
                                        }
                                    }
                                } catch (e: Exception) {
                                    // Fallback to current time if parse fails
                                }

                                val rawBWeight = birthWeight.replace("[^0-9.]".toRegex(), "").toDoubleOrNull()
                                val bWeight = if (rawBWeight != null) {
                                    when (birthWeightUnit) {
                                        "lbs" -> rawBWeight * 0.45359237
                                        "g" -> rawBWeight / 1000.0
                                        else -> rawBWeight
                                    }
                                } else null
                                val rawBLength = birthLength.replace("[^0-9.]".toRegex(), "").toDoubleOrNull()
                                val bLength = if (rawBLength != null) {
                                    if (birthLengthUnit == "inches") rawBLength * 2.54 else rawBLength
                                } else null
                                val rawBHc = headCircumference.replace("[^0-9.]".toRegex(), "").toDoubleOrNull()
                                val bHc = if (rawBHc != null) {
                                    if (headCircumferenceUnit == "inches") rawBHc * 2.54 else rawBHc
                                } else null

                                val rawCWeight = currentWeight.replace("[^0-9.]".toRegex(), "").toDoubleOrNull()
                                val cWeight = if (rawCWeight != null) {
                                    when (currentWeightUnit) {
                                        "lbs" -> rawCWeight * 0.45359237
                                        "g" -> rawCWeight / 1000.0
                                        else -> rawCWeight
                                    }
                                } else null
                                val rawCLength = currentLength.replace("[^0-9.]".toRegex(), "").toDoubleOrNull()
                                val cLength = if (rawCLength != null) {
                                    if (currentLengthUnit == "inches") rawCLength * 2.54 else rawCLength
                                } else null
                                val rawCHc = currentHeadCircumference.replace("[^0-9.]".toRegex(), "").toDoubleOrNull()
                                val cHc = if (rawCHc != null) {
                                    if (currentHeadCircumferenceUnit == "inches") rawCHc * 2.54 else rawCHc
                                } else null

                                val growthLogs = mutableListOf<GrowthRecord>()

                                val days = (System.currentTimeMillis() - dobMillis) / (1000 * 60 * 60 * 24)
                                val currentAgeMonths = (days / 30.44).toInt().coerceAtLeast(0)

                                if (bWeight != null || bLength != null || bHc != null) {
                                    growthLogs.add(
                                        GrowthRecord(
                                            id = System.currentTimeMillis().toString() + "_0",
                                            dateMillis = dobMillis,
                                            weightKg = bWeight ?: 0.0,
                                            lengthCm = bLength ?: 0.0,
                                            headCircumferenceCm = bHc ?: 0.0,
                                            isEstimated = false,
                                            monthIndex = 0
                                        )
                                    )

                                    if (currentAgeMonths > 0 && (cWeight != null || cLength != null || cHc != null)) {
                                        for (i in 1 until currentAgeMonths) {
                                            val fraction = i.toDouble() / currentAgeMonths
                                            val w = if (bWeight != null && cWeight != null) bWeight + (cWeight - bWeight) * fraction else (bWeight ?: cWeight ?: 0.0)
                                            val l = if (bLength != null && cLength != null) bLength + (cLength - bLength) * fraction else (bLength ?: cLength ?: 0.0)
                                            val hc = if (bHc != null && cHc != null) bHc + (cHc - bHc) * fraction else (bHc ?: cHc ?: 0.0)

                                            val cal = Calendar.getInstance().apply {
                                                timeInMillis = dobMillis
                                                add(Calendar.MONTH, i)
                                            }

                                            growthLogs.add(
                                                GrowthRecord(
                                                    id = System.currentTimeMillis().toString() + "_$i",
                                                    dateMillis = cal.timeInMillis,
                                                    weightKg = w,
                                                    lengthCm = l,
                                                    headCircumferenceCm = hc,
                                                    isEstimated = true,
                                                    monthIndex = i
                                                )
                                            )
                                        }
                                    }

                                    if (cWeight != null || cLength != null || cHc != null) {
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
                                } else if (currentAgeMonths > 0 && (cWeight != null || cLength != null || cHc != null)) {
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

                                val child = Child(
                                    id = System.currentTimeMillis().toString(),
                                    name = name,
                                    profileImageUri = profileImageUri,
                                    dateOfBirthMillis = dobMillis,
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
                                    growthLogs = growthLogs,
                                    vaccines = VaccineScheduleProvider.generateIAPSchedule(dobMillis),
                                    milestones = MilestoneProvider.generateDefaultMilestones()
                                )
                                onChildAdded(child)
                            },
                            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
                            enabled = !isSaving
                        ) {
                            if (isSaving) {
                                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                            } else {
                                Text("Complete")
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(Icons.Filled.Check, contentDescription = "Complete")
                            }
                        }
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
                            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
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
}
}
}
