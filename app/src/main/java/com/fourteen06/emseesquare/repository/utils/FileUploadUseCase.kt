package com.fourteen06.emseesquare.repository.utils

import android.app.Application
import android.net.Uri
import com.fourteen06.emseesquare.R
import com.fourteen06.emseesquare.models.AttachmentType
import com.fourteen06.emseesquare.utils.Resource
import com.fourteen06.emseesquare.utils.getMimeType
import com.google.firebase.FirebaseException
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class FileUploadUseCase @Inject constructor(
    private val firebaseStorage: FirebaseStorage,
    private val applicationContext: Application
) {
    operator fun invoke(file: Uri, storageLocation: String): Flow<Resource<AttachmentType>> = flow {
        emit(Resource.Loading())
        try {
            val ref = firebaseStorage.getReference(storageLocation)
            ref.putFile(file).await()
            val downloadUrl = ref.downloadUrl.await()
            val mime = file.getMimeType(applicationContext)
            val attachmentType = when {
                mime?.lowercase(Locale.getDefault())
                    ?.contains("image") == true -> AttachmentType.Image(
                    downloadUrl.toString()
                )
                mime?.lowercase(Locale.getDefault())
                    ?.contains("pdf") == true -> AttachmentType.File(
                    downloadUrl.toString()
                )
                else -> throw IllegalStateException(applicationContext.getString(R.string.invalid_file_type))
            }
            emit(Resource.Success(attachmentType))
        } catch (e: FirebaseException) {
            emit(Resource.Error(e.message.toString()))
        } catch (e: IllegalStateException) {
            emit(Resource.Error(e.message.toString()))
        }
    }

}

