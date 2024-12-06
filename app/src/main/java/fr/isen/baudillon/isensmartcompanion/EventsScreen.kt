package fr.isen.baudillon.isensmartcompanion

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun EventsScreen() {
    val context = LocalContext.current
    val eventList = remember { mutableStateOf<List<Event>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            isLoading = true
            responseBehavior(eventList, context)



        } finally {
            isLoading = false
        }
    }
    loadNotificationPreferences(eventList, context)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Événements",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            textAlign = TextAlign.Center,
            color = Color.Red
        )

        Spacer(modifier = Modifier.height(30.dp))

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
        } else if (eventList.value.isEmpty()) {
            Text(
                text = "Aucun événement disponible.",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 16.dp),
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        } else {
            LazyColumn {
                items(eventList.value) { event ->
                    Row()
                    {

                        Button(
                            onClick = {
                                val intent = Intent(context, EventDetailActivity::class.java).apply {
                                    putExtra("eventId", event.id)
                                    putExtra("eventTitle", event.title)
                                    putExtra("eventDescription", event.description)
                                    putExtra("eventDate", event.date)
                                    putExtra("eventLocation", event.location)
                                    putExtra("eventCategory", event.category)
                                }
                                context.startActivity(intent)
                            },
                            modifier = Modifier
                                .width(350.dp)
                                .height(75.dp),
                            colors = ButtonDefaults.buttonColors(
                                contentColor = Color.Black,
                                containerColor = Color.LightGray
                            )
                        ) {
                            Text(event.title,fontSize = 20.sp)

                        }



                        IconButton(
                            onClick = {

                                event.isNotified.value = !event.isNotified.value
                                saveNotificationPreference(
                                    event,
                                    context
                                )
                                if (event.isNotified.value) {
                                    delayNotification(context, event.title)

                                }
                            }


                        ) {
                            Icon(
                                imageVector = if (event.isNotified.value) Icons.Default.Notifications else Icons.Default.NotificationsOff,
                                contentDescription = "Notification",
                                tint = Color.Black,


                            )
                        }
                    }


                    Spacer(modifier = Modifier.height(15.dp))
                }
            }
        }
    }
}

fun responseBehavior(eventList: MutableState<List<Event>>, context: Context) {
    RetrofitInstance.api.getEvents().enqueue(object : Callback<List<Event>> {
        override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
            if (response.isSuccessful) {
                val events = response.body()?.map { event ->
                    event.copy(isNotified = mutableStateOf(false)) // Initialisation ici
                } ?: emptyList()
                eventList.value = events
            } else {
                Toast.makeText(context, "Erreur serveur : ${response.code()}", Toast.LENGTH_LONG)
                    .show()
            }
        }

        override fun onFailure(call: Call<List<Event>>, t: Throwable) {
            Toast.makeText(context, "Erreur de chargement : ${t.message}", Toast.LENGTH_LONG).show()
        }
    })
}

fun saveNotificationPreference(event: Event, context: Context) {
    val sharedPreferences = context.getSharedPreferences("user_preferences", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putBoolean("event_${event.id}_notification", event.isNotified.value)
    editor.apply()
}

fun loadNotificationPreferences(events: MutableState<List<Event>>, context: Context) {
    val sharedPreferences = context.getSharedPreferences("user_preferences", Context.MODE_PRIVATE)
    events.value.forEach { event ->
        val isNotified = sharedPreferences.getBoolean("event_${event.id}_notification", false)
        event.isNotified.value = isNotified // Mets à jour l'état mutable
    }
}



