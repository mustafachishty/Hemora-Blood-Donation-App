package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.RedPrimary
import com.example.ui.theme.RedSecondary
import com.example.ui.theme.RedContainer
import com.example.ui.viewmodel.LifeDropViewModel
import com.example.ui.viewmodel.Screen
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LifeDropAppContent(viewModel: LifeDropViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        AnimatedContent(
            targetState = currentScreen,
            transitionSpec = {
                slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() with
                        slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut()
            },
            label = "ScreenTransition"
        ) { screen ->
            when (screen) {
                is Screen.Splash -> SplashScreen(viewModel)
                is Screen.Onboarding -> OnboardingScreen(viewModel)
                is Screen.Login -> LoginScreen(viewModel)
                is Screen.ProfileSetup -> ProfileSetupScreen(viewModel)
                is Screen.Home -> MainLayout(viewModel) { HomeScreen(viewModel) }
                is Screen.MapScreen -> MainLayout(viewModel) { MapScreen(viewModel) }
                is Screen.PostRequest -> PostRequestScreen(viewModel)
                is Screen.RequestDetail -> RequestDetailScreen(viewModel, screen.requestId)
                is Screen.ChatsList -> MainLayout(viewModel) { ChatsListScreen(viewModel) }
                is Screen.ChatRoom -> ChatRoomScreen(viewModel, screen.requestId, screen.contactId, screen.contactName)
                is Screen.LiveLocationSharing -> LiveLocationSharingScreen(viewModel, screen.requestId, screen.contactId, screen.contactName)
                is Screen.Notifications -> NotificationsScreen(viewModel)
                is Screen.BloodBankDirectory -> MainLayout(viewModel) { BloodBankDirectoryScreen(viewModel) }
                is Screen.EligibilityChecker -> EligibilityCheckerScreen(viewModel)
                is Screen.DonationHistoryList -> DonationHistoryListScreen(viewModel)
                is Screen.LeaderboardScreen -> LeaderboardScreen(viewModel)
                is Screen.AchievementsScreen -> AchievementsScreen(viewModel)
                is Screen.SettingsScreen -> MainLayout(viewModel) { SettingsScreen(viewModel) }
                is Screen.EditProfileScreen -> EditProfileScreen(viewModel)
                is Screen.AdminLoginScreen -> AdminLoginScreen(viewModel)
                is Screen.AdminDashboardScreen -> AdminDashboardScreen(viewModel)
            }
        }
    }
}

@Composable
fun MainLayout(
    viewModel: LifeDropViewModel,
    content: @Composable () -> Unit
) {
    val currentScreen by viewModel.currentScreen.collectAsState()

    Scaffold(
        bottomBar = {
            NavigationBar(
                tonalElevation = 8.dp,
                modifier = Modifier.testTag("main_bottom_nav"),
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                NavigationBarItem(
                    selected = currentScreen is Screen.Home,
                    onClick = { viewModel.clearHistoryAndNavigateTo(Screen.Home) },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RedPrimary,
                        selectedTextColor = RedPrimary,
                        indicatorColor = RedPrimary.copy(alpha = 0.1f)
                    )
                )
                NavigationBarItem(
                    selected = currentScreen is Screen.MapScreen,
                    onClick = { viewModel.clearHistoryAndNavigateTo(Screen.MapScreen) },
                    icon = { Icon(Icons.Default.LocationOn, contentDescription = "Map") },
                    label = { Text("Map") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RedPrimary,
                        selectedTextColor = RedPrimary,
                        indicatorColor = RedPrimary.copy(alpha = 0.1f)
                    )
                )
                NavigationBarItem(
                    selected = currentScreen is Screen.BloodBankDirectory,
                    onClick = { viewModel.clearHistoryAndNavigateTo(Screen.BloodBankDirectory) },
                    icon = { Icon(Icons.Default.LocalHospital, contentDescription = "Banks") },
                    label = { Text("Banks") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RedPrimary,
                        selectedTextColor = RedPrimary,
                        indicatorColor = RedPrimary.copy(alpha = 0.1f)
                    )
                )
                NavigationBarItem(
                    selected = currentScreen is Screen.ChatsList,
                    onClick = { viewModel.clearHistoryAndNavigateTo(Screen.ChatsList) },
                    icon = { Icon(Icons.Default.Chat, contentDescription = "Chats") },
                    label = { Text("Chats") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RedPrimary,
                        selectedTextColor = RedPrimary,
                        indicatorColor = RedPrimary.copy(alpha = 0.1f)
                    )
                )
                NavigationBarItem(
                    selected = currentScreen is Screen.SettingsScreen,
                    onClick = { viewModel.clearHistoryAndNavigateTo(Screen.SettingsScreen) },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RedPrimary,
                        selectedTextColor = RedPrimary,
                        indicatorColor = RedPrimary.copy(alpha = 0.1f)
                    )
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            content()
        }
    }
}

