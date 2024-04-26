package com.dicoding.asclepius.interfaces

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.adapters.NewsAdapter
import com.dicoding.asclepius.data.reponse.ArticlesItem
import com.dicoding.asclepius.data.reponse.NewsResponse
import com.dicoding.asclepius.databinding.FragmentNewsBinding
import com.dicoding.asclepius.viewmodels.NewsViewModel

class NewsFragment : Fragment() {

    private val newsViewModel by viewModels<NewsViewModel>()
    private lateinit var binding: FragmentNewsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoading(true)

        newsViewModel.news.observe(viewLifecycleOwner) {
            setupRecyclerView(it)
            showLoading(false)
        }
    }

    private fun showLoading(status: Boolean) {
        if (status) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setupRecyclerView(newsResponse: NewsResponse?) {
        binding.rvNews.layoutManager = LinearLayoutManager(requireContext())

        // Set up the adapter
        val adapter = NewsAdapter()
        binding.rvNews.adapter = adapter

        // Submit the list to the adapter
        newsResponse?.articles?.let {
            adapter.submitList(it)
        }
    }


}