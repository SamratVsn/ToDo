package com.example.todovsn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.todovsn.ui.ToDoApp
import com.example.todovsn.ui.theme.ToDoVsnTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ToDoVsnTheme {
                ToDoApp()
            }
        }
    }
}