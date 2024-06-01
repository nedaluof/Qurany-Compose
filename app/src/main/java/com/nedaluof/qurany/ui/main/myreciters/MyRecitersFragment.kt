package com.nedaluof.qurany.ui.main.myreciters

/**
 * Created by nedaluof on 7/5/2020. {JAVA}
 * Created by nedaluof on 12/12/2020. {Kotlin}
 * Updated by nedaluof on 9/13/2021.
 */
/*
@AndroidEntryPoint
class MyRecitersFragment : BaseFragment<FragmentMyRecitersBinding>() {

  override val layoutId = R.layout.fragment_my_reciters
  override val bindingVariable = BR.viewmodel
  private val myRecitersViewModel: MyRecitersViewModel by viewModels()
  override fun getViewModel() = myRecitersViewModel

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initRecyclerView()
    observeMyRecitersViewModel()
  }

  private fun initRecyclerView() {
    */
/*binding.recitersRecyclerView.adapter = MyRecitersAdapter(
      { reciter ->
        startActivity(
          Intent(activity, SurasActivity::class.java)
            .putExtra(AppConstants.RECITER_KEY, reciter)
        )
      },
      { reciter ->
        val msg1 = resources.getString(R.string.alrt_delete_msg1)
        val msg2 = resources.getString(R.string.alrt_delete_msg2)

        Alerter.create(activity)
          .setTitle(R.string.alrt_delete_title)
          .setText(msg1 + reciter.name + msg2)
          .addButton(
            resources.getString(R.string.alrt_delete_btn_ok),
            R.style.AlertButton
          ) {
            myRecitersViewModel.deleteFromMyReciters(reciter)
            // view.setImageResource(R.drawable.ic_favorite_selected)
            Alerter.hide()
          }
          .addButton(
            resources.getString(R.string.alrt_delete_btn_cancel),
            R.style.AlertButton
          ) { Alerter.hide() }
          .enableSwipeToDismiss()
          .show()
      }
    )*//*

  }

  private fun observeMyRecitersViewModel() {
    with(myRecitersViewModel) {
        error.collectFlow { showError ->
          if (showError) {
            toastyError(R.string.alrt_err_occur_msg)
          }
        }
        resultOfDeleteReciter.collectFlow { success ->
          if (success) {
            toastySuccess(R.string.alrt_delete_success)
          } else {
            toastyError(R.string.alrt_delete_fail)
          }
        }
    }
  }
}*/