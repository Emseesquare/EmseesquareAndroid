package com.fourteen06.emseesquare.utils

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.fourteen06.emseesquare.BuildConfig
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


fun Fragment.getTmpFileUri(): Uri {
    val tmpFile = File.createTempFile(createUniquePictureName(), ".png").apply {
        createNewFile()
        deleteOnExit()
    }
    return FileProvider.getUriForFile(
        requireContext().applicationContext,
        "${BuildConfig.APPLICATION_ID}.provider",
        tmpFile
    )
}

fun Fragment.createUniquePictureName(): String {
    val sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
    val now = Date()
    val timeStamp = sdf.format(now);
    return "image$timeStamp"
}

fun Fragment.getFileName(uri: Uri): String? {
    var result: String? = null
    if (uri.scheme == "content") {
        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        cursor.use { cursor ->
            if (cursor != null && cursor.moveToFirst()) {
                result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
        }
    }
    if (result == null) {
        result = uri.path
        val cut = result!!.lastIndexOf('/')
        if (cut != -1) {
            result = result?.substring(cut + 1)
        }
    }
    return result
}

fun Fragment.getMimeType(uri: Uri): String? {
    var mimeType: String? = null
    mimeType = if (ContentResolver.SCHEME_CONTENT == uri.scheme) {
        val cr: ContentResolver = requireContext().contentResolver
        cr.getType(uri)
    } else {
        val fileExtension = MimeTypeMap.getFileExtensionFromUrl(
            uri
                .toString()
        )
        MimeTypeMap.getSingleton().getMimeTypeFromExtension(
            fileExtension.lowercase(Locale.getDefault())
        )
    }
    return mimeType
}

