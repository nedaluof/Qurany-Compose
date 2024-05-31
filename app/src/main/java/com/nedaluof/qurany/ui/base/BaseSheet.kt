package com.nedaluof.qurany.ui.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nedaluof.qurany.R

/**
 * Created by NedaluOf on 12/13/2021.
 */
abstract class BaseSheet<DB : ViewDataBinding> : BottomSheetDialogFragment() {

  @get:LayoutRes
  abstract val layoutId: Int
  abstract val variable: Int
  abstract fun viewModel(): ViewModel
  protected lateinit var binding: DB

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    initBottomSheetBehavior { state ->
      when (state) {
        BottomSheetBehavior.STATE_COLLAPSED -> dismiss()
        BottomSheetBehavior.STATE_HIDDEN -> dismiss()
      }
    }
    binding = DataBindingUtil.inflate(
      inflater, layoutId, container, false
    )
    with(binding) {
      lifecycleOwner = viewLifecycleOwner
      setVariable(variable, viewModel())
      executePendingBindings()
    }
    return binding.root
  }

  override fun getTheme() = R.style.AppBottomSheetDialogTheme

  private fun BottomSheetDialogFragment.initBottomSheetBehavior(stateChanged: (Int) -> Unit) {
    // expand the bottom sheet
    (dialog as? BottomSheetDialog)?.behavior?.state = BottomSheetBehavior.STATE_EXPANDED
    // Set the callback to know the state of the bottom sheet
    val sheetBehavior = (this.dialog as BottomSheetDialog).behavior
    sheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
      override fun onStateChanged(bottomSheet: View, newState: Int) {
        stateChanged.invoke(newState)
      }

      override fun onSlide(bottomSheet: View, slideOffset: Float) {}
    })
  }
}