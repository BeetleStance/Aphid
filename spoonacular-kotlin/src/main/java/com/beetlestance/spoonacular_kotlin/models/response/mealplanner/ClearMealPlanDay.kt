package com.beetlestance.spoonacular_kotlin.models.response.mealplanner

import com.squareup.moshi.Json

data class ClearMealPlanDay(

    @Json(name = "status")
    val status: String? = null
)
