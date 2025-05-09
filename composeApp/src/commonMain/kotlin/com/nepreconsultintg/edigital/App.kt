package com.nepreconsultintg.edigital

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nepreconsultintg.edigital.dependences.appModules
import com.nepreconsultintg.edigital.screens.Login

import edigitalmineracao.composeapp.generated.resources.Res
import edigitalmineracao.composeapp.generated.resources.scr_login
import edigitalmineracao.composeapp.generated.resources.scr_main_name
import edigitalmineracao.composeapp.generated.resources.scr_qrcode_name
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.dsl.module

enum class ScannerScreens(val title: StringResource) {
    Start(title = Res.string.scr_main_name),
    QrCode(title = Res.string.scr_qrcode_name),
    Login(title = Res.string.scr_login)

}

@Composable
@Preview
fun App(
    navController: NavHostController = rememberNavController(),
    prefs: DataStore<Preferences>
) {
    KoinApplication(application = {
        modules(
            appModules,
            module { single { prefs } }
        )
    }) {
        NavHost(
            navController = navController,
            startDestination = ScannerScreens.Start.name,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            composable(route = ScannerScreens.Start.name) {
                Home(navController)
            }
            composable(route = ScannerScreens.Login.name) {
                Login(navController)
            }
            composable(route = ScannerScreens.QrCode.name) {
                CameraScreen { code ->
                    navController.previousBackStackEntry?.savedStateHandle?.set("qrcode", code)
                    navController.popBackStack()
                }
            }
        }
    }
}