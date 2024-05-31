package com.nedaluof.qurany.data.repositoryImpl

import android.content.Context
import android.content.pm.PackageManager
import com.nedaluof.qurany.R
import com.nedaluof.qurany.data.repository.SplashRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Created by NedaluOf on 9/11/2021.
 */
class SplashRepositoryImpl @Inject constructor(
  @ApplicationContext
  private val context: Context
) : SplashRepository {
  override fun getAppVersionName() = try {
    val versionLabel = context.getString(R.string.app_version)
    val versionName = context.packageManager.getPackageInfo(context.packageName, 0).versionName
    "$versionLabel $versionName"
  } catch (e: PackageManager.NameNotFoundException) {
    e.printStackTrace()
    "1.0"
  }
}