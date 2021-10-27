package pe.edu.upc.eventcompose.ui.events

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import pe.edu.upc.eventcompose.model.Event

@Composable
fun Events(viewModel: EventsViewModel) {

    Scaffold {
        Column {
            EventSearch(viewModel)
            EventList(viewModel)
        }
    }

}

@Composable
fun EventSearch(viewModel: EventsViewModel) {
    val name: String by viewModel.query.observeAsState("")
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = name,
        onValueChange = {
            viewModel.updateQuery(it)
        },
        leadingIcon = {
            Icon(Icons.Filled.Search, null)
        },
        modifier = Modifier.fillMaxWidth(),
        label = {
            Text("Search")
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search,
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                viewModel.fetchEventsByName(name)
                focusManager.clearFocus()
            }
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colors.onBackground
        )
    )
}

@Composable
fun EventList(viewModel: EventsViewModel) {
    val events: List<Event> by viewModel.events.observeAsState(listOf())
    LazyColumn(modifier = Modifier.padding(start = 8.dp, end = 8.dp)) {
        items(events) { event ->
            Row {
                EventDate(event, modifier = Modifier.height(144.dp))
                EventBox(event, modifier = Modifier)
            }

        }
    }
}

@Composable
fun EventDate(event: Event, modifier: Modifier) {
    Column(
        modifier = modifier
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(event.month, fontWeight = FontWeight.Bold)
        Text(event.day)
    }
}


@OptIn(ExperimentalCoilApi::class)
@Composable
fun EventBox(event: Event, modifier: Modifier = Modifier) {
    Box {

        Card(modifier = modifier.padding(8.dp)) {
            Image(
                painter = rememberImagePainter(event.poster),
                contentDescription = null,
                modifier = Modifier
                    .height(128.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colors.onPrimary,
                        shape = RoundedCornerShape(4.dp)
                    )
            ) {
                Text(
                    text = event.name,
                    color = MaterialTheme.colors.onBackground,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(4.dp)
                )
            }

        }

    }

}