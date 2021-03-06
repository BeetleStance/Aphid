/*
 * Copyright 2020 BeetleStance
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:Suppress("NOTHING_TO_INLINE", "unused")

@file:JvmName("ComposeInsets")
@file:JvmMultifileClass

package com.beetlestance.aphid.common_compose.insets

import android.os.Build
import android.view.View
import android.view.WindowInsetsAnimation
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.ProvidableAmbient
import androidx.compose.runtime.Providers
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticAmbientOf
import androidx.compose.ui.platform.AmbientView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsCompat.Type
import android.view.WindowInsets as WindowInsetsPlatform

/**
 * Main holder of our inset values.
 */
@Stable
class WindowInsets {
    /**
     * Inset values which match [WindowInsetsCompat.Type.systemBars]
     */
    val systemBars: Insets = Insets()

    /**
     * Inset values which match [WindowInsetsCompat.Type.systemGestures]
     */
    val systemGestures: Insets = Insets()

    /**
     * Inset values which match [WindowInsetsCompat.Type.navigationBars]
     */
    val navigationBars: Insets = Insets()

    /**
     * Inset values which match [WindowInsetsCompat.Type.statusBars]
     */
    val statusBars: Insets = Insets()

    /**
     * Inset values which match [WindowInsetsCompat.Type.ime]
     */
    val ime: Insets = Insets()
}

@Stable
class Insets {
    /**
     * The left dimension of these insets in pixels.
     */
    var left: Int by mutableStateOf(0)
        internal set

    /**
     * The top dimension of these insets in pixels.
     */
    var top: Int by mutableStateOf(0)
        internal set

    /**
     * The right dimension of these insets in pixels.
     */
    var right: Int by mutableStateOf(0)
        internal set

    /**
     * The bottom dimension of these insets in pixels.
     */
    var bottom: Int by mutableStateOf(0)
        internal set

    /**
     * Whether the insets are currently visible.
     */
    var isVisible: Boolean by mutableStateOf(true)
        internal set

    internal var ongoingAnimations by mutableStateOf(0)

    fun copy(
        left: Int = this.left,
        top: Int = this.top,
        right: Int = this.right,
        bottom: Int = this.bottom,
        isVisible: Boolean = this.isVisible,
    ): Insets = Insets().apply {
        this.left = left
        this.top = top
        this.right = right
        this.bottom = bottom
        this.isVisible = isVisible
        this.ongoingAnimations = ongoingAnimations
    }

    operator fun minus(other: Insets): Insets = copy(
        left = this@Insets.left - other.left,
        top = this@Insets.top - other.top,
        right = this@Insets.right - other.right,
        bottom = this@Insets.bottom - other.bottom,
    )

    operator fun plus(other: Insets): Insets = copy(
        left = this@Insets.left + other.left,
        top = this@Insets.top + other.top,
        right = this@Insets.right + other.right,
        bottom = this@Insets.bottom + other.bottom,
    )
}

val AmbientWindowInsets: ProvidableAmbient<WindowInsets> = staticAmbientOf {
    error("AmbientWindowInsets value not available. Are you using ProvideWindowInsets?")
}

/**
 * Applies any [WindowInsetsCompat] values to [AmbientWindowInsets], which are then available
 * within [content].
 *
 * @param consumeWindowInsets Whether to consume any [WindowInsetsCompat]s which are dispatched to
 * the host view. Defaults to `true`.
 * @param windowInsetsAnimationsEnabled Whether to listen for [WindowInsetsAnimation]s, such as
 * IME animations. Defaults to `true`.
 */
