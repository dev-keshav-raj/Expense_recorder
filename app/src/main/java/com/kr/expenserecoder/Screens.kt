package com.kr.expenserecoder

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush.Companion.horizontalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kr.expenserecoder.ui.settings.MyViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.delay

// Screens.kt - Fixed
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    expenses: List<Expense>,
    modifier: Modifier = Modifier,
    viewModel : MyViewModel = viewModel()
) {
    val today = LocalDate.now()
    val todaysExpenses = expenses.filter {
        it.localDate == today
    }
    val todaysTotal = todaysExpenses.sumOf { it.amount }
    var expenseToEdit by remember { mutableStateOf<Expense?>(null) }
    var showAddExpense by remember { mutableStateOf(false) }
    var radius by remember { mutableStateOf(0.dp) }
    val context = LocalContext.current
    var expand by remember { mutableStateOf(false) }

    val brush = horizontalGradient(
        colors = listOf(
            Color(0xFFFFE0E0),
            Color(0xFFBDF2FE)
        )
    )

    LaunchedEffect(Unit) {
        delay(40)
        expand = true
    }

//    val total = filteredItems.sumOf { it.price.toInt() }

//    VideoBackgroundBox(R.raw.background) {

        // Top text
        WavyBounceText("Today's Expenses ₹")

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, top = 50.dp)
                .blur(radius),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
