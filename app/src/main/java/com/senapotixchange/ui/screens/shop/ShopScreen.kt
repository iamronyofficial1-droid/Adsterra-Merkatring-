package com.senapotixchange.ui.screens.shop

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.senapotixchange.data.model.OrderDto
import com.senapotixchange.data.model.ProductDto
import com.senapotixchange.data.repository.ExchangeRepository
import com.senapotixchange.ui.components.*
import com.senapotixchange.ui.theme.*

@Composable
fun ShopScreen(
    repository: ExchangeRepository,
    onNavigate: (String) -> Unit
) {
    val currentUser by repository.currentUser.collectAsState()
    val products = repository.getProducts()
    var selectedProductForBuy by remember { mutableStateOf<ProductDto?>(null) }
    var completedOrder by remember { mutableStateOf<OrderDto?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
        ) {
            // Header Balance Banner
            item {
                NebulaCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Color(0xFF1B2034),
                    borderColor = AccentGold.copy(alpha = 0.5f)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Current Credit Balance", color = TextSecondary, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = AccentGold, modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = String.format("%.1f Credits", currentUser.credits),
                                    color = TextPrimary,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(AccentGold.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = AccentGold, modifier = Modifier.size(22.dp))
                        }
                    }
                }
            }

            item {
                Text("Credit Packages", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            items(products) { product ->
                NebulaCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = SurfaceCard,
                    borderColor = if (product.popular) PrimaryBlue else SurfaceCardBorder
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(product.name, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    if (product.popular) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(PrimaryBlue.copy(alpha = 0.2f))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text("MOST POPULAR", color = PrimaryBlue, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(product.description, color = TextSecondary, fontSize = 12.sp)
                            }

                            Text(
                                text = "$${String.format("%.2f", product.priceUsd)}",
                                color = AccentGold,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 20.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.AddCircle, contentDescription = null, tint = AccentGreen, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${product.creditsAmount.toInt()} + ${product.bonusCredits.toInt()} Bonus",
                                    color = AccentGreen,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Button(
                                onClick = { selectedProductForBuy = product },
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text("Purchase", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }
        }

        // Checkout Modal
        if (selectedProductForBuy != null) {
            CheckoutDialog(
                product = selectedProductForBuy!!,
                onDismiss = { selectedProductForBuy = null },
                onConfirm = { method ->
                    val order = repository.purchaseCredits(selectedProductForBuy!!, method)
                    selectedProductForBuy = null
                    completedOrder = order
                }
            )
        }

        // Order Complete Dialog
        if (completedOrder != null) {
            AlertDialog(
                onDismissRequest = { completedOrder = null },
                title = { Text("Payment Successful!", color = TextPrimary, fontWeight = FontWeight.Bold) },
                text = {
                    Text(
                        "Order #${completedOrder?.orderId} processed successfully. +${completedOrder?.amount?.toInt()} Credits have been added to your balance.",
                        color = TextSecondary
                    )
                },
                confirmButton = {
                    Button(
                        onClick = { completedOrder = null },
                        colors = ButtonDefaults.buttonColors(containerColor = AccentGreen)
                    ) {
                        Text("Done", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                },
                containerColor = SurfaceDark,
                shape = RoundedCornerShape(16.dp)
            )
        }
    }
}

@Composable
fun CheckoutDialog(
    product: ProductDto,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var selectedMethod by remember { mutableStateOf("USDT / Crypto") }
    val paymentMethods = listOf("USDT / Crypto", "PayPal", "Credit Card", "Google Pay")

    Dialog(onDismissRequest = onDismiss) {
        NebulaCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = SurfaceDark,
            borderColor = PrimaryBlue
        ) {
            Column {
                Text("Complete Purchase", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text("${product.name} • ${product.creditsAmount.toInt() + product.bonusCredits.toInt()} Credits", color = AccentGold, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)

                Spacer(modifier = Modifier.height(16.dp))
                Text("Select Payment Method", color = TextSecondary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(8.dp))

                paymentMethods.forEach { method ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (selectedMethod == method) SurfaceCardElevated else SurfaceCard)
                            .border(1.dp, if (selectedMethod == method) PrimaryBlue else SurfaceCardBorder, RoundedCornerShape(10.dp))
                            .clickable { selectedMethod = method }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(method, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                        RadioButton(
                            selected = selectedMethod == method,
                            onClick = { selectedMethod = method },
                            colors = RadioButtonDefaults.colors(selectedColor = PrimaryBlue)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Total Amount", color = TextSecondary, fontSize = 11.sp)
                        Text("$${String.format("%.2f", product.priceUsd)}", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }

                    Row {
                        TextButton(onClick = onDismiss) {
                            Text("Cancel", color = TextSecondary)
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Button(
                            onClick = { onConfirm(selectedMethod) },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Pay Now", color = TextPrimary, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
