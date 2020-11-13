package com.beetlestance.aphid.common_compose.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ConfigurationAmbient
import androidx.compose.ui.platform.DensityAmbient
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun widthPercentage(fraction: Float, excludeRootPadding: Dp): Dp {
    return (ConfigurationAmbient.current.screenWidthDp * fraction).dp - excludeRootPadding.times(2)
}

@Composable
fun heightPercentage(fraction: Float, excludeRootPadding: Dp): Dp {
    return (ConfigurationAmbient.current.screenHeightDp * fraction).dp - excludeRootPadding.times(2)
}

@Composable
fun Dp.toPx(): Float = with(DensityAmbient.current) { this@toPx.toPx() }

@Composable
fun Float.toDp() = with(DensityAmbient.current) { this@toDp.toDp() }
