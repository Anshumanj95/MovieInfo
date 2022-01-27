package com.example.movieinfo.repository

import com.example.movieinfo.api.RetrofitInstance

class MovieRepository {
    suspend fun searchMovie(search:String)=
        RetrofitInstance.api.getMovieBySearch(search)

    suspend fun getMovie(id:String)=
        RetrofitInstance.api.getMovieById(id)
}