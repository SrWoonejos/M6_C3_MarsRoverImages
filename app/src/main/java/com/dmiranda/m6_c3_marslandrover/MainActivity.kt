package com.dmiranda.m6_c3_marslandrover

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import java.net.URL
import kotlin.coroutines.CoroutineContext
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.dmiranda.m6_c3_marslandrover.databinding.ActivityMainBinding
import kotlinx.coroutines.launch


class MainActivity() : AppCompatActivity(), CoroutineScope {

    private val job = Job()
    private lateinit var binding: ActivityMainBinding
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

    private val urlImageMarsRover: Array<String> = arrayOf(
        "http://mars.jpl.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/01000/opgs/edr/fcam/FLB_486265257EDR_F0481570FHAZ00323M_.JPG",
        "http://mars.jpl.nasa.gov/msl-raw-images/msss/01000/mcam/1000MR0044631300503690E01_DXXX.jpg",
        "http://mars.jpl.nasa.gov/msl-raw-images/msss/01000/mcam/1000ML0044631270305224E03_DXXX.jpg",
        "http://mars.jpl.nasa.gov/msl-raw-images/msss/01000/mcam/1000ML0044631230305220E02_DXXX.jpg"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonThreadUi.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Mensaje en Thread UI")
            builder.setMessage("Alerta para mostrar el funcionamiento de corrutinas en background")
            builder.setCancelable(true)
            builder.setPositiveButton("OK") {
                    dialog, _ ->
                dialog.cancel()
            }
            builder.show()
        }
    }

    override fun onStart() {
        super.onStart()
        downloadImages()
    }


        private fun downloadImages(){
            launch {
                binding.progressBar1.visibility = View.VISIBLE

                for(i in urlImageMarsRover.indices){
                    val bitmap: Bitmap? = withContext(Dispatchers.IO) {
                        downloadImagesNasaRover(urlImageMarsRover[i])
                    }

                    if(bitmap != null){
                        when(i) {
                            0 -> {
                                binding.imageView1.setImageBitmap(bitmap)
                                binding.progressBar1.visibility = View.GONE
                                binding.progressBar2.visibility = View.VISIBLE
                            }
                            1 -> {
                                binding.imageView2.setImageBitmap(bitmap)
                                binding.progressBar2.visibility = View.GONE
                                binding.progressBar3.visibility = View.VISIBLE
                            }
                            2 -> {
                                binding.imageView3.setImageBitmap(bitmap)
                                binding.progressBar3.visibility = View.GONE
                                binding.progressBar4.visibility = View.VISIBLE
                            }
                            3 -> {
                                binding.imageView4.setImageBitmap(bitmap)
                                binding.progressBar4.visibility = View.GONE
                            }
                        }
                    }
                }
            }
        }
    }
    private fun downloadImagesNasaRover(url:String): Bitmap?{
        val bitmap: Bitmap?
        try {
            val inputStream = URL(url).openStream()
            bitmap = BitmapFactory.decodeStream(inputStream)
            return bitmap
        }
        catch (e: Exception){
            e.printStackTrace()
        }
        return null
    }
