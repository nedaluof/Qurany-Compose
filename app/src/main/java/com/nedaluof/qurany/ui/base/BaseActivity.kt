package com.nedaluof.qurany.ui.base

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.nedaluof.qurany.di.LocaleManagerEntryPoint
import com.nedaluof.qurany.util.LocaleManager
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by NedaluOf on 9/11/2021.
 */
abstract class BaseActivity<DB : ViewDataBinding> : AppCompatActivity() {

  lateinit var binding: DB
  abstract val layoutId: Int
  abstract val bindingVariable: Int
  abstract fun getViewModel(): ViewModel?

  @Inject
  lateinit var localeManager: LocaleManager

  override fun attachBaseContext(newBase: Context?) {
    val localeManager = EntryPointAccessors.fromApplication(
      newBase!!,
      LocaleManagerEntryPoint::class.java
    ).localeManager
    super.attachBaseContext(newBase.let { localeManager.configureAppLocale(it) })
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, layoutId)
    with(binding) {
      setVariable(bindingVariable, getViewModel())
      lifecycleOwner = this@BaseActivity
      executePendingBindings()
    }
  }

  fun <T> StateFlow<T>.collectFlow(data: (T) -> Unit) {
    lifecycleScope.launch {
      collect { data(it) }
    }
  }
}
