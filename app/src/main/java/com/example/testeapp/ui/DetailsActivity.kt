package com.example.testeapp.ui

import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testeapp.R
import com.example.testeapp.common.BaseActivity
import com.example.testeapp.databinding.ActivityDetailsBinding
import com.example.testeapp.model.Post
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsActivity : BaseActivity<ActivityDetailsBinding, DetailsViewModel>(ActivityDetailsBinding::inflate) {
    val commentAdapter = CommentAdapter()

    override fun observeData() {
        lifecycleScope.launch {
            viewModel.state
                .filter { !it.commentsList.isNullOrEmpty() }
                .distinctUntilChanged()
                .collectLatest {
                    it.commentsList?.let {
                        commentAdapter.submitList(it)
                        binding.commentsRecyclerView.visibility = View.VISIBLE
                        binding.commentsLabel.visibility = View.VISIBLE
                    }
                }
        }

        lifecycleScope.launch {
            viewModel.state
                .filter { it.user != null }
                .distinctUntilChanged()
                .collectLatest {
                    it.user?.let {
                        binding.authorNameValueTextView.text = it.name
                    }
                }
        }

        lifecycleScope.launch {
            viewModel.state
                .filter { it.inProgress == true }
                .distinctUntilChanged()
                .collectLatest {
                    binding.commentsRecyclerView.visibility = View.GONE
                }
        }

    }

    override fun setUpUi() {
        val post = intent.getParcelableExtra<Post>("post")
        binding.postTitleValueTextView.text = post?.title ?: "no_title"
        binding.postBodyValueTextView.text = post?.body ?: "no_content"

        post?.id?.let { viewModel.fetchComments(it) }
        post?.userId?.let { viewModel.fetchUser(it) }

        binding.commentsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = commentAdapter
            val dividerItemDecoration = DividerItemDecoration(
                context, LinearLayoutManager.VERTICAL
            )
            val verticalDivider = ContextCompat.getDrawable(context, R.drawable.divisor)

            if (verticalDivider != null) {
                dividerItemDecoration.setDrawable(verticalDivider)
            }
            addItemDecoration(dividerItemDecoration)
        }
    }
}