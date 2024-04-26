package com.dicoding.asclepius.interfaces

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.database.History
import com.dicoding.asclepius.helpers.ImageClassifierHelper
import com.dicoding.asclepius.viewmodels.HistoryViewModel
import com.dicoding.asclepius.viewmodels.HistoryViewModelFactory
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Calendar

class AnalyzeFragment : Fragment() {

    private lateinit var imageClassifierHelper: ImageClassifierHelper

    private var currentImageUri: Uri? = null
    private var result: String? = null
    private var conclusion: String? = null
    private var history: History? = null
    private var firstCategory: String? = null
    private var secondCategory: String? = null

    private lateinit var historyViewModel: HistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_analyze, container, false)
        val buttonAnalyze = view.findViewById<Button>(R.id.analyzeButton)
        val buttonGalery = view.findViewById<Button>(R.id.galleryButton)

        history = History()
        historyViewModel = obtainViewModel(requireActivity())

        buttonGalery.setOnClickListener {
            startGallery()
        }

        imageClassifierHelper = ImageClassifierHelper(
            context = requireActivity(),
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    showToast(error)
                }
                override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                    results?.let { it ->
                        val sortedCategories = it[0].categories.sortedByDescending { it?.score }
                        val displayResult =
                            sortedCategories.joinToString("\n") {
                                "${it.label} " + NumberFormat.getPercentInstance()
                                    .format(it.score).trim()
                            }
                        val cancerCategory = sortedCategories.firstOrNull { it.label == "Cancer" }
                        val nonCancerCategory = sortedCategories.firstOrNull { it.label.trim() == "Non Cancer" }



                        if (cancerCategory != null && nonCancerCategory != null) {
                            val cancerScore = cancerCategory.score
                            val nonCancerScore = nonCancerCategory.score
                            conclusion = if (cancerScore < nonCancerScore) {
                                "Foto tersebut bukan merupakan kanker. Untuk diagnosa lebih lanjut, silahkan hubungi dokter terdekat"
                            } else {
                                "Foto tersebut mungkin merupakan kanker. Untuk diagnosa lebih lanjut, silahkan hubungi dokter terdekat"
                            }
                        }

                        firstCategory = "${cancerCategory?.label} - ${NumberFormat.getPercentInstance()
                            .format(cancerCategory?.score).trim()}"
                        secondCategory = "${nonCancerCategory?.label} - ${NumberFormat.getPercentInstance()
                            .format(nonCancerCategory?.score).trim()}"

                        result = displayResult
                    }
                }
            }
        )

        buttonAnalyze.setOnClickListener {
            analyzeImage()
        }

        return view
    }

    private fun obtainViewModel(activity: FragmentActivity): HistoryViewModel {
        val factory = HistoryViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(HistoryViewModel::class.java)
    }

    private fun startGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        launcherIntentGallery.launch(intent)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            currentImageUri = selectedImg
            showImage()
            imageClassifierHelper.classifyStaticImage(currentImageUri)
        }
    }

    private fun showImage() {
        val imageView = view?.findViewById<ImageView>(R.id.previewImageView)
        currentImageUri.let {
            Log.d("Image URI", "showImage: $it")
            imageView?.setImageURI(it)
        }

        val buttonAnalyze = view?.findViewById<Button>(R.id.analyzeButton)
        buttonAnalyze?.visibility = View.VISIBLE
    }

    private fun analyzeImage() {
        val currentDateTime = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val formattedDateTime = dateFormat.format(currentDateTime)

        moveToResult(currentImageUri, result, conclusion)

        history.let {
            it?.first_category = firstCategory
            it?.second_category = secondCategory
            it?.date = formattedDateTime
        }

        historyViewModel.insert(history as History)

    }

    private fun moveToResult(image: Uri?, resultFinal: String?, conclusion: String?) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, ResultFragment.newInstance(image, resultFinal, conclusion))
            .addToBackStack(null)
            .commit()
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}