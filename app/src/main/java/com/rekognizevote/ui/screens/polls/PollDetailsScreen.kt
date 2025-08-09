package com.rekognizevote.ui.screens.polls

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
fun PollDetailsScreen(
    pollId: String,
    onNavigateBack: () -> Unit,
    onNavigateToVote: (String) -> Unit,
    viewModel: PollsViewModel
) {
    LaunchedEffect(pollId) {
        viewModel.loadPolls()
    }
    
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Detalhes da Enquete") },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                }
            }
        )
        
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Enquete: $pollId",
                    style = MaterialTheme.typography.headlineMedium
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Button(
                    onClick = { onNavigateToVote(pollId) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Iniciar Votação")
                }
            }
        }
    }
}