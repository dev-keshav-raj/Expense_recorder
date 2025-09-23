package com.kr.expenserecoder
// 20 sept 2025 - Fixed Version

import android.content.Context
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

// -- new import
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
import android.widget.Toast

// MainActivity.kt
class MainActivity : ComponentActivity() {
    private val SMS_PERMISSION_REQUEST_CODE = 123
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
    private fun requestSMSPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
            != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.READ_SMS
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
    TRANSPORT("Transport", Icons.Default.DirectionsCar, Color(0xFF2196F3)),
    SHOPPING("Shopping", Icons.Default.ShoppingCart, Color(0xFF4CAF50)),
    ENTERTAINMENT("Entertainment", Icons.Default.Movie, Color(0xFF9C27B0)),
    HEALTH("Health", Icons.Default.LocalHospital, Color(0xFFF44336)),
    UTILITIES("Bills", Icons.Default.Receipt, Color(0xFF607D8B))
}



// Room Database - Fixed version
@Database(entities = [Expense::class], version = 4) // Changed version to 4
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
    BottomNavItem("Monthly Analysis", Icons.Default.CalendarMonth),
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
