package com.example.testeapp.data.repositores

import com.example.testeapp.api.XApi
import com.example.testeapp.model.*
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException
import javax.inject.Inject

class RemoteRepositoryImpl @Inject constructor(private val retrofit: Retrofit): RemoteRepository {
    val postService = retrofit.create(XApi::class.java)

    override suspend fun fetchPostList(): Result<List<Post>> {
        return getResponse(
            request = {
                postService.getPostsList()
            },
            defaultErrorMessage = "Error fetching post List")
    }

    override suspend fun fetchCommentList(postId: Int): Result<List<Comments>> {
        return getResponse(
            request = {
                postService.getCommentsForPost(postId)
            },
            defaultErrorMessage = "Error fetching post List")
    }

    override suspend fun fetchAuthorInfo(userId: Int): Result<User> {
        return getResponse(
            request = {
                postService.getUserById(userId)
            },
            defaultErrorMessage = "Error fetching post List")
    }


    override suspend fun <T> getResponse(request: suspend () -> Response<T>, defaultErrorMessage: String): Result<T> {
        return try {
            val result = request.invoke()
            if (result.isSuccessful) {
                return Result.success(result.body())
            } else {
                val errorResponse = parseError(result, retrofit)
                Result.error(errorResponse?.status_message ?: defaultErrorMessage, errorResponse)
            }
        } catch (e: Throwable) {
            Result.error("Unknown Error", null)
        }
    }


    override fun parseError(response: Response<*>, retrofit: Retrofit): Error? {
        val converter = retrofit.responseBodyConverter<Error>(Error::class.java, arrayOfNulls(0))
        return try {
            converter.convert(response.errorBody()!!)
        } catch (e: IOException) {
            Error()
        }
    }

}