package com.example.forusuistudy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.forusuistudy.data.Plan
import com.example.forusuistudy.databinding.ItemRcBinding

class ListRecyclerViewAdapter : ListAdapter<Plan, RecyclerView.ViewHolder>(LIstDiffCallback()) {

    inner class ListViewHolder(val binding: ItemRcBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Plan) {
            binding.item = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ListViewHolder(
            ItemRcBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ListViewHolder).bind(getItem(position))
    }

    private class LIstDiffCallback : DiffUtil.ItemCallback<Plan>() {
        override fun areItemsTheSame(oldItem: Plan, newItem: Plan): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Plan, newItem: Plan): Boolean {
            return oldItem == newItem
        }
    }

}