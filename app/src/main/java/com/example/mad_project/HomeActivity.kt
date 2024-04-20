package com.example.mad_project

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.cardview.widget.CardView
import android.content.Intent

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val sharedPreferences: SharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)
        val username:String=sharedPreferences.getString("username","").toString()
        val exit:CardView=findViewById(R.id.cardLogout)
        exit.setOnClickListener {
            val editor = getSharedPreferences("pref_shared", Context.MODE_PRIVATE).edit()
            editor.clear()
            editor.apply()
            startActivity(Intent(this, LoginActivity::class.java))
        }
        val btnMedicine:CardView = findViewById(R.id.cardBuyMedicine)
        btnMedicine.setOnClickListener {
            startActivity(Intent(this, BuyMedicineActivity::class.java))
        }
        val viewOrder:CardView=findViewById(R.id.cardOrderDetails)
        viewOrder.setOnClickListener {
            val db = Database(this)
            val dbData = db.getOrderData(username)
            if (dbData.isEmpty()) {
                Toast.makeText(applicationContext, "No order against $username", Toast.LENGTH_SHORT).show()
            }
            else {
                val intent:Intent = Intent(this, OrdersActivity::class.java)
                intent.putExtra("isEditMode", true)
                startActivity(intent)
            }
        }
        val viewArticles:CardView=findViewById(R.id.cardHealthArticles)
        viewArticles.setOnClickListener {
            startActivity(Intent(this, HealthArticlesActivity::class.java))
        }
    }
}