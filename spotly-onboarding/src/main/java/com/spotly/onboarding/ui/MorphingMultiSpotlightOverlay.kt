package com.spotly.onboarding.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.spotly.onboarding.model.SpotlightShape
import com.spotly.onboarding.model.TargetShape
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

@Composable
fun MorphingMultiSpotlightOverlay(
    spotlights: List<SpotlightShape>,
    modifier: Modifier = Modifier,
    overlayColor: Color = Color.Black.copy(alpha = 0.65f)
) {

    val animatedSpotlights =
        spotlights.mapIndexed { index, item ->

            val targetWidth: Dp
            val targetHeight: Dp
            val targetExtra: Dp
            val shapeType: ShapeType

            when (val shape = item.shape) {

                is TargetShape.Circle -> {
                    targetWidth =
                        shape.radiusDp * 2f

                    targetHeight =
                        shape.radiusDp * 2f

                    targetExtra =
                        shape.radiusDp

                    shapeType =
                        ShapeType.ROUNDED_RECT
                }

                is TargetShape.RoundedRect -> {
                    targetWidth =
                        shape.widthDp

                    targetHeight =
                        shape.heightDp

                    targetExtra =
                        shape.cornerRadiusDp

                    shapeType =
                        ShapeType.ROUNDED_RECT
                }

                is TargetShape.Rectangle -> {
                    targetWidth =
                        shape.widthDp

                    targetHeight =
                        shape.heightDp

                    targetExtra =
                        shape.cornerRadiusDp

                    shapeType =
                        ShapeType.ROUNDED_RECT
                }

                is TargetShape.Oval -> {
                    targetWidth =
                        shape.widthDp

                    targetHeight =
                        shape.heightDp

                    targetExtra = 0.dp

                    shapeType =
                        ShapeType.OVAL
                }

                is TargetShape.Capsule -> {
                    targetWidth =
                        shape.widthDp

                    targetHeight =
                        shape.heightDp

                    targetExtra =
                        min(
                            shape.widthDp.value,
                            shape.heightDp.value
                        ).dp / 2f

                    shapeType =
                        ShapeType.ROUNDED_RECT
                }

                is TargetShape.CutCornerRect -> {
                    targetWidth =
                        shape.widthDp

                    targetHeight =
                        shape.heightDp

                    targetExtra =
                        shape.cutSizeDp

                    shapeType =
                        ShapeType.CUT_CORNER
                }

                is TargetShape.Star -> {
                    require(shape.points >= 3) {
                        "Star points must be >= 3"
                    }

                    targetWidth =
                        shape.radiusDp * 2f

                    targetHeight =
                        shape.radiusDp * 2f

                    targetExtra =
                        shape.radiusDp *
                                shape.innerRadiusRatio

                    shapeType =
                        ShapeType.STAR(
                            points = shape.points
                        )
                }

                is TargetShape.Triangle -> {
                    targetWidth =
                        shape.widthDp

                    targetHeight =
                        shape.heightDp

                    targetExtra = 0.dp

                    shapeType =
                        ShapeType.TRIANGLE
                }

                is TargetShape.Polygon -> {
                    require(shape.sides >= 3) {
                        "Polygon sides must be >= 3"
                    }

                    targetWidth =
                        shape.radiusDp * 2f

                    targetHeight =
                        shape.radiusDp * 2f

                    targetExtra = 0.dp

                    shapeType =
                        ShapeType.POLYGON(
                            sides = shape.sides
                        )
                }
            }

            val animOffsetX by animateDpAsState(
                targetValue = item.offsetDp.x,
                animationSpec = spring(
                    stiffness = Spring.StiffnessLow
                ),
                label = "offsetX_$index"
            )

            val animOffsetY by animateDpAsState(
                targetValue = item.offsetDp.y,
                animationSpec = spring(
                    stiffness = Spring.StiffnessLow
                ),
                label = "offsetY_$index"
            )

            val animWidth by animateDpAsState(
                targetValue = targetWidth,
                animationSpec = spring(
                    stiffness = Spring.StiffnessLow
                ),
                label = "width_$index"
            )

            val animHeight by animateDpAsState(
                targetValue = targetHeight,
                animationSpec = spring(
                    stiffness = Spring.StiffnessLow
                ),
                label = "height_$index"
            )

            val animExtra by animateDpAsState(
                targetValue = targetExtra,
                animationSpec = spring(
                    stiffness = Spring.StiffnessLow
                ),
                label = "extra_$index"
            )

            AnimatedSpotlightDpState(
                offset = DpOffset(
                    animOffsetX,
                    animOffsetY
                ),
                width = animWidth,
                height = animHeight,
                extra = animExtra,
                shapeType = shapeType
            )
        }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .graphicsLayer(
                compositingStrategy =
                    CompositingStrategy.Offscreen
            )
    ) {

        drawRect(
            color = overlayColor
        )

        animatedSpotlights.forEach { state ->

            val centerX =
                state.offset.x.toPx()

            val centerY =
                state.offset.y.toPx()

            val width =
                state.width.toPx()

            val height =
                state.height.toPx()

            val topLeft =
                Offset(
                    x = centerX - width / 2f,
                    y = centerY - height / 2f
                )

            when (val shape = state.shapeType) {

                ShapeType.ROUNDED_RECT -> {

                    drawRoundRect(
                        color = Color.Transparent,
                        topLeft = topLeft,
                        size = Size(
                            width,
                            height
                        ),
                        cornerRadius = CornerRadius(
                            x = state.extra.toPx(),
                            y = state.extra.toPx()
                        ),
                        blendMode = BlendMode.Clear
                    )
                }

                ShapeType.OVAL -> {

                    drawOval(
                        color = Color.Transparent,
                        topLeft = topLeft,
                        size = Size(
                            width,
                            height
                        ),
                        blendMode = BlendMode.Clear
                    )
                }

                ShapeType.CUT_CORNER -> {

                    drawCutCornerRect(
                        topLeft = topLeft,
                        width = width,
                        height = height,
                        cutSize = state.extra.toPx()
                    )
                }

                ShapeType.TRIANGLE -> {

                    drawTriangle(
                        topLeft = topLeft,
                        width = width,
                        height = height
                    )
                }

                is ShapeType.STAR -> {

                    drawStar(
                        center = Offset(
                            centerX,
                            centerY
                        ),
                        radius =
                            min(width, height) / 2f,
                        innerRadius =
                            state.extra.toPx(),
                        points = shape.points
                    )
                }

                is ShapeType.POLYGON -> {

                    drawPolygon(
                        center = Offset(
                            centerX,
                            centerY
                        ),
                        radius =
                            min(width, height) / 2f,
                        sides = shape.sides
                    )
                }
            }
        }
    }
}

