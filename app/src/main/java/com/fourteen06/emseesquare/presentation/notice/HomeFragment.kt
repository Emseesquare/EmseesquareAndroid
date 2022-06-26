package com.fourteen06.emseesquare.presentation.notice

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fourteen06.emseesquare.R
import com.fourteen06.emseesquare.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    lateinit var noticeAdapter: NoticeAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<HomeViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        noticeAdapter = NoticeAdapter()
        noticeAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        linearLayoutManager = LinearLayoutManager(requireContext())

        binding.noticeRecyclerView.apply {
            this.adapter = noticeAdapter
            this.layoutManager = linearLayoutManager
            setHasFixedSize(true)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val lastVisible = linearLayoutManager.findLastVisibleItemPosition() + 1
                    if (viewModel.lastVisibleItem.value < lastVisible) {
                        viewModel.lastVisibleItem.value = lastVisible
                    }
                }
            })
        }

        lifecycleScope.launchWhenCreated {
            viewModel.notices.collect {
                noticeAdapter.submitList(it)
            }
        }

    }
}