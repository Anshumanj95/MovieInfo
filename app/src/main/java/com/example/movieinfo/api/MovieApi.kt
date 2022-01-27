package com.example.movieinfo.api

import com.example.movieinfo.model.MovieRespone
import com.example.movieinfo.model.SearchResponse
import com.example.movieinfo.util.constant.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {
    @GET("/")
    suspend fun getMovieById(
        @Query("i")
        id:String,
        @Query("apikey")
        apikey:String=API_KEY
    ): Response<MovieRespone>

    @GET("/")
    suspend fun getMovieBySearch(
        @Query("s")
        search:String,
        @Query("apikey")
        apikey:String=API_KEY
    ): Response<SearchResponse>
}