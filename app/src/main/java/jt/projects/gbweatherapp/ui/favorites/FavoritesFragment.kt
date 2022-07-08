package jt.projects.gbweatherapp.ui.favorites

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import jt.projects.gbweatherapp.databinding.FragmentFavoritesBinding
import jt.projects.gbweatherapp.memo.ExService
import jt.projects.gbweatherapp.utils.getLines
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

const val BROADCAST_ACTION_CALCFINISHED = "BROADCAST_ACTION_CALCFINISHED"
class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!


    val isBound = false
    lateinit var serviceBinder : ExService

    private val listener = View.OnClickListener {
        val uri = URL(binding.url.text.toString())
        Thread {
            var urlConnection: HttpsURLConnection? = null
            try {
                urlConnection = (uri.openConnection() as HttpsURLConnection).apply {
                    requestMethod = "GET" //установка метода получения
                    readTimeout = 10000 //установка таймаута — 10 000
                }

                val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                val result = getLines(reader)

                activity?.runOnUiThread {
                    binding.webview.settings.javaScriptEnabled = true
                    binding.webview.loadDataWithBaseURL(
                        null,
                        result,
                        "text/html; charset=utf-8",
                        "utf-8",
                        null
                    )
                }
            } catch (e: Exception) {
                Log.e("@@@", "Fail connection", e)
                e.printStackTrace()
            } finally {
                urlConnection?.disconnect()
            }
        }.start()
    }


    companion object {
        fun newInstance() = FavoritesFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
   //           initView()
  //      initNotificationChannel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ok.setOnClickListener(listener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}