private sealed class ShapeType {

    data object ROUNDED_RECT : ShapeType()

    data object OVAL : ShapeType()

    data object CUT_CORNER : ShapeType()

    data object TRIANGLE : ShapeType()

    data class STAR(
        val points: Int
    ) : ShapeType()

    data class POLYGON(
        val sides: Int
    ) : ShapeType()
}

private data class AnimatedSpotlightDpState(
    val offset: DpOffset,
    val width: Dp,
    val height: Dp,
    val extra: Dp,
    val shapeType: ShapeType
)

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawCutCornerRect(
    topLeft: Offset,
    width: Float,
    height: Float,
    cutSize: Float
) {

    val safeCut =
        cutSize.coerceAtMost(
            min(width, height) / 2f
        )

    val path = Path().apply {

        moveTo(
            topLeft.x + safeCut,
            topLeft.y
        )

        lineTo(
            topLeft.x + width,
            topLeft.y
        )

        lineTo(
            topLeft.x + width,
            topLeft.y + height - safeCut
        )

        lineTo(
            topLeft.x + width - safeCut,
            topLeft.y + height
        )

        lineTo(
            topLeft.x,
            topLeft.y + height
        )

        lineTo(
            topLeft.x,
            topLeft.y + safeCut
        )

        close()
    }

    drawPath(
        path = path,
        color = Color.Transparent,
        blendMode = BlendMode.Clear
    )
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawTriangle(
    topLeft: Offset,
    width: Float,
    height: Float
) {

    val path = Path().apply {

        moveTo(
            topLeft.x + width / 2f,
            topLeft.y
        )

        lineTo(
            topLeft.x + width,
            topLeft.y + height
        )

        lineTo(
            topLeft.x,
            topLeft.y + height
        )

        close()
    }

    drawPath(
        path = path,
        color = Color.Transparent,
        blendMode = BlendMode.Clear
    )
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawStar(
    center: Offset,
    radius: Float,
    innerRadius: Float,
    points: Int
) {

    val path = createStarPath(
        center = center,
        outerRadius = radius,
        innerRadius =
            innerRadius.coerceIn(
                0f,
                radius
            ),
        points = points
    )

    drawPath(
        path = path,
        color = Color.Transparent,
        blendMode = BlendMode.Clear
    )
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawPolygon(
    center: Offset,
    radius: Float,
    sides: Int
) {

    val path = createPolygonPath(
        center = center,
        radius = radius,
        sides = sides
    )

    drawPath(
        path = path,
        color = Color.Transparent,
        blendMode = BlendMode.Clear
    )
}

private fun createStarPath(
    center: Offset,
    outerRadius: Float,
    innerRadius: Float,
    points: Int
): Path {

    val path = Path()

    val totalPoints =
        points * 2

    val angleStep =
        (2f * PI.toFloat()) /
                totalPoints

    val startAngle =
        -PI.toFloat() / 2f

    for (index in 0 until totalPoints) {

        val radius =
            if (index % 2 == 0) {
                outerRadius
            } else {
                innerRadius
            }

        val angle =
            startAngle +
                    index * angleStep

        val x =
            center.x +
                    cos(angle) * radius

        val y =
            center.y +
                    sin(angle) * radius

        if (index == 0) {
            path.moveTo(x, y)
        } else {
            path.lineTo(x, y)
        }
    }

    path.close()

    return path
}

private fun createPolygonPath(
    center: Offset,
    radius: Float,
    sides: Int
): Path {

    val path = Path()

    val angleStep =
        (2f * PI.toFloat()) /
                sides

    val startAngle =
        -PI.toFloat() / 2f

    for (index in 0 until sides) {

        val angle =
            startAngle +
                    index * angleStep

        val x =
            center.x +
                    cos(angle) * radius

        val y =
            center.y +
                    sin(angle) * radius

        if (index == 0) {
            path.moveTo(x, y)
        } else {
            path.lineTo(x, y)
        }
    }

    path.close()

    return path
}