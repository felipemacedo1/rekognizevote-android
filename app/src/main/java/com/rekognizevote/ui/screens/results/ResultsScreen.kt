package com.rekognizevote.ui.screens.results

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
// Usando emojis alternativos
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
import com.rekognizevote.domain.model.Poll
import com.rekognizevote.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(
    pollId: String,
    onNavigateBack: () -> Unit,
    viewModel: ResultsViewModel = hiltViewModel()
) {
    val pollState by viewModel.pollState.collectAsState()
    
    LaunchedEffect(pollId) {
        viewModel.loadPollResults(pollId)
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
                    text = "Resultados",
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
                            text = "Erro ao carregar resultados",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { viewModel.loadPollResults(pollId) }
                        ) {
                            Text("Tentar novamente")
                        }
                    }
                }
            }
            is Result.Success -> {
                ResultsContent(poll = state.data)
            }
        }
    }
}

@Composable
private fun ResultsContent(poll: Poll) {
    val sortedCandidates = poll.candidates.sortedByDescending { it.voteCount }
    val winner = sortedCandidates.firstOrNull()
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Informações da enquete
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
                        text = "Total de votos: ${poll.totalVotes}",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = TextSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }
        
        // Vencedor
        winner?.let { winnerCandidate ->
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Success.copy(alpha = 0.1f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🏆",
                            style = MaterialTheme.typography.headlineLarge
                        )
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        Column {
                            Text(
                                text = "Vencedor",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = Success,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                            
                            Text(
                                text = winnerCandidate.name,
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontFamily = MontserratFontFamily,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            
                            Text(
                                text = "${winnerCandidate.voteCount} votos (${String.format("%.1f", winnerCandidate.percentage)}%)",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = TextSecondary
                                )
                            )
                        }
                    }
                }
            }
        }
        
        item {
            Text(
                text = "Todos os Resultados",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontFamily = MontserratFontFamily,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
        
        items(sortedCandidates) { candidate ->
            ResultCard(
                candidate = candidate,
                isWinner = candidate.id == winner?.id,
                totalVotes = poll.totalVotes
            )
        }
    }
}

@Composable
private fun ResultCard(
    candidate: Candidate,
    isWinner: Boolean,
    totalVotes: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isWinner) Success.copy(alpha = 0.05f) 
                           else MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = candidate.name,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Medium
                            )
                        )
                        
                        if (isWinner) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "🏆",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                    
                    Text(
                        text = "${candidate.voteCount} votos",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextSecondary
                        )
                    )
                }
                
                Text(
                    text = "${String.format("%.1f", candidate.percentage)}%",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (isWinner) Success else Primary
                    )
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Barra de progresso
            LinearProgressIndicator(
                progress = { candidate.percentage / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = if (isWinner) Success else Action,
                trackColor = NeutralLight,
                strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
            )
        }
    }
}