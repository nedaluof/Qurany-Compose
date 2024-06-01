package com.nedaluof.qurany.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.nedaluof.qurany.R
import es.dmoral.toasty.Toasty
import java.io.File
import java.util.Locale

/**
 * Created by nedaluof on 12/13/2020.
 */

fun View.click(block: () -> Unit) {
  this.setOnClickListener { block() }
}

fun Activity.toast(@StringRes message: Int) =
  Toast.makeText(this, message, Toast.LENGTH_LONG).show()

fun Context.toastyError(@StringRes msg: Int) =
  Toasty.error(this, msg).show()

fun Context.toastySuccess(@StringRes msg: Int) =
  Toasty.success(this, msg).show()

fun Context.toastyInfo(@StringRes msg: Int) =
  Toasty.info(this, msg).show()

fun Context.toastyInfo(msg: String) =
  Toasty.info(this, msg).show()

/**
 * @return user device language
 */
fun getLanguage() =
  if (Locale.getDefault().language.contains("ar")) "_arabic" else "_english"

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
  val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE)
      as ConnectivityManager
  return run {
    val capabilities =
      connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
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
inline fun <reified CLASS> Intent?.getParcelableExtraT(key: String): CLASS {
  return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    this?.getParcelableExtra(key, CLASS::class.java)!!
  } else {
    this?.getParcelableExtra(key)!!
  }
}

fun Context.getAppVersionName() = packageManager.getPackageInfo(packageName, 0).versionName