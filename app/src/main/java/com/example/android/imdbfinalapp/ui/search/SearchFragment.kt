package com.example.android.imdbfinalapp.ui.search

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.android.imdbfinalapp.adapters.MovieListAdapter
import com.example.android.imdbfinalapp.databinding.SearchFragmentBinding
import com.example.android.imdbfinalapp.viewmodels.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : Fragment(){

    private lateinit var searchListAdapter: MovieListAdapter
    private lateinit var binding: SearchFragmentBinding

    private  val viewModel by viewModel<SearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SearchFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.searchViewModel = viewModel

        searchListAdapter = MovieListAdapter(requireContext(),
            MovieListAdapter.OnItemClickListener{ viewModel.displayMovieDetail(it)},
            MovieListAdapter.OnFavoriteClickListener{ viewModel.setMovieToFavorite(it)})
        binding.searchResults.adapter = searchListAdapter
        //binding.searchResults.isNestedScrollingEnabled = false

        viewModel.navigateToSelectedDetail.observe(viewLifecycleOwner) {
            if (it != null) {
                this.findNavController()
                    .navigate(SearchFragmentDirections.actionSearchFragmentToDetailFragment(it))
                viewModel.displayMovieDetailComplete()
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) {
            binding.progressbar.isVisible = it
        }

        viewModel.message.observe(viewLifecycleOwner) {
            if (it != null) {
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
            }
        }

        binding.searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchView.clearFocus()
                if(query!=null && !TextUtils.isEmpty(query)) {
                    viewModel.getSearchResults(query)
                }
                else{
                    Toast.makeText(requireContext(),"Query Cannot be empty",Toast.LENGTH_SHORT).show()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        viewModel.navigateBack.observe(viewLifecycleOwner) { shouldNavigate ->
            if (shouldNavigate == true) {
                Navigation.findNavController(binding.root).popBackStack()
                viewModel.onNavigatedBack()
            }
        }

        return binding.root
    }




}