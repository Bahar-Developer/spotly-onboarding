package com.smart.kahkashan.spotlyonboardinglibrary.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
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
                title = if (isPersian) "سلام و اطلاعات کاربری" else "Greeting & Account Info",
                description = if (isPersian) "در این بخش نام سفیر و منطقه فعالیت شما نمایش داده می‌شود." else "Shows your ambassador name and active zone.",
                targetRect = targetRects["dashboard_header"]
            ),
            GuideStep(
                title = if (isPersian) "کارت مجموع درآمد امروز" else "Today's Earnings Card",
                description = if (isPersian) "مجموع درآمد کسب شده در شیفت‌های امروز را به‌صورت لحظه‌ای بررسی کنید." else "Check your total earnings in real-time.",
                targetRect = targetRects["dashboard_earnings_card"]
            ),
            GuideStep(
                title = if (isPersian) "دکمه جستجوی شیفت جدید" else "Search New Shifts Button",
                description = if (isPersian) "با یک کلیک وارد بخش شیفت‌ها شوید و شیفت دلخواه خود را رزرو کنید." else "Quickly search and book shifts.",
                targetRect = targetRects["dashboard_search_btn"]
            )
        )
    }

    // تعریف کامل استپ‌های شیفت (شامل لایو UI و تمامی استپ‌های تصویری و چرخشی، از جمله بخش آموزش)
    val shiftsSteps = remember(targetRects.toMap()) {
        val centerX = 180.dp
        val fullWidth = 360.dp
        listOf(
            GuideStep(
                title = if (isPersian) "فیلتر هوشمند شیفت‌ها" else "Smart Shift Filter",
                description = if (isPersian) "در صفحه شیفت‌ها می‌توانید فیلترهای روز و ساعت را روی این دکمه اعمال کنید." else "Filter shifts by day and time.",
                targetRect = targetRects["shifts_filter_chip"]
            ),
            GuideStep(
                title = "انتخاب محدودهٔ کاری",
                description = "ابتدا محدوده\u200Cای را که می\u200Cخواهید در آن فعالیت کنید، انتخاب کنید.",
                trackingTag = "shift-onboarding-polygon_page-view" ,
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

    // تعریف استپ‌های چندگانه برای تب پروفایل
    val profileSteps = remember(targetRects.toMap()) {
        listOf(
            GuideStep(
                title = if (isPersian) "مشخصات حساب کاربری" else "User Account Info",
                description = if (isPersian) "در این بخش نام و سمت سفیر نمایش داده می‌شود." else "Shows your ambassador profile info.",
                targetRect = targetRects["profile_user_card"]
            ),
            GuideStep(
                title = "مرکز آموزش و ویدیوها",
                description = "این مرحله روی کارت واقعی آموزش در صفحه پروفایل قرار دارد.",
                targetRect = targetRects["profile_education_card"]
            ),
            GuideStep(
                title = if (isPersian) "تنظیمات تم و زبان" else "Theme & Language Settings",
                description = if (isPersian) "می‌توانید حالت تاریک (Dark Mode) یا زبان برنامه را تغییر دهید." else "Switch between dark mode and languages.",
                targetRect = targetRects["profile_settings_card"]
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
                            label = { Text(if (isPersian) "داشبورد" else "Dashboard") },
                            icon = { Text("📊") }
                        )
                        NavigationBarItem(
                            selected = currentScreen == AppScreen.SHIFTS,
                            onClick = {
                                currentScreen = AppScreen.SHIFTS
                                isGuideVisible = false
                            },
                            label = { Text(if (isPersian) "شیفت‌ها" else "Shifts") },
                            icon = { Text("📋") }
                        )
                        NavigationBarItem(
                            selected = currentScreen == AppScreen.PROFILE,
                            onClick = {
                                currentScreen = AppScreen.PROFILE
                                isGuideVisible = false
                            },
                            label = { Text(if (isPersian) "پروفایل" else "Profile") },
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
                            isPersian = isPersian,
                            onStartGuide = {
                                currentIndex = 0
                                isGuideVisible = true
                            },
                            onTargetPositioned = { key, rect ->
                                targetRects[key] = rect
                            }
                        )
                        AppScreen.SHIFTS -> ShiftsScreen(
                            isPersian = isPersian,
                            onStartGuide = {
                                currentIndex = 0
                                isGuideVisible = true
                            },
                            onTargetPositioned = { key, rect ->
                                targetRects[key] = rect
                            }
                        )
                        AppScreen.PROFILE -> ProfileScreen(
                            isDarkMode = isDarkMode,
                            onDarkModeChange = { isDarkMode = it },
                            isPersian = isPersian,
                            onLanguageChange = { isPersian = it },
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
// تب ۱: داشبورد حرفه‌ای و شیک با چند گاید لایو UI هم‌اندازه
// -------------------------------------------------------------
@Composable
private fun DashboardScreen(
    isPersian: Boolean,
    onStartGuide: () -> Unit,
    onTargetPositioned: (String, Rect) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        // هدر بالای صفحه (هدف گاید اول)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    onTargetPositioned("dashboard_header", coordinates.boundsInRoot())
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = if (isPersian) "سلام، بهار عزیز 👋" else "Hello, Ali 👋",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = if (isPersian) "منطقه فعالیت: تهرانپارس" else "Zone: Tehranpars",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }
            Button(
                onClick = onStartGuide,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3200EE)),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(if (isPersian) "💡 راهنما" else "💡 Guide", color = Color.White, fontSize = 13.sp)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // کارت آمار درآمد (هدف گاید دوم)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    onTargetPositioned("dashboard_earnings_card", coordinates.boundsInRoot())
                },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF3200EE).copy(alpha = 0.08f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (isPersian) "مجموع درآمد امروز" else "Today's Earnings",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "۱,۲۵۰,۰۰۰ تومان",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF3200EE)
                    )
                }
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF3200EE)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("💰", fontSize = 24.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = if (isPersian) "عملیات سریع" else "Quick Actions",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(12.dp))

        // دکمه جستجو (هدف گاید سوم)
        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .onGloballyPositioned { coordinates ->
                    onTargetPositioned("dashboard_search_btn", coordinates.boundsInRoot())
                },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3200EE)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = if (isPersian) "🔍 جستجوی شیفت جدید" else "🔍 Search New Shifts",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

