package com.nepreconsultintg.edigital

import androidx.compose.runtime.Composable

@Composable
expect fun CameraScreen(onCodeRead: (code : ByteArray?) -> Unit)