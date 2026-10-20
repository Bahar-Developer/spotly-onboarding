package com.smart.kahkashan.spotlyonboardinglibrary.sample

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spotly.onboarding.model.SpotlyBottomContentData

/**
 * A custom, stylish bottom content implemented completely inside the sample app module,
 * passed dynamically to SpotlyGuideManager.
 */
@Composable
fun CustomSampleBottomContent(
    data: SpotlyBottomContentData,
    modifier: Modifier = Modifier
) {
    val colors = data.colors

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.18f)
                .navigationBarsPadding(),
            shape = RectangleShape,
            colors = CardDefaults.cardColors(
                containerColor = colors.containerBackground
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    // Slim Progress Indicator
                    LinearProgressIndicator(
                        progress = { data.progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(CircleShape),
                        color = colors.progressColor,
                        trackColor = colors.progressColor.copy(alpha = 0.2f),
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Title
                    Text(
                        text = data.step.title,
                        color = colors.titleColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 24.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Description
                    Text(
                        text = data.step.description,
                        color = colors.descriptionColor,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        fontWeight = FontWeight.Normal
                    )
                }

                // Action Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Left Action (Previous or Skip)
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (!data.isFirstStep) {
                            TextButton(
                                onClick = data.onPrevious,
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = colors.titleColor.copy(alpha = 0.8f)
                                ),
                                contentPadding = PaddingValues(4.dp)
                            ) {
                                Text("قبلی", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                            }
                        } else if (!data.isLastStep) {
                            TextButton(
                                onClick = data.onSkipOrFinish,
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = colors.titleColor.copy(alpha = 0.7f)
                                ),
                                contentPadding = PaddingValues(4.dp)
                            ) {
                                Text("رد کردن", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }

                    // Primary Action (Next or Finish)
                    Button(
                        onClick = {
                            if (data.isLastStep) data.onSkipOrFinish() else data.onNext()
                        },
                        modifier = Modifier
                            .height(40.dp)
                            .defaultMinSize(minWidth = 110.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors.buttonBackgroundColor,
                            contentColor = colors.buttonTextColor
                        ),
                        shape = RoundedCornerShape(14.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        Text(
                            text = if (data.isLastStep) "پایان" else "بعدی",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}
