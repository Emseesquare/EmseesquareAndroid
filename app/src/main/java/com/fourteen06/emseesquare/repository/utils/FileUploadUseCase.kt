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
    operator fun invoke(
        file: Uri,
        storageLocation: String,
        exclusiveFile: ExclusiveFile = ExclusiveFile.ALL
    ): Flow<Resource<AttachmentType>> =
        flow {
            emit(Resource.Loading())
            try {
                val mime = file.getMimeType(applicationContext)
                val isImage = when {
                    mime?.lowercase(Locale.getDefault())
                        ?.contains("image") == true -> true
                    mime?.lowercase(Locale.getDefault())
                        ?.contains("pdf") == true -> false
                    else -> throw IllegalStateException(applicationContext.getString(R.string.invalid_file_type))
                }
                val ref = firebaseStorage.getReference(storageLocation)

                when (exclusiveFile) {
                    ExclusiveFile.ALL -> {
                        ref.putFile(file).await()
                    }
                    ExclusiveFile.PDF_ONLY -> {
                        if (!isImage) {
                            ref.putFile(file).await()
                        } else {
                            throw  IllegalStateException("Only Pdf files are acceptable.")
                        }
                    }
                    ExclusiveFile.IMAGE_ONLY -> {
                        if (isImage) {
                            ref.putFile(file).await()
                        } else {
                            throw  IllegalStateException("Only Images are acceptable.")
                        }
                    }
                }
                val downloadUrl = ref.downloadUrl.await()
                val attachmentType=if(isImage){
                    AttachmentType.Image(imageUrl = downloadUrl.toString())
                }else{
                    AttachmentType.File(fileUrl = downloadUrl.toString())
                }
                emit(Resource.Success(attachmentType))
            } catch (e: FirebaseException) {
                emit(Resource.Error(e.message.toString()))
            } catch (e: IllegalStateException) {
                emit(Resource.Error(e.message.toString()))
            }
        }

    companion object {
        enum class ExclusiveFile {
            ALL, PDF_ONLY, IMAGE_ONLY
        }
    }
}

