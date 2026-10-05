package com.koaidev.backdrop.components.internal

import kotlinx.coroutines.android.awaitFrame as awaitAndroidFrame

internal actual suspend fun awaitFrame() {
    awaitAndroidFrame()
}
