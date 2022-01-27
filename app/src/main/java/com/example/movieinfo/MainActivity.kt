package com.example.movieinfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.movieinfo.databinding.ActivityMainBinding
import com.example.movieinfo.repository.MovieRepository

class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController
    lateinit var viewModel: MovieInfoViewModel
    private var _binding:ActivityMainBinding?=null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment=supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController=navHostFragment.navController
        val repository=MovieRepository()
        val viewModelProviderFactory=MovieInfoViewModelProvider(application,repository)
        viewModel=ViewModelProvider(this,viewModelProviderFactory).get(MovieInfoViewModel::class.java)

    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() ||super.onSupportNavigateUp()
    }
}