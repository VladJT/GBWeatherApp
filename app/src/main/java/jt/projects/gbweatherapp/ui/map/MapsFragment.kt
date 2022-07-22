package jt.projects.gbweatherapp.ui.map

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
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import jt.projects.gbweatherapp.BaseActivity
import jt.projects.gbweatherapp.R
import jt.projects.gbweatherapp.databinding.FragmentMapsBinding
import jt.projects.gbweatherapp.model.City
import jt.projects.gbweatherapp.model.Weather
import jt.projects.gbweatherapp.utils.showSnackBarShort
import jt.projects.gbweatherapp.utils.showSnackBarWithAction
import java.util.*

class MapsFragment : Fragment() {
    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    lateinit var map: GoogleMap

    companion object {
        fun newInstance() = MapsFragment()
    }

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        map = googleMap
        val city = LatLng(68.9792, 33.0925)
        googleMap.addMarker(MarkerOptions().position(city).title("Marker in Murmansk"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(city))
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

        binding.buttonSearchAddress.setOnClickListener() {
            binding.editTextAddress.text.toString().let {
                val geocoder = Geocoder(context, Locale("ru_RU"))
                val result = geocoder.getFromLocationName(it, 1)

                if (result != null) {
                    val location = LatLng(result.first().latitude, result.first().longitude)
                    setMarker(
                        location,
                        it,
                        R.drawable.ic_map_marker
                    )
                    map.moveCamera(CameraUpdateFactory.newLatLng(location))
                    view.showSnackBarWithAction("Показать погоду?", "Да") {
                        showWeather(
                            location,
                            result[0].locality
                        )
                    }
                } else {
                    view.showSnackBarShort("Адрес не найден")
                }
            }

        }
    }

    private fun showWeather(location: LatLng, name: String) {
        (activity as BaseActivity).showWeatherDetails(
            Weather(
                City(
                    name,
                    location.latitude,
                    location.longitude
                )
            )
        )
    }

    private fun setMarker(location: LatLng, searchText: String, resId: Int): Marker {
        return map.addMarker(
            MarkerOptions().position(location).title(searchText).icon(
                BitmapDescriptorFactory.fromResource(resId)
            )
        )!!
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}