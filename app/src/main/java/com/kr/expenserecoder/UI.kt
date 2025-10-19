package com.kr.expenserecoder


import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kr.expenserecoder.ui.settings.MyViewModel

// Required imports
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

// Required imports exp click
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay

// explore
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.media3.exoplayer.ExoPlayer
import kotlin.math.roundToInt
import kotlin.random.Random




// Settings UI to enable/disable auto-tracking
@Composable
fun AutoTrackingSettings(viewModel : MyViewModel = viewModel()) {
    val isAutoTrackingEnabled = viewModel.isAutoTrackingEnabled
        .collectAsState(initial = true).value
    var expend by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(10)
        expend = true
    }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .animateContentSize()
        ) {
            if (expend){
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Auto-Add Expenses",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )

                        Text(
                            text = if (isAutoTrackingEnabled) "SMS-tracking is enabled" else "SMS-tracking is disabled",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isAutoTrackingEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = isAutoTrackingEnabled,
                        onCheckedChange = { viewModel.setAutoTracking(it) }
                    )
                }

                if (isAutoTrackingEnabled) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Only debit/expense transactions will be added automatically",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "• If your bank doesn't send SMS, expenses cannot be auto-tracked",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "• If you receive SMS and not added automatically, please give the feedback",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}


//@Composable
//fun ExpenseItem(
//    expense: Expense,
//    onEdit: () -> Unit,
//    onDelete: () -> Unit,
//    onClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Card(modifier = modifier.fillMaxWidth().clickable(onClick = onClick)) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Box(
//                    modifier = Modifier
//                        .size(40.dp)
//                        .background(
//                            expense.category.color.copy(alpha = 0.1f),
//                            CircleShape
//                        ),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Icon(
//                        imageVector = expense.category.icon,
//                        contentDescription = expense.category.displayName,
//                        tint = expense.category.color,
//                        modifier = Modifier.size(20.dp)
//                    )
//                }
//                Spacer(modifier = Modifier.width(12.dp))
//                Column {
//                    Text(
//                        text = if (expense.description.length > 18)
//                            expense.description.take(18) + "..."
//                        else
//                            expense.description,
//                        style = MaterialTheme.typography.bodyMedium,
//                        fontWeight = FontWeight.Medium
//                    )
//                    if(expense.category.displayName == "Smoke" || expense.category.displayName == "Drink"  ){
//                        Text(
//                            text = "Drinking and smoking is injurious to health",
//                            style = MaterialTheme.typography.bodySmall,
//                            color = MaterialTheme.colorScheme.onSurfaceVariant
//                        )
//                    }
//                    else{
//                        Text(
//                            text = "${expense.category.displayName.take(9)}+..",
//                            style = MaterialTheme.typography.bodySmall,
//                            color = MaterialTheme.colorScheme.onSurfaceVariant
//                        )
//                    }
//                    Text(
//                        text = "\uD83D\uDCC5 ${expense.date.toString()}  \uD83D\uDD56 ${expense.time.toString().take(5)}",
//                        style = MaterialTheme.typography.bodySmall,
//                        color = MaterialTheme.colorScheme.onSurfaceVariant
//                    )
//
//                }
//            }
//
//            Column(horizontalAlignment = Alignment.End) {
//                Text(
//                    text = "-₹.${"%.2f".format(expense.amount)}",
//                    style = MaterialTheme.typography.bodyMedium,
//                    fontWeight = FontWeight.SemiBold,
//                    color = MaterialTheme.colorScheme.error
//                )
//                Row {
//                    IconButton(
//                        onClick = onEdit,
//                        modifier = Modifier.size(24.dp)
//                    ) {
//                        Icon(
//                            Icons.Default.Edit,
//                            contentDescription = "Edit",
//                            modifier = Modifier.size(16.dp)
//                        )
//                    }
//                    IconButton(
//                        onClick = onDelete,
//                        modifier = Modifier.size(24.dp)
//                    ) {
//                        Icon(
//                            Icons.Default.Delete,
//                            contentDescription = "Delete",
//                            modifier = Modifier.size(16.dp)
//                        )
//                    }
//                }
//            }
//        }
//    }
//}

@Composable
fun ExpenseItem(
    expense: Expense,
    Key : Int =1 ,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    index: Int = 0 // Add index for staggered animation
) {
    val Context = LocalContext.current
    // Entrance animation
    var visible by remember { mutableStateOf(false) }

    androidx.compose.runtime.LaunchedEffect(Unit) {
        delay(index * 50L)
        visible = true
    }

    // Scale animation for press effect
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    // Slide and fade animation
    val offsetY by animateDpAsState(
        targetValue = if (visible) 0.dp else 20.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )
    val undoState = rememberUndoState()
    val scope = rememberCoroutineScope()

    var xvaluerandom = remember (Key){Random.nextInt(0,100)}

    var offsetX by remember { mutableStateOf(xvaluerandom.toFloat()) }
    var color : Color = if (offsetX >= 101) MaterialTheme.colorScheme.error
            else if (offsetX < -1f ) MaterialTheme.colorScheme.primary
            else Color.Transparent

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 400)
    )

    var isExpand by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
