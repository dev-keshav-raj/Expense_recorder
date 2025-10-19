package com.kr.expenserecoder

// SMSReceiver.kt
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.telephony.SmsMessage
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.kr.expenserecoder.ui.settings.MyViewModel
import com.kr.expenserecoder.ui.settings.dataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

import androidx.datastore.preferences.core.booleanPreferencesKey

private val AUTO_TRACKING_KEY: Preferences.Key<Boolean> =
    booleanPreferencesKey("auto_tracking")


class SMSReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val bundle = intent.extras
            if (bundle != null) {
                val pdus = bundle.get("pdus") as Array<*>

                for (pdu in pdus) {
                    val smsMessage = SmsMessage.createFromPdu(pdu as ByteArray)
                    val sender = smsMessage.displayOriginatingAddress
                    val messageBody = smsMessage.displayMessageBody


                    // ✅ Access DataStore directly instead of ViewModel
                    val appContext = context.applicationContext
                    val dataStore = (appContext as Application).dataStore

                    CoroutineScope(Dispatchers.IO).launch {
                        val isAutoTrackingEnabled = dataStore.data
                            .map { prefs -> prefs[AUTO_TRACKING_KEY] ?: true }
                            .first()  // ✅ get current value once

                        if (isAutoTrackingEnabled) {
                            // Only process bank debit transactions
                            if (isBankDebitTransaction(sender, messageBody)) {
                                val expense = parseDebitTransaction(messageBody)
                                expense?.let {
                                    saveExpenseToDatabase(context, it)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun isBankDebitTransaction(sender: String, message: String): Boolean {
        // Common Indian bank sender IDs

        val bankSenders = listOf(
            // NEW added
            "EQUTAS", "EQUITAS", // Equitas Small Finance Bank
            "SLICT", "SLCEIT", "SLICIT", // SLICIT Small Finance Bank
            "AU", "AUBANK", "AUBL", // AU Small Finance Bank
            "FINSERV", "FINB", // Bajaj Finserv
            "IDBI", "IDBIBANK", // IDBI Bank
            "TMB", "TMBL", "TAMILNAD", // Tamilnad Mercantile Bank
            "DCB", "DCBBANK", // DCB Bank

            // SBI group
            "SBI", "SBIINB", "SBIPSG", "SBICRD", "SBICRD", "SBIPAY","SBYONO","YONO",
            // HDFC
            "HDFCBANK", "HDFC", "HDFCCR", "HDFCCB", "HDFCPY",
            // ICICI
            "ICICIBANK", "ICICI", "ICICIPR", "ICICICR", "ICICIP",
            // Axis
            "AXISBANK", "AXIS", "AXISUPI", "AXISCR","AXISBK",
            // Kotak
            "KOTAK", "KOTAKB", "KMBL", "KOTAKUPI",
            // Yes Bank
            "YESBANK", "YESBNK", "YESUPI",
            // IndusInd
            "INDUSIND", "INDUSB", "INDUSUPI",
            // PNB
            "PNB", "PNBANK", "PNBCRD",
            // Bank of Baroda
            "BOB", "BARODA", "BOBCARD",
            // Canara
            "CANARA", "CANBNK", "CANUPI",
            // Indian Overseas Bank
            "IOB", "IOBBNK",
            // Union Bank
            "UBI", "UNIONB", "UNION",
            // IDFC First
            "IDFC", "IDFCFB", "IDFCCB",
            // Federal Bank
            "FEDERAL", "FEDBNK",

            // UPI Apps
            "PAYTM", "PAYTMB", "PAYTMP", "PAYTMUPI",
            "GPAY", "GOOGLE", "GOOGLEPAY",
            "PHONEPE", "PHONEPEP", "PHONPYEUPI","PPAY",
            "BHIM", "BHIMUPI",
            "TEZ", // Old Google Pay
            // Flipkart
            "FLIPKART", "FLPKRT",
            // Swiggy
            "SWIGGY", "SWIGGYP",
            // Zomato
            "ZOMATO", "ZOMATOP",
            // Ola
            "OLA", "OLAPAY",
            // Uber
            "UBER", "UBERPAY",
            // Rapido
            "RAPIDO", "RAPIDOP",
            // Namma Yatri
            "NAMMA", "YATRI",
            // Myntra
            "MYNTRA", "MYNTRAP",
            // Nykaa
            "NYKAA", "NYKAAP",
            // Ajio
            "AJIO", "AJIOP",
            // BigBasket
            "BIGBASKET", "BBKART",
            // Domino's
            "DOMINOS", "DOMINOSP",
            // McDonald's
            "MCDONALDS", "MCDONALDSP",
            // KFC
            "KFC", "KFCPAY",
            // BookMyShow
            "BOOKMYSHOW", "BMS",
            // Netflix
            "NETFLIX", "NFLX",
            // Amazon Prime
            "AMAZONPRIME", "AMZPRIME",
            // Hotstar
            "HOTSTAR", "DISNEY",
            // Spotify
            "SPOTIFY", "SPOTIFYP",
            //Amazon Pay
            "AMAZON", "AMZPAY","APAY","PAY",
            // Others
            "CITI", "CITIBANK", "HSBC", "SCBANK", "STANDARDCHARTERED",
            "RBL", "RBLBNK", "RBLCRD",
            "KVB", "SOUTHIND", "UCO", "SYND", "CORP", "INDIANBANK"
        )

        val dltSenderPattern = Regex("^[A-Z]{2}-[A-Z0-9]{4,9}-[ST]$") // Generic pattern for Bank SMS senders

        // Debit keywords only
        val debitKeywords = listOf(
            "debited", "withdrawn", "spent", "paid", "purchase", "debit",
            "payment", "transaction", "charged", "sent", "transferred",
            "transfer", "withdrawal", "deducted", "sent to", "to",
            "auto-debit", "upi", "purchase at", "purchase from", "mandate" ,"auto-pay"
        )

//        val isBankSender =
//            dltSenderPattern.matches(sender) || bankSenders.any { sender.contains(it, ignoreCase = true) }
////            dltSenderPattern.matches(sender)

        val normalizedSender = sender.uppercase()
        val isBankSender = dltSenderPattern.matches(sender) ||
                bankSenders.any { normalizedSender.contains(it) }


        val isDebitTransaction = debitKeywords.any {
            message.contains(it, ignoreCase = true)
        }

        // Exclude credit transactions
        val creditKeywords = listOf("credited", "deposited", "received", "refund","OTP")
        val isCreditTransaction = creditKeywords.any {
            message.contains(it, ignoreCase = true)
        }

        return isBankSender && isDebitTransaction && !isCreditTransaction
    }

    private fun parseDebitTransaction(message: String): Expense? {
        try {
            // Extract amount - Common patterns in Indian bank SMS

            // Require Rs/INR/₹ before the amount
//            val amountPattern = """(?:Rs\.?\s*|INR\s*|₹\s+)(\d+(?:,\d{3})*(?:\.\d{1,2})?)""".toRegex()
            val amountPattern = """(?i)(?:rs\.?\s*|inr\s*|₹\s*)(\d+(?:,\d{3})*(?:\.\d{1,2})?)""".toRegex()



            val amountMatch = amountPattern.find(message)
            val amount = amountMatch?.groupValues?.get(1)?.replace(",", "")?.toDoubleOrNull()


            if (amount == null || amount <= 0) return null

            // Extract merchant/description
            val description = extractDescription(message)

            // Auto-categorize the expense
            val category = categorizeExpense(description, message)

            return Expense(
                amount = amount,
                category = category,
                description = description,
                date = getCurrentDate()
            )

        } catch (e: Exception) {
            return null
        }
    }

    private fun extractDescription(message: String): String {
        // Try to extract merchant name or description
        val patterns = listOf(
            """(?:at|to)\s+([A-Z][A-Z\s&.]+)""".toRegex(), // "at AMAZON" or "to SWIGGY"
            """(?:spent at|paid to)\s+([A-Z][A-Z\s&.]+)""".toRegex(), // "spent at McDONALDS"
            """([A-Z][A-Z\s&.]+)\s+(?:transaction|payment)""".toRegex() // "FLIPKART transaction"
        )

        for (pattern in patterns) {
            val match = pattern.find(message)
            if (match != null) {
                return match.groupValues[1].trim()
            }
        }

        // Fallback: Look for any capitalized words
        val capitalWordsPattern = """[A-Z][A-Z\s&.]{3,}""".toRegex()
        val capitalMatch = capitalWordsPattern.find(message)

        return capitalMatch?.value?.trim() ?: "Bank Transaction"
    }

    private fun categorizeExpense(description: String, fullMessage: String): ExpenseCategory {
        val text = (description + " " + fullMessage).lowercase()

        return when {
            // Food & Dining
            text.contains("swiggy") || text.contains("zomato") ||
                    text.contains("dominos") || text.contains("mcdonalds") ||
                    text.contains("kfc") || text.contains("restaurant") ||
                    text.contains("cafe") || text.contains("hotel") ||
                    text.contains("food") -> ExpenseCategory.FOOD

            // Transportation
            text.contains("uber") || text.contains("ola") ||
                    text.contains("rapido") || text.contains("namma yatri") ||
                    text.contains("petrol") || text.contains("fuel") ||
                    text.contains("metro") || text.contains("bus") ||
                    text.contains("taxi") -> ExpenseCategory.TRANSPORT

            // Shopping
            text.contains("amazon") || text.contains("flipkart") ||
                    text.contains("myntra") || text.contains("ajio") ||
                    text.contains("nykaa") || text.contains("shopping") ||
                    text.contains("mall") || text.contains("store") ||
                    text.contains("purchase") -> ExpenseCategory.SHOPPING

            // Entertainment
            text.contains("netflix") || text.contains("amazon prime") ||
                    text.contains("hotstar") || text.contains("spotify") ||
                    text.contains("youtube") || text.contains("bookmyshow") ||
                    text.contains("movie") || text.contains("cinema") -> ExpenseCategory.ENTERTAINMENT

            // Health
            text.contains("apollo") || text.contains("pharmacy") ||
                    text.contains("medical") || text.contains("hospital") ||
                    text.contains("clinic") || text.contains("doctor") ||
                    text.contains("medicine") -> ExpenseCategory.HEALTH

            // Utilities & Bills
            text.contains("electricity") || text.contains("water") ||
                    text.contains("gas") || text.contains("internet") ||
                    text.contains("mobile") || text.contains("recharge") ||
                    text.contains("bill") || text.contains("payment") -> ExpenseCategory.UTILITIES

            else -> ExpenseCategory.TRANSFER // Default category
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveExpenseToDatabase(context: Context, expense: Expense) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Create ViewModel instance
                val viewModel = MyViewModel(context.applicationContext as Application)

                // Insert the expense
                viewModel.insert(
                    amount = expense.amount,
                    category = expense.category,
                    description = expense.description,
                    date = expense.date
                )

                // Show notification to user
                showExpenseNotification(context, expense)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ServiceCast")
    private fun showExpenseNotification(context: Context, expense: Expense) {
        // Create notification channel first
        createNotificationChannel(context)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val appContext = context.applicationContext
        val intent = Intent(appContext, AudioService::class.java)
        intent.putExtra("resId", R.raw.cashsound)
        appContext.startForegroundService(intent)

        val notification = NotificationCompat.Builder(context, "expense_auto_add")
            .setSmallIcon(R.drawable.logo) // Use your app icon
            .setContentTitle("Expense Auto-Added")
            .setContentText("${expense.category.displayName}: ₹${expense.amount} - ${expense.description}")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setAutoCancel(true)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Amount: ₹${expense.amount}\nCategory: ${expense.category.displayName}\nDescription: ${expense.description}"))
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "expense_auto_add",
                "Auto Added Expenses",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Notifications for automatically added expenses from SMS"
            }

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}
/*
// Add this to your MainActivity.kt for permission handling
class MainActivity : ComponentActivity() {
    private val SMS_PERMISSION_REQUEST_CODE = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Request SMS permission
        requestSMSPermissions()

        setContent {
            FinanceAppTheme {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    FinanceApp()
                } else {
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
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                Toast.makeText(this, "SMS permissions granted. Auto expense tracking enabled!", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "SMS permissions denied. Auto expense tracking disabled.", Toast.LENGTH_LONG).show()
            }
        }
    }
}

// Settings UI to enable/disable auto-tracking
@Composable
fun AutoTrackingSettings() {
    var isAutoTrackingEnabled by remember { mutableStateOf(true) }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
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
                        text = "Automatically add expenses from bank SMS",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Switch(
                    checked = isAutoTrackingEnabled,
                    onCheckedChange = { isAutoTrackingEnabled = it }
                )
            }

            if (isAutoTrackingEnabled) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Only debit/expense transactions will be added automatically",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
 */