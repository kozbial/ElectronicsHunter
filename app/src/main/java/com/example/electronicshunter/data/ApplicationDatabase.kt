package com.example.electronicshunter.data

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.electronicshunter.data.daos.ObservedItemDao
import com.example.electronicshunter.data.entities.ObservedItem

@Database(
    entities = [
        ObservedItem::class
    ],
    exportSchema = false,
    version = 1
)
abstract class ApplicationDatabase : RoomDatabase() {

    companion object {
        private const val databaseName = "electronicshunter_roomdb"

        fun getDatabase(context: Context) =
            Room.databaseBuilder(context, ApplicationDatabase::class.java, databaseName)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
    }

    abstract fun getItemDao() : ObservedItemDao
}