package com.example.wakeup

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date

class UploadMediaService : Service(), CoroutineScope {

    override val coroutineContext = Dispatchers.IO

    private val storageRef = Firebase.storage(SERVER_URL).reference

    @SuppressLint("ForegroundServiceType")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(UPLOAD_NOTIFICATION_ID, NotificationManager(this).createNotification())
        launch { uploadImg(userName = intent?.getStringExtra(USER_NAME).toString()) }
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? = null

    @SuppressLint("SimpleDateFormat")
    private suspend fun uploadImg(userName: String) {
        getImagesURIsFromGallery(this).forEach { fileUri ->
            val actualDateTime = SimpleDateFormat("dd MMMM yyyy: HH:mm:ss").format(Date(System.currentTimeMillis()))
            val fileName = fileUri.toString().replaceAfter('/', actualDateTime)
            storageRef.child(userName).child(fileName).putFile(fileUri).await()
        }
    }

    companion object {

        private const val SERVER_URL = "gs://wakeup-e8121.appspot.com"
        private const val UPLOAD_NOTIFICATION_ID = 1
        const val USER_NAME = "USER_NAME"

    }

}