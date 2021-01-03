package com.beetlestance.spoonacular_kotlin

import com.beetlestance.spoonacular_kotlin.services.RecipesService
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class SpoonacularServiceTest {

    lateinit var recipesService: RecipesService

    private val api: String? = System.getenv()["TEST_API_KEY"]

    private val spoonacular = Spoonacular(api ?: "")
        .retrofitBuilder()
        .build()

    @Before
    fun setUpService() {
        recipesService = spoonacular.create(RecipesService::class.java)
    }

    @Test
    fun validateApiKey() {
        assertThat(api.isNullOrBlank().not())
    }

}