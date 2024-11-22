package fr.isen.baudillon.isensmartcompanion

import retrofit2.Call
import retrofit2.http.GET

interface EventApiService {
    @GET("events.json")
    fun getEvents(): Call<List<Event>>
}
