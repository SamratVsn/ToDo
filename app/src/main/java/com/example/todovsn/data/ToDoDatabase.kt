package com.example.todovsn.data

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.room3.TypeConverter
import androidx.room3.TypeConverters
import androidx.room3.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@TypeConverters(DateConverters::class)
@Database(entities = [ToDoItem::class, Category::class], version = 7, exportSchema = true)
abstract class ToDoDatabase : RoomDatabase() {
    abstract fun toDoDao() : ToDoDao
    abstract fun categoryDao() : CategoryDao

    companion object {
        @Volatile
        private var Instance: ToDoDatabase? = null

        val MIGRATION_6_7 = object : Migration(6, 7) {
            override suspend fun migrate(connection: SQLiteConnection) {
                connection.execSQL("CREATE TABLE IF NOT EXISTS `Categories` (`name` TEXT NOT NULL, PRIMARY KEY(`name`))")
                // Pre-populate default categories
                val defaults = listOf("Study", "Work", "Productive", "Personal", "Important")
                defaults.forEach { name ->
                    connection.execSQL("INSERT OR IGNORE INTO Categories (name) VALUES ('$name')")
                }
            }
        }

        fun getDatabase(context: Context): ToDoDatabase {
            return Instance?: synchronized(this) {
                Room.databaseBuilder(context, ToDoDatabase::class.java, "todo_database")
                    .addCallback(object : Callback() {
                        override suspend fun onCreate(connection: SQLiteConnection) {
                            super.onCreate(connection)
                            val defaults = listOf("Study", "Work", "Productive", "Personal", "Important")
                            defaults.forEach { name ->
                                connection.execSQL("INSERT OR IGNORE INTO Categories (name) VALUES ('$name')")
                            }
                        }
                    })
                    .addMigrations(MIGRATION_6_7)
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

    @TypeConverter
    fun fromLocalTime(time: LocalTime?): String? {
        return time?.toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toLocalTime(value: String?): LocalTime? {
        return value?.let { LocalTime.parse(it) }
    }

    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime?): String? {
        return dateTime?.toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }
}