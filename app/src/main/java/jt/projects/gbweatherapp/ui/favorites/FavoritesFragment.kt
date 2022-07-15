package jt.projects.gbweatherapp.ui.favorites

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import jt.projects.gbweatherapp.R
import jt.projects.gbweatherapp.databinding.FragmentFavoritesBinding
import jt.projects.gbweatherapp.memo.ExService
import jt.projects.gbweatherapp.model.Weather
import jt.projects.gbweatherapp.ui.home.HomeViewModel
import jt.projects.gbweatherapp.ui.weatherdetails.BUNDLE_EXTRA
import jt.projects.gbweatherapp.ui.weatherdetails.WeatherDetailsFragment
import jt.projects.gbweatherapp.utils.DURATION_ITEM_ANIMATOR
import jt.projects.gbweatherapp.utils.getLines
import jt.projects.gbweatherapp.utils.showSnackBarWithAction
import jt.projects.gbweatherapp.viewmodel.AppState
import jt.projects.gbweatherapp.viewmodel.SharedPref
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FavoritesViewModel

    companion object {
        fun newInstance() = FavoritesFragment()
    }

    private val adapter = FavFragmentAdapter(object : FavFragmentAdapter.OnItemViewClickListener {
        override fun onItemViewClick(weather: Weather) {
            activity?.supportFragmentManager?.also { manager ->
                val bundle = Bundle()
                bundle.putParcelable(BUNDLE_EXTRA, weather)
                manager.beginTransaction()
                    .add(R.id.fragment_container, WeatherDetailsFragment.newInstance(bundle))
                    .addToBackStack("").commit()
            }
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as? AppCompatActivity)?.let {
            it.supportActionBar?.subtitle = "Избранное (история)"
        }
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        val observer =
            Observer<AppState<List<Weather>>> { renderData(it) }// it = конкрeтный экзмепляр AppState

        viewModel = ViewModelProvider(this)[FavoritesViewModel::class.java].also {
            it.getLiveData().observe(viewLifecycleOwner, observer)
            it.getWeather()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.action_clean_room).isVisible = true
    }

    private fun initRecyclerView() {
        binding.favoritesFragmentRecyclerView.adapter = adapter
        //  разделитель карточек
        DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL).also {
            it.setDrawable(resources.getDrawable(R.drawable.separator, null))
            binding.favoritesFragmentRecyclerView.addItemDecoration(it)
        }

        // Установим анимацию. А чтобы было хорошо заметно, сделаем анимацию долгой
        val animator = DefaultItemAnimator().apply {
            addDuration = DURATION_ITEM_ANIMATOR
            changeDuration = DURATION_ITEM_ANIMATOR
            removeDuration = DURATION_ITEM_ANIMATOR
            moveDuration = DURATION_ITEM_ANIMATOR
        }
        binding.favoritesFragmentRecyclerView.itemAnimator = animator
    }

    private fun renderData(appState: AppState<List<Weather>>) {
        when (appState) {
            is AppState.Success<*> -> {
                binding.loadingLayout.visibility = View.GONE
                adapter.setWeather(appState.data as List<Weather>)
            }
            is AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                adapter.setWeather(listOf())
                val action =
                    fun() { viewModel.getWeather() }
                binding.root.showSnackBarWithAction(
                    appState.error.message ?: "",
                    R.string.reload,
                    action
                )
            }
        }
    }

    override fun onDestroy() {
        adapter.removeListener()//чтобы не возникало утечек памяти
        _binding = null
        super.onDestroy()
    }
}