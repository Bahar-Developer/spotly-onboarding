# 🔦 Spotly Onboarding Library
A dynamic and customizable Jetpack Compose onboarding spotlight library with morphing animations and multi-target support.
-------------------------------
[![Android Studio](https://img.shields.io/badge/Android%20Studio-2024.1+-green.svg)](https://developer.android.com/studio)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0+-blue.svg)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-1.7+-4285F4.svg)](https://developer.android.com/jetpack/compose)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

**Spotly Onboarding** is a modern, lightweight, and highly dynamic onboarding spotlight library for **Jetpack Compose**. It features state-of-the-art **morphing animations** between various target shapes and provides clean state management for complex UI walkthroughs.
 

## ✨ Highlights & Features

- 🔄 **Fluid Shape Morphing**: Smooth spring-based transitions (`Spring.StiffnessLow`) when animating from one shape to another.
- 📐 **Rich Geometric Shape Engine**: Built-in support for multiple shapes:
  - `Circle`, `RoundedRect`, `Oval`, `Capsule`, `CutCornerRect`
  - 🌟 **Special Shapes**: `Star` (customizable points & inner ratio), `Triangle`, and `Polygon` (Hexagon, Octagon, etc.).
- 🎯 **Multi-Target Highlighting**: Highlight one or multiple interactive elements simultaneously per step.
- 🛠️ **Declarative Architecture**: Clean separation between state management (`SpotlyGuideManager`) and custom `Canvas` overlay rendering.
- 🚀 **100% Jetpack Compose Native**: Zero legacy View system dependencies, highly performant, and memory efficient.

---

## 🚀 Getting Started

### 1. State Management Setup

Initialize `SpotlyGuideManager` and define your walkthrough steps using `GuideStep` and `SpotlightShape`:

```kotlin
val manager = remember { SpotlyGuideManager() }

// Define onboarding steps
val steps = listOf(
    GuideStep(
        title = "Welcome to Feature X",
        description = "This star highlight draws attention to key rewards.",
        spotlights = listOf(
            SpotlightShape(
                offsetDp = DpOffset(180.dp, 200.dp),
                shape = TargetShape.Star(radiusDp = 60.dp, points = 5)
            )
        )
    ),
    GuideStep(
        title = "Action Button",
        description = "Morphing seamlessly into a Capsule highlight.",
        spotlights = listOf(
            SpotlightShape(
                offsetDp = DpOffset(180.dp, 400.dp),
                shape = TargetShape.Capsule(widthDp = 140.dp, heightDp = 50.dp)
            )
        )
    )
)

// Start onboarding
LaunchedEffect(Unit) {
    manager.start(steps)
}


2. Render Overlay Screen
Integrate MorphingMultiSpotlightOverlay with your bottom controls layout:
Box(modifier = Modifier.fillMaxSize()) {
    // Your Main Screen Layout
    MainContent()

    // Spotly Onboarding Overlay Layer
    if (manager.isShowing) {
        MorphingMultiSpotlightOverlay(
            spotlights = manager.currentStep?.spotlights ?: emptyList(),
            overlayColor = Color.Black.copy(alpha = 0.7f)
        )

        // Onboarding Navigation & Description Card
        SpotlyBottomContent(
            step = manager.currentStep,
            onNext = { manager.next() },
            onPrevious = { manager.previous() },
            onSkip = { manager.dismiss() }
        )
    }
}


## 📐 Supported Shapes

| Shape Class | Key Parameters | Description |
| :--- | :--- | :--- |
| `TargetShape.Circle` | `radiusDp` | Standard circular highlight |
| `TargetShape.RoundedRect` | `widthDp`, `heightDp`, `cornerRadiusDp` | Rounded rectangle cutout |
| `TargetShape.Capsule` | `widthDp`, `heightDp` | Fully rounded pill-shaped cutout |
| `TargetShape.CutCornerRect` | `widthDp`, `heightDp`, `cutSizeDp` | Modern chamfered corner shape |
| `TargetShape.Star` | `radiusDp`, `points`, `innerRadiusRatio` | Customizable star geometry |
| `TargetShape.Triangle` | `widthDp`, `heightDp` | Directional & play button cutouts |
| `TargetShape.Polygon` | `radiusDp`, `sides` | Regular polygons (Hexagons, etc.) |


LaunchedEffect(Unit) {
    manager.start(steps)
}
