package com.rekognizevote.ui.screens.vote

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rekognizevote.core.Result

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoteScreen(
    pollId: String,
    onNavigateBack: () -> Unit,
    onVoteSuccess: () -> Unit,
    viewModel: VoteViewModel
) {
    val pollState by viewModel.pollState.collectAsState()
    val voteState by viewModel.voteState.collectAsState()
    
    LaunchedEffect(pollId) {
        viewModel.loadPoll(pollId)
    }
    
    LaunchedEffect(voteState) {
        if (voteState is Result.Success) {
            onVoteSuccess()
        }
    }
    
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Votar") },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                }
            }
        )
        
        when (val state = pollState) {
            is Result.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is Result.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Erro ao carregar enquete")
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.loadPoll(pollId) }) {
                            Text("Tentar novamente")
                        }
                    }
                }
            }
            is Result.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = state.data.title,
                        style = MaterialTheme.typography.headlineMedium
                    )
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    Button(
                        onClick = { 
                            viewModel.submitVote(pollId, "candidate1", "mock_image_uri")
                        },
                        enabled = voteState !is Result.Loading,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (voteState is Result.Loading) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp))
                        } else {
                            Text("Confirmar Voto")
                        }
                    }
                    
                    voteState?.let { state ->
                        if (state is Result.Error) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = state.exception.message ?: "Erro ao votar",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}