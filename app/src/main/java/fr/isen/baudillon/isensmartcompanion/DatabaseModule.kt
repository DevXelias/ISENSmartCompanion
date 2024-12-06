package fr.isen.baudillon.isensmartcompanion

import android.content.Context

object DatabaseModule {


    @Volatile
    private var database: AppDatabase? = null


    private fun getDatabase(context: Context): AppDatabase {
        return database ?: synchronized(this) {
            val instance = AppDatabase.getDatabase(context)
            database = instance
            instance
        }
    }


    fun getDao(context: Context): QuestionResponseDao {
        return getDatabase(context).questionResponseDao()
    }
}
