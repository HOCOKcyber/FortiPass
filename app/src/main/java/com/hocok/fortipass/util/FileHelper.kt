package com.hocok.fortipass.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.hocok.fortipass.R
import java.io.File
import java.io.IOException

object FileHelper {

    fun checkFileExists(context: Context, uri: Uri): Boolean {
        if ("content" == uri.scheme) {
            try {
                context.contentResolver.openInputStream(uri).use {
                    return it != null
                }
            } catch (e: IOException) {
                return false
            }
        }

        else if ("file" == uri.scheme) {
            return uri.path?.let { File(it).exists() } == true
        }

        return false
    }

    fun openInOtherApp(context: Context, uri: Uri) {
        val intent = createOpenFileIntent(uri)

        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException){
            Toast.makeText(context, context.getString(R.string.not_found_app_json), Toast.LENGTH_SHORT).show()
        }
    }

    fun createOpenFileIntent(uri: Uri): Intent{
        return Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/json")
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
    }

    fun intentCreateFile(): Intent{
        return Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/json"
            putExtra(Intent.EXTRA_TITLE, "fortiPassData.json")
        }
    }

    fun savePersistableUriPermission(context: Context, uri: Uri){
        val contentResolver = context.contentResolver
        val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        contentResolver.takePersistableUriPermission(uri, takeFlags)
    }


}