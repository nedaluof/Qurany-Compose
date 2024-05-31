package com.nedaluof.qurany.ui.base

import android.annotation.SuppressLint
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by NedaluOf on 9/12/2021.
 */
abstract class BaseRecyclerAdapter<T> :
  RecyclerView.Adapter<BaseRecyclerAdapter.BaseViewHolder>() {

  val items = ArrayList<T>()

  @SuppressLint("NotifyDataSetChanged")
  open fun addItems(items: List<T>) {
    with(this.items) {
      clear()
      addAll(items)
    }
    notifyDataSetChanged()
  }

  override fun getItemCount() = items.size

  override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
    holder.onBind(position)
  }

  abstract class BaseViewHolder(
    binding: ViewDataBinding
  ) : RecyclerView.ViewHolder(binding.root) {
    abstract fun onBind(position: Int)
  }
}