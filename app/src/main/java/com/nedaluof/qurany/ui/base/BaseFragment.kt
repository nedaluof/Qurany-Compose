package com.nedaluof.qurany.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.nedaluof.qurany.util.toastyError
import com.nedaluof.qurany.util.toastySuccess
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Created by NedaluOf on 9/11/2021.
 */
abstract class BaseFragment<DB : ViewDataBinding> : Fragment() {

  lateinit var binding: DB
  abstract val layoutId: Int
  abstract val bindingVariable: Int
  abstract fun getViewModel(): BaseViewModel?

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
    with(binding) {
      setVariable(bindingVariable, getViewModel())
      lifecycleOwner = viewLifecycleOwner
      executePendingBindings()
    }
    return binding.root
  }

  fun toastyError(stringId: Int) {
    requireActivity().toastyError(stringId)
  }

  fun toastySuccess(stringId: Int) {
    requireActivity().toastySuccess(stringId)
  }

  fun <T> StateFlow<T>.collectFlow(block: (T) -> Unit) {
    lifecycleScope.launch {
      this@collectFlow.collect { block(it) }
    }
  }
}
