package com.dicoding.asclepius.interfaces

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.adapters.HistoryAdapter
import com.dicoding.asclepius.databinding.FragmentHomeBinding
import com.dicoding.asclepius.viewmodels.HistoryViewModel
import com.dicoding.asclepius.viewmodels.HistoryViewModelFactory

class HomeFragment : Fragment() {

    private lateinit var adapter: HistoryAdapter
    private lateinit var binding: FragmentHomeBinding
    private var name: String? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        arguments?.let {
            name = it.getString(EXTRA_NAME)
        }

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.tvWelcome.text = "Selamat Datang, $name"

        val mainViewModel = obtainViewModel(requireActivity())

        mainViewModel.getAllHistories().observe(viewLifecycleOwner) { historyList ->
            if (historyList != null && historyList.isNotEmpty()) {
                adapter.setListHistories(historyList)
                binding.tvEmpty.visibility = View.GONE
            } else {
                binding.tvEmpty.visibility = View.VISIBLE
            }
        }

        adapter = HistoryAdapter()
        binding.rvHistory.layoutManager = LinearLayoutManager(context)
        binding.rvHistory.setHasFixedSize(true)
        binding.rvHistory.adapter = adapter

        return binding.root
    }


    private fun obtainViewModel(activity: FragmentActivity): HistoryViewModel {
        val factory = HistoryViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(HistoryViewModel::class.java)
    }

    companion object {
        private const val EXTRA_NAME = "extra_name"

        fun newInstance(name: String?): HomeFragment {
            val fragment = HomeFragment()
            val args = Bundle()
            args.putString(EXTRA_NAME, name)
            fragment.arguments = args
            return fragment
        }
    }

}