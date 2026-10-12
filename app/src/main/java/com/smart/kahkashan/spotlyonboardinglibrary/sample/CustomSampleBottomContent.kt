package com.smart.kahkashan.spotlyonboardinglibrary.sample

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smart.kahkashan.spotlyonboardinglibrary.R
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
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 40.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(
                containerColor = colors.containerBackground.copy(alpha = 0.98f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Column(
                modifier = Modifier.padding(28.dp)
            ) {
                // Slim Progress Indicator
                LinearProgressIndicator(
                    progress = { data.progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(CircleShape),
                    color = colors.progressColor,
                    trackColor = colors.progressColor.copy(alpha = 0.15f),
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Title
                Text(
                    text = data.step.title,
                    color = colors.titleColor,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    lineHeight = 30.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Description
                Text(
                    text = data.step.description,
                    color = colors.descriptionColor.copy(alpha = 0.85f),
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Action Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Left Action (Previous or Skip)
                    Box(modifier = Modifier.weight(1f)) {
                        if (!data.isFirstStep) {
                            TextButton(
                                onClick = data.onPrevious,
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = colors.titleColor.copy(alpha = 0.7f)
                                )
                            ) {
                                Text("قبلی", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                            }
                        } else if (!data.isLastStep) {
                            TextButton(
                                onClick = data.onSkipOrFinish,
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = colors.titleColor.copy(alpha = 0.5f)
                                )
                            ) {
                                Text("رد کردن", fontSize = 15.sp, fontWeight = FontWeight.Normal)
                            }
                        }
                    }

                    // Primary Action (Next or Finish)
                    Button(
                        onClick = {
                            if (data.isLastStep) data.onSkipOrFinish() else data.onNext()
                        },
                        modifier = Modifier.height(52.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors.buttonBackgroundColor,
                            contentColor = colors.buttonTextColor
                        ),
                        shape = RoundedCornerShape(18.dp),
                        contentPadding = PaddingValues(horizontal = 24.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = if (data.isLastStep) "شروع کنیم" else "بعدی",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            if (!data.isLastStep) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    painter = painterResource(R.drawable.ic_launcher_foreground) ,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
