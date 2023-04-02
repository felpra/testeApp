package com.example.testeapp.api

import com.example.testeapp.model.Comments
import com.example.testeapp.model.Post
import com.example.testeapp.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface XApi {
    @GET("/posts")
    suspend fun getPostsList(): Response<List<Post>>

    @GET("/posts/{id}/comments")
    suspend fun getCommentsForPost(@Path("id") id: Int): Response<List<Comments>>

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Int): Response<User>
}