package com.koaidev.backdrop.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastCoerceIn
import androidx.compose.ui.util.lerp
import com.koaidev.backdrop.Backdrop
import com.koaidev.backdrop.backdrops.layerBackdrop
import com.koaidev.backdrop.backdrops.rememberBackdrop
import com.koaidev.backdrop.backdrops.rememberCombinedBackdrop
import com.koaidev.backdrop.backdrops.rememberLayerBackdrop
import com.koaidev.backdrop.components.internal.DampedDragAnimation
import com.koaidev.backdrop.drawBackdrop
import com.koaidev.backdrop.effects.blur
import com.koaidev.backdrop.effects.lens
import com.koaidev.backdrop.highlight.Highlight
import com.koaidev.backdrop.shadow.InnerShadow
import com.koaidev.backdrop.shadow.Shadow
import com.kyant.shapes.Capsule
import kotlinx.coroutines.flow.collectLatest

/**
 * A glass switch. Thumb travel is derived from the measured track width, [thumbSize], and
 * [thumbPadding]. Caller constraints can resize the track; the thumb is limited to fit it.
 */
@Composable
fun LiquidToggle(
    selected: () -> Boolean,
    onSelect: (Boolean) -> Unit,
    backdrop: Backdrop,
    modifier: Modifier = Modifier,
    trackSize: DpSize = DpSize(64.dp, 28.dp),
    thumbSize: DpSize = DpSize(40.dp, 24.dp),
    thumbPadding: Dp = 2.dp,
    trackShape: Shape = Capsule(),
    thumbShape: Shape = Capsule(),
    accentColor: Color = Color.Unspecified,
    trackColor: Color = Color.Unspecified,
    thumbColor: Color = Color.White
) {
    require(trackSize.width.value.isFinite() && trackSize.width > 0.dp &&
        trackSize.height.value.isFinite() && trackSize.height > 0.dp) { "trackSize must be positive and finite" }
    require(thumbSize.width.value.isFinite() && thumbSize.width > 0.dp &&
        thumbSize.height.value.isFinite() && thumbSize.height > 0.dp) { "thumbSize must be positive and finite" }
    require(thumbPadding.value.isFinite() && thumbPadding >= 0.dp &&
        thumbSize.width + thumbPadding * 2 < trackSize.width && thumbSize.height <= trackSize.height) {
        "thumbSize and thumbPadding must fit inside trackSize and leave horizontal travel"
    }

    val isLightTheme = !isSystemInDarkTheme()
    val resolvedAccentColor =
        if (accentColor.isSpecified) accentColor
        else if (isLightTheme) Color(0xFF34C759)
        else Color(0xFF30D158)
    val resolvedTrackColor =
        if (trackColor.isSpecified) trackColor
        else if (isLightTheme) Color(0xFF787878).copy(0.2f)
        else Color(0xFF787880).copy(0.36f)

    val currentSelected by rememberUpdatedState(selected)
    val currentOnSelect by rememberUpdatedState(onSelect)
    val trackBackdrop = rememberLayerBackdrop()

    BoxWithConstraints(
        modifier.size(trackSize),
        contentAlignment = Alignment.CenterStart
    ) {
        require(maxWidth > thumbPadding * 2 && maxHeight > 0.dp) {
            "The available track size must leave room for the thumb"
        }
        val actualThumbWidth = minOf(thumbSize.width, maxWidth - thumbPadding * 2)
        val actualThumbHeight = minOf(thumbSize.height, maxHeight)
        val density = LocalDensity.current
        val isLtr by rememberUpdatedState(LocalLayoutDirection.current == LayoutDirection.Ltr)
        val dragWidth by rememberUpdatedState(with(density) {
            (maxWidth - actualThumbWidth - thumbPadding * 2).toPx()
        })
        val animationScope = rememberCoroutineScope()
        var didDrag by remember { mutableStateOf(false) }
        var fraction by remember { mutableFloatStateOf(if (selected()) 1f else 0f) }
        val dampedDragAnimation = remember(animationScope) {
            DampedDragAnimation(
                animationScope = animationScope,
                initialValue = fraction,
                valueRange = 0f..1f,
                visibilityThreshold = 0.001f,
                initialScale = 1f,
                pressedScale = 1.5f,
                onDragStarted = {},
                onDragStopped = {
                    if (didDrag) {
                        fraction = if (targetValue >= 0.5f) 1f else 0f
                        currentOnSelect(fraction == 1f)
                        didDrag = false
                    } else {
                        fraction = if (currentSelected()) 0f else 1f
                        currentOnSelect(fraction == 1f)
                    }
                },
                onDrag = { _, dragAmount ->
                    if (!didDrag) {
                        didDrag = dragAmount.x != 0f
                    }
                    val delta = if (dragWidth > 0f) dragAmount.x / dragWidth else 0f
                    fraction =
                        if (isLtr) (fraction + delta).fastCoerceIn(0f, 1f)
                        else (fraction - delta).fastCoerceIn(0f, 1f)
                }
            )
        }
        LaunchedEffect(dampedDragAnimation) {
            snapshotFlow { fraction }
                .collectLatest { fraction ->
                    dampedDragAnimation.updateValue(fraction)
                }
        }
        LaunchedEffect(selected) {
            snapshotFlow { selected() }
                .collectLatest { isSelected ->
                    val target = if (isSelected) 1f else 0f
                    if (target != fraction) {
                        fraction = target
                        dampedDragAnimation.animateToValue(target)
                    }
                }
        }

        Box(
            Modifier
                .layerBackdrop(trackBackdrop)
                .clip(trackShape)
                .drawBehind {
                    val fraction = dampedDragAnimation.value
                    drawRect(lerp(resolvedTrackColor, resolvedAccentColor, fraction))
                }
                .fillMaxSize()
        )

        Box(
            Modifier
                .graphicsLayer {
                    val fraction = dampedDragAnimation.value
                    val padding = thumbPadding.toPx()
                    translationX =
                        if (isLtr) lerp(padding, padding + dragWidth, fraction)
                        else lerp(-padding, -(padding + dragWidth), fraction)
                }
                .semantics {
                    role = Role.Switch
                }
                .then(dampedDragAnimation.modifier)
                .drawBackdrop(
                    backdrop = rememberCombinedBackdrop(
                        backdrop,
                        rememberBackdrop(trackBackdrop) { drawBackdrop ->
                            val progress = dampedDragAnimation.pressProgress
                            val scaleX = lerp(2f / 3f, 0.75f, progress)
                            val scaleY = lerp(0f, 0.75f, progress)
                            scale(scaleX, scaleY) {
                                drawBackdrop()
                            }
                        }
                    ),
                    shape = { thumbShape },
                    effects = {
                        val progress = dampedDragAnimation.pressProgress
                        blur(8f.dp.toPx() * (1f - progress))
                        lens(
                            5f.dp.toPx() * progress,
                            10f.dp.toPx() * progress,
                            chromaticAberration = true
                        )
                    },
                    highlight = {
                        val progress = dampedDragAnimation.pressProgress
                        Highlight.Ambient.copy(
                            width = Highlight.Ambient.width / 1.5f,
                            blurRadius = Highlight.Ambient.blurRadius / 1.5f,
                            alpha = progress
                        )
                    },
                    shadow = {
                        Shadow(
                            radius = 4f.dp,
                            color = Color.Black.copy(alpha = 0.05f)
                        )
                    },
                    innerShadow = {
                        val progress = dampedDragAnimation.pressProgress
                        InnerShadow(
                            radius = 4f.dp * progress,
                            alpha = progress
                        )
                    },
                    layerBlock = {
                        scaleX = dampedDragAnimation.scaleX
                        scaleY = dampedDragAnimation.scaleY
                        val velocity = dampedDragAnimation.velocity / 50f
                        scaleX /= 1f - (velocity * 0.75f).fastCoerceIn(-0.2f, 0.2f)
                        scaleY *= 1f - (velocity * 0.25f).fastCoerceIn(-0.2f, 0.2f)
                    },
                    onDrawSurface = {
                        val progress = dampedDragAnimation.pressProgress
                        drawRect(thumbColor, alpha = 1f - progress)
                    }
                )
                .size(actualThumbWidth, actualThumbHeight)
        )
    }
}
