package com.senapotixchange

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.senapotixchange.data.repository.ExchangeRepository
import com.senapotixchange.ui.navigation.MainScreen
import com.senapotixchange.ui.theme.SenapotiExchangeTheme

class MainActivity : ComponentActivity() {
    private val repository = ExchangeRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SenapotiExchangeTheme {
                MainScreen(repository = repository)
            }
        }
    }
}
