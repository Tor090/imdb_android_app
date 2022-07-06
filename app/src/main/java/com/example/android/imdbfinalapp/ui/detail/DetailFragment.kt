package com.example.android.imdbfinalapp.ui.detail

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.android.imdbfinalapp.databinding.DetailFragmentBinding
import com.example.android.imdbfinalapp.models.Movie
import com.example.android.imdbfinalapp.util.Util
import com.example.android.imdbfinalapp.viewmodels.DetailViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailFragment : Fragment() {

    private lateinit var binding : DetailFragmentBinding
    private lateinit var movie : Movie
    private var genres: ArrayList<String>? = null

    private  val viewModel by viewModel<DetailViewModel>{ parametersOf(id)}


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DetailFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        movie = DetailFragmentArgs.fromBundle(requireArguments()).movie

        binding.detailViewModel = viewModel

        if(!movie.favorite){
            binding.shareButton.visibility = View.GONE
        }

        binding.shareButton.setOnClickListener {
           val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, movie.title)
            val chooser = Intent.createChooser(intent,"Share using")
            if(chooser.resolveActivity(requireActivity().packageManager) != null){
                startActivity(chooser)
            }
        }

        movie.backdrop_path?.let {  Util.loadImage(requireContext(), it, binding.headImage) }
        movie.poster_path.let { Util.loadImage(requireContext(), it!!, binding.posterImage) }

        binding.title.text = "${movie.title} ${movie.release_date!!.substringBefore("-","")}"

        genres = movie.genre_ids?.let { Util.getGenresFromIds(it) }

        binding.genre.text = genres.toString().substring(1,genres.toString().lastIndex)
        binding.rating.text = movie.vote_average.toString()
        binding.overview.text = movie.overview

        viewModel.navigateBack.observe(viewLifecycleOwner) { shouldNavigate ->
            if (shouldNavigate == true) {
                Navigation.findNavController(binding.root).popBackStack()
                viewModel.onNavigatedBack()
            }
        }

        viewModel.navigateTrailer.observe(viewLifecycleOwner){ shouldNavigate ->
            if(shouldNavigate == true){
                val video = Intent(Intent.ACTION_VIEW, viewModel.trailerPath.value)

                if (video.resolveActivity(requireActivity().packageManager) != null) {
                    startActivity(video)
                }else{
                    Toast.makeText(requireContext(),"Can`t open video",Toast.LENGTH_LONG).show()
                }
                viewModel.onPlayed()
            }
        }
        return binding.root
    }



}