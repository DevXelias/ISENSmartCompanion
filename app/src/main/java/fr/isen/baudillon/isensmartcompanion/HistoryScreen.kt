package fr.isen.baudillon.isensmartcompanion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun HistoryScreen() {
    val context = LocalContext.current
    val dao = DatabaseModule.getDao(context) // Obtenez le DAO à partir du contexte
    val coroutineScope = rememberCoroutineScope()


    // Récupération des échanges en utilisant DAO
    val exchanges = dao.getAll().collectAsState(initial = emptyList()).value
    Column (verticalArrangement = Arrangement.Top){

        Text(text = "Historique",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            textAlign = TextAlign.Center,
            color = Color.Red)

        LazyColumn(modifier = Modifier.padding(16.dp)) {

            items(exchanges) { exchange ->
                val date = DateConverter().fromTimestamp(exchange.timestamp)
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(text = "Date: $date", style = MaterialTheme.typography.bodySmall)
                        Text(text = "Q: ${exchange.question}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "R: ${exchange.response}", style = MaterialTheme.typography.bodyMedium)

                    }
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                dao.delete(exchange) // Supprimer un élément
                            }
                        },
                        modifier = Modifier.padding(start = 8.dp)
                            .align(Alignment.CenterVertically),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),

                    ) {
                        Text("X")
                    }



                }
            }



        }
    }


    Column (
        modifier = Modifier.fillMaxHeight()
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally){
        Button(
            onClick = {
                coroutineScope.launch {
                    dao.deleteAll()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)

        ) { Text("Supprimer Tout")}
    }
}
