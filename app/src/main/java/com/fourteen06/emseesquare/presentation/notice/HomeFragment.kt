package com.fourteen06.emseesquare.presentation.notice

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fourteen06.emseesquare.R
import com.fourteen06.emseesquare.databinding.FragmentHomeBinding
import com.fourteen06.emseesquare.repository.notice.AddNoticeUseCase
import com.fourteen06.emseesquare.utils.AlertExt.makeLongToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    lateinit var noticeAdapter: NoticeAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var binding: FragmentHomeBinding


    @Inject
    lateinit var addNoticeUseCase: AddNoticeUseCase
    private val viewModel by viewModels<HomeViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        noticeAdapter = NoticeAdapter()
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
        (activity as AppCompatActivity).title = getString(R.string.title_home)
        setHasOptionsMenu(true)
    }

    private fun sendUserToAddNotice() {
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAddNotice())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home_menu_option_pin -> {
                makeLongToast("HOME PINNED")
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }
}