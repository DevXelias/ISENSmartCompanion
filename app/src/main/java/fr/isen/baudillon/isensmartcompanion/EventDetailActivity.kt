package fr.isen.baudillon.isensmartcompanion


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class EventDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val eventId = intent.getIntExtra("eventId", -1)
        val eventTitle = intent.getStringExtra("eventTitle") ?: "Titre inconnu"
        val eventDescription = intent.getStringExtra("eventDescription") ?: "Description manquante"
        val eventDate = intent.getStringExtra("eventDate") ?: "Date inconnue"
        val eventLocation = intent.getStringExtra("eventLocation") ?: "Lieu inconnu"
        val eventCategory = intent.getStringExtra("eventCategory") ?: "Catégorie inconnue"


        setContent {
            EventDetailScreen(
                title = eventTitle,
                description = eventDescription,
                date = eventDate,
                location = eventLocation,
                category = eventCategory
            )
        }
    }
}

@Composable
fun EventDetailScreen(
    title: String,
    description: String,
    date: String,
    location: String,
    category: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Détails de l'événement",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp,
            color = Color.Red
        )
        Spacer(modifier = Modifier.height(50.dp))

        Text("Titre : $title", style = MaterialTheme.typography.bodyLarge, fontSize = 20.sp)
        Text("Description : $description", style = MaterialTheme.typography.bodyMedium, fontSize = 20.sp, textAlign = TextAlign.Center)
        Text("Date : $date", style = MaterialTheme.typography.bodyMedium, fontSize = 20.sp)
        Text("Lieu : $location", style = MaterialTheme.typography.bodyMedium, fontSize = 20.sp)
        Text("Catégorie : $category", style = MaterialTheme.typography.bodyMedium, fontSize = 20.sp)

        Spacer(modifier = Modifier.height(180.dp))

        Row(modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center)
        {
            Image(
                painter = painterResource(id = R.drawable.bdeisen_logo),
                contentDescription = "BDE Image",
                modifier = Modifier
                    .width(125.dp)
                    .height(200.dp)
            )
            Spacer(modifier = Modifier.width(70.dp))
            Image(
                painter = painterResource(id = R.drawable.bdsisen_logo),
                contentDescription = "BDS Image",
                modifier = Modifier
                    .width(125.dp)
                    .height(200.dp)
            )
        }
        Spacer(modifier = Modifier.height(30.dp))

        Image(
            painter = painterResource(id = R.drawable.isen_logo),
            contentDescription = "Top Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        )

    }
}




