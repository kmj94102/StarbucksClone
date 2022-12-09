package com.example.starbucksclone.view.main.pay.registration

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.starbucksclone.ui.theme.DarkGray
import com.example.starbucksclone.ui.theme.White
import com.example.starbucksclone.util.getTextStyle
import com.example.starbucksclone.util.toast
import com.google.zxing.BarcodeFormat

@Composable
fun BarcodeBottomSheetContents(
    decodedListener: (String) -> Unit,
    errorListener: () -> Unit
){
    val context = LocalContext.current
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED
        )
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCameraPermission = granted
        }
    )
    LaunchedEffect(key1 = true) {
        launcher.launch(Manifest.permission.CAMERA)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.8f)
            .background(White)
    ) {

        Box(
            modifier = Modifier
                .padding(top = 7.dp)
                .size(67.dp, 4.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(DarkGray)
                .align(Alignment.CenterHorizontally)
        )

        Text(
            text = "바코드 인식하기",
            style = getTextStyle(16, true),
            modifier = Modifier
                .padding(top = 35.dp)
                .align(Alignment.CenterHorizontally)
        )

        if (hasCameraPermission) {
            AndroidView(
                factory = {
                    val scannerView = CodeScannerView(it)
                    val scanner = CodeScanner(it, scannerView).apply {
                        camera = CodeScanner.CAMERA_BACK
                        formats = listOf(BarcodeFormat.CODE_128)
                        autoFocusMode = AutoFocusMode.SAFE
                        scanMode = ScanMode.SINGLE
                        isAutoFocusEnabled = true
                        isFlashEnabled = false
                        decodeCallback = DecodeCallback { result ->
                            decodedListener(result.text)
                        }
                        errorCallback = ErrorCallback { e ->
                            e.printStackTrace()
                            errorListener()
                        }
                    }

                    scanner.startPreview()
                    scannerView
                },
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}