package com.hocok.fortipass.service

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.ServiceCompat
import com.hocok.fortipass.R
import com.hocok.fortipass.domain.model.toNotCryptoExport
import com.hocok.fortipass.domain.repository.AccountRepository
import com.hocok.fortipass.domain.repository.DataStoreRepository
import com.hocok.fortipass.util.FileHelper
import com.hocok.fortipass.util.IS_CRYPTO_INTENT
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.io.FileOutputStream
import javax.inject.Inject

private const val CHANNEL_ID = "export_data_id"
private const val END_NOTIFY_ID = 1
private const val TAG = "ExportService"

@AndroidEntryPoint
class ExportService: Service() {
    @Inject
    lateinit var dbRep: AccountRepository

    @Inject
    lateinit var dataStoreRep: DataStoreRepository

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        val notifyManger = getSystemService(NotificationManager::class.java)
        val channelNotify = NotificationChannel(
            CHANNEL_ID,
            getString(R.string.export_data_notify_title),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notifyManger.createNotificationChannel(channelNotify)

        ServiceCompat.startForeground(
            this,
            100,
            createNotification(R.string.open_type_export_data),
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            } else {
                0
            },
        )

        CoroutineScope(Dispatchers.IO).launch {
            val endNotify = try {
                val isCrypto =  intent!!.getBooleanExtra(IS_CRYPTO_INTENT, false)
                val uri = intent.data!!

                dataStoreRep.saveUri(uri)

                val jsonData = getJsonData(isCrypto)

                contentResolver.openFileDescriptor(uri, "w")?.use {
                    FileOutputStream(it.fileDescriptor).use { streamOutput ->
                        streamOutput.write(
                            "{\"accounts\":${jsonData.first},\"directory\":${jsonData.second}}".toByteArray()
                        )
                    }
                }

                val intentToFile = FileHelper.createOpenFileIntent(uri)
                val pendingIntent = PendingIntent.getActivity(this@ExportService, 0, intentToFile, PendingIntent.FLAG_IMMUTABLE)

                createNotification(R.string.export_data_success, pendingIntent)
            } catch (e: Exception) {
                e.message?.let { Log.e(TAG, it) }
                createNotification(R.string.export_data_fail)
            }

            if (ActivityCompat.checkSelfPermission(
                    this@ExportService,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED) notifyManger.notify(END_NOTIFY_ID, endNotify)

            stopSelf()
        }

        return START_NOT_STICKY
    }


    private fun createNotification(textId: Int, pendingIntent: PendingIntent? = null): Notification{
        return Notification.Builder(this, CHANNEL_ID).apply {
            setContentTitle(getString(R.string.export_data_notify_title))
            setContentText(getString(textId))
            setSmallIcon(R.mipmap.ic_launcher)
            setContentIntent(pendingIntent)
            setAutoCancel(true)
        }.build()
    }

    private suspend fun getJsonData(isCrypto: Boolean): Pair<String, String>{
        val accountList = dbRep.getAllAccount().first().map { if (isCrypto) it else it.toNotCryptoExport() }
        val directoryList = dbRep.getAllDirectory().first()

        val accountJson = Json.encodeToString(accountList)
        val directoryJson = Json.encodeToString(directoryList)

        return Pair(accountJson, directoryJson)
    }
}