package com.example.movieinfo.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieinfo.MainActivity
import com.example.movieinfo.MovieInfoViewModel
import com.example.movieinfo.R
import com.example.movieinfo.adapters.MovieAdapter
import com.example.movieinfo.databinding.FragmentListBinding
import com.example.movieinfo.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class ListFragment : Fragment() {
    lateinit var viewModel: MovieInfoViewModel
    lateinit var adapter: MovieAdapter
    private var _binding: FragmentListBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding= FragmentListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=(activity as MainActivity).viewModel
        setupRecyclerView()
        binding.icon.setOnClickListener {
            search()
        }
        findNavController().navigate(
            R.id.action_listFragment_to_searchFragment
        )
        var job: Job?=null
        binding.search.addTextChangedListener {it->
            job?.cancel()
            job= MainScope().launch {
                delay(500L)
                it?.let {
                    if (it.toString().isNotEmpty()){
                        showShimmerAndHideRecycler()
                        viewModel.searchMovie(it.toString())
                    }
                }
            }
        }
        viewModel.searchMovie.observe(viewLifecycleOwner,{response->
            when(response){
                is Resource.Success->{
                    hideShimmerAndShowRecycler()
                    response.data?.let {
                        searchResponse -> adapter.differ.submitList(searchResponse.Search)
                    }
                }
                is Resource.Error->{
                    hideShimmerAndShowRecycler()
                    response.message?.let {
                        Toast.makeText(activity,"An error occurred: $it", Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading->{
                    showShimmerAndHideRecycler()
                }
            }
        })
    }
    private fun hideShimmerAndShowRecycler(){
        binding.shimmerLayout.visibility=View.GONE
        binding.recycleview.visibility=View.VISIBLE
    }
    private fun showShimmerAndHideRecycler(){
        binding.recycleview.visibility=View.GONE
        binding.shimmerLayout.visibility=View.VISIBLE
        binding.shimmerLayout.startShimmer()
    }
    private fun setupRecyclerView(){
        adapter= MovieAdapter()
        binding.recycleview.apply {
            adapter=adapter
            layoutManager=LinearLayoutManager(activity)
        }
    }
    private fun search(){
        binding.icon.visibility=View.GONE
        binding.search.visibility=View.VISIBLE

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }


}