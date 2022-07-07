package jt.projects.gbweatherapp.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import jt.projects.gbweatherapp.R
import jt.projects.gbweatherapp.databinding.FragmentSearchBinding
import jt.projects.gbweatherapp.utils.hideKeyboard
import java.util.*
import java.util.concurrent.TimeUnit

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SearchViewModel
    private var counterThread = 0

    companion object {
        fun newInstance() = SearchFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

//        binding.textSearch.text = """Здесь будет осуществляться поиск погоды по вводимым параметрам
//            Найденный город можно будет добавлять в список Избранного"""

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        val observer = Observer<Int> { binding.textCounter.text = it.toString() }
        //    viewModel.counter.observe(viewLifecycleOwner, observer)


        binding.buttonThreadsUi.setOnClickListener {
            binding.textViewThreads.text =
                startCalculations(binding.editTextThreads.text.toString().toInt())
            binding.mainContainerThreads.addView(AppCompatTextView(view.context).apply {
                text = getString(R.string.in_main_thread)
            })
        }

        binding.buttonThreads.setOnClickListener {
            Thread {
                counterThread++
                val calcText =
                    startCalculations(binding.editTextThreads.text.toString().toInt())
                activity?.runOnUiThread {
                    binding.textViewThreads.text = calcText
                    binding.mainContainerThreads.addView(AppCompatTextView(it.context).apply {
                        text = String.format(
                            getString(R.string.from_thread),
                            counterThread
                        )
                    })
                }

            }.start()
        }
    }


    private fun startCalculations(seconds: Int): String {
        val date = Date()
        var diffInSec: Long
        do {
            val curDate = Date()
            val diffInMs: Long = curDate.time - date.time
            diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffInMs)

        } while (diffInSec < seconds)
        return diffInSec.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}