// -------------------------------------------------------------
// تب ۲: شیفت‌ها با گاید لایو UI هم‌اندازه کامپوننت‌ها
// -------------------------------------------------------------
@Composable
private fun ShiftsScreen(
    isPersian: Boolean,
    onStartGuide: () -> Unit,
    onTargetPositioned: (String, Rect) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isPersian) "شیفت‌های کاری" else "Work Shifts",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Button(
                onClick = onStartGuide,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00897B)),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(if (isPersian) "💡 راهنما" else "💡 Guide", color = Color.White, fontSize = 13.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .onGloballyPositioned { coordinates ->
                    onTargetPositioned("shifts_filter_chip", coordinates.boundsInRoot())
                },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00897B)),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                text = if (isPersian) "⚙️ فیلتر روز، ساعت و محدوده" else "⚙️ Filter Date & Time",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = if (isPersian) "شیفت‌های پیشنهادی" else "Suggested Shifts",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(12.dp))

        // کارت شیفت اول (هدف گاید دوم شیفت‌ها)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    onTargetPositioned("shifts_list_card", coordinates.boundsInRoot())
                },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "شیفت عصر تهرانپارس", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Badge(containerColor = Color(0xFF00897B)) {
                        Text("گارانتی دار", color = Color.White, modifier = Modifier.padding(4.dp))
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "ساعت: ۱۶:۰۰ الی ۲۱:۰۰ • ظرفیت: محدود", fontSize = 13.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))
            }
        }
    }
}

// -------------------------------------------------------------
// تب ۳: پروفایل با گاید لایو UI هم‌اندازه کامپوننت‌ها
// -------------------------------------------------------------
@Composable
private fun ProfileScreen(
    isDarkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    isPersian: Boolean,
    onLanguageChange: (Boolean) -> Unit,
    onStartGuide: () -> Unit,
    onTargetPositioned: (String, Rect) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isPersian) "پروفایل کاربری" else "User Profile",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Button(
                onClick = onStartGuide,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(if (isPersian) "💡 راهنما" else "💡 Guide", color = Color.White, fontSize = 13.sp)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // کارت اطلاعات کاربر (هدف گاید اول پروفایل)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    onTargetPositioned("profile_user_card", coordinates.boundsInRoot())
                },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF3200EE)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("👤", fontSize = 28.sp)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = if (isPersian) "بهار رضایی" else "Ali Rezaei",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isPersian) "سفیر برتر منطقه تهران" else "Top Ambassador",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // کارت آموزش (هدف گاید دوم پروفایل)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(84.dp)
                .onGloballyPositioned { coordinates ->
                    onTargetPositioned("profile_education_card", coordinates.boundsInRoot())
                },
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFF9800)),
            shape = RoundedCornerShape(20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = if (isPersian) "مرکز آموزش و ویدیوها" else "Tutorial Center",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = if (isPersian) "نکات مهم افزایش درآمد و شیفت‌ها" else "Tips to increase earnings",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp
                    )
                }
                Text("📺", fontSize = 28.sp)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // کارت تنظیمات (هدف گاید سوم پروفایل)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    onTargetPositioned("profile_settings_card", coordinates.boundsInRoot())
                },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDarkMode) Color(0xFF2D2D2D) else Color(0xFFF8F9FA)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isPersian) "حالت تاریک (Dark Mode)" else "Dark Mode",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Medium
                    )
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = onDarkModeChange
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isPersian) "زبان فارسی (Persian)" else "Language: English",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Medium
                    )
                    Switch(
                        checked = isPersian,
                        onCheckedChange = onLanguageChange
                    )
                }
            }
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
