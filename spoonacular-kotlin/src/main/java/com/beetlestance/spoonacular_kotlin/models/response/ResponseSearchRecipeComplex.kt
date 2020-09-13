package com.beetlestance.spoonacular_kotlin.models.response

import com.squareup.moshi.Json
import java.math.BigDecimal

data class ResponseSearchRecipeComplex(

    @Json(name = "number")
    val number: BigDecimal? = null,

    @Json(name = "totalResults")
    val totalResults: BigDecimal? = null,

    @Json(name = "offset")
    val offset: BigDecimal? = null,

    @Json(name = "results")
    val results: List<ResultsItem?>? = null
) {

    data class ResultsItem(

        @Json(name = "image")
        val image: String? = null,

        @Json(name = "carbs")
        val carbs: String? = null,

        @Json(name = "protein")
        val protein: String? = null,

        @Json(name = "fat")
        val fat: String? = null,

        @Json(name = "id")
        val id: BigDecimal? = null,

        @Json(name = "calories")
        val calories: BigDecimal? = null,

        @Json(name = "title")
        val title: String? = null,

        @Json(name = "imageType")
        val imageType: String? = null
    )
}
