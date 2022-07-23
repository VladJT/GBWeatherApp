package jt.projects.gbweatherapp.ui.favorites

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import jt.projects.gbweatherapp.BaseActivity
import jt.projects.gbweatherapp.R
import jt.projects.gbweatherapp.databinding.FragmentFavoritesBinding
import jt.projects.gbweatherapp.model.Weather
import jt.projects.gbweatherapp.model.repository.OperationType
import jt.projects.gbweatherapp.ui.OnItemViewClickListener
import jt.projects.gbweatherapp.utils.*
import jt.projects.gbweatherapp.viewmodel.AppState

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FavoritesViewModel

    companion object {
        fun newInstance() = FavoritesFragment()
    }

    private val adapter = FavFragmentAdapter(object : OnItemViewClickListener {
        override fun onItemViewClick(weather: Weather) {
            requireActivity().showWeatherDetailsFragment(weather)
        }

        override fun onButtonFavoritesClick(
            weather: Weather,
            operation: OperationType
        ) {
            when (operation) {
                OperationType.ADD -> {
                    viewModel.addToFavorites(weather)
                    binding.root.showSnackBarShort(weather.city.name.plus(" добавлен в Избранное"))
                }
                OperationType.DELETE -> {
                    viewModel.deleteFromFavorites(weather)
                    binding.root.showSnackBarShort(weather.city.name.plus(" удален из Избранного"))
                }
            }
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true) // эта строчка говорит о том, что у фрагмента должен быть доступ к меню Активити

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
        //    menu.findItem(R.id.action_clean_room).isVisible = true
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