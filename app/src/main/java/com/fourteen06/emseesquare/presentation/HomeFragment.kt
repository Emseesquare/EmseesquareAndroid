package com.fourteen06.emseesquare.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.fourteen06.emseesquare.R
import com.fourteen06.emseesquare.databinding.FragmentHomeBinding
import com.fourteen06.emseesquare.models.AttachmentType
import com.fourteen06.emseesquare.models.NoticeModel
import com.fourteen06.emseesquare.models.User
import com.fourteen06.emseesquare.models.UserRole
import com.fourteen06.emseesquare.repository.getPagedNotices
import kotlinx.coroutines.flow.collect

class HomeFragment : Fragment(R.layout.fragment_home) {
    lateinit var noticeAdapter: NoticeAdapter
    lateinit var binding: FragmentHomeBinding
//    private val user = User(
//        id = "1",
//        name = "Shashank",
//        subTitle = "Teacher",
//        profileImageUrl = "",
//        role = UserRole.Student,
//        instituteName = "First",
//        instituteId = "1"
//    )
//    private val list = listOf(
//        NoticeModel(
//            user = this.user,
//            id = 1,
//            time = 1000,
//            content = "Text styling is one of the important aspects when it comes to enhancing the UI of an Android application. In Android, we can change the size, color, weight, style, etc of a text and make the text more attractive and appealing.\n" +
//                    "\n" +
//                    "But think of a situation, when you want different colors for different parts of a TextView. For example, if the text is \"Hello Android\" and you want to have the color of \"Hello\" as green and \"Android\" as red. How can you achieve this? You can make two TextViews and set the textColor to green and red respectively. But it is not a good way of doing this. So, here comes the role of Styling the texts with the help of Spans.\n",
//            pins = 213,
//            attachmentType = AttachmentType.None
//        ),
//        NoticeModel(
//            user = this.user,
//            id = 1,
//            time = 1000,
//            content = "Text styling is one of the important aspects when it comes to enhancing the UI of an Android application. In Android, we can change the size, color, weight, style, etc of a text and make the text more attractive and appealing.\n" +
//                    "\n" +
//                    "But think of a situation, when you want different colors for different parts of a TextView. For example, if the text is \"Hello Android\" and you want to have the color of \"Hello\" as green and \"Android\" as red. How can you achieve this? You can make two TextViews and set the textColor to green and red respectively. But it is not a good way of doing this. So, here comes the role of Styling the texts with the help of Spans.\n",
//            pins = 213,
//            attachmentType = AttachmentType.None
//        ),
//        NoticeModel(
//            user = this.user,
//            id = 1,
//            time = 1000,
//            content = "Text styling is one of the important aspects when it comes to enhancing the UI of an Android application. In Android, we can change the size, color, weight, style, etc of a text and make the text more attractive and appealing.\n" +
//                    "\n" +
//                    "But think of a situation, when you want different colors for different parts of a TextView. For example, if the text is \"Hello Android\" and you want to have the color of \"Hello\" as green and \"Android\" as red. How can you achieve this? You can make two TextViews and set the textColor to green and red respectively. But it is not a good way of doing this. So, here comes the role of Styling the texts with the help of Spans.\n",
//            pins = 213,
//            attachmentType = AttachmentType.None
//        ),
//        NoticeModel(
//            user = this.user,
//            id = 1,
//            time = 1000,
//            content = "Text styling is one of the important aspects when it comes to enhancing the UI of an Android application. In Android, we can change the size, color, weight, style, etc of a text and make the text more attractive and appealing.\n" +
//                    "\n" +
//                    "But think of a situation, when you want different colors for different parts of a TextView. For example, if the text is \"Hello Android\" and you want to have the color of \"Hello\" as green and \"Android\" as red. How can you achieve this? You can make two TextViews and set the textColor to green and red respectively. But it is not a good way of doing this. So, here comes the role of Styling the texts with the help of Spans.\n",
//            pins = 213,
//            attachmentType = AttachmentType.None
//        ),
//    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        noticeAdapter = NoticeAdapter()
        binding.noticeRecyclerView.apply {
            this.adapter = noticeAdapter
            this.layoutManager = LinearLayoutManager(requireContext())
        }
        lifecycleScope.launchWhenStarted {
            getPagedNotices().collect {
                noticeAdapter.submitList(it)
            }
        }

    }
}