//                    Text(
//                        text = "Today's Expenses ₹",
//                        style = MaterialTheme.typography.headlineMedium,
//                        fontWeight = FontWeight.Bold
//                    )
                        Text(
                            text = today.format(DateTimeFormatter.ofPattern("EEEE, MMMM d")),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
//                    Text(
//                        text = "Today's Total",
//                        style = MaterialTheme.typography.bodySmall,
//                        color = MaterialTheme.colorScheme.onSurfaceVariant
//                    )
                        Text(
                            text = "₹ ${"%.2f".format(todaysTotal)}",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
//                Column(horizontalAlignment = Alignment.End) {
//                }
                }
            }


            item {
                Card(
                    modifier = Modifier.fillMaxWidth() .background(brush, shape = RoundedCornerShape(17.dp)),
//                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Transparent
                    ),
                    border = CardDefaults.outlinedCardBorder(true)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp).background(Color.Transparent).animateContentSize(),
                    ) {
                        if (expand) {
                            Text(
                                text = "Quick Stats",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = "This Week",
                                        style = MaterialTheme.typography.bodySmall,
//                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                                        color = Color.Black.copy(alpha = 0.7f)
                                    )
                                    val weekAgo = today.minusDays(7)
                                    val weeklyTotal = expenses.filter {
                                        it.localDate?.isAfter(weekAgo) == true
                                    }.sumOf { it.amount }
                                    Text(
                                        text = "₹ ${"%.2f".format(weeklyTotal)}",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                }
                                Column {
                                    Text(
                                        text = "This Month",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Black.copy(alpha = 0.7f)
                                    )
                                    val monthlyTotal = expenses.filter {
                                        it.localDate?.month == today.month
                                    }.sumOf { it.amount }
                                    Text(
                                        text = "₹ ${"%.2f".format(monthlyTotal)}",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )

                                }
                            }
                        }// Expand end
                    }
                }
            }

            item {
                Text(
                    text = "Today's Transactions",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }


            if (todaysExpenses.isEmpty()) {
                item {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "No expenses recorded today",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            TextButton(
                                onClick = { showAddExpense = true }) {
                                Text("Add your first expense")
                            }

                            if (showAddExpense) {
                                radius = 8.dp
                                AddExpenseDialog(
                                    onDismiss = {
                                        showAddExpense = false
                                        radius = 0.dp
                                    },
                                    onEditExpense = { },
                                    isupdate = false,
                                    onAddExpense = { expense ->
                                        viewModel.insert(
                                            amount = expense.amount,
                                            category = expense.category,
                                            description = expense.description,
                                            date = expense.date
                                        )
                                        showAddExpense = false
                                        radius = 0.dp
                                    }
                                )
                            }
                        }
                    }
                }
                item {
                    SmoothGifPlayer(R.raw.emptycatu)
                }
            } else {

                itemsIndexed(todaysExpenses) { index, expense ->
//                ExplosionParticles(
//                    exploded = true,
//                    particleCount = 100,
//                    modifier = Modifier.size(800.dp, 10.dp)
//                )
                    ExpenseItem(
                        expense = expense,
                        Key = index.toInt(),
                        onClick = { /* Handle item click if needed */ },
                        onEdit = { expenseToEdit = expense },
                        onDelete = {
                            viewModel.deletItem(expense.id)
                            Toast.makeText(context, "Expense deleted", Toast.LENGTH_SHORT).show()
                        },
                        index = index,
//                    modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }

        }
//    }
    // Show the dialog for the selected expense
    expenseToEdit?.let { selectedExpense ->
        AddExpenseDialog(
            isupdate = true,
            expenseToEdit = selectedExpense,
            onDismiss = { expenseToEdit = null },
            onAddExpense = { /* No-op for editing */ },
            onEditExpense = { updatedExpense ->
                viewModel.updateExpense(
                    id = updatedExpense.id,
                    amount = updatedExpense.amount,
                    category = updatedExpense.category,
                    description = updatedExpense.description,
                    date = updatedExpense.date
                )
                expenseToEdit = null
                Toast.makeText(context, "Expense Updated", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonthlyScreen(
    expenses : List<Expense> ,
    modifier: Modifier = Modifier,
    viewModel : MyViewModel = viewModel()
) {
    val context = LocalContext.current
    var selectedMonthNumber by remember { mutableStateOf(getCurrentDate(true).toInt()) }
    var selectedMonthName by remember { mutableStateOf(getMonthName(selectedMonthNumber)) }

        // filter for months
    val filteredItems_all = remember(expenses, selectedMonthNumber) {
        expenses.filter { item ->
            val month = item.date.split("-").getOrNull(1)
            month?.toIntOrNull() == selectedMonthNumber
        }
    }

    var expenseToEdit by remember { mutableStateOf<Expense?>(null) }
    var expenseclick by remember { mutableStateOf<Expense?>(null) }

    //--new code added --

    val expenses by viewModel.filteredExpenses.collectAsState()
    val selectedCategories by viewModel.selectedCategories.collectAsState()

    // filter for cato
    val filteredItems = expenses.filter { item ->
        val month = item.date.split("-").getOrNull(1)?.toIntOrNull()
        month == selectedMonthNumber &&
                (selectedCategories.isEmpty() || item.category in selectedCategories)
    }
    val total : Double = filteredItems.sumOf { it.amount }

    // for todays
    val today = LocalDate.now()
    val todaysExpenses = expenses.filter {
        it.localDate == today
    }
    val todaysTotal = todaysExpenses.sumOf { it.amount }


    val budget by viewModel.monthlyLimit.collectAsState(initial = 0) // Int
    var totalPer by remember { mutableStateOf(0.0) }
    totalPer = ((filteredItems_all.sumOf { it.amount })/budget) * 100

    // username

    val username by viewModel.username.collectAsState(initial = "User")
    val texts = listOf("Hello, $username", "Your Monthly Breakdown")




    SlidingTextAnimation(texts = texts,
        modifier = Modifier.padding(16.dp),
        style = MaterialTheme.typography.headlineMedium)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 16.dp , end = 16.dp , top = 50.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
//                    Text(
//                        text = "Monthly Expenses",
//                        style = MaterialTheme.typography.headlineMedium,
//                        fontWeight = FontWeight.Bold
//                    )
//                    Text(
//                        text = selectedMonthName,
//                        style = MaterialTheme.typography.bodyMedium,
//                        color = MaterialTheme.colorScheme.onSurfaceVariant
//                    )
                    Text(
                        text = "${selectedMonthName} Total",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "₹ ${"%.2f".format(filteredItems_all.sumOf { it.amount })}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                    Text(
                        text = ("Your Expense is ${"%.2f".format(totalPer)}% of your total budget"),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
//                Column(horizontalAlignment = Alignment.End) {
//                }
            }
        }

        item{
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp),
                border = CardDefaults.outlinedCardBorder(true)
            ) {
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(6.dp)

                ){
                    PieChart(
                        data = filteredItems.groupBy { it.category }
                            .mapValues { entry -> entry.value.sumOf { it.amount }}
                    )

                    Card(
                        modifier = Modifier.size(250.dp),
                       shape =  MaterialTheme.shapes.small
                    ){
                        when (totalPer) {
                            in 0.0..5.0 -> SmoothGifPlayer(R.raw.ss)
                            in 5.1..9.99 -> SmoothGifPlayer(R.raw.a)
                            in 10.0..19.99 -> SmoothGifPlayer(R.raw.b)
                            in 20.0..29.99 -> SmoothGifPlayer(R.raw.c)
                            in 30.0..39.99 -> SmoothGifPlayer(R.raw.d)
                            in 40.0..49.99 -> SmoothGifPlayer(R.raw.e)
                            in 50.0..59.99 -> SmoothGifPlayer(R.raw.f)
                            in 60.0..69.99 -> SmoothGifPlayer(R.raw.g)
                            in 70.0..79.99 -> SmoothGifPlayer(R.raw.h)
                            in 80.0..89.99 -> SmoothGifPlayer(R.raw.i)
                            in 90.0..94.99 -> SmoothGifPlayer(R.raw.j)
                            in 95.0..100.00 -> SmoothGifPlayer(R.raw.k)
                            else -> SmoothGifPlayer(R.raw.l)
                        }

                    }

                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MonthWithMenu(
                    selectedMonth = selectedMonthName,
                    onMonthSelected = { number ->
                        selectedMonthNumber = number
                        selectedMonthName = getMonthName(number)
                    },
                    onDeleteMonth = { viewModel.deleteMonth(selectedMonthNumber)
                        Toast.makeText(context,"Deleted all expenses of $selectedMonthName", Toast.LENGTH_SHORT).show()
                    },
                    onDeleteAll = { viewModel.deleteAll()
                        Toast.makeText(context,"All expenses deleted", Toast.LENGTH_SHORT).show()
                    },
                    onSave = {
                        saveToPDF(filteredItems, context, total.toDouble())
                    },
                    onSaveToday = {
                        saveToPDF(todaysExpenses, context, todaysTotal.toDouble())
                    }
                    )
            }
        }

        item{
            CategoryCheckboxes(
                selectedCategories = selectedCategories,
                onToggle = { category -> viewModel.toggleCategory(category) }
            )
        }


        item {
            Text(
                text = "Transactions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }

        if (filteredItems.isEmpty()) {
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No expenses recorded found",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextButton(
                            onClick = {   /* Handle  */  } ) {
                            Text("Are your press delete all ")
                        }
                    }
                }
            }
        } else {
            items(filteredItems) { expense ->
                ExpenseItem(
                    expense = expense,
                    onClick = {
                        expenseclick = expense
                    },
                    onEdit = { expenseToEdit = expense },
                    onDelete = { viewModel.deletItem(expense.id)
                        Toast.makeText(context,"Expense deleted", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
    expenseclick?.let { selectedExpense ->
            ExpenseClick(
                expenseToEdit = selectedExpense,
                onDismiss = { expenseclick = null },
            )
        }

    // Show the dialog only once, for the selected expense
    expenseToEdit?.let { selectedExpense ->
        AddExpenseDialog(
            isupdate = true,
            expenseToEdit = selectedExpense,
            onDismiss = { expenseToEdit = null },
            onAddExpense = { /* No-op for editing */ },
            onEditExpense = { updatedExpense ->
                viewModel.updateExpense(
                    id = updatedExpense.id,
                    amount = updatedExpense.amount,
                    category = updatedExpense.category,
                    description = updatedExpense.description,
                    date = updatedExpense.date
                )
                expenseToEdit = null
                Toast.makeText(context, "Expense Updated", Toast.LENGTH_SHORT).show()
            }
        )
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AnalyticsScreen(
    expenses: List<Expense>,
    modifier: Modifier = Modifier
) {
    var expand by remember { mutableStateOf(false) }
    val currentMonth = LocalDate.now().month
    val monthlyTotal = expenses.filter {
        it.localDate?.month == currentMonth
    }.sumOf { it.amount }

    val categoryTotals = expenses
        .filter { it.localDate?.month == currentMonth }
        .groupBy { it.category }
        .mapValues { it.value.sumOf { expense -> expense.amount } }

    // Launch a coroutine when the composable enters composition
    LaunchedEffect(Unit) {
            delay(50) // Wait .5 seconds between text changes
            expand = true
    }


    val texts = listOf("Category Breakdown", "Tap to ${if(expand) "collapse" else "expand"}")

    VideoBackgroundBox(R.raw.background) {

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Analytics",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp),
                    border = CardDefaults.outlinedCardBorder(true)
                )
                {
                    Column(
                        modifier = Modifier.padding(16.dp)
//                    .animateContentSize() // smoothly resizes as children appear/disappear
//                    .background(Color.LightGray)
//                    .clickable { expand = !expand }
                    ) {
                        Text(
                            text = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
//                    if (expand) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            StatCard(
                                title = "Total Spent",
                                value = "₹ ${"%.2f".format(monthlyTotal)}",
                                color = MaterialTheme.colorScheme.error
                            )
                            val dayOfMonth = LocalDate.now().dayOfMonth
                            StatCard(
                                title = "Daily Average",
                                value = "₹ ${"%.2f".format(if (dayOfMonth > 0) monthlyTotal / dayOfMonth else 0.0)}",
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
//                    }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                        .clickable { expand = !expand }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                            .animateContentSize()
                    ) {
//                    Text(
//                        text = "Category Breakdown",
//                        style = MaterialTheme.typography.titleMedium,
//                        fontWeight = FontWeight.SemiBold
//                    )
                        SlidingTextAnimation(texts = texts)

                        if (expand) {
                            Spacer(modifier = Modifier.height(16.dp))

                            categoryTotals.forEach { (category, total) ->
                                val percentage =
                                    if (monthlyTotal > 0) (total / monthlyTotal) * 100 else 0.0
                                CategoryItem(
                                    category = category,
                                    amount = total,
                                    percentage = percentage
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    color: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

//Profile Screen
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileScreen(

    modifier: Modifier = Modifier,
    viewModel : MyViewModel = viewModel()
) {
    val username by viewModel.username.collectAsState(initial = "User")
    val limit by viewModel.monthlyLimit.collectAsState(initial = 0)

    var showSetting by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Profile",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Hello, $username",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Medium
            )
        }
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Monthly Budget",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "₹ $limit",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        item {
            IconButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { showDialog = !showDialog }
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person2,
                            contentDescription = "update",
                            modifier = Modifier.size(19.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "Settings & Preferences",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }


        item {
            IconButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { showSetting = !showSetting }) {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "SMS access",
                            Modifier.size(19.dp)
                        )
                        Spacer(modifier = Modifier.padding(4.dp))
                        Text(
                            text = "SMS Access",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
        item {
            if (showSetting) {
                AutoTrackingSettings(viewModel)
            }
        }
    }
//
    if (showDialog) {
        ModalBottomSheetUI(
            viewModel = viewModel,
            onDismiss = {
                showDialog = false
            }
        )
    }

} // END OF PROFILE SCREEN
