package co.csedge.cubycare.ui.profile

import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.csedge.cubycare.data.model.Child
import co.csedge.cubycare.data.repository.AccountManager
import co.csedge.cubycare.data.repository.UserAccount
import co.csedge.cubycare.utils.MedicalRecordExporter
import co.csedge.cubycare.ui.components.ChildProfileAvatar
import com.google.firebase.auth.FirebaseAuth

import co.csedge.cubycare.utils.tr

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    children: List<Child> = emptyList(),
    onAddChildClick: () -> Unit = {},
    onSelectChild: (Child) -> Unit = {},
    onEditChild: (Child) -> Unit = {},
    onAddAccountClick: () -> Unit = {},
    onSignOut: () -> Unit = {},
    bottomBar: @Composable () -> Unit = {}
) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    val accountManager = remember { AccountManager(context) }
    var savedAccounts by remember { mutableStateOf(accountManager.getSavedAccounts()) }

    var showSignOutDialog by remember { mutableStateOf(false) }
    var accountToRemove by remember { mutableStateOf<UserAccount?>(null) }

    val prefs = remember(context) { context.getSharedPreferences("parent_profile_prefs", android.content.Context.MODE_PRIVATE) }
    var parentPhotoUriStr by remember(user) {
        mutableStateOf(
            prefs.getString("parent_photo_uri_${user?.email ?: "default"}", user?.photoUrl?.toString())
        )
    }
    var parentBitmap by remember(parentPhotoUriStr) { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(parentPhotoUriStr) {
        if (!parentPhotoUriStr.isNullOrBlank()) {
            try {
                val uri = Uri.parse(parentPhotoUriStr)
                val inputStream = context.contentResolver.openInputStream(uri)
                val decoded = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()
                if (decoded != null) {
                    parentBitmap = decoded.asImageBitmap()
                }
            } catch (e: Exception) {
                parentBitmap = null
            }
        } else {
            parentBitmap = null
        }
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            try {
                context.contentResolver.takePersistableUriPermission(
                    it,
                    android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (e: Exception) {
                // Ignore if not applicable
            }
            val uriString = it.toString()
            prefs.edit().putString("parent_photo_uri_${user?.email ?: "default"}", uriString).apply()
            parentPhotoUriStr = uriString
            Toast.makeText(context, "Parent profile photo updated!", Toast.LENGTH_SHORT).show()
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
                        Text(tr("parent_profile"))
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
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Parent Header Card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.85f),
                                            MaterialTheme.colorScheme.tertiary.copy(alpha = 0.85f)
                                        )
                                    )
                                )
                                .padding(20.dp)
                        ) {
                            val parentDisplayName = remember(user) {
                                co.csedge.cubycare.data.repository.getDerivedDisplayName(
                                    user?.email,
                                    user?.displayName,
                                    user?.isAnonymous
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(68.dp)
                                            .clickable { photoPickerLauncher.launch("image/*") }
                                    ) {
                                        if (parentBitmap != null) {
                                            Image(
                                                bitmap = parentBitmap!!,
                                                contentDescription = "Parent Profile Photo",
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier
                                                    .size(64.dp)
                                                    .clip(CircleShape)
                                                    .align(Alignment.Center)
                                            )
                                        } else {
                                            Surface(
                                                shape = CircleShape,
                                                color = Color.White.copy(alpha = 0.3f),
                                                modifier = Modifier
                                                    .size(64.dp)
                                                    .align(Alignment.Center)
                                            ) {
                                                Box(contentAlignment = Alignment.Center) {
                                                    Text(
                                                        text = parentDisplayName.take(1).uppercase(java.util.Locale.getDefault()),
                                                        style = MaterialTheme.typography.headlineMedium,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color.White
                                                    )
                                                }
                                            }
                                        }

                                        Surface(
                                            shape = CircleShape,
                                            color = MaterialTheme.colorScheme.primary,
                                            shadowElevation = 4.dp,
                                            modifier = Modifier
                                                .size(24.dp)
                                                .align(Alignment.BottomEnd)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                    imageVector = Icons.Filled.Edit,
                                                    contentDescription = "Edit Parent Photo",
                                                    tint = Color.White,
                                                    modifier = Modifier.size(12.dp)
                                                )
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Column {
                                    Text(
                                        text = parentDisplayName,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = if (user?.isAnonymous == true || user?.email == "guest@cubycare.app") "Signed in as Guest" else (user?.email ?: "No email linked"),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.White.copy(alpha = 0.9f)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = Color.White.copy(alpha = 0.2f)
                                    ) {
                                        Text(
                                            text = "${children.size} Children Registered",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Multi-Account Switcher Section
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Filled.AccountBox,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Switch Account / Multi-Accounts",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                TextButton(onClick = onAddAccountClick) {
                                    Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Add", fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            savedAccounts.forEach { acc ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            if (!acc.isCurrent) {
                                                accountManager.switchAccount(acc.email)
                                                savedAccounts = accountManager.getSavedAccounts()
                                                Toast
                                                    .makeText(context, "Switching session to ${acc.displayName}", Toast.LENGTH_SHORT)
                                                    .show()
                                                auth.signOut()
                                                onSignOut()
                                            }
                                        }
                                        .padding(vertical = 10.dp, horizontal = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                        Surface(
                                            shape = CircleShape,
                                            color = if (acc.isCurrent) MaterialTheme.colorScheme.primary else Color.LightGray.copy(alpha = 0.5f),
                                            modifier = Modifier.size(36.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Text(
                                                    text = acc.displayName.take(1).uppercase(),
                                                    color = Color.White,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(acc.displayName, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                                            Text(acc.email, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                    }

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        if (acc.isCurrent) {
                                            Surface(
                                                shape = RoundedCornerShape(12.dp),
                                                color = MaterialTheme.colorScheme.primaryContainer
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(Icons.Filled.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(14.dp))
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text("Active", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                                                }
                                            }
                                            Spacer(modifier = Modifier.width(4.dp))
                                        }

                                        IconButton(
                                            onClick = { accountToRemove = acc },
                                            modifier = Modifier.size(36.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Delete,
                                                contentDescription = "Remove Account",
                                                tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f),
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                }
                                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f), thickness = 0.5.dp)
                            }

                            TextButton(
                                onClick = onAddAccountClick,
                                modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                            ) {
                                Icon(Icons.Filled.Add, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("+ Sign in to another parent account")
                            }
                        }
                    }
                }

                // Children Section: Switch & Manage Profiles
                item {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Children Profiles",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            TextButton(onClick = onAddChildClick) {
                                Icon(Icons.Filled.Add, contentDescription = "Add Child", modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(tr("add_child"), fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        if (children.isEmpty()) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onAddChildClick() },
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(20.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Filled.Add,
                                        contentDescription = "Add",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(32.dp)
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Column {
                                        Text(
                                            "No children added yet",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            "Tap here to create your baby's profile",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                children.forEach { child ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { onSelectChild(child) },
                                        shape = RoundedCornerShape(16.dp),
                                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                ChildProfileAvatar(
                                                    profileImageUri = child.profileImageUri,
                                                    name = child.name,
                                                    size = 54.dp
                                                )
                                                Spacer(modifier = Modifier.width(14.dp))
                                                Column {
                                                    Text(
                                                        text = child.name,
                                                        style = MaterialTheme.typography.titleMedium,
                                                        fontWeight = FontWeight.Bold,
                                                        color = MaterialTheme.colorScheme.primary
                                                    )
                                                    Spacer(modifier = Modifier.height(2.dp))
                                                    Text(
                                                        text = "Age: ${child.ageFormatted} | ${child.gender}",
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                }
                                            }

                                             Row(verticalAlignment = Alignment.CenterVertically) {
                                                val isGeneral = child.id == "default_general_baby" || child.name.contains("General Baby") || child.dateOfBirthMillis == 0L
                                                if (!isGeneral) {
                                                    IconButton(onClick = { onEditChild(child) }) {
                                                        Icon(
                                                            imageVector = Icons.Filled.Edit,
                                                            contentDescription = "Edit Profile",
                                                            tint = MaterialTheme.colorScheme.primary
                                                        )
                                                    }
                                                }
                                                Icon(
                                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                                    contentDescription = "Open Dashboard",
                                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                        }

                                        // Medical Report & CSV Export Buttons for this child
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 10.dp),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            OutlinedButton(
                                                onClick = { MedicalRecordExporter.exportChildMedicalReport(context, child) },
                                                modifier = Modifier.weight(1f),
                                                shape = RoundedCornerShape(10.dp),
                                                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
                                            ) {
                                                Text("📄 Medical Report", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                            }
                                            OutlinedButton(
                                                onClick = { MedicalRecordExporter.exportChildCSV(context, child) },
                                                modifier = Modifier.weight(1f),
                                                shape = RoundedCornerShape(10.dp),
                                                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
                                            ) {
                                                Text("📊 Data Sheet (CSV)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Account & Sign Out Section
                item {
                    Button(
                        onClick = { showSignOutDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.9f))
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Sign Out")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(tr("sign_out"), style = MaterialTheme.typography.titleMedium, color = Color.White)
                    }
                }
            }
        }

    if (showSignOutDialog) {
        AlertDialog(
            onDismissRequest = { showSignOutDialog = false },
            title = { Text(tr("sign_out")) },
            text = { Text("Are you sure you want to sign out of CubyCare?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSignOutDialog = false
                        context.getSharedPreferences("cubycare_session", android.content.Context.MODE_PRIVATE)
                            .edit().putBoolean("is_guest_logged_in", false).apply()
                        auth.signOut()
                        onSignOut()
                    }
                ) {
                    Text(tr("sign_out"), color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showSignOutDialog = false }) {
                    Text(tr("cancel"))
                }
            }
        )
    }

    if (accountToRemove != null) {
        val targetAcc = accountToRemove!!
        AlertDialog(
            onDismissRequest = { accountToRemove = null },
            title = { Text(tr("remove_account")) },
            text = { Text("Are you sure you want to remove ${targetAcc.displayName} (${targetAcc.email}) from saved accounts on this device?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        val isCurrent = targetAcc.isCurrent
                        accountManager.removeAccount(targetAcc.email)
                        savedAccounts = accountManager.getSavedAccounts()
                        accountToRemove = null
                        if (isCurrent) {
                            Toast.makeText(context, "Active account removed", Toast.LENGTH_SHORT).show()
                            auth.signOut()
                            onSignOut()
                        } else {
                            Toast.makeText(context, "Account removed", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text("Remove", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { accountToRemove = null }) {
                    Text(tr("cancel"))
                }
            }
        )
    }
}
}
