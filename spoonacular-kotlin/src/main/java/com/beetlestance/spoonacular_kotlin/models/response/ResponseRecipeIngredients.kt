package com.beetlestance.spoonacular_kotlin.models.response

import com.squareup.moshi.Json

data class ResponseRecipeIngredients(

    @Json(name = "ingredients")
    val ingredients: List<IngredientsItem?>? = null
) {

    data class IngredientsItem(

        @Json(name = "image")
        val image: String? = null,

        @Json(name = "amount")
        val amount: Amount? = null,

        @Json(name = "name")
        val name: String? = null
    )

    data class Us(

        @Json(name = "unit")
        val unit: String? = null,

        @Json(name = "value")
        val value: Double? = null
    )

    data class Amount(

        @Json(name = "metric")
        val metric: Metric? = null,

        @Json(name = "us")
        val us: Us? = null
    )

    data class Metric(

        @Json(name = "unit")
        val unit: String? = null,

        @Json(name = "value")
        val value: Double? = null
    )
}
