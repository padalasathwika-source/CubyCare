package co.csedge.cubycare

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import co.csedge.cubycare.ui.MainViewModel
import co.csedge.cubycare.ui.auth.AuthScreen
import co.csedge.cubycare.ui.dashboard.DashboardScreen
import co.csedge.cubycare.ui.dashboard.ChildHomeScreen
import co.csedge.cubycare.ui.dashboard.DoctorAppointmentsScreen
import co.csedge.cubycare.ui.dashboard.CubyAlertScreen
import co.csedge.cubycare.ui.dashboard.CubyParentingScreen
import co.csedge.cubycare.ui.dashboard.DisorderDetailScreen
import co.csedge.cubycare.ui.dashboard.AllergyDetailScreen
import co.csedge.cubycare.ui.dashboard.GrowthScreen
import co.csedge.cubycare.ui.dashboard.VaccinationScreen
import co.csedge.cubycare.ui.dashboard.MilestoneScreen
import co.csedge.cubycare.ui.dashboard.MilestoneDetailScreen
import co.csedge.cubycare.ui.dashboard.NutritionHomeScreen
import co.csedge.cubycare.ui.dashboard.NutritionAgeDetailScreen
import co.csedge.cubycare.ui.dashboard.HealthTrackerAgeScreen
import co.csedge.cubycare.ui.dashboard.HealthTrackerDetailScreen
import co.csedge.cubycare.ui.dashboard.VaccineDetailScreen
import co.csedge.cubycare.ui.dashboard.VaccineFlowchartScreen
import co.csedge.cubycare.ui.dashboard.CubyJoyScreen
import co.csedge.cubycare.ui.dashboard.CubyJoyDetailScreen
import co.csedge.cubycare.ui.dashboard.CubyHealthSelectionScreen
import co.csedge.cubycare.ui.dashboard.CubySmileAgeScreen
import co.csedge.cubycare.ui.dashboard.CubySmileDetailScreen
import co.csedge.cubycare.ui.dashboard.CubyNapsAgeScreen
import co.csedge.cubycare.ui.dashboard.CubyNapsDetailScreen
import co.csedge.cubycare.ui.dashboard.MedicineTrackerScreen
import co.csedge.cubycare.ui.dashboard.AddMedicineScreen
import co.csedge.cubycare.ui.dashboard.MedicineFlowchartScreen
import co.csedge.cubycare.ui.dashboard.CubyChatScreen
import androidx.compose.foundation.isSystemInDarkTheme
import co.csedge.cubycare.ui.onboarding.OnboardingScreen
import co.csedge.cubycare.ui.profile.AddChildScreen
import co.csedge.cubycare.ui.profile.EditChildScreen
import co.csedge.cubycare.ui.profile.ProfileScreen
import co.csedge.cubycare.ui.settings.FAQScreen
import co.csedge.cubycare.ui.settings.SettingsScreen

