package com.kr.expenserecoder

import android.os.Build
import android.widget.Toast
import androidx.activity.contextaware.ContextAware
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kr.expenserecoder.ui.settings.MyViewModel
import java.nio.file.WatchEvent

// FinanceApp.kt - Fixed

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun FinanceApp() {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showAddExpense by remember { mutableStateOf(false) }
    val viewModel: MyViewModel = viewModel()
    val expenses by viewModel.allItems.collectAsState()
    val context = LocalContext.current

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = selectedTab == index,
                        alwaysShowLabel = false,
                        interactionSource = MutableInteractionSource() ,
                        onClick = { selectedTab = index }
                    )
                }
            }
        },
        floatingActionButton = {
            if (selectedTab == 0) {
                FloatingActionButton(
                    onClick = { showAddExpense = true },
                    containerColor = Color(0xFFBDF2FE),
                    shape = RoundedCornerShape(100.dp),
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Expense", tint = Color.Black)
                }
            }
            if (selectedTab == 1) {
                FloatingActionButton(
                    onClick = { Toast.makeText(context, "Chat with AI coming soon..", Toast.LENGTH_LONG).show() },
                    containerColor = Color(0xFFBDF2FE),
                    shape = RoundedCornerShape(100.dp),
                ) {
                    Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = "Chart with AI", tint = Color.Black)
                }
            }
        }
    ) { paddingValues ->
        when (selectedTab) {
            0 -> HomeScreen(
                expenses = expenses,
                modifier = Modifier.padding(paddingValues),
                viewModel = viewModel
            )
            1 -> AnalyticsScreen(
                expenses = expenses,
                modifier = Modifier.padding(paddingValues)
            )
            2 -> MonthlyScreen(
                expenses = expenses,
                modifier = Modifier.padding(paddingValues),
                viewModel = viewModel
            )
            3 -> ProfileScreen(
                modifier = Modifier.padding(paddingValues),
                viewModel = viewModel
            )
        }

        if (showAddExpense) {
            AddExpenseDialog(
                onDismiss = { showAddExpense = false },
                onEditExpense = { /* No-op for adding */ },
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