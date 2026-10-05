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
   Unlock the key locally before a non-interactive publish if the agent has not
   cached its passphrase. For CI, supply the ASCII-armored key through
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
