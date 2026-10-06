package com.golrang.zap.zapdriver.core.guid

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.spotly.onboarding.model.GuideStep
import com.spotly.onboarding.model.SpotlightShape
import com.spotly.onboarding.model.TargetShape
import com.spotly.onboarding.theme.SpotlyColors
import com.spotly.onboarding.ui.MorphingMultiSpotlightOverlay

import kotlinx.coroutines.delay


@Composable
fun SpotlyGuideManager(
    steps: List<GuideStep>,
    currentStepIndex: Int,
    onNextStep: () -> Unit,
    onSkipOrFinish: () -> Unit,
    onPreviousStep: () -> Unit,
    modifier: Modifier = Modifier,
    colors: SpotlyColors = SpotlyColors()
) {

    if (
        steps.isEmpty() ||
        currentStepIndex !in steps.indices
    ) {
        return
    }

    val currentStep =
        steps[currentStepIndex]

    val isFirstStep =
        currentStepIndex == 0

    val isLastStep =
        currentStepIndex == steps.lastIndex

    val isAutoAdvanceStep =
        currentStep.isAutoAdvance

    /*
     * Automatic navigation
     *
     * Every time the current step changes,
     * this effect is cancelled and restarted.
     */
    LaunchedEffect(
        currentStepIndex,
        currentStep.isAutoAdvance,
        currentStep.autoAdvanceDurationMillis
    ) {

        if (!currentStep.isAutoAdvance) {
            return@LaunchedEffect
        }

        delay(
            currentStep.autoAdvanceDurationMillis
                .coerceAtLeast(1L)
        )

        if (isLastStep) {
            onSkipOrFinish()
        } else {
            onNextStep()
        }
    }

    val scrollState =
        rememberScrollState()

    val density =
        LocalDensity.current

    var imageSize by remember {
        mutableStateOf(
            IntSize.Zero
        )
    }

    /*
     * Scroll the onboarding image
     * according to the current step.
     */
    LaunchedEffect(
        currentStepIndex,
        currentStep.imageScrollYRatio
    ) {

        val targetScrollPx =
            (
                    scrollState.maxValue *
                            currentStep.imageScrollYRatio
                                .coerceIn(0f, 1f)
                    ).toInt()

        scrollState.animateScrollTo(
            value = targetScrollPx.coerceIn(
                0,
                scrollState.maxValue
            ),
            animationSpec = spring(
                stiffness = Spring.StiffnessLow
            )
        )
    }

    val calculatedSpotlights =
        remember(
            currentStep.spotlights,
            imageSize,
            scrollState.value,
            density
        ) {

            if (imageSize.width == 0) {
                currentStep.spotlights
            } else {

                val baseWidthPx =
                    with(density) {
                        360.dp.toPx()
                    }

                val scale =
                    imageSize.width.toFloat() /
                            baseWidthPx

                val scrollYPx =
                    scrollState.value.toFloat()

                currentStep.spotlights.map { item ->

                    val xPx =
                        with(density) {
                            item.offsetDp.x.toPx()
                        } * scale

                    val yPx =
                        (
                                with(density) {
                                    item.offsetDp.y.toPx()
                                } * scale
                                ) - scrollYPx

                    SpotlightShape(
                        offsetDp = DpOffset(
                            x = with(density) {
                                xPx.toDp()
                            },
                            y = with(density) {
                                yPx.toDp()
                            }
                        ),
                        shape = scaleTargetShape(
                            shape = item.shape,
                            scale = scale,
                            density = density
                        )
                    )
                }
            }
        }

    Box(
        modifier = modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
                .verticalScroll(
                    state = scrollState,
                    enabled = false
                )
        ) {

           CustomImage(
                id = currentStep.imageRes,
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        imageSize =
                            coordinates.size
                    },
                contentScale =
                    ContentScale.FillWidth
            )
        }

        MorphingMultiSpotlightOverlay(
            spotlights =
                calculatedSpotlights,
            overlayColor =
                colors.overlayColor,
            modifier =
                Modifier.fillMaxSize()
        )

        SpotlyBottomContent(
            title =
                currentStep.title,

            description =
                currentStep.description,

            progress =
                (currentStepIndex + 1f) /
                        steps.size,

            isFirstStep =
                isFirstStep,

            isLastStep =
                isLastStep,

            showNavigationButtons =
                !isAutoAdvanceStep,

            onNext =
                onNextStep,

            onSkipOrFinish =
                onSkipOrFinish,

            onPreviousStep =
                onPreviousStep,

            colors =
                colors
        )
    }
}

private fun scaleTargetShape(
    shape: TargetShape,
    scale: Float,
    density: androidx.compose.ui.unit.Density
): TargetShape {

    fun Dp.scale(): Dp {
        return with(density) {
            (toPx() * scale).toDp()
        }
    }

    return when (shape) {

        is TargetShape.Circle -> {
            TargetShape.Circle(
                radiusDp =
                    shape.radiusDp.scale()
            )
        }

        is TargetShape.RoundedRect -> {
            TargetShape.RoundedRect(
                widthDp =
                    shape.widthDp.scale(),
                heightDp =
                    shape.heightDp.scale(),
                cornerRadiusDp =
                    shape.cornerRadiusDp.scale()
            )
        }

        is TargetShape.Rectangle -> {
            TargetShape.Rectangle(
                widthDp =
                    shape.widthDp.scale(),
                heightDp =
                    shape.heightDp.scale(),
                cornerRadiusDp =
                    shape.cornerRadiusDp.scale()
            )
        }

        is TargetShape.Oval -> {
            TargetShape.Oval(
                widthDp =
                    shape.widthDp.scale(),
                heightDp =
                    shape.heightDp.scale()
            )
        }

        is TargetShape.Capsule -> {
            TargetShape.Capsule(
                widthDp =
                    shape.widthDp.scale(),
                heightDp =
                    shape.heightDp.scale()
            )
        }

        is TargetShape.CutCornerRect -> {
            TargetShape.CutCornerRect(
                widthDp =
                    shape.widthDp.scale(),
                heightDp =
                    shape.heightDp.scale(),
                cutSizeDp =
                    shape.cutSizeDp.scale()
            )
        }

        is TargetShape.Star -> {
            TargetShape.Star(
                radiusDp =
                    shape.radiusDp.scale(),
                innerRadiusRatio =
                    shape.innerRadiusRatio,
                points =
                    shape.points
            )
        }

        is TargetShape.Triangle -> {
            TargetShape.Triangle(
                widthDp =
                    shape.widthDp.scale(),
                heightDp =
                    shape.heightDp.scale()
            )
        }

        is TargetShape.Polygon -> {
            TargetShape.Polygon(
                radiusDp =
                    shape.radiusDp.scale(),
                sides =
                    shape.sides
            )
        }
    }
}
@Composable
fun CustomImage(modifier: Modifier = Modifier, id: Int, contentScale:ContentScale= ContentScale.FillBounds) {
    Image(
        painter = painterResource(id = id),
        contentDescription = "",
        modifier = modifier,
        contentScale = contentScale,)

}