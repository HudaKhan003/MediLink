package com.example.mad_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class HealthArticleDetailsActivity : AppCompatActivity() {
    private lateinit var Title :TextView
    private lateinit var Image :ImageView
    private lateinit var back_btn :Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_health_article_details)
        Title =findViewById(R.id.title)
        Image = findViewById(R.id.image)
        back_btn = findViewById(R.id.BackButton)
        back_btn.setOnClickListener {
            startActivity(Intent(this@HealthArticleDetailsActivity, HealthArticlesActivity::class.java))
        }
        Title.text =  intent.getStringExtra("text1")
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            val resId: Int? = bundle.getInt("text2")
            resId?.let {
                Image.setImageResource(it)
            }
        }
    }
}