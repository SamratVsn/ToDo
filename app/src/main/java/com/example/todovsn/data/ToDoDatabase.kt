package com.example.todovsn.data

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.room3.TypeConverter
import androidx.room3.TypeConverters
import java.time.LocalDate

@TypeConverters(DateConverters::class)
@Database(entities = [ToDoItem::class], version = 2, exportSchema = false)
abstract class ToDoDatabase : RoomDatabase() {
    abstract fun toDoDao() : ToDoDao

    companion object {
        @Volatile
        private var Instance: ToDoDatabase? = null

        fun getDatabase(context: Context): ToDoDatabase {
            return Instance?: synchronized(this) {
                Room.databaseBuilder(context, ToDoDatabase::class.java, "todo_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}

class DateConverters {

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? {
        return date?.toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it) }
    }
}