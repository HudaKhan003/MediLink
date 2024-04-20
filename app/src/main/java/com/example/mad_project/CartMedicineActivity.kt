package com.example.mad_project

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
import android.widget.Toast
import java.util.Calendar

class CartMedicineActivity : AppCompatActivity() {
    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var dateBtn: Button
    private lateinit var list: ListView
    private lateinit var moreButton: Button
    private lateinit var checkoutButton: Button
    private lateinit var array: ArrayList<HashMap<String, String>>
    private lateinit var adapter: SimpleAdapter
    private lateinit var cost: TextView
    private lateinit var Username: TextView

    private fun initDatePicker() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val formattedDate = "${dayOfMonth}/${month + 1}/${year}"
            dateBtn.text = formattedDate
        }
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val style = AlertDialog.THEME_HOLO_DARK

        datePickerDialog = DatePickerDialog(this, style, dateSetListener, year, month, day)
        datePickerDialog.datePicker.minDate = cal.timeInMillis + 86400000
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_medicine)

        list = findViewById(R.id.listView)
        moreButton = findViewById(R.id.back_more)
        dateBtn = findViewById(R.id.date)
        checkoutButton = findViewById(R.id.buttonCheckout)
        cost = findViewById(R.id.textViewCartTotalCost)
        Username = findViewById(R.id.user)

        initDatePicker()
        dateBtn.setOnClickListener {
            datePickerDialog.show()
        }

        moreButton.setOnClickListener {
            startActivity(Intent(this, BuyMedicineActivity::class.java))
        }

        val db = Database(this)
        var totalAmount = 0.0F
        val sharedPreferences: SharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)
        val username:String=sharedPreferences.getString("username","").toString()
        Username.text = username
        val dbData = db.getCartData(username, "medicine")
        array = ArrayList()
        for ((product, price) in dbData) {
            val itemMap = HashMap<String, String>()
            itemMap["line1"] = product
            itemMap["line2"] = ""
            itemMap["line3"] = ""
            itemMap["line4"] = "Cost : $price/-"
            totalAmount += price
            array.add(itemMap)
        }

        cost.text = "Total Cost: ${totalAmount}/-"
        adapter = SimpleAdapter(
            this@CartMedicineActivity,
            array,
            R.layout.multi_lines,
            arrayOf("line1", "line2", "line3", "line4"),
            intArrayOf(R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d)
        )
        list.adapter = adapter

        checkoutButton.setOnClickListener {
            val intent = Intent(this@CartMedicineActivity, MedicineBookActivity::class.java)
            intent.putExtra("price", totalAmount.toString())
            intent.putExtra("date", dateBtn.text.toString())
            intent.putExtra("isEditMode", false)
            startActivity(intent)
        }
    }
}
