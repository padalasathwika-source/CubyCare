package co.csedge.cubycare.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.csedge.cubycare.data.model.Child
import co.csedge.cubycare.utils.tr

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutritionAgeDetailScreen(
    child: Child,
    ageRange: String,
    onBack: () -> Unit
) {
    val currentLang = co.csedge.cubycare.utils.LocalAppLanguage.current
    val trNonComposable: (String) -> String = { key ->
        co.csedge.cubycare.utils.AppLanguageManager.getString(key, currentLang)
    }

    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Diet & Feeding Plan", "Diet Plan")

    val matchedAgeBlock = when {
        ageRange == "0-6 Months" || ageRange.contains("0-6") || ageRange.contains("Infant") -> "0-6 Months"
        ageRange == "6-12 Months" || ageRange.contains("6-12") -> "6-12 Months"
        ageRange == "1-5 Years" || ageRange.contains("1-5") || ageRange.contains("Toddler") || ageRange.contains("Year") -> "1-5 Years"
        else -> {
            val months = child.ageInMonths
            when {
                months <= 6 -> "0-6 Months"
                months in 7..12 -> "6-12 Months"
                else -> "1-5 Years"
            }
        }
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("$ageRange Nutrition Plan") },
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
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        if (selectedTabIndex == 0) {
            // Guidelines Tab
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = "Dietary Guidelines & Feeding Tips ($ageRange)",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                when (matchedAgeBlock) {
                    "0-6 Months" -> {
                        item { GuidelineCard("Exclusive Breastfeeding / Formula", "Breastmilk or infant formula provides 100% of required nutrition for the first 6 months. No extra water, tea, or juice is needed.", listOf("Feed on demand (8-12 times daily)", "Administer Vitamin D drops as prescribed by pediatrician")) }
                        item { GuidelineCard("Feeding Cues & Satiety", "Feed when baby roots, sucks fingers, or smacks lips. Stop when baby turns head away or relaxes hands.", listOf("Burp baby halfway through and after every feed", "Keep baby upright for 15-20 mins after feeding")) }
                        item { GuidelineCard("Hygiene & Bottle Care", "Sterilize bottles, nipples, and pump parts thoroughly in boiling water for 5 minutes.", listOf("Discard leftover bottle milk after 1 hour of feeding")) }
                    }
                    "6-12 Months" -> {
                        item { GuidelineCard("Introducing Weaning Purees", "Begin with single-ingredient purees (ragi, apple, sweet potato, dal mash). Continue breastmilk/formula alongside solids.", listOf("Introduce 1 new food every 3 days to monitor allergies", "Avoid adding salt, sugar, or honey before 1 year")) }
                        item { GuidelineCard("Iron & Micronutrients", "Serve iron-fortified cereals, well-cooked lentils, and mashed dark leafy greens.", listOf("Offer small sips of boiled cooled water in a cup")) }
                        item { GuidelineCard("Texture Progression", "Progress from smooth purees at 6 months to soft lumps at 8 months and finger foods at 10 months.", listOf("Encourage self-feeding with soft banana or steamed carrot sticks")) }
                    }
                    else -> {
                        item { GuidelineCard("Balanced Family Meals", "Serve 3 main meals and 2 healthy snacks daily incorporating all food groups (grains, proteins, dairy, fruits, veggies).", listOf("Use ghee/oil in moderation for essential fatty acids", "Encourage family dining together at table")) }
                        item { GuidelineCard("Healthy Hydration & Dairy", "Offer 1.5 to 2 cups of full-fat cow's milk or curd daily alongside plain water.", listOf("Limit fruit juices to under 120ml daily")) }
                        item { GuidelineCard("Picky Eating Strategy", "Offer new foods alongside liked foods without forcing or bribing.", listOf("Keep mealtime relaxed and free of screens")) }
                    }
                }
            }
        } else {
            // Weekly Meal Plan Tab
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = "Weekly Sample Meal Plan ($ageRange)",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                when (matchedAgeBlock) {
                    "0-6 Months" -> {
                        item {
                            Text(
                                text = "For 0-6 months infants, exclusive breastmilk or infant formula feeds are recommended on demand.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.DarkGray
                            )
                        }
                        val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
                        days.forEach { day ->
                            item { WeeklyPlanCard(day, "Morning: Milk Feed\nMid-day: Milk Feed\nAfternoon: Milk Feed\nEvening: Milk Feed\nNight: Milk Feed (On Demand)") }
                        }
                    }
                    "6-12 Months" -> {
                        val days = listOf(
                            "Monday" to "Morning: Breast milk/Formula\nBreakfast: Ragi porridge (mashed)\nLunch: Mashed potato & carrot\nSnack: Breast milk/Formula\nDinner: Moong dal mash",
                            "Tuesday" to "Morning: Breast milk/Formula\nBreakfast: Mashed banana\nLunch: Rice khichdi with peas (mashed)\nSnack: Breast milk/Formula\nDinner: Pureed pumpkin",
                            "Wednesday" to "Morning: Breast milk/Formula\nBreakfast: Apple puree\nLunch: Mashed dal & rice\nSnack: Breast milk/Formula\nDinner: Soft mashed papaya",
                            "Thursday" to "Morning: Breast milk/Formula\nBreakfast: Oats porridge\nLunch: Soft boiled sweet potato\nSnack: Breast milk/Formula\nDinner: Mashed lentils",
                            "Friday" to "Morning: Breast milk/Formula\nBreakfast: Mashed avocado or pureed pear\nLunch: Khichdi with spinach (pureed)\nSnack: Breast milk/Formula\nDinner: Mashed carrots",
                            "Saturday" to "Morning: Breast milk/Formula\nBreakfast: Ragi & apple mash\nLunch: Soft idli mashed in dal\nSnack: Breast milk/Formula\nDinner: Pureed bottle gourd (lauki)",
                            "Sunday" to "Morning: Breast milk/Formula\nBreakfast: Mashed chickoo\nLunch: Dal & rice mash\nSnack: Breast milk/Formula\nDinner: Soft vegetable soup (strained)"
                        )
                        days.forEach { (day, plan) ->
                            item { WeeklyPlanCard(day, plan) }
                        }
                    }
                    else -> {
                        val days = listOf(
                            "Monday" to "Breakfast: Upma with veggies & Milk\nLunch: Roti, Dal, Palak sabzi\nSnack: Seasonal fruit slices\nDinner: Rice, Sambar, Mild chicken/paneer curry",
                            "Tuesday" to "Breakfast: Poha with peanuts & Milk\nLunch: Veg Pulao, Raita\nSnack: Roasted makhana or yogurt\nDinner: Roti, Mixed veg sabzi, Dal",
                            "Wednesday" to "Breakfast: Idli with mild chutney/sambar\nLunch: Rajma (kidney beans) chawal (rice)\nSnack: Banana or Apple slices\nDinner: Dosa with potato filling",
                            "Thursday" to "Breakfast: Paratha (stuffed with paneer or potato) & Curd\nLunch: Lemon rice with boiled egg/soya chunks\nSnack: Carrot/Cucumber sticks with hummus\nDinner: Roti, Bhindi (okra) sabzi, Dal",
                            "Friday" to "Breakfast: Oats with milk and fruits\nLunch: Khichdi with lots of veggies & ghee\nSnack: Cheese cubes and fruit\nDinner: Roti, Dal makhani, Salad",
                            "Saturday" to "Breakfast: Besan chilla (gram flour pancake)\nLunch: Chicken/Veg biryani with raita\nSnack: Boiled sweet corn\nDinner: Whole wheat pasta with hidden veggie sauce",
                            "Sunday" to "Breakfast: Dosa/Appam with vegetable stew\nLunch: Roti, Chole (chickpeas), Salad\nSnack: Homemade fruit smoothie\nDinner: Vegetable soup and toast"
                        )
                        days.forEach { (day, plan) ->
                            item { WeeklyPlanCard(day, plan) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WeeklyPlanCard(day: String, plan: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = day,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = plan,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray,
                lineHeight = 22.sp // Improved spacing
            )
        }
    }
}

@Composable
fun GuidelineCard(title: String, reason: String, examples: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = "Reason",
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(20.dp).padding(top = 2.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = reason,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray
                )
            }
            if (examples.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF3F4F6), RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        examples.forEach { example ->
                            Text(
                                text = "• $example",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF4B5563)
                            )
                        }
                    }
                }
            }
        }
    }
}
