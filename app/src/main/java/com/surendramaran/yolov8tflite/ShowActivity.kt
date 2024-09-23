//package com.surendramaran.yolov8tflite
//
//import android.net.Uri
//import android.os.Bundle
//import android.widget.ImageView
//import androidx.appcompat.app.AppCompatActivity
//
//class ShowActivity : AppCompatActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_show)
//
//        val imageUri = intent.getStringExtra("imageUri")
//        val imageView: ImageView = findViewById(R.id.imageView)
//
//        imageUri?.let {
//            imageView.setImageURI(Uri.parse(it))
//        }
//    }
//}

package com.surendramaran.yolov8tflite

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException

class ShowActivity : AppCompatActivity(), Detector.DetectorListener {

    private lateinit var detector: Detector
    private lateinit var imageView: ImageView
    private lateinit var overlayView: OverlayView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show)

        imageView = findViewById(R.id.imageView)
        overlayView = findViewById(R.id.overlayView)  // Đảm bảo ID này tồn tại trong layout

        val imageUri = intent.getStringExtra("imageUri")
        val uri = Uri.parse(imageUri)
        val bitmap: Bitmap

        try {
            bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            imageView.setImageBitmap(bitmap)
        } catch (e: IOException) {
            e.printStackTrace()
            return
        }

        // Khởi tạo detector với các tham số cần thiết
        detector = Detector(baseContext, Constants.MODEL_PATH, Constants.LABELS_PATH, this)
        detector.setup()

        // Chạy nhận diện trên ảnh
        detector.detect(bitmap)
    }

    override fun onDetect(boundingBoxes: List<BoundingBox>, inferenceTime: Long) {
        // Hiển thị kết quả nhận diện trên OverlayView
        overlayView.setResults(boundingBoxes)
    }

    override fun onEmptyDetect() {
        // Xử lý khi không có đối tượng nào được nhận diện
        overlayView.setResults(emptyList())
    }
}
