package pe.edu.upc.eventcompose.repository

import android.util.Log
import pe.edu.upc.eventcompose.model.Event
import pe.edu.upc.eventcompose.network.EventService
import javax.inject.Inject

class EventRepository @Inject constructor(private val eventService: EventService) {

    suspend fun fetchEvents(): List<Event> {
        val response = eventService.fetchEvents()

        if (response.isSuccessful && response.body() != null) {
            Log.d("EventRepository", "response: Successful")
            Log.d("EventRepository", "Size: ${response.body()!!.size}")
            return response.body() as List<Event>

        }
        Log.d("EventRepository", "response: Failure")
        return listOf()
    }

    suspend fun fetchEventsByName(query: String): List<Event> {
        val response = eventService.fetchEventsByName(query)

        if (response.isSuccessful && response.body() != null) {
            return response.body() as List<Event>
        }
        return listOf()
    }

    suspend fun fetchEventByDay(day: String): Event? {
        val response = eventService.fetchEventByDay(day)
        if (response.isSuccessful && response.body() != null) {
            return response.body() as Event
        }
        return null
    }
}