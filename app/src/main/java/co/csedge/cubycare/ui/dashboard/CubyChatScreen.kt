package co.csedge.cubycare.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import co.csedge.cubycare.R
import co.csedge.cubycare.data.model.Child
import co.csedge.cubycare.data.repository.AIResponse
import co.csedge.cubycare.data.repository.CubyAIChatEngine
import co.csedge.cubycare.utils.tr
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val sender: MessageSender,
    val text: String,
    val timestamp: String = java.text.SimpleDateFormat("hh:mm a", java.util.Locale.getDefault()).format(java.util.Date()),
    val routeToOpen: String? = null,
    val routeLabel: String? = null
)

enum class MessageSender { USER, AI }

object CubyAIChatSessionStore {
    val messages = mutableStateListOf<ChatMessage>()

    fun initIfNeeded(welcomeText: String) {
        if (messages.isEmpty()) {
            messages.add(
                ChatMessage(
                    sender = MessageSender.AI,
                    text = welcomeText
                )
            )
        }
    }

    fun clear(welcomeText: String) {
        messages.clear()
        messages.add(
            ChatMessage(
                sender = MessageSender.AI,
                text = welcomeText
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CubyChatScreen(
    navController: NavController,
    activeChild: Child? = null
) {
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    var inputText by remember { mutableStateOf("") }
    var isThinking by remember { mutableStateOf(false) }

    val welcomeText = tr("ai_chat_welcome_msg")
    
    LaunchedEffect(welcomeText) {
        CubyAIChatSessionStore.initIfNeeded(welcomeText)
    }

    val messages = CubyAIChatSessionStore.messages

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            kotlin.runCatching {
                listState.animateScrollToItem(messages.size - 1)
            }
        }
    }

    val suggestionKeys = listOf(
        "ai_chip_vaccine",
        "ai_chip_alert",
        "ai_chip_milestone",
        "ai_chip_nutrition",
        "ai_chip_sleep"
    )

    val context = LocalContext.current
    var ttsEngine by remember { mutableStateOf<TextToSpeech?>(null) }
    var currentlySpeakingId by remember { mutableStateOf<String?>(null) }

    DisposableEffect(context) {
        var tts: TextToSpeech? = null
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = java.util.Locale.getDefault()
                try {
                    val voices = tts?.voices
                    if (voices != null) {
                        val femaleVoice = voices.find { voice ->
                            val vName = voice.name.lowercase()
                            vName.contains("female") || 
                            vName.contains("woman") || 
                            vName.contains("en-us-x-sfg") || 
                            vName.contains("en-in-x-cfa") ||
                            vName.contains("hi-in-x-hie")
                        }
                        if (femaleVoice != null) {
                            tts?.voice = femaleVoice
                        }
                    }
                } catch (e: Exception) {
                    // Fallback
                }
                tts?.setPitch(1.22f)
                tts?.setSpeechRate(0.92f)
                tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        currentlySpeakingId = utteranceId
                    }
                    override fun onDone(utteranceId: String?) {
                        currentlySpeakingId = null
                    }
                    @Deprecated("Deprecated in Java")
                    override fun onError(utteranceId: String?) {
                        currentlySpeakingId = null
                    }
                })
                ttsEngine = tts
            }
        }
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }

    fun speakMessage(id: String, rawText: String) {
        if (currentlySpeakingId == id) {
            ttsEngine?.stop()
            currentlySpeakingId = null
        } else {
            ttsEngine?.stop()
            currentlySpeakingId = id
            val cleanText = rawText
                .replace(Regex("Open Screen:.*"), "")
                .replace(Regex("[#*`_]"), "")
                .replace("CubyCare", "Kahbee Care", ignoreCase = true)
                .replace("Cuby", "Kahbee", ignoreCase = true)
                .replace("cuby", "kahbee", ignoreCase = true)
                .trim()
            val params = Bundle()
            params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, id)
            ttsEngine?.speak(cleanText, TextToSpeech.QUEUE_FLUSH, params, id)
        }
    }

    fun sendMessage(query: String) {
        val cleanQuery = query.trim()
        if (cleanQuery.isEmpty() || isThinking) return

        val userMsg = ChatMessage(sender = MessageSender.USER, text = cleanQuery)
        messages.add(userMsg)
        inputText = ""
        isThinking = true

        coroutineScope.launch {
            try {
                kotlin.runCatching { listState.animateScrollToItem(messages.size - 1) }
                delay(300)

                val aiResult: AIResponse = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Default) {
                    CubyAIChatEngine.askQuestion(cleanQuery, activeChild)
                }

                val aiMsg = ChatMessage(
                    sender = MessageSender.AI,
                    text = aiResult.answer,
                    routeToOpen = aiResult.routeToOpen,
                    routeLabel = aiResult.routeLabel
                )
                messages.add(aiMsg)
            } catch (e: Exception) {
                e.printStackTrace()
                messages.add(
                    ChatMessage(
                        sender = MessageSender.AI,
                        text = "I'm sorry, I encountered a temporary issue while answering. Please try asking your question again!"
                    )
                )
            } finally {
                isThinking = false
                kotlin.runCatching { listState.animateScrollToItem(messages.size - 1) }
            }
        }
    }

    val speechRecognizerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val data = result.data
            val results = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (!results.isNullOrEmpty()) {
                val spokenText = results[0]
                if (spokenText.isNotBlank()) {
                    inputText = spokenText
                    sendMessage(spokenText)
                }
            }
        }
    }

    fun startVoiceRecording() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, java.util.Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Ask Cuby AI Assistant...")
        }
        try {
            speechRecognizerLauncher.launch(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Voice speech recognition not available", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.cuby_ai_robot_mother_baby),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = tr("ai_chat_title"),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.DarkGray
                            )
                            Text(
                                text = tr("ai_chat_subtitle"),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.DarkGray
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        CubyAIChatSessionStore.clear(welcomeText)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Clear Chat",
                            tint = Color.Gray
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.cuby_ai_robot_mother_baby),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                alpha = 0.22f
            )

            // Light dusky overlay for elegant contrast
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFF2F0F7).copy(alpha = 0.75f),
                                Color(0xFFE8E5F0).copy(alpha = 0.85f)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Quick Suggestion Chips Row
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(suggestionKeys) { key ->
                        val labelText = tr(key)
                        SuggestionChip(
                            onClick = { sendMessage(labelText) },
                            label = { Text(labelText, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary) },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = Color.White.copy(alpha = 0.9f)
                            ),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)),
                            shape = RoundedCornerShape(16.dp)
                        )
                    }
                }

                // Chat Messages List
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    items(messages, key = { it.id }) { msg ->
                        ChatBubbleItem(
                            msg = msg,
                            onRouteClick = { route ->
                                if (!route.isNullOrBlank()) {
                                    val targetChildId = activeChild?.id ?: "default_general_baby"
                                    val encodedChildId = android.net.Uri.encode(targetChildId)

                                    val targetDestination = when (route) {
                                        "vaccines", "vaccination" -> "vaccination/$encodedChildId"
                                        "cuby_alert", "alert" -> "cuby_alert/$encodedChildId"
                                        "milestones", "growth" -> "growth/$encodedChildId"
                                        "nutrition" -> "nutrition/$encodedChildId"
                                        "cuby_parenting", "parenting" -> "cuby_parenting/$encodedChildId"
                                        "health_tracker", "doctor_appointments" -> "doctor_appointments/$encodedChildId"
                                        "cuby_naps", "naps" -> "cuby_naps/$encodedChildId"
                                        "cuby_smile", "smile" -> "cuby_smile/$encodedChildId"
                                        "allergies", "allergy_detail" -> "allergy_detail/$encodedChildId"
                                        "disorders", "disorder_detail" -> "disorder_detail/$encodedChildId"
                                        "cuby_joy", "play_joy" -> "cuby_joy/$encodedChildId"
                                        "medicines", "medicine_tracker" -> "medicine_tracker/$encodedChildId"
                                        "profile" -> "profile"
                                        "dashboard" -> "dashboard"
                                        "settings" -> "settings"
                                        else -> {
                                            if (route.contains("/")) route else "child_home/$encodedChildId"
                                        }
                                    }

                                    try {
                                        navController.navigate(targetDestination)
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                        try {
                                            navController.navigate("dashboard")
                                        } catch (_: Exception) {}
                                    }
                                }
                            },
                            currentlySpeakingId = currentlySpeakingId,
                            onSpeakClick = { id, text -> speakMessage(id, text) }
                        )
                    }

                    if (isThinking) {
                        item {
                            TypingIndicatorItem()
                        }
                    }
                }

                // Input Bar
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shadowElevation = 8.dp,
                    color = Color.White
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Mic Recording Button
                        IconButton(
                            onClick = { startVoiceRecording() },
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_mic),
                                contentDescription = "Voice Record",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        OutlinedTextField(
                            value = inputText,
                            onValueChange = { inputText = it },
                            placeholder = { Text(tr("ai_chat_placeholder"), fontSize = 14.sp) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(24.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFFF7FAFC),
                                unfocusedContainerColor = Color(0xFFF7FAFC),
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = Color.LightGray
                            ),
                            maxLines = 3
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = { sendMessage(inputText) },
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(
                                    if (inputText.trim().isNotEmpty()) MaterialTheme.colorScheme.primary
                                    else Color.LightGray
                                )
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Send",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChatBubbleItem(
    msg: ChatMessage,
    onRouteClick: (String?) -> Unit,
    currentlySpeakingId: String? = null,
    onSpeakClick: (String, String) -> Unit = { _, _ -> }
) {
    val isUser = msg.sender == MessageSender.USER
    val isSpeakingThis = currentlySpeakingId == msg.id

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            Image(
                painter = painterResource(id = R.drawable.cuby_ai_robot_mother_baby),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start,
            modifier = Modifier.widthIn(max = 290.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = if (isUser) 16.dp else 4.dp,
                    bottomEnd = if (isUser) 4.dp else 16.dp
                ),
                color = if (isUser) MaterialTheme.colorScheme.primary else Color.White,
                shadowElevation = if (isUser) 2.dp else 4.dp,
                border = if (!isUser) BorderStroke(1.dp, Color(0xFFE2E8F0)) else null
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = msg.text,
                        color = if (isUser) Color.White else Color(0xFF2D3748),
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )

                    msg.routeLabel?.let { label ->
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = { onRouteClick(msg.routeToOpen) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.primary
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = label, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = if (isUser) Arrangement.End else Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp)
            ) {
                Text(
                    text = msg.timestamp,
                    fontSize = 10.sp,
                    color = Color.Gray
                )
                if (!isUser) {
                    IconButton(
                        onClick = { onSpeakClick(msg.id, msg.text) },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = if (isSpeakingThis) R.drawable.ic_volume_off else R.drawable.ic_volume_up),
                            contentDescription = if (isSpeakingThis) "Stop Reading" else "Read Answer",
                            tint = if (isSpeakingThis) MaterialTheme.colorScheme.primary else Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TypingIndicatorItem() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 44.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 2.dp,
            modifier = Modifier.padding(6.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(14.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = tr("ai_chat_thinking"),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}
