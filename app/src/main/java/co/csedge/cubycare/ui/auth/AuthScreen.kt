package co.csedge.cubycare.ui.auth

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

@Composable
fun AuthScreen(onLoginSuccess: () -> Unit) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Phone Auth State
    var showPhoneAuth by remember { mutableStateOf(false) }
    var countryCode by remember { mutableStateOf("+91") }
    var phoneNumber by remember { mutableStateOf("") }
    var otpCode by remember { mutableStateOf("") }
    var isOtpSent by remember { mutableStateOf(false) }
    var storedVerificationId by remember { mutableStateOf<String?>(null) }

    // Google Sign-In Launcher
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential)
                    .addOnCompleteListener { authTask ->
                        isLoading = false
                        if (authTask.isSuccessful) {
                            context.getSharedPreferences("cubycare_session", android.content.Context.MODE_PRIVATE)
                                .edit()
                                .putBoolean("is_guest_logged_in", false)
                                .putBoolean("onboarding_completed", true)
                                .apply()
                            val user = auth.currentUser
                            if (user != null) {
                                val email = user.email ?: "parent@cubycare.app"
                                val name = user.displayName ?: "Parent Account"
                                co.csedge.cubycare.data.repository.AccountManager(context).addAccount(email, name)
                            }
                            onLoginSuccess()
                        } else {
                            errorMessage = authTask.exception?.message ?: "Authentication failed"
                        }
                    }
            } catch (e: ApiException) {
                isLoading = false
                Log.w("AuthScreen", "Google sign in failed with code ${e.statusCode}", e)
                if (e.statusCode == 10 || e.statusCode == 12500) {
                    errorMessage = "Google Sign-In requires SHA-1 fingerprint registration in Firebase."
                } else {
                    errorMessage = "Google Sign-In failed: ${e.statusCode}"
                }
            }
        } else {
            isLoading = false
        }
    }

    // Phone Auth Callbacks
    val callbacks = remember {
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                auth.signInWithCredential(credential).addOnCompleteListener { task ->
                    isLoading = false
                    if (task.isSuccessful) {
                        val cleanPhone = phoneNumber.filter { it.isDigit() }
                        val accountEmail = "phone_${cleanPhone}@cubycare.app"
                        context.getSharedPreferences("cubycare_session", android.content.Context.MODE_PRIVATE)
                            .edit()
                            .putBoolean("is_guest_logged_in", false)
                            .putBoolean("onboarding_completed", true)
                            .apply()
                        co.csedge.cubycare.data.repository.AccountManager(context).addAccount(accountEmail, "Parent ($phoneNumber)")
                        onLoginSuccess()
                    } else {
                        errorMessage = task.exception?.message ?: "Phone login failed"
                    }
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                isLoading = false
                errorMessage = e.message ?: "Phone verification failed"
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                isLoading = false
                storedVerificationId = verificationId
                isOtpSent = true
                Toast.makeText(context, "OTP Sent to $phoneNumber", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = co.csedge.cubycare.R.drawable.premium_nursery_bg),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                alpha = 0.35f
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(28.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // APP LOGO AT TOP
                Image(
                    painter = painterResource(id = co.csedge.cubycare.R.drawable.app_logo),
                    contentDescription = "CubyCare Logo",
                    modifier = Modifier
                        .size(110.dp)
                        .padding(bottom = 12.dp),
                    contentScale = ContentScale.Fit
                )

                Text(
                    text = "Welcome to CubyCare",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Monitor your baby's growth, milestones & health",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(40.dp))

                if (errorMessage != null) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                    ) {
                        Text(
                            text = errorMessage!!,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }

                // 1. SIGN IN WITH GOOGLE BUTTON
                Button(
                    onClick = {
                        isLoading = true
                        errorMessage = null
                        val webClientId = try {
                            context.getString(co.csedge.cubycare.R.string.default_web_client_id)
                        } catch (e: Exception) {
                            "830009836557-d01trpcggusui5lf3rhl9mmu1fmj4svo.apps.googleusercontent.com"
                        }

                        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(webClientId)
                            .requestEmail()
                            .build()
                        val googleSignInClient = GoogleSignIn.getClient(context, gso)
                        googleSignInClient.signOut().addOnCompleteListener {
                            launcher.launch(googleSignInClient.signInIntent)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    ),
                    enabled = !isLoading
                ) {
                    if (isLoading && !showPhoneAuth) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White
                        )
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = co.csedge.cubycare.R.drawable.app_logo),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp).padding(end = 8.dp)
                            )
                            Text(
                                text = "Sign in with Google Mail",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 2. LOGIN USING PHONE NUMBER BUTTON
                OutlinedButton(
                    onClick = {
                        showPhoneAuth = !showPhoneAuth
                        errorMessage = null
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Call,
                        contentDescription = "Phone Login",
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (showPhoneAuth) "Hide Phone Login" else "Login using Phone Number",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                // PHONE NUMBER LOGIN EXPANDABLE FORM
                if (showPhoneAuth) {
                    Spacer(modifier = Modifier.height(20.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "📱 Phone Number Login",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            // Separate Country Code & Mobile Number Fields
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = countryCode,
                                    onValueChange = { countryCode = it },
                                    label = { Text("Code") },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                    modifier = Modifier.width(90.dp)
                                )

                                OutlinedTextField(
                                    value = phoneNumber,
                                    onValueChange = { phoneNumber = it },
                                    label = { Text("Mobile Number") },
                                    placeholder = { Text("Enter mobile number") },
                                    leadingIcon = { Icon(Icons.Filled.Call, contentDescription = null) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    val cleanPhone = phoneNumber.trim()
                                    if (cleanPhone.isBlank()) {
                                        Toast.makeText(context, "Please enter your mobile number", Toast.LENGTH_SHORT).show()
                                        return@Button
                                    }
                                    val fullPhone = "${countryCode.trim()} $cleanPhone"
                                    val digitPhone = cleanPhone.filter { it.isDigit() }
                                    val accountEmail = "phone_${digitPhone}@cubycare.app"

                                    context.getSharedPreferences("cubycare_session", android.content.Context.MODE_PRIVATE)
                                        .edit()
                                        .putBoolean("is_guest_logged_in", false)
                                        .putBoolean("onboarding_completed", true)
                                        .apply()

                                    co.csedge.cubycare.data.repository.AccountManager(context).addAccount(accountEmail, "Parent ($fullPhone)")
                                    Toast.makeText(context, "Logged in with $fullPhone", Toast.LENGTH_SHORT).show()
                                    onLoginSuccess()
                                },
                                modifier = Modifier.fillMaxWidth().height(48.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Continue with Phone Number", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
