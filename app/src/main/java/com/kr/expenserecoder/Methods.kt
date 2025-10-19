package com.kr.expenserecoder

import com.kr.expenserecoder.R
import android.content.Context
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import android.graphics.pdf.PdfDocument
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import android.graphics.Paint
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin
import androidx.compose.ui.graphics.drawscope.Stroke
import android.content.ContentValues
import android.provider.MediaStore
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.material3.Checkbox
import androidx.compose.ui.platform.LocalContext
import coil.request.ImageRequest
import coil.compose.AsyncImage
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.CheckboxDefaults
import kotlinx.coroutines.delay

// Function to get month name from number
@RequiresApi(Build.VERSION_CODES.O)
fun getMonthName(number: Int): String {
    return LocalDate.now()
        .withMonth(number)
        .month
        .getDisplayName(TextStyle.FULL, Locale.ENGLISH)
}

fun getCurrentTime(): String {
    val current = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        java.time.LocalTime.now()
    } else {
        return "12:00:00"
    }
    return current.toString()
}

// User confirmation dialog
@Composable
fun ConfirmDialog(
    title: String,
    message: String,
    color : Color,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row{
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(R.drawable.warning) // your GIF
                        .build(),
                    contentDescription = "Warning Animation",
                    modifier = Modifier.size(30.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(title)
            }
        },
        text = { Text(message,color = color) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Yes")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("No")
            }
        }
    )
}

// Fixed saveToPDF function

