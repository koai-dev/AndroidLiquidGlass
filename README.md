# Liquid Glass (Backdrop)

![frontPhoto](artworks/banner.jpg)

A customizable Liquid Glass effect library for Compose Multiplatform.

## Docs

[![Maven Central](https://img.shields.io/maven-central/v/io.github.kyant0/backdrop)](https://central.sonatype.com/artifact/io.github.kyant0/backdrop)

[Documentation](https://kyant.gitbook.io/backdrop)

## Components

The `backdrop` library includes reusable Liquid Glass components in
`com.kyant.backdrop.components`:

- [LiquidButton](/backdrop/src/commonMain/kotlin/com/kyant/backdrop/components/LiquidButton.kt)
- [LiquidToggle](/backdrop/src/commonMain/kotlin/com/kyant/backdrop/components/LiquidToggle.kt)
- [LiquidSlider](/backdrop/src/commonMain/kotlin/com/kyant/backdrop/components/LiquidSlider.kt)
- [LiquidBottomTabs](/backdrop/src/commonMain/kotlin/com/kyant/backdrop/components/LiquidBottomTabs.kt)
- [LiquidBottomTab](/backdrop/src/commonMain/kotlin/com/kyant/backdrop/components/LiquidBottomTab.kt)

Import the components directly from the library; their gesture, animation, and
highlight helpers are included internally, without a dependency on the catalog app.

```kotlin
import androidx.compose.foundation.text.BasicText
import com.kyant.backdrop.components.LiquidButton

LiquidButton(onClick = { /* Handle click */ }, backdrop = backdrop) {
    BasicText("Continue")
}
```

## Demo

- [Backdrop Catalog](./androidApp/release/androidApp-release.apk)

![Screenshots of Backdrop Catalog](artworks/catalog_app.jpg)
