package com.fourteen06.emseesquare.presentation.notice

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fourteen06.emseesquare.R
import com.fourteen06.emseesquare.databinding.FragmentHomeBinding
import com.fourteen06.emseesquare.repository.notice.AddNoticeUseCase
import com.fourteen06.emseesquare.utils.AlertExt.makeShortToast
import com.fourteen06.emseesquare.utils.MultistackBaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : MultistackBaseFragment(
    R.layout.fragment_home,
    R.string.title_home,
    null,
    true,
    true,
    null,
    true,
    R.menu.home_menu
) {
    lateinit var noticeAdapter: NoticeAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var binding: FragmentHomeBinding


    @Inject
    lateinit var addNoticeUseCase: AddNoticeUseCase
    private val viewModel by viewModels<HomeViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        noticeAdapter = NoticeAdapter { noticeId ->
            viewModel.init(HomeInState.ChangeLikeStatus(noticeId))
        }
        noticeAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        linearLayoutManager = LinearLayoutManager(requireContext())
        binding.fab.setOnClickListener {
            sendUserToAddNotice()
        }
        binding.noticeRecyclerView.apply {
            this.adapter = noticeAdapter
            this.layoutManager = linearLayoutManager
            setHasFixedSize(true)
        }

        viewModel.notices.observe(viewLifecycleOwner) {
            noticeAdapter.submitList(it)
        }
        viewModel.events.asLiveData().observe(viewLifecycleOwner) {
            when (it) {
                is HomeOutState.MakeToast -> makeShortToast(it.message)
            }
        }
    }

    private fun sendUserToAddNotice() {
        findChildNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAddNotice())
    }
}