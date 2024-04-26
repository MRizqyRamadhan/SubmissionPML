package com.dicoding.asclepius.interfaces

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.dicoding.asclepius.R

@Suppress("DEPRECATION")
class ResultFragment : Fragment() {
    private var data: Uri? = null
    private var resultCancer: String? = null
    private var conclusion: String? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            data = it.getParcelable(EXTRA_IMAGE_URI)
            resultCancer = it.getString(EXTRA_RESULT)
            conclusion = it.getString(EXTRA_CONCLUSION)
        }
        val view = inflater.inflate(R.layout.fragment_result, container, false)
        val resultText = view?.findViewById<TextView>(R.id.result_text)
        val conclusionText = view?.findViewById<TextView>(R.id.conclusion_text)
        val imageView = view?.findViewById<ImageView>(R.id.result_image)
        resultText?.text = resultCancer
        conclusionText?.text = conclusion
        imageView?.setImageURI(data)

        return view
    }

    companion object {
        private const val EXTRA_IMAGE_URI = "extra_image_uri"
        private const val EXTRA_RESULT = "result"
        private const val EXTRA_CONCLUSION = "conclusion"

        fun newInstance(data: Uri?, result: String?, conclusion: String?): ResultFragment {
            val fragment = ResultFragment()
            val args = Bundle()
            args.putParcelable(EXTRA_IMAGE_URI, data)
            args.putString(EXTRA_RESULT, result)
            args.putString(EXTRA_CONCLUSION, conclusion)
            fragment.arguments = args
            return fragment
        }
    }
}