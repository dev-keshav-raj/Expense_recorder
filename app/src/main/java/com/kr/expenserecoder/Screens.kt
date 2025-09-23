package com.kr.expenserecoder

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
    var showAddExpense by remember { mutableStateOf(false) }
    val context = LocalContext.current


//    val total = filteredItems.sumOf { it.price.toInt() }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = "Today's Expenses ₹",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = today.format(DateTimeFormatter.ofPattern("EEEE, MMMM d")),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Today's Total",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Rs.${"%.2f".format(todaysTotal)}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF619D69)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Quick Stats",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
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
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                            )
                            val weekAgo = today.minusDays(7)
                            val weeklyTotal = expenses.filter {
                                it.localDate?.isAfter(weekAgo) == true
                            }.sumOf { it.amount }
                            Text(
                                text = "Rs.${"%.2f".format(weeklyTotal)}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Column {
                            Text(
                                text = "This Month",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                            )
                            val monthlyTotal = expenses.filter {
                                it.localDate?.month == today.month
                            }.sumOf { it.amount }
                            Text(
                                text = "Rs.${"%.2f".format(monthlyTotal)}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
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
                            onClick = { showAddExpense = true } ) {
                            Text("Add your first expense")
                        }

                        if (showAddExpense) {
                            AddExpenseDialog(
                                onDismiss = { showAddExpense = false },
                                onAddExpense = { expense ->
                                    viewModel.insert(
                                        amount = expense.amount,
                                        category = expense.category,
                                        description = expense.description,
                                        date = expense.date
                                    )
                                    showAddExpense = false
                                }
                            )
                        }
                    }
                }
            }
        } else {
            items(todaysExpenses) { expense ->
                ExpenseItem(
                    expense = expense,
                    onEdit = { /* Handle edit */ },
                    onDelete = { viewModel.deletItem(expense.id)
                        Toast.makeText(context,"Expense deleted", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonthlyScreen(
    expenses: List<Expense>,
    modifier: Modifier = Modifier,
    viewModel : MyViewModel = viewModel()
) {
    val context = LocalContext.current

    var selectedMonthNumber by remember { mutableStateOf(getCurrentDate(true).toInt()) }
    var selectedMonthName by remember { mutableStateOf(getMonthName(selectedMonthNumber)) }
//
    val filteredItems = remember(expenses, selectedMonthNumber) {
        expenses.filter { item ->
            val month = item.date.split("-").getOrNull(1)
            month?.toIntOrNull() == selectedMonthNumber
        }
    }

        val total : Double = filteredItems.sumOf { it.amount } // hit and try

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = "Monthly Expenses",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = selectedMonthName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${selectedMonthName} Total",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "₹ ${"%.2f".format(total)}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        item{
            Card(
                modifier = Modifier.fillMaxWidth(),
//                colors = CardDefaults.cardColors(
//                    containerColor = MaterialTheme.colorScheme.primaryContainer
//                )
            ) {
                Column (
                    modifier = Modifier.padding(16.dp)
                ){
                    PieChart(
                        data = filteredItems.groupBy { it.category }
                            .mapValues { entry -> entry.value.sumOf { it.amount }}
                    )
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
                    onDeleteMonth = { viewModel.deleteMonth(getCurrentDate(true).toInt())
                        Toast.makeText(context,"Deleted all expenses of $selectedMonthName", Toast.LENGTH_SHORT).show()
                    },
                    onDeleteAll = { viewModel.deleteAll()
                        Toast.makeText(context,"All expenses deleted", Toast.LENGTH_SHORT).show()
                    },
                    onSave = {
                        saveToPDF(filteredItems, context, total.toDouble())
                    },
                    )
            }
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
                    onEdit = { /* Handle edit */ },
                    onDelete = { viewModel.deletItem(expense.id)
                        Toast.makeText(context,"Expense deleted", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AnalyticsScreen(
    expenses: List<Expense>,
    modifier: Modifier = Modifier
) {
    val currentMonth = LocalDate.now().month
    val monthlyTotal = expenses.filter {
        it.localDate?.month == currentMonth
    }.sumOf { it.amount }

    val categoryTotals = expenses
        .filter { it.localDate?.month == currentMonth }
        .groupBy { it.category }
        .mapValues { it.value.sumOf { expense -> expense.amount } }

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
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
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
                            value = "₹ ${"%.2f".format(if(dayOfMonth > 0) monthlyTotal / dayOfMonth else 0.0)}",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Category Breakdown",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    categoryTotals.forEach { (category, total) ->
                        val percentage = if (monthlyTotal > 0) (total / monthlyTotal) * 100 else 0.0
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
@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    var showSetting by remember { mutableStateOf(false) }
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
                        contentDescription = "Settings",
                        Modifier.size(19.dp)
                    )
                    Spacer(modifier = Modifier.padding(4.dp))

                    Text("Setting", fontSize = 18.sp)
                }
            }
        }
    }
        item{
            if(showSetting){
                AutoTrackingSettings()
            }
        }

                item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Update Coming Soon...",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Wait Until next Update",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

//        item {
//            Card(modifier = Modifier.fillMaxWidth()) {
//                Column(modifier = Modifier.padding(16.dp)) {
//                    Text(
//                        text = "Monthly Budget",
//                        style = MaterialTheme.typography.bodyMedium,
//                        fontWeight = FontWeight.Medium
//                    )
//                    Text(
//                        text = "Rs.2,000",
//                        style = MaterialTheme.typography.headlineSmall,
//                        fontWeight = FontWeight.Bold,
//                        color = MaterialTheme.colorScheme.primary
//                    )
//                }
//            }
//        }
//
//        item {
//            Card(modifier = Modifier.fillMaxWidth()) {
//                Column(modifier = Modifier.padding(16.dp)) {
//                    Text(
//                        text = "Settings & Preferences",
//                        style = MaterialTheme.typography.bodyMedium,
//                        fontWeight = FontWeight.Medium
//                    )
//                    Text(
//                        text = "Currency, notifications, categories",
//                        style = MaterialTheme.typography.bodySmall,
//                        color = MaterialTheme.colorScheme.onSurfaceVariant
//                    )
//                }
//            }
//        }
    }
} // END OF PROFILE SCREEN

//@Composable
//fun SettingsScreen(modifier: Modifier = Modifier){
//    LazyColumn(
//        modifier = modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp)
//    ) {
//        item{
//            Text(
//                text = "Settings",
//                style = MaterialTheme.typography.headlineMedium,
//                fontWeight = FontWeight.Bold
//            )
//        }
//
//        item{
//            AutoTrackingSettings()
//        }
//    }
//}
