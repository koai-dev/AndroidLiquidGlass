# Liquid Glass (Backdrop)

![frontPhoto](artworks/banner.jpg)

A customizable Liquid Glass effect library for Compose Multiplatform.

This fork is maintained by [koai-dev](https://github.com/koai-dev/AndroidLiquidGlass),
based on [Kyant0's original project](https://github.com/Kyant0/AndroidLiquidGlass).

## Dependency

The publication coordinates for this fork are `io.github.koai-dev:backdrop:2.0.1`.
Once this version has been published to Maven Central, add it to your dependencies:

```kotlin
implementation("io.github.koai-dev:backdrop:2.0.1")
```

The Kotlin and Android namespace is `com.koaidev.backdrop`. Maven coordinates retain
the GitHub account's hyphen (`koai-dev`), while source packages use `koaidev`.
Existing imports from `com.kyant.backdrop` must be updated to `com.koaidev.backdrop`.

## Docs

[![Maven Central](https://img.shields.io/maven-central/v/io.github.koai-dev/backdrop)](https://central.sonatype.com/artifact/io.github.koai-dev/backdrop)

[Upstream documentation](https://kyant.gitbook.io/backdrop)

## Components

The `backdrop` library includes reusable Liquid Glass components in
`com.koaidev.backdrop.components`:

- [LiquidButton](/backdrop/src/commonMain/kotlin/com/koaidev/backdrop/components/LiquidButton.kt)
- [LiquidToggle](/backdrop/src/commonMain/kotlin/com/koaidev/backdrop/components/LiquidToggle.kt)
- [LiquidSlider](/backdrop/src/commonMain/kotlin/com/koaidev/backdrop/components/LiquidSlider.kt)
- [LiquidBottomTabs](/backdrop/src/commonMain/kotlin/com/koaidev/backdrop/components/LiquidBottomTabs.kt)
- [LiquidBottomTab](/backdrop/src/commonMain/kotlin/com/koaidev/backdrop/components/LiquidBottomTab.kt)

Import the components directly from the library; their gesture, animation, and
highlight helpers are included internally, without a dependency on the catalog app.

```kotlin
import androidx.compose.foundation.text.BasicText
import com.koaidev.backdrop.components.LiquidButton

LiquidButton(onClick = { /* Handle click */ }, backdrop = backdrop) {
    BasicText("Continue")
}
```

### Customizing component UI

The defaults retain the catalog appearance. Use the component parameters for internal
geometry and styling, and `Modifier` for outer sizing, placement, and padding.

| Component | Layout parameters | Shape and color parameters |
| --- | --- | --- |
| `LiquidButton` | `minHeight`, `contentPadding: PaddingValues`, `horizontalArrangement`, `verticalAlignment` | `shape`, existing `tint` and `surfaceColor` |
| `LiquidBottomTabs` | `height`, `contentPadding: Dp` (uniform inset) | `shape`, `indicatorShape`, `accentColor`, `containerColor`, `indicatorColor` |
| `LiquidBottomTab` | `contentPadding: PaddingValues`, `verticalArrangement`, `horizontalAlignment` | `shape` |
| `LiquidToggle` | `trackSize: DpSize`, `thumbSize: DpSize`, `thumbPadding` (horizontal inset) | `trackShape`, `thumbShape`, `accentColor`, `trackColor`, `thumbColor` |
| `LiquidSlider` | `trackHeight`, `thumbSize: DpSize` | `trackShape`, `thumbShape`, `accentColor`, `trackColor`, `thumbColor` |

`LiquidButton` uses a minimum height of `48.dp`, so taller content can grow the button.
An explicit caller size such as `Modifier.height(40.dp)` takes precedence over that minimum.
The tab indicator and its content backdrop derive their height from the measured bar
height minus twice `contentPadding`; each tab uses an equal share of the remaining width.
The toggle derives thumb travel from its measured track width, thumb width, and padding.
A constrained toggle limits its thumb size to fit the available track.

Unspecified accent, track, container, and indicator colors follow the system light/dark
theme. Use `Color.Transparent` for an explicitly transparent surface. Thumb colors default
to white and fade with the existing glass animation. The animation and optical effect
constants remain internal to keep the public API focused on UI customization.

```kotlin
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

LiquidButton(
    onClick = { /* Handle click */ },
    backdrop = backdrop,
    shape = RoundedCornerShape(16.dp),
    minHeight = 56.dp,
    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp)
) {
    BasicText("Continue")
}

LiquidBottomTabs(
    selectedTabIndex = { selectedIndex },
    onTabSelected = { selectedIndex = it },
    backdrop = backdrop,
    tabsCount = 3,
    height = 80.dp,
    contentPadding = 6.dp,
    shape = RoundedCornerShape(24.dp),
    indicatorShape = RoundedCornerShape(18.dp)
) {
    repeat(3) { index ->
        LiquidBottomTab(
            onClick = { selectedIndex = index },
            shape = RoundedCornerShape(18.dp)
        ) {
            BasicText("Tab ${index + 1}")
        }
    }
}
```

Tab bars and sliders need a bounded, positive width. Tab content must contain exactly
`tabsCount` equally weighted items. Toggle thumb dimensions must fit its configured track,
and horizontal padding must leave room for travel. Slider ranges must be finite and
increasing, with a positive `visibilityThreshold`.

## Publishing to Maven Central

The `backdrop` module publishes all configured Kotlin Multiplatform targets under
`io.github.koai-dev`, with POM metadata pointing to this fork. The publishing plugin
configures source and Javadoc artifacts, and publication signing is enabled.

1. Verify the `io.github.koai-dev` namespace in the
   [Central Portal](https://central.sonatype.com/), following
   [Sonatype's namespace instructions](https://central.sonatype.org/register/namespace/).
2. Provide the Central Portal user-token credentials through
   `ORG_GRADLE_PROJECT_mavenCentralUsername` and
   `ORG_GRADLE_PROJECT_mavenCentralPassword`.
3. For local signing, create a GPG key and set the following in your private
   `~/.gradle/gradle.properties` (replace the executable path and fingerprint):

   ```properties
   signing.gnupg.executable=/opt/homebrew/bin/gpg
   signing.gnupg.keyName=YOUR_FULL_KEY_FINGERPRINT
   ```

   Gradle will use the local GPG keyring and `gpg-agent` for the passphrase.
   Local signing tasks run one at a time. On macOS, install `pinentry-mac` with
   Homebrew and set `pinentry-program /opt/homebrew/bin/pinentry-mac` in your
   private `~/.gnupg/gpg-agent.conf` (adjust the path for your Homebrew install).
   Reload the agent with `gpgconf --reload gpg-agent`; it will show a native
   passphrase dialog when the key needs unlocking. For CI, supply the ASCII-armored key through
   `ORG_GRADLE_PROJECT_signingInMemoryKey` and its password through
   `ORG_GRADLE_PROJECT_signingInMemoryKeyPassword`, when applicable. An in-memory
   key takes precedence over the local keyring configuration.
4. Make the matching public key available on a
   [keyserver supported by Sonatype](https://central.sonatype.org/publish/requirements/gpg/):

   ```shell
   gpg --keyserver hkps://keyserver.ubuntu.com --send-keys YOUR_FULL_KEY_FINGERPRINT
   ```

Use environment variables or your private `~/.gradle/gradle.properties` for these
settings. See the [publishing plugin's credential configuration](https://vanniktech.github.io/gradle-maven-publish-plugin/central/#secrets).

Upload the publications for validation and manual release in the Central Portal:

```shell
bash ./gradlew :backdrop:publishToMavenCentral
```

After the deployment passes validation, publish it from the Central Portal.
For an automatic upload and release, use:

```shell
bash ./gradlew :backdrop:publishAndReleaseToMavenCentral
```

The external `io.github.kyant0:shapes` dependency and its `com.kyant.shapes` package
remain unchanged. Original copyright and Apache 2.0 license notices are retained.

## Demo

- [Backdrop Catalog](./androidApp/release/androidApp-release.apk)

![Screenshots of Backdrop Catalog](artworks/catalog_app.jpg)
