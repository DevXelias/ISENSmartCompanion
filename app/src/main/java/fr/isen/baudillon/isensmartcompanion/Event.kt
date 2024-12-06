package fr.isen.baudillon.isensmartcompanion

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import java.io.Serializable

data class Event(
    val id: String,
    val title: String,
    val description: String,
    val date: String,
    val location: String,
    val category: String,
    var isNotified: MutableState<Boolean> = mutableStateOf(false)) : Serializable