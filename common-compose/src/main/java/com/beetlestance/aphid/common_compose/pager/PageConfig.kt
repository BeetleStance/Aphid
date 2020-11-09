package com.beetlestance.aphid.common_compose.pager

import androidx.compose.animation.animate
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class PageConfig(
    val maxWidth: Dp,
    val fraction: Float,
    val aspectRatio: Float,
    val pageElevation: Dp = 2.dp,
    val currentPageElevation: Dp = 12.dp,
    val horizontalOffset: Dp,
    val horizontalOffsetFraction: Float,
    val enableDefaultTransformation: Boolean = false,
    val enableDefaultAnimation: Boolean = false,
) {
    val minWidth: Dp = maxWidth - maxWidth.times(horizontalOffsetFraction)

    val maxHeight: Dp = maxWidth / aspectRatio

    val minHeight: Dp = minWidth / aspectRatio

    @Composable
    fun elevation(isSelected: Boolean): Dp {
        val elevation = if (isSelected || enableDefaultAnimation.not()) currentPageElevation
        else pageElevation

        return if (enableDefaultAnimation) animate(target = elevation) else elevation
    }

    @Composable
    fun width(isSelected: Boolean): Dp {
        val width = if (isSelected || enableDefaultAnimation.not()) maxWidth else minWidth
        return if (enableDefaultAnimation) animate(target = width) else width
    }

    @Composable
    fun height(isSelected: Boolean): Dp {
        val height = if (isSelected || enableDefaultAnimation.not()) maxHeight else minHeight
        return if (enableDefaultAnimation) animate(target = height) else height
    }
}