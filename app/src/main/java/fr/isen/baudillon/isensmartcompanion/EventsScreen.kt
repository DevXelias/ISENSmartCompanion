package fr.isen.baudillon.isensmartcompanion

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EventsScreen()
{
    val context = LocalContext.current
    val events = listOf(
        Event(1, "Soirée BDE", "Rejoins nous pour une soirée d'enfer", "25/11/2024", "Marseille", "Soirée"),
        Event(2, "Atelier voile", "Fais de la voile avec nous !", "02/06/2025", "Marseille", "Atelier"),
        Event(3, "Global Game Jam", "48h de code intensif", "29/01/25", "Toulon", "Compétition")
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
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

        LazyColumn()
        {
            items(events) { event ->
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
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Black,
                        containerColor = Color.LightGray
                    )
                )
                { Text(event.title, fontSize = 20.sp) }

                Spacer(modifier = Modifier.height(15.dp))
            }
        }


    }
}