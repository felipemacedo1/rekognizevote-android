package com.rekognizevote.ui.screens.polls

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rekognizevote.core.Constants
import com.rekognizevote.core.Result
import com.rekognizevote.domain.model.Candidate
import com.rekognizevote.domain.model.Poll
import com.rekognizevote.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PollDetailsScreen(
    pollId: String,
    onNavigateBack: () -> Unit,
    onNavigateToVote: (String) -> Unit,
    onNavigateToResults: (String) -> Unit,
    viewModel: PollDetailsViewModel = hiltViewModel()
) {
    val pollState by viewModel.pollState.collectAsState()
    var accessCode by remember { mutableStateOf("") }
    var showAccessDialog by remember { mutableStateOf(false) }
    
    LaunchedEffect(pollId) {
        viewModel.loadPoll(pollId)
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
                    text = "Detalhes da Enquete",
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
                PollDetailsContent(
                    poll = state.data,
                    onVoteClick = { 
                        if (state.data.isPrivate && !showAccessDialog) {
                            showAccessDialog = true
                        } else {
                            onNavigateToVote(pollId)
                        }
                    },
                    onResultsClick = { onNavigateToResults(pollId) }
                )
            }
        }
    }
    
    // Dialog para código de acesso
    if (showAccessDialog) {
        AlertDialog(
            onDismissRequest = { showAccessDialog = false },
            title = {
                Text(
                    text = "Enquete Privada",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontFamily = MontserratFontFamily,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            },
            text = {
                Column {
                    Text(
                        text = "Esta enquete é privada. Digite o código de acesso:",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = accessCode,
                        onValueChange = { accessCode = it },
                        label = { Text("Código de acesso") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.validateAccessCode(pollId, accessCode)
                        showAccessDialog = false
                        onNavigateToVote(pollId)
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAccessDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun PollDetailsContent(
    poll: Poll,
    onVoteClick: () -> Unit,
    onResultsClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Título e descrição
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
                        text = poll.description,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = TextSecondary
                        )
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Total de votos: ${poll.totalVotes}",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium
                            )
                        )
                        
                        val statusColor = when (poll.status) {
                            Constants.POLL_STATUS_ACTIVE -> Success
                            Constants.POLL_STATUS_UPCOMING -> Action
                            Constants.POLL_STATUS_CLOSED -> TextSecondary
                            else -> TextSecondary
                        }
                        
                        Surface(
                            color = statusColor.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = when (poll.status) {
                                    Constants.POLL_STATUS_ACTIVE -> "Ativa"
                                    Constants.POLL_STATUS_UPCOMING -> "Em breve"
                                    Constants.POLL_STATUS_CLOSED -> "Encerrada"
                                    else -> "Desconhecido"
                                },
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = statusColor
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
        
        item {
            Text(
                text = "Candidatos",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontFamily = MontserratFontFamily,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
        
        items(poll.candidates) { candidate ->
            CandidateCard(candidate = candidate)
        }
        
        item {
            Spacer(modifier = Modifier.height(16.dp))
            
            // Botões de ação
            when (poll.status) {
                Constants.POLL_STATUS_ACTIVE -> {
                    if (!poll.hasUserVoted) {
                        Button(
                            onClick = onVoteClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Action
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Votar",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontFamily = MontserratFontFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            )
                        }
                    } else {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = Success.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Você já votou nesta enquete",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Success,
                                    fontWeight = FontWeight.Medium
                                ),
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
                Constants.POLL_STATUS_CLOSED -> {
                    Button(
                        onClick = onResultsClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Primary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Ver Resultados",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontFamily = MontserratFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        )
                    }
                }
                else -> {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = TextSecondary.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Enquete ainda não iniciada",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = TextSecondary,
                                fontWeight = FontWeight.Medium
                            ),
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CandidateCard(candidate: Candidate) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = NeutralLight
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar placeholder
            Surface(
                modifier = Modifier.size(48.dp),
                color = Primary.copy(alpha = 0.1f),
                shape = RoundedCornerShape(24.dp)
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
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = candidate.name,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
                
                candidate.description?.let { description ->
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary
                        ),
                        maxLines = 1
                    )
                }
            }
        }
    }
}