@Composable
fun SplashScreen(viewModel: LifeDropViewModel) {
    val currentUser by viewModel.currentUser.collectAsState()
    var logoTapCount by remember { mutableStateOf(0) }
    val infiniteTransition = rememberInfiniteTransition(label = "SplashTransition")
    
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "PulseScale"
    )

    LaunchedEffect(Unit) {
        delay(3000)
        if (currentUser == null) {
            viewModel.clearHistoryAndNavigateTo(Screen.Onboarding)
        } else {
            viewModel.clearHistoryAndNavigateTo(Screen.Home)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(RedPrimary, RedSecondary)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        logoTapCount++
                        if (logoTapCount >= 3) {
                            logoTapCount = 0
                            viewModel.navigateTo(Screen.AdminLoginScreen)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(120.dp * pulseScale)) {
                    val width = size.width
                    val height = size.height

                    val path = androidx.compose.ui.graphics.Path().apply {
                        moveTo(width / 2, height / 4)
                        cubicTo(width / 4, 0f, 0f, height / 3, width / 2, height)
                        cubicTo(width, height / 3, 3 * width / 4, 0f, width / 2, height / 4)
                        close()
                    }
                    drawPath(
                        path = path,
                        brush = Brush.radialGradient(
                            colors = listOf(GoldAccent, Color.Transparent),
                            center = Offset(width / 2, height / 3),
                            radius = width * 0.8f
                        )
                    )
                    drawPath(
                        path = path,
                        color = Color.White
                    )

                    drawCircle(
                        color = RedPrimary,
                        radius = width / 6,
                        center = Offset(width / 2, height / 2.2f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Hemora",
                fontSize = 36.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "BLOOD DONATION NETWORK",
                fontSize = 12.sp,
                color = GoldAccent,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 3.sp
            )
        }
        
        Text(
            text = "Saving Lives Instantly",
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.7f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        )
    }
}

@Composable
fun OnboardingScreen(viewModel: LifeDropViewModel) {
    var slideIndex by remember { mutableStateOf(0) }
    
    val titles = listOf(
        "Urgent Blood Network",
        "Direct Coordination",
        "Earn Recognition"
    )
    val descs = listOf(
        "Connect with verified blood donors within a 5km radius and save lives during critical emergencies.",
        "Communicate seamlessly with responders using our integrated secure real-time private chat system.",
        "Receive Life Saver points and beautiful gold certificates for your noble, lifesaving contributions."
    )
    val icons = listOf(
        Icons.Default.Favorite,
        Icons.Default.Forum,
        Icons.Default.WorkspacePremium
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = { viewModel.navigateTo(Screen.Login) }) {
                Text("SKIP", color = RedPrimary, fontWeight = FontWeight.Bold)
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .background(RedContainer, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icons[slideIndex],
                    contentDescription = null,
                    tint = RedPrimary,
                    modifier = Modifier.size(80.dp)
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = titles[slideIndex],
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = RedPrimary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = descs[slideIndex],
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                repeat(3) { i ->
                    Box(
                        modifier = Modifier
                            .size(if (i == slideIndex) 24.dp else 8.dp, 8.dp)
                            .background(
                                color = if (i == slideIndex) RedPrimary else RedPrimary.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(4.dp)
                            )
                    )
                }
            }

            Button(
                onClick = {
                    if (slideIndex < 2) {
                        slideIndex++
                    } else {
                        viewModel.navigateTo(Screen.Login)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("onboarding_next_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = RedPrimary),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = if (slideIndex == 2) "GET STARTED" else "CONTINUE",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun LoginScreen(viewModel: LifeDropViewModel) {
    val phoneInput by viewModel.phoneInput.collectAsState()
    val otpInput by viewModel.otpInput.collectAsState()
    var isOtpSent by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = "Logo",
            tint = RedPrimary,
            modifier = Modifier.size(72.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Welcome to Hemora",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = RedPrimary
        )
        Text(
            text = "Sign in to coordinate immediate donation needs",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = phoneInput,
            onValueChange = { viewModel.setPhoneInput(it) },
            label = { Text("Phone Number") },
            placeholder = { Text("+1 (555) 0192") },
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = RedPrimary) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("login_phone_input"),
            shape = RoundedCornerShape(12.dp)
        )

        if (isOtpSent) {
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = otpInput,
                onValueChange = { viewModel.setOtpInput(it) },
                label = { Text("6-Digit OTP Verification Code") },
                placeholder = { Text("123456") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = RedPrimary) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("login_otp_input"),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Enter any 6 digit code for sandbox bypass",
                fontSize = 12.sp,
                color = Color.Gray,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (errorMsg.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = errorMsg, color = RedPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (!isOtpSent) {
                    if (phoneInput.length >= 8) {
                        isOtpSent = true
                        errorMsg = ""
                        Toast.makeText(context, "Verification OTP code sent!", Toast.LENGTH_SHORT).show()
                    } else {
                        errorMsg = "Please enter a valid phone number"
                    }
                } else {
                    val ok = viewModel.submitPhoneLogin()
                    if (!ok) {
                        errorMsg = "Verification failed. Please enter 6-digit code."
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag("login_submit_btn"),
            colors = ButtonDefaults.buttonColors(containerColor = RedPrimary),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = if (isOtpSent) "VERIFY & CONTINUE" else "SEND OTP CODE",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "OR", fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = {
                viewModel.submitGoogleLogin("donor@lifedrop.org")
                Toast.makeText(context, "Connected with Google Sandbox!", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag("google_login_btn"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onBackground)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    tint = RedPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text("Sign In with Google Account", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ProfileSetupScreen(viewModel: LifeDropViewModel) {
    var name by remember { mutableStateOf("") }
    var selectedBloodGroup by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("25") }
    var gender by remember { mutableStateOf("Male") }
    var city by remember { mutableStateOf("") }
    var hasDiabetes by remember { mutableStateOf(false) }
    var hasSurgery by remember { mutableStateOf(false) }

    val bloodGroups = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Complete Your Profile Setup",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = RedPrimary,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "We require these details to verify blood compatibility matches",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Full Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Select Blood Group",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .horizontalScroll(rememberScrollState())
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    bloodGroups.forEach { group ->
                        val isSelected = selectedBloodGroup == group
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(
                                    color = if (isSelected) RedPrimary else RedContainer,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .border(
                                    width = 2.dp,
                                    color = if (isSelected) GoldAccent else Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { selectedBloodGroup = group },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = group,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else RedPrimary
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedTextField(
                value = age,
                onValueChange = { age = it },
                label = { Text("Age") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            )
            OutlinedTextField(
                value = gender,
                onValueChange = { gender = it },
                label = { Text("Gender") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("Current City Location") },
            placeholder = { Text("Chicago, IL") },
            leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = RedPrimary) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Medical Eligibility Checklist",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Diabetes Conditions", fontWeight = FontWeight.SemiBold)
                        Text("Toggle if diagnosed with Diabetes", fontSize = 12.sp, color = Color.Gray)
                    }
                    Switch(
                        checked = hasDiabetes,
                        onCheckedChange = { hasDiabetes = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = RedPrimary)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Recent Surgery (Past 6 Months)", fontWeight = FontWeight.SemiBold)
                        Text("Toggle if you had minor/major surgery recently", fontSize = 12.sp, color = Color.Gray)
                    }
                    Switch(
                        checked = hasSurgery,
                        onCheckedChange = { hasSurgery = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = RedPrimary)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (name.isNotEmpty() && selectedBloodGroup.isNotEmpty() && city.isNotEmpty()) {
                    viewModel.setupProfile(
                        name = name,
                        bloodGroup = selectedBloodGroup,
                        age = age.toIntOrNull() ?: 25,
                        gender = gender,
                        city = city,
                        hasDiabetes = hasDiabetes,
                        hasRecentSurgery = hasSurgery
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag("profile_setup_submit"),
            colors = ButtonDefaults.buttonColors(containerColor = RedPrimary),
            shape = RoundedCornerShape(12.dp),
            enabled = name.isNotEmpty() && selectedBloodGroup.isNotEmpty() && city.isNotEmpty()
        ) {
            Text("FINALIZE REGISTRATION", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
        }
    }
}

@Composable
fun HomeScreen(viewModel: LifeDropViewModel) {
    val currentUser by viewModel.currentUser.collectAsState()
    val activeRequests by viewModel.activeRequests.collectAsState()
    val notifications by viewModel.notifications.collectAsState()
    val unreadNotifsCount = notifications.count { !it.isRead }

    val unfulfilledRequests = activeRequests.filter { !it.isFulfilled }
    val criticalEmergency = unfulfilledRequests.find { it.urgencyLevel == "Critical" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)) // High Density Slate light-gray background
            .verticalScroll(rememberScrollState())
    ) {
        // High Density Top App Bar / Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .drawBehind {
                    // Draw a clean bottom border line
                    drawLine(
                        color = Color(0xFFE2E8F0),
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = 2f
                    )
                }
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Logo: bg-[#B71C1C], rounded-xl, custom white droplet + gold dot accent
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(RedPrimary, shape = RoundedCornerShape(12.dp))
                        .shadow(2.dp, shape = RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    // White droplet symbol
                    Canvas(modifier = Modifier.size(14.dp, 20.dp)) {
                        val width = size.width
                        val height = size.height
                        val path = androidx.compose.ui.graphics.Path().apply {
                            moveTo(width / 2, 0f)
                            cubicTo(width / 4, 0f, 0f, height / 3, width / 2, height)
                            cubicTo(width, height / 3, 3 * width / 4, 0f, width / 2, 0f)
                            close()
                        }
                        drawPath(path = path, color = Color.White)
                    }
                    // Gold accent dot in top right
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .align(Alignment.TopEnd)
                            .offset(x = 2.dp, y = (-2).dp)
                            .background(GoldAccent, shape = CircleShape)
                            .border(1.5.dp, RedPrimary, CircleShape)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Hemora",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = RedPrimary,
                        letterSpacing = (-0.5).sp
                    )
                    Text(
                        text = "BLOOD NETWORK",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF94A3B8),
                        letterSpacing = 1.5.sp
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Notifications bell with red status badge dot
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFFF1F5F9), shape = CircleShape)
                        .clickable { viewModel.navigateTo(Screen.Notifications) }
                        .testTag("notif_bell_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Alerts",
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(20.dp)
                    )
                    if (unreadNotifsCount > 0) {
                        Box(
                            modifier = Modifier
                                .size(9.dp)
                                .background(RedPrimary, shape = CircleShape)
                                .border(1.5.dp, Color.White, CircleShape)
                                .align(Alignment.TopEnd)
                                .offset(x = (-1).dp, y = 1.dp)
                        )
                    }
                }

                // Profile Avatar with dynamic Gold border
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFFE2E8F0), shape = CircleShape)
                        .border(2.dp, GoldAccent, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (currentUser?.fullName?.firstOrNull() ?: 'L').toString(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = RedPrimary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Greeting Section with Pill Badge
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Welcome Back,",
                    color = Color(0xFF64748B),
                    fontSize = 14.sp
                )
                Text(
                    text = currentUser?.fullName ?: "Life Saver",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF1E293B),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // High Density Pill Badge (bg-[#FFD700]/20, border border-[#FFD700])
            Box(
                modifier = Modifier
                    .background(GoldAccent.copy(alpha = 0.2f), shape = RoundedCornerShape(24.dp))
                    .border(1.dp, GoldAccent, shape = RoundedCornerShape(24.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = currentUser?.bloodGroup ?: "O+",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = RedPrimary
                    )
                    Text(
                        text = "DONOR",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF8B6E00)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Compact Status & Switch row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = RedPrimary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${currentUser?.points ?: 150} PTS SAVED",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF8B6E00)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = if (currentUser?.isAvailable == true) "Available" else "Busy",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (currentUser?.isAvailable == true) Color(0xFF16A34A) else Color(0xFF64748B)
                )
                Switch(
                    checked = currentUser?.isAvailable ?: true,
                    onCheckedChange = { viewModel.toggleAvailability(it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = GoldAccent,
                        checkedTrackColor = RedPrimary.copy(alpha = 0.5f),
                        uncheckedThumbColor = Color(0xFF94A3B8),
                        uncheckedTrackColor = Color(0xFFE2E8F0)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // High Density Stats Grid (3 Columns)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            val activeReq = unfulfilledRequests.size.toString().padStart(2, '0')
            val livesSaved = (((currentUser?.points ?: 150) - 50) / 100).coerceAtLeast(0).toString().padStart(2, '0')

            // Card 1: Donors Near
            Card(
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "142", // HTML specific High Density static element for visual polish
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = RedPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "DONORS NEAR",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF94A3B8),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Card 2: Active Req
            Card(
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = activeReq,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = RedPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "ACTIVE REQ",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF94A3B8),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Card 3: Lives Saved (Gold Accent border)
            Card(
                modifier = Modifier
                    .weight(1f)
                    .border(1.5.dp, GoldAccent, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = livesSaved,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = RedPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "LIVES SAVED",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF94A3B8),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Urgent Emergency Alert Banner
        if (criticalEmergency != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clickable { viewModel.navigateTo(Screen.RequestDetail(criticalEmergency.id)) },
                colors = CardDefaults.cardColors(containerColor = RedPrimary),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color.White.copy(alpha = 0.2f), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🆘",
                            fontSize = 20.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "CRITICAL REQUEST NEARBY",
                            color = Color.White.copy(alpha = 0.8f),
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${criticalEmergency.patientName} needs ${criticalEmergency.bloodGroup}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${criticalEmergency.hospitalName} • 1.2 km away",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 11.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(Color.White, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Details",
                            tint = RedPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Quick Services
        Text(
            text = "Quick Services",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color(0xFF1E293B),
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Find Donors / Request Blood Now
            Card(
                onClick = { viewModel.navigateTo(Screen.PostRequest) },
                modifier = Modifier
                    .weight(1f)
                    .height(130.dp)
                    .border(2.dp, Color(0xFFF1F5F9), RoundedCornerShape(24.dp))
                    .testTag("request_blood_fab"),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color(0xFFFFCDD2), shape = RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🚨", fontSize = 24.sp)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Request Blood",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color(0xFF334155),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Can I Donate / Eligibility Checker
            Card(
                onClick = { viewModel.navigateTo(Screen.EligibilityChecker) },
                modifier = Modifier
                    .weight(1f)
                    .height(130.dp)
                    .border(2.dp, Color(0xFFF1F5F9), RoundedCornerShape(24.dp))
                    .testTag("donate_checker_btn"),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color(0xFFFFF9C4), shape = RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("📋", fontSize = 24.sp)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Can I Donate?",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color(0xFF334155),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Donor Coverage Map Preview
        Text(
            text = "Donor Coverage Map Preview",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color(0xFF1E293B),
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .padding(horizontal = 20.dp)
                .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(24.dp))
                .clickable { viewModel.navigateTo(Screen.MapScreen) },
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFE2E8F0)) // light grid map canvas background
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height
                    
                    // Draw clean grid lines
                    val numX = 12
                    val numY = 8
                    for (i in 0..numX) {
                        val x = i * (width / numX)
                        drawLine(
                            color = Color.White,
                            start = Offset(x, 0f),
                            end = Offset(x, height),
                            strokeWidth = 2f
                        )
                    }
                    for (i in 0..numY) {
                        val y = i * (height / numY)
                        drawLine(
                            color = Color.White,
                            start = Offset(0f, y),
                            end = Offset(width, y),
                            strokeWidth = 2f
                        )
                    }
                }

                // AB- Pin with tail
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 80.dp, bottom = 20.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(RedPrimary.copy(alpha = 0.2f), CircleShape)
                    )
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(RedPrimary, shape = RoundedCornerShape(6.dp))
                                .border(1.dp, Color.White, RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("AB-", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                        Canvas(modifier = Modifier.size(6.dp, 4.dp)) {
                            val path = androidx.compose.ui.graphics.Path().apply {
                                moveTo(0f, 0f)
                                lineTo(size.width, 0f)
                                lineTo(size.width / 2, size.height)
                                close()
                            }
                            drawPath(path = path, color = RedPrimary)
                        }
                    }
                }

                // A+ Pin
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 60.dp, top = 20.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .background(Color.White, shape = RoundedCornerShape(6.dp))
                                .border(1.dp, GoldAccent, RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("A+", color = Color.Black, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                        Canvas(modifier = Modifier.size(6.dp, 4.dp)) {
                            val path = androidx.compose.ui.graphics.Path().apply {
                                moveTo(0f, 0f)
                                lineTo(size.width, 0f)
                                lineTo(size.width / 2, size.height)
                                close()
                            }
                            drawPath(path = path, color = Color.White)
                        }
                    }
                }

                // Bottom location overlay bar inside the map card
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(10.dp)
                        .background(Color.White.copy(alpha = 0.95f), shape = RoundedCornerShape(16.dp))
                        .border(1.dp, Color.White.copy(alpha = 0.5f), shape = RoundedCornerShape(16.dp))
                        .padding(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("📍", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Current View",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF94A3B8)
                                )
                                Text(
                                    text = currentUser?.city ?: "Chicago, IL",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E293B)
                                )
                            }
                        }

                        Button(
                            onClick = { viewModel.navigateTo(Screen.MapScreen) },
                            colors = ButtonDefaults.buttonColors(containerColor = RedPrimary),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text("EXPAND MAP", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Active Blood Requests Feed
        Text(
            text = "Active Blood Requests Feed",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color(0xFF1E293B),
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))

        if (unfulfilledRequests.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Favorite, contentDescription = null, tint = RedContainer, modifier = Modifier.size(56.dp))
                    Text("No active blood requests in your area.", color = Color.Gray, fontSize = 14.sp)
                }
            }
        } else {
            unfulfilledRequests.forEach { req ->
                RequestFeedItem(req) {
                    viewModel.navigateTo(Screen.RequestDetail(req.id))
                }
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun RequestFeedItem(request: BloodRequest, onClick: () -> Unit) {
    val urgencyColor = when (request.urgencyLevel) {
        "Critical" -> RedPrimary
        "Urgent" -> Color(0xFFD97706)
        else -> Color(0xFF16A34A)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(16.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(RedPrimary.copy(alpha = 0.1f), shape = RoundedCornerShape(12.dp))
                    .border(1.dp, RedPrimary.copy(alpha = 0.2f), shape = RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = request.bloodGroup,
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp,
                    color = RedPrimary
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = request.patientName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color(0xFF1E293B)
                )
                Text(
                    text = request.hospitalName,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(urgencyColor.copy(alpha = 0.15f), shape = RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = request.urgencyLevel.uppercase(),
                            color = urgencyColor,
                            fontWeight = FontWeight.Black,
                            fontSize = 8.sp,
                            letterSpacing = 0.5.sp
                        )
                    }
                    Text(
                        text = "•",
                        color = Color.LightGray,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "Need: ${request.unitsRequired} Units",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF475569)
                    )
                }
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "View Details",
                tint = Color(0xFF94A3B8),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun MapScreen(viewModel: LifeDropViewModel) {
    val distanceFilter by viewModel.distanceFilter.collectAsState()
    val activeRequests by viewModel.activeRequests.collectAsState()
    val allUsers by viewModel.allUsers.collectAsState()

    val unfulfilledRequests = activeRequests.filter { !it.isFulfilled }

    // Navigation and simulation state
    var selectedItemText by remember { mutableStateOf("Tap any pin on the map to display coordination details.") }
    var actionButtonRoute by remember { mutableStateOf<Screen?>(null) }
    var selectedItemName by remember { mutableStateOf<String?>(null) }
    var selectedItemType by remember { mutableStateOf<String?>(null) } // "request" or "donor"
    var selectedItemRef by remember { mutableStateOf<BloodRequest?>(null) }

    var isNavigating by remember { mutableStateOf(false) }
    var driveProgress by remember { mutableStateOf(0f) }
    var currentStreet by remember { mutableStateOf("Main Street") }
    var etaMinutes by remember { mutableStateOf(2.5) }
    var speedMph by remember { mutableStateOf(45) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    // Define the routes
    val userLocation = Offset(180f, 200f)
    val mercyHospitalLocation = Offset(140f, 400f)
    val northwesternHospitalLocation = Offset(450f, 180f)

    val mercyRoute = listOf(
        userLocation,
        Offset(180f, 300f),
        Offset(140f, 300f),
        mercyHospitalLocation
    )

    val northwesternRoute = listOf(
        userLocation,
        Offset(180f, 120f),
        Offset(320f, 120f),
        Offset(320f, 180f),
        northwesternHospitalLocation
    )

    var activeRoute by remember { mutableStateOf(mercyRoute) }
    var destinationName by remember { mutableStateOf("Mercy Hospital") }

    // Simulated navigation loop
    LaunchedEffect(isNavigating) {
        if (isNavigating) {
            driveProgress = 0f
            while (driveProgress < 1f) {
                delay(150)
                driveProgress = (driveProgress + 0.015f).coerceAtMost(1f)
                
                // Simulate speed changes
                speedMph = when {
                    driveProgress < 0.2f -> (40..45).random()
                    driveProgress in 0.2f..0.3f -> 0 // Red light!
                    driveProgress in 0.3f..0.6f -> (48..55).random()
                    driveProgress in 0.6f..0.8f -> (35..42).random()
                    else -> (15..25).random() // Approaching destination
                }

                // Simulate street names and turn instructions
                currentStreet = when {
                    driveProgress < 0.2f -> "Head North on Grand Ave (150m)"
                    driveProgress in 0.2f..0.3f -> "Stopped at Red Light on Erie St"
                    driveProgress in 0.3f..0.5f -> "Turn Right on Michigan Ave (400m)"
                    driveProgress in 0.5f..0.7f -> "Slight Left toward St. Mary's Way"
                    driveProgress in 0.7f..0.9f -> "Entering Hospital Plaza Road"
                    else -> "Arriving at destination shortly"
                }

                // Simulate ETA remaining
                etaMinutes = ((1f - driveProgress) * 2.5).coerceAtLeast(0.1)
            }
            // Finished!
            isNavigating = false
            showSuccessDialog = true
            viewModel.addPoints(100)
        }
    }

    // Main layout
    Column(modifier = Modifier.fillMaxSize()) {
        // Radius filter (hidden when in Navigation Mode to maximize map viewing)
        if (!isNavigating) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .drawBehind {
                        drawLine(
                            color = Color(0xFFE2E8F0),
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = 2f
                        )
                    }
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Radius Search Filter:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color(0xFF475569)
                )
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf(5, 10, 25, 50).forEach { dist ->
                        val isSelected = distanceFilter == dist
                        Box(
                            modifier = Modifier
                                .background(
                                    color = if (isSelected) RedPrimary else Color(0xFFF1F5F9),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { viewModel.setDistanceFilter(dist) }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "${dist}km",
                                color = if (isSelected) Color.White else Color(0xFF64748B),
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        } else {
            // Turn-by-Turn Navigation Header
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Turn left or right icon
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color(0xFF334155), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when {
                                driveProgress < 0.3f -> "⬆️"
                                driveProgress in 0.3f..0.7f -> "➡️"
                                else -> "📍"
                            },
                            fontSize = 20.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = currentStreet,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "ETA: ${"%.1f".format(etaMinutes)} min",
                                color = GoldAccent,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "•",
                                color = Color(0xFF64748B),
                                fontSize = 12.sp
                            )
                            Text(
                                text = if (speedMph == 0) "Stopped" else "$speedMph mph",
                                color = if (speedMph == 0) Color.Red else Color(0xFF38BDF8),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = { isNavigating = false },
                        modifier = Modifier
                            .background(Color(0xFFEF4444).copy(alpha = 0.2f), shape = CircleShape)
                            .size(36.dp)
                    ) {
                        Text("❌", fontSize = 12.sp)
                    }
                }
            }
        }

        // Map Canvas Area
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color(0xFFE2E8F0)) // High Density grid background
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val height = size.height

                // Draw grass/park areas
                drawRect(
                    color = Color(0xFFDCFCE7),
                    topLeft = Offset(20f, 40f),
                    size = Size(120f, 160f)
                )
                drawCircle(
                    color = Color(0xFFDCFCE7),
                    radius = 80f,
                    center = Offset(width - 100f, height - 120f)
                )

                // Draw water/river area
                drawRect(
                    color = Color(0xFFE0F2FE),
                    topLeft = Offset(width - 150f, 20f),
                    size = Size(100f, 250f)
                )

                // Draw grid road network (proper roads layout)
                val roadColor = Color.White
                val borderRoadColor = Color(0xFFCBD5E1)

                fun drawRoad(start: Offset, end: Offset, thickness: Float = 24f) {
                    // Draw outer border road line first
                    drawLine(borderRoadColor, start, end, strokeWidth = thickness + 4f)
                    // Draw inner white road
                    drawLine(roadColor, start, end, strokeWidth = thickness)
                }

                // Horizontal roads
                drawRoad(Offset(0f, 120f), Offset(width, 120f))
                drawRoad(Offset(0f, 200f), Offset(width, 200f))
                drawRoad(Offset(0f, 300f), Offset(width, 300f))
                drawRoad(Offset(0f, 400f), Offset(width, 400f))

                // Vertical roads
                drawRoad(Offset(140f, 0f), Offset(140f, height))
                drawRoad(Offset(180f, 0f), Offset(180f, height))
                drawRoad(Offset(320f, 0f), Offset(320f, height))
                drawRoad(Offset(450f, 0f), Offset(450f, height))

                // Draw the Navigation Path if navigating
                if (isNavigating) {
                    val path = androidx.compose.ui.graphics.Path().apply {
                        if (activeRoute.isNotEmpty()) {
                            moveTo(activeRoute.first().x.dp.toPx(), activeRoute.first().y.dp.toPx())
                            for (i in 1 until activeRoute.size) {
                                lineTo(activeRoute[i].x.dp.toPx(), activeRoute[i].y.dp.toPx())
                            }
                        }
                    }
                    // Outer route outline glow
                    drawPath(
                        path = path,
                        color = Color(0xFF38BDF8).copy(alpha = 0.4f),
                        style = Stroke(width = 16f, cap = StrokeCap.Round)
                    )
                    // Inner beautiful routing polyline
                    drawPath(
                        path = path,
                        color = Color(0xFF0284C7),
                        style = Stroke(width = 8f, cap = StrokeCap.Round)
                    )
                }
            }

            // Render all standard pins if NOT navigating
            if (!isNavigating) {
                // Pin 1: User position dot
                Box(
                    modifier = Modifier
                        .offset(x = userLocation.x.dp, y = userLocation.y.dp)
                        .size(24.dp)
                        .background(Color(0xFF2196F3).copy(alpha = 0.2f), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(Color(0xFF2196F3), shape = CircleShape)
                            .border(1.5.dp, Color.White, CircleShape)
                    )
                }

                // Pin 2: Active Donor Sarah
                MapPinItem(
                    label = "Sarah (O+)",
                    offset = Offset(110f, 100f),
                    isRequest = false,
                    urgency = "Normal"
                ) {
                    selectedItemName = "Sarah Jenkins"
                    selectedItemType = "donor"
                    selectedItemText = "Verified Active Donor: Sarah Jenkins\nBlood Type: O+ | Nearby (0.8 km away)"
                    actionButtonRoute = null
                    selectedItemRef = null
                }

                // Pin 3: Active Donor David
                MapPinItem(
                    label = "David (A-)",
                    offset = Offset(280f, 280f),
                    isRequest = false,
                    urgency = "Normal"
                ) {
                    selectedItemName = "David Miller"
                    selectedItemType = "donor"
                    selectedItemText = "Verified Active Donor: David Miller\nBlood Type: A- | Distance: 2.4 km away"
                    actionButtonRoute = null
                    selectedItemRef = null
                }

                // Pin 4: Request Robert (Mercy Hospital)
                MapPinItem(
                    label = "Robert (O-)",
                    offset = mercyHospitalLocation,
                    isRequest = true,
                    urgency = "Urgent"
                ) {
                    val req = unfulfilledRequests.find { it.patientName.contains("Robert") }
                    selectedItemName = "Robert Chen"
                    selectedItemType = "request"
                    selectedItemText = "🆘 URGENT NEED: Robert Chen\nHospital: Mercy Hospital & Medical Center\nBlood Group: O- (Needs 2 Units)"
                    actionButtonRoute = req?.let { Screen.RequestDetail(it.id) }
                    selectedItemRef = req
                    activeRoute = mercyRoute
                    destinationName = "Mercy Hospital & Medical Center"
                }

                // Pin 5: Request Emily (Northwestern)
                MapPinItem(
                    label = "Emily (A-)",
                    offset = northwesternHospitalLocation,
                    isRequest = true,
                    urgency = "Critical"
                ) {
                    val req = unfulfilledRequests.find { it.patientName.contains("Emily") }
                    selectedItemName = "Emily Watson"
                    selectedItemType = "request"
                    selectedItemText = "🚨 CRITICAL MATCH: Emily Watson\nHospital: Northwestern Memorial Hospital\nBlood Group: A- (Needs 3 Units)"
                    actionButtonRoute = req?.let { Screen.RequestDetail(it.id) }
                    selectedItemRef = req
                    activeRoute = northwesternRoute
                    destinationName = "Northwestern Memorial Hospital"
                }
            } else {
                // If navigating, render only:
                // 1. Destination pin
                val destLocation = activeRoute.last()
                MapPinItem(
                    label = destinationName.take(15) + "...",
                    offset = destLocation,
                    isRequest = true,
                    urgency = "Critical"
                ) {}

                // 2. Beautiful animated vehicle moving along the path segment coordinates
                if (activeRoute.isNotEmpty()) {
                    val numSegments = activeRoute.size - 1
                    val segmentIndex = (driveProgress * numSegments).toInt().coerceAtMost(numSegments - 1)
                    val segmentProgress = (driveProgress * numSegments) - segmentIndex

                    val startPt = activeRoute[segmentIndex]
                    val endPt = activeRoute[segmentIndex + 1]

                    val animatedX = startPt.x + (endPt.x - startPt.x) * segmentProgress
                    val animatedY = startPt.y + (endPt.y - startPt.y) * segmentProgress

                    Box(
                        modifier = Modifier
                            .offset(x = animatedX.dp, y = animatedY.dp)
                            .size(36.dp)
                            .background(Color(0xFF0284C7).copy(alpha = 0.2f), shape = CircleShape)
                            .border(2.dp, Color(0xFF0284C7), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🚑", fontSize = 18.sp)
                    }
                }
            }
        }

        // Details Panel at the Bottom (only when NOT navigating)
        if (!isNavigating) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Map Coordination & Navigation",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = RedPrimary,
                            letterSpacing = 0.5.sp
                        )
                        if (selectedItemType == "donor") {
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFDCFCE7), shape = RoundedCornerShape(12.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text("ACTIVE DONOR", color = Color(0xFF15803D), fontSize = 9.sp, fontWeight = FontWeight.Black)
                            }
                        } else if (selectedItemType == "request") {
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFFEE2E2), shape = RoundedCornerShape(12.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text("URGENT REQUEST", color = RedPrimary, fontSize = 9.sp, fontWeight = FontWeight.Black)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = selectedItemText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF334155),
                        minLines = 2
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        if (selectedItemType == "request") {
                            // Emergency Delivery Simulation Button!
                            Button(
                                onClick = { isNavigating = true },
                                colors = ButtonDefaults.buttonColors(containerColor = RedPrimary),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text("🚨", fontSize = 16.sp)
                                    Text(
                                        text = "SIMULATE EMERGENCY DRIVE",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        if (actionButtonRoute != null) {
                            Button(
                                onClick = { viewModel.navigateTo(actionButtonRoute!!) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF334155)),
                                shape = RoundedCornerShape(12.dp),
                                modifier = if (selectedItemType == "request") Modifier.weight(1f) else Modifier.fillMaxWidth()
                            ) {
                                Text("VIEW DETAILS", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color.White)
                            }
                        } else if (selectedItemType == "donor") {
                            Button(
                                onClick = {
                                    // Simulated action: open chat with donor
                                    viewModel.navigateTo(Screen.ChatsList)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = RedPrimary),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text("💬", fontSize = 16.sp)
                                    Text("CONTACT DONOR", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color.White)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Success dialog when the simulation completes
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("🏆 Delivery Succeeded!", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column {
                    Text(
                        text = "Great job! You have completed the simulated emergency blood delivery drive to $destinationName.",
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(GoldAccent.copy(alpha = 0.2f), shape = RoundedCornerShape(12.dp))
                            .border(1.dp, GoldAccent, shape = RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("✨", fontSize = 20.sp)
                            Column {
                                Text("REWARD AWARDED", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFF8B6E00))
                                Text("+100 Saving Points Added!", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = RedPrimary)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showSuccessDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = RedPrimary)
                ) {
                    Text("DONE", color = Color.White)
                }
            }
        )
    }
}

@Composable
fun MapPinItem(
    label: String,
    offset: Offset,
    isRequest: Boolean,
    urgency: String,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "PulsingPin")
    val pulseSize by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Pulse"
    )

    Box(
        modifier = Modifier
            .offset(offset.x.dp, offset.y.dp)
            .clickable { onClick() }
    ) {
        if (isRequest && (urgency == "Critical" || urgency == "Urgent")) {
            Box(
                modifier = Modifier
                    .size(28.dp * pulseSize)
                    .background(
                        color = if (urgency == "Critical") RedPrimary.copy(alpha = 0.25f) else Color(0xFFD97706).copy(alpha = 0.25f),
                        shape = CircleShape
                    )
            )
        }
        
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .background(
                        color = if (isRequest) RedPrimary else Color(0xFF16A34A),
                        shape = RoundedCornerShape(6.dp)
                    )
                    .border(1.dp, GoldAccent, RoundedCornerShape(6.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(label, color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = if (isRequest) RedPrimary else Color(0xFF16A34A),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostRequestScreen(viewModel: LifeDropViewModel) {
    var patientName by remember { mutableStateOf("") }
    var selectedBloodGroup by remember { mutableStateOf("A+") }
    var unitsRequired by remember { mutableStateOf(1) }
    var hospitalName by remember { mutableStateOf("") }
    var contactNumber by remember { mutableStateOf("") }
    var urgencyLevel by remember { mutableStateOf("Normal") }
    var additionalNotes by remember { mutableStateOf("") }

    val bloodGroups = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Post Urgent Blood Request", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = patientName,
                onValueChange = { patientName = it },
                label = { Text("Patient Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Column {
                Text("Required Blood Group", fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.padding(bottom = 6.dp))
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    bloodGroups.forEach { grp ->
                        val isSel = selectedBloodGroup == grp
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(if (isSel) RedPrimary else RedContainer, shape = RoundedCornerShape(8.dp))
                                .clickable { selectedBloodGroup = grp },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(grp, color = if (isSel) Color.White else RedPrimary, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Units Required", fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.padding(bottom = 4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { if (unitsRequired > 1) unitsRequired-- }) {
                            Icon(Icons.Default.Remove, contentDescription = "Decrease")
                        }
                        Text(unitsRequired.toString(), fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(horizontal = 12.dp))
                        IconButton(onClick = { if (unitsRequired < 10) unitsRequired++ }) {
                            Icon(Icons.Default.Add, contentDescription = "Increase")
                        }
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text("Urgency Level", fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.padding(bottom = 6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        listOf("Normal", "Urgent", "Critical").forEach { urg ->
                            val isSel = urgencyLevel == urg
                            val color = when (urg) {
                                "Critical" -> RedPrimary
                                "Urgent" -> Color(0xFFE65100)
                                else -> Color(0xFF2E7D32)
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(if (isSel) color else color.copy(alpha = 0.1f), shape = RoundedCornerShape(6.dp))
                                    .clickable { urgencyLevel = urg }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(urg, color = if (isSel) Color.White else color, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            OutlinedTextField(
                value = hospitalName,
                onValueChange = { hospitalName = it },
                label = { Text("Hospital Name") },
                placeholder = { Text("e.g. Mercy Hospital") },
                leadingIcon = { Icon(Icons.Default.LocalHospital, contentDescription = null, tint = RedPrimary) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = contactNumber,
                onValueChange = { contactNumber = it },
                label = { Text("Hospital Contact Number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = additionalNotes,
                onValueChange = { additionalNotes = it },
                label = { Text("Additional Notes") },
                placeholder = { Text("Please state special circumstances.") },
                minLines = 3,
                maxLines = 5,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Button(
                onClick = {
                    if (patientName.isNotEmpty() && hospitalName.isNotEmpty() && contactNumber.isNotEmpty()) {
                        viewModel.postRequest(
                            patientName = patientName,
                            bloodGroup = selectedBloodGroup,
                            units = unitsRequired,
                            hospital = hospitalName,
                            contact = contactNumber,
                            urgency = urgencyLevel,
                            notes = additionalNotes
                        )
                        Toast.makeText(context, "Blood Request Broadcasted to nearby Donors!", Toast.LENGTH_LONG).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("submit_request_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = RedPrimary),
                shape = RoundedCornerShape(12.dp),
                enabled = patientName.isNotEmpty() && hospitalName.isNotEmpty() && contactNumber.isNotEmpty()
            ) {
                Text("BROADCAST URGENT REQUEST 🆘", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestDetailScreen(viewModel: LifeDropViewModel, requestId: String) {
    val activeRequests by viewModel.activeRequests.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val request = activeRequests.find { it.id == requestId }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Request Coordination details") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (request == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Request details not found.")
            }
        } else {
            val isRequester = request.requesterId == currentUser?.id
            val urgencyColor = when (request.urgencyLevel) {
                "Critical" -> RedPrimary
                "Urgent" -> Color(0xFFE65100)
                else -> Color(0xFF2E7D32)
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = RedContainer),
                    border = BorderStroke(2.dp, RedPrimary)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .background(Color.White, shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(request.bloodGroup, fontWeight = FontWeight.Bold, fontSize = 28.sp, color = RedPrimary)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Patient: ${request.patientName}", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = RedPrimary)
                        Text("Urgency: ${request.urgencyLevel}", color = urgencyColor, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocalHospital, contentDescription = null, tint = RedPrimary)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Hospital", color = Color.Gray, fontSize = 12.sp)
                                Text(request.hospitalName, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Bloodtype, contentDescription = null, tint = RedPrimary)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Units Requested", color = Color.Gray, fontSize = 12.sp)
                                Text("${request.unitsRequired} Units (Whole Blood)", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Phone, contentDescription = null, tint = RedPrimary)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Contact Representative", color = Color.Gray, fontSize = 12.sp)
                                Text(request.contactNumber, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            }
                        }
                    }
                }

                if (isRequester) {
                    Button(
                        onClick = {
                            viewModel.fulfillRequest(request.id)
                            Toast.makeText(context, "Completed! Points and certificates added to donor.", Toast.LENGTH_LONG).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Text("MARK REQUEST AS FULFILLED ✅", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                } else {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(
                            onClick = {
                                viewModel.respondToRequest(request.id, "user_self")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = RedPrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1.2f)
                                .height(56.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("VOLUNTEER & CHAT", fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }

                        IconButton(
                            onClick = {
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_SUBJECT, "Urgent Blood Need")
                                    putExtra(Intent.EXTRA_TEXT, "Hemora Urgent: ${request.patientName} needs ${request.bloodGroup} Whole Blood at ${request.hospitalName}. Please help share!")
                                }
                                context.startActivity(Intent.createChooser(shareIntent, "Share Request"))
                            },
                            modifier = Modifier
                                .size(56.dp)
                                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
                        ) {
                            Icon(Icons.Default.Share, contentDescription = "Share", tint = RedPrimary)
                        }
                    }
                }

                Button(
                    onClick = {
                        val mapUri = Uri.parse("google.navigation:q=${request.hospitalLat},${request.hospitalLng}")
                        val mapIntent = Intent(Intent.ACTION_VIEW, mapUri).apply {
                            setPackage("com.google.android.apps.maps")
                        }
                        try {
                            context.startActivity(mapIntent)
                        } catch (e: Exception) {
                            Toast.makeText(context, "Opening Google Maps in web browser", Toast.LENGTH_SHORT).show()
                            val browserUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=${Uri.encode(request.hospitalName)}")
                            context.startActivity(Intent(Intent.ACTION_VIEW, browserUri))
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Navigation, contentDescription = null, tint = RedPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("NAVIGATE IN GOOGLE MAPS", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                    }
                }
            }
        }
    }
}

@Composable
fun ChatsListScreen(viewModel: LifeDropViewModel) {
    val chatMessages by viewModel.chatMessages.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val allUsers by viewModel.allUsers.collectAsState()
    val activeRequests by viewModel.activeRequests.collectAsState()

    val groupedMessages = chatMessages.groupBy { it.requestId }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(RedPrimary)
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            Text("Private Coordination Chats", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }

        if (groupedMessages.isEmpty()) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Forum, contentDescription = null, tint = RedContainer, modifier = Modifier.size(72.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("No active coordination messages.", color = Color.Gray, fontSize = 16.sp)
                    Text("Chat is opened once you respond to requests.", color = Color.Gray, fontSize = 12.sp)
                }
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(groupedMessages.keys.toList()) { reqId ->
                    val msgs = groupedMessages[reqId] ?: emptyList()
                    val lastMsg = msgs.last()
                    val request = activeRequests.find { it.id == reqId }
                    
                    val otherUserId = if (lastMsg.senderId == currentUser?.id) lastMsg.receiverId else lastMsg.senderId
                    val otherUser = allUsers.find { it.id == otherUserId }
                    val nameText = otherUser?.fullName ?: request?.requesterName ?: "Coordinator"

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                            .clickable {
                                viewModel.navigateTo(Screen.ChatRoom(reqId, otherUserId, nameText))
                            },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(RedContainer, shape = CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = nameText.firstOrNull()?.toString() ?: "U",
                                    color = RedPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(nameText, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                    Text(
                                        text = if (request?.isFulfilled == true) "Fulfilled" else "Active Match",
                                        color = if (request?.isFulfilled == true) Color.Gray else RedPrimary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                Text(
                                    text = lastMsg.text,
                                    color = Color.Gray,
                                    fontSize = 13.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatRoomScreen(
    viewModel: LifeDropViewModel,
    requestId: String,
    contactId: String,
    contactName: String
) {
    val chatMessages by viewModel.chatMessages.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val roomMessages = chatMessages.filter { it.requestId == requestId }
    val context = LocalContext.current

    var messageText by remember { mutableStateOf("") }
    var showShareLocationDialog by remember { mutableStateOf(false) }
    var selectedDurationIndex by remember { mutableStateOf(1) } // Default to "1 Hour"
    val durationsList = listOf("15 Minutes", "1 Hour", "8 Hours")

    val quickTemplates = listOf(
        "I'm on my way!",
        "What is your hospital contact?",
        "I have arrived at the blood bank."
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(contactName, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:3125550192"))
                            context.startActivity(intent)
                        }
                    ) {
                        Icon(Icons.Default.Call, contentDescription = "Call Representative", tint = RedPrimary)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(roomMessages) { msg ->
                        val isSelf = msg.senderId == currentUser?.id
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = if (isSelf) Arrangement.End else Arrangement.Start
                        ) {
                            if (msg.locationShareLat != null && msg.locationShareLng != null) {
                                // WhatsApp-style Live Location Bubble
                                Card(
                                    modifier = Modifier
                                        .width(260.dp)
                                        .clickable {
                                            viewModel.navigateTo(
                                                Screen.LiveLocationSharing(
                                                    requestId = requestId,
                                                    contactId = contactId,
                                                    contactName = contactName
                                                )
                                            )
                                        },
                                    shape = RoundedCornerShape(
                                        topStart = 16.dp,
                                        topEnd = 16.dp,
                                        bottomStart = if (isSelf) 16.dp else 0.dp,
                                        bottomEnd = if (isSelf) 0.dp else 16.dp
                                    ),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSelf) Color(0xFFFEF2F2) else Color.White
                                    ),
                                    border = BorderStroke(
                                        1.dp,
                                        if (isSelf) RedPrimary.copy(alpha = 0.3f) else Color(0xFFE2E8F0)
                                    ),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                ) {
                                    Column {
                                        // Header
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(if (isSelf) RedPrimary.copy(alpha = 0.08f) else Color(0xFFF1F5F9))
                                                .padding(horizontal = 12.dp, vertical = 8.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Default.PinDrop,
                                                    contentDescription = null,
                                                    tint = RedPrimary,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = "Live Location",
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 12.sp,
                                                    color = Color(0xFF1E293B)
                                                )
                                            }
                                            
                                            // Live indicator with flashing animation
                                            val infiniteTransition = rememberInfiniteTransition(label = "bubble_pulse")
                                            val alpha by infiniteTransition.animateFloat(
                                                initialValue = 0.4f,
                                                targetValue = 1.0f,
                                                animationSpec = infiniteRepeatable(
                                                    animation = tween(1000, easing = LinearEasing),
                                                    repeatMode = RepeatMode.Reverse
                                                ),
                                                label = "bubble_alpha"
                                            )
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(8.dp)
                                                        .background(Color(0xFF22C55E).copy(alpha = alpha), shape = CircleShape),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(4.dp)
                                                            .background(Color(0xFF22C55E), shape = CircleShape)
                                                    )
                                                }
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = "LIVE",
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Black,
                                                    color = Color(0xFF22C55E)
                                                )
                                            }
                                        }
                                        
                                        // Mini Map graphic
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(95.dp)
                                                .background(Color(0xFFF1F5F9))
                                        ) {
                                            Canvas(modifier = Modifier.fillMaxSize()) {
                                                val w = size.width
                                                val h = size.height
                                                
                                                // Mini road grids
                                                val roadCol = Color.White
                                                drawLine(roadCol, Offset(0f, h / 2), Offset(w, h / 2), strokeWidth = 8f)
                                                drawLine(roadCol, Offset(w / 3, 0f), Offset(w / 3, h), strokeWidth = 8f)
                                                drawLine(roadCol, Offset(2 * w / 3, 0f), Offset(2 * w / 3, h), strokeWidth = 8f)
                                                
                                                // Connection routing line
                                                drawLine(
                                                    color = Color(0xFF0284C7),
                                                    start = Offset(w / 3, h / 2),
                                                    end = Offset(2 * w / 3, h / 2),
                                                    strokeWidth = 4f,
                                                    pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(
                                                        floatArrayOf(10f, 10f), 0f
                                                    )
                                                )
                                            }
                                            
                                            // Pin 1 (You)
                                            Box(
                                                modifier = Modifier
                                                    .offset(x = 65.dp, y = 38.dp)
                                                    .size(16.dp)
                                                    .background(Color(0xFF3B82F6).copy(alpha = 0.2f), shape = CircleShape),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(8.dp)
                                                        .background(Color(0xFF3B82F6), shape = CircleShape)
                                                        .border(1.dp, Color.White, CircleShape)
                                                )
                                            }

                                            // Pin 2 (Friend)
                                            Box(
                                                modifier = Modifier
                                                    .offset(x = 155.dp, y = 38.dp)
                                                    .size(16.dp)
                                                    .background(RedPrimary.copy(alpha = 0.2f), shape = CircleShape),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(8.dp)
                                                        .background(RedPrimary, shape = CircleShape)
                                                        .border(1.dp, Color.White, CircleShape)
                                                )
                                            }
                                            
                                            // Label overlay
                                            Box(
                                                modifier = Modifier
                                                    .align(Alignment.BottomEnd)
                                                    .padding(6.dp)
                                                    .background(Color.Black.copy(alpha = 0.6f), shape = RoundedCornerShape(4.dp))
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Text(
                                                    text = "1.2 km",
                                                    color = Color.White,
                                                    fontSize = 8.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                        
                                        // Bottom CTA bar
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(Color.White)
                                                .padding(8.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "VIEW LIVE LOCATION",
                                                color = RedPrimary,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp,
                                                letterSpacing = 0.5.sp
                                            )
                                        }
                                    }
                                }
                            } else {
                                Box(
                                    modifier = Modifier
                                        .background(
                                            color = if (isSelf) RedPrimary else MaterialTheme.colorScheme.surfaceVariant,
                                            shape = RoundedCornerShape(
                                                topStart = 12.dp,
                                                topEnd = 12.dp,
                                                bottomStart = if (isSelf) 12.dp else 0.dp,
                                                bottomEnd = if (isSelf) 0.dp else 12.dp
                                            )
                                        )
                                        .padding(12.dp)
                                        .widthIn(max = 280.dp)
                                ) {
                                    Text(
                                        text = msg.text,
                                        color = if (isSelf) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                quickTemplates.forEach { temp ->
                    Box(
                        modifier = Modifier
                            .background(RedContainer, shape = RoundedCornerShape(16.dp))
                            .clickable {
                                viewModel.sendChatMessage(requestId, contactId, temp)
                            }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(temp, color = RedPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        showShareLocationDialog = true
                    }
                ) {
                    Icon(Icons.Default.PinDrop, contentDescription = "Share Location", tint = RedPrimary)
                }

                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    placeholder = { Text("Write message...") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (messageText.isNotEmpty()) {
                            viewModel.sendChatMessage(requestId, contactId, messageText)
                            messageText = ""
                        }
                    },
                    modifier = Modifier
                        .background(RedPrimary, shape = CircleShape)
                        .size(44.dp)
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White)
                }
            }
        }
    }

    if (showShareLocationDialog) {
        AlertDialog(
            onDismissRequest = { showShareLocationDialog = false },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(RedPrimary.copy(alpha = 0.1f), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("📍", fontSize = 18.sp)
                    }
                    Text(
                        text = "Share Live Location",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF1E293B)
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Your live location will be visible to $contactName on the map in real-time. This helps you coordinate the blood delivery or donation efficiently.",
                        fontSize = 13.sp,
                        color = Color(0xFF475569)
                    )
                    Text(
                        text = "Select sharing duration:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = RedPrimary
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        durationsList.forEachIndexed { idx, label ->
                            val isSelected = selectedDurationIndex == idx
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(
                                        color = if (isSelected) RedPrimary else Color(0xFFF1F5F9),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .border(
                                        1.5.dp,
                                        if (isSelected) RedPrimary else Color(0xFFE2E8F0),
                                        RoundedCornerShape(12.dp)
                                    )
                                    .clickable { selectedDurationIndex = idx }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else Color(0xFF475569)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(GoldAccent.copy(alpha = 0.15f), shape = RoundedCornerShape(10.dp))
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("✨", fontSize = 14.sp)
                        Text(
                            text = "Sharing awards you 10 Saving Points!",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF8B6E00)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.sendLocationMessage(requestId, contactId, 41.8781, -87.6298)
                        viewModel.addPoints(10)
                        Toast.makeText(context, "Live Location Shared for ${durationsList[selectedDurationIndex]}!", Toast.LENGTH_SHORT).show()
                        showShareLocationDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RedPrimary),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("START SHARING", fontWeight = FontWeight.Bold, color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showShareLocationDialog = false }) {
                    Text("CANCEL", color = Color(0xFF64748B))
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveLocationSharingScreen(
    viewModel: LifeDropViewModel,
    requestId: String,
    contactId: String,
    contactName: String
) {
    val context = LocalContext.current
    
    // Live Location Simulation State
    var isSimulating by remember { mutableStateOf(false) }
    var simulationProgress by remember { mutableStateOf(0.0f) } // 0.0 to 1.0
    
    // Status states
    var myBattery by remember { mutableStateOf(94) }
    var friendBattery by remember { mutableStateOf(89) }
    var showingRendezvousSuccess by remember { mutableStateOf(false) }
    var buzzerCount by remember { mutableStateOf(0) }
    
    // Route locations
    val userStartLoc = Offset(240f, 130f)
    val contactStartLoc = Offset(130f, 350f)
    
    // Interpolated simulated routing path segments
    val pathSegments = listOf(
        userStartLoc,
        Offset(240f, 240f),
        Offset(130f, 240f),
        contactStartLoc
    )
    
    // Dynamic distance & ETA based on animation progress
    val distanceRemainingKm = remember(simulationProgress) {
        ((1.0f - simulationProgress) * 1.6).coerceAtLeast(0.0)
    }
    
    val etaRemainingMinutes = remember(simulationProgress) {
        ((1.0f - simulationProgress) * 5.5).coerceAtLeast(0.0)
    }
    
    val simulatedSpeedMph = remember(isSimulating, simulationProgress) {
        if (!isSimulating) 0 else {
            when {
                simulationProgress > 0.9f -> 3
                simulationProgress > 0.6f -> 12
                else -> (18..25).random()
            }
        }
    }
    
    val simulatedStreet = remember(simulationProgress) {
        when {
            simulationProgress < 0.3f -> "Grand Avenue (Heading South)"
            simulationProgress in 0.3f..0.7f -> "Turning Right onto Erie Street"
            simulationProgress in 0.7f..0.9f -> "Entering Plaza Court Plaza"
            else -> "Arrived at meeting point!"
        }
    }
    
    // Simulation Loop
    LaunchedEffect(isSimulating) {
        if (isSimulating) {
            while (simulationProgress < 1.0f) {
                delay(200)
                simulationProgress = (simulationProgress + 0.02f).coerceAtMost(1.0f)
                
                // Randomly vary battery slightly for realism
                if (Math.random() < 0.1) {
                    myBattery = (myBattery - 1).coerceAtLeast(1)
                }
                if (Math.random() < 0.08) {
                    friendBattery = (friendBattery - 1).coerceAtLeast(1)
                }
            }
            isSimulating = false
            showingRendezvousSuccess = true
            viewModel.addPoints(50)
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Live Location Sharing",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(Color(0xFF22C55E), shape = CircleShape)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Active sharing with $contactName",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            isSimulating = !isSimulating
                            if (isSimulating && simulationProgress >= 1.0f) {
                                simulationProgress = 0.0f
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isSimulating) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "Simulate",
                            tint = RedPrimary
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF1F5F9))
        ) {
            // Live Status Header Banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Simulated Compass/Indicator
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(Color(0xFF334155), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (simulationProgress >= 1.0f) "🎉" else "📍",
                            fontSize = 20.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (simulationProgress >= 1.0f) "Rendezvous Met!" else simulatedStreet,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Dist: ${"%.2f".format(distanceRemainingKm)} km",
                                color = GoldAccent,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text("•", color = Color(0xFF64748B), fontSize = 11.sp)
                            Text(
                                text = "ETA: ${"%.1f".format(etaRemainingMinutes)} mins",
                                color = Color(0xFF38BDF8),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            if (simulatedSpeedMph > 0) {
                                Text("•", color = Color(0xFF64748B), fontSize = 11.sp)
                                Text(
                                    text = "$simulatedSpeedMph mph",
                                    color = Color(0xFF22C55E),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Interactive Live Grid Map Canvas
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .background(Color(0xFFE2E8F0), shape = RoundedCornerShape(20.dp))
                    .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
            ) {
                // Determine current animated coordinates along path segments
                val segmentCount = pathSegments.size - 1
                val activeSegIndex = (simulationProgress * segmentCount).toInt().coerceAtMost(segmentCount - 1)
                val activeSegProgress = (simulationProgress * segmentCount) - activeSegIndex
                
                val currentMyPos = if (simulationProgress >= 1.0f) contactStartLoc else {
                    val sPt = pathSegments[activeSegIndex]
                    val ePt = pathSegments[activeSegIndex + 1]
                    Offset(
                        sPt.x + (ePt.x - sPt.x) * activeSegProgress,
                        sPt.y + (ePt.y - sPt.y) * activeSegProgress
                    )
                }
                
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    
                    // Draw lush parks
                    drawRect(
                        color = Color(0xFFDCFCE7),
                        topLeft = Offset(20f, 40f),
                        size = Size(110f, 150f)
                    )
                    drawCircle(
                        color = Color(0xFFDCFCE7),
                        radius = 90f,
                        center = Offset(w - 120f, h - 140f)
                    )
                    
                    // Draw Water Body / Canal
                    drawRect(
                        color = Color(0xFFE0F2FE),
                        topLeft = Offset(w - 90f, 20f),
                        size = Size(70f, 220f)
                    )
                    
                    // Draw complex Grid Road lines
                    val roadCol = Color.White
                    val roadBorder = Color(0xFF94A3B8)
                    
                    fun drawDetailedRoad(start: Offset, end: Offset) {
                        drawLine(roadBorder, start, end, strokeWidth = 28f, cap = StrokeCap.Round)
                        drawLine(roadCol, start, end, strokeWidth = 24f, cap = StrokeCap.Round)
                    }
                    
                    // Horizontal roads
                    drawDetailedRoad(Offset(0f, 130f), Offset(w, 130f))
                    drawDetailedRoad(Offset(0f, 240f), Offset(w, 240f))
                    drawDetailedRoad(Offset(0f, 350f), Offset(w, 350f))
                    
                    // Vertical roads
                    drawDetailedRoad(Offset(130f, 0f), Offset(130f, h))
                    drawDetailedRoad(Offset(240f, 0f), Offset(240f, h))
                    drawDetailedRoad(Offset(w - 120f, 0f), Offset(w - 120f, h))

                    // Draw Live routing path
                    val routePath = androidx.compose.ui.graphics.Path().apply {
                        moveTo(userStartLoc.x.dp.toPx(), userStartLoc.y.dp.toPx())
                        for (i in 1 until pathSegments.size) {
                            lineTo(pathSegments[i].x.dp.toPx(), pathSegments[i].y.dp.toPx())
                        }
                    }
                    
                    // Route glow outline
                    drawPath(
                        path = routePath,
                        color = Color(0xFF38BDF8).copy(alpha = 0.45f),
                        style = Stroke(width = 14f, cap = StrokeCap.Round)
                    )
                    // Inner route routing dash
                    drawPath(
                        path = routePath,
                        color = Color(0xFF0284C7),
                        style = Stroke(width = 6f, cap = StrokeCap.Round)
                    )
                }
                
                // Render Pins dynamically
                
                // Pin 1: My Moving Avatar Pin (or Blue Dot)
                Box(
                    modifier = Modifier
                        .offset(x = currentMyPos.x.dp, y = currentMyPos.y.dp)
                        .size(34.dp)
                        .background(Color(0xFF3B82F6).copy(alpha = 0.25f), shape = CircleShape)
                        .border(1.5.dp, Color(0xFF3B82F6), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🚑", fontSize = 16.sp)
                }

                // Pin 2: Matched Friend Pin (Static Rendezvous Point or Matched Donor)
                Box(
                    modifier = Modifier
                        .offset(x = contactStartLoc.x.dp, y = contactStartLoc.y.dp)
                        .size(34.dp)
                        .background(RedPrimary.copy(alpha = 0.25f), shape = CircleShape)
                        .border(1.5.dp, RedPrimary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🏥", fontSize = 16.sp)
                }
                
                // Floating Action Map controls
                Column(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FloatingActionButton(
                        onClick = {
                            simulationProgress = 0.0f
                            isSimulating = false
                        },
                        containerColor = Color.White,
                        contentColor = Color(0xFF475569),
                        modifier = Modifier.size(40.dp),
                        shape = CircleShape
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Reset Route", modifier = Modifier.size(18.dp))
                    }
                    
                    FloatingActionButton(
                        onClick = {
                            myBattery = 99
                            friendBattery = 95
                            Toast.makeText(context, "Signals & Battery Calibrated!", Toast.LENGTH_SHORT).show()
                        },
                        containerColor = Color.White,
                        contentColor = Color(0xFF475569),
                        modifier = Modifier.size(40.dp),
                        shape = CircleShape
                    ) {
                        Icon(Icons.Default.LocationSearching, contentDescription = "Calibrate GPS", modifier = Modifier.size(18.dp))
                    }
                }
                
                // Calibration banner overlay at bottom
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp)
                        .background(Color(0xFF1E293B).copy(alpha = 0.85f), shape = RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("📡", fontSize = 10.sp)
                        Text(
                            text = "GPS: Accurate to 4m • 4G LTE",
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Bottom Control Sheet (WhatsApp styled)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Participant Details Panel
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // User Profile Panel
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(Color(0xFFDBEAFE), shape = CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🧑‍⚕️", fontSize = 20.sp)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("You (Donor)", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF1E293B))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("🔋 $myBattery%", fontSize = 10.sp, color = Color(0xFF15803D), fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("⚡ GPS Active", fontSize = 10.sp, color = Color(0xFF64748B))
                                }
                            }
                        }
                        
                        // Connection Line
                        Box(
                            modifier = Modifier
                                .width(30.dp)
                                .height(1.dp)
                                .background(Color(0xFFE2E8F0))
                        )
                        
                        // matched contact panel
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(horizontalAlignment = Alignment.End) {
                                Text(contactName, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF1E293B))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("GPS Synced", fontSize = 10.sp, color = Color(0xFF0284C7))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("🔋 $friendBattery%", fontSize = 10.sp, color = Color(0xFF15803D), fontWeight = FontWeight.Bold)
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(Color(0xFFFEE2E2), shape = CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🩸", fontSize = 20.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Buttons Drawer
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Buzz Friend Button (simulates a WhatsApp buzzer nudge)
                        OutlinedButton(
                            onClick = {
                                buzzerCount++
                                Toast.makeText(context, "🚨 Buzzed $contactName with high priority ping!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, RedPrimary.copy(alpha = 0.5f))
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text("📣", fontSize = 14.sp)
                                Text("BUZZ MATCH", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = RedPrimary)
                            }
                        }

                        // Stop Sharing (closes live location session)
                        Button(
                            onClick = {
                                Toast.makeText(context, "Stopped live location sharing session.", Toast.LENGTH_SHORT).show()
                                viewModel.navigateBack()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF64748B)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text("🛑", fontSize = 14.sp)
                                Text("STOP SHARING", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                    
                    if (simulationProgress < 1.0f) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                isSimulating = true
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = RedPrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text("🏃‍♂️", fontSize = 14.sp)
                                Text("SIMULATE MEETING UP", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }

    // Success Rendezvous Dialog
    if (showingRendezvousSuccess) {
        AlertDialog(
            onDismissRequest = { showingRendezvousSuccess = false },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("🏆 Rendezvous Succeeded!", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column {
                    Text(
                        text = "Outstanding! You have successfully arrived at the designated meeting point and physically reached $contactName.",
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(GoldAccent.copy(alpha = 0.15f), shape = RoundedCornerShape(12.dp))
                            .border(1.dp, GoldAccent, shape = RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("✨", fontSize = 20.sp)
                            Column {
                                Text("SAVIOR XP AWARDED", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFF8B6E00))
                                Text("+50 Saving Points Added!", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = RedPrimary)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showingRendezvousSuccess = false },
                    colors = ButtonDefaults.buttonColors(containerColor = RedPrimary)
                ) {
                    Text("AWESOME", color = Color.White)
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(viewModel: LifeDropViewModel) {
    val notifications by viewModel.notifications.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Updates & Alerts", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (notifications.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("No notifications yet.", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(notifications) { notif ->
                    val borderStroke = if (!notif.isRead) BorderStroke(1.5.dp, RedPrimary) else null
                    val bgColor = if (notif.type == "emergency") RedContainer else MaterialTheme.colorScheme.surface

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.markNotificationAsRead(notif.id) },
                        colors = CardDefaults.cardColors(containerColor = bgColor),
                        border = borderStroke,
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (notif.type == "emergency") Icons.Default.Warning else Icons.Default.Notifications,
                                contentDescription = null,
                                tint = RedPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(notif.title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(notif.message, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BloodBankDirectoryScreen(viewModel: LifeDropViewModel) {
    val bloodBanks by viewModel.bloodBanks.collectAsState()
    val geminiResponse by viewModel.geminiResponse.collectAsState()
    val geminiLoading by viewModel.geminiLoading.collectAsState()
    val context = LocalContext.current

    var aiSearchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF1E1E1E), Color(0xFF121212))
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .background(GoldAccent, CircleShape)
                            .padding(6.dp)
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = RedPrimary, modifier = Modifier.size(16.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Intelligent Maps Grounded Assistant", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "Ask our AI to locate any blood bank or hospital in your city instantly. Coordinates are compiled in real-time.",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = aiSearchQuery,
                        onValueChange = { aiSearchQuery = it },
                        placeholder = { Text("e.g. Find blood banks in Chicago", color = Color.Gray) },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GoldAccent,
                            unfocusedBorderColor = Color.Gray,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (aiSearchQuery.isNotEmpty()) {
                                viewModel.askGeminiAssistant(aiSearchQuery)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RedPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.height(56.dp)
                    ) {
                        Text("ASK AI", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }

                if (geminiLoading || geminiResponse.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.08f)),
                        border = BorderStroke(1.dp, GoldAccent.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            if (geminiLoading) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    CircularProgressIndicator(modifier = Modifier.size(18.dp), color = GoldAccent, strokeWidth = 2.dp)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text("AI is querying Google Maps and Search...", color = GoldAccent, fontSize = 12.sp)
                                }
                            } else {
                                Text("AI Assistant Output:", fontWeight = FontWeight.Bold, color = GoldAccent, fontSize = 12.sp)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(geminiResponse, color = Color.White, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }
        }

        Text(
            text = "Verified Blood Centers Nearby",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
        )

        bloodBanks.forEach { bank ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(bank.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.weight(1f))
                        Box(
                            modifier = Modifier
                                .background(
                                    color = if (bank.isOpen) Color(0xFFE8F5E9) else RedContainer,
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (bank.isOpen) "Open" else "Closed",
                                color = if (bank.isOpen) Color(0xFF2E7D32) else RedPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(bank.address, fontSize = 13.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Distance: ${bank.distance} km", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = RedPrimary)
                        
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            IconButton(
                                onClick = {
                                    val dial = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${bank.phone}"))
                                    context.startActivity(dial)
                                },
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(RedContainer, CircleShape)
                            ) {
                                Icon(Icons.Default.Call, contentDescription = "Call", tint = RedPrimary, modifier = Modifier.size(16.dp))
                            }

                            IconButton(
                                onClick = {
                                    val gmapsIntent = Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=${bank.lat},${bank.lng}")).apply {
                                        setPackage("com.google.android.apps.maps")
                                    }
                                    context.startActivity(gmapsIntent)
                                },
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape)
                            ) {
                                Icon(Icons.Default.Navigation, contentDescription = "Navigate", tint = RedPrimary, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EligibilityCheckerScreen(viewModel: LifeDropViewModel) {
    var ageInput by remember { mutableStateOf("25") }
    var weightInput by remember { mutableStateOf("65") }
    var hasIllness by remember { mutableStateOf(false) }
    var gapInput by remember { mutableStateOf(true) }
    var resultText by remember { mutableStateOf("") }
    var resultOk by remember { mutableStateOf<Boolean?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Eligibility Diagnostics", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Verify your donating parameters before visiting the hospital.",
                fontSize = 14.sp,
                color = Color.Gray
            )

            OutlinedTextField(
                value = ageInput,
                onValueChange = { ageInput = it },
                label = { Text("Your Age") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = weightInput,
                onValueChange = { weightInput = it },
                label = { Text("Your Weight (kg)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Recent Illness / Fever", fontWeight = FontWeight.SemiBold)
                            Text("Any active infection or illness within past 14 days", fontSize = 12.sp, color = Color.Gray)
                        }
                        Switch(
                            checked = hasIllness,
                            onCheckedChange = { hasIllness = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = RedPrimary)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("90-Day Donation Gap", fontWeight = FontWeight.SemiBold)
                            Text("At least 3 months have passed since your last donation", fontSize = 12.sp, color = Color.Gray)
                        }
                        Switch(
                            checked = gapInput,
                            onCheckedChange = { gapInput = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = RedPrimary)
                        )
                    }
                }
            }

            Button(
                onClick = {
                    val age = ageInput.toIntOrNull() ?: 0
                    val weight = weightInput.toDoubleOrNull() ?: 0.0
                    
                    if (age < 18 || age > 65) {
                        resultOk = false
                        resultText = "❌ Not Eligible: You must be between 18 and 65 years old to donate blood."
                    } else if (weight < 50.0) {
                        resultOk = false
                        resultText = "❌ Not Eligible: Minimum weight threshold is 50 kg."
                    } else if (hasIllness) {
                        resultOk = false
                        resultText = "❌ Not Eligible: You must recover fully from active illnesses (wait at least 14 days)."
                    } else if (!gapInput) {
                        resultOk = false
                        resultText = "❌ Not Eligible: At least 90 days must pass between whole blood donations."
                    } else {
                        resultOk = true
                        resultText = "✅ Eligible! You meet all whole blood donor parameters. Feel free to volunteer."
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RedPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("RUN DIAGNOSTIC EVALUATION", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
            }

            if (resultText.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (resultOk == true) Color(0xFFE8F5E9) else RedContainer
                    ),
                    border = BorderStroke(1.5.dp, if (resultOk == true) Color(0xFF2E7D32) else RedPrimary)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = resultText,
                            fontWeight = FontWeight.Bold,
                            color = if (resultOk == true) Color(0xFF2E7D32) else RedPrimary,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonationHistoryListScreen(viewModel: LifeDropViewModel) {
    val donationHistory by viewModel.donationHistory.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val context = LocalContext.current

    var selectedCert by remember { mutableStateOf<DonationHistory?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Donation Registry & Awards", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "My Donation History",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            if (donationHistory.isEmpty()) {
                Box(modifier = Modifier.height(100.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text("No past donations registered yet.", color = Color.Gray)
                }
            } else {
                donationHistory.forEach { dh ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(dh.hospitalName, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Text("Date: ${dh.date} | ${dh.units} Unit whole blood", fontSize = 12.sp, color = Color.Gray)
                            }
                            Button(
                                onClick = { selectedCert = dh },
                                colors = ButtonDefaults.buttonColors(containerColor = RedContainer),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("AWARD 🏆", color = RedPrimary, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }

            if (selectedCert != null) {
                Spacer(modifier = Modifier.height(32.dp))
                
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(8.dp, RoundedCornerShape(16.dp))
                        .background(Color.White, RoundedCornerShape(16.dp))
                        .border(4.dp, GoldAccent, RoundedCornerShape(16.dp))
                        .padding(2.dp)
                        .border(1.5.dp, RedPrimary, RoundedCornerShape(14.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(56.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("CERTIFICATE OF APPRECIATION", color = RedPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp, letterSpacing = 1.sp)
                        Text("PROUDLY PRESENTED TO", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = currentUser?.fullName ?: "Noble Donor",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                        Box(modifier = Modifier.width(180.dp).height(1.dp).background(RedPrimary).padding(vertical = 4.dp))
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "In recognition of selfless blood donation on ${selectedCert!!.date} at ${selectedCert!!.hospitalName}, helping save multiple lives in emergencies.",
                            fontSize = 12.sp,
                            color = Color.DarkGray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        Spacer(modifier = Modifier.height(24.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("LIFEDROP NETWORK", color = RedPrimary, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                                Text("Official Coordinator", color = Color.Gray, fontSize = 8.sp)
                            }
                            IconButton(
                                onClick = {
                                    Toast.makeText(context, "Certificate PNG downloaded to storage!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.background(RedContainer, CircleShape)
                            ) {
                                Icon(Icons.Default.Download, contentDescription = "Save Cert", tint = RedPrimary)
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(viewModel: LifeDropViewModel) {
    val leaderboard = viewModel.getLeaderboard()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Top Donors Leaderboard", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(RedPrimary)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("2nd", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                        Box(modifier = Modifier.size(40.dp).background(Color.White.copy(alpha = 0.2f), CircleShape), contentAlignment = Alignment.Center) {
                            Text("D", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("David", color = Color.White, fontSize = 11.sp)
                        Text("500 pts", color = GoldAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.EmojiEvents, contentDescription = "Winner", tint = GoldAccent, modifier = Modifier.size(28.dp))
                        Text("1st", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Box(modifier = Modifier.size(54.dp).background(GoldAccent, CircleShape).border(2.dp, Color.White, CircleShape), contentAlignment = Alignment.Center) {
                            Text("M", color = RedPrimary, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Michael", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text("750 pts", color = GoldAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("3rd", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                        Box(modifier = Modifier.size(40.dp).background(Color.White.copy(alpha = 0.2f), CircleShape), contentAlignment = Alignment.Center) {
                            Text("S", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Sarah", color = Color.White, fontSize = 11.sp)
                        Text("250 pts", color = GoldAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                items(leaderboard) { entry ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .background(RedContainer, shape = CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("#${entry.rank}", color = RedPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(entry.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text("${entry.city} | ${entry.bloodGroup}", fontSize = 11.sp, color = Color.Gray)
                                }
                            }
                            Text("${entry.points} PTS", fontWeight = FontWeight.Bold, color = RedPrimary, fontSize = 15.sp)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementsScreen(viewModel: LifeDropViewModel) {
    val currentUser by viewModel.currentUser.collectAsState()
    val earnedBadges = currentUser?.badges ?: listOf("First Profile")

    val allBadgesList = listOf(
        Pair("First Profile", "Completed registration setup correctly (+50 pts)"),
        Pair("First Donor", "Volunteered and saved your first patient emergency match (+100 pts)"),
        Pair("Life Saver", "Completed at least 5 certified blood donations (+300 pts)"),
        Pair("Hero", "Completed at least 10 certified blood donations (+500 pts)"),
        Pair("Legend", "Completed 25+ certified blood donations (+1000 pts)")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gamified Achievements", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Earn unique life saver medals and boost your community standing.", fontSize = 14.sp, color = Color.Gray)

            allBadgesList.forEach { (badgeTitle, desc) ->
                val hasBadge = earnedBadges.contains(badgeTitle)
                val badgeColor = if (hasBadge) GoldAccent else Color.LightGray
                val tintColor = if (hasBadge) RedPrimary else Color.Gray

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (hasBadge) RedContainer.copy(alpha = 0.3f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                    ),
                    border = if (hasBadge) BorderStroke(1.5.dp, GoldAccent) else null
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(badgeColor, shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.WorkspacePremium,
                                contentDescription = null,
                                tint = tintColor,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = badgeTitle,
                                fontWeight = FontWeight.Bold,
                                color = if (hasBadge) RedPrimary else Color.Gray,
                                fontSize = 16.sp
                            )
                            Text(desc, fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsScreen(viewModel: LifeDropViewModel) {
    val currentUser by viewModel.currentUser.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(RedPrimary)
                .padding(24.dp)
        ) {
            Column {
                Text(currentUser?.fullName ?: "Sarah Jenkins", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text("App Version 1.0.4 - Premium Production", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val settingsGroup = listOf(
            Triple("Edit Profile Details", Icons.Default.Edit, Screen.EditProfileScreen),
            Triple("Donation Reg & Awards", Icons.Default.EmojiEvents, Screen.DonationHistoryList),
            Triple("Gamified Achievements", Icons.Default.WorkspacePremium, Screen.AchievementsScreen),
            Triple("Score Leaderboard", Icons.Default.FormatListNumbered, Screen.LeaderboardScreen),
            Triple("Medical Diagnostic quiz", Icons.Default.VerifiedUser, Screen.EligibilityChecker)
        )

        settingsGroup.forEach { (title, icon, route) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.navigateTo(route) }
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(icon, contentDescription = null, tint = RedPrimary)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(title, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                }
                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
            }
            HorizontalDivider(modifier = Modifier.padding(horizontal = 24.dp))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Show Approximate Location Only", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                Text("Blurs your map pins (+-2km radius) to secure your exact address.", fontSize = 11.sp, color = Color.Gray)
            }
            Switch(
                checked = currentUser?.showApproximateLocationOnly ?: false,
                onCheckedChange = {
                    val current = currentUser ?: return@Switch
                    viewModel.editProfile(current.copy(showApproximateLocationOnly = it))
                },
                colors = SwitchDefaults.colors(checkedThumbColor = RedPrimary)
            )
        }
        HorizontalDivider(modifier = Modifier.padding(horizontal = 24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_SUBJECT, "Join Hemora")
                        putExtra(Intent.EXTRA_TEXT, "Join Hemora Blood Donation Network to save critical emergency lives. Use my link to get 50pts bonus: https://hemora.org/invite/sarah")
                    }
                    context.startActivity(Intent.createChooser(intent, "Share Referral Link"))
                }
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.GroupAdd, contentDescription = null, tint = RedPrimary)
            Spacer(modifier = Modifier.width(16.dp))
            Text("Invite Friends (+20 pts Referral)", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
        }

        HorizontalDivider(modifier = Modifier.padding(horizontal = 24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { viewModel.logout() }
                .padding(horizontal = 24.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.ExitToApp, contentDescription = null, tint = RedPrimary)
            Spacer(modifier = Modifier.width(16.dp))
            Text("Logout Session", fontWeight = FontWeight.Bold, color = RedPrimary, fontSize = 15.sp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(viewModel: LifeDropViewModel) {
    val currentUser by viewModel.currentUser.collectAsState()

    var name by remember { mutableStateOf(currentUser?.fullName ?: "") }
    var city by remember { mutableStateOf(currentUser?.city ?: "") }
    var age by remember { mutableStateOf((currentUser?.age ?: 25).toString()) }
    val bloodGroups = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
    var selectedGroup by remember { mutableStateOf(currentUser?.bloodGroup ?: "O+") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile Details", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = city,
                onValueChange = { city = it },
                label = { Text("Current City") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = age,
                onValueChange = { age = it },
                label = { Text("Age") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Column {
                Text("My Blood Group", fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.padding(bottom = 6.dp))
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    bloodGroups.forEach { group ->
                        val isSelected = selectedGroup == group
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(if (isSelected) RedPrimary else RedContainer, shape = RoundedCornerShape(8.dp))
                                .clickable { selectedGroup = group },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(group, color = if (isSelected) Color.White else RedPrimary, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val updated = currentUser?.copy(
                        fullName = name,
                        city = city,
                        age = age.toIntOrNull() ?: 25,
                        bloodGroup = selectedGroup
                    )
                    if (updated != null) {
                        viewModel.editProfile(updated)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RedPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("SAVE PROFILE CHANGES", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@Composable
fun AdminLoginScreen(viewModel: LifeDropViewModel) {
    var passwordInput by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = RedPrimary, modifier = Modifier.size(72.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Text("Admin Access Portal", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = RedPrimary)
        Text("Protected Area. Enter standard passcode.", fontSize = 13.sp, color = Color.Gray, textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = passwordInput,
            onValueChange = { passwordInput = it },
            label = { Text("Secure Admin Password") },
            placeholder = { Text("admin123") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        if (errorMsg.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(errorMsg, color = RedPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (passwordInput == "admin123" || passwordInput == "admin") {
                    viewModel.clearHistoryAndNavigateTo(Screen.AdminDashboardScreen)
                } else {
                    errorMsg = "Unauthorized access passcode. Please try again."
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag("admin_login_submit"),
            colors = ButtonDefaults.buttonColors(containerColor = RedPrimary),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("AUTHENTICATE ADMIN", fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = { viewModel.clearHistoryAndNavigateTo(Screen.Splash) }) {
            Text("Return to Application", color = RedPrimary)
        }
    }
}

@Composable
fun AdminDashboardScreen(viewModel: LifeDropViewModel) {
    val stats = viewModel.getAdminStats()
    val allUsers by viewModel.allUsers.collectAsState()

    var broadcastTitle by remember { mutableStateOf("") }
    var broadcastMsg by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1E1E1E))
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Control Panel", color = GoldAccent, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                    Text("Emergency Broadcast Authority active", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                }
                IconButton(onClick = { viewModel.clearHistoryAndNavigateTo(Screen.Home) }) {
                    Icon(Icons.Default.ExitToApp, contentDescription = "Exit Admin", tint = Color.White)
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val items = listOf(
                Pair("Total Donors", stats["TotalDonors"].toString()),
                Pair("Active Needs", stats["ActiveRequests"].toString()),
                Pair("Fulfilled", stats["FulfilledToday"].toString())
            )
            items.forEach { (lbl, cnt) ->
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(cnt, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = RedPrimary)
                        Text(lbl, fontSize = 11.sp, color = Color.Gray)
                    }
                }
            }
        }

        Text(
            text = "Blood Group Distribution (Verified Donors)",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(200.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Canvas(modifier = Modifier.size(140.dp)) {
                    val diam = size.minDimension
                    drawArc(Color(0xFFB71C1C), 0f, 130f, true, size = Size(diam, diam))
                    drawArc(Color(0xFFE53935), 130f, 90f, true, size = Size(diam, diam))
                    drawArc(Color(0xFFD32F2F), 220f, 80f, true, size = Size(diam, diam))
                    drawArc(Color(0xFFFFD700), 300f, 60f, true, size = Size(diam, diam))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(10.dp).background(Color(0xFFB71C1C)))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("O+ Donors (36%)", fontSize = 11.sp)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(10.dp).background(Color(0xFFE53935)))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("A- Donors (25%)", fontSize = 11.sp)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(10.dp).background(Color(0xFFD32F2F)))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("B+ Donors (22%)", fontSize = 11.sp)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(10.dp).background(Color(0xFFFFD700)))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("AB- Donors (17%)", fontSize = 11.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Broadcast Emergency Alert (Simulated)",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = broadcastTitle,
                    onValueChange = { broadcastTitle = it },
                    label = { Text("Emergency Title") },
                    placeholder = { Text("e.g. Critical Blood Deficit O-") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = broadcastMsg,
                    onValueChange = { broadcastMsg = it },
                    label = { Text("Notification Body Text") },
                    placeholder = { Text("Required immediately at Mercy Hospital.") },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        if (broadcastTitle.isNotEmpty() && broadcastMsg.isNotEmpty()) {
                            viewModel.broadcastEmergency(broadcastTitle, broadcastMsg)
                            broadcastTitle = ""
                            broadcastMsg = ""
                            Toast.makeText(context, "FCM Emergency Broadcast sent successfully!", Toast.LENGTH_LONG).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = RedPrimary)
                ) {
                    Text("PUSH FCM BROADCAST 🚨", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Manage Users and Reports",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        allUsers.forEach { user ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(user.fullName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("${user.city} | Type ${user.bloodGroup}", fontSize = 11.sp, color = Color.Gray)
                    }
                    Button(
                        onClick = {
                            viewModel.banUser(user.id, !user.isBanned)
                            Toast.makeText(context, "User status modified!", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (user.isBanned) Color(0xFF2E7D32) else RedPrimary
                        ),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(if (user.isBanned) "UNBAN" else "BAN USER", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}
