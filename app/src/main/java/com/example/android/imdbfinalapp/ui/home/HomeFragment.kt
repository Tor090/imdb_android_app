package com.example.android.imdbfinalapp.ui.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.android.imdbfinalapp.adapters.MovieListAdapter
import com.example.android.imdbfinalapp.databinding.HomeFragmentBinding
import com.example.android.imdbfinalapp.viewmodels.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(){

    private lateinit var nowPlayingAdapter: MovieListAdapter
    private lateinit var binding: HomeFragmentBinding

    private  val viewModel by viewModel<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.homeViewModel = viewModel

        initRootView()
        setUpObservers()
        return binding.root
    }


    private fun initRootView() {
        nowPlayingAdapter = MovieListAdapter(requireContext(),
            MovieListAdapter.OnItemClickListener{ viewModel.displayMovieDetail(it)},
            MovieListAdapter.OnFavoriteClickListener{ viewModel.setMovieToFavorite(it)})
        binding.nowPlayingRecycler.adapter = nowPlayingAdapter
        binding.nowPlayingRecycler.isNestedScrollingEnabled = false
    }

    private fun setUpObservers() {
        viewModel.navigateToSelectedDetail.observe(viewLifecycleOwner) {
            if (it != null) {
                this.findNavController()
                    .navigate(HomeFragmentDirections.actionHomeFragmentToDetailFragment(it))
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

        viewModel.navigateToSearch.observe(viewLifecycleOwner) { shouldNavigate ->
            if (shouldNavigate == true) {
                this.findNavController()
                    .navigate(HomeFragmentDirections.actionHomeFragmentToSearchFragment())
                viewModel.onNavigatedToSearch()
            }
        }

        viewModel.navigateToFavorite.observe(viewLifecycleOwner) { shouldNavigate ->
            if (shouldNavigate == true) {
                this.findNavController()
                    .navigate(HomeFragmentDirections.actionHomeFragmentToFavoriteFragment())
                viewModel.onNavigatedToFavorite()
            }
        }
    }


//    override fun onItemClick(movie: Movie?) {
//        movie?.let {
//            Toast.makeText(requireActivity(), "Pressed ${it.id}", Toast.LENGTH_SHORT).show()
//            val action: NavDirections =
//                HomeFragmentDirections.actionHomeFragmentToDetailFragment(it)
//            Navigation.findNavController(binding.getRoot()).navigate(action)
//        }
//    }


}