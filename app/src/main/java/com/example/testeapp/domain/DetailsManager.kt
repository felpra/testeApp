package com.example.testeapp.domain

import com.example.testeapp.common.areListsEqual
import com.example.testeapp.data.repositores.RemoteRepository
import com.example.testeapp.model.Comments
import com.example.testeapp.model.Post
import com.example.testeapp.model.Result
import com.example.testeapp.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DetailsManager @Inject constructor(
    private val remoteRepository: RemoteRepository) {


    suspend fun getComments(postId: Int): Flow<Result<List<Comments>>> {
        return flow {
            emit(Result.inProgress())
            val remote = remoteRepository.fetchCommentList(postId)

            if (remote.status == Result.Status.SUCCESS) {
                remote.data?.let {
                   emit(Result.success(it))
                }
            } else
                emit(remote)

        }.flowOn(Dispatchers.IO)
    }

    suspend fun getUser(userId: Int): Flow<Result<User>> {
        return flow {
            emit(Result.inProgress())
            val remote = remoteRepository.fetchAuthorInfo(userId)

            if (remote.status == Result.Status.SUCCESS) {
                remote.data?.let {
                    emit(Result.success(it))
                }
            } else
                emit(remote)

        }.flowOn(Dispatchers.IO)
    }
}