package com.rekognizevote.ui.screens.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rekognizevote.core.Result
import com.rekognizevote.core.utils.ImageUtils
import com.rekognizevote.domain.repository.VoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val voteRepository: VoteRepository,
    private val okHttpClient: OkHttpClient
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(CameraUiState())
    val uiState: StateFlow<CameraUiState> = _uiState
    
    fun uploadImage(imageFile: File) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isUploading = true, error = null)
            
            try {
                // Comprimir imagem
                val compressedFile = ImageUtils.compressImage(imageFile) ?: imageFile
                
                // Obter URL pré-assinada
                when (val urlResult = voteRepository.getPresignedUrl()) {
                    is Result.Success -> {
                        val presignedUrl = urlResult.data
                        
                        // Upload para S3
                        val requestBody = compressedFile.asRequestBody("image/jpeg".toMediaType())
                        val request = Request.Builder()
                            .url(presignedUrl.uploadUrl)
                            .put(requestBody)
                            .build()
                        
                        val response = okHttpClient.newCall(request).execute()
                        
                        if (response.isSuccessful) {
                            _uiState.value = _uiState.value.copy(
                                isUploading = false,
                                imageUrl = presignedUrl.imageUrl
                            )
                        } else {
                            _uiState.value = _uiState.value.copy(
                                isUploading = false,
                                error = "Erro no upload: ${response.message}"
                            )
                        }
                    }
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isUploading = false,
                            error = urlResult.exception.message
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isUploading = false,
                    error = e.message
                )
            }
        }
    }
    
    fun setError(message: String) {
        _uiState.value = _uiState.value.copy(error = message)
    }
}

data class CameraUiState(
    val isUploading: Boolean = false,
    val imageUrl: String? = null,
    val error: String? = null
)