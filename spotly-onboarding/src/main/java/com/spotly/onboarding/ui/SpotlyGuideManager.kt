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
import com.spotly.onboarding.model.GuideStep
import com.spotly.onboarding.model.SpotlightShape
import com.spotly.onboarding.model.TargetShape
import com.spotly.onboarding.model.SpotlyBottomContentData
import com.spotly.onboarding.theme.SpotlyColors
import com.spotly.onboarding.ui.MorphingMultiSpotlightOverlay

import kotlinx.coroutines.delay


@Composable
fun SpotlyGuideManager(
    steps: List<GuideStep>,
    currentStepIndex: Int,

    /*
     * API فعلی شما بدون تغییر باقی می‌ماند.
     */
    onNextStep: () -> Unit,
    onSkipOrFinish: () -> Unit,
    onPreviousStep: () -> Unit,

    modifier: Modifier = Modifier,

    colors: SpotlyColors = SpotlyColors(),
    bottomContent: @Composable (SpotlyBottomContentData) -> Unit = { data ->
        SpotlyBottomContent(
            title = data.step.title,
            description = data.step.description,
            progress = data.progress,
            isFirstStep = data.isFirstStep,
            isLastStep = data.isLastStep,
            showNavigationButtons = true,
            onNext = data.onNext,
            onSkipOrFinish = data.onSkipOrFinish,
            onPreviousStep = data.onPrevious,
            colors = data.colors
        )
    }
) {

    /*
     * ---------------------------------------------------------
     * SAFETY
     * ---------------------------------------------------------
     */
    if (
        steps.isEmpty() ||
        currentStepIndex !in steps.indices
    ) {
        return
    }


    /*
     * ---------------------------------------------------------
     * CURRENT STEP
     * ---------------------------------------------------------
     */
    val currentStep =
        steps[currentStepIndex]


    val isFirstStep =
        currentStepIndex == 0


    val isLastStep =
        currentStepIndex == steps.lastIndex


    /*
     * ---------------------------------------------------------
     * BOTTOM CONTENT VISIBILITY
     * ---------------------------------------------------------
     */
    var showBottomContent by remember {
        mutableStateOf(
            currentStep.showDescription
        )
    }


    /*
     * هر بار step عوض شد،
     * visibility مربوط به step جدید را تنظیم کن.
     */
    LaunchedEffect(
        currentStepIndex,
        currentStep.showDescription
    ) {

        showBottomContent =
            currentStep.showDescription
    }


    /*
     * ---------------------------------------------------------
     * AUTO ADVANCE
     * ---------------------------------------------------------
     *
     * اگر step Auto باشد:
     *
     *     2 -> بعد از تایمر -> 3
     *     3 -> بعد از تایمر -> 4
     *     4 -> بعد از تایمر -> 5
     *     5 -> بعد از تایمر -> 6
     *
     * وقتی Previous از 6 زده شود:
     *
     *     6 -> 2
     *
     * و چون currentStepIndex تغییر کرده،
     * LaunchedEffect برای step 2 دوباره اجرا می‌شود.
     */
    LaunchedEffect(
        currentStepIndex,
        currentStep.isAutoAdvance,
        currentStep.autoAdvanceDurationMillis
    ) {

        if (!currentStep.isAutoAdvance) {
            return@LaunchedEffect
        }


        /*
         * صبر برای تایمر step فعلی
         */
        delay(
            currentStep.autoAdvanceDurationMillis
                .coerceAtLeast(1L)
        )


        /*
         * fade out
         */
        showBottomContent = false


        /*
         * صبر برای تمام شدن fade
         */
        delay(
            BOTTOM_CONTENT_ANIMATION_DURATION.toLong()
        )


        /*
         * اگر آخرین step هستیم،
         * onboarding تمام شود.
         */
        if (currentStepIndex == steps.lastIndex) {

            onSkipOrFinish()

        } else {

            /*
             * step بعدی
             */
            onNextStep()
        }
    }


    /*
     * ---------------------------------------------------------
     * SCROLL
     * ---------------------------------------------------------
     */
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
     * ---------------------------------------------------------
     * IMAGE SCROLL
     * ---------------------------------------------------------
     *
     * با تغییر step،
     * تصویر به position مربوط به آن step می‌رود.
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

            value =
                targetScrollPx.coerceIn(
                    0,
                    scrollState.maxValue
                ),

            animationSpec =
                spring(
                    stiffness =
                        Spring.StiffnessLow
                )
        )
    }


    /*
     * ---------------------------------------------------------
     * SPOTLIGHT CALCULATION
     * ---------------------------------------------------------
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

                /*
                 * Base design width
                 */
                val baseWidthPx =
                    with(density) {
                        360.dp.toPx()
                    }


                /*
                 * Scale image
                 */
                val scale =
                    imageSize.width.toFloat() /
                            baseWidthPx


                /*
                 * Current scroll
                 */
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

                        offsetDp =
                            DpOffset(

                                x =
                                    with(density) {
                                        xPx.toDp()
                                    },

                                y =
                                    with(density) {
                                        yPx.toDp()
                                    }
                            ),

                        shape =
                            scaleTargetShape(
                                shape = item.shape,
                                scale = scale,
                                density = density
                            )
                    )
                }
            }
        }


    /*
     * ---------------------------------------------------------
     * MAIN UI
     * ---------------------------------------------------------
     */
    Box(
        modifier =
            modifier.fillMaxSize()
    ) {


        /*
         * -----------------------------------------------------
         * ONBOARDING IMAGE
         * -----------------------------------------------------
         */
        Column(

            modifier =
                Modifier
                    .fillMaxSize()
                    .clipToBounds()
                    .verticalScroll(
                        state = scrollState,
                        enabled = true
                    )
        ) {

            CustomImage(

                id =
                    currentStep.imageRes,

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned {
                                coordinates ->

                            imageSize =
                                coordinates.size
                        },

                contentScale =
                    ContentScale.FillWidth
            )
        }


        /*
         * -----------------------------------------------------
         * SPOTLIGHT OVERLAY
         * -----------------------------------------------------
         */
        MorphingMultiSpotlightOverlay(

            spotlights =
                calculatedSpotlights,

            overlayColor =
                colors.overlayColor,

            modifier =
                Modifier.fillMaxSize()
        )


        /*
         * -----------------------------------------------------
         * BOTTOM CONTENT
         * -----------------------------------------------------
         */
        AnimatedVisibility(

            visible =
                showBottomContent,

            enter =
                fadeIn(
                    animationSpec =
                        tween(
                            durationMillis =
                                BOTTOM_CONTENT_ANIMATION_DURATION
                        )
                ),

            exit =
                fadeOut(
                    animationSpec =
                        tween(
                            durationMillis =
                                BOTTOM_CONTENT_ANIMATION_DURATION
                        )
                ),

            modifier =
                Modifier.fillMaxSize()
        ) {

            val contentData = SpotlyBottomContentData(
                step = currentStep,
                progress = (currentStepIndex + 1f) / steps.size,
                isFirstStep = isFirstStep,
                isLastStep = isLastStep,
                onNext = {
                    handleManualStepChange(
                        showBottomContent = {
                            showBottomContent = false
                        },
                        action = {
                            onNextStep()
                        }
                    )
                },
                onPrevious = {
                    handleManualStepChange(
                        showBottomContent = {
                            showBottomContent = false
                        },
                        action = {
                            val previousIndex =
                                findPreviousStepIndex(
                                    steps = steps,
                                    currentIndex = currentStepIndex
                                )

                            val stepsToGoBack =
                                currentStepIndex - previousIndex

                            repeat(stepsToGoBack) {
                                onPreviousStep()
                            }
                        }
                    )
                },
                onSkipOrFinish = {
                    handleManualStepChange(
                        showBottomContent = {
                            showBottomContent = false
                        },
                        action = {
                            onSkipOrFinish()
                        }
                    )
                },
                colors = colors
            )

            bottomContent(contentData)
        }
    }
}


