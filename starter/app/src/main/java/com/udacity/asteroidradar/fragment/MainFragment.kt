package com.udacity.asteroidradar.fragment

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
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

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_overflow_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.view_weekly_asteroids -> {
                        viewModel.setAsteroidFilterToWeekly()
                        true
                    }
                    R.id.view_today_asteroids -> {
                        viewModel.setAsteroidFilterToToday()
                        true
                    }
                    R.id.view_saved_asteroids -> {
                        viewModel.setAsteroidFilterToSaved()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        return binding.root
    }
}
