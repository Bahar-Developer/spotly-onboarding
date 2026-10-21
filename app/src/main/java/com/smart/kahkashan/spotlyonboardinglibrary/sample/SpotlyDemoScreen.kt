package com.smart.kahkashan.spotlyonboardinglibrary.sample

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smart.kahkashan.spotlyonboardinglibrary.R
import com.smart.kahkashan.spotlyonboardinglibrary.ui.theme.SpotlyOnboardingLibraryTheme
import com.spotly.onboarding.model.GuideStep
import com.spotly.onboarding.model.ProgressMode
import com.spotly.onboarding.model.SpotlightShape
import com.spotly.onboarding.model.TargetShape
import com.spotly.onboarding.theme.SpotlyColors
import com.spotly.onboarding.ui.SpotlyGuideManager

enum class AppScreen {
    DASHBOARD,
    SHIFTS,
    PROFILE
}

@Composable
fun SpotlyDemoScreen() {
    var currentScreen by remember { mutableStateOf(AppScreen.DASHBOARD) }
    val targetRects = remember { mutableStateMapOf<String, Rect>() }

    var isGuideVisible by remember { mutableStateOf(false) }
    var currentIndex by remember { mutableIntStateOf(0) }

    // تنظیمات تم (دارک مود) و زبان
    var isDarkMode by remember { mutableStateOf(false) }
    var isPersian by remember { mutableStateOf(true) }

    // تعریف استپ‌های چندگانه برای تب داشبورد
    val dashboardSteps = remember(targetRects.toMap()) {
        listOf(
            GuideStep(
                title = "نوار ناوبری بالا (نمای کلی)",
                description = "در این بخش می‌توانید به صفحه قبلی بازگردید یا عنوان صفحه را مشاهده کنید.",
                targetRect = targetRects["dashboard_header"]
            ),
            GuideStep(
                title = "کارت‌های آماری",
                description = "آمار کلیدی مانند بازدیدها، مراجعات، کاربران جدید و کاربران فعال به صورت دسته‌بندی شده نمایش داده می‌شوند.",
                targetRect = targetRects["dashboard_earnings_card"]
            ),
            GuideStep(
                title = "بخش ترافیک دستگاه‌ها",
                description = "نمودار میله‌ای ترافیک کاربران بر اساس پلتفرم‌های مختلف (لینوکس، اندروید، مک و ...).",
                targetRect = targetRects["dashboard_search_btn"]
            )
        )
    }

    // تعریف کامل استپ‌های شیفت (مطابق با طراحی انتخاب شیفت)
    val shiftsSteps = remember(targetRects.toMap()) {
        val centerX = 180.dp
        val fullWidth = 360.dp
        listOf(
            GuideStep(
                title = "فیلتر هوشمند شیفت‌ها",
                description = "در صفحه شیفت‌ها می‌توانید فیلترهای روز و ساعت را روی این بخش اعمال کنید.",
                targetRect = targetRects["shifts_filter_chip"]
            ),
            GuideStep(
                title = "کارت شیفت پیشنهادی",
                description = "اطلاعات کلی هر شیفت مانند ساعت کاری، وضعیت گارانتی و میانگین درآمد در این کارت قابل مشاهده است.",
                targetRect = targetRects["shifts_list_card"]
            ),
            GuideStep(
                title = "انتخاب محدودهٔ کاری",
                description = "ابتدا محدوده\u200Cای را که می\u200Cخواهید در آن فعالیت کنید، انتخاب کنید.",
                imageRes = R.drawable.img_shift_onboarding_step1,
                spotlights = listOf(SpotlightShape(offsetDp = DpOffset(x = 150.dp, y = 460.dp), shape = TargetShape.Circle(radiusDp = 190.dp)))
            ),
            GuideStep(
                title = "پیدا کردن شیفت مناسب",
                description = "روز، ساعت و فیلترهای دلخواه را مشخص کنید تا شیفت\u200Cهای مناسب همان روز نمایش داده شوند.",
                imageRes = R.drawable.img_shift_onboarding_step2,
                spotlights = listOf(SpotlightShape(offsetDp = DpOffset(x = centerX, y = 60.dp), shape = TargetShape.Circle(radiusDp = 220.dp)))
            ),
            GuideStep(
                title = "وضعیت های شیفت",
                description = "هر کارت، وضعیت و میزان ظرفیت شیفت را نمایش می\u200Cدهد تا بتوانید شرایط آن را پیش از انتخاب بررسی کنید.",
                imageRes = R.drawable.img_shift_onboarding_step345,
                spotlights = listOf(SpotlightShape(offsetDp = DpOffset(x = centerX, y = 298.dp), shape = TargetShape.RoundedRect(widthDp = fullWidth, heightDp = 211.dp, cornerRadiusDp = 0.dp))),
                groupId = "1",
                progressMode = ProgressMode.GROUP_STATIC,
                isAutoAdvance = true
            ),
            GuideStep(
                title = "وضعیت های شیفت",
                description = "هر کارت، وضعیت و میزان ظرفیت شیفت را نمایش می\u200Cدهد تا بتوانید شرایط آن را پیش از انتخاب بررسی کنید.",
                imageRes = R.drawable.img_shift_onboarding_step345,
                imageScrollYRatio = 0.1f,
                spotlights = listOf(SpotlightShape(offsetDp = DpOffset(x = centerX, y = 480.dp), shape = TargetShape.RoundedRect(widthDp = fullWidth, heightDp = 205.dp, cornerRadiusDp = 0.dp))),
                groupId = "1",
                progressMode = ProgressMode.GROUP_STATIC,
                isAutoAdvance = true
            ),
            GuideStep(
                title = "وضعیت های شیفت",
                description = "هر کارت، وضعیت و میزان ظرفیت شیفت را نمایش می\u200Cدهد تا بتوانید شرایط آن را پیش از انتخاب بررسی کنید.",
                imageRes = R.drawable.img_shift_onboarding_step345,
                imageScrollYRatio = 0.8f,
                spotlights = listOf(SpotlightShape(offsetDp = DpOffset(x = centerX, y = 630.dp), shape = TargetShape.RoundedRect(widthDp = fullWidth, heightDp = 205.dp, cornerRadiusDp = 0.dp))),
                groupId = "1",
                progressMode = ProgressMode.GROUP_STATIC,
                isAutoAdvance = true,
                autoAdvanceDurationMillis = 2000L
            ),
            GuideStep(
                title = "وضعیت های شیفت",
                description = "هر کارت، وضعیت و میزان ظرفیت شیفت را نمایش می\u200Cدهد تا بتوانید شرایط آن را پیش از انتخاب بررسی کنید.",
                imageRes = R.drawable.img_shift_onboarding_step345,
                imageScrollYRatio = 1.4f,
                spotlights = listOf(SpotlightShape(offsetDp = DpOffset(x = centerX, y = 780.dp), shape = TargetShape.RoundedRect(widthDp = fullWidth, heightDp = 170.dp, cornerRadiusDp = 0.dp))),
                groupId = "1",
                isAutoAdvance = true,
                isLooping = true,
                progressMode = ProgressMode.GROUP_STATIC
            ),
            GuideStep(
                title = "جزئیات و مزایای شیفت",
                description = "با انتخاب هر کارت، جزئیات شیفت را ببینید. نشان\u200Cهای «گارانتی» و «آب\u200Cوهوا» یعنی امکان درآمد بیشتر در آن شیفت",
                imageRes = R.drawable.img_shift_onboarding_step6,
                spotlights = listOf(SpotlightShape(offsetDp = DpOffset(x = centerX, y = 334.dp), shape = TargetShape.RoundedRect(widthDp = fullWidth, heightDp = 520.dp, cornerRadiusDp = 16.dp)))
            ),
            GuideStep(
                title = "شیفت انتخاب شده",
                description = "پس از انتخاب، وضعیت کارت به «انتخاب\u200Cشده» تغییر می\u200Cکند و شیفت به برنامه کاری شما اضافه می\u200Cشود",
                imageRes = R.drawable.img_shift_onboarding_step78,
                spotlights = listOf(SpotlightShape(offsetDp = DpOffset(x = centerX, y = 320.dp), shape = TargetShape.RoundedRect(widthDp = fullWidth, heightDp = 210.dp, cornerRadiusDp = 0.dp)))
            ),
            GuideStep(
                title = "بخش آموزش",
                description = " برای توضیحات کامل\u200Cتر و تماشای ویدیوهای مربوط به شیفت هم به پروفایل بخش «آموزش» بروید.",
                imageRes = R.drawable.img_profile_onboarding_step9,
                spotlights = listOf(SpotlightShape(offsetDp = DpOffset(x = centerX, y = 450.dp), shape = TargetShape.RoundedRect(widthDp = fullWidth, heightDp = 67.dp, cornerRadiusDp = 999.dp)))
            )
        )
    }

    // تعریف استپ‌های چندگانه برای تب پروفایل مطابق با طراحی جدید
    val profileSteps = remember(targetRects.toMap()) {
        listOf(
            GuideStep(
                title = "مشخصات حساب کاربری",
                description = "در این بخش نام و ایمیل سفیر نمایش داده می‌شود.",
                targetRect = targetRects["profile_user_card"]
            ),
            GuideStep(
                title = "گواهینامه و مدارک",
                description = "مدارک و گواهینامه‌های تایید شده شما در این بخش قابل مشاهده است.",
                targetRect = targetRects["profile_certificate_card"]
            ),
            GuideStep(
                title = "اطلاعات پرداخت",
                description = "اطلاعات حساب بانکی و تراکنش‌ها را مدیریت کنید.",
                targetRect = targetRects["profile_payment_card"]
            )
        )
    }

    val currentSteps = when (currentScreen) {
        AppScreen.DASHBOARD -> dashboardSteps
        AppScreen.SHIFTS -> shiftsSteps
        AppScreen.PROFILE -> profileSteps
    }

    SpotlyOnboardingLibraryTheme(darkTheme = isDarkMode) {
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                bottomBar = {
                    NavigationBar(
                        containerColor = if (isDarkMode) Color(0xFF1E1E1E) else Color.White
                    ) {
                        NavigationBarItem(
                            selected = currentScreen == AppScreen.DASHBOARD,
                            onClick = {
                                currentScreen = AppScreen.DASHBOARD
                                isGuideVisible = false
                            },
                            label = { Text("داشبورد") },
                            icon = { Text("📊") }
                        )
                        NavigationBarItem(
                            selected = currentScreen == AppScreen.SHIFTS,
                            onClick = {
                                currentScreen = AppScreen.SHIFTS
                                isGuideVisible = false
                            },
                            label = { Text("شیفت‌ها") },
                            icon = { Text("📋") }
                        )
                        NavigationBarItem(
                            selected = currentScreen == AppScreen.PROFILE,
                            onClick = {
                                currentScreen = AppScreen.PROFILE
                                isGuideVisible = false
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
                        AppScreen.DASHBOARD -> DashboardScreen(
                            onStartGuide = {
                                currentIndex = 0
                                isGuideVisible = true
                            },
                            onTargetPositioned = { key, rect ->
                                targetRects[key] = rect
                            }
                        )
                        AppScreen.SHIFTS -> ShiftsScreen(
                            onStartGuide = {
                                currentIndex = 0
                                isGuideVisible = true
                            },
                            onTargetPositioned = { key, rect ->
                                targetRects[key] = rect
                            }
                        )
                        AppScreen.PROFILE -> ProfileScreen(
                            onStartGuide = {
                                currentIndex = 0
                                isGuideVisible = true
                            },
                            onTargetPositioned = { key, rect ->
                                targetRects[key] = rect
                            }
                        )
                    }
                }
            }

            if (isGuideVisible) {
                SpotlyGuideManager(
                    steps = currentSteps,
                    currentStepIndex = currentIndex,
                    onNextStep = {
                        if (currentIndex < currentSteps.lastIndex) {
                            currentIndex++
                        } else {
                            isGuideVisible = false
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
}

// -------------------------------------------------------------
// تب ۱: داشبورد (کاملاً فارسی)
// -------------------------------------------------------------
@Composable
private fun DashboardScreen(
    onStartGuide: () -> Unit,
    onTargetPositioned: (String, Rect) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    onTargetPositioned("dashboard_header", coordinates.boundsInRoot())
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "‹", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2563EB))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "خانه", fontSize = 15.sp, fontWeight = FontWeight.Medium, color = Color(0xFF2563EB))
            }
            Text(
                text = "نمای کلی",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Button(
                onClick = onStartGuide,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text("💡 راهنما", color = Color.White, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    onTargetPositioned("dashboard_earnings_card", coordinates.boundsInRoot())
                },
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "بازدیدها",
                    value = "7,265",
                    change = "+11.01%",
                    isPositive = true,
                    backgroundColor = Color(0xFF2563EB),
                    textColor = Color.White
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "مراجعات",
                    value = "3,671",
                    change = "-0.03%",
                    isPositive = false,
                    backgroundColor = Color(0xFF1E293B),
                    textColor = Color.White
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "کاربران جدید",
                    value = "256",
                    change = "+15.03%",
                    isPositive = true,
                    backgroundColor = Color(0xFF1E293B),
                    textColor = Color.White
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "کاربران فعال",
                    value = "2,318",
                    change = "+6.08%",
                    isPositive = true,
                    backgroundColor = Color(0xFF2563EB),
                    textColor = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("کاربران", fontWeight = FontWeight.Bold, color = Color(0xFF9333EA))
                    Text("پروژه‌ها", color = Color.Gray)
                    Text("وضعیت سیستم", color = Color.Gray)
                }
                Spacer(modifier = Modifier.height(16.dp))
                UsersLineChart()
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    onTargetPositioned("dashboard_search_btn", coordinates.boundsInRoot())
                },
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "ترافیک دستگاه‌ها",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF2563EB)
                )
                Spacer(modifier = Modifier.height(16.dp))
                DeviceTrafficBarChart()
            }
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    change: String,
    isPositive: Boolean,
    backgroundColor: Color,
    textColor: Color
) {
    Card(
        modifier = modifier.height(110.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = title, color = textColor.copy(alpha = 0.8f), fontSize = 13.sp, fontWeight = FontWeight.Medium)
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(if (isPositive) "↗" else "↘", color = textColor, fontSize = 14.sp)
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(text = value, color = textColor, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(text = change, color = if (isPositive) Color(0xFF4ADE80) else Color(0xFFF87171), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun UsersLineChart() {
    val months = listOf("فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور")
    val points = listOf(0.4f, 0.2f, 0.35f, 0.7f, 0.6f, 0.8f)

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val height = size.height
                val stepX = width / (points.size - 1)

                val path = Path()
                points.forEachIndexed { index, fl ->
                    val x = index * stepX
                    val y = height * (1f - fl)
                    if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
                }

                drawPath(
                    path = path,
                    color = Color(0xFFC084FC),
                    style = Stroke(width = 3.dp.toPx())
                )

                points.forEachIndexed { index, fl ->
                    val x = index * stepX
                    val y = height * (1f - fl)
                    drawCircle(color = Color.White, radius = 6.dp.toPx(), center = Offset(x, y))
                    drawCircle(color = Color(0xFF9333EA), radius = 4.dp.toPx(), center = Offset(x, y))
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            months.forEach { month ->
                Text(text = month, fontSize = 11.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
private fun DeviceTrafficBarChart() {
    val devices = listOf("لینوکس", "مک", "آی‌اواس", "ویندوز", "اندروید", "سایر")
    val heights = listOf(0.3f, 0.7f, 0.4f, 0.9f, 0.8f, 0.25f)

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.Bottom
            ) {
                heights.forEachIndexed { index, h ->
                    val isAndroid = devices[index] == "اندروید"
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        if (isAndroid) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.Black,
                                modifier = Modifier.padding(bottom = 6.dp)
                            ) {
                                Text(
                                    text = "243K",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .width(32.dp)
                                .fillMaxHeight(h)
                                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                                .background(if (isAndroid) Color(0xFF3B82F6) else Color(0xFFE2E8F0))
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            devices.forEach { dev ->
                Text(text = dev, fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
            }
        }
    }
}

// -------------------------------------------------------------
// تب ۲: شیفت‌ها (مطابق با طرح انتخاب شیفت ارسالی)
// -------------------------------------------------------------
@Composable
private fun ShiftsScreen(
    onStartGuide: () -> Unit,
    onTargetPositioned: (String, Rect) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top
    ) {
        // هدر بالای صفحه
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    onTargetPositioned("shifts_filter_chip", coordinates.boundsInRoot())
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "انتخاب شیفت",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = onStartGuide,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text("💡 راهنما", color = Color.White, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F5F9)),
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text("📅 برنامه کاری", color = Color.DarkGray, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ردیف تاریخ‌ها (Date Selector Row)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // روز انتخاب شده (امروز / یک‌شنبه ۹ آذر)
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                modifier = Modifier.width(80.dp).height(80.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFF334155)) {
                        Text("امروز", color = Color.White, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("یک‌شنبه", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("۹ آذر", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp)
                }
            }
            // سایر روزها
            val days = listOf("دوشنبه" to "۱۰ آذر", "سه‌شنبه" to "۱۱ آذر", "چهارشنبه" to "۱۲ آذر", "پنج‌شنبه" to "۱۳ آذر")
            days.forEach { (day, date) ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.width(75.dp).height(80.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(day, color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(date, color = Color.DarkGray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // تب‌ها (همه شیفت‌ها، شیفت‌های پردرآمد)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("همه شیفت‌ها", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF2563EB))
                Spacer(modifier = Modifier.height(4.dp))
                Box(modifier = Modifier.width(60.dp).height(3.dp).background(Color(0xFF2563EB), RoundedCornerShape(2.dp)))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("شیفت‌های پردرآمد", fontWeight = FontWeight.Medium, fontSize = 15.sp, color = Color.Gray)
                Spacer(modifier = Modifier.width(6.dp))
                Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFFEF4444)) {
                    Text("جدید", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // فیلتر چیپ‌ها
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFFEFF6FF),
                border = BorderStroke(1.dp, Color(0xFF3B82F6))
            ) {
                Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("✕", color = Color(0xFF3B82F6), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("تجریش، چیذر، دزاشیب", color = Color(0xFF2563EB), fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }
            }
            val filters = listOf("زمان شیفت", "دارای پاداش", "منطقه ۱", "ویژه")
            filters.forEach { f ->
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Text(f, color = Color.DarkGray, fontSize = 13.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // کارت شیفت ۱ (هدف گاید کارت لیست)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    onTargetPositioned("shifts_list_card", coordinates.boundsInRoot())
                },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "تجریش، چیذر، دزاشیب", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.DarkGray)
                    Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFFF1F5F9)) {
                        Text("۰۷:۰۰ - ۰۹:۰۰", color = Color.DarkGray, fontSize = 11.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Surface(shape = RoundedCornerShape(10.dp), color = Color(0xFFDCFCE7)) {
                    Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("🎁", fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("۴۰%+ درآمد بیشتر", color = Color(0xFF166534), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "میانگین درآمد از سفارش: ۲۵,۰۰۰ تومان", fontSize = 12.sp, color = Color.Gray)
                }
                Spacer(modifier = Modifier.height(14.dp))
                OutlinedButton(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth().height(44.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFF2563EB))
                ) {
                    Text("🔔 ورود به صف انتظار", color = Color(0xFF2563EB), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // کارت شیفت ۲
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "تجریش، چیذر، دزاشیب", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.DarkGray)
                    Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFFF1F5F9)) {
                        Text("۱۵:۰۰ - ۱۹:۰۰", color = Color.DarkGray, fontSize = 11.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(shape = RoundedCornerShape(10.dp), color = Color(0xFFEFF6FF)) {
                        Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text("🛡️", fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("گارانتی: ۱۲۰,۰۰۰ تومان", color = Color(0xFF1D4ED8), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Surface(shape = RoundedCornerShape(10.dp), color = Color(0xFFE0F2FE)) {
                        Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text("🌧️", fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("آب و هوا", color = Color(0xFF0369A1), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "میانگین درآمد از سفارش: ۲۵,۰۰۰ تومان", fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(14.dp))
                Button(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth().height(44.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
                ) {
                    Text("📅 انتخاب شیفت", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
}

// -------------------------------------------------------------
// تب ۳: پروفایل (کاملاً فارسی)
// -------------------------------------------------------------
@Composable
private fun ProfileScreen(
    onStartGuide: () -> Unit,
    onTargetPositioned: (String, Rect) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2E236C))
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("‹", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                Text(
                    text = "پروفایل",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Button(
                    onClick = onStartGuide,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f)),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text("💡 راهنما", color = Color.White, fontSize = 12.sp)
                }
            }
        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height((40.dp)))
                Box(
                    modifier = Modifier
                        .offset(y = (-60.dp))
                        .size(100.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(Color(0xFFE2E8F0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("👨‍💼", fontSize = 44.sp)
                    }
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF3200EE)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("✏️", fontSize = 12.sp)
                        }
                    }
                }



                Text(
                    text = "بهار رضایی",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.onGloballyPositioned { coordinates ->
                        onTargetPositioned("profile_user_card", coordinates.boundsInRoot())
                    }
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "nothing@gmail.com",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )

                Spacer(modifier = Modifier.height(24.dp))

                ProfileMenuItem(
                    icon = "👤",
                    iconBg = Color(0xFFFFE4E6),
                    title = "اطلاعات کاربری",
                    onClick = {}
                )
                Spacer(modifier = Modifier.height(12.dp))
                ProfileMenuItem(
                    icon = "🎓",
                    iconBg = Color(0xFFE0E7FF),
                    title = "گواهینامه و مدارک",
                    modifier = Modifier.onGloballyPositioned { coordinates ->
                        onTargetPositioned("profile_certificate_card", coordinates.boundsInRoot())
                    },
                    onClick = {}
                )
                Spacer(modifier = Modifier.height(12.dp))
                ProfileMenuItem(
                    icon = "💳",
                    iconBg = Color(0xFFFFEDD5),
                    title = "اطلاعات پرداخت",
                    modifier = Modifier.onGloballyPositioned { coordinates ->
                        onTargetPositioned("profile_payment_card", coordinates.boundsInRoot())
                    },
                    onClick = {}
                )
                Spacer(modifier = Modifier.height(12.dp))
                ProfileMenuItem(
                    icon = "📄",
                    iconBg = Color(0xFFF3E8FF),
                    title = "اسناد و مدارک",
                    onClick = {}
                )
                Spacer(modifier = Modifier.height(12.dp))
                ProfileMenuItem(
                    icon = "🚪",
                    iconBg = Color(0xFFFEE2E2),
                    title = "خروج از حساب",
                    onClick = {}
                )
            }
        }
    }
}

@Composable
private fun ProfileMenuItem(
    icon: String,
    iconBg: Color,
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(iconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Text(icon, fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Text(
                text = "›",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SpotlyOnboardingLibraryTheme {
        SpotlyDemoScreen()
    }
}
