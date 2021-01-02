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
package com.beetlestance.spoonacular_kotlin.models.response.recipe

import com.squareup.moshi.Json

data class RecipeTaste(

    @Json(name = "fattiness")
    val fattiness: Int? = null,

    @Json(name = "spiciness")
    val spiciness: Double? = null,

    @Json(name = "saltiness")
    val saltiness: Double? = null,

    @Json(name = "bitterness")
    val bitterness: Double? = null,

    @Json(name = "savoriness")
    val savoriness: Double? = null,

    @Json(name = "sweetness")
    val sweetness: Double? = null,

    @Json(name = "sourness")
    val sourness: Double? = null
)
