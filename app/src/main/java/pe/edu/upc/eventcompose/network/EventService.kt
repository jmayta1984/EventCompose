package pe.edu.upc.eventcompose.network

import pe.edu.upc.eventcompose.model.Event
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EventService {

    @GET("events")
    suspend fun fetchEvents(): Response<List<Event>>

    @GET("events")
    suspend fun fetchEventsByName(@Query("q") query: String): Response<List<Event>>

    @GET("events/{day}")
    suspend fun fetchEventByDay(@Path("day") day: String): Response<Event>
}