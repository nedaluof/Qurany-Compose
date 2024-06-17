package com.nedaluof.qurany.util

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.annotation.StringRes
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Created by nedaluof on 12/13/2020.
 */
fun Context.toast(@StringRes msg: Int) =
  Toast.makeText(this, this.getString(msg), Toast.LENGTH_LONG).show()

fun Context.isInternetAvailable(): Boolean {
  val connectivityManager =
    this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
  val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
  return if (capabilities != null) {
    when {
      capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
      capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
      else -> capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    }
  } else {
    false
  }
}

inline fun <reified CLASS> Intent?.parcelable(key: String): CLASS {
  return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    this?.getParcelableExtra(key, CLASS::class.java)!!
  } else {
    @Suppress("DEPRECATION") this?.getParcelableExtra(key)!!
  }
}

fun <T> MutableStateFlow<T>.set(
  value: T,
  idleValue: T,
  duration: Long = 2000
) {
  this.value = value
  Handler(Looper.getMainLooper()).postDelayed({
    this.value = idleValue
  }, duration)
}