@Composable
fun ProvideWindowInsets(
    consumeWindowInsets: Boolean = true,
    windowInsetsAnimationsEnabled: Boolean = true,
    content: @Composable () -> Unit
) {
    val view = AmbientView.current

    val windowInsets = remember { WindowInsets() }

    DisposableEffect(view) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { _, wic ->
            // Go through each inset type and update it from the WindowInsetsCompat.
            //
            // If the inset type is currently being animated we ignore this pass. This is
            // because WindowInsetsAnimation is built with the view system in mind, such that
            // it forces consumers to be laid out in the final state, then immediately transform
            // itself to look like the start state (because we shouldn't use layout
            // animations in views). For more information, see:
            // https://medium.com/androiddevelopers/animating-your-keyboard-reacting-to-inset-animations-839be3d4c31b
            //
            // Compose (hopefully) doesn't need that, so we can just apply the animated insets
            // directly to the layout.
            windowInsets.statusBars.run {
                if (ongoingAnimations == 0) updateFrom(wic, Type.statusBars())
            }
            windowInsets.navigationBars.run {
                if (ongoingAnimations == 0) updateFrom(wic, Type.navigationBars())
            }
            windowInsets.systemBars.run {
                if (ongoingAnimations == 0) updateFrom(wic, Type.systemBars())
            }
            windowInsets.systemGestures.run {
                if (ongoingAnimations == 0) updateFrom(wic, Type.systemGestures())
            }
            windowInsets.ime.run {
                if (ongoingAnimations == 0) updateFrom(wic, Type.ime())
            }

            if (consumeWindowInsets) WindowInsetsCompat.CONSUMED else wic
        }

        // Add an OnAttachStateChangeListener to request an inset pass each time we're attached
        // to the window
        val attachListener = object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) = v.requestApplyInsets()
            override fun onViewDetachedFromWindow(v: View) = Unit
        }
        view.addOnAttachStateChangeListener(attachListener)

        if (Build.VERSION.SDK_INT >= 30 && windowInsetsAnimationsEnabled) {
            InnerWindowInsetsAnimationCallback.setup(view, windowInsets)
        }

        if (view.isAttachedToWindow) {
            // If the view is already attached, we can request an inset pass now
            view.requestApplyInsets()
        }

        onDispose {
            view.removeOnAttachStateChangeListener(attachListener)
        }
    }

    Providers(AmbientWindowInsets provides windowInsets) {
        content()
    }
}

@RequiresApi(30)
private class InnerWindowInsetsAnimationCallback(
    private val windowInsets: WindowInsets,
) : WindowInsetsAnimation.Callback(DISPATCH_MODE_STOP) {
    override fun onPrepare(animation: WindowInsetsAnimation) {
        if (animation.typeMask and WindowInsetsPlatform.Type.ime() != 0) {
            windowInsets.ime.ongoingAnimations++
        }
        // TODO add rest of types
    }

    override fun onProgress(
        insets: WindowInsetsPlatform,
        runningAnimations: MutableList<WindowInsetsAnimation>
    ): WindowInsetsPlatform {
        windowInsets.ime.updateFrom(insets, WindowInsetsPlatform.Type.ime())
        // TODO add rest of types

        return insets
    }

    override fun onEnd(animation: WindowInsetsAnimation) {
        if (animation.typeMask and WindowInsetsPlatform.Type.ime() != 0) {
            windowInsets.ime.ongoingAnimations--
        }
        // TODO add rest of types
    }

    companion object {
        /**
         * This function may look useless, but this keeps the API 30 method call in a
         * separate class, which makes class loaders on older platforms happy.
         */
        fun setup(view: View, windowInsets: WindowInsets) {
            view.setWindowInsetsAnimationCallback(InnerWindowInsetsAnimationCallback(windowInsets))
        }
    }
}

/**
 * Updates our mutable state backed [Insets] from an Android system insets.
 */
private fun Insets.updateFrom(wic: WindowInsetsCompat, type: Int) {
    val insets = wic.getInsets(type)
    left = insets.left
    top = insets.top
    right = insets.right
    bottom = insets.bottom

    isVisible = wic.isVisible(type)
}

/**
 * Updates our mutable state backed [Insets] from an Android system insets.
 */
@RequiresApi(30)
internal fun Insets.updateFrom(windowInsets: WindowInsetsPlatform, type: Int) {
    val insets = windowInsets.getInsets(type)
    left = insets.left
    top = insets.top
    right = insets.right
    bottom = insets.bottom

    isVisible = windowInsets.isVisible(type)
}

internal fun Insets.coerceEachDimensionAtLeast(other: Insets): Insets {
    // Fast path, no need to copy if `this` >= `other`
    if (left >= other.left && top >= other.top && right >= other.right && bottom >= other.bottom) {
        return this
    }
    return copy(
        left = left.coerceAtLeast(other.left),
        top = top.coerceAtLeast(other.top),
        right = right.coerceAtLeast(other.right),
        bottom = bottom.coerceAtLeast(other.bottom),
    )
}

enum class HorizontalSide { Left, Right }
enum class VerticalSide { Top, Bottom }
