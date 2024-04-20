package com.example.mad_project

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
import android.widget.Toast

class OrdersActivity : AppCompatActivity() {
    private lateinit var list: ListView
    private lateinit var backButton: Button
    private lateinit var detailsButton: Button
    private lateinit var array: ArrayList<HashMap<String, String>>
    private lateinit var adapter: SimpleAdapter
    private lateinit var title : TextView
    companion object {
        private const val EDIT_ORDER_REQUEST = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)
        list =findViewById(R.id.orders_list)
        backButton = findViewById(R.id.back_button)
        detailsButton = findViewById(R.id.details_button)
        title=findViewById(R.id.order_title)
        registerForContextMenu(list)
        val sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "").toString()

        backButton.setOnClickListener {
            startActivity(Intent(this@OrdersActivity, HomeActivity::class.java))
        }
        getOrderList()

        detailsButton.setOnClickListener {
            val intent = Intent(this@OrdersActivity, cart_details::class.java)
            startActivity(intent)
        }
        list.setOnItemLongClickListener { _, _, position, _ ->
            val selectedItem = array[position]
            val intent = Intent(this@OrdersActivity, MedicineBookActivity::class.java)
            intent.putExtra("isEditMode", true)
            intent.putExtra("username", username)
            intent.putExtra("fullName", selectedItem["line1"]?.substringAfter(":")?.trim())
            intent.putExtra("contact", selectedItem["line2"]?.substringAfter(":")?.trim())
            intent.putExtra("pincode", selectedItem["line3"]?.substringAfter(":")?.trim())
            intent.putExtra("address", selectedItem["line6"]?.substringAfter(":")?.trim())
            startActivity(intent)
            true
        }
    }

    private fun getOrderList() {
        val db = Database(this)
        val sharedPreferences: SharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)
        val username:String = sharedPreferences.getString("username","").toString()
        val ordersData = db.getOrderData(username)

        title.text = "${username}'s Order"
        array = ArrayList()
        for (orderData in ordersData) {
            val parts = orderData.split(" ")
            var fullname = parts[0]
            fullname += (" " + parts[1])
            val address = parts[2]
            val contact = parts[3]
            val pincode = parts[4]
            val date = parts[5]
            val amount = parts[6]
            val otype = parts[7]

            val itemMap = HashMap<String, String>()
            itemMap["line1"] = "Name:   ${fullname}"
            itemMap["line2"] = "Contact No:     ${contact}"
            itemMap["line3"] = "Pincode:    ${pincode}"
            itemMap["line4"] = "Delivery Date:  ${date}"
            itemMap["line5"] = "Total Amount:   ${amount}"
            itemMap["line6"] = "Address:  ${address}"
            array.add(itemMap)
        }

        adapter = SimpleAdapter(
            this@OrdersActivity, array, R.layout.order_details,
            arrayOf("line1", "line2", "line3", "line4","line5","line6"),
            intArrayOf(R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d,R.id.line_e,R.id.line_f )
        )
        list.adapter = adapter
    }
}
