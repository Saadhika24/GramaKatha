package com.example.gramakatha

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Customer(
    val id: Int,
    var name: String,
    var phone: String,
    var balance: Int
)

data class DailyReport(
    val customerName: String,
    val amount: Int,
    val type: String
)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                GramaKathaApp()
            }
        }
    }
}

fun tr(lang: String, key: String): String {

    return when (lang) {

        "Kannada" -> when (key) {
            "appName" -> "ಗ್ರಾಮಕಥಾ"
            "addCustomer" -> "ಗ್ರಾಹಕರನ್ನು ಸೇರಿಸಿ"
            "customerName" -> "ಗ್ರಾಹಕರ ಹೆಸರು"
            "phoneNumber" -> "ಫೋನ್ ಸಂಖ್ಯೆ"
            "amountDue" -> "ಪಡೆಯಬೇಕಾದ ಮೊತ್ತ"
            "advanceBalance" -> "ಮುಂಗಡ ಶೇಷ"
            "add" -> "ಸೇರಿಸಿ"
            "cancel" -> "ರದ್ದುಮಾಡಿ"
            "searchCustomers" -> "ಗ್ರಾಹಕರನ್ನು ಹುಡುಕಿ"
            "customers" -> "ಗ್ರಾಹಕರು"
            "smartLedger" -> "ಸ್ಮಾರ್ಟ್ ಕ್ರೆಡಿಟ್ ಲೆಡ್ಜರ್"
            "totalDue" -> "ಒಟ್ಟು ಬಾಕಿ"
            "today" -> "ಇಂದಿನ ಸಂಗ್ರಹ"
            "amountDueText" -> "ಪಡೆಯಬೇಕಾದುದು"
            "advanceText" -> "ಮುಂಗಡ"
            "selectLanguage" -> "ಭಾಷೆ ಆಯ್ಕೆಮಾಡಿ"
            else -> key
        }

        "Hindi" -> when (key) {
            "appName" -> "ग्रामकथा"
            "addCustomer" -> "ग्राहक जोड़ें"
            "customerName" -> "ग्राहक का नाम"
            "phoneNumber" -> "फोन नंबर"
            "amountDue" -> "लेने वाली राशि"
            "advanceBalance" -> "अग्रिम राशि"
            "add" -> "जोड़ें"
            "cancel" -> "रद्द करें"
            "searchCustomers" -> "ग्राहक खोजें"
            "customers" -> "ग्राहक"
            "smartLedger" -> "स्मार्ट क्रेडिट लेजर"
            "totalDue" -> "कुल बकाया"
            "today" -> "आज का संग्रह"
            "amountDueText" -> "लेना है"
            "advanceText" -> "अग्रिम"
            "selectLanguage" -> "भाषा चुनें"
            else -> key
        }

        else -> when (key) {
            "appName" -> "GramaKatha"
            "addCustomer" -> "Add Customer"
            "customerName" -> "Customer Name"
            "phoneNumber" -> "Phone Number"
            "amountDue" -> "Amount Due"
            "advanceBalance" -> "Advance Balance"
            "add" -> "Add"
            "cancel" -> "Cancel"
            "searchCustomers" -> "Search Customers"
            "customers" -> "Customers"
            "smartLedger" -> "Smart Credit Ledger"
            "totalDue" -> "Total Due"
            "today" -> "Today's Collection"
            "amountDueText" -> "Amount Due"
            "advanceText" -> "Advance"
            "selectLanguage" -> "Select Language"
            else -> key
        }
    }
}

