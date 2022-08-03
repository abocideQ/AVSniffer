package com.media.sniffer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import tv.av.sniffer.sniff.AVSniffer

class MainActivity : AppCompatActivity() {
    private val flv =
        "https://sf1-hscdn-tos.pstatp.com/obj/media-fe/xgplayer_doc_video/flv/xgplayer-demo-720p.flv"
    private val m3u8 = "http://devimages.apple.com/iphone/samples/bipbop/bipbopall.m3u8"
    private val mp4 = "https://www.w3schools.com/html/movie.mp4"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btFlv = findViewById<Button>(R.id.bt_check_flv)
        val btM3u8 = findViewById<Button>(R.id.bt_check_m3u8)
        val btMp4 = findViewById<Button>(R.id.bt_check_mp4)
        btFlv.setOnClickListener {
            AVSniffer(flv) {
                runOnUiThread{
                    if (it == null) {
                        Toast.makeText(this, "不支持", Toast.LENGTH_SHORT).show()
                        return@runOnUiThread
                    }
                    Toast.makeText(this, "支持$it", Toast.LENGTH_SHORT).show()
                    Log.e("sniffer", "$it")
                }
            }.sniff()
        }
        btM3u8.setOnClickListener {
            AVSniffer(m3u8) {
                runOnUiThread{
                    if (it == null) {
                        Toast.makeText(this, "不支持", Toast.LENGTH_SHORT).show()
                        return@runOnUiThread
                    }
                    Toast.makeText(this, "支持$it", Toast.LENGTH_SHORT).show()
                    Log.e("sniffer", "$it")
                }
            }.sniff()
        }
        btMp4.setOnClickListener {
            AVSniffer(mp4) {
                runOnUiThread{
                    if (it == null) {
                        Toast.makeText(this, "不支持", Toast.LENGTH_SHORT).show()
                        return@runOnUiThread
                    }
                    Toast.makeText(this, "支持$it", Toast.LENGTH_SHORT).show()
                    Log.e("sniffer", "$it")
                }
            }.sniff()
        }
    }
}