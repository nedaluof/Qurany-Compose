package com.nedaluof.qurany.ui.screens.main.suras

import android.view.LayoutInflater
import android.view.ViewGroup
import com.nedaluof.qurany.data.model.SuraModel
import com.nedaluof.qurany.databinding.ItemSuraBinding
import com.nedaluof.qurany.ui.base.BaseRecyclerAdapter

/**
 * Created by nedaluof on 12/17/2020.
 */

class SurasAdapter(
  val onPlaySuraClicked: (SuraModel) -> Unit,
  val onDownloadSuraClicked: (SuraModel) -> Unit,
) : BaseRecyclerAdapter<SuraModel>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SurasVH(
    ItemSuraBinding.inflate(
      LayoutInflater.from(parent.context), parent, false
    )
  )

  inner class SurasVH(
    private val binding: ItemSuraBinding,
  ) : BaseViewHolder(binding) {
    override fun onBind(position: Int) {
      val suraData = items[position]
      with(binding) {
        sura = suraData
        executePendingBindings()
        playSuraBtn.setOnClickListener { onPlaySuraClicked(suraData) }
        downloadSuraBtn.setOnClickListener { onDownloadSuraClicked(suraData) }
      }
    }
  }
}