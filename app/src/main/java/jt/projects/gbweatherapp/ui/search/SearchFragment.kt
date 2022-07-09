package jt.projects.gbweatherapp.ui.search

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import jt.projects.gbweatherapp.R
import jt.projects.gbweatherapp.databinding.FragmentSearchBinding
import jt.projects.gbweatherapp.memo.EX_SERVICE_STRING_EXTRA
import jt.projects.gbweatherapp.memo.ExIntentService
import java.util.*
import java.util.concurrent.TimeUnit

const val TEST_BROADCAST_INTENT_FILTER = "TEST BROADCAST INTENT FILTER"
const val BROADCAST_EXTRA = "THREADS_FRAGMENT_EXTRA"


class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SearchViewModel

    companion object {
        fun newInstance() = SearchFragment()
    }

    //Создаём свой BroadcastReceiver (получатель широковещательного сообщения)
    private val testReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            //Достаём данные из интента
            intent.getStringExtra(BROADCAST_EXTRA)?.let {
                binding.textViewThreads.text = "Получено: " + it
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //  context?.registerReceiver(testReceiver, IntentFilter(TEST_BROADCAST_INTENT_FILTER))
        context?.let {
            LocalBroadcastManager.getInstance(it)
                .registerReceiver(
                    testReceiver,
                    IntentFilter(TEST_BROADCAST_INTENT_FILTER)
                )
        }

    }

    override fun onDestroy() {
        //context?.unregisterReceiver(testReceiver)
        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(testReceiver)
        }
        _binding = null
        super.onDestroy()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        val observer = Observer<Int> { binding.textCounter.text = it.toString() }
        //    viewModel.counter.observe(viewLifecycleOwner, observer)


        // UI THREAD
        with(binding) {
            buttonThreadsUi.setOnClickListener {
                textViewThreads.text =
                    startCalculations(binding.editTextThreads.text.toString().toInt())
                mainContainerThreads.addView(AppCompatTextView(view.context).apply {
                    text = getString(R.string.in_main_thread)
                })
            }
        }

        // new THREAD
        var counterThread = 0
        with(binding) {
            buttonThreads.setOnClickListener {
                Thread {
                    counterThread++
                    val calcText =
                        startCalculations(binding.editTextThreads.text.toString().toInt())
                    activity?.runOnUiThread {
                        textViewThreads.text = calcText
                        mainContainerThreads.addView(AppCompatTextView(it.context).apply {
                            text = String.format(getString(R.string.from_thread), counterThread)
                        })
                    }
                }.start()
            }
        }

        // HandlerThread
        val handlerThread = HandlerThread("MyHandlerThread")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        with(binding) {
            buttonThreadHandler.setOnClickListener {
                mainContainerThreads.addView(AppCompatTextView(it.context).apply {
                    text = String.format(
                        getString(R.string.calculate_in_thread),
                        handlerThread.name
                    )
                })
                handler.post {
                    val calcText =
                        startCalculations(editTextThreads.text.toString().toInt())
                    mainContainerThreads.post {
                        textViewThreads.text = calcText
                        mainContainerThreads.addView(AppCompatTextView(it.context).apply {
                            text = String.format(
                                getString(R.string.calculate_in_thread),
                                Thread.currentThread().name
                            )
                        })
                    }
                }
            }//onclick
        }

        initServiceButton()
        initServiceWithBroadcastButton()
    }

    private fun initServiceWithBroadcastButton() {
        binding.serviceWithBroadcastButton.setOnClickListener {
            context?.let {
                it.startService(Intent(it, ExIntentService::class.java).apply {
                    putExtra(
                        EX_SERVICE_STRING_EXTRA,
                        binding.editTextThreads.text.toString()
                    )
                })
            }
        }

    }

    private fun initServiceButton() {
        binding.serviceButton.setOnClickListener {
            context?.let {
                it.startService(Intent(it, ExIntentService::class.java).apply {
                    putExtra(
                        EX_SERVICE_STRING_EXTRA,
                        getString(R.string.hello_from_thread_fragment)
                    )
                })
            }
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