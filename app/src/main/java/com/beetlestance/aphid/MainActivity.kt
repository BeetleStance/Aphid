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
package com.beetlestance.aphid

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.compose.KEY_ROUTE
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.beetlestance.aphid.common_compose.AndroidIcon
import com.beetlestance.aphid.common_compose.AphidContent
import com.beetlestance.aphid.common_compose.bottomnavigation.CurveCutMenuItem
import com.beetlestance.aphid.common_compose.bottomnavigation.CurveCutNavBar
import com.beetlestance.aphid.common_compose.insets.navigationBarsPadding
import com.beetlestance.aphid.common_compose.utils.composableContent
import com.beetlestance.aphid.feature_chat.Chat
import com.beetlestance.aphid.feature_explore.Explore
import com.beetlestance.aphid.feature_profile.Profile
import dagger.hilt.android.AndroidEntryPoint

/**
 * Use component activity once there is no use of [AppCompatDelegate]
 *
 * [ComponentActivity] is a low level activity class, removing all the nested hierarchies
 * or functionalities
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Draw behind the system bars
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // disabled dark theme temporarily
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> {
                // Night mode is not active, we're using the light theme
            }
            Configuration.UI_MODE_NIGHT_YES -> {
                // Night mode is active, we're using dark theme
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        resources.configuration.uiMode = Configuration.UI_MODE_NIGHT_NO

        setContent {
            AphidContent {
                AphidHome()
            }
        }
    }

    @Composable
    fun AphidHome() {
        val navController = rememberNavController()
        Scaffold(
            bottomBar = {
                CurveCutNavBar(
                    modifier = Modifier.navigationBarsPadding(),
                    backgroundColor = MaterialTheme.colors.surface,
                    fabBackgroundColor = MaterialTheme.colors.primarySurface,
                    selectedItem = navItems.indexOf(Screen.Explore),
                    maxItems = navItems.size,
                    fabIcon = {
                        val resId = navItems.elementAt(selectedId).iconFilled
                        val color = MaterialTheme.colors.surface
                        AndroidIcon(drawableId = resId, tint = color)
                    }
                ) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE)
                    navItems.forEachIndexed { index, screen ->
                        CurveCutMenuItem(
                            content = {
                                val resId = screen.iconOutlined
                                val color = MaterialTheme.colors.background
                                Icon(
                                    imageVector = vectorResource(id = resId),
                                    contentDescription = "SelectedItem",
                                    tint = color
                                )
                            },
                            index = index,
                            selected = currentRoute == screen.route,
                            onClick = {
                                // This if check gives us a "singleTop" behavior where we do not create a
                                // second instance of the composable if we are already on that destination
                                if (currentRoute != screen.route) {
                                    // This is the equivalent to popUpTo the start destination
                                    navController.popBackStack(
                                        navController.graph.startDestination,
                                        false
                                    )
                                    if (screen.route != Screen.Explore.route) {
                                        navController.navigate(screen.route)
                                    }
                                }
                            }
                        )
                    }
                }
            }
        ) { navBarPadding ->
            NavHost(navController, startDestination = Screen.Explore.route) {
                composableContent(Screen.Chat.route) { Chat(paddingValues = navBarPadding) }
                composableContent(Screen.Explore.route) { Explore(paddingValues = navBarPadding) }
                composableContent(Screen.MealPlanner.route) { Dummy() }
                composableContent(Screen.Grocery.route) { Dummy() }
                composableContent(Screen.Profile.route) { Profile(paddingValues = navBarPadding) }
            }
        }
    }

    @Composable
    private fun Dummy() {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(bottom = 124.dp)
                        .padding(horizontal = 16.dp)
                )
            }
        }
    }

    private val navItems = listOf(
        Screen.Chat,
        Screen.Explore,
        Screen.MealPlanner,
        Screen.Grocery,
        Screen.Profile
    )
}
