package com.example.mad_project

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MedicineDetailsActivity : AppCompatActivity() {
    private lateinit var tvPackageName: TextView
    private lateinit var edDetails: EditText
    private lateinit var tvTotalCost: TextView
    private lateinit var btnBack: Button
    private lateinit var btnAddToCart: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicine_details)
        tvPackageName = findViewById(R.id.textView)
        edDetails = findViewById(R.id.editTextTextMultiline)
        edDetails.keyListener = null
        tvTotalCost = findViewById(R.id.TotalCost)
        btnBack = findViewById(R.id.btn_back_med_details)
        btnAddToCart = findViewById(R.id.btn_cart_med_details)
        tvPackageName.text = intent.getStringExtra("text1")
        edDetails.setText(intent.getStringExtra("text2"))
        tvTotalCost.text = "Total Cost: ${intent.getStringExtra("text3")}/-"

        btnBack.setOnClickListener {
            startActivity(Intent(this@MedicineDetailsActivity, BuyMedicineActivity::class.java))
        }

        btnAddToCart.setOnClickListener {
            val sharedpreferences:SharedPreferences =
                getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)
            val username = sharedpreferences.getString("username", "").toString()
            val product = tvPackageName.text.toString()
            val price = intent.getStringExtra("text3")?.toFloat() ?: 0f

            val db = Database(this)
            if (db.checkCart(username, product) == 1) {
                Toast.makeText(this, "Product Already Added", Toast.LENGTH_SHORT).show()
            } else {
                db.addCart(username, product, price,"medicine")
                Toast.makeText(this, "Record Inserted to Cart", Toast.LENGTH_SHORT).show()
            }
            startActivity(Intent(this@MedicineDetailsActivity, CartMedicineActivity::class.java))
        }
    }
}