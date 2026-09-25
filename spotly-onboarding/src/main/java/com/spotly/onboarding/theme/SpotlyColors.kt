package com.spotly.onboarding.theme

import androidx.compose.ui.graphics.Color

data class SpotlyColors(
    val overlayColor: Color = Color.Black.copy(alpha = 0.65f),
    val containerBackground: Color = Color(0xFF1E1E1E),
    val titleColor: Color = Color.White,
    val descriptionColor: Color = Color(0xFFE0E0E0),
    val progressColor: Color = Color.White,
    val buttonBackgroundColor: Color = Color.White,
    val buttonTextColor: Color = Color.Black
)