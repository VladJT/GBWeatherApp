package jt.projects.gbweatherapp.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import jt.projects.gbweatherapp.databinding.FragmentSearchBinding
import jt.projects.gbweatherapp.ui.favorites.FavoritesFragment
import jt.projects.gbweatherapp.ui.home.HomeViewModel
import jt.projects.gbweatherapp.viewmodel.AppState

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SearchViewModel

    companion object{
        fun newInstance() = SearchFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        binding.textSearch.text= """Здесь будет осуществляться поиск погоды по вводимым параметрам
            Найденный город можно будет добавлять в список Избранного или устанавливать как стартовый город
            Сведения о стартовом городе и городах в Избранном будут хранится в shared_pref (или DataStore)
            
            Функционал: 
            1. ввод параметров + поиск
            2. добавление в Избранное
            3. задать найденный город как стартовый"""

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        val observer = Observer<Int> { binding.textCounter.text = it.toString() }
        viewModel.counter.observe(viewLifecycleOwner, observer)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}