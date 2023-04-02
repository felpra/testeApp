package com.example.testeapp.ui

import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testeapp.R
import com.example.testeapp.common.BaseActivity
import com.example.testeapp.databinding.ActivityMainBinding
import com.example.testeapp.model.Post
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(ActivityMainBinding::inflate) {

    val adapterPost = PostAdapter(object : OnPostItemClickListener {
        override fun onFavoriteClicked(post: Post) {
            viewModel.update(post)
        }
    })

    override fun observeData() {

        lifecycleScope.launch {
            viewModel.state
                .filter { !it.postList.isNullOrEmpty() }
                .distinctUntilChanged()
                .collectLatest {
                    it.postList?.let {
                          adapterPost.submitList(it)
                        binding.recyclerview.visibility = View.VISIBLE
                        binding.loading.visibility = View.GONE
                    }
                }
        }

        lifecycleScope.launch {
            viewModel.state
                .filter { it.inProgress == true }
                .distinctUntilChanged()
                .collectLatest {
                    binding.loading.visibility = View.VISIBLE
                    binding.recyclerview.visibility = View.GONE
                }
        }

        lifecycleScope.launch {
            viewModel.state
                .filter { !it.errorMessage.isNullOrEmpty() && it.postList.isNullOrEmpty() }
                .distinctUntilChanged()
                .collectLatest {
                    binding.error.apply {
                        text = (it.errorMessage + "\n Click to try again")
                        visibility = View.VISIBLE
                        setOnClickListener {
                            viewModel.fetchPosts()
                        }
                    }
                    binding.loading.visibility = View.GONE
                    binding.recyclerview.visibility = View.GONE
                }
        }

    }

    override fun setUpUi() {
        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = adapterPost
            val dividerItemDecoration = DividerItemDecoration(
                context, LinearLayoutManager.VERTICAL
            )
            val verticalDivider = ContextCompat.getDrawable(context, R.drawable.divisor)

            if (verticalDivider != null) {
                dividerItemDecoration.setDrawable(verticalDivider)
            }
            addItemDecoration(dividerItemDecoration)
        }

        binding.fab.setOnClickListener {
            viewModel.deleteAllExceptFavorites()
        }
    }


}