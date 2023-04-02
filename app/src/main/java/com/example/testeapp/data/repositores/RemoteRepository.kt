package com.example.testeapp.data.repositores

import com.example.testeapp.model.*
import retrofit2.Response
import retrofit2.Retrofit

interface RemoteRepository {

    suspend fun fetchPostList(): Result<List<Post>>
    suspend fun fetchCommentList(postId: Int): Result<List<Comments>>
    suspend fun fetchAuthorInfo(userId: Int): Result<User>
    suspend fun <T> getResponse(request: suspend () -> Response<T>, defaultErrorMessage: String): Result<T>
    fun parseError(response: Response<*>, retrofit: Retrofit): Error?

}