package com.gateoftruth.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.gateoftruth.sample.databinding.ActivityGlideTestBinding

class GlideTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding=ActivityGlideTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val url = "picture url"
        val url2 = "picture url"
        val options = RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)
        //binding.glideProgressImageView.into(url, GlideApp.with(this).load(url).apply(options))
        //binding.glideProgressImageView2.into(url2, GlideApp.with(this).load(url2).apply(options))

    }
}
