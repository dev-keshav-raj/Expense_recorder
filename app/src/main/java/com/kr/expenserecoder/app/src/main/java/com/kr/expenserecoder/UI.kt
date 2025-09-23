package com.kr.expenserecoder

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Paint
//import android.databinding.tool.writer.Scope
//import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
//import android.text.Layout
//import android.text.StaticLayout
import android.widget.Toast
import android.widget.VideoView
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.border
import androidx.compose.material3.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream
import java.nio.file.WatchEvent
import java.time.LocalDate
import java.time.format.TextStyle
import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.ui.platform.LocalContext

//private var Paint.isFakeBoldText: Boolean
//private var Paint.textSize: Float


fun getCurrentDate(getMonthNo : Boolean = false  ): String {
    if (getMonthNo){
        val monthFormatter = SimpleDateFormat("MM", Locale.getDefault())
        return monthFormatter.format(Date())
    }

    val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return formatter.format(Date())
}

@RequiresApi(Build.VERSION_CODES.O)
fun getMonthName(number: Int): String {
    return LocalDate.now()
        .withMonth(number)
        .month
        .getDisplayName(TextStyle.FULL, Locale.ENGLISH)
}
@SuppressLint("ContextCastToActivity")
@Composable
fun StylishForm(viewModel : MyViewModel) {
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    val context = LocalContext.current

    val activity = LocalContext.current as Activity
    // Capture back gesture
    BackHandler(enabled = true) {
        activity.finishAffinity() // exit app
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) // scroll if needed
            .background(
                brush = Brush.verticalGradient(
                    listOf(Color(0xFF2193b0), Color(0xFF6dd5ed))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp)
                .shadow(8.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f))
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "ExPeNsE",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2193b0)
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { input ->
                        // Allow only alphabets and max length 30
                        if (input.length <= 30 && input.all { it.isLetter() || it.isWhitespace() || it.isDigit() }) {
                            name = input
                        }
                    },
                    label = { Text("Item") },
                    shape = RoundedCornerShape(12.dp),
                    placeholder = { Text("xyz", color = Color.LightGray) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF000000),
                        cursorColor = Color(0xFF2193b0)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )



                OutlinedTextField(
                    value = price,
                    onValueChange = { newValue ->
                        if (newValue.length <= 9) { // limit length to 9
                            // allow only digits and at most one '.'
                            val filtered = buildString {
                                var dotAdded = false
                                for (ch in newValue) {
                                    if (ch.isDigit()) {
                                        append(ch)
                                    } else if (ch == '.' && !dotAdded) {
                                        append(ch)
                                        dotAdded = true
                                    }
                                }
                            }
                            price = filtered
                        }
                    }
                    ,
                    label = { Text("Price") },
                    placeholder = { Text("e.g., 100", color = Color.LightGray) },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF000000),
                        cursorColor = Color(0xFF2193b0)
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = date,
                    onValueChange = {
                       date = it
                    },
                    label = { Text("Date (keep blank for Today)") },
                    placeholder = { Text("${getCurrentDate()} ", color = Color.LightGray) },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF000000),
                        cursorColor = Color(0xFF2193b0)
                    ),
