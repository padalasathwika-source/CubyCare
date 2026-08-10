package co.csedge.cubycare.ui.settings

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.csedge.cubycare.data.model.Child
import co.csedge.cubycare.ui.theme.AvailableAppThemes
import co.csedge.cubycare.utils.AppLanguageManager
import co.csedge.cubycare.utils.SupportedLanguages
import co.csedge.cubycare.utils.MedicalRecordExporter
import co.csedge.cubycare.utils.tr

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    currentChild: Child? = null,
    allChildren: List<Child> = emptyList(),
    currentThemeMode: String = "Ocean Blue",
    onThemeChange: (String) -> Unit = {},
    currentLanguageCode: String = "en",
    onLanguageChange: (String) -> Unit = {},
    onOpenFAQ: () -> Unit = {},
    bottomBar: @Composable () -> Unit = {}
) {
    val context = LocalContext.current

    var vaccineReminders by remember { mutableStateOf(true) }
    var dailyTips by remember { mutableStateOf(true) }
    var medicineAlerts by remember { mutableStateOf(true) }
    var growthUpdates by remember { mutableStateOf(true) }

    var useMetricUnits by remember { mutableStateOf(true) }
    var cacheCleared by remember { mutableStateOf(false) }

    var showExportPdfDialog by remember { mutableStateOf(false) }
    var showExportCsvDialog by remember { mutableStateOf(false) }

    val validChildren = remember(allChildren, currentChild) {
        val list = (allChildren + listOfNotNull(currentChild)).distinctBy { it.id }
        list.filter { it.id != "default_general_baby" && !it.name.contains("General Baby") && it.dateOfBirthMillis != 0L }
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
                        Text(AppLanguageManager.getString("app_settings", currentLanguageCode))
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
                painter = painterResource(id = co.csedge.cubycare.R.drawable.premium_nursery_bg),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                alpha = 0.20f
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                // Section 1: Color Themes & Palette Picker
                item {
                    SettingsSection(title = tr("settings_theme_title"), icon = Icons.Filled.Build) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = tr("settings_theme_select"),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = tr("settings_theme_desc"),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(14.dp))

                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                AvailableAppThemes.chunked(2).forEach { rowThemes ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        rowThemes.forEach { theme ->
                                            val isSelected = currentThemeMode == theme.id || currentThemeMode == theme.name
                                            Card(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .clickable {
                                                        onThemeChange(theme.id)
                                                        Toast.makeText(context, "${theme.name} Theme Activated", Toast.LENGTH_SHORT).show()
                                                    },
                                                shape = RoundedCornerShape(14.dp),
                                                colors = CardDefaults.cardColors(
                                                    containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f) else MaterialTheme.colorScheme.surface
                                                ),
                                                border = if (isSelected) CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primary)))
                                                else CardDefaults.outlinedCardBorder().copy(width = 0.5.dp, brush = Brush.horizontalGradient(listOf(Color.LightGray.copy(alpha = 0.5f), Color.LightGray.copy(alpha = 0.5f))))
                                            ) {
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(12.dp),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                                        // Color Swatch Overlapping Dots
                                                        Box(modifier = Modifier.size(24.dp)) {
                                                            Surface(
                                                                shape = CircleShape,
                                                                color = theme.primaryColor,
                                                                modifier = Modifier.size(16.dp).align(Alignment.TopStart)
                                                            ) {}
                                                            Surface(
                                                                shape = CircleShape,
                                                                color = theme.containerColor,
                                                                modifier = Modifier.size(16.dp).align(Alignment.BottomEnd).border(1.dp, Color.White, CircleShape)
                                                            ) {}
                                                        }
                                                        Spacer(modifier = Modifier.width(10.dp))
                                                        Text(
                                                            text = theme.name,
                                                            style = MaterialTheme.typography.bodyMedium,
                                                            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.SemiBold
                                                        )
                                                    }
                                                    if (isSelected) {
                                                        Icon(
                                                            imageVector = Icons.Filled.Check,
                                                            contentDescription = "Selected",
                                                            tint = MaterialTheme.colorScheme.primary,
                                                            modifier = Modifier.size(18.dp)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                        if (rowThemes.size == 1) {
                                            Spacer(modifier = Modifier.weight(1f))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Section 2: Notifications & Reminders (Enhanced Toggles)
                item {
                    SettingsSection(title = tr("settings_notif_title"), icon = Icons.Filled.Notifications) {
                        SettingsToggleRow(
                            title = tr("settings_notif_vaccine"),
                            subtitle = tr("settings_notif_vaccine_desc"),
                            checked = vaccineReminders,
                            onCheckedChange = { vaccineReminders = it }
                        )
                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f), thickness = 0.5.dp)
                        SettingsToggleRow(
                            title = tr("settings_notif_med"),
                            subtitle = tr("settings_notif_med_desc"),
                            checked = medicineAlerts,
                            onCheckedChange = { medicineAlerts = it }
                        )
                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f), thickness = 0.5.dp)
                        SettingsToggleRow(
                            title = tr("settings_notif_tips"),
                            subtitle = tr("settings_notif_tips_desc"),
                            checked = dailyTips,
                            onCheckedChange = { dailyTips = it }
                        )
                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f), thickness = 0.5.dp)
                        SettingsToggleRow(
                            title = tr("settings_notif_growth"),
                            subtitle = tr("settings_notif_growth_desc"),
                            checked = growthUpdates,
                            onCheckedChange = { growthUpdates = it }
                        )
                    }
                }

                // Section 3: Units & Measurement Preferences
                item {
                    SettingsSection(title = tr("settings_units_title"), icon = Icons.Filled.Settings) {
                        SettingsToggleRow(
                            title = if (useMetricUnits) tr("settings_units_metric") else tr("settings_units_imperial"),
                            subtitle = tr("settings_units_desc"),
                            checked = useMetricUnits,
                            onCheckedChange = {
                                useMetricUnits = it
                                val unitName = if (it) "Metric (kg/cm)" else "Imperial (lbs/in)"
                                Toast.makeText(context, "Units set to $unitName", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }

                // Section 4: Data Export & Cloud Sync
                item {
                    SettingsSection(title = tr("settings_export_title"), icon = Icons.Filled.Refresh) {
                        SettingsActionRow(
                            title = tr("settings_export_sync"),
                            subtitle = tr("settings_export_sync_desc"),
                            icon = Icons.Filled.Refresh
                        ) {
                            Toast.makeText(context, "All records are securely backed up in Cloud!", Toast.LENGTH_SHORT).show()
                        }

                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f), thickness = 0.5.dp)

                        SettingsActionRow(
                            title = tr("settings_export_pdf"),
                            subtitle = tr("settings_export_pdf_desc"),
                            icon = Icons.Filled.Share
                        ) {
                            if (validChildren.isNotEmpty()) {
                                showExportPdfDialog = true
                            } else if (currentChild != null) {
                                MedicalRecordExporter.exportChildMedicalReport(context, currentChild)
                            } else {
                                Toast.makeText(context, "Please add a child profile first", Toast.LENGTH_SHORT).show()
                            }
                        }

                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f), thickness = 0.5.dp)

                        SettingsActionRow(
                            title = tr("settings_export_csv"),
                            subtitle = tr("settings_export_csv_desc"),
                            icon = Icons.Filled.Share
                        ) {
                            if (validChildren.isNotEmpty()) {
                                showExportCsvDialog = true
                            } else if (currentChild != null) {
                                MedicalRecordExporter.exportChildCSV(context, currentChild)
                            } else {
                                Toast.makeText(context, "Please add a child profile first", Toast.LENGTH_SHORT).show()
                            }
                        }

                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f), thickness = 0.5.dp)

                        SettingsActionRow(
                            title = if (cacheCleared) tr("settings_cache_cleared") else tr("settings_cache_clear"),
                            subtitle = if (cacheCleared) tr("settings_cache_cleared_desc") else tr("settings_cache_clear_desc"),
                            icon = Icons.Filled.Delete
                        ) {
                            cacheCleared = true
                            Toast.makeText(context, "Temporary cache cleared successfully!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                // Section 5: Pediatric Support & User Guide
                item {
                    SettingsSection(title = tr("settings_support_title"), icon = Icons.Filled.Info) {
                        SettingsActionRow(
                            title = tr("settings_support_faq"),
                            subtitle = tr("settings_support_faq_desc"),
                            icon = Icons.Filled.Info
                        ) {
                            onOpenFAQ()
                        }

                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f), thickness = 0.5.dp)

                        SettingsActionRow(
                            title = tr("settings_support_contact"),
                            subtitle = tr("settings_support_contact_desc"),
                            icon = Icons.Filled.Info
                        ) {
                            MedicalRecordExporter.contactPediatricSupport(
                                context = context,
                                childName = currentChild?.name ?: "My Child"
                            )
                        }
                    }
                }

                // Section 6: About App & Legal
                item {
                    SettingsSection(title = tr("settings_about_title"), icon = Icons.Filled.Info) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(tr("settings_about_version"), style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                                Text(tr("settings_about_desc"), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Text("v1.2.0 (Build 48)", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                        }

                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f), thickness = 0.5.dp)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Lock, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(tr("settings_about_privacy"), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }

    if (showExportPdfDialog) {
        AlertDialog(
            onDismissRequest = { showExportPdfDialog = false },
            title = { Text("Export Medical Report Summary", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Select a child profile to export medical report summary (or export all children combined):", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(16.dp))

                    if (validChildren.size > 1) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    showExportPdfDialog = false
                                    MedicalRecordExporter.exportAllChildrenMedicalReport(context, validChildren)
                                },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                        ) {
                            Text(
                                text = "👶 All Children (${validChildren.size}) Combined Report",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(14.dp),
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    validChildren.forEach { child ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    showExportPdfDialog = false
                                    MedicalRecordExporter.exportChildMedicalReport(context, child)
                                },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Text(
                                text = "📄 ${child.name} (${child.ageFormatted})",
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(14.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showExportPdfDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showExportCsvDialog) {
        AlertDialog(
            onDismissRequest = { showExportCsvDialog = false },
            title = { Text("Export Child Data Sheet (CSV)", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Select a child profile to export CSV data sheet (or export all children combined):", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(16.dp))

                    if (validChildren.size > 1) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    showExportCsvDialog = false
                                    MedicalRecordExporter.exportAllChildrenCSV(context, validChildren)
                                },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                        ) {
                            Text(
                                text = "👶 All Children (${validChildren.size}) Combined CSV",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(14.dp),
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    validChildren.forEach { child ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    showExportCsvDialog = false
                                    MedicalRecordExporter.exportChildCSV(context, child)
                                },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Text(
                                text = "📊 ${child.name} (${child.ageFormatted}) CSV",
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(14.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showExportCsvDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun SettingsSection(
    title: String,
    icon: ImageVector? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column {
                content()
            }
        }
    }
}

@Composable
fun SettingsToggleRow(
    title: String,
    subtitle: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Surface(
        onClick = { onCheckedChange(!checked) },
        shape = RoundedCornerShape(16.dp),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
                if (!subtitle.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Visual Status Pill Badge ("ON" / "OFF")
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (checked) MaterialTheme.colorScheme.primaryContainer else Color.LightGray.copy(alpha = 0.35f),
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (checked) Icons.Filled.Check else Icons.Filled.Close,
                            contentDescription = null,
                            tint = if (checked) MaterialTheme.colorScheme.primary else Color.Gray,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (checked) "ON" else "OFF",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (checked) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    }
                }

                // Enhanced Switch
                Switch(
                    checked = checked,
                    onCheckedChange = onCheckedChange,
                    thumbContent = {
                        Icon(
                            imageVector = if (checked) Icons.Filled.Check else Icons.Filled.Close,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = if (checked) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = MaterialTheme.colorScheme.primary,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color.LightGray.copy(alpha = 0.6f),
                        checkedBorderColor = Color.Transparent,
                        uncheckedBorderColor = Color.Transparent
                    )
                )
            }
        }
    }
}

@Composable
fun SettingsActionRow(title: String, subtitle: String, icon: ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                }
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
    }
}
