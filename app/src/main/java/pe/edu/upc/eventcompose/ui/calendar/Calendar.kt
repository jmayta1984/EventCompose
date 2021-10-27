package pe.edu.upc.eventcompose.ui.calendar

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.selection.DynamicSelectionState
import io.github.boguszpawlowski.composecalendar.selection.SelectionMode
import pe.edu.upc.eventcompose.model.Event
import pe.edu.upc.eventcompose.ui.maps.EventList

@Composable
fun Calendar(viewModel: CalendarViewModel) {
    val calendarState = rememberSelectableCalendarState()

    Column(
        Modifier.verticalScroll(rememberScrollState())
    ) {
        SelectableCalendar(calendarState = calendarState)
        Spacer(modifier = Modifier.height(16.dp))
        SelectionControls(viewModel, selectionState = calendarState.selectionState)
    }
}

@Composable
private fun SelectionControls(
    viewModel: CalendarViewModel,
    selectionState: DynamicSelectionState,
) {

    val event by viewModel.event.observeAsState(Event())

    if (selectionState.selection.isNotEmpty()) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val day = selectionState.selection[0].dayOfMonth.toString()
            viewModel.fetchEventByDay(day)

            event?.let { EventItem(event) }
        }
    }


}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun EventItem(event: Event, modifier: Modifier = Modifier) {
    Box {

        Card(
            modifier = modifier.padding(8.dp),
            backgroundColor = MaterialTheme.colors.onBackground
        ) {
            Column {
                EventImage(event)
                Spacer(modifier = Modifier.height(8.dp))
                EventBody(event)
                Spacer(modifier = Modifier.height(8.dp))
            }
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

@Composable
fun EventImage(event: Event) {
    Image(
        painter = rememberImagePainter(event.poster),
        contentDescription = null,
        modifier = Modifier
            .height(128.dp)
            .fillMaxWidth(),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun EventBody(event: Event) {
    Text(
        "${event.month} ${event.day}",
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 12.dp),
        color = MaterialTheme.colors.onPrimary
    )
    Text(
        "Address: ${event.address}", modifier = Modifier.padding(start = 12.dp),
        color = MaterialTheme.colors.onPrimary
    )
    Text(
        "District: ${event.district}", modifier = Modifier.padding(start = 12.dp),
        color = MaterialTheme.colors.onPrimary
    )
    Text(
        "Rating: ${event.rating}", modifier = Modifier.padding(start = 12.dp),
        color = MaterialTheme.colors.onPrimary
    )
}
