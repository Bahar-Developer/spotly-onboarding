package com.smart.kahkashan.spotlyonboardinglibrary.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smart.kahkashan.spotlyonboardinglibrary.ui.theme.SpotlyOnboardingLibraryTheme
import com.spotly.onboarding.model.GuideStep
import com.spotly.onboarding.model.SpotlightShape
import com.spotly.onboarding.model.TargetShape
import com.spotly.onboarding.theme.SpotlyColors
import com.spotly.onboarding.ui.SpotlyGuideManager

enum class AppScreen {
    HOME,
    SHIFTS,
    PROFILE
}

@Composable
fun SpotlyDemoScreen() {
    var currentScreen by remember { mutableStateOf(AppScreen.HOME) }
    val targetRects = remember { mutableStateMapOf<String, Rect>() }

    var currentIndex by remember { mutableIntStateOf(0) }
    var isGuideVisible by remember { mutableStateOf(true) }

    // همگام‌سازی صفحه بر اساس مرحله راهنما (Guide Step)
    LaunchedEffect(currentIndex) {
        currentScreen = when (currentIndex) {
            0 -> AppScreen.HOME
            1 -> AppScreen.SHIFTS
            else -> AppScreen.PROFILE
        }
    }

    // تعریف مراحل راهنمای چند صفحه‌ای با Live UI
    val steps = remember(targetRects.toMap()) {
        listOf(
            GuideStep(
                title = "صفحه خانه - جستجوی شیفت",
                description = "از این بخش می‌توانید شیفت‌های کاری دلخواه خود را پیدا کنید.",
                targetRect = targetRects["home_search_btn"],
                spotlights = listOf(
                    SpotlightShape(
                        offsetDp = DpOffset(0.dp, 0.dp),
                        shape = TargetShape.RoundedRect(
                            widthDp = 220.dp,
                            heightDp = 50.dp,
                            cornerRadiusDp = 14.dp
                        )
                    )
                )
            ),
            GuideStep(
                title = "صفحه شیفت‌ها - فیلتر هوشمند",
                description = "در این صفحه می‌توانید شیفت‌ها را بر اساس روز و ساعت فیلتر کنید.",
                targetRect = targetRects["shifts_filter_chip"],
                spotlights = listOf(
                    SpotlightShape(
                        offsetDp = DpOffset(0.dp, 0.dp),
                        shape = TargetShape.RoundedRect(
                            widthDp = 180.dp,
                            heightDp = 48.dp,
                            cornerRadiusDp = 12.dp
                        )
                    )
                )
            ),
            GuideStep(
                title = "صفحه پروفایل - بخش آموزش",
                description = "در صفحه پروفایل می‌توانید ویدیوهای آموزشی و راهنما را مشاهده کنید.",
                targetRect = targetRects["profile_education_card"],
                spotlights = listOf(
                    SpotlightShape(
                        offsetDp = DpOffset(0.dp, 0.dp),
                        shape = TargetShape.RoundedRect(
                            widthDp = 300.dp,
                            heightDp = 80.dp,
                            cornerRadiusDp = 20.dp
                        )
                    )
                )
            )
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // ۱. برنامه سه صفحه‌ای اصلی به همراه Bottom Navigation Bar
        Scaffold(
            bottomBar = {
                NavigationBar(containerColor = Color.White) {
                    NavigationBarItem(
                        selected = currentScreen == AppScreen.HOME,
                        onClick = {
                            currentScreen = AppScreen.HOME
                            currentIndex = 0
                        },
                        label = { Text("خانه") },
                        icon = { Text("🏠") }
                    )
                    NavigationBarItem(
                        selected = currentScreen == AppScreen.SHIFTS,
                        onClick = {
                            currentScreen = AppScreen.SHIFTS
                            currentIndex = 1
                        },
                        label = { Text("شیفت‌ها") },
                        icon = { Text("📋") }
                    )
                    NavigationBarItem(
                        selected = currentScreen == AppScreen.PROFILE,
                        onClick = {
                            currentScreen = AppScreen.PROFILE
                            currentIndex = 2
                        },
                        label = { Text("پروفایل") },
                        icon = { Text("👤") }
                    )
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (currentScreen) {
                    AppScreen.HOME -> SampleHomeScreen { rect ->
                        targetRects["home_search_btn"] = rect
                    }
                    AppScreen.SHIFTS -> SampleShiftsScreen { rect ->
                        targetRects["shifts_filter_chip"] = rect
                    }
                    AppScreen.PROFILE -> SampleProfileScreen { rect ->
                        targetRects["profile_education_card"] = rect
                    }
                }
            }
        }

        // ۲. اورلی راهنما (SpotlyGuideManager) روی تمام صفحات قرار می‌گیرد
        if (isGuideVisible) {
            SpotlyGuideManager(
                steps = steps,
                currentStepIndex = currentIndex,
                onNextStep = {
                    if (currentIndex < steps.lastIndex) {
                        currentIndex++
                    }
                },
                onPreviousStep = {
                    if (currentIndex > 0) {
                        currentIndex--
                    }
                },
                onSkipOrFinish = {
                    isGuideVisible = false
                },
                colors = SpotlyColors(
                    overlayColor = Color.Black.copy(alpha = 0.65f),
                    containerBackground = Color(0xFF3200EE),
                    titleColor = Color.White,
                    descriptionColor = Color(0xFFE0E0E0),
                    progressColor = Color.White,
                    buttonBackgroundColor = Color(0xFFFFEB3B),
                    buttonTextColor = Color(0xFF3200EE)
                ),
                bottomContent = { data ->
                    CustomSampleBottomContent(data = data)
                }
            )
        }
    }
}

// -------------------------------------------------------------
// ۳ صفحه نمونه با المان‌های زنده (Live UI Elements)
// -------------------------------------------------------------

@Composable
private fun SampleHomeScreen(onTargetPositioned: (Rect) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("صفحه ۱: خانه", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {},
            modifier = Modifier
                .height(50.dp)
                .width(220.dp)
                .onGloballyPositioned { coordinates ->
                    onTargetPositioned(coordinates.boundsInRoot())
                },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3200EE))
        ) {
            Text("جستجوی شیفت جدید", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun SampleShiftsScreen(onTargetPositioned: (Rect) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("صفحه ۲: شیفت‌ها", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {},
            modifier = Modifier
                .height(48.dp)
                .width(180.dp)
                .onGloballyPositioned { coordinates ->
                    onTargetPositioned(coordinates.boundsInRoot())
                },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00897B))
        ) {
            Text("فیلتر روز و ساعت", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun SampleProfileScreen(onTargetPositioned: (Rect) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("صفحه ۳: پروفایل", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(horizontal = 16.dp)
                .onGloballyPositioned { coordinates ->
                    onTargetPositioned(coordinates.boundsInRoot())
                },
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFF9800)),
            shape = RoundedCornerShape(20.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("ویدیوهای آموزش و راهنما", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun GreetingPreview() {
    SpotlyOnboardingLibraryTheme {
        SpotlyDemoScreen()
    }
}
