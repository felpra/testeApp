package com.example.testeapp.data

import androidx.room.*
import com.example.testeapp.model.Post

@Dao
interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: Post)

    @Query("SELECT * FROM posts order by id asc")
    fun getAll(): List<Post>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(posts: Collection<Post>)

    @Query("DELETE FROM posts")
    fun deleteAll()

    @Update
    suspend fun update(post: Post)
}