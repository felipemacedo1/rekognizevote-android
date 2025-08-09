package com.rekognizevote.ui.screens.vote

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
// Usando emoji alternativo
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rekognizevote.core.Result
import com.rekognizevote.domain.model.Candidate
import com.rekognizevote.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoteScreen(
    pollId: String,
    onNavigateBack: () -> Unit,
    onVoteSuccess: () -> Unit,
    viewModel: VoteViewModel = hiltViewModel()
) {
    val pollState by viewModel.pollState.collectAsState()
    val voteState by viewModel.voteState.collectAsState()
    var selectedCandidate by remember { mutableStateOf<Candidate?>(null) }
    var showCamera by remember { mutableStateOf(false) }
    
    LaunchedEffect(pollId) {
        viewModel.loadPoll(pollId)
    }
    
    LaunchedEffect(voteState) {
        if (voteState is Result.Success) {
            onVoteSuccess()
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        TopAppBar(
            title = {
                Text(
                    text = "Votar",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontFamily = MontserratFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                )
            },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Voltar",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Primary
            )
        )
        
        when (val state = pollState) {
            is Result.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Action)
                }
            }
            is Result.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Erro ao carregar enquete",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { viewModel.loadPoll(pollId) }
                        ) {
                            Text("Tentar novamente")
                        }
                    }
                }
            }
            is Result.Success -> {
                VoteContent(
                    poll = state.data,
                    selectedCandidate = selectedCandidate,
                    onCandidateSelected = { selectedCandidate = it },
                    onConfirmVote = { showCamera = true },
                    voteState = voteState
                )
            }
        }
    }
    
    // Camera para captura facial
    if (showCamera) {
        CameraScreen(
            onImageCaptured = { imageUri ->
                selectedCandidate?.let { candidate ->
                    viewModel.submitVote(pollId, candidate.id, imageUri)
                }
                showCamera = false
            },
            onDismiss = { showCamera = false }
        )
    }
}

@Composable
private fun VoteContent(
    poll: com.rekognizevote.domain.model.Poll,
    selectedCandidate: Candidate?,
    onCandidateSelected: (Candidate) -> Unit,
    onConfirmVote: () -> Unit,
    voteState: Result<com.rekognizevote.domain.model.Vote>?
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = poll.title,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontFamily = MontserratFontFamily,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Selecione um candidato para votar:",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = TextSecondary
                        )
                    )
                }
            }
        }
        
        items(poll.candidates) { candidate ->
            CandidateVoteCard(
                candidate = candidate,
                isSelected = selectedCandidate?.id == candidate.id,
                onSelect = { onCandidateSelected(candidate) }
            )
        }
        
        item {
            Spacer(modifier = Modifier.height(16.dp))
            
            // Botão de confirmação
            Button(
                onClick = onConfirmVote,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Action
                ),
                shape = RoundedCornerShape(12.dp),
                enabled = selectedCandidate != null && voteState !is Result.Loading
            ) {
                if (voteState is Result.Loading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "📷",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Confirmar com Reconhecimento Facial",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontFamily = MontserratFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        )
                    }
                }
            }
            
            // Erro
            if (voteState is Result.Error) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Error.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = voteState.exception.message ?: "Erro ao processar voto",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Error
                        ),
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CandidateVoteCard(
    candidate: Candidate,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Action.copy(alpha = 0.1f) 
                           else MaterialTheme.colorScheme.surface
        ),
        border = if (isSelected) CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(Action)
        ) else null
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar placeholder
            Surface(
                modifier = Modifier.size(56.dp),
                color = Primary.copy(alpha = 0.1f),
                shape = RoundedCornerShape(28.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = candidate.name.first().toString(),
                        style = MaterialTheme.typography.headlineSmall.copy(
                            color = Primary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = candidate.name,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium,
                        color = if (isSelected) Action else MaterialTheme.colorScheme.onSurface
                    )
                )
                
                candidate.description?.let { description ->
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextSecondary
                        )
                    )
                }
            }
            
            if (isSelected) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = "Selecionado",
                    tint = Action,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun CameraScreen(
    onImageCaptured: (String) -> Unit,
    onDismiss: () -> Unit
) {
    // TODO: Implementar CameraX
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "📷",
                style = MaterialTheme.typography.displayLarge,
                color = Color.White
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Posicione seu rosto na câmera",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Row {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Gray
                    )
                ) {
                    Text("Cancelar")
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Button(
                    onClick = { 
                        // Simular captura
                        onImageCaptured("mock_image_uri")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Action
                    )
                ) {
                    Text("Capturar")
                }
            }
        }
    }
}