//        delay(1)
        for (i in 60 downTo 0) {
            delay(1)
            offsetX = i.toFloat()
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .background(color, RoundedCornerShape(20.dp))
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                translationY = offsetY.toPx()
                this.alpha = alpha
            }
            .animateContentSize()
//            .clickable(
//                indication = null,
//                interactionSource = remember { MutableInteractionSource() }
//            ) {
//               onClick()
//            }
            .offset {
                IntOffset(x = offsetX.roundToInt(), y = 0)
            }
            .draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta ->
                    offsetX += delta
                },
                onDragStopped = {
                    if ( offsetX  > 500) {
                        undoState.showUndo(
                            message = "Expense deleted",
                            onUndo = {  offsetX = 0f },
                            onExecute = {
                                onDelete()
                                offsetX = 0f
                            }
                        )
                        offsetX = 0f
                    }
                    if (offsetX < -500){
                        onEdit()
                        offsetX = 0f
                    }
                    offsetX = 0f
                },

                )
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        onClick()
                    },
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    },
                    onLongPress = {
                        Toast.makeText(Context,
                            "Short Description",
                            Toast.LENGTH_LONG)
                            .show()
                        isExpand = !isExpand
                    },
                    onTap = {
                        onClick()
                    }
                )
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 8.dp
        )
    ) {
        // Undo Snackbar at the bottom
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            UndoSnackbar(
                undoAction = undoState.currentAction,
                onDismiss = { undoState.dismiss() }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Animated icon container
                val iconScale by animateFloatAsState(
                    targetValue = if (visible) 1f else 0f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .graphicsLayer {
                            scaleX = iconScale
                            scaleY = iconScale
                        }
                        .background(
                            expense.category.color.copy(alpha = 0.1f),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = expense.category.icon,
                        contentDescription = expense.category.displayName,
                        tint = expense.category.color,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    if(!isExpand){
                        Text(
                            text  =  expense.description.take(12) + "...",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    AnimatedVisibility(visible = isExpand) {
                        Text(
                            text = expense.description.take(80) + if (expense.description.length > 80) "..." else "",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    if (expense.category.displayName == "Smoke" || expense.category.displayName == "Drink") {
                        // Pulsing warning text
                        val warningAlpha by rememberInfiniteTransition().animateFloat(
                            initialValue = 0.6f,
                            targetValue = 1f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(1500, easing = FastOutSlowInEasing),
                                repeatMode = RepeatMode.Reverse
                            )
                        )
                        Text(
                            text = "⚠️ Drinking and smoking is injurious to health",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error.copy(alpha = warningAlpha),
                            fontWeight = FontWeight.Medium
                        )
                    } else {
                        Text(
                            text = "${expense.category.displayName.take(9)}${if (expense.category.displayName.length > 9) "+.." else ""}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Text(
                        text = "📅 ${expense.date}  🕖 ${expense.time.toString().take(5)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                // Animated amount with slide effect
                val amountOffset by animateDpAsState(
                    targetValue = if (visible) 0.dp else 10.dp,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )

                Text(
                    text = "-₹${"%.2f".format(expense.amount)}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.offset(x = amountOffset)
                )

                /*Spacer(modifier = Modifier.height(4.dp))

                Row(horizontalArrangement = Arrangement.End) {
                    // Edit button with hover effect
                    var editHovered by remember { mutableStateOf(false) }
                    val editScale by animateFloatAsState(
                        targetValue = if (editHovered) 1.2f else 1f,
                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                    )

                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier
                            .size(24.dp)
                            .graphicsLayer {
                                scaleX = editScale
                                scaleY = editScale
                            }
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onPress = {
                                        editHovered = true
                                        tryAwaitRelease()
                                        editHovered = false
                                    }
                                )
                            }
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Delete button with hover effect
                    var deleteHovered by remember { mutableStateOf(false) }
                    val deleteScale by animateFloatAsState(
                        targetValue = if (deleteHovered) 1.2f else 1f,
                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                    )

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier
                            .size(24.dp)
                            .graphicsLayer {
                                scaleX = deleteScale
                                scaleY = deleteScale
                            }
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onPress = {
                                        deleteHovered = true
                                        tryAwaitRelease()
                                        deleteHovered = false
                                    }
                                )
                            }
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }*/
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseDialog(
    onDismiss: () -> Unit,
    onAddExpense: (Expense) -> Unit,
    onEditExpense: (Expense) -> Unit,
    isupdate: Boolean = false,
    expenseToEdit: Expense? = null,
) {
    var amount by remember { mutableStateOf(expenseToEdit?.amount?.toString() ?: "") }
    var selectedCategory by remember { mutableStateOf(expenseToEdit?.category ?: ExpenseCategory.FOOD) }
    var description by remember { mutableStateOf(expenseToEdit?.description ?: "") }
    var date by remember { mutableStateOf(expenseToEdit?.date ?: getCurrentDate()) }
    var expanded by remember { mutableStateOf(false) }
    var visible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    androidx.compose.runtime.LaunchedEffect(Unit) {
        delay(50)
        visible = true
    }

    // Scale animation for dialog
    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.85f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "scale"
    )

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(300),
        label = "alpha"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    this.alpha = alpha
                },
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Header with icon
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Animated icon
                        val iconScale by animateFloatAsState(
                            targetValue = if (visible) 1f else 0f,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessMedium
                            ),
                            label = "iconScale"
                        )

                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .graphicsLayer {
                                    scaleX = iconScale
                                    scaleY = iconScale
                                }
                                .background(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isupdate) Icons.Default.Edit else Icons.Default.Add,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Text(
                            text = if (isupdate) "Update Expense" else "Add New Expense",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Form fields with staggered animation
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    AnimatedFormField(index = 0, visible = visible) {
                        OutlinedTextField(
                            value = amount,
                            onValueChange = { amount = it },
                            label = { Text("Amount") },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.CurrencyRupee,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                            )
                        )
                    }

                    AnimatedFormField(index = 1, visible = visible) {
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            OutlinedTextField(
                                value = selectedCategory.displayName,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Category") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = selectedCategory.icon,
                                        contentDescription = null,
                                        tint = selectedCategory.color,
                                        modifier = Modifier.size(20.dp)
                                    )
                                },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                ExpenseCategory.values().forEach { category ->
                                    DropdownMenuItem(
                                        text = {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(36.dp)
                                                        .background(
                                                            category.color.copy(alpha = 0.1f),
                                                            RoundedCornerShape(8.dp)
                                                        ),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Icon(
                                                        imageVector = category.icon,
                                                        contentDescription = null,
                                                        tint = category.color,
                                                        modifier = Modifier.size(20.dp)
                                                    )
                                                }
                                                Text(
                                                    category.displayName,
                                                    style = MaterialTheme.typography.bodyLarge
                                                )
                                            }
                                        },
                                        onClick = {
                                            selectedCategory = category
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    AnimatedFormField(index = 2, visible = visible) {
                        OutlinedTextField(
                            value = description,
                            onValueChange = {
                                if (it.length <= 500) description = it
                            },
                            label = { Text("Description") },
                            placeholder = { Text("What did you spend on?") },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Description,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            supportingText = {
                                Text(
                                    "${description.length}/500",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                            ),
                            maxLines = 3
                        )
                    }

                    AnimatedFormField(index = 3, visible = visible) {
                        OutlinedTextField(
                            value = date,
                            onValueChange = { date = it },
                            label = { Text("Date (optional)") },
                            placeholder = { Text(getCurrentDate()) },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.CalendarToday,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            supportingText = {
                                Text(
                                    "Format: YYYY-MM-DD",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Cancel", fontWeight = FontWeight.SemiBold)
                    }

                    Button(
                        onClick = {
                            if (amount.isNotBlank() && description.isNotBlank()) {
                                val finalDate = if (date.isBlank()) getCurrentDate() else date

                                val expense = Expense(
                                    id = expenseToEdit?.id ?: 0L,
                                    amount = amount.toDoubleOrNull() ?: 0.0,
                                    category = selectedCategory,
                                    description = description,
                                    date = finalDate
                                )

                                if (isupdate) {
                                    onEditExpense(expense)
                                } else {
                                    onAddExpense(expense)
                                    val player = ExoPlayer.Builder(context).build()
                                    playAudioFromRaw(context, player, R.raw.cashsound)


                                }
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        enabled = amount.isNotBlank() && description.isNotBlank()
                    ) {
                        Icon(
                            if (isupdate) Icons.Default.Check else Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            if (isupdate) "Update" else "Add",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedFormField(
    index: Int,
    visible: Boolean,
    content: @Composable () -> Unit
) {
    var fieldVisible by remember { mutableStateOf(false) }

    LaunchedEffect(visible) {
        if (visible) {
            delay((index * 50L) + 100)
            fieldVisible = true
        }
    }

    val offsetY by animateDpAsState(
        targetValue = if (fieldVisible) 0.dp else 15.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "offsetY"
    )

    val alpha by animateFloatAsState(
        targetValue = if (fieldVisible) 1f else 0f,
        animationSpec = tween(400),
        label = "fieldAlpha"
    )

    Box(
        modifier = Modifier.graphicsLayer {
            translationY = offsetY.toPx()
            this.alpha = alpha
        }
    ) {
        content()
    }
}
//
//@RequiresApi(Build.VERSION_CODES.O)
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun UpdateUserDialog2(viewModel: MyViewModel, onDismiss: () -> Unit) {
//
//    val username by viewModel.username.collectAsState(initial = "")
//    val limit by viewModel.monthlyLimit.collectAsState(initial = 0)
//
//    // Local states to hold temporary input values
//    var tempUsername by remember { mutableStateOf(username) }
//    var tempLimit by remember { mutableStateOf(limit.toString()) }
//
//    AlertDialog(
//        onDismissRequest = { onDismiss() },
//        title = { Text("Update User and Your Expense Limit") },
//        text = {
//            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
//                OutlinedTextField(
//                    value = tempUsername,
//                    onValueChange = { if (it.length <= 30) tempUsername = it},
//                    label = { Text("Username*") },
//                    modifier = Modifier.fillMaxWidth()
//                )
//                OutlinedTextField(
//                    value = tempLimit,
//                    onValueChange = { tempLimit = it  },
//                    label = { Text("Set limit*") },
//                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
//                    placeholder = { Text("Your monthly limit") },
//                    modifier = Modifier.fillMaxWidth()
//                )
//            }
//        },
//        confirmButton = {
//            TextButton(
//                onClick = {
//                    if (tempUsername.isNotBlank() ) {
//                        viewModel.updateUsername(tempUsername)
//                        viewModel.updateMonthlyLimit(tempLimit.toInt())
//                        onDismiss()
//                    }
//                }
//            ) {
//                Text("Update")
//            }
//        },
//        dismissButton = {
//            TextButton(onClick = { onDismiss() }) {
//                Text("Cancel")
//            }
//        }
//    )
//}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomSheetUI(
    viewModel: MyViewModel,
    onDismiss: () -> Unit
) {
    val username by viewModel.username.collectAsState(initial = "")
    val limit by viewModel.monthlyLimit.collectAsState(initial = 0)

    // Local states to hold temporary input values
    var tempUsername by remember { mutableStateOf(username) }
    var tempLimit by remember { mutableStateOf(limit.toString()) }

    // Keep local states in sync with ViewModel when it changes
    LaunchedEffect(username, limit) {
        tempUsername = username
        tempLimit = limit.toString()
    }

    var visible by remember { mutableStateOf(false) }

    // Trigger animation when sheet opens
    LaunchedEffect(Unit) {
        delay(100)
        visible = true
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        ),
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
        ) {
            // Header with icon
            AnimatedSheetItem(index = 0, visible = visible) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "Update Profile",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Manage your account settings",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Form fields with staggered animation
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                AnimatedSheetItem(index = 1, visible = visible) {
                    OutlinedTextField(
                        value = tempUsername,
                        onValueChange = { if (it.length < 25) tempUsername = it },
                        label = { Text("Username") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.PersonAdd,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        supportingText = {
                            Text(
                                "${tempUsername.length}/25",
                                style = MaterialTheme.typography.bodySmall
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                        ),
                        singleLine = true
                    )
                }

                AnimatedSheetItem(index = 2, visible = visible) {
                    OutlinedTextField(
                        value = tempLimit,
                        onValueChange = { tempLimit = it },
                        label = { Text("Monthly Spending Limit") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.CurrencyRupee,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        supportingText = {
                            Text(
                                "Set your monthly budget limit",
                                style = MaterialTheme.typography.bodySmall
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                        ),
                        singleLine = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action buttons
            AnimatedSheetItem(index = 3, visible = visible) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            width = 1.5.dp
                        )
                    ) {
                        Text(
                            "Cancel",
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    Button(
                        onClick = {
                            if (tempUsername.isNotBlank()) {
                                viewModel.updateUsername(tempUsername)
                                viewModel.updateMonthlyLimit(tempLimit.toIntOrNull() ?: 0)
                                onDismiss()
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        enabled = tempUsername.isNotBlank()
                    ) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Update",
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedSheetItem(
    index: Int,
    visible: Boolean,
    content: @Composable () -> Unit
) {
    var itemVisible by remember { mutableStateOf(false) }

    LaunchedEffect(visible) {
        if (visible) {
            delay((index * 80L))
            itemVisible = true
        }
    }

    val offsetY by animateDpAsState(
        targetValue = if (itemVisible) 0.dp else 20.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "offsetY"
    )

    val alpha by animateFloatAsState(
        targetValue = if (itemVisible) 1f else 0f,
        animationSpec = tween(500),
        label = "itemAlpha"
    )

    Box(
        modifier = Modifier.graphicsLayer {
            translationY = offsetY.toPx()
            this.alpha = alpha
        }
    ) {
        content()
    }
}


    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ExpenseClick(
        onDismiss: () -> Unit,
        expenseToEdit: Expense? = null,
    ) {
        var visible by remember { mutableStateOf(false) }

        androidx.compose.runtime.LaunchedEffect(Unit) {
            delay(50)
            visible = true
        }

        // Scale animation for dialog
        val scale by animateFloatAsState(
            targetValue = if (visible) 1f else 0.8f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            ),
            label = "scale"
        )

        val alpha by animateFloatAsState(
            targetValue = if (visible) 1f else 0f,
            animationSpec = tween(300),
            label = "alpha"
        )

        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        this.alpha = alpha
                    },
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    // Header with icon and title
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Animated category icon
                            val iconScale by animateFloatAsState(
                                targetValue = if (visible) 1f else 0f,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessMedium
                                ),
                                label = "iconScale"
                            )

                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .graphicsLayer {
                                        scaleX = iconScale
                                        scaleY = iconScale
                                    }
                                    .background(
                                        expenseToEdit?.category?.color?.copy(alpha = 0.15f)
                                            ?: MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(0.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = expenseToEdit?.category?.icon
                                        ?: Icons.Default.AccountBalanceWallet,
                                    contentDescription = null,
                                    tint = expenseToEdit?.category?.color
                                        ?: MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Text(
                                text = "Expense Details",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        IconButton(onClick = onDismiss) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Close",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Amount with animated highlight
                    val infiniteTransition = rememberInfiniteTransition(label = "infinite")
                    val amountAlpha by infiniteTransition.animateFloat(
                        initialValue = 0.7f,
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(2000, easing = FastOutSlowInEasing),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "amountAlpha"
                    )

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Amount Spent",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "-₹${"%.2f".format(expenseToEdit?.amount ?: 0.0)}",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.error.copy(alpha = amountAlpha)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Details with staggered animation
                    expenseToEdit?.let { expense ->
                        DetailItem(
                            icon = Icons.Default.Category,
                            label = "Category",
                            value = expense.category.displayName,
                            index = 0,
                            visible = visible
                        )

                        DetailItem(
                            icon = Icons.Default.Description,
                            label = "Description",
                            value = expense.description,
                            index = 1,
                            visible = visible
                        )

                        DetailItem(
                            icon = Icons.Default.CalendarToday,
                            label = "Date",
                            value = expense.date.toString(),
                            index = 2,
                            visible = visible
                        )

                        DetailItem(
                            icon = Icons.Default.AccessTime,
                            label = "Time",
                            value = expense.time.toString().take(8),
                            index = 3,
                            visible = visible
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Close button
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF838884)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "Close",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun DetailItem(
        icon: ImageVector,
        label: String,
        value: String,
        index: Int,
        visible: Boolean
    ) {
        var itemVisible by remember { mutableStateOf(false) }

        LaunchedEffect(visible) {
            if (visible) {
                delay((index * 50L) + 100)
                itemVisible = true
            }
        }

        val offsetX by animateDpAsState(
            targetValue = if (itemVisible) 0.dp else 20.dp,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            ),
            label = "offsetX"
        )

        val alpha by animateFloatAsState(
            targetValue = if (itemVisible) 1f else 0f,
            animationSpec = tween(400),
            label = "itemAlpha"
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .graphicsLayer {
                    translationX = offsetX.toPx()
                    this.alpha = alpha
                },
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        MaterialTheme.colorScheme.primaryContainer,
                        RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }


    // for Analytics screen
    @Composable
    fun CategoryItem(
        category: ExpenseCategory,
        amount: Double,
        percentage: Double
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(category.color, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = category.icon,
                        contentDescription = category.displayName,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = category.displayName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "Rs.${"%.2f".format(amount)}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${"%.0f".format(percentage)}%",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }


// card explosion effect

@Composable
fun ExplosionParticles(
    exploded: Boolean,
    particleCount: Int,
    modifier: Modifier = Modifier
) {
    val particles = remember {
        List(particleCount) {
            Particle(
                startOffset = Offset.Zero,
                velocity = Offset(
                    x = Random.nextFloat() * 600f - 300f,
                    y = Random.nextFloat() * 600f - 300f
                ),
                color = Color(
                    red = Random.nextFloat(),
                    green = Random.nextFloat(),
                    blue = Random.nextFloat(),
                    alpha = 1f
                ),
                radius = Random.nextFloat() * 8f + 4f
            )
        }
    }

    val progress = remember { Animatable(0f) }

    LaunchedEffect(exploded) {
        if (exploded) {
            progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 800, easing = LinearOutSlowInEasing)
            )
        } else {
            progress.snapTo(0f)
        }
    }

    if (exploded) {
        Canvas(modifier = modifier) {
            val width = size.width
            val height = size.height
            val center = Offset(width / 2, height / 2)

            particles.forEach { particle ->
                // Calculate position based on velocity and progress
                val offset = center + particle.velocity * progress.value

                // Particles shrink and fade out as progress advances
                val radius = particle.radius * (1f - progress.value)
                val alpha = 1f - progress.value

                drawCircle(
                    color = particle.color.copy(alpha = alpha),
                    radius = radius,
                    center = offset
                )
            }
        }
    }
}

data class Particle(
    val startOffset: Offset,
    val velocity: Offset,
    val color: Color,
    val radius: Float
)
