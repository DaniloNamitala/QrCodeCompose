package com.nepreconsultintg.edigital

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import kotlin.math.max
import kotlin.math.min

@OptIn(ExperimentalStdlibApi::class)
@Composable
fun Home(navHostController: NavHostController) {
    val factory = rememberPermissionsControllerFactory()
    val controller = remember(factory) {
        factory.createPermissionsController()
    }
    var openDialog  by remember { mutableStateOf(true) }

    BindEffect(controller)

    val viewModel = viewModel {
        PermissionsViewModel(controller)
    }

    var code by remember { mutableStateOf<ByteArray?>(null) }
    code = navHostController.currentBackStackEntry?.savedStateHandle?.get("qrcode")

    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (viewModel.state == PermissionState.DeniedAlways) {
                Text("Permission was permanently declined.")
                Button(onClick = {
                    controller.openAppSettings()
                }) {
                    Text("Open app settings")
                }
            } else {
                Button(onClick = {
                    if (viewModel.state != PermissionState.Granted)
                        viewModel.provideOrRequestRecordAudioPermission()
                    if (viewModel.state == PermissionState.Granted) {
                        navHostController.navigate(ScannerScreens.QrCode.name)
                        code = null
                        openDialog = true
                    }
                }) {
                    Text("Scan QR Code")
                }
                if (code != null && openDialog) {
                    val strCode = code!!.toHexString().uppercase()
                    AlertDialog(
                        onDismissRequest = {},
                        title = { Text("QrCode") },
                        text = { Text(strCode.substring(0, min(strCode.length, 100)) + (if (strCode.length > 100) "[...]" else "")) },
                        buttons = {
                            Row(
                                modifier = Modifier.padding(all = 8.dp),
                                horizontalArrangement = Arrangement. Center,
                            ) {
                                Button(
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = {
                                    openDialog = false;
                                }) {
                                    Text("OK")
                                }
                            }
                        }
                    )
                }
            }
        }
    }

}