package com.nedaluof.qurany.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.nedaluof.qurany.R
import java.io.File
import java.io.Serializable

/**
 * Created by nedaluof on 12/13/2020.
 */
fun Context.toast(@StringRes msg: Int) =
  Toast.makeText(this, this.getString(msg), Toast.LENGTH_LONG).show()

fun Context.checkIfSuraExist(subPath: String) =
  File(this.getExternalFilesDir(null).toString() + subPath).exists()

fun Context.getSuraPath(subPath: String): String =
  File(this.getExternalFilesDir(null).toString() + subPath).absolutePath

fun Context.getLogoAsBitmap(): Bitmap {
  val width = 200
  val height = 200
  val drawable = ContextCompat.getDrawable(this, R.drawable.ic_qurany)
  val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
  val canvas = Canvas(bitmap)
  drawable?.setBounds(0, 0, width, height)
  drawable?.draw(canvas)
  return bitmap
}

fun Context.isNetworkOk(): Boolean {
  val connectivityManager =
    this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
  return run {
    val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    if (capabilities != null) {
      when {
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
          true
        }

        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
          true
        }

        else -> capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
      }
    } else {
      false
    }
  }
}

@Suppress("DEPRECATION")
inline fun <reified CLASS> Intent?.parcelable(key: String): CLASS {
  return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    this?.getParcelableExtra(key, CLASS::class.java)!!
  } else {
    this?.getParcelableExtra(key)!!
  }
}

inline fun <reified T : Serializable> Bundle.serializable(key: String): T? = when {
  Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializable(key, T::class.java)
  else -> @Suppress("DEPRECATION") getSerializable(key) as? T
}

fun (() -> Unit).postDelayed(
  mills: Long = 1000
) {
  Handler(Looper.getMainLooper()).postDelayed(this, mills)
}