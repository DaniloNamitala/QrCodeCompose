package com.nepreconsultintg.edigital

import android.graphics.Rect
import androidx.camera.core.ImageAnalysis
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toComposeRect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode

@OptIn(ExperimentalStdlibApi::class)
@Composable
actual fun CameraScreen(onCodeRead: (code : ByteArray?) -> Unit) {
    var barcode by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    var boundingRect by remember { mutableStateOf<Rect?>(null) }
    val cameraController = remember {
        LifecycleCameraController(context)
    }
    var found : Boolean = false
    AndroidView(
        modifier = Modifier
            .fillMaxSize(),
        factory = { ctx ->
            PreviewView(ctx).apply {
                val options = BarcodeScannerOptions.Builder()
                    .setBarcodeFormats(
                        Barcode.FORMAT_QR_CODE,
                        Barcode.TYPE_DRIVER_LICENSE
                    )
                    .build()
                val barcodeScanner = BarcodeScanning.getClient(options)
                cameraController.setImageAnalysisAnalyzer(
                    ContextCompat.getMainExecutor(ctx),
                    MlKitAnalyzer(
                        listOf(barcodeScanner),
                        ImageAnalysis.COORDINATE_SYSTEM_VIEW_REFERENCED,
                        ContextCompat.getMainExecutor(ctx)
                    ) { result: MlKitAnalyzer.Result? ->
                        val barcodeResults = result?.getValue(barcodeScanner)
                        if (!barcodeResults.isNullOrEmpty()) {
                            barcode = barcodeResults.first().rawBytes?.toHexString()
                            boundingRect = barcodeResults.first().boundingBox
                            if (!found) {
                                found = true
                                onCodeRead(barcodeResults.first().rawBytes)
                            }
                        } else {
                            barcode = null;
                            boundingRect = null;
                        }
                    }
                )
                cameraController.bindToLifecycle(lifecycleOwner)
                this.controller = cameraController
            }
        }
    )

    if (barcode != null) {
        DrawRectangle(rect = boundingRect)
    }

}

@Composable
fun DrawRectangle(rect: Rect?) {
    // Convert the Android Rect to a Compose Rect
    val composeRect = rect?.toComposeRect()

    // Draw the rectangle on a Canvas if the rect is not null
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        composeRect?.let {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                drawRect(
                    color = Color.Red,
                    topLeft = Offset(it.left, it.top), // Set the top-left position
                    size = Size(it.width, it.height), // Set the size of the rectangle
                    style = Stroke(width = 5f) // Use a stroke style with a width of 5f
                )
            }
        }
    }

}