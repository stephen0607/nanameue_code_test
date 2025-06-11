package com.example.nanameue_code_test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.nanameue_code_test.ui.theme.Nanameue_code_testTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Nanameue_code_testTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    NavigationStack()
                }
            }
        }
    }
}