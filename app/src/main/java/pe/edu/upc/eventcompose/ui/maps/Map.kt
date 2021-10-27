package pe.edu.upc.eventcompose.ui.maps

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import pe.edu.upc.eventcompose.model.Event
import java.lang.IllegalStateException


@Composable
fun MapView(viewModel: MapsViewModel) {
    val context = LocalContext.current
    val mapView = remember {
        Log.d("Map", "Load info")
        MapView(context)
    }
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    lifecycle.addObserver(rememberMapLifecycleObserver(mapView))

    AndroidView(factory = {
        mapView.apply {
            mapView.getMapAsync { googleMap ->
                viewModel.updateMap(googleMap)
                Log.d("Map", "factory")
            }

        }
    })
}

@Composable
fun rememberMapLifecycleObserver(map: MapView): LifecycleEventObserver {
    return remember {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> map.onCreate(Bundle())
                Lifecycle.Event.ON_START -> map.onStart()
                Lifecycle.Event.ON_RESUME -> map.onResume()
                Lifecycle.Event.ON_PAUSE -> map.onPause()
                Lifecycle.Event.ON_STOP -> map.onStop()
                Lifecycle.Event.ON_DESTROY -> map.onDestroy()
                Lifecycle.Event.ON_ANY -> throw IllegalStateException()
            }
        }
    }
}

@Composable
fun Map(
    viewModel: MapsViewModel = hiltViewModel(),
) {

    val events by viewModel.events.observeAsState(listOf())
    val googleMap by viewModel.map.observeAsState()

    Box(modifier = Modifier.fillMaxSize()) {

        MapView(viewModel)
        EventSearch(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
        )
        googleMap?.let { EventLocations(events = events, map = it) }
        EventList(events, modifier = Modifier.align(Alignment.BottomCenter))
    }

}

@Composable
fun EventSearch(modifier: Modifier) {

    Button(
        onClick = {
        },
        shape = CircleShape,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.onPrimary,
            //contentColor = MaterialTheme.colors.onPrimary
        )
    ) {

        Text(
            text = "Search this area",
            color = MaterialTheme.colors.onBackground,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.caption,
            modifier = Modifier.padding(4.dp)
        )

    }
}


@Composable
fun EventLocations(events: List<Event>, map: GoogleMap) {
    for (event in events) {
        val destination = LatLng(event.latitude, event.longitude)
        val markerOptions = MarkerOptions().title(event.name).position(destination)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        map.addMarker(markerOptions)
    }
}

@Composable
fun EventList(events: List<Event>, modifier: Modifier) {

    LazyRow(modifier = modifier) {
        items(events) { event ->
            EventCard(event)
        }
    }

}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun EventCard(event: Event) {

    Box {
        Card(modifier = Modifier.padding(8.dp)) {
            Image(
                painter = rememberImagePainter(event.poster),
                contentDescription = null,
                modifier = Modifier
                    .size(192.dp),
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
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(4.dp)
                )
            }

        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun EventBox(event: Event) {
    Box {

        Card(modifier = Modifier.padding(8.dp)) {
            Image(
                painter = rememberImagePainter(event.poster),
                contentDescription = null,
                modifier = Modifier
                    .height(192.dp)
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


@OptIn(ExperimentalCoilApi::class)
@Composable
fun EventItem(event: Event, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {

        Card(
            modifier = modifier
                .padding(8.dp),
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
            modifier = Modifier
                .padding(16.dp)
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