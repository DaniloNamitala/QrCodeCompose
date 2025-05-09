package com.nepreconsultintg.edigital.screens

import androidx.compose.runtime.Composable

@Composable
expect fun CameraScreen(onCodeRead: (code : ByteArray?) -> Unit)