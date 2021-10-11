package com.example.android.manifestproject.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [GuestEntity::class], version = 1, exportSchema = false)
abstract class ManifestoDatabase : RoomDatabase() {

    //Connects the database to the DAO.
    abstract val manifestoDatabaseDao: ManifestoDatabaseDao


    //Companion object that allows you to add functions in the manifestodatabase class
    companion object {

        //Makes sure you dont accidentally reinitialize your database multiple times
        @Volatile
        private var INSTANCE: ManifestoDatabase? = null

        /**
        Singleton pattern. If a database has already been retrieved, then return the previous
        database, otherwise, make a new database.
        */

        fun getInstance(context: Context): ManifestoDatabase {

            synchronized(this) {
                var instance = INSTANCE

                if (instance == null)
                {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ManifestoDatabase::class.java,
                        "guest_history_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}