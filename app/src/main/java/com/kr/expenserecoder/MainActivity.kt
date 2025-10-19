package com.kr.expenserecoder
// 20 sept 2025 - Fixed Version

// -- new import

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.icons.Icons
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.*
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import java.time.LocalDate
import android.Manifest
import android.app.Notification
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.core.app.NotificationCompat
import androidx.media3.exoplayer.offline.DownloadService
import androidx.media3.exoplayer.offline.DownloadService.startForeground
import com.kr.expenserecoder.ui.settings.MyViewModel

// MainActivity.kt
class MainActivity : ComponentActivity() {
    private val SMS_PERMISSION_REQUEST_CODE = 123
    private val viewModel : MyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        // Request SMS permission
        requestSMSPermissions()

        setContent {
            val context = LocalContext.current
            if(ContextCompat.checkSelfPermission(context ,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1001)
            }


            val showSplash by viewModel.showSplash.collectAsState()

            if (showSplash) {
                VideoSplashScreen(
                    videoResId = R.raw.splash,
                    onVideoFinished = {
                        viewModel.hideSplash()
                    }
                )
            } else {
                FinanceAppTheme {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        FinanceApp()
                    } else {
                        // Fallback for older Android versions
                        Text("This app requires Android O (API 26) or higher")
                    }
                }
            }
        }

    }
//    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestSMSPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
            != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
            != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.READ_SMS,
                    Manifest.permission.POST_NOTIFICATIONS,
                ),
                SMS_PERMISSION_REQUEST_CODE
            )
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                Toast.makeText(this, "SMS permissions granted. Auto expense tracking enabled!",
                    Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "SMS permissions denied. Auto expense tracking disabled.",
                    Toast.LENGTH_LONG).show()
            }
        }
    }
}


//get current month name
fun getCurrentDate(monthOnly: Boolean = false): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val date = LocalDate.now()
        if (monthOnly) {
            String.format("%02d", date.monthValue)
        } else {
            date.toString()
        }
    } else {
        if (monthOnly) "09" else "2024-09-20"
    }
}


// Move ExpenseCategory to match your existing data class structure
enum class ExpenseCategory(val displayName: String, val icon: ImageVector, val color: Color) {
    FOOD("Food", Icons.Default.Restaurant, Color(0xFFFF9800)),
    DRINK("Drink", Icons.Default.NoDrinks, Color(0xFF9AA67E)),
    TRANSPORT("Transport", Icons.Default.DirectionsCar, Color(0xFF2196F3)),
    SHOPPING("Shopping", Icons.Default.ShoppingCart, Color(0xFF4CAF50)),
    ENTERTAINMENT("Entertainment", Icons.Default.Movie, Color(0xFF9C27B0)),
    HEALTH("Health", Icons.Default.LocalHospital, Color(0xFFF44336)),
    UTILITIES("Bills", Icons.Default.Receipt, Color(0xFF607D8B)),
    TRANSFER("Transfer", Icons.Default.Money, Color(0xFFA2DFFA)),

    SMOKE("Smoke", Icons.Default.AutoFixOff, Color(0xFFB6585B)),
}



// Room Database - Fixed version
@Database(entities = [Expense::class], version = 7)
@TypeConverters(RoomConverters::class)
abstract class MyDatabase : RoomDatabase() {
    abstract fun myDao(): MyDao
}



data class BottomNavItem(
    val label: String,
    val icon: ImageVector
)
val bottomNavItems = listOf(
    BottomNavItem("Home", Icons.Default.Home),
    BottomNavItem("Analytics", Icons.Default.BarChart),
    BottomNavItem("Monthly", Icons.Default.CalendarMonth),
    BottomNavItem("Profile", Icons.Default.Person),
//    BottomNavItem("Setting", Icons.Default.Settings)
)



// Theme auto select
@Composable
fun FinanceAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> darkColorScheme()
        else -> lightColorScheme()
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}