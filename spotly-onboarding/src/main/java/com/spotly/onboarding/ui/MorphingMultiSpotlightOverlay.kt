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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.spotly.onboarding.model.SpotlightShape
import com.spotly.onboarding.model.TargetShape
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun MorphingMultiSpotlightOverlay(
    spotlights: List<SpotlightShape>,
    modifier: Modifier = Modifier,
    overlayColor: Color = Color.Black.copy(alpha = 0.65f)
) {
    val density = LocalDensity.current

    val animatedSpotlights = spotlights.mapIndexed { index, item ->
        val (targetWidth, targetHeight, targetExtra, shapeType) = when (val shape = item.shape) {
            is TargetShape.Circle -> Quadruple(shape.radiusDp * 2, shape.radiusDp * 2, shape.radiusDp, ShapeType.ROUNDED_RECT)
            is TargetShape.RoundedRect -> Quadruple(shape.widthDp, shape.heightDp, shape.cornerRadiusDp, ShapeType.ROUNDED_RECT)
            is TargetShape.Oval -> Quadruple(shape.widthDp, shape.heightDp, 0.dp, ShapeType.OVAL)
            is TargetShape.Capsule -> {
                val minDim = if (shape.widthDp < shape.heightDp) shape.widthDp else shape.heightDp
                Quadruple(shape.widthDp, shape.heightDp, minDim / 2, ShapeType.ROUNDED_RECT)
            }
            is TargetShape.CutCornerRect -> Quadruple(shape.widthDp, shape.heightDp, shape.cutSizeDp, ShapeType.CUT_CORNER)
            is TargetShape.Star -> Quadruple(shape.radiusDp * 2, shape.radiusDp * 2, shape.radiusDp * shape.innerRadiusRatio, ShapeType.STAR(shape.points))
            is TargetShape.Triangle -> Quadruple(shape.widthDp, shape.heightDp, 0.dp, ShapeType.TRIANGLE)
            is TargetShape.Polygon -> Quadruple(shape.radiusDp * 2, shape.radiusDp * 2, 0.dp, ShapeType.POLYGON(shape.sides))
        }

        val animOffsetX by animateDpAsState(
            targetValue = item.offsetDp.x,
            animationSpec = spring(stiffness = Spring.StiffnessLow),
            label = "offsetX_$index"
        )
        val animOffsetY by animateDpAsState(
            targetValue = item.offsetDp.y,
            animationSpec = spring(stiffness = Spring.StiffnessLow),
            label = "offsetY_$index"
        )
        val animWidth by animateDpAsState(
            targetValue = targetWidth,
            animationSpec = spring(stiffness = Spring.StiffnessLow),
            label = "width_$index"
        )
        val animHeight by animateDpAsState(
            targetValue = targetHeight,
            animationSpec = spring(stiffness = Spring.StiffnessLow),
            label = "height_$index"
        )
        val animExtra by animateDpAsState(
            targetValue = targetExtra,
            animationSpec = spring(stiffness = Spring.StiffnessLow),
            label = "extra_$index"
        )

        AnimatedSpotlightDpState(
            offsetX = animOffsetX,
            offsetY = animOffsetY,
            width = animWidth,
            height = animHeight,
            extra = animExtra,
            shapeType = shapeType
        )
    }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    ) {
        drawRect(color = overlayColor)

        animatedSpotlights.forEach { state ->
            val centerXpx = with(density) { state.offsetX.toPx() }
            val centerYpx = with(density) { state.offsetY.toPx() }

            val widthPx = with(density) { state.width.toPx() }
            val heightPx = with(density) { state.height.toPx() }
            val extraPx = with(density) { state.extra.toPx() }

            val topLeftPx = Offset(
                x = centerXpx - (widthPx / 2),
                y = centerYpx - (heightPx / 2)
            )

            when (val type = state.shapeType) {
                ShapeType.ROUNDED_RECT -> {
                    drawRoundRect(
                        color = Color.Transparent,
                        topLeft = topLeftPx,
                        size = Size(widthPx, heightPx),
                        cornerRadius = CornerRadius(extraPx, extraPx),
                        blendMode = BlendMode.Clear
                    )
                }
                ShapeType.OVAL -> {
                    drawOval(
                        color = Color.Transparent,
                        topLeft = topLeftPx,
                        size = Size(widthPx, heightPx),
                        blendMode = BlendMode.Clear
                    )
                }
                ShapeType.CUT_CORNER -> {
                    val path = Path().apply {
                        val left = topLeftPx.x
                        val top = topLeftPx.y
                        val right = left + widthPx
                        val bottom = top + heightPx
                        val c = extraPx.coerceAtMost(minOf(widthPx, heightPx) / 2)

                        moveTo(left + c, top)
                        lineTo(right - c, top)
                        lineTo(right, top + c)
                        lineTo(right, bottom - c)
                        lineTo(right - c, bottom)
                        lineTo(left + c, bottom)
                        lineTo(left, bottom - c)
                        lineTo(left, top + c)
                        close()
                    }
                    drawPath(path = path, color = Color.Transparent, blendMode = BlendMode.Clear)
                }
                is ShapeType.STAR -> {
                    val path = createStarPath(
                        centerX = centerXpx,
                        centerY = centerYpx,
                        outerRadius = widthPx / 2,
                        innerRadius = extraPx,
                        points = type.points
                    )
                    drawPath(path = path, color = Color.Transparent, blendMode = BlendMode.Clear)
                }
                ShapeType.TRIANGLE -> {
                    val path = Path().apply {
                        moveTo(centerXpx, topLeftPx.y)
                        lineTo(topLeftPx.x + widthPx, topLeftPx.y + heightPx)
                        lineTo(topLeftPx.x, topLeftPx.y + heightPx)
                        close()
                    }
                    drawPath(path = path, color = Color.Transparent, blendMode = BlendMode.Clear)
                }
                is ShapeType.POLYGON -> {
                    val path = createPolygonPath(
                        centerX = centerXpx,
                        centerY = centerYpx,
                        radius = widthPx / 2,
                        sides = type.sides
                    )
                    drawPath(path = path, color = Color.Transparent, blendMode = BlendMode.Clear)
                }
            }
        }
    }
}

