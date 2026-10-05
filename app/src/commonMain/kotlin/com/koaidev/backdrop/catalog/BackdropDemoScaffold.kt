package com.koaidev.backdrop.catalog

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.koaidev.backdrop.backdrops.LayerBackdrop

@Composable
expect fun BackdropDemoScaffold(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.(backdrop: LayerBackdrop) -> Unit
)
