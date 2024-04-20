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

class cart_details : AppCompatActivity() {
    private lateinit var list: ListView
    private lateinit var array: ArrayList<HashMap<String, String>>
    private lateinit var adapter: SimpleAdapter
    private lateinit var cost: TextView
    private lateinit var Username: TextView
    private lateinit var back:Button

    companion object {
        private const val DELETE_CART_ITEM_REQUEST = 1002
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_details)

        list = findViewById(R.id.listView)
        cost = findViewById(R.id.CartTotalCost)
        Username = findViewById(R.id.title)
        back = findViewById(R.id.back_button)

        val sharedPreferences: SharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)
        val username:String=sharedPreferences.getString("username","").toString()
        Username.text = "${username}'s Cart"

        val db = Database(this)
        var totalAmount = 0.0F
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
            this@cart_details, array, R.layout.multi_lines,
            arrayOf("line1", "line2", "line3", "line4"),
            intArrayOf(R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d)
        )
        list.adapter = adapter

        list.setOnItemClickListener { _, _, _, _ ->
            openContextMenu(list)
        }
        list.setOnItemLongClickListener { _, _, position, _ ->
            openContextMenu(list)
            true
        }

        registerForContextMenu(list)

        back.setOnClickListener {
            startActivity(Intent(this@cart_details, HomeActivity::class.java))
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        when (item.itemId) {
            R.id.action_delete -> {
                val deletedItem = array.removeAt(info.position)
                val updatedCost = updateTotalCost()
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "Deleted: ${deletedItem["line1"]}", Toast.LENGTH_SHORT).show()

                val db = Database(this)
                val sharedPreferences: SharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)
                val username: String = sharedPreferences.getString("username", "").toString()
                db.removeCartItem(username, "medicine", deletedItem["line1"].toString())
                db.updatePrice(username,updatedCost)
                return true
            }
            else -> return super.onContextItemSelected(item)
        }
    }

    private fun updateTotalCost() :Float{
        var totalAmount = 0.0F
        for (item in array) {
            val price = item["line4"]?.substringAfterLast(" ")?.replace("/-", "")?.toFloatOrNull() ?: 0.0F
            totalAmount += price
        }
        cost.text = "Total Cost: ${totalAmount}/-"
        return totalAmount
    }
}
