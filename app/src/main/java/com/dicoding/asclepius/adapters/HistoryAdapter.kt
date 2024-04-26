package com.dicoding.asclepius.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.database.History
import com.dicoding.asclepius.databinding.FragmentHomeBinding
import com.dicoding.asclepius.databinding.ItemHistoryBinding
import com.dicoding.asclepius.helpers.HistoryDiffCallback

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
    private val listHistories = ArrayList<History>()
    private lateinit var binding: ItemHistoryBinding

    fun setListHistories(listHistories: List<History>) {
        val diffCallback = HistoryDiffCallback(this.listHistories, listHistories)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listHistories.clear()
        this.listHistories.addAll(listHistories)
        diffResult.dispatchUpdatesTo(this)

        val allData = listHistories.joinToString(separator = "\n") { history ->
            "History: ${history.first_category}, ${history.second_category}, ${history.date}"
        }
        Log.d("HistoryAdapter", "All History Data:\n$allData")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history = listHistories[position]
        holder.bind(history)
    }

    override fun getItemCount(): Int {
        return listHistories.size
    }

    inner class HistoryViewHolder(binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(history: History) {
            with(binding) {
                itemFirstCategory.text = history.first_category
                itemSecondCategory.text = history.second_category
                itemDate.text = history.date
            }
        }
    }
}