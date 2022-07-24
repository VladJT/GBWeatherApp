package jt.projects.gbweatherapp.utils

import android.content.pm.PackageManager
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import jt.projects.gbweatherapp.BaseActivity


open class PermissionActivity : AppCompatActivity() {
    private val REQUEST_CODE = 999
    lateinit var functionSuccess: () -> Unit
    lateinit var functionFailed: () -> Unit
    lateinit var permission: String

    // permission like Manifest.permission.ACCESS_FINE_LOCATION
    fun tryJob(
        permission: String,
        functionSucess: () -> Unit,
        rDlgTitle: String,
        rDlgMessage: String,
        functionFailed: () -> Unit = { defaultFailedNotification() }
    ) {
        this.functionSuccess = functionSucess
        this.functionFailed = functionFailed
        this.permission = permission

        val permResult =
            ContextCompat.checkSelfPermission(this, permission)
        if (permResult == PackageManager.PERMISSION_GRANTED) {
            functionSucess.invoke()
        } else if (shouldShowRequestPermissionRationale(permission)) {
            AlertDialog.Builder(this)
                .setTitle(rDlgTitle)
                .setMessage(rDlgMessage)
                .setPositiveButton("Предоставить доступ") { _, _ ->
                    permissionRequest(permission)
                }
                .setNegativeButton("Отказаться") { dialog, _ ->
                    dialog.dismiss()
                    functionFailed.invoke()
                }
                .create()
                .show()
        } else {
            permissionRequest(permission)
        }
    }

    private fun defaultFailedNotification() {
        (this as BaseActivity).showMsgDialog(
            "Отказ",
            "Без соответствующих разрешений выполнение невозможно"
        )
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
                    Log.d("@@@", "Доступ получен")
                    functionSuccess.invoke()
                    return
                }
            }
            functionFailed.invoke()
        }
    }
}