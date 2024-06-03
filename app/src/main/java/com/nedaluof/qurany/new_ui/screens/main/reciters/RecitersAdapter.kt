package com.nedaluof.qurany.new_ui.screens.main.reciters

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.RequiresApi
import com.nedaluof.qurany.data.model.Reciter
import com.nedaluof.qurany.databinding.ItemReciterBinding
import com.nedaluof.qurany.new_ui.base.BaseRecyclerAdapter
import com.nedaluof.qurany.util.getLanguage

/**
 * Created by nedaluof on 12/11/2020.
 */
class RecitersAdapter(
  val onReciterClicked: (Reciter) -> Unit,
  val onAddReciterToFavoriteClicked: (Pair<View, Reciter>) -> Unit,
) : BaseRecyclerAdapter<Reciter>(), Filterable {

  private val filteredList = ArrayList<Reciter>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RecitersVH(
    ItemReciterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
  )

  override fun addItems(items: List<Reciter>) {
    with(filteredList) {
      clear()
      addAll(items)
    }
    super.addItems(items)
  }

  inner class RecitersVH(
    private val binding: ItemReciterBinding,
  ) : BaseViewHolder(binding) {
    override fun onBind(position: Int) {
      try {
        val comingReciter = items[position]
        with(binding) {
          reciter = comingReciter
          executePendingBindings()
          reciterItemLyt.setOnClickListener {
            onReciterClicked(comingReciter)
          }
          addReciterToFavorite.setOnClickListener {
            onAddReciterToFavoriteClicked(Pair(it, comingReciter))
          }
        }
      } catch (e: Exception) {
        e.printStackTrace()
      }
    }
  }

  override fun getFilter(): Filter = searchFilter

  private val searchFilter = object : Filter() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun performFiltering(constraint: CharSequence?): FilterResults {
      val filterResults = FilterResults()
      filterResults.values = if (constraint == null || constraint.isEmpty()) {
        filteredList
      } else {
        if (getLanguage() == "_arabic") {
          val pattern = constraint.toString().trim { it <= ' ' }
          filteredList.filter { reciter ->
            reciter.name?.contains(pattern)!!
          }
        } else {
          filteredList.filter { reciter ->
            reciter.name?.firstName()
              ?.contains(constraint.toString().trim { it <= ' ' })!! ||
                reciter.name?.secondName()?.contains(constraint.toString())!!
          }
        }
      }
      return filterResults
    }

    @SuppressLint("NotifyDataSetChanged")
    @Suppress("UNCHECKED_CAST")
    override fun publishResults(p0: CharSequence?, results: FilterResults?) {
      with(items) {
        if (results == null) {
          clear()
          addAll(ArrayList())
        } else {
          clear()
          addAll(results.values as List<Reciter>)
        }
      }
      notifyDataSetChanged()
    }
  }

  fun String.firstName(): String = this.substring(0, this.indexOf(' '))
  fun String.secondName(): String = this.substring(this.indexOf(' ') + 1)
}