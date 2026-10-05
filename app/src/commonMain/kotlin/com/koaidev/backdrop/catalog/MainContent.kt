package com.koaidev.backdrop.catalog

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.koaidev.backdrop.catalog.destinations.AdaptiveLuminanceGlassContent
import com.koaidev.backdrop.catalog.destinations.BottomTabsContent
import com.koaidev.backdrop.catalog.destinations.ButtonsContent
import com.koaidev.backdrop.catalog.destinations.ControlCenterContent
import com.koaidev.backdrop.catalog.destinations.DialogContent
import com.koaidev.backdrop.catalog.destinations.GlassPlaygroundContent
import com.koaidev.backdrop.catalog.destinations.HomeContent
import com.koaidev.backdrop.catalog.destinations.LazyScrollContainerContent
import com.koaidev.backdrop.catalog.destinations.LockScreenContent
import com.koaidev.backdrop.catalog.destinations.MagnifierContent
import com.koaidev.backdrop.catalog.destinations.ProgressiveBlurContent
import com.koaidev.backdrop.catalog.destinations.ScrollContainerContent
import com.koaidev.backdrop.catalog.destinations.SliderContent
import com.koaidev.backdrop.catalog.destinations.ToggleContent
import com.koaidev.backdrop.catalog.utils.BackHandler

@Composable
fun MainContent() {
    val isLightTheme = !isSystemInDarkTheme()

    CompositionLocalProvider(
        LocalIndication provides ripple(color = if (isLightTheme) Color.Black else Color.White)
    ) {
        var destination by rememberSaveable { mutableStateOf(CatalogDestination.Home) }

        when (destination) {
            CatalogDestination.Home -> HomeContent(onNavigate = { destination = it })

            CatalogDestination.Buttons -> ButtonsContent()
            CatalogDestination.Toggle -> ToggleContent()
            CatalogDestination.Slider -> SliderContent()
            CatalogDestination.BottomTabs -> BottomTabsContent()
            CatalogDestination.Dialog -> DialogContent()

            CatalogDestination.LockScreen -> LockScreenContent()
            CatalogDestination.ControlCenter -> ControlCenterContent()
            CatalogDestination.Magnifier -> MagnifierContent()

            CatalogDestination.GlassPlayground -> GlassPlaygroundContent()
            CatalogDestination.AdaptiveLuminanceGlass -> AdaptiveLuminanceGlassContent()
            CatalogDestination.ProgressiveBlur -> ProgressiveBlurContent()
            CatalogDestination.ScrollContainer -> ScrollContainerContent()
            CatalogDestination.LazyScrollContainer -> LazyScrollContainerContent()
        }

        BackHandler(destination != CatalogDestination.Home) {
            destination = CatalogDestination.Home
        }
    }
}
