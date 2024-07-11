package com.example.wakeup

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore

fun getImagesURIsFromGallery(context: Context): List<Uri> {
    val imagesURIList = mutableListOf<Uri>()

    val projection = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA)
    val query = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

    val cursor = context.contentResolver.query(query, projection, null, null, null)

    cursor?.use { cursor ->
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumn)
            val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
            imagesURIList.add(imageUri)
        }
    }

    return imagesURIList.reversed()
}