fun saveToPDF(items: List<Expense>, context: Context, total: Double) {
    val pdfDocument = PdfDocument()
    val pageWidth = 595
    val pageHeight = 842
    val marginTop = 50f
    val marginBottom = 50f
    val lineHeight = 25f

    var y = 0f
    var pageNumber = 1
    val currentDateStr = getCurrentDate()

    val paint = Paint().apply {
        textSize = 12f
        isFakeBoldText = false
        isAntiAlias = true
    }

    fun newPage(): PdfDocument.Page {
        val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        paint.textSize = 18f
        paint.isFakeBoldText = true
        canvas.drawText("Expense Report - $currentDateStr", 40f, marginTop, paint)

        paint.textSize = 14f
        paint.isFakeBoldText = true
        y = marginTop + 50f
        canvas.drawText("Category", 40f, y, paint)
        canvas.drawText("Description", 120f, y, paint)
        canvas.drawText("Amount", 320f, y, paint)
        canvas.drawText("Date", 420f, y, paint)

        paint.textSize = 12f
        paint.isFakeBoldText = false
        y += 30f

        return page
    }

    var page = newPage()
    var canvas = page.canvas

    for (item in items) {
        if (y + lineHeight > pageHeight - marginBottom) {
            pdfDocument.finishPage(page)
            pageNumber++
            page = newPage()
            canvas = page.canvas
        }

        val catText = item.category.displayName
        val itemText = if (item.description.length > 25) item.description.take(25) + "..." else item.description
        val amountText = "₹%.2f".format(item.amount)
        val dateText = item.date

        canvas.drawText(catText, 40f, y, paint)
        canvas.drawText(itemText, 120f, y, paint)
        canvas.drawText(amountText, 320f, y, paint)
        canvas.drawText(dateText, 420f, y, paint)

        y += lineHeight
    }

    // Draw total
    paint.isFakeBoldText = true
    canvas.drawText("Total: ₹%.2f".format(total), 40f, pageHeight - marginBottom, paint)

    pdfDocument.finishPage(page)

    // Save to public Downloads folder using MediaStore (Android 10+ compatible)
    val filename = "${currentDateStr}_Expense_Report.pdf"

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
        // Android 10+ - Use MediaStore
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }

        val uri = context.contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

        try {
            uri?.let {
                context.contentResolver.openOutputStream(it)?.use { outputStream ->
                    pdfDocument.writeTo(outputStream)
                }
                Toast.makeText(context, "PDF saved to Downloads folder", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    } else {
        // Android 9 and below - Use legacy method
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloadsDir, filename)

        try {
            FileOutputStream(file).use { out ->
                pdfDocument.writeTo(out)
            }
            Toast.makeText(context, "PDF saved to Downloads: $filename", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    pdfDocument.close()
}
// Month selection dropdown
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthWithMenu(
    selectedMonth: String,
    onMonthSelected: (Int) -> Unit,
    onDeleteAll: () -> Unit,
    onDeleteMonth: () -> Unit,
    onSave: () -> Unit,
    onSaveToday: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    // Key = Name, Value = Number

    val months = listOf(
        "January" to 1,
        "February" to 2,
        "March" to 3,
        "April" to 4,
        "May" to 5,
        "June" to 6,
        "July" to 7,
        "August" to 8,
        "September" to 9,
        "October" to 10,
        "November" to 11,
        "December" to 12
    )
   var selectedMonth2 by remember { mutableStateOf(10) }
//    val selectedMonthName = months.firstOrNull { it.second == selectedMonth }?.first ?: "Select Month"


    // Rotate the list so it starts from the selected month:
    val rotatedMonths = remember(selectedMonth2) {
        val index = months.indexOfFirst { it.second == selectedMonth2-1}
        if (index != -1) {
            months.drop(index) + months.take(index)
        } else {
            months
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp)) // rounded edges
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Month dropdown (takes most space)
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.weight(1f)
        ) {
            OutlinedTextField(
                value = selectedMonth,
                onValueChange = {},
                readOnly = true,
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                shape = RoundedCornerShape(24.dp),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor() // ✅ only valid inside ExposedDropdownMenuBox
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                rotatedMonths.forEach { (name, number) ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                name,
                                color = if (number == selectedMonth2) Color(0xFF8CD3E5) else Color.Unspecified
                            )
                        },
                        onClick = {
                            onMonthSelected(number)
                            selectedMonth2 = number
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Three dot menu at end
        ThreeDotMenu(
            onDeleteAll = onDeleteAll,
            onDeleteMonth = onDeleteMonth,
            onSave = onSave,
            onSaveToday = onSaveToday,
        )
    }
}


@Composable
fun PieChart(
    data: Map<ExpenseCategory, Double>,
    modifier: Modifier = Modifier.size(200.dp),
    centerHoleSize: Float = 0.4f,
    animationDurationMs: Int = 1000
) {
    val total = data.values.sum()
    val maxEntry = data.maxByOrNull { it.value }

    // Animation state
    var animationPlayed by remember { mutableStateOf(false) }
    val animationProgress = animateFloatAsState(
        targetValue = if (animationPlayed) 1f else 0f,
        animationSpec = tween(
            durationMillis = animationDurationMs,
            easing = EaseOutCubic
        ),
        label = "pie_animation"
    )

    // Start animation when component loads
    LaunchedEffect(Unit) {
        animationPlayed = true
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val centerX = size.width / 2f
            val centerY = size.height / 2f
            val radius = minOf(size.width, size.height) / 2f * 0.8f
            val innerRadius = radius * centerHoleSize

            var startAngle = -90f

            data.forEach { (category, value) ->
                val sweepAngle = (value.toFloat() / total.toFloat()) * 360f * animationProgress.value
                val isLargest = value == maxEntry?.value

                // Outer ring with category color
                drawArc(
                    color = category.color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true,
                    topLeft = Offset(
                        centerX - radius,
                        centerY - radius
                    ),
                    size = Size(radius * 2, radius * 2)
                )

                // Highlight largest category with stroke
                if (isLargest && animationProgress.value > 0.8f) {
                    drawArc(
                        color = Color.White,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        style = Stroke(width = 6.dp.toPx()),
                        topLeft = Offset(
                            centerX - radius - 3.dp.toPx(),
                            centerY - radius - 3.dp.toPx()
                        ),
                        size = Size(
                            (radius + 2.dp.toPx()) * 2,
                            (radius + 2.dp.toPx()) * 2
                        )
                    )
                }

                // Add percentage text for larger slices
                val percentage = (value / total * 100).toFloat()
                if (percentage > 8f && animationProgress.value > 0.9f) {
                    val textAngle = startAngle + sweepAngle / 2f
                    val textRadius = radius * 0.7f
                    val textX = centerX + cos(Math.toRadians(textAngle.toDouble())).toFloat() * textRadius
                    val textY = centerY + sin(Math.toRadians(textAngle.toDouble())).toFloat() * textRadius

                    drawContext.canvas.nativeCanvas.drawText(
                        "${percentage.toInt()}%",
                        textX,
                        textY,
                        android.graphics.Paint().apply {
                            color = android.graphics.Color.WHITE
                            textSize = 12.sp.toPx()
                            textAlign = android.graphics.Paint.Align.CENTER
                            isFakeBoldText = true
                            setShadowLayer(4f, 0f, 0f, android.graphics.Color.BLACK)
                        }
                    )
                }

                startAngle += sweepAngle
            }

            // Draw inner circle (donut hole)
            drawCircle(
                color = Color(0xFF659460),
                radius = innerRadius,
                center = Offset(centerX, centerY)
            )

            // Draw border around inner circle
            drawCircle(
                color = Color.Blue,
                radius = innerRadius,
                center = Offset(centerX, centerY),
                style = Stroke(width = 1.dp.toPx())
            )
        }

        // Center content showing total amount
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.alpha(
                if (animationProgress.value > 0.7f) 1f else 0f
            )
        ) {
            Text(
                text = "Total",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "₹${"%.0f".format(total)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}


@Composable
fun ThreeDotMenu(
    onDeleteAll: () -> Unit,
    onDeleteMonth: () -> Unit,
    onSave: () -> Unit,
    onSaveToday: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var Showdialog_month by remember { mutableStateOf(false) }
    var Showdialog_all by remember { mutableStateOf(false) }

    Box() {
        // Three-dot icon
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector =Icons.Default.MoreVert, // the 3-dot icon
                contentDescription = "Menu"
            )
        }

        // Dropdown menu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Delete All") },
                onClick = {
                    Showdialog_all = true
                }
            )
//            ⚠️
            if(Showdialog_all) {
            ConfirmDialog(
                title = "Confirm Delete All",
                message = "Are you sure you want to delete all records? This action cannot be undone.",
                color = Color.Red,
                onConfirm = {
                    onDeleteAll()
                    expanded = false
                    Showdialog_all = false
                },
                onDismiss = { Showdialog_all = false
                              expanded = false }
            )
            }

            DropdownMenuItem(
                text = { Text("Delete current month") },
                onClick = {
                    Showdialog_month = true

                }
            )
            if(Showdialog_month) {
                ConfirmDialog(
                    title = "Confirm Delete ",
                    message = "Are you sure you want to delete this month records? This action cannot be undone.",
                    color = Color.Red,
                    onConfirm = {
                        onDeleteMonth()
                        expanded = false
                        Showdialog_month = false
                    },
                    onDismiss = { Showdialog_month = false
                        expanded = false }
                )
            }
            DropdownMenuItem(
                text = { Text("Save to PDF ( Monthly )") },
                onClick = {
                    expanded = false
                    onSave()
                }
            )
            DropdownMenuItem(
                text = { Text("Save to PDF ( Today )") },
                onClick = {
                    expanded = false
                    onSaveToday()
                }
            )
        }

    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategoryCheckboxes(
    selectedCategories: List<ExpenseCategory>,
    onToggle: (ExpenseCategory) -> Unit
) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            item{
                ExpenseCategory.values().forEach { category ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 4.dp)
                    ) {
                        Checkbox(
                            checked = selectedCategories.contains(category),
                            onCheckedChange = { onToggle(category) },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color(0xFF789AA1),      // color when checked
                                uncheckedColor = Color.Gray,     // color when unchecked
                                checkmarkColor = Color.White      // color of the checkmark
                            ),
                        )
                        Icon(
                            imageVector = category.icon,
                            contentDescription = category.displayName,
                            tint = category.color,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                    }
                }
            }
        }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SlidingTextAnimation(texts: List<String>, modifier: Modifier = Modifier, style : androidx.compose.ui.text.TextStyle = MaterialTheme.typography.titleMedium) {
    var currentIndex by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(4000) // Wait 2 seconds between text changes
            currentIndex = (currentIndex + 1) % texts.size
        }
    }

    AnimatedContent(
        targetState = currentIndex,
        transitionSpec = {
            slideInVertically { height -> height } + fadeIn() with
                    slideOutVertically { height -> -height } + fadeOut()
        },
        modifier = modifier
    ) { targetIndex ->
        Text(
            text = texts[targetIndex],
            style = style,
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.SemiBold

        )
    }
}


