package com.example.testeapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.testeapp.databinding.ItemCommentBinding
import com.example.testeapp.model.Comments

class CommentAdapter : ListAdapter<Comments, CommentAdapter.CommentViewHolder>(CommentDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemCommentBinding.inflate(layoutInflater, parent, false)
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CommentViewHolder(private val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(comment: Comments) {
            binding.body.text = comment.body
        }
    }
}

class CommentDiffCallback : DiffUtil.ItemCallback<Comments>() {

    override fun areItemsTheSame(oldItem: Comments, newItem: Comments): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Comments, newItem: Comments): Boolean {
        return oldItem == newItem
    }
}