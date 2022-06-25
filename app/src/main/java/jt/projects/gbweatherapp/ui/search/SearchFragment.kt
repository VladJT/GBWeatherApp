package jt.projects.gbweatherapp.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import jt.projects.gbweatherapp.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}