package com.fourteen06.emseesquare.presentation.notice

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.fourteen06.emseesquare.R
import com.fourteen06.emseesquare.databinding.FragmentHomeBinding
import com.fourteen06.emseesquare.repository.getPagedNotices
import kotlinx.coroutines.flow.collect

class HomeFragment : Fragment(R.layout.fragment_home) {
    lateinit var noticeAdapter: NoticeAdapter
    lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<HomeViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        noticeAdapter = NoticeAdapter()
        binding.noticeRecyclerView.apply {
            this.adapter = noticeAdapter
            this.layoutManager = LinearLayoutManager(requireContext())
        }
        lifecycleScope.launchWhenStarted {
            viewModel.notices.collect {
                noticeAdapter.submitList(it)
            }
        }

    }
}