package com.nepreconsultintg.edigital

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.UIKitViewController
import cocoapods.GoogleMLKit.*
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreImage.provideImageData

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun CameraScreen(onCodeRead: (code : ByteArray?) -> Unit) {
    val factory = LocalNativeViewFactory.current
    val options = MLKBarcodeScannerOptions(MLKBarcodeFormatQRCode or MLKBarcodeFormatPDF417 or MLKBarcodeFormatDataMatrix)
    val scanner = MLKBarcodeScanner.barcodeScannerWithOptions(options)

    private fun RealDeviceCamera(
        camera: AVCaptureDevice,
        onQrCodeScanned: (String) -> Boolean
    )
    {
        val capturePhotoOutput = remember { AVCapturePhotoOutput() }
        var actualOrientation by remember {
            mutableStateOf(
                AVCaptureVideoOrientationPortrait
            )
        }
        val captureSession: AVCaptureSession = remember {
            AVCaptureSession().also { captureSession ->
                captureSession.sessionPreset = AVCaptureSessionPresetPhoto
                val captureDeviceInput: AVCaptureDeviceInput =
                    deviceInputWithDevice(device = camera, error = null)!!
                captureSession.addInput(captureDeviceInput)
                captureSession.addOutput(capturePhotoOutput)

                //Initialize an AVCaptureMetadataOutput object and set it as the output device to the capture session.
                val metadataOutput = AVCaptureMetadataOutput()
                if (captureSession.canAddOutput(metadataOutput)) {
                    captureSession.addOutput(metadataOutput)
                    metadataOutput.setSampleBufferDelegate(objectsDelegate = object : NSObject(),
                        AVCaptureVideoDataOutputSampleBufferDelegateProtocol {
                        override fun captureOutput(
                            output : AVCaptureOutput,
                            buffer: CMSampleBufferRefVar,
                            connection: AVCaptureConnection
                        ) {
                            val image = VisionImage
                            scanner.processImage(

                            )
                        }

                    }, queue = dispatch_get_main_queue())

                    metadataOutput.metadataObjectTypes = listOf(AVMetadataObjectTypeQRCode)
                    /*
                    Commenting this line as this provides support for many ISO Image formats.
                    Later if needed to extend QR scanner to support other formats comment this line and uncomment the above line.
                     */
//                metadataOutput.metadataObjectTypes = metadataOutput.availableMetadataObjectTypes()
                }
            }
        }
        val cameraPreviewLayer = remember {
            AVCaptureVideoPreviewLayer(session = captureSession)
        }

        DisposableEffect(Unit) {
            class OrientationListener : NSObject() {
                @Suppress("UNUSED_PARAMETER")
                @ObjCAction
                fun orientationDidChange(arg: NSNotification) {
                    val cameraConnection = cameraPreviewLayer.connection
                    if (cameraConnection != null) {
                        actualOrientation = when (UIDevice.currentDevice.orientation) {
                            UIDeviceOrientation.UIDeviceOrientationPortrait ->
                                AVCaptureVideoOrientationPortrait

                            UIDeviceOrientation.UIDeviceOrientationLandscapeLeft ->
                                AVCaptureVideoOrientationLandscapeRight

                            UIDeviceOrientation.UIDeviceOrientationLandscapeRight ->
                                AVCaptureVideoOrientationLandscapeLeft

                            UIDeviceOrientation.UIDeviceOrientationPortraitUpsideDown ->
                                AVCaptureVideoOrientationPortrait

                            else -> cameraConnection.videoOrientation
                        }
                        cameraConnection.videoOrientation = actualOrientation
                    }
                    capturePhotoOutput.connectionWithMediaType(AVMediaTypeVideo)
                        ?.videoOrientation = actualOrientation
                }
            }

            val listener = OrientationListener()
            val notificationName = UIDeviceOrientationDidChangeNotification
            NSNotificationCenter.defaultCenter.addObserver(
                observer = listener,
                selector = NSSelectorFromString(
                    OrientationListener::orientationDidChange.name + ":"
                ),
                name = notificationName,
                `object` = null
            )
            onDispose {
                NSNotificationCenter.defaultCenter.removeObserver(
                    observer = listener,
                    name = notificationName,
                    `object` = null
                )
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            UIKitView(
                modifier = Modifier.fillMaxSize(),
                factory = {
                    val cameraContainer = UIView()
                    cameraContainer.layer.addSublayer(cameraPreviewLayer)
                    cameraPreviewLayer.videoGravity = AVLayerVideoGravityResizeAspectFill
                    captureSession.startRunning()
                    cameraContainer
                },
                onResize = { view: UIView, rect: CValue<CGRect> ->
                    CATransaction.begin()
                    CATransaction.setValue(true, kCATransactionDisableActions)
                    view.layer.setFrame(rect)
                    cameraPreviewLayer.setFrame(rect)
                    CATransaction.commit()
                },
            )
        }
    }

}