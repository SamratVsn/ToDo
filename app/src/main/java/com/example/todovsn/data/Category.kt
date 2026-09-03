package com.example.todovsn.data

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "Categories")
data class Category(
    @PrimaryKey
    val name: String
)