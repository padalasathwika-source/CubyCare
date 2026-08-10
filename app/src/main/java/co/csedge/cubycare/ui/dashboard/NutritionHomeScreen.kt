package co.csedge.cubycare.ui.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*

import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import co.csedge.cubycare.data.model.Child

import co.csedge.cubycare.data.model.DietaryPreferences
import co.csedge.cubycare.data.model.FoodDiaryEntry
import java.text.SimpleDateFormat
import java.util.*
import co.csedge.cubycare.utils.tr

import co.csedge.cubycare.utils.currentVitalsAgeRange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutritionHomeScreen(
    child: Child,
    onBack: () -> Unit,
    onAgeBlockClick: (String) -> Unit,
    onUpdateChild: (Child) -> Unit
) {
    val isGeneralProfile = child.id == "default_general_baby" || child.name.contains("General Baby") || child.dateOfBirthMillis == 0L
    var selectedAgeRange by remember { mutableStateOf<String?>(if (isGeneralProfile) null else child.currentVitalsAgeRange) }

    if (selectedAgeRange != null) {
        NutritionAgeDetailScreen(
            child = child,
            ageRange = selectedAgeRange!!,
            onBack = { selectedAgeRange = null }
        )
    } else {
        var showAddEntryDialog by remember { mutableStateOf(false) }
        var selectedTabIndex by remember { mutableStateOf(0) }
        val tabs = listOf("Age & Diet Plans", "Food Log")

        // State for Dietary Preferences
        var isVegetarian by remember { mutableStateOf(child.dietaryPreferences.isVegetarian) }
        var regionalPreference by remember { mutableStateOf(child.dietaryPreferences.regionalPreference) }
        var foodAllergies by remember { mutableStateOf(child.dietaryPreferences.foodAllergies) }
        var pickyEatingNotes by remember { mutableStateOf(child.dietaryPreferences.pickyEatingNotes) }

        val savePreferences = {
            val updatedPreferences = DietaryPreferences(
                isVegetarian = isVegetarian,
                regionalPreference = regionalPreference,
                foodAllergies = foodAllergies,
                pickyEatingNotes = pickyEatingNotes
            )
            onUpdateChild(child.copy(dietaryPreferences = updatedPreferences))
        }

        val ageBlocks = listOf("0-6 Months", "6-12 Months", "1-5 Years")

        Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Nutrition & Feeding Guide") },
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
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = Color.White,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = { Text(title, fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal) },
                            selectedContentColor = MaterialTheme.colorScheme.primary,
                            unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            if (selectedTabIndex == 1) {
                FloatingActionButton(
                    onClick = { showAddEntryDialog = true },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Food Log", tint = Color.White)
                }
            }
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = co.csedge.cubycare.R.drawable.mother_baby_nutrition_bg),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                alpha = 0.20f
            )
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {

            if (selectedTabIndex == 0) {
                // Diet Guidelines Grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(ageBlocks) { ageRange ->
                        val hue = Math.abs(ageRange.hashCode()) % 360f
                        val blockColor = Color.hsl(hue = hue, saturation = 0.5f, lightness = 0.90f)
                        
                        Card(
                            modifier = Modifier
                                .height(140.dp)
                                .clickable { onAgeBlockClick(ageRange) },
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = blockColor),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize().padding(12.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                androidx.compose.material3.Icon(
                                    imageVector = androidx.compose.material.icons.Icons.Filled.Favorite,
                                    contentDescription = ageRange,
                                    tint = Color(0xFF2E7D32),
                                    modifier = Modifier.size(44.dp).padding(bottom = 6.dp)
                                )

                                Text(
                                    text = ageRange.replace(" ", "\n"),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.DarkGray,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }

                }
            } else {
                // Diary & Settings
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    item {
                        Text(
                            text = "Dietary Preferences",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Vegetarian", modifier = Modifier.weight(1f))
                                    Switch(
                                        checked = isVegetarian,
                                        onCheckedChange = { 
                                            isVegetarian = it
                                            savePreferences()
                                        }
                                    )
                                }
                                
                                OutlinedTextField(
                                    value = regionalPreference,
                                    onValueChange = { 
                                        regionalPreference = it 
                                        savePreferences()
                                    },
                                    label = { Text("Regional Preference (e.g. South Indian)") },
                                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                                )
                                
                                OutlinedTextField(
                                    value = foodAllergies,
                                    onValueChange = { 
                                        foodAllergies = it
                                        savePreferences()
                                    },
                                    label = { Text("Food Allergies") },
                                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                                )
                                
                                OutlinedTextField(
                                    value = pickyEatingNotes,
                                    onValueChange = { 
                                        pickyEatingNotes = it
                                        savePreferences()
                                    },
                                    label = { Text("Picky Eating Notes") },
                                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                                )
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Food Diary",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    if (child.foodDiary.isEmpty()) {
                        item {
                            Text(
                                text = tr("no_food"),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    } else {
                        items(child.foodDiary.sortedByDescending { it.dateMillis }) { entry ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = entry.timeStr,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = "Response: ${entry.response}",
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.Bold,
                                            color = when (entry.response) {
                                                "Liked" -> Color(0xFF4CAF50)
                                                "Disliked" -> Color(0xFFE53935)
                                                else -> Color(0xFFFFA000)
                                            }
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = entry.food,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Quantity: ${entry.quantity}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


    if (showAddEntryDialog) {
        var timeStr by remember { mutableStateOf(SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())) }
        var food by remember { mutableStateOf("") }
        var quantity by remember { mutableStateOf("") }
        var response by remember { mutableStateOf("Neutral") }

        AlertDialog(
            onDismissRequest = { showAddEntryDialog = false },
            title = { Text("Add Food Diary Entry") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = timeStr,
                        onValueChange = { timeStr = it },
                        label = { Text("Time") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = food,
                        onValueChange = { food = it },
                        label = { Text("Food Item") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = quantity,
                        onValueChange = { quantity = it },
                        label = { Text("Quantity") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Text("Child's Response:", modifier = Modifier.padding(top = 8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        listOf("Liked", "Neutral", "Disliked").forEach { option ->
                            FilterChip(
                                selected = response == option,
                                onClick = { response = option },
                                label = { Text(option) }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (food.isNotBlank()) {
                        val newEntry = FoodDiaryEntry(
                            id = UUID.randomUUID().toString(),
                            timeStr = timeStr,
                            food = food,
                            quantity = quantity,
                            response = response
                        )
                        val updatedList = child.foodDiary + newEntry
                        onUpdateChild(child.copy(foodDiary = updatedList))
                        showAddEntryDialog = false
                    }
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddEntryDialog = false }) {
                    Text(tr("cancel"))
                }
            }
        )
    }
}
}
