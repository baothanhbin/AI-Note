package com.ainote.feature.graph

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ainote.core.model.graph.GraphData
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun GraphRoute(
    onBackClick: () -> Unit,
    onNodeClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GraphViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    GraphScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onNodeClick = onNodeClick,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GraphScreen(
    uiState: GraphUiState,
    onBackClick: () -> Unit,
    onNodeClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Knowledge Graph") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when (uiState) {
                is GraphUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is GraphUiState.Error -> {
                    com.ainote.core.ui.component.EmptyStateView(
                        title = "Error",
                        message = "Could not load graph.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is GraphUiState.Success -> {
                    GraphVisualization(
                        graphData = uiState.graphData,
                        onNodeClick = onNodeClick,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun GraphVisualization(
    graphData: GraphData,
    onNodeClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var pan by remember { mutableStateOf(Offset.Zero) }
    val textMeasurer = rememberTextMeasurer()
    val primaryColor = MaterialTheme.colorScheme.primary
    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary
    
    // Auto-layout logic (circle or basic force simulated placeholder)
    val nodePositions = remember(graphData.nodes) {
        val positions = mutableMapOf<String, Offset>()
        val centerX = 500f
        val centerY = 500f
        val radius = 300f
        val count = graphData.nodes.size
        graphData.nodes.forEachIndexed { index, node ->
            val angle = (2 * Math.PI * index) / count
            positions[node.noteId] = Offset(
                x = centerX + (radius * cos(angle)).toFloat(),
                y = centerY + (radius * sin(angle)).toFloat()
            )
        }
        positions
    }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, panChange, zoomChange, _ ->
                    scale = (scale * zoomChange).coerceIn(0.5f, 5f)
                    pan += panChange
                }
            }
    ) {
        // Draw Edges
        graphData.edges.forEach { edge ->
            val start = nodePositions[edge.fromNoteId]
            val end = nodePositions[edge.toNoteId]
            if (start != null && end != null) {
                drawLine(
                    color = Color.Gray.copy(alpha = 0.5f),
                    start = start * scale + pan,
                    end = end * scale + pan,
                    strokeWidth = 2f * scale
                )
            }
        }

        // Draw Nodes
        graphData.nodes.forEach { node ->
            val position = nodePositions[node.noteId]
            if (position != null) {
                val offset = position * scale + pan
                drawCircle(
                    color = primaryColor,
                    radius = 20f * scale,
                    center = offset
                )
                // Just dropping quick titles
                drawText(
                    textMeasurer = textMeasurer,
                    text = node.title,
                    style = TextStyle(fontSize = 12.sp, color = primaryColor),
                    topLeft = Offset(offset.x - 20f, offset.y + 25f)
                )
            }
        }
    }
}
