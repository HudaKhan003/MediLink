package com.example.mad_project

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MedicineBookActivity : AppCompatActivity() {
    private lateinit var edname: EditText
    private lateinit var edaddress: EditText
    private lateinit var edcontact: EditText
    private lateinit var edpincode: EditText
    private lateinit var btnBooking: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicine_book)

        edname = findViewById(R.id.fullName)
        edaddress = findViewById(R.id.address)
        edcontact = findViewById(R.id.ContactNumber)
        edpincode = findViewById(R.id.pin)
        btnBooking = findViewById(R.id.placeOrder)

        val intent = intent
        val price = intent.getStringExtra("price")
        val date = intent.getStringExtra("date")

        val isEditMode = intent.getBooleanExtra("isEditMode",true)

        if (isEditMode) {
            edname.setText(intent.getStringExtra("fullName"))
            edaddress.setText(intent.getStringExtra("address"))
            edcontact.setText(intent.getStringExtra("contact"))
            edpincode.setText(intent.getStringExtra("pincode"))
        }

        btnBooking.setOnClickListener {
            val sharedPreferences: SharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)
            val username: String = sharedPreferences.getString("username", "").toString()
            val db = Database(this)
            if (isEditMode) {
                    val updated = db.updateOrder(
                        username,
                        edname.text.toString(),
                        edaddress.text.toString(),
                        edcontact.text.toString(),
                        edpincode.text.toString().toInt(),
                        "Medicine"
                    )

                    if (updated) {
                        Toast.makeText(
                            applicationContext,
                            "Order details updated successfully",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Order details not updated",
                            Toast.LENGTH_LONG
                        ).show()
                    }
            }
            else {
                if (!price.isNullOrBlank()) {
                    val inserted = db.addOrder(
                        username,
                        edname.text.toString(),
                        edaddress.text.toString(),
                        edcontact.text.toString(),
                        edpincode.text.toString().toInt(),
                        date.toString(),
                        price.toFloat(),
                        "Medicine"
                    )
                    db.removeCart(username, "Medicine")
                    if(inserted)
                        Toast.makeText(applicationContext, "Your booking is done successfully", Toast.LENGTH_LONG).show()
                }
            }
            val homeIntent = Intent(this@MedicineBookActivity, HomeActivity::class.java)
            startActivity(homeIntent)

            finish()
        }
    }
}
