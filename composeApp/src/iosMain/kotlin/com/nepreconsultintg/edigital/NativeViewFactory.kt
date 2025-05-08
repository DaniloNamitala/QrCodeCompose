package com.nepreconsultintg.edigital

import platform.UIKit.UIViewController

interface NativeViewFactory {

    fun createQRCodeScannerView(onCodeRead: (ByteArray?) -> Unit): UIViewController

}