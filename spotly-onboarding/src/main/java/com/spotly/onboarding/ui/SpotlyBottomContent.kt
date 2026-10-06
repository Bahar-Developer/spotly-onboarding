package com.golrang.zap.zapdriver.core.guid

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
import androidx.compose.ui.zIndex
import androidx.compose.ui.unit.dp
import com.spotly.onboarding.theme.SpotlyColors

@Composable
internal fun SpotlyBottomContent(
    title: String,
    description: String,
    progress: Float,
    isFirstStep: Boolean,
    isLastStep: Boolean,
    showNavigationButtons: Boolean,
    onNext: () -> Unit,
    onSkipOrFinish: () -> Unit,
    onPreviousStep: () -> Unit,
    colors: SpotlyColors
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {

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
                    .background(
                        color = colors.containerBackground
                    )
                    .navigationBarsPadding()
                    .padding(16.dp)
            ) {

                Text(
                    text = title,
                    color = colors.titleColor
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = description,
                    color = colors.descriptionColor
                )

                if (showNavigationButtons) {

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        if (!isFirstStep) {

                            TextButton(
                                onClick = onPreviousStep
                            ) {
                                Text(
                                    text = "قبلی",
                                    color = colors.buttonBackgroundColor
                                )
                            }

                        } else if (!isLastStep) {

                            TextButton(
                                onClick = onSkipOrFinish
                            ) {
                                Text(
                                    text = "رد کردن راهنما",
                                    color = colors.buttonBackgroundColor
                                )
                            }

                        } else {

                            Spacer(
                                modifier = Modifier.width(1.dp)
                            )
                        }

                        Button(
                            modifier = Modifier.width(120.dp),
                            onClick = {
                                if (isLastStep) {
                                    onSkipOrFinish()
                                } else {
                                    onNext()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor =
                                    colors.buttonBackgroundColor,
                                contentColor =
                                    colors.buttonTextColor
                            )
                        ) {
                            Text(
                                text = if (isLastStep) {
                                    "پایان راهنما"
                                } else {
                                    "بعدی"
                                }
                            )
                        }
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
    Canvas(
        modifier = modifier.height(4.dp)
    ) {

        val clampedProgress =
            progress.coerceIn(0f, 1f)

        val progressWidth =
            size.width * clampedProgress

        if (progressWidth <= 0f) {
            return@Canvas
        }

        drawRoundRect(
            color = color,
            topLeft = Offset(
                x = size.width - progressWidth,
                y = 0f
            ),
            size = Size(
                width = progressWidth,
                height = size.height
            ),
            cornerRadius = CornerRadius(
                x = size.height / 2f,
                y = size.height / 2f
            )
        )
    }
}