/*
 * =============================================================
 * FIND PREVIOUS STEP
 * =============================================================
 *
 * هدف:
 *
 * اگر step قبلی Auto باشد،
 * ابتدای گروه Auto را پیدا کن.
 *
 *
 * مثال:
 *
 * 0 normal
 * 1 normal
 * 2 auto
 * 3 auto
 * 4 auto
 * 5 auto
 * 6 normal
 *
 *
 * نتیجه:
 *
 * current 6 -> 2
 * current 5 -> 2
 * current 4 -> 2
 * current 3 -> 2
 * current 2 -> 1
 * current 1 -> 0
 */
private fun findPreviousStepIndex(
    steps: List<GuideStep>,
    currentIndex: Int
): Int {

    /*
     * اولین step
     */
    if (currentIndex <= 0) {
        return 0
    }


    /*
     * step قبلی
     */
    var previousIndex =
        currentIndex - 1


    /*
     * اگر step قبلی Auto نیست،
     * فقط یک step عقب برو.
     *
     * مثال:
     *
     * 6 normal
     * 5 normal
     *
     * 6 -> 5
     */
    if (
        !steps[previousIndex].isAutoAdvance
    ) {

        return previousIndex
    }


    /*
     * step قبلی Auto است.
     *
     * حالا ابتدای گروه Auto را پیدا می‌کنیم.
     *
     * مثلاً:
     *
     * 2 Auto
     * 3 Auto
     * 4 Auto
     * 5 Auto
     *
     * از 5 شروع می‌کنیم:
     *
     * 5
     * 4
     * 3
     * 2
     */
    while (
        previousIndex > 0 &&
        steps[previousIndex - 1].isAutoAdvance
    ) {

        previousIndex--
    }


    /*
     * اولین Auto گروه
     */
    return previousIndex
}


/*
 * =============================================================
 * MANUAL STEP CHANGE
 * =============================================================
 */
private fun handleManualStepChange(
    showBottomContent: () -> Unit,
    action: () -> Unit
) {

    /*
     * اول fade out
     */
    showBottomContent()


    /*
     * بعد تغییر step
     */
    action()
}


/*
 * =============================================================
 * CONSTANT
 * =============================================================
 */
private const val
        BOTTOM_CONTENT_ANIMATION_DURATION =
    300


/*
 * =============================================================
 * SCALE TARGET SHAPE
 * =============================================================
 */
private fun scaleTargetShape(
    shape: TargetShape,
    scale: Float,
    density: androidx.compose.ui.unit.Density
): TargetShape {


    fun Dp.scale(): Dp {

        return with(density) {

            (
                    toPx() * scale
                    ).toDp()
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