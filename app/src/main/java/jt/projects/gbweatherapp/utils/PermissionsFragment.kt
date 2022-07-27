package jt.projects.gbweatherapp.utils

import android.content.pm.PackageManager
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

@Deprecated("Удобнее через PersmissionActivity")
open class PermissionsFragment : Fragment() {
    private val REQUEST_CODE = 999
    lateinit var function: () -> Unit
    lateinit var permission: String

    // permission like Manifest.permission.ACCESS_FINE_LOCATION
    fun tryJob(permission: String, function: () -> Unit, rDlgTitle: String, rDlgMessage: String) {
        this.function = function
        this.permission = permission
        val permResult =
            ContextCompat.checkSelfPermission(requireContext(), permission)
        if (permResult == PackageManager.PERMISSION_GRANTED) {
            function.invoke()
        } else if (shouldShowRequestPermissionRationale(permission)) {
            AlertDialog.Builder(requireContext())
                .setTitle(rDlgTitle)
                .setMessage(rDlgMessage)
                .setPositiveButton("Предоставить доступ") { _, _ ->
                    permissionRequest(permission)
                }
                .setNegativeButton("Отказаться") { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        } else {
            permissionRequest(permission)
        }
    }

    private fun permissionRequest(permission: String) {
        requestPermissions(arrayOf(permission), REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE) {
            for (pIndex in permissions.indices) {
                if (permissions[pIndex] == permission
                    && grantResults[pIndex] == PackageManager.PERMISSION_GRANTED
                ) {
                    Log.d(TAG, "Доступ получен")
                    function.invoke()
                }
            }
        }
    }
}