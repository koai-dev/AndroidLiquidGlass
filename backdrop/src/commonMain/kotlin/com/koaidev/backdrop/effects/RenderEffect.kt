package com.koaidev.backdrop.effects

import androidx.compose.ui.graphics.RenderEffect
import com.koaidev.backdrop.BackdropEffectScope
import com.koaidev.backdrop.RuntimeShader
import com.koaidev.backdrop.internal.RuntimeShaderEffect
import com.koaidev.backdrop.internal.chain
import com.koaidev.backdrop.isRenderEffectSupported
import com.koaidev.backdrop.isRuntimeShaderSupported
import org.intellij.lang.annotations.Language
import kotlin.contracts.ExperimentalContracts

fun BackdropEffectScope.effect(effect: RenderEffect) {
    if (!isRenderEffectSupported()) return

    renderEffect = renderEffect.chain(effect)
}

@OptIn(ExperimentalContracts::class)
fun BackdropEffectScope.runtimeShaderEffect(
    key: String,
    @Language("AGSL") shaderString: String,
    uniformShaderName: String,
    block: RuntimeShader.() -> Unit
) {
    if (!isRuntimeShaderSupported()) return

    val effect =
        RuntimeShaderEffect(
            runtimeShader = obtainRuntimeShader(key, shaderString).apply(block),
            uniformShaderName = uniformShaderName
        )
    renderEffect = renderEffect.chain(effect)
}
