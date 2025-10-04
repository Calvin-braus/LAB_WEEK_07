package com.example.lab_week_07

// 🔹 Import library yang dibutuhkan
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.lab_week_07.databinding.ActivityMapsBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

// 🔹 Konstanta izin lokasi
private const val ACCESS_FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    // 🔹 13. ActivityResultLauncher untuk mengatur request permission
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    // 🔹 Variabel untuk Map & View Binding
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 🔹 7–8. Mengambil fragment peta dari layout
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // 🔹 14. Registrasi ActivityResult untuk permission request
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    // Jika diizinkan → ambil lokasi
                    getLastLocation()
                } else {
                    // Jika ditolak → tampilkan rationale dialog
                    showPermissionRationale {
                        requestPermissionLauncher.launch(ACCESS_FINE_LOCATION)
                    }
                }
            }
    }

    // 🔹 17. Dijalankan ketika peta sudah siap
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        when {
            // Jika sudah punya izin lokasi
            hasLocationPermission() -> getLastLocation()

            // Jika sebelumnya user menolak izin
            shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION) -> {
                showPermissionRationale {
                    requestPermissionLauncher.launch(ACCESS_FINE_LOCATION)
                }
            }

            // Kalau belum pernah diminta sama sekali
            else -> requestPermissionLauncher.launch(ACCESS_FINE_LOCATION)
        }
    }

    // 🔹 16. Cek apakah izin lokasi sudah diberikan
    private fun hasLocationPermission() =
        ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED

    // 🔹 15. Tampilkan rationale dialog jika user menolak izin
    private fun showPermissionRationale(positiveAction: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle("Location permission")
            .setMessage("This app will not work without knowing your current location.")
            .setPositiveButton(android.R.string.ok) { _, _ -> positiveAction() }
            .setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    // 🔹 11. Fungsi yang dijalankan setelah izin diberikan
    private fun getLastLocation() {
        Log.d("MapsActivity", "getLastLocation() called.")
        // (Nanti di part berikutnya akan diisi dengan kode untuk ambil lokasi pengguna)
    }
}
