package com.nedaluof.qurany.ui.main.reciters

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import com.nedaluof.qurany.BR
import com.nedaluof.qurany.R
import com.nedaluof.qurany.databinding.FragmentRecitersBinding
import com.nedaluof.qurany.ui.base.BaseFragment
import com.nedaluof.qurany.ui.main.suras.SurasActivity
import com.nedaluof.qurany.util.AppConstants
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by nedaluof on 12/11/2020.
 */
@AndroidEntryPoint
class RecitersFragment : BaseFragment<FragmentRecitersBinding>() {

  override val layoutId = R.layout.fragment_reciters
  override val bindingVariable = BR.viewmodel
  private val recitersViewModel by viewModels<RecitersViewModel>()
  override fun getViewModel() = recitersViewModel
  private lateinit var reciterAdapter: RecitersAdapter

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initRecyclerViewAdapter()
    initSearchOfReciters()
    observeViewModel()
  }

  private fun initRecyclerViewAdapter() {
    reciterAdapter = RecitersAdapter(
      { reciter ->
        startActivity(
          Intent(context, SurasActivity::class.java)
            .putExtra(AppConstants.RECITER_KEY, reciter)
        )
      },
      { pair ->
        pair.first.visibility = View.GONE
        recitersViewModel.addReciterToMyReciters(pair.second)
      }
    )
    binding.recitersRecyclerView.adapter = reciterAdapter
  }

  private fun initSearchOfReciters() {
    binding.recitersSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
      override fun onQueryTextSubmit(query: String): Boolean {
        doSearch(query)
        return false
      }

      override fun onQueryTextChange(newText: String): Boolean {
        doSearch(newText)
        return false
      }

      private fun doSearch(text: String) {
        reciterAdapter.filter.filter(text)
      }
    })
  }


  private fun observeViewModel() {
    with(recitersViewModel) {
      error.collectFlow { (_, showError) ->
        if (showError) toastyError(R.string.alrt_err_occur_msg)
      }

      resultOfAddReciter.collectFlow { success ->
        if (success) toastySuccess(R.string.alrt_add_success_msg)
      }
    }
  }
}