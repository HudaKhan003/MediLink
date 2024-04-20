package com.example.mad_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.SimpleAdapter

class HealthArticlesActivity : AppCompatActivity() {

    private val healthData: Array<Array<String>> = arrayOf(
        arrayOf("Walking Daily","","", "Click More Details"),
        arrayOf("Home care of COVID-19","","", "Click More Details"),
        arrayOf("Stop Smoking","","", "Click More Details"),
        arrayOf("Menstrual Cramps","","", "Click More Details"),
        arrayOf("Healthy Gut","","", "Click More Details")
    )

    private val healthImages: IntArray = intArrayOf(
        R.drawable.health1,
        R.drawable.health2,
        R.drawable.health3,
        R.drawable.health4,
        R.drawable.health5
    )
    private lateinit var backButton: Button
    private lateinit var adapter: SimpleAdapter
    private lateinit var array: ArrayList<HashMap<String, String>>
    private lateinit var list: ListView

    private fun initializeArticles() {
        array = ArrayList()
        for (i in healthData.indices) {
            val item = HashMap<String, String>()
            item["line1"] = healthData[i][0]
            item["line2"] = healthData[i][1]
            item["line3"] = healthData[i][2]
            item["line4"] = healthData[i][3]
            array.add(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_health_articles)

        list =findViewById(R.id.Articles_list)
        backButton = findViewById(R.id.BackButton)
        backButton.setOnClickListener {
            startActivity(Intent(this@HealthArticlesActivity, HomeActivity::class.java))
        }
        initializeArticles()
        adapter = SimpleAdapter(
            this@HealthArticlesActivity, array, R.layout.multi_lines,
            arrayOf("line1", "line2","line3","line4"),
            intArrayOf(R.id.line_a, R.id.line_b,R.id.line_c,R.id.line_d)
        )
        list.adapter = adapter
        list.setOnItemClickListener { _, _, i, _ ->
            val intent = Intent(this@HealthArticlesActivity, HealthArticleDetailsActivity::class.java)
            intent.putExtra("text1", healthData[i][0])
            intent.putExtra("text2", healthImages[i])
            startActivity(intent)
        }
    }
}