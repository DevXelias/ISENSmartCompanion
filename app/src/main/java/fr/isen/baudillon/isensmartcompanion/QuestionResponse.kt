package fr.isen.baudillon.isensmartcompanion

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "question_response_table")
data class QuestionResponse(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val question: String,
    val response: String,
    val timestamp: Long
)