//                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        if (name.isNotBlank() && price.isNotBlank()) {
                            if (date.isBlank()){
                                date = getCurrentDate()
                            }
                            viewModel.insert(name, price.toFloat(),date)
                            Toast.makeText(
                                context,
                                "Record $name with $price Saved",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else{
                            Toast.makeText(
                                context,
                                "Can't Save",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        name = ""
                        price = ""
                        date =""
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    elevation = ButtonDefaults.buttonElevation(4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2193b0),
                        contentColor = Color.White
                    )
                ) {
                    Text("Save", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyScreen(viewModel: MyViewModel) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    val allItems by viewModel.allItems.collectAsState()

    var selectedMonthNumber by remember { mutableStateOf(getCurrentDate(true).toInt()) }
    var selectedMonthName by remember { mutableStateOf(getMonthName(selectedMonthNumber)) }

    val filteredItems = remember(allItems, selectedMonthNumber) {
        allItems.filter { item ->
            val month = item.date.split("-").getOrNull(1)
            month?.toIntOrNull() == selectedMonthNumber
        }
    }

    val total = filteredItems.sumOf { it.price.toInt() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF6dd5ed),Color(0xFF2193b0))
                )
            )
            .padding(8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // 🔹 Month selector row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(1f))
            MonthDropdown(
                selectedMonth = selectedMonthName,
                onMonthSelected = { number ->
                    selectedMonthNumber = number
                    selectedMonthName = getMonthName(number)
                }
            )
        }

        Spacer(Modifier.height(12.dp))

        //  Action Cards Row
        Row(
            modifier = Modifier.fillMaxWidth()
                .background(Color.Transparent),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ActionCard(
                text = "Delete All",
                ImageId = R.drawable.delete_red,
                textColor = Color.Red,
                onClick = {
//                    viewModel.deleteall()
//                    Toast.makeText(context, "All Saved deleted", Toast.LENGTH_SHORT).show()
                    showDialog = true
                },
                onLongClick = {
                    Toast.makeText(context, "Use to Delete all month record at same time", Toast.LENGTH_LONG).show()
                }
            )
            if (showDialog) {
                ConfirmDialog(
                    title = "Confirm Delete",
                    message = "Are you sure to delete all months saved items?",
                    color = Color.Red,
                    onConfirm = {
                        viewModel.deleteall()
                        Toast.makeText(context, "All Saved deleted", Toast.LENGTH_SHORT).show()
                        showDialog = false
                    },
                    onDismiss = {
                        showDialog = false
                    }
                )
            }

            ActionCard(
                text = "Delete Month",
                ImageId = R.drawable.delete_blue,
                textColor = Color.Blue,
                onClick = {
                    try {
                        viewModel.deleteMonth(selectedMonthNumber)
                        Toast.makeText(context, "Month $selectedMonthName deleted", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                    }
                },
                onLongClick = {
                    Toast.makeText(context, "Use to Delete current month record ", Toast.LENGTH_LONG).show()
                }
            )

            ActionCard(
                text = "Save PDF",
                ImageId = R.drawable.download_blue,
                textColor = Color(0xFF4CAF50),
                onClick = {
                    saveToPDF(filteredItems, context, total.toInt())
                },
                onLongClick = {
                    Toast.makeText(context, "Save this month record as PDF in Download/Expense folder", Toast.LENGTH_LONG).show()
                }
            )
        }

        Spacer(Modifier.height(16.dp))

        Divider(color = Color.Black, thickness = 1.dp)

        // 🔹 Section Title
        Text(
            text = "Saved Items",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // 🔹 Total Row
        Row(modifier = Modifier.padding(8.dp)) {
            TableCell("Total:", isHeader = true, weight = 1f)
            TableCell("Rs. $total", isHeader = true, weight = 3f)
        }

        // 🔹 Table
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF2193b0))
                        .padding(vertical = 4.dp)
                ) {
                    TableCell("ID", true)
                    TableCell("Item", true)
                    TableCell("Price", true)
                    TableCell("Date", true)
                }

                // Data
                filteredItems.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        TableCell(item.id.toString())
                        TableCell(item.name)
                        TableCell("Rs. ${item.price}")
                        TableCell(item.date)
                    }
                    Divider()
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ActionCard(
    text: String,
    ImageId: Int,
    textColor: Color = Color.Black,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null // optional
) {
    Card(
        modifier = Modifier
            .size(40.dp) // uniform square
            .combinedClickable(
                onClick = { onClick() },
                onLongClick = { onLongClick?.invoke() }
            ),
        shape = RoundedCornerShape(70.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Image(
            painter = painterResource(id = ImageId),
            contentDescription = text
        )
        // If you want text below image uncomment and wrap in Column
        // Text(
        //     text = text,
        //     fontSize = 10.sp,
        //     color = textColor,
        //     maxLines = 1,
        //     overflow = TextOverflow.Ellipsis
        // )
    }
}


@Composable
fun RowScope.TableCell(
    text: String,
    isHeader: Boolean = false,
    weight: Float = 1f
) {
    Text(
        text = text,
        fontSize = 14.sp,
        fontWeight = if (isHeader) FontWeight.Bold else FontWeight.Normal,
        color = if (isHeader) Color.Black else Color.DarkGray,
        modifier = Modifier
            .weight(weight)
            .padding(4.dp),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}



fun saveToPDF(items: List<MyEntity>, context: Context, total: Int) {
    val pdfDocument = PdfDocument()
    val pageWidth = 595
    val pageHeight = 842
    val marginTop = 50f
    val marginBottom = 50f
    val lineHeight = 25f

    var y = 0f
    var pageNumber = 1

    // Paint setup
    val paint = Paint().apply {
        textSize = 12f
        isFakeBoldText = false
    }

    fun newPage(): PdfDocument.Page {
        val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        // Title
        paint.textSize = 18f
        paint.isFakeBoldText = true
        canvas.drawText("Saved Items At ${getCurrentDate()}", 40f, marginTop, paint)

        // Table Header
        paint.textSize = 14f
        paint.isFakeBoldText = true
        y = marginTop + 50f
        canvas.drawText("ID", 40f, y, paint)
        canvas.drawText("Item", 100f, y, paint)
        canvas.drawText("Price", 300f, y, paint)
        canvas.drawText("Date", 400f, y, paint)

        // Reset for rows
        paint.textSize = 12f
        paint.isFakeBoldText = false
        y += 30f

        return page
    }

    var page = newPage()
    var canvas = page.canvas

    // Draw each item
    for (item in items) {
        if (y + lineHeight > pageHeight - marginBottom) {
            pdfDocument.finishPage(page)
            pageNumber++
            page = newPage()
            canvas = page.canvas
        }

        canvas.drawText(item.id.toString(), 40f, y, paint)
        canvas.drawText(item.name.take(31), 100f, y, paint) // limit long names
        canvas.drawText(item.price.toString(), 300f, y, paint)
        canvas.drawText(item.date, 400f, y, paint)

        y += lineHeight
    }

    // Total on last page
    paint.isFakeBoldText = true
    canvas.drawText("Total : Rs.", 40f, pageHeight - marginBottom, paint)
    canvas.drawText(total.toString(), 120f, pageHeight - marginBottom, paint)

    pdfDocument.finishPage(page)

    // Save file
//    val downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//    if (!downloads.exists()) downloads.mkdirs()
//    val file = File(downloads, "${getCurrentDate()}_Expense_Saved.pdf")

    // Create "Expense" folder inside Downloads
    val downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    val expenseDir = File(downloads, "Expense")

    if (!expenseDir.exists()) {
        expenseDir.mkdirs()  // create folder if not exists
    }

// Now create the file in that directory
    val file = File(expenseDir, "${getCurrentDate()}_Expense_Saved.pdf")

    try {
        pdfDocument.writeTo(FileOutputStream(file))
        Toast.makeText(context, "Saved to ${expenseDir.absolutePath}", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(context, "Error while saving", Toast.LENGTH_SHORT).show()
        e.printStackTrace()
    }
    pdfDocument.close()
}


@Composable
fun DevCont(context: Context) {

    VideoBackgroundBox(videoResId = R.raw.guitar) {

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .padding(16.dp),
                //.fillMaxWidth(),
                shape = RoundedCornerShape(100.dp),
                elevation = CardDefaults.cardElevation(6.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE6F2F5))
//                colors = CardDefaults.cardColors(
//                    containerColor = Color.Transparent
//                )
            ) {
                // Profile Icon
                Image(
                    painter = painterResource(id = R.drawable.devlogo),
                    contentDescription = "Developer",
                    modifier = Modifier.size(60.dp)
                )
            }

            Card(
                modifier = Modifier
                    .padding(16.dp),
                shape = RoundedCornerShape(5.dp),
                elevation = CardDefaults.cardElevation(1.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                )
            ) {
                    Column(
                        modifier = Modifier
                            .background(Color.Transparent),
                    )
                    {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                val intent = Intent(Intent.ACTION_DIAL).apply {
                                    data = "tel:+917292870631".toUri()
                                }
                                context.startActivity(intent)
                            }
                                .padding(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = "Phone",
                                tint = Color(0xFF2E7D32),
                                modifier = Modifier.size(18.dp)
                            )

                            Spacer(modifier = Modifier.weight(1f) .background(Color.Transparent))

                            Text(
                                text = "+91 7292870631",
                                color = Color(0xFF2E7D32),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Black
                                )
                            )
                        }

                        Divider()
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                val intent = Intent(Intent.ACTION_SENDTO).apply {
                                    data =
                                        "mailto:keshavrajbingo@gmail.com".toUri() // recipient email
                                    putExtra(
                                        Intent.EXTRA_SUBJECT,
                                        "Feedback for Expense Recoder App"
                                    )
                                }
                                context.startActivity(intent)
                            }
                                .padding(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "email",
                                tint = Color(0xFF2E7D32),
                                modifier = Modifier.size(18.dp)
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            Text(
                                text = "Keshavrajbingo@gmail.com",
                                color = Color(0xFF2E7D32),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                        Divider()
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    "https://www.github.com/blackshark1213/".toUri()
                                )
                                context.startActivity(intent)
                            }
                                .padding(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "github",
                                tint = Color(0xFF2E7D32),
                                modifier = Modifier.size(18.dp)
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            Text(
                                text = "blackshark1213",
                                color = Color(0xFF2E7D32),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                        Divider()
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    "https://www.linkedin.com/in/dev-keshav".toUri()
                                )
                                context.startActivity(intent)
                            }
                                .padding(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Linkedin",
                                tint = Color(0xFF2E7D32),
                                modifier = Modifier.size(18.dp)
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            Text(
                                text = "dev-keshav",
                                color = Color(0xFF2E7D32),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                }

            } // card end
        }
    }
    }// End of method
// drop down menu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthDropdown(
    selectedMonth: String,
    onMonthSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    // Key = Name, Value = Number
    val months = mapOf(
        "January" to 1, "February" to 2, "March" to 3, "April" to 4,
        "May" to 5, "June" to 6, "July" to 7, "August" to 8,
        "September" to 9, "October" to 10, "November" to 11, "December" to 12
    )

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedMonth,
            onValueChange = { }, // 🔒 disable typing
            readOnly = true,
            label = { Text("Select Month") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor() // 👈 important for correct positioning
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
//            months.forEach { month ->
//                DropdownMenuItem(
//                    text = { Text(month) },
//                    onClick = {
//                        onMonthSelected(month) // ✅ pass selection back
//                        expanded = false
//                    }
//                )
//            }
                months.forEach { (name, number) ->
                    DropdownMenuItem(
                        text = { Text(name) },
                        onClick = {
                            onMonthSelected(number) // ✅ return month number
                            expanded = false
                        }
                    )
                }
            }
        }
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
            Text(text = title, fontWeight = FontWeight.Bold)
        },
        text = {
            Text(message, color = color)
        },
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
