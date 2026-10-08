package com.spotly.onboarding.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import com.golrang.zap.zapdriver.core.guid.SpotlyBottomContent
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

    val currentStep = steps[currentStepIndex]

    val isFirstStep =
        currentStepIndex == 0

    val isLastStep =
        currentStepIndex == steps.lastIndex


    /*
     * Controls the visibility of the whole bottom content.
     *
     * This is NOT related to showDescription.
     *
     * It is only responsible for the transition
     * between onboarding steps.
     */
    var showBottomContent by remember {
        mutableStateOf(currentStep.showDescription)
    }


    /*
     * Every time the step changes,
     * show the new bottom content.
     */
    LaunchedEffect(currentStepIndex, currentStep.showDescription) {
        showBottomContent = currentStep.showDescription
    }


    /*
     * Automatic step handling.
     *
     * This logic is completely independent
     * from showDescription.
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


        /*
         * Fade out the whole bottom content
         * before changing the step.
         */
        showBottomContent = false


        /*
         * Give the fade-out animation enough
         * time to finish.
         */
        delay(
            BOTTOM_CONTENT_ANIMATION_DURATION.toLong()
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
        mutableStateOf(IntSize.Zero)
    }


    /*
     * Animate image scroll position
     * when the current step changes.
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


    /*
     * Calculate spotlight positions according
     * to the actual image width and scroll position.
     */
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

        /*
         * Onboarding image.
         */
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
                        imageSize = coordinates.size
                    },
                contentScale = ContentScale.FillWidth
            )
        }


        /*
         * Spotlight overlay.
         */
        MorphingMultiSpotlightOverlay(
            spotlights = calculatedSpotlights,
            overlayColor = colors.overlayColor,
            modifier = Modifier.fillMaxSize()
        )


        /*
         * Bottom content.
         *
         * This animation controls the whole
         * bottom content when changing steps.
         *
         * showDescription is handled separately
         * inside SpotlyBottomContent.
         */
        AnimatedVisibility(
            visible = showBottomContent,
            enter = fadeIn(
                animationSpec = tween(
                    durationMillis =
                        BOTTOM_CONTENT_ANIMATION_DURATION
                )
            ),
            exit = fadeOut(
                animationSpec = tween(
                    durationMillis =
                        BOTTOM_CONTENT_ANIMATION_DURATION
                )
            ),
            modifier = Modifier.fillMaxSize()
        ) {

          SpotlyBottomContent(
                    title = currentStep.title,

                    description = currentStep.description,

                    progress =
                        (currentStepIndex + 1f) /
                                steps.size,

                    isFirstStep = isFirstStep,

                    isLastStep = isLastStep,

                    /*
                     * Navigation buttons are independent
                     * from showDescription.
                     */
                    showNavigationButtons = true,

                    onNext = {
                        handleManualStepChange(
                            showBottomContent = {
                                showBottomContent = false
                            },
                            onNextStep = onNextStep
                        )
                    },

                    onSkipOrFinish = {
                        handleManualStepChange(
                            showBottomContent = {
                                showBottomContent = false
                            },
                            onSkipOrFinish = onSkipOrFinish
                        )
                    },

                    onPreviousStep = {
                        handleManualStepChange(
                            showBottomContent = {
                                showBottomContent = false
                            },
                            onPreviousStep = onPreviousStep
                        )
                    },

                    colors = colors
                )
        }
    }
}


private fun handleManualStepChange(
    showBottomContent: () -> Unit,
    onNextStep: (() -> Unit)? = null,
    onPreviousStep: (() -> Unit)? = null,
    onSkipOrFinish: (() -> Unit)? = null
) {

    /*
     * Fade out bottom content first.
     */
    showBottomContent()

    when {
        onNextStep != null ->
            onNextStep()

        onPreviousStep != null ->
            onPreviousStep()

        onSkipOrFinish != null ->
            onSkipOrFinish()
    }
}


private const val BOTTOM_CONTENT_ANIMATION_DURATION = 300


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