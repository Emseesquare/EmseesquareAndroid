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
import com.fourteen06.emseesquare.models.AttachmentType
import com.fourteen06.emseesquare.models.NoticeModel
import com.fourteen06.emseesquare.models.User
import com.fourteen06.emseesquare.models.UserRole
import com.fourteen06.emseesquare.repository.notice.AddNoticeUseCase
import com.fourteen06.emseesquare.utils.AlertExt.makeLongToast
import com.fourteen06.emseesquare.utils.Resource
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    lateinit var noticeAdapter: NoticeAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var binding: FragmentHomeBinding
    val user = User(
        id = Firebase.auth.currentUser!!.uid,
        name = "Shashank Daima",
        subTitle = "PROGRAMMER",
        profileImageUrl = "https://avatars.githubusercontent.com/u/64317542?s=96&v=4",
//        role = UserRole.Student,
        instituteId = "",
        instituteName = ""
    )
    val notice = NoticeModel(
        id = "1", time = Date(System.currentTimeMillis()),
        content = "Some Lorem ipsum is there siadjkghadsghj asdjkhgasdg kdasdhf dsakjghae rkjsdahgadgda",
        pins = 0,
        attachmentType = AttachmentType.None,
        user = user
    )

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
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                addNoticeUseCase(notice).collect {
                    when (it) {
                        is Resource.Error -> {
                            makeLongToast(it.message)
                        }
                        is Resource.Loading -> {
                            makeLongToast("LOADING")
                        }
                        is Resource.Success -> {
                            makeLongToast("Success")

                        }
                    }
                }
            }
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

    }
}