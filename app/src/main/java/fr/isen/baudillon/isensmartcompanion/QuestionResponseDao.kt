package fr.isen.baudillon.isensmartcompanion

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionResponseDao {

    @Query("SELECT * FROM question_response_table ORDER BY timestamp DESC")
    fun getAll(): Flow<List<QuestionResponse>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(questionResponse: QuestionResponse)

    @Delete
    suspend fun delete(questionResponse: QuestionResponse)

    @Query("DELETE FROM question_response_table")
    suspend fun deleteAll()
}
