package com.rekognizevote.ui.screens.polls

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
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
import com.rekognizevote.domain.model.Poll
import com.rekognizevote.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToPollDetails: (String) -> Unit,
    onNavigateToProfile: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Ativas", "Futuras", "Encerradas")
    val pollsState by viewModel.pollsState.collectAsState()
    
    LaunchedEffect(selectedTab) {
        val status = when (selectedTab) {
            0 -> Constants.POLL_STATUS_ACTIVE
            1 -> Constants.POLL_STATUS_UPCOMING
            2 -> Constants.POLL_STATUS_CLOSED
            else -> Constants.POLL_STATUS_ACTIVE
        }
        viewModel.loadPolls(status)
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
                    text = "RekognizeVote",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontFamily = MontserratFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            },
            actions = {
                IconButton(onClick = onNavigateToProfile) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Perfil",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Primary
            )
        )
        
        // Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Primary,
            contentColor = Color.White
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontFamily = MontserratFontFamily,
                                fontWeight = if (selectedTab == index) FontWeight.SemiBold else FontWeight.Normal
                            )
                        )
                    }
                )
            }
        }
        
        // Content
        when (val state = pollsState) {
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
                            text = "Erro ao carregar enquetes",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                val status = when (selectedTab) {
                                    0 -> Constants.POLL_STATUS_ACTIVE
                                    1 -> Constants.POLL_STATUS_UPCOMING
                                    2 -> Constants.POLL_STATUS_CLOSED
                                    else -> Constants.POLL_STATUS_ACTIVE
                                }
                                viewModel.loadPolls(status)
                            }
                        ) {
                            Text("Tentar novamente")
                        }
                    }
                }
            }
            is Result.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.data) { poll ->
                        PollCard(
                            poll = poll,
                            onClick = { onNavigateToPollDetails(poll.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PollCard(
    poll: Poll,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = poll.title,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontFamily = MontserratFontFamily,
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.weight(1f)
                )
                
                if (poll.isPrivate) {
                    Surface(
                        color = Warning.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "Privada",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Warning
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = poll.description,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextSecondary
                ),
                maxLines = 2
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${poll.totalVotes} votos",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary
                    )
                )
                
                val statusColor = when (poll.status) {
                    Constants.POLL_STATUS_ACTIVE -> Success
                    Constants.POLL_STATUS_UPCOMING -> Action
                    Constants.POLL_STATUS_CLOSED -> TextSecondary
                    else -> TextSecondary
                }
                
                val statusText = when (poll.status) {
                    Constants.POLL_STATUS_ACTIVE -> "Ativa"
                    Constants.POLL_STATUS_UPCOMING -> "Em breve"
                    Constants.POLL_STATUS_CLOSED -> "Encerrada"
                    else -> "Desconhecido"
                }
                
                Surface(
                    color = statusColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = statusText,
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