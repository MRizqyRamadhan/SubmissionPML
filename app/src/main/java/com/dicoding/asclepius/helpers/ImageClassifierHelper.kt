package com.dicoding.asclepius.helpers

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import android.util.Log
import androidx.camera.core.ImageProxy
import com.google.android.gms.tflite.client.TfLiteInitializationOptions
import com.google.android.gms.tflite.gpu.support.TfLiteGpu
import com.dicoding.asclepius.R
import org.tensorflow.lite.DataType
import org.tensorflow.lite.gpu.CompatibilityList
import org.tensorflow.lite.support.common.ops.CastOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.core.vision.ImageProcessingOptions
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier

@Suppress("DEPRECATION")
class ImageClassifierHelper(
    val modelName: String = "cancer_classification.tflite",
    val context: Context,
    val classifierListener: ClassifierListener?
) {

    private var imageClassifier: ImageClassifier? = null
    private var isImageClassified = false

    init {
        setupImageClassifier()
    }

    private fun setupImageClassifier() {
        val optionsBuilder = ImageClassifier.ImageClassifierOptions.builder()
        val baseOptionsBuilder = BaseOptions.builder()

        optionsBuilder.setBaseOptions(baseOptionsBuilder.build())

        try {
            imageClassifier = ImageClassifier.createFromFileAndOptions(
                context,
                modelName,
                optionsBuilder.build()
            )
        } catch (e: IllegalStateException) {
            classifierListener?.onError(context.getString(R.string.image_classifier_failed))
            Log.e(TAG, e.message.toString())
        }
    }

    @SuppressLint("Recycle")
    fun classifyStaticImage(imageUri: Uri?) {
        if (!isImageClassified) {
            isImageClassified = true

            if (imageClassifier == null) {
                setupImageClassifier()
            }

            val imageProcessor = ImageProcessor.Builder()
                .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
                .add(CastOp(DataType.UINT8))
                .build()

            val inputStream = context.contentResolver.openInputStream(imageUri!!)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val tensorImage = imageProcessor.process(TensorImage.fromBitmap(bitmap))

            val imageProcessingOptions = ImageProcessingOptions.builder()
                .build()

            var inferenceTime = SystemClock.uptimeMillis()
            val results = imageClassifier?.classify(tensorImage, imageProcessingOptions)
            inferenceTime = SystemClock.uptimeMillis() - inferenceTime
            classifierListener?.onResults(
                results,
                inferenceTime
            )
        }
    }

    interface ClassifierListener {
        fun onError(error: String)
        fun onResults(
            results: List<Classifications>?,
            inferenceTime: Long)
    }

    companion object {
        private const val TAG = "ImageClassificationHelper"
    }
}
