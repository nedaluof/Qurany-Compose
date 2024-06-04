@file:Suppress("UNCHECKED_CAST")

package com.nedaluof.qurany.util

import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.databinding.BindingAdapter
import com.nedaluof.qurany.R

object QuranyViewsBindings {

  @JvmStatic
  @BindingAdapter("suraCount")
  fun suraCount(textView: TextView, count: String?) {
    if (count != null) {
      val context = textView.context
      val surasCountLabelOne = context.getString(R.string.sura_count_1)
      val surasCountLabelTwo = context.getString(R.string.sura_count_2)
      val surasCountLabelThree = context.getString(R.string.sura_count_3)
      textView.text = if (count.toInt() > 1) {
        "$surasCountLabelOne $count $surasCountLabelTwo"
      } else {
        surasCountLabelThree
      }
    }
  }

  @JvmStatic
  @BindingAdapter("suraNumber")
  fun suraNumber(textView: TextView, number: Int) {
    val context = textView.context
    val suraNumberLabel = context.getString(R.string.sura_number)
    val suraNum = "$suraNumberLabel $number"
    textView.text = suraNum
  }

  @JvmStatic
  @BindingAdapter("setEmptyQuery")
  fun setEmptyQuery(view: SearchView, emptying: Boolean?) {
    if (emptying != null && emptying) {
      view.setQuery("", false)
      view.clearFocus()
    }
  }
}