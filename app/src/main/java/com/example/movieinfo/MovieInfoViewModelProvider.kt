package com.example.movieinfo

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movieinfo.repository.MovieRepository

class MovieInfoViewModelProvider(val app:Application,val movieRepository: MovieRepository):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MovieInfoViewModel(app,movieRepository)as T
    }

}