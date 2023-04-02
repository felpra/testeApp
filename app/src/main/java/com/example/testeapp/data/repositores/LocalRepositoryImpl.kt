package com.example.testeapp.data.repositores

import com.example.testeapp.data.PostDao
import com.example.testeapp.model.Post
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRepositoryImpl @Inject constructor(private val postDao: PostDao): LocalRepository {
    override fun getPostsCache(): List<Post> {
        return postDao.getAll()
    }

    override fun savePosts(posts: List<Post>) {
        postDao.insertAll(posts)
    }

    override fun savePost(post: Post) {
        postDao.insert(post)
    }

    suspend override fun updatePost(post: Post) {
        postDao.update(post)
    }

    override fun deleteAll() {
        postDao.deleteAll()
    }
}