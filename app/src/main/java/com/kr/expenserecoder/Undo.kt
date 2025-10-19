package com.kr.expenserecoder

// Required imports
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

// Data class to hold undo action info
data class UndoAction(
    val message: String,
    val onUndo: () -> Unit,
    val onExecute: () -> Unit
)

// Composable for the Undo Snackbar
@Composable
fun UndoSnackbar(
    undoAction: UndoAction?,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var timeLeft by remember { mutableStateOf(3) }
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(undoAction) {
        if (undoAction != null) {
            isVisible = true
            timeLeft = 3

            // Countdown timer
            repeat(3) {
                delay(1000)
                timeLeft--
            }

            // Execute action after 5 seconds
            undoAction.onExecute()
            isVisible = false
            onDismiss()
        }
    }

    // Progress animation
    val progress by animateFloatAsState(
        targetValue = if (isVisible) (3 - timeLeft) / 3f else 0f,
        animationSpec = tween(durationMillis = 10, easing = LinearEasing),
        label = "progress"
    )

    AnimatedVisibility(
        visible = isVisible && undoAction != null,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        ),
        exit = slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(300)
        ),
        modifier = modifier
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFFEBDC7),
            shadowElevation = 8.dp
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Progress bar
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp),
                    color = Color(0xFFBDF2FE),
                    trackColor = Color.Transparent
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = Color.Red,
                            modifier = Modifier.size(24.dp)
                        )

                        Column {
                            Text(
                                text = undoAction?.message ?: "",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = Color.Red
                            )
                            Text(
                                text = "Deleting in ${timeLeft}s",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Black
                            )
                        }
                    }

                    TextButton(
                        onClick = {
                            undoAction?.onUndo?.invoke()
                            isVisible = false
                            onDismiss()
                        },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Undo,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "UNDO",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.Black
                        )

                    }
                }
            }
        }
    }
}

// ViewModel or State holder for managing undo actions
@Composable
fun rememberUndoState(): UndoState {
    return remember { UndoState() }
}

class UndoState {
    var currentAction by mutableStateOf<UndoAction?>(null)
        private set

    fun showUndo(
        message: String,
        onUndo: () -> Unit,
        onExecute: () -> Unit
    ) {
        currentAction = UndoAction(
            message = message,
            onUndo = onUndo,
            onExecute = onExecute
        )
    }

    fun dismiss() {
        currentAction = null
    }
}

