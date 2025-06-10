package com.example.nanameue_code_test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.nanameue_code_test.di.appModule
import com.example.nanameue_code_test.ui.theme.Nanameue_code_testTheme
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startKoin {
            androidContext(this@MainActivity)
            modules(appModule)
        }
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