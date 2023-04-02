package com.example.testeapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testeapp.domain.DetailsManager
import com.example.testeapp.model.Comments
import com.example.testeapp.model.Result
import com.example.testeapp.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val detailsManager: DetailsManager) : ViewModel() {
    private val _state = MutableStateFlow(UiDetailState())
    val state: StateFlow<UiDetailState> = _state.asStateFlow()


    fun fetchComments(postId: Int) {
        viewModelScope.launch {
            detailsManager.getComments(postId).collect { result ->
                when(result.status){
                    Result.Status.SUCCESS -> {
                        _state.update {
                            it.copy(commentsList = result.data, inProgress = false)
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

    fun fetchUser(userId: Int) {
        viewModelScope.launch {
            detailsManager.getUser(userId).collect { result ->
                when(result.status){
                    Result.Status.SUCCESS -> {
                        _state.update {
                            it.copy(user = result.data, inProgress = false)
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
}

data class UiDetailState(
    val commentsList: List<Comments>? = null,
    val user: User? = null,
    val inProgress: Boolean? = null,
    val errorMessage: String? = null
)