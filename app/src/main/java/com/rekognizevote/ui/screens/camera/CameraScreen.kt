package com.rekognizevote.ui.screens.camera

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rekognizevote.ui.components.CameraPreview
import com.rekognizevote.ui.components.LoadingScreen

@Composable
fun CameraScreen(
    onImageCaptured: (String) -> Unit,
    onBack: () -> Unit,
    viewModel: CameraViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    when {
        uiState.isUploading -> {
            LoadingScreen("Processando imagem...")
        }
        
        uiState.imageUrl != null -> {
            LaunchedEffect(uiState.imageUrl) {
                onImageCaptured(uiState.imageUrl)
            }
        }
        
        else -> {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    CameraPreview(
                        onImageCaptured = { file ->
                            viewModel.uploadImage(file)
                        },
                        onError = { exception ->
                            viewModel.setError(exception.message ?: "Erro na câmera")
                        }
                    )
                }
                
                if (uiState.error != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = uiState.error,
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
                
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Cancelar")
                }
            }
        }
    }
}