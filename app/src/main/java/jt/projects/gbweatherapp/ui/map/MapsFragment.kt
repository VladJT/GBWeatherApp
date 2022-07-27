package jt.projects.gbweatherapp.ui.map

import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import jt.projects.gbweatherapp.BaseActivity
import jt.projects.gbweatherapp.R
import jt.projects.gbweatherapp.databinding.FragmentMapsBinding
import jt.projects.gbweatherapp.memo.Notifications
import jt.projects.gbweatherapp.utils.showSnackBarShort
import jt.projects.gbweatherapp.utils.showSnackBarWithAction
import jt.projects.gbweatherapp.utils.showWeatherDetailsFragment
import java.util.*

class MapsFragment : Fragment() {
    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    lateinit var map: GoogleMap
    private val markers = mutableListOf<Marker>()

    companion object {
        fun newInstance() = MapsFragment()
    }

    private val callback = OnMapReadyCallback { googleMap ->
        val location = LatLng(68.9792, 33.0925)
        googleMap.apply {
            uiSettings.isZoomControlsEnabled = true // добавить кнопки zoom[+][-]
            isMyLocationEnabled = true
            uiSettings.isMyLocationButtonEnabled = true
            addMarker(MarkerOptions().position(location).title("Marker in Murmansk"))
            moveCamera(CameraUpdateFactory.newLatLng(location))

            setOnMapLongClickListener {
                addMarkerToArray(it)
                drawLine()
            }

            setOnMarkerClickListener {
                val location = it.position
                requireActivity().showWeatherDetailsFragment(location)
                true
            }
        }

        map = googleMap

        view?.showSnackBarWithAction("Показать подсказку?", "Да") {
            (requireActivity() as BaseActivity).showMsgDialog(
                "Подсказка",
                """Длинное нажатие - установить 🚩,
                    |нажать 🚩 - погода в данной локации.
                """.trimMargin()
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        binding.buttonSearchAddress.setOnClickListener {
            binding.editTextAddress.text.toString().let {
                val geocoder = Geocoder(context, Locale("ru_RU"))
                try {
                    val result = geocoder.getFromLocationName(it, 1)

                    if (result != null && result.size > 0) {
                        val location = LatLng(result.first().latitude, result.first().longitude)
                        setMarker(location, it, R.drawable.ic_map_marker)
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 6f))
                       Notifications.pushNotificationLocationFound(result[0].locality, location)
                    } else {
                        view.showSnackBarShort("Адрес не найден")
                    }
                } catch (e: Exception) {
                    binding.root.showSnackBarShort("Ошибка: ".plus(e.message))
                }
            }
        }
    }


    private fun setMarker(location: LatLng, searchText: String, resId: Int): Marker {
        return map.addMarker(
            MarkerOptions().position(location).title(searchText).icon(
                BitmapDescriptorFactory.fromResource(resId)
            )
        )!!
    }

    private fun addMarkerToArray(location: LatLng) {
        val marker = setMarker(location, markers.size.toString(), R.drawable.ic_map_pin)
        markers.add(marker)
    }

    private fun drawLine() {
        val last: Int = markers.size - 1
        if (last >= 1) {
            val previous: LatLng = markers[last - 1].position
            val current: LatLng = markers[last].position
            map.addPolyline(
                PolylineOptions()
                    .add(previous, current)
                    .color(Color.RED)
                    .width(15f)
            )
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}