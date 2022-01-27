package com.example.movieinfo

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.*
import com.example.movieinfo.model.SearchResponse
import com.example.movieinfo.repository.MovieRepository
import com.example.movieinfo.util.Resource
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response

class MovieInfoViewModel(app:Application,val movieRepository: MovieRepository):AndroidViewModel(app) {
    val searchMovie:MutableLiveData<Resource<SearchResponse>> = MutableLiveData()

    fun searchMovie(searchQuery: String)=viewModelScope.launch {
        safeSearchMovieCall(searchQuery)
    }
    private fun handleSearchMovieResponse(response: Response<SearchResponse>): Resource<SearchResponse> {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }
    private suspend fun safeSearchMovieCall(searchQuery: String){
        searchMovie.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()){
                val response=movieRepository.searchMovie(searchQuery)
                searchMovie.postValue(handleSearchMovieResponse(response))
            }
            else{
                searchMovie.postValue(Resource.Error("No Internet Connection"))
            }
        }catch (t:Throwable){
            when(t){
                is IOException->searchMovie.postValue(Resource.Error("Network Failure"))
                else ->searchMovie.postValue(Resource.Error("Conversion Error"))
            }
        }
    }
    private fun hasInternetConnection():Boolean{
        val connectivityManager=getApplication<MovieApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        )as ConnectivityManager
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            val activeNetwork =connectivityManager.activeNetwork?:return false
            val capabilities= connectivityManager.getNetworkCapabilities(activeNetwork)?:return false
            return when{
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)->true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)->true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)->true
                else->false
            }
        }
        return false
    }
}
