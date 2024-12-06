package fr.isen.baudillon.isensmartcompanion

import android.content.Context
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class CalendarEvent(
    val date: Date,
    val title: String,
    val isSchoolClass: Boolean = false,
    val description: String = ""
)

@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier
) {
    var selectedDate by remember { mutableStateOf<Date?>(null) }
    var currentCalendar by remember {
        mutableStateOf(Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 1)
        })
    }
    var showEventDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val schoolEvents = remember {
        val calendar = Calendar.getInstance()
        listOf(
            CalendarEvent(
                calendar.apply { add(Calendar.DAY_OF_MONTH, 1) }.time,
                "Mathématiques 10h-12h",
                isSchoolClass = true,
                "Salle 101"
            ),
            CalendarEvent(
                calendar.apply { add(Calendar.DAY_OF_MONTH, 2) }.time,
                "Physique 15h-17h30",
                isSchoolClass = true,
                "Salle 203"
            ),
            CalendarEvent(
                    calendar.apply { add(Calendar.DAY_OF_MONTH, 3) }.time,
            "Informatique 13h-15h\n" +
                    "Géographie 15h30-17h30",
            isSchoolClass = true,
            "Salle B54"
            )
        )
    }

    var events by remember {
        mutableStateOf(schoolEvents + loadEvents(context))
    }


    val monthYearFormatter = remember { SimpleDateFormat("MMMM yyyy", Locale.FRANCE) }
    val dayFormatter = remember { SimpleDateFormat("d", Locale.FRANCE) }
    val fullDateFormatter = remember { SimpleDateFormat("MMMM d, yyyy", Locale.FRANCE) }

    Column(modifier = modifier.fillMaxWidth()) {
        // Calendar Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {




            IconButton(onClick = {
                currentCalendar = Calendar.getInstance().apply {
                    time = currentCalendar.time
                    add(Calendar.MONTH, -1)
                    set(Calendar.DAY_OF_MONTH, 1)
                }
            }) {
                Text("<")
            }

            Text(
                monthYearFormatter.format(currentCalendar.time),
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )



            IconButton(onClick = {
                currentCalendar = Calendar.getInstance().apply {
                    time = currentCalendar.time
                    add(Calendar.MONTH, 1)
                    set(Calendar.DAY_OF_MONTH, 1)
                }
            }) {
                Text(">")
            }
        }


        Row(modifier = Modifier.fillMaxWidth()) {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
            repeat(7) {
                Text(
                    SimpleDateFormat("EEE", Locale.FRANCE).format(calendar.time),
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
                calendar.add(Calendar.DAY_OF_WEEK, 1)
            }
        }

        // Calendar grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth()
        ) {

            val firstDayOffset = currentCalendar.get(Calendar.DAY_OF_WEEK) - currentCalendar.firstDayOfWeek


            items(firstDayOffset) {
                Box(modifier = Modifier
                    .aspectRatio(1f)
                    .padding(4.dp))
            }


            val daysInMonth = currentCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            items(daysInMonth) { day ->
                val date = Calendar.getInstance().apply {
                    time = currentCalendar.time
                    set(Calendar.DAY_OF_MONTH, day + 1)
                }.time

                val hasEvent = events.any { event ->
                    val eventCal = Calendar.getInstance().apply { time = event.date }
                    val dateCal = Calendar.getInstance().apply { time = date }
                    eventCal.get(Calendar.YEAR) == dateCal.get(Calendar.YEAR) &&
                            eventCal.get(Calendar.MONTH) == dateCal.get(Calendar.MONTH) &&
                            eventCal.get(Calendar.DAY_OF_MONTH) == dateCal.get(Calendar.DAY_OF_MONTH)
                }

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(4.dp)
                        .border(
                            width = 1.dp,
                            color = if (selectedDate == date) MaterialTheme.colorScheme.primary
                            else Color.LightGray,
                            shape = MaterialTheme.shapes.small
                        )
                        .clickable {
                            selectedDate = date
                            showEventDialog = true
                        }
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = dayFormatter.format(date),
                            modifier = Modifier.padding(4.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        if (hasEvent) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = CircleShape
                                    )
                            )
                        }
                    }
                }
            }
        }
    }

    if (showEventDialog && selectedDate != null) {
        var newEventTitle by remember { mutableStateOf("") }
        val dateEvents = events.filter { event ->
            val eventCal = Calendar.getInstance().apply { time = event.date }
            val selectedCal = Calendar.getInstance().apply { time = selectedDate!! }
            eventCal.get(Calendar.YEAR) == selectedCal.get(Calendar.YEAR) &&
                    eventCal.get(Calendar.MONTH) == selectedCal.get(Calendar.MONTH) &&
                    eventCal.get(Calendar.DAY_OF_MONTH) == selectedCal.get(Calendar.DAY_OF_MONTH)
        }

        Dialog(onDismissRequest = { showEventDialog = false }) {
            Surface(
                modifier = Modifier.padding(16.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        fullDateFormatter.format(selectedDate!!),
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))


                    if (dateEvents.isNotEmpty()) {
                        Text("Evènements pour ce jour:", style = MaterialTheme.typography.titleSmall)
                        dateEvents.forEach { event ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Text(
                                    event.title,
                                    modifier = Modifier.padding(8.dp),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }


                    Text("Ajouter un évènement", style = MaterialTheme.typography.titleSmall)
                    TextField(
                        value = newEventTitle,
                        onValueChange = { newEventTitle = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        placeholder = { Text("Entrez le titre") },
                        singleLine = true
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextButton(
                            onClick = { showEventDialog = false }
                        ) {
                            Text("Annuler")
                        }

                        Button(
                            onClick = {
                                if (newEventTitle.isNotEmpty()) {
                                    val newEvent = CalendarEvent(
                                        selectedDate!!,
                                        newEventTitle,
                                        isSchoolClass = false  // Explicitly mark as not a school event
                                    )
                                    events = schoolEvents + loadEvents(context) + newEvent
                                    saveEvents(events, context)
                                    showEventDialog = false
                                }
                            },
                            enabled = newEventTitle.isNotEmpty()
                        ) {
                            Text("Ajouter")
                        }
                    }
                }
            }
        }
    }
}

fun saveEvents(events: List<CalendarEvent>, context: Context) {
    val sharedPreferences = context.getSharedPreferences("user_events", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()


    val userEvents = events.filter { !it.isSchoolClass }

    val gson = Gson()
    val eventsJson = gson.toJson(userEvents)

    editor.putString("events_list", eventsJson)
    editor.apply()
}

fun loadEvents(context: Context): List<CalendarEvent> {
    val sharedPreferences = context.getSharedPreferences("user_events", Context.MODE_PRIVATE)
    val eventsJson = sharedPreferences.getString("events_list", null)


    if (eventsJson.isNullOrEmpty()) {
        return emptyList()
    }


    val gson = Gson()
    val type = object : TypeToken<List<CalendarEvent>>() {}.type
    return gson.fromJson(eventsJson, type)
}