@Composable
fun GramaKathaApp() {

    var search by remember { mutableStateOf("") }
    var selectedLanguage by remember { mutableStateOf("English") }

    var shopName by remember {
        mutableStateOf("Sri Lakshmi Stores")
    }

    var tempShopName by remember {
        mutableStateOf("")
    }

    var showSettingsDialog by remember {
        mutableStateOf(false)
    }

    var showAddDialog by remember {
        mutableStateOf(false)
    }

    var showDailyReport by remember {
        mutableStateOf(false)
    }

    var customerName by remember {
        mutableStateOf("")
    }

    var customerPhone by remember {
        mutableStateOf("")
    }

    var amountDue by remember {
        mutableStateOf("")
    }

    var advanceBalance by remember {
        mutableStateOf("")
    }

    val customers = remember {
        mutableStateListOf(
            Customer(1, "Ravi Kumar", "9876543210", 1200),
            Customer(2, "Suresh", "9123456780", 500),
            Customer(3, "Anjali", "9988776655", -300)
        )
    }

    val dailyReports = remember {
        mutableStateListOf<DailyReport>()
    }

    val filtered = customers.filter {
        it.name.contains(search, true)
    }

    val totalDue = customers.sumOf {
        if (it.balance > 0) it.balance else 0
    }

    val totalAdvance = customers.sumOf {
        if (it.balance < 0) -it.balance else 0
    }

    // DAILY REPORT SCREEN

    if (showDailyReport) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0D2B5C))
                    .padding(16.dp),

                verticalAlignment = Alignment.CenterVertically
            ) {

                TextButton(
                    onClick = {
                        showDailyReport = false
                    }
                ) {
                    Text(
                        text = "←",
                        color = Color.White,
                        fontSize = 24.sp
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "Daily Report",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),

                shape = RoundedCornerShape(20.dp)
            ) {

                Column(
                    modifier = Modifier.padding(20.dp)
                ) {

                    Text(
                        text = "$shopName — Daily Collection Report",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "5/13/2026")

                    Spacer(modifier = Modifier.height(30.dp))

                    Text(text = "Collected: ₹$totalAdvance")

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "Given on credit: ₹$totalDue")

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "Total Outstanding: ₹${totalDue - totalAdvance}")
                }
            }

            Button(
                onClick = {
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(60.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2D5B91)
                ),

                shape = RoundedCornerShape(18.dp)
            ) {

                Text(
                    text = "Copy Report",
                    fontSize = 18.sp
                )
            }

            LazyColumn {

                items(dailyReports) { report ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {

                            Text(
                                text = report.customerName,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(5.dp))

                            Text(text = "Amount: ₹${report.amount}")

                            Text(text = "Type: ${report.type}")
                        }
                    }
                }
            }
        }

        return
    }

    // MAIN SCREEN

    Scaffold(

        floatingActionButton = {

            Column {

                FloatingActionButton(
                    onClick = {
                        showDailyReport = true
                    },

                    containerColor = Color(0xFF1565C0)
                ) {
                    Text("📄")
                }

                Spacer(modifier = Modifier.height(12.dp))

                FloatingActionButton(
                    onClick = {
                        showAddDialog = true
                    },

                    containerColor = Color(0xFF4CAF50)
                ) {
                    Text("+")
                }
            }
        }

    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF5F5F5))
        ) {

            TopSection(
                customers = customers,
                selectedLanguage = selectedLanguage,
                shopName = shopName,

                onSettingsClick = {
                    tempShopName = shopName
                    showSettingsDialog = true
                }
            )

            OutlinedTextField(
                value = search,
                onValueChange = {
                    search = it
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),

                label = {
                    Text(tr(selectedLanguage, "searchCustomers"))
                }
            )

            Text(
                text = tr(selectedLanguage, "customers"),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp)
            )

            LazyColumn {

                items(filtered) { customer ->

                    CustomerCard(
                        customer = customer,
                        selectedLanguage = selectedLanguage,
                        shopName = shopName,

                        onDelete = {
                            customers.remove(customer)
                        },

                        onReportAdded = { report ->
                            dailyReports.add(report)
                        }
                    )
                }
            }
        }

        // SETTINGS DIALOG

        if (showSettingsDialog) {

            AlertDialog(
                onDismissRequest = {
                    showSettingsDialog = false
                },

                title = {
                    Text("Settings")
                },

                text = {

                    Column {

                        Text(
                            text = "Shop Name",
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = tempShopName,
                            onValueChange = {
                                tempShopName = it
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = tr(selectedLanguage, "selectLanguage"),
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                selectedLanguage = "English"
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("English")
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                selectedLanguage = "Kannada"
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("ಕನ್ನಡ")
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                selectedLanguage = "Hindi"
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("हिन्दी")
                        }
                    }
                },

                confirmButton = {

                    Button(
                        onClick = {
                            shopName = tempShopName
                            showSettingsDialog = false
                        }
                    ) {
                        Text("Save")
                    }
                },

                dismissButton = {

                    Button(
                        onClick = {
                            showSettingsDialog = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }

        // ADD CUSTOMER DIALOG

        if (showAddDialog) {

            AlertDialog(
                onDismissRequest = {
                    showAddDialog = false
                },

                title = {
                    Text(tr(selectedLanguage, "addCustomer"))
                },

                text = {

                    Column {

                        OutlinedTextField(
                            value = customerName,
                            onValueChange = {
                                customerName = it
                            },

                            label = {
                                Text(tr(selectedLanguage, "customerName"))
                            }
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = customerPhone,
                            onValueChange = {
                                customerPhone = it
                            },

                            label = {
                                Text(tr(selectedLanguage, "phoneNumber"))
                            }
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = amountDue,
                            onValueChange = {
                                amountDue = it
                            },

                            label = {
                                Text(tr(selectedLanguage, "amountDue"))
                            }
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = advanceBalance,
                            onValueChange = {
                                advanceBalance = it
                            },

                            label = {
                                Text(tr(selectedLanguage, "advanceBalance"))
                            }
                        )
                    }
                },

                confirmButton = {

                    Button(
                        onClick = {

                            customers.add(

                                Customer(
                                    id = customers.size + 1,
                                    name = customerName,
                                    phone = customerPhone,

                                    balance =
                                        (amountDue.toIntOrNull() ?: 0) -
                                                (advanceBalance.toIntOrNull() ?: 0)
                                )
                            )

                            customerName = ""
                            customerPhone = ""
                            amountDue = ""
                            advanceBalance = ""

                            showAddDialog = false
                        }
                    ) {
                        Text(tr(selectedLanguage, "add"))
                    }
                },

                dismissButton = {

                    Button(
                        onClick = {
                            showAddDialog = false
                        }
                    ) {
                        Text(tr(selectedLanguage, "cancel"))
                    }
                }
            )
        }
    }
}

@Composable
fun TopSection(
    customers: List<Customer>,
    selectedLanguage: String,
    shopName: String,
    onSettingsClick: () -> Unit
) {

    val totalDue = customers.sumOf {
        if (it.balance > 0) it.balance else 0
    }

    val totalAdvance = customers.sumOf {
        if (it.balance < 0) -it.balance else 0
    }

    val todayCollection = totalAdvance

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1E88E5))
            .padding(16.dp)
    ) {

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column {

                Text(
                    text = shopName,
                    color = Color.Yellow,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = tr(selectedLanguage, "appName"),
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = tr(selectedLanguage, "smartLedger"),
                    color = Color.White
                )
            }

            IconButton(
                onClick = {
                    onSettingsClick()
                }
            ) {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = "",
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {

            StatCard(
                tr(selectedLanguage, "totalDue"),
                "₹$totalDue"
            )

            StatCard(
                tr(selectedLanguage, "customers"),
                customers.size.toString()
            )

            StatCard(
                tr(selectedLanguage, "today"),
                "₹$todayCollection"
            )
        }
    }
}

@Composable
fun StatCard(title: String, value: String) {

    Card(
        modifier = Modifier
            .width(110.dp)
            .height(90.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.2f)
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            Text(
                text = title,
                color = Color.White,
                fontSize = 12.sp
            )

            Text(
                text = value,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun CustomerCard(
    customer: Customer,
    selectedLanguage: String,
    shopName: String,
    onDelete: () -> Unit,
    onReportAdded: (DailyReport) -> Unit
) {

    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),

        shape = RoundedCornerShape(16.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF4CAF50)),

                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        Icons.Default.Person,
                        contentDescription = "",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = customer.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Text(
                        text = customer.phone,
                        color = Color.Gray
                    )
                }

                IconButton(
                    onClick = {
                        onDelete()
                    }
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "",
                        tint = Color.Red
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text =
                    if (customer.balance >= 0)

                        "${tr(selectedLanguage, "amountDueText")} ₹${customer.balance}"

                    else

                        "${tr(selectedLanguage, "advanceText")} ₹${-customer.balance}",

                color =
                    if (customer.balance >= 0)
                        Color.Red
                    else
                        Color(0xFF2E7D32),

                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Button(
                    onClick = {

                        val message =
                            "Your due at $shopName is ₹${customer.balance}. Please pay soon."

                        val intent = Intent(Intent.ACTION_VIEW)

                        intent.data = Uri.parse(
                            "https://wa.me/91${customer.phone}?text=${Uri.encode(message)}"
                        )

                        context.startActivity(intent)

                        onReportAdded(
                            DailyReport(
                                customer.name,
                                customer.balance,
                                "WhatsApp Reminder"
                            )
                        )
                    }
                ) {
                    Text("WhatsApp")
                }

                Button(
                    onClick = {

                        val smsIntent = Intent(Intent.ACTION_VIEW)

                        smsIntent.data = Uri.parse("sms:${customer.phone}")

                        smsIntent.putExtra(
                            "sms_body",
                            "Your due at $shopName is ₹${customer.balance}. Please pay soon."
                        )

                        context.startActivity(smsIntent)

                        onReportAdded(
                            DailyReport(
                                customer.name,
                                customer.balance,
                                "SMS Reminder"
                            )
                        )
                    }
                ) {
                    Text("SMS")
                }
            }
        }
    }
}

