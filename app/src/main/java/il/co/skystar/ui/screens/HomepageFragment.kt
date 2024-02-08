package il.co.skystar.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import il.co.skystar.R
import il.co.skystar.databinding.HomepageLayoutBinding
import il.co.skystar.utils.autoCleared

@AndroidEntryPoint
class HomepageFragment : Fragment(R.layout.homepage_layout) {

    private var binding: HomepageLayoutBinding by autoCleared()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomepageLayoutBinding.inflate(layoutInflater)

        binding.apply {
            skyStarIcon.setOnClickListener(){
                skyStarIcon.animate().apply {
                    duration = 800
                    rotationXBy(360f)
                }.start()
            }

            btnDeparture.setOnClickListener{
                findNavController().navigate(R.id.action_homepageFragment_to_departuresFlightsFragment)
            }

            btnArrivals.setOnClickListener{
                findNavController().navigate(R.id.action_homepageFragment_to_arrivalsFlightsFragment)
            }

            btnSearch.setOnClickListener{
                findNavController().navigate(R.id.action_homepageFragment_to_searchFragment)
            }

            btnFavorites.setOnClickListener{
                findNavController().navigate(R.id.action_homepageFragment_to_favoriteFragment)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}