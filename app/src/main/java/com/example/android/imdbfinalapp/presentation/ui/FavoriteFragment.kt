package com.example.android.imdbfinalapp.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.android.imdbfinalapp.presentation.adapters.MovieListAdapter
import com.example.android.imdbfinalapp.databinding.FavoriteFragmentBinding
import com.example.android.imdbfinalapp.presentation.viewmodels.FavoriteViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment() {


    private lateinit var binding: FavoriteFragmentBinding
    private lateinit var favoriteListAdapter: MovieListAdapter

    private  val viewModel by viewModel<FavoriteViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FavoriteFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.favoriteViewModel = viewModel

        favoriteListAdapter = MovieListAdapter(requireContext(),
            MovieListAdapter.OnItemClickListener{ viewModel.displayMovieDetail(it)},
            MovieListAdapter.OnFavoriteClickListener{ viewModel.setMovieToFavorite(it)}
        )
        binding.favoriteRecyclerview.adapter = favoriteListAdapter

        viewModel.message.observe(viewLifecycleOwner) {
            if (it != null) {
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.navigateToSelectedDetail.observe(viewLifecycleOwner) {
            if (it != null) {
                this.findNavController()
                    .navigate(
                        FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(
                            it
                        )
                    )
                viewModel.displayMovieDetailComplete()
            }
        }

        binding.topLayout.setListener {
            viewModel.onNavigateBackClicked()
        }

        viewModel.navigateBack.observe(viewLifecycleOwner) { shouldNavigate ->
            if (shouldNavigate == true) {
                this.findNavController().popBackStack()
                viewModel.onNavigatedBack()
            }
        }

        return binding.root
    }


}