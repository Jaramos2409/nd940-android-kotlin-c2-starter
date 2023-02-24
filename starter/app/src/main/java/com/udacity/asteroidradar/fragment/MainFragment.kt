package com.udacity.asteroidradar.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.adapter.MainAsteroidListAdapter
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.viewmodels.MainFragmentViewModel

class MainFragment : Fragment() {

    private val viewModel: MainFragmentViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        ViewModelProvider(
            this,
            MainFragmentViewModel.Factory(activity.application)
        )[MainFragmentViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.asteroidRecycler.adapter =
            MainAsteroidListAdapter(MainAsteroidListAdapter.OnClickListener {
                viewModel.navigateToAsteroidDetailsScreen(it)
            })

        viewModel.navigateToAsteroidDetailsScreen.observe(viewLifecycleOwner) {
            it?.let {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.navigationToAsteroidDetailsScreenComplete()
            }
        }

        viewModel.areThereInternetIssues.observe(viewLifecycleOwner) {
            it.let {
                if (it) {
                    Toast.makeText(
                        context,
                        "Application is having issues with accessing the internet.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        return binding.root
    }
}