import co.csedge.cubycare.ui.components.CubyBottomNavBar
import co.csedge.cubycare.ui.components.ChildBottomNavBar
import co.csedge.cubycare.ui.theme.CubyCareTheme
import com.google.firebase.auth.FirebaseAuth
import co.csedge.cubycare.utils.AppLanguageManager
import co.csedge.cubycare.utils.LocalAppLanguage
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FastOutLinearInEasing

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        co.csedge.cubycare.utils.AppTranslations.loadDynamicTranslations(this)
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "vaccine_reminders",
                "Vaccine Reminders",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }

        val workRequest = PeriodicWorkRequestBuilder<co.csedge.cubycare.worker.VaccineReminderWorker>(1, TimeUnit.DAYS).build()
        WorkManager.getInstance(this).enqueue(workRequest)

        setContent {
            val context = androidx.compose.ui.platform.LocalContext.current
            val prefs = remember { context.getSharedPreferences("cubycare_theme_prefs", android.content.Context.MODE_PRIVATE) }
            var themeMode by remember { mutableStateOf(prefs.getString("theme_mode", "Ocean Blue") ?: "Ocean Blue") }
            var currentLanguageCode by remember { mutableStateOf(AppLanguageManager.getSavedLanguage(context)) }

            CompositionLocalProvider(LocalAppLanguage provides currentLanguageCode) {
                CubyCareTheme(themeMode = themeMode) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        CubyCareApp(
                            themeMode = themeMode,
                            onThemeChange = { newMode ->
                                themeMode = newMode
                                prefs.edit().putString("theme_mode", newMode).apply()
                            },
                            currentLanguageCode = currentLanguageCode,
                            onLanguageChange = { newLang ->
                                currentLanguageCode = newLang
                                AppLanguageManager.saveLanguage(context, newLang)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CubyCareApp(
    themeMode: String = "System",
    onThemeChange: (String) -> Unit = {},
    currentLanguageCode: String = "en",
    onLanguageChange: (String) -> Unit = {}
) {
    val context = androidx.compose.ui.platform.LocalContext.current

    LaunchedEffect(currentLanguageCode) {
        AppLanguageManager.setLocale(context, currentLanguageCode)
    }

    val navController = rememberNavController()
    val viewModel: MainViewModel = viewModel()
    val children by viewModel.children.collectAsState()
    val prefs = remember { context.getSharedPreferences("cubycare_session", android.content.Context.MODE_PRIVATE) }
    val onboardingCompleted = prefs.getBoolean("onboarding_completed", false)
    
    // Check auth state for starting destination
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val isGuestLoggedIn = prefs.getBoolean("is_guest_logged_in", false)
    val startDest = if (currentUser != null || isGuestLoggedIn) "loading_or_dashboard" else if (onboardingCompleted) "auth" else "onboarding"

    LaunchedEffect(currentUser, isGuestLoggedIn) {
        if (currentUser == null && isGuestLoggedIn) {
            auth.signInAnonymously()
        }
        viewModel.refreshChildren()
    }

    NavHost(
        navController = navController,
        startDestination = startDest,
        enterTransition = { fadeIn(animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)) },
        exitTransition = { fadeOut(animationSpec = tween(durationMillis = 180, easing = FastOutLinearInEasing)) },
        popEnterTransition = { fadeIn(animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)) },
        popExitTransition = { fadeOut(animationSpec = tween(durationMillis = 180, easing = FastOutLinearInEasing)) }
    ) {
        composable("onboarding") {
            OnboardingScreen(
                onNavigateToAuth = {
                    prefs.edit().putBoolean("onboarding_completed", true).apply()
                    navController.navigate("auth") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }
        composable("auth") {
            AuthScreen(
                onLoginSuccess = {
                    prefs.edit().putBoolean("onboarding_completed", true).apply()
                    viewModel.refreshChildren()
                    navController.navigate("loading_or_dashboard") {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            )
        }
        composable("loading_or_dashboard") {
            // Wait for children to load to decide next screen
            val isLoading by viewModel.isLoading.collectAsState()
            
            if (!isLoading) {
                LaunchedEffect(children) {
                    if (children.isEmpty()) {
                        navController.navigate("add_child") {
                            popUpTo("loading_or_dashboard") { inclusive = true }
                        }
                    } else {
                        navController.navigate("dashboard") {
                            popUpTo("loading_or_dashboard") { inclusive = true }
                        }
                    }
                }
            } else {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    androidx.compose.material3.CircularProgressIndicator()
                }
            }
        }
        composable("add_child") {
            AddChildScreen(
                onChildAdded = { child ->
                    prefs.edit().putBoolean("onboarding_completed", true).apply()
                    viewModel.saveChild(child) {
                        navController.navigate("child_home/${child.id}") {
                            popUpTo("add_child") { inclusive = true }
                        }
                    }
                }
            )
        }

        composable("dashboard") {
            val currentRoute = navController.currentBackStackEntry?.destination?.route ?: "dashboard"
            DashboardScreen(
                children = children,
                onAddChildClick = { navController.navigate("add_child") },
                onChildClick = { childId -> navController.navigate("child_home/$childId") },
                onDeleteChild = { childId -> viewModel.deleteChild(childId) {} },
                onGlobalMedicineTrackerClick = {
                    if (children.isNotEmpty()) {
                        // If multiple children, ideally a selection screen. For now, route to first child's tracker.
                        navController.navigate("medicine_tracker/${children.first().id}")
                    }
                },
                onVaccineClick = { childId, vaccineId -> 
                    if (vaccineId != null) navController.navigate("vaccine_detail/$childId/$vaccineId")
                    else navController.navigate("vaccination/$childId")
                },
                onFeedingClick = { childId -> navController.navigate("nutrition/$childId") },
                onMedicineClick = { childId, medicineId -> 
                    if (medicineId != null) navController.navigate("medicine_flowchart/$childId/$medicineId")
                    else navController.navigate("medicine_tracker/$childId")
                },
                onSleepClick = { childId, ageRange -> navController.navigate("cuby_naps_detail/$childId/$ageRange") },
                onActivityClick = { childId, ageRange -> navController.navigate("cuby_joy_detail/$childId/$ageRange") },
                onAppointmentClick = { childId -> navController.navigate("doctor_appointments/$childId") },
                onAiChatClick = { navController.navigate("ai_chat") },
                bottomBar = { CubyBottomNavBar(navController, currentRoute) }
            )
        }
        composable("profile") {
            val currentRoute = navController.currentBackStackEntry?.destination?.route ?: "profile"
            ProfileScreen(
                children = children,
                onAddChildClick = { navController.navigate("add_child") },
                onSelectChild = { child -> navController.navigate("child_home/${child.id}") },
                onEditChild = { child -> navController.navigate("edit_child/${child.id}") },
                onAddAccountClick = {
                    prefs.edit().putBoolean("is_guest_logged_in", false).apply()
                    auth.signOut()
                    viewModel.refreshChildren()
                    navController.navigate("auth") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onSignOut = {
                    prefs.edit().putBoolean("is_guest_logged_in", false).apply()
                    auth.signOut()
                    viewModel.refreshChildren()
                    navController.navigate("auth") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                bottomBar = { CubyBottomNavBar(navController, currentRoute) }
            )
        }

        composable("settings") {
            val currentRoute = navController.currentBackStackEntry?.destination?.route ?: "settings"
            SettingsScreen(
                currentChild = children.firstOrNull(),
                allChildren = children,
                currentThemeMode = themeMode,
                onThemeChange = onThemeChange,
                currentLanguageCode = currentLanguageCode,
                onLanguageChange = onLanguageChange,
                onOpenFAQ = { navController.navigate("faq") },
                bottomBar = { CubyBottomNavBar(navController, currentRoute) }
            )
        }

        composable("ai_chat") {
            val currentRoute = navController.currentBackStackEntry?.destination?.route ?: "ai_chat"
            CubyChatScreen(
                navController = navController,
                activeChild = children.firstOrNull()
            )
        }

        composable("faq") {
            FAQScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable("nearby_pediatricians") {
            co.csedge.cubycare.ui.dashboard.NearbyPediatriciansScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable("child_home/{childId}") { backStackEntry ->
            val childId = backStackEntry.arguments?.getString("childId")
            val currentRoute = navController.currentBackStackEntry?.destination?.route ?: ""
            val child = children.find { it.id == childId } ?: children.firstOrNull()
            if (child != null) {
                ChildHomeScreen(
                    child = child,
                    onGrowthClick = { navController.navigate("growth/${child.id}") },
                    onVaccinationClick = { navController.navigate("vaccination/${child.id}") },
                    onMilestonesClick = { navController.navigate("milestones/${child.id}") },
                    onNutritionClick = { navController.navigate("nutrition/${child.id}") },
                    onHealthTrackerClick = { navController.navigate("doctor_appointments/${child.id}") },
                    onCubyJoyClick = { navController.navigate("cuby_joy/${child.id}") },
                    onDisorderClick = { navController.navigate("disorder_detail/${child.id}") },
                    onAllergyClick = { navController.navigate("allergy_detail/${child.id}") },
                    onMedicineTrackerClick = { navController.navigate("medicine_tracker/${child.id}") },
                    onCubyAlertClick = { navController.navigate("cuby_alert/${child.id}") },
                    onCubyParentingClick = { navController.navigate("cuby_parenting/${child.id}") },
                    onNapsClick = { navController.navigate("cuby_naps/${child.id}") },
                    onVitalsClick = { navController.navigate("health_tracker_vitals/${child.id}") },
                    onSmileClick = { navController.navigate("cuby_smile/${child.id}") },
                    onBackClick = { navController.popBackStack() },
                    onEditProfileClick = { navController.navigate("edit_child/${child.id}") },
                    onAiChatClick = { navController.navigate("ai_chat") },
                    onUpdateChild = { updatedChild ->
                        viewModel.saveChild(updatedChild) {}
                    }
                )
            } else {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    androidx.compose.material3.CircularProgressIndicator()
                }
            }
        }
        composable("edit_child/{childId}") { backStackEntry ->
            val childId = backStackEntry.arguments?.getString("childId")
            val child = children.find { it.id == childId }
            if (child != null) {
                EditChildScreen(
                    child = child,
                    onSaveChild = { updatedChild ->
                        viewModel.saveChild(updatedChild) {
                            navController.popBackStack()
                        }
                    },
                    onBack = { navController.popBackStack() }
                )
            }
        }
        composable("cuby_parenting/{childId}") {
            CubyParentingScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable("cuby_alert/{childId}") {
            CubyAlertScreen(
                onBack = { navController.popBackStack() },
                onOpenNearby = { navController.navigate("nearby_pediatricians") }
            )
        }
        composable("growth/{childId}") { backStackEntry ->
            val childId = backStackEntry.arguments?.getString("childId")
            val child = children.find { it.id == childId } ?: children.firstOrNull()
            if (child != null) {
                GrowthScreen(
                    child = child,
                    onBack = { navController.popBackStack() },
                    onUpdateChild = { updatedChild ->
                        viewModel.saveChild(updatedChild) {}
                    }
                )
            }
        }
        composable("vaccination/{childId}") { backStackEntry ->
            val childId = backStackEntry.arguments?.getString("childId")
            val child = children.find { it.id == childId } ?: children.firstOrNull()
            if (child != null) {
                VaccinationScreen(
                    child = child,
                    allChildren = children,
                    onSelectChild = { selected ->
                        navController.navigate("vaccination/${selected.id}") {
                            popUpTo("dashboard")
                        }
                    },
                    onBack = { navController.popBackStack() },
                    onUpdateChild = { updatedChild ->
                        viewModel.saveChild(updatedChild) {}
                    },
                    onVaccineClick = { vaccineId ->
                        navController.navigate("vaccine_detail/${child.id}/$vaccineId")
                    }
                )
            }
        }
        composable("nutrition/{childId}") { backStackEntry ->
            val childId = backStackEntry.arguments?.getString("childId")
            val child = children.find { it.id == childId } ?: children.firstOrNull()
            if (child != null) {
                NutritionHomeScreen(
                    child = child,
                    onBack = { navController.popBackStack() },
                    onAgeBlockClick = { ageRange ->
                        navController.navigate("nutrition_detail/${child.id}/$ageRange")
                    },
                    onUpdateChild = { updatedChild ->
                        viewModel.saveChild(updatedChild) {}
                    }
                )
            }
        }
        composable("nutrition_detail/{childId}/{ageRange}") { backStackEntry ->
            val childId = backStackEntry.arguments?.getString("childId")
            val ageRange = backStackEntry.arguments?.getString("ageRange")
            val child = children.find { it.id == childId } ?: children.firstOrNull()
            if (child != null && ageRange != null) {
                NutritionAgeDetailScreen(
                    child = child,
                    ageRange = ageRange,
                    onBack = { navController.popBackStack() }
                )
            }
        }
        composable("health_tracker/{childId}") { backStackEntry ->
            val childId = backStackEntry.arguments?.getString("childId")
            val child = children.find { it.id == childId } ?: children.firstOrNull()
            if (child != null) {
                CubyHealthSelectionScreen(
                    child = child,
                    onBack = { navController.popBackStack() },
                    onNapsClick = { navController.navigate("cuby_naps/${child.id}") },
                    onVitalsClick = { navController.navigate("health_tracker_vitals/${child.id}") },
                    onSmileClick = { navController.navigate("cuby_smile/${child.id}") }
                )
            }
        }
        composable("cuby_smile/{childId}") { backStackEntry ->
            val childId = backStackEntry.arguments?.getString("childId")
            val child = children.find { it.id == childId } ?: children.firstOrNull()
            if (child != null) {
                CubySmileAgeScreen(
                    child = child,
                    onBack = { navController.popBackStack() },
                    onAgeBlockClick = { ageRange ->
                        navController.navigate("cuby_smile_detail/${child.id}/$ageRange")
                    }
                )
            }
        }
        composable("cuby_smile_detail/{childId}/{ageRange}") { backStackEntry ->
            val childId = backStackEntry.arguments?.getString("childId")
            val ageRange = backStackEntry.arguments?.getString("ageRange")
            val child = children.find { it.id == childId } ?: children.firstOrNull()
            if (child != null && ageRange != null) {
                CubySmileDetailScreen(
                    child = child,
                    ageRange = ageRange,
                    onBack = { navController.popBackStack() }
                )
            }
        }
        composable("cuby_naps/{childId}") { backStackEntry ->
            val childId = backStackEntry.arguments?.getString("childId")
            val child = children.find { it.id == childId } ?: children.firstOrNull()
            if (child != null) {
                CubyNapsAgeScreen(
                    child = child,
                    onBack = { navController.popBackStack() },
                    onAgeBlockClick = { ageRange ->
                        navController.navigate("cuby_naps_detail/${child.id}/$ageRange")
                    }
                )
            }
        }
        composable("cuby_naps_detail/{childId}/{ageRange}") { backStackEntry ->
            val childId = backStackEntry.arguments?.getString("childId")
            val ageRange = backStackEntry.arguments?.getString("ageRange")
            val child = children.find { it.id == childId } ?: children.firstOrNull()
            if (child != null && ageRange != null) {
                CubyNapsDetailScreen(
                    child = child,
                    ageRange = ageRange,
                    onBack = { navController.popBackStack() }
                )
            }
        }
        composable("health_tracker_vitals/{childId}") { backStackEntry ->
            val childId = backStackEntry.arguments?.getString("childId")
            val child = children.find { it.id == childId } ?: children.firstOrNull()
            if (child != null) {
                HealthTrackerAgeScreen(
                    child = child,
                    onBack = { navController.popBackStack() },
                    onAgeBlockClick = { ageRange ->
                        navController.navigate("health_tracker_detail/${child.id}/$ageRange")
                    }
                )
            }
        }
        composable("health_tracker_detail/{childId}/{ageRange}") { backStackEntry ->
            val childId = backStackEntry.arguments?.getString("childId")
            val ageRange = backStackEntry.arguments?.getString("ageRange") ?: ""
            val child = children.find { it.id == childId } ?: children.firstOrNull()
            if (child != null) {
                HealthTrackerDetailScreen(
                    child = child,
                    ageRange = ageRange,
                    onBack = { navController.popBackStack() }
                )
            }
        }
        composable("doctor_appointments/{childId}") { backStackEntry ->
            val childId = backStackEntry.arguments?.getString("childId")
            val child = children.find { it.id == childId } ?: children.firstOrNull()
            if (child != null) {
                DoctorAppointmentsScreen(
                    child = child,
                    onBack = { navController.popBackStack() }
                )
            }
        }
        composable("milestones/{childId}") { backStackEntry ->
            val childId = backStackEntry.arguments?.getString("childId")
            val child = children.find { it.id == childId } ?: children.firstOrNull()
            if (child != null) {
                MilestoneScreen(
                    child = child,
                    onBack = { navController.popBackStack() },
                    onAgeBlockClick = { ageRange ->
                        navController.navigate("milestones_detail/${child.id}/$ageRange")
                    },
                    onUpdateChild = { updatedChild ->
                        viewModel.saveChild(updatedChild) {}
                    }
                )
            }
        }
        composable("milestones_detail/{childId}/{ageRange}") { backStackEntry ->
            val childId = backStackEntry.arguments?.getString("childId")
            val ageRange = backStackEntry.arguments?.getString("ageRange")
            val child = children.find { it.id == childId } ?: children.firstOrNull()
            if (child != null && ageRange != null) {
                MilestoneDetailScreen(
                    child = child,
                    ageRange = ageRange,
                    onBack = { navController.popBackStack() },
                    onUpdateChild = { updatedChild ->
                        viewModel.saveChild(updatedChild) {}
                    }
                )
            }
        }
        composable("vaccine_detail/{childId}/{vaccineId}") { backStackEntry ->
            val childId = backStackEntry.arguments?.getString("childId")
            val vaccineId = backStackEntry.arguments?.getString("vaccineId")
            val child = children.find { it.id == childId } ?: children.firstOrNull()
            val vaccine = child?.vaccines?.find { it.id == vaccineId } ?: child?.vaccines?.firstOrNull()
            
            if (child != null && vaccine != null) {
                VaccineDetailScreen(
                    child = child,
                    vaccine = vaccine,
                    onBack = { navController.popBackStack() },
                    onUpdateChild = { updatedChild ->
                        viewModel.saveChild(updatedChild) {}
                    },
                    onFlowchartClick = {
                        val encoded = android.net.Uri.encode(vaccine.name)
                        navController.navigate("vaccine_info/$encoded")
                    }
                )
            }
        }
        composable("vaccine_info/{vaccineName}") { backStackEntry ->
            val rawVaccineName = backStackEntry.arguments?.getString("vaccineName") ?: "Vaccine"
            val decodedVaccineName = android.net.Uri.decode(rawVaccineName)
            VaccineFlowchartScreen(
                vaccineName = decodedVaccineName,
                onBack = { navController.popBackStack() }
            )
        }
        composable("disorder_detail/{childId}") { backStackEntry ->
            val childId = backStackEntry.arguments?.getString("childId")
            val child = children.find { it.id == childId } ?: children.firstOrNull()
            if (child != null) {
                DisorderDetailScreen(
                    child = child,
                    onBack = { navController.popBackStack() }
                )
            }
        }
        composable("allergy_detail/{childId}") { backStackEntry ->
            val childId = backStackEntry.arguments?.getString("childId")
            val child = children.find { it.id == childId } ?: children.firstOrNull()
            if (child != null) {
                AllergyDetailScreen(
                    child = child,
                    onBack = { navController.popBackStack() }
                )
            }
        }
        composable("cuby_joy/{childId}") { backStackEntry ->
            val childId = backStackEntry.arguments?.getString("childId")
            val child = children.find { it.id == childId } ?: children.firstOrNull()
            if (child != null) {
                CubyJoyScreen(
                    child = child,
                    onBack = { navController.popBackStack() },
                    onAgeBlockClick = { ageRange ->
                        navController.navigate("cuby_joy_detail/${child.id}/$ageRange")
                    }
                )
            }
        }
        composable("cuby_joy_detail/{childId}/{ageRange}") { backStackEntry ->
            val childId = backStackEntry.arguments?.getString("childId")
            val ageRange = backStackEntry.arguments?.getString("ageRange")
            val child = children.find { it.id == childId } ?: children.firstOrNull()
            if (child != null && ageRange != null) {
                CubyJoyDetailScreen(
                    child = child,
                    ageRange = ageRange,
                    onBack = { navController.popBackStack() }
                )
            }
        }
        composable("medicine_tracker/{childId}") { backStackEntry ->
            val childId = backStackEntry.arguments?.getString("childId")
            val child = children.find { it.id == childId } ?: children.firstOrNull()
            if (child != null) {
                MedicineTrackerScreen(
                    child = child,
                    onBack = { navController.popBackStack() },
                    onAddMedicineClick = { navController.navigate("add_medicine/${child.id}") },
                    onMedicineClick = { medicine ->
                        navController.navigate("medicine_flowchart/${child.id}/${medicine.id}")
                    },
                    onUpdateChild = { updatedChild -> viewModel.saveChild(updatedChild) {} }
                )
            }
        }
        composable("add_medicine/{childId}") { backStackEntry ->
            val childId = backStackEntry.arguments?.getString("childId")
            val child = children.find { it.id == childId } ?: children.firstOrNull()
            if (child != null) {
                AddMedicineScreen(
                    child = child,
                    onBack = { navController.popBackStack() },
                    onUpdateChild = { updatedChild -> viewModel.saveChild(updatedChild) {} },
                    onMedicineAdded = { medicineId ->
                        // Pop AddMedicineScreen and navigate directly to the Flowchart
                        navController.popBackStack()
                        navController.navigate("medicine_flowchart/${child.id}/$medicineId")
                    }
                )
            }
        }
        composable("medicine_flowchart/{childId}/{medicineId}") { backStackEntry ->
            val childId = backStackEntry.arguments?.getString("childId")
            val medicineId = backStackEntry.arguments?.getString("medicineId")
            val child = children.find { it.id == childId } ?: children.firstOrNull()
            val medicine = child?.medicines?.find { it.id == medicineId } ?: child?.medicines?.firstOrNull()
            if (child != null && medicine != null) {
                MedicineFlowchartScreen(
                    medicineName = medicine.name,
                    flowchartInfo = medicine.flowchartInfo,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
