package com.bazi.app.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.bazi.app.data.model.Profile

/**
 * 八字应用数据库
 */
@Database(
    entities = [Profile::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class BaziDatabase : RoomDatabase() {
    
    abstract fun profileDao(): ProfileDao
    
    companion object {
        @Volatile
        private var INSTANCE: BaziDatabase? = null
        
        fun getDatabase(context: Context): BaziDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BaziDatabase::class.java,
                    "bazi_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}