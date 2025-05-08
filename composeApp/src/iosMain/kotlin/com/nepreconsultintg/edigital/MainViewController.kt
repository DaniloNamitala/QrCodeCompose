package com.nepreconsultintg.edigital
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.window.ComposeUIViewController

val LocalNativeViewFactory = staticCompositionLocalOf<NativeViewFactory> {
    error("No view factory provided.")
}

fun MainViewController(
    nativeViewFactory: NativeViewFactory
) = ComposeUIViewController {
    CompositionLocalProvider(LocalNativeViewFactory provides nativeViewFactory) {
        App()
    }
}