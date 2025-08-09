package com.rekognizevote.ui.screens.polls

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rekognizevote.core.Result

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToPollDetails: (String) -> Unit,
    onNavigateToResults: (String) -> Unit,
    viewModel: PollsViewModel
) {
    val pollsState by viewModel.pollsState.collectAsState()
    
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Enquetes") }
        )
        
        when (val state = pollsState) {
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
                        Text("Erro ao carregar enquetes")
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.refreshPolls() }) {
                            Text("Tentar novamente")
                        }
                    }
                }
            }
            is Result.Success -> {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.data) { poll ->
                        Card(
                            onClick = { onNavigateToPollDetails(poll.id) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = poll.title,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = poll.description ?: "",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row {
                                    Button(
                                        onClick = { onNavigateToPollDetails(poll.id) }
                                    ) {
                                        Text("Votar")
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    OutlinedButton(
                                        onClick = { onNavigateToResults(poll.id) }
                                    ) {
                                        Text("Resultados")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}