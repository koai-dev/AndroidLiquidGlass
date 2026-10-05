package com.kyant.backdrop.components.internal

import kotlinx.coroutines.delay

internal actual suspend fun awaitFrame() {
    delay(1000L / 60L)
}
