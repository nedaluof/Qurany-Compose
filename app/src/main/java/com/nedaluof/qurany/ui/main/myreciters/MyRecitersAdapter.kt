package com.nedaluof.qurany.ui.main.myreciters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.nedaluof.qurany.data.model.Reciter
import com.nedaluof.qurany.databinding.ItemMyReciterBinding
import com.nedaluof.qurany.ui.base.BaseRecyclerAdapter

/**
 * Created by nedaluof on 12/12/2020.
 */
class MyRecitersAdapter(
  val onReciterClicked: (Reciter) -> Unit,
  val onDeleteFromMyRecitersClicked: (Reciter) -> Unit,
) : BaseRecyclerAdapter<Reciter>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
    MyRecitersVH(
      ItemMyReciterBinding.inflate(
        LayoutInflater.from(parent.context), parent,
        false
      )
    )

  inner class MyRecitersVH(
    val binding: ItemMyReciterBinding,
  ) : BaseRecyclerAdapter.BaseViewHolder(binding) {
    override fun onBind(position: Int) {
      val reciterData = items[position]
      with(binding) {
        myRecitersItemLyt.setOnClickListener { onReciterClicked(reciterData) }
        deleteReciterFromFavorite.setOnClickListener {
          onDeleteFromMyRecitersClicked(
            reciterData
          )
        }
        reciter = reciterData
        executePendingBindings()
      }
    }
  }
}