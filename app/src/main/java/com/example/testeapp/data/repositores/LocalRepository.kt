package com.example.testeapp.data.repositores

import com.example.testeapp.model.Post

interface LocalRepository {
    fun getPostsCache(): List<Post>
    fun savePosts(posts: List<Post>)
    fun savePost(post: Post)
    suspend fun updatePost(post: Post)
    fun deleteAll()
}