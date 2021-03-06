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
package com.beetlestance.aphid.common_compose

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AmbientContentAlpha
import androidx.compose.material.Card
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.beetlestance.aphid.base_android.R

@Composable
fun RecipeDetailedPosterCard(
    modifier: Modifier = Modifier,
    elevation: Dp = 4.dp,
    isFavourite: Boolean,
    onCheckedChange: (Boolean) -> Unit = {},
    cardShape: Shape = RoundedCornerShape(16.dp),
    posterImage: @Composable BoxScope.() -> Unit,
    posterDetails: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(
            space = 4.dp,
            alignment = Alignment.Top
        )
    ) {

        RecipePosterCard(
            modifier = Modifier.padding(bottom = 8.dp),
            elevation = elevation,
            content = posterImage,
            cardShape = cardShape,
            isFavourite = isFavourite,
            onCheckChanged = onCheckedChange,
        )

        posterDetails()
    }
}

@Composable
fun RecipePosterCard(
    modifier: Modifier = Modifier,
    elevation: Dp,
    isFavourite: Boolean,
    cardShape: Shape = RoundedCornerShape(16.dp),
    onCheckChanged: (Boolean) -> Unit = {},
    content: @Composable BoxScope.() -> Unit
) {
    Card(
        modifier = modifier,
        elevation = animateDpAsState(targetValue = elevation).value,
        shape = cardShape
    ) {
        Box(modifier = Modifier.clipToBounds()) {

            content()

            Providers(AmbientContentAlpha provides ContentAlpha.high) {
                MarkFavouriteButton(
                    modifier = Modifier.align(Alignment.TopEnd),
                    isFavourite = isFavourite,
                    onCheckChanged = onCheckChanged
                )
            }
        }
    }
}

@Composable
fun MarkFavouriteButton(
    modifier: Modifier = Modifier,
    isFavourite: Boolean,
    onCheckChanged: (Boolean) -> Unit
) {
    IconToggleButton(
        modifier = modifier
            .padding(16.dp)
            .background(
                shape = CircleShape,
                color = colorResource(id = R.color.grey_400_alpha_30)
            ),
        content = {
            Icon(
                imageVector = vectorResource(id = R.drawable.ic_like),
                tint = animateColorAsState(
                    targetValue = colorResource(if (isFavourite) R.color.deep_orange_a200 else R.color.white)
                ).value,
                contentDescription = "Mark As Favourite Recipe"
            )
        },
        checked = isFavourite,
        onCheckedChange = onCheckChanged
    )
}
