package com.example.liveattendanceapp.views.attendance

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.liveattendanceapp.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class AttendanceFragment : Fragment(), OnMapReadyCallback {

    private var mapAttendance: SupportMapFragment? = null
    private var map: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_attendance, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMaps()
    }

    private fun setupMaps() {
        mapAttendance = childFragmentManager.findFragmentById(R.id.map_attendance) as SupportMapFragment
        mapAttendance?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap
        val sydney = LatLng(-33.852, 151.211)
        map?.addMarker(
            MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney")
        )
        map?.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        map?.animateCamera(CameraUpdateFactory.zoomTo(20f))
    }

}