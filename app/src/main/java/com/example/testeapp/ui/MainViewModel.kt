package com.example.testeapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testeapp.domain.PostsManager
import com.example.testeapp.model.Post
import com.example.testeapp.model.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val postsManager: PostsManager) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        fetchPosts()
    }

    fun fetchPosts() {
        viewModelScope.launch {
            postsManager.getPosts().collect { result ->
                when(result.status){
                    Result.Status.SUCCESS -> {
                        _state.update {
                            it.copy(postList = result.data, inProgress = false)
                        }
                    }
                    Result.Status.IN_PROGRESS -> {
                        _state.update {
                            it.copy(inProgress = true)
                        }
                    }
                    Result.Status.ERROR -> {
                        _state.update {
                            it.copy(inProgress = false, errorMessage = result.error?.status_message ?: result.message)
                        }
                    }
                }
            }
        }
    }

    fun update(post: Post) {
        viewModelScope.launch {
            postsManager.updatePost(post)
        }
    }

    fun deleteAllExceptFavorites() {
        _state.update {
            it.copy(postList = state.value.postList?.filter { it.isFavorite })
        }
    }
}



data class UiState(
    val postList: List<Post>? = null,
    val inProgress: Boolean? = null,
    val errorMessage: String? = null
)