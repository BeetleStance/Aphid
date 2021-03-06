/*
 * Copyright 2021 BeetleStance
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
@file:Suppress("NOTHING_TO_INLINE")

package com.beetlestance.aphid.common_compose

import androidx.compose.runtime.Composable
import com.beetlestance.aphid.common_compose.insets.ProvideWindowInsets
import com.google.android.material.composethemeadapter.MdcTheme

@Composable
inline fun AphidContent(
    noinline content: @Composable () -> Unit
) {
    // Provides theme
    MdcTheme {
        // Provides window insets
        ProvideWindowInsets {
            content()
        }
    }
}
