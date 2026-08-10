package co.csedge.cubycare.ui.dashboard

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import co.csedge.cubycare.data.model.Child
import co.csedge.cubycare.ui.components.ChildProfileAvatar
import co.csedge.cubycare.utils.tr

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChildHomeScreen(
    child: Child,
    onGrowthClick: () -> Unit,
    onVaccinationClick: () -> Unit,
    onMilestonesClick: () -> Unit,
    onNutritionClick: () -> Unit,
    onHealthTrackerClick: () -> Unit,
    onCubyJoyClick: () -> Unit,
    onDisorderClick: () -> Unit,
    onAllergyClick: () -> Unit,
    onMedicineTrackerClick: () -> Unit,
    onCubyAlertClick: () -> Unit,
    onCubyParentingClick: () -> Unit,
    onBackClick: () -> Unit,
    onNapsClick: () -> Unit = {},
    onVitalsClick: () -> Unit = {},
    onSmileClick: () -> Unit = {},
    onEditProfileClick: () -> Unit = {},
    onAiChatClick: () -> Unit = {},
    onUpdateChild: (Child) -> Unit = {},
    bottomBar: @Composable () -> Unit = {}
) {
    val photoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val updatedChild = child.copy(profileImageUri = it.toString())
            onUpdateChild(updatedChild)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = co.csedge.cubycare.R.drawable.app_logo),
                            contentDescription = "CubyCare Logo",
                            modifier = Modifier.size(32.dp).padding(end = 8.dp)
                        )
                        Text("${child.name}'s Dashboard")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
                    val isGeneralProfile = child.id == "default_general_baby" || child.dateOfBirthMillis == 0L || child.name.contains("General Baby")
                    if (!isGeneralProfile) {
                        IconButton(onClick = onEditProfileClick) {
                            Icon(Icons.Filled.Edit, contentDescription = "Edit Profile", tint = MaterialTheme.colorScheme.primary)
                        }
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
        val isGeneralProfile = child.id == "default_general_baby" || child.dateOfBirthMillis == 0L || child.name.contains("General Baby")
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = co.csedge.cubycare.R.drawable.premium_nursery_bg),
                contentDescription = "Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                alpha = 0.65f
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header Card with Profile Photo Upload / Edit support
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                contentAlignment = Alignment.BottomEnd,
                                modifier = if (!isGeneralProfile) Modifier.clickable { photoLauncher.launch("image/*") } else Modifier
                            ) {
                                ChildProfileAvatar(
                                    profileImageUri = child.profileImageUri,
                                    name = child.name,
                                    size = 68.dp
                                )
                                if (!isGeneralProfile) {
                                    Surface(
                                        shape = CircleShape,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = Icons.Filled.AccountCircle,
                                                contentDescription = "Upload/Change Photo",
                                                tint = Color.White,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = child.name,
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                if (!isGeneralProfile) {
                                    Text(
                                        text = if (child.profileImageUri.isNotBlank()) "Tap avatar to change photo" else "Tap avatar to upload photo",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.clickable { photoLauncher.launch("image/*") }
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                }
                                Text(
                                    text = "Age: ${child.ageFormatted} | Gender: ${child.gender}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                if (child.isPremature) {
                                    Text(
                                        text = "Premature Born (${child.prematureMonths})",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.tertiary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        IconButton(onClick = onEditProfileClick) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = "Edit All Details",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Row 1: Growth & Milestones and Cuby Naps
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SquareBlock(
                        title = "Growth &\nMilestones 📈",
                        color = Color(0xFFFCE4EC),
                        onClick = onGrowthClick,
                        imageRes = co.csedge.cubycare.R.drawable.growth_charts_icon
                    )

                    SquareBlock(
                        title = "Cuby Naps 😴",
                        color = Color(0xFFE8EAF6),
                        onClick = onNapsClick,
                        imageRes = co.csedge.cubycare.R.drawable.cuby_naps_icon
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Row 2: Cuby Alert and Vaccination Medical Tracker
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SquareBlock(
                        title = tr("cuby_alert"),
                        color = Color(0xFFFFCDD2),
                        onClick = onCubyAlertClick,
                        imageRes = co.csedge.cubycare.R.drawable.alert
                    )
                    
                    SquareBlock(
                        title = tr("vaccines"),
                        color = Color(0xFFE3F2FD),
                        onClick = onVaccinationClick,
                        imageRes = co.csedge.cubycare.R.drawable.vaccine_block_icon
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
                
                // Row 3: Nutrition & Food Diary and Cuby Vitals & Symptoms
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SquareBlock(
                        title = tr("food_diary"),
                        color = Color(0xFFE4F2D8),
                        onClick = onNutritionClick,
                        imageRes = co.csedge.cubycare.R.drawable.nutrition_food_diary
                    )

                    SquareBlock(
                        title = "Cuby Vitals\n& Symptoms 🩺",
                        color = Color(0xFFFCE4EC),
                        onClick = onVitalsClick,
                        imageRes = co.csedge.cubycare.R.drawable.vital
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
                
                // Row 4: Cuby Smile and Medicines
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SquareBlock(
                        title = "Cuby Smile 🦷",
                        color = Color(0xFFE0F7FA),
                        onClick = onSmileClick,
                        imageRes = co.csedge.cubycare.R.drawable.dental
                    )

                    SquareBlock(
                        title = tr("medicines"),
                        color = Color(0xFFE0F7FA),
                        onClick = onMedicineTrackerClick,
                        imageRes = co.csedge.cubycare.R.drawable.medicine_tracker
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
                
                // Row 5: Doctor Appointments and Play & Joy
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SquareBlock(
                        title = "Doctor\nAppointments 📅",
                        color = Color(0xFFF3E5F5),
                        onClick = onHealthTrackerClick,
                        imageRes = co.csedge.cubycare.R.drawable.cuby_health_icon
                    )
                    
                    SquareBlock(
                        title = tr("play_joy"),
                        color = Color(0xFFFFE0B2),
                        onClick = onCubyJoyClick,
                        imageRes = co.csedge.cubycare.R.drawable.cuby_joy_icon
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
                
                // Row 6: Parenting Guide and Genetic Disorders / Allergy Care
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SquareBlock(
                        title = tr("parenting_guide"),
                        color = Color(0xFFB2DFDB),
                        onClick = onCubyParentingClick,
                        imageRes = co.csedge.cubycare.R.drawable.cuby_parenting_icon
                    )
                    
                    if (child.geneticIssues.isNotBlank() && child.geneticIssues.lowercase().trim() != "none") {
                        val title = if (child.geneticIssues.contains(",")) "Genetic\nDisorders" else child.geneticIssues.trim().replaceFirstChar { it.uppercase() }
                        SquareBlock(
                            title = title,
                            color = Color(0xFFFFF9C4),
                            onClick = onDisorderClick,
                            imageRes = co.csedge.cubycare.R.drawable.disorders_care_icon
                        )
                    } else if (child.allergies.isNotBlank() && child.allergies.lowercase().trim() != "none") {
                        val title = if (child.allergies.contains(",")) "Multiple\nAllergies" else child.allergies.trim().replaceFirstChar { it.uppercase() } + "\nAllergy"
                        SquareBlock(
                            title = title,
                            color = Color(0xFFFFE0B2),
                            onClick = onAllergyClick,
                            imageRes = co.csedge.cubycare.R.drawable.allergy_care_icon
                        )
                    } else {
                        Spacer(modifier = Modifier.size(140.dp))
                    }
                }

                // Malla Reddy Hospital Feature at Very Bottom
                Spacer(modifier = Modifier.height(20.dp))
                val context = androidx.compose.ui.platform.LocalContext.current
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
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "🏥 Malla Reddy Narayana Hospital",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF4A148C)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Speciality: Pediatrics & Neonatology Department",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF6A1B9A)
                            )
                        }
                        Button(
                            onClick = {
                                co.csedge.cubycare.utils.HospitalUtils.openPediatricsSpecialty(context)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7B1FA2)),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text("Book / Visit", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun SquareBlock(
    title: String,
    color: Color,
    onClick: () -> Unit,
    imageRes: Int? = null,
    vectorIcon: ImageVector? = null,
    iconTint: Color = Color.Unspecified,
    onDelete: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .size(140.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (onDelete != null) {
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.align(Alignment.TopEnd).size(36.dp).padding(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete",
                        tint = Color.Red.copy(alpha = 0.7f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Column(
                modifier = Modifier.fillMaxSize().padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (imageRes != null) {
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = title,
                        modifier = Modifier.size(60.dp).padding(bottom = 6.dp),
                        contentScale = ContentScale.Fit
                    )
                } else if (vectorIcon != null) {
                    Icon(
                        imageVector = vectorIcon,
                        contentDescription = title,
                        tint = if (iconTint != Color.Unspecified) iconTint else Color(0xFF37474F),
                        modifier = Modifier.size(54.dp).padding(bottom = 6.dp)
                    )
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
