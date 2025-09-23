package com.kr.expenserecoder

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kr.expenserecoder.ui.theme.ExpenseRecoderTheme

import androidx.compose.material3.*

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import androidx.core.net.toUri
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch



class MainActivity : ComponentActivity() {
    private val viewModel: MyViewModel by viewModels()
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // MaterialTheme provides basic styling; replace with your actual theme if available.
            MaterialTheme {
                val navController = rememberNavController()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope() // Scope for launching drawer operations

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        AppDrawerContent(
                            onCloseDrawer = { scope.launch { drawerState.close() } },
                            // Add navigation logic here when you have multiple screens
                            onNavigateToUserForm = {
                                navController.navigate("form")
                                scope.launch { drawerState.close() }
                            },
                            onNavigateToShowData = {
                                navController.navigate("show")
                                scope.launch { drawerState.close() }
                            },

                            onNavigateTodevCont = {
                                navController.navigate("dev")
                                scope.launch { drawerState.close() }
                            }
                        )
                    }
                )
                {
                        Scaffold(
                            modifier = Modifier
                                .fillMaxSize(),
                            containerColor = Color.Transparent, // key point
                            topBar = {
                                AppTopBar(
                                    onMenuClick = { scope.launch { drawerState.open() } },
                                    appName = "Expense Recoder",
                                    context = this@MainActivity
                                )
                            }
                        ) { padding ->
                            // The main content of my app, now with padding from the Scaffold's topBar
                            NavHost(
                                navController = navController,
                                startDestination = "form",
                                modifier = Modifier
                                    .padding(padding)
                                    .fillMaxSize()
//                                    .verticalScroll(rememberScrollState())
                            ) {
                                composable("form") { StylishForm(viewModel) }
                                composable("show") { MyScreen(viewModel) }
//                            composable("update") { Update() }
                            composable("dev") { DevCont(context = LocalContext.current) }
                            }
                    }
                }
            }
        }
    }



    // Top Bar ---
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AppTopBar(onMenuClick: () -> Unit, appName: String, context: Context) {
        val animatedBackgroundColor by animateColorAsState(
            targetValue = Color(0xFFE0D09F),
            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
            label = "TopBarColor"
        )
        TopAppBar(
            title = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Card(
                                modifier = Modifier
                                    .padding(2.dp),
                                //.fillMaxWidth(),
                                shape = RoundedCornerShape(80.dp),
                                elevation = CardDefaults.cardElevation(6.dp),
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.logo),
                                    contentDescription = "App Logo",
                                    modifier = Modifier
                                        .size(45.dp)
                                        .background(Color.Transparent)
                                        .clickable {
                                            // EDIT IN FUTURE
                                        }
                                )
                            }
//                            Text("Your Expense", color = Color.Black)
                        }

                }
            },
            navigationIcon = {
                IconButton(onClick = onMenuClick) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Menu",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = animatedBackgroundColor
            )
        )
    }


    // NAV HOST
    @Composable
    fun AppDrawerContent(
        onCloseDrawer: () -> Unit,
        onNavigateToUserForm: () -> Unit,
        onNavigateToShowData:() ->Unit,
        onNavigateTodevCont:() ->Unit,
    ) {
        ModalDrawerSheet {
            // Drawer Header
            var selectedItem by remember { mutableStateOf("DATA") }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(150.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
//
//                Image(
//                    painter = painterResource(id = R.drawable.logo),
//                    contentDescription = "App Icon",
//                    modifier = Modifier.size(150.dp)
//                )
                //tint = MaterialTheme.colorScheme.onSurface)
                Spacer(Modifier.height(8.dp))
            }
            Divider()
            // Drawer Menu Items
            NavigationDrawerItem(
                label = { Text("Add Data") },
                selected = selectedItem == "DATA", // Highlight this item as it's the current screen
                onClick = {
                    onNavigateToUserForm()
                    selectedItem = "DATA"
                    onCloseDrawer()
                },
                icon = { Icon(Icons.Filled.Add, contentDescription = "Add Data") },
                colors = NavigationDrawerItemDefaults.colors(
                    //selectedContainerColor = Color.Blue, // Background when selected
                    selectedTextColor = Color.Black,     // Text when selected
                    selectedIconColor = Color(0xFF0EA3E1),      // Icon when selected
                    unselectedTextColor = Color.Gray,    // Optional
                    unselectedIconColor = Color.Gray     // Optional
                )
            )
            Divider()
            NavigationDrawerItem(
                label = { Text("Show Data") },
                selected = selectedItem == "S_DATA", // Highlight this item as it's the current screen
                onClick = {
                    onNavigateToShowData()
                    selectedItem = "S_DATA"
                    onCloseDrawer()
                },
                icon = { Icon(Icons.Filled.Add, contentDescription = "Show Data") },
                colors = NavigationDrawerItemDefaults.colors(
                    //selectedContainerColor = Color.Blue, // Background when selected
                    selectedTextColor = Color.Black,     // Text when selected
                    selectedIconColor = Color(0xFF0EA3E1),      // Icon when selected
                    unselectedTextColor = Color.Gray,    // Optional
                    unselectedIconColor = Color.Gray     // Optional
                )
            )
            Divider()
            NavigationDrawerItem(
                label = { Text("Contact us") },
                selected = selectedItem == "dev", // Highlight this item as it's the current screen
                onClick = {
                    onNavigateTodevCont()
                    selectedItem = "dev"
                    onCloseDrawer()
                },
                icon = { Icon(Icons.Filled.Email, contentDescription = "Show Dev") },
                colors = NavigationDrawerItemDefaults.colors(
                    //selectedContainerColor = Color.Blue, // Background when selected
                    selectedTextColor = Color.Black,     // Text when selected
                    selectedIconColor = Color(0xFF0EA3E1),      // Icon when selected
                    unselectedTextColor = Color.Gray,
                    unselectedIconColor = Color.Gray
                )
            )

        }
    }

}