private fun createStarPath(
    centerX: Float,
    centerY: Float,
    outerRadius: Float,
    innerRadius: Float,
    points: Int
): Path {
    val path = Path()
    val angleStep = Math.PI / points
    var angle = -Math.PI / 2

    path.moveTo(
        (centerX + outerRadius * cos(angle)).toFloat(),
        (centerY + outerRadius * sin(angle)).toFloat()
    )

    for (i in 1 until points * 2) {
        val r = if (i % 2 == 0) outerRadius else innerRadius
        angle += angleStep
        path.lineTo(
            (centerX + r * cos(angle)).toFloat(),
            (centerY + r * sin(angle)).toFloat()
        )
    }
    path.close()
    return path
}

private fun createPolygonPath(
    centerX: Float,
    centerY: Float,
    radius: Float,
    sides: Int
): Path {
    val path = Path()
    val angleStep = 2 * Math.PI / sides
    var angle = -Math.PI / 2

    path.moveTo(
        (centerX + radius * cos(angle)).toFloat(),
        (centerY + radius * sin(angle)).toFloat()
    )

    for (i in 1 until sides) {
        angle += angleStep
        path.lineTo(
            (centerX + radius * cos(angle)).toFloat(),
            (centerY + radius * sin(angle)).toFloat()
        )
    }
    path.close()
    return path
}

private sealed class ShapeType {
    object ROUNDED_RECT : ShapeType()
    object OVAL : ShapeType()
    object CUT_CORNER : ShapeType()
    data class STAR(val points: Int) : ShapeType()
    object TRIANGLE : ShapeType()
    data class POLYGON(val sides: Int) : ShapeType()
}

private data class Quadruple<A, B, C, D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
)

private data class AnimatedSpotlightDpState(
    val offsetX: Dp,
    val offsetY: Dp,
    val width: Dp,
    val height: Dp,
    val extra: Dp,
    val shapeType: ShapeType
)