package com.nepreconsultintg.edigital

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.nepreconsultintg.edigital.datastore.createDataStore

fun MainViewController() = ComposeUIViewController {
    App(
        prefs = remember {
            createDataStore()
        }
    )
}