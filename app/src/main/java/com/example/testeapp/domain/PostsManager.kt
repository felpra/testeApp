package com.example.testeapp.domain

import com.example.testeapp.data.repositores.LocalRepository
import com.example.testeapp.data.repositores.RemoteRepository
import com.example.testeapp.model.Result
import com.example.testeapp.model.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import com.example.testeapp.common.areListsEqual
import javax.inject.Inject

class PostsManager @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository
) {

    suspend fun getPosts(): Flow<Result<List<Post>>> {
        return flow {
            emit(Result.inProgress())
            val cache = localRepository.getPostsCache()
            emit(Result.success(cache.sortedWith(
                compareByDescending { it.isFavorite })))
            val remote = remoteRepository.fetchPostList()

            if (remote.status == Result.Status.SUCCESS) {
                remote.data?.let {
                    if(!cache.areListsEqual(it)){
                        val updatedWithFavorites = updateFavorites(cache, it)
                        localRepository.deleteAll()
                        localRepository.savePosts(updatedWithFavorites)
                        emit(Result.success(updatedWithFavorites.sortedWith(
                            compareByDescending { it.isFavorite })))
                    }
                }
            } else
                emit(remote)

        }.flowOn(Dispatchers.IO)
    }

    suspend fun updatePost(post: Post) {
            localRepository.updatePost(post.copy(isFavorite = true))
    }

    fun updateFavorites(postsA: List<Post>, postsB: List<Post>): List<Post> {
        return postsB.map { postB ->
            val postA = postsA.find { it.id == postB.id }
            if (postA != null) {
                postB.copy(isFavorite = postA.isFavorite)
            } else {
                postB
            }
        }
    }

}
