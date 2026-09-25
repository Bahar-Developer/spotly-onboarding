package com.spotly.onboarding.ui


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.spotly.onboarding.theme.SpotlyColors

@Composable
internal fun SpotlyBottomContent(
    title: String,
    description: String,
    progress: Float,
    isLastStep: Boolean,
    isFirstStep: Boolean,
    colors: SpotlyColors,
    nextText: String = "بعدی",
    previousText: String = "قبلی",
    skipText: String = "رد کردن راهنما",
    finishText: String = "پایان راهنما",
    onNext: () -> Unit,
    onSkipOrFinish: () -> Unit,
    onPreviousStep: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            SpotlyProgressIndicator(
                progress = progress,
                color = colors.progressColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .zIndex(1f)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.containerBackground)
                    .navigationBarsPadding()
                    .padding(16.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.titleColor
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = colors.descriptionColor
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (!isFirstStep) {
                        TextButton(onClick = onPreviousStep) {
                            Text(text = previousText, color = colors.titleColor)
                        }
                    } else if (!isLastStep) {
                        TextButton(onClick = onSkipOrFinish) {
                            Text(text = skipText, color = colors.titleColor)
                        }
                    } else {
                        Spacer(modifier = Modifier.width(1.dp))
                    }

                    Button(
                        onClick = {
                            if (isLastStep) onSkipOrFinish() else onNext()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors.buttonBackgroundColor,
                            contentColor = colors.buttonTextColor
                        ),
                        shape = CircleShape
                    ) {
                        Text(
                            text = if (isLastStep) finishText else nextText,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SpotlyProgressIndicator(
    progress: Float,
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.height(4.dp)) {
        val clampedProgress = progress.coerceIn(0f, 1f)
        val progressWidth = size.width * clampedProgress

        if (progressWidth <= 0f) return@Canvas

        drawRoundRect(
            color = color,
            topLeft = Offset(x = size.width - progressWidth, y = 0f),
            size = Size(width = progressWidth, height = size.height),
            cornerRadius = CornerRadius(x = size.height / 2f, y = size.height / 2f)
        )
    }
}