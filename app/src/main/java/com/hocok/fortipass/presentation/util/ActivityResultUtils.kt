package com.hocok.fortipass.presentation.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import com.hocok.fortipass.R
import com.hocok.fortipass.service.ExportService
import com.hocok.fortipass.util.FileHelper
import com.hocok.fortipass.util.IS_CRYPTO_INTENT

class SafeFileContract : ActivityResultContract<Unit, Uri?>() {
    override fun createIntent(context: Context, input: Unit): Intent = FileHelper.intentCreateFile()

    override fun parseResult(resultCode: Int, intent: Intent?) : Uri? {
        if (resultCode != Activity.RESULT_OK) {
            return null
        }
        return intent?.data
    }
}

class SafeFileCallBack(private val isCrypto: Boolean, private val context: Context):
    ActivityResultCallback<Uri?> {
    override fun onActivityResult(result: Uri?) {
        if (result == null) {
            Toast.makeText(context, context.getString(R.string.error_wrong_path), Toast.LENGTH_SHORT).show()
            return
        }

        // Сохранение разрешения на uri
        FileHelper.savePersistableUriPermission(context, result)

        val intent = Intent(context, ExportService::class.java).apply {
            putExtra(IS_CRYPTO_INTENT, isCrypto)
            data = result
        }
        context.startForegroundService(intent)
    }
}