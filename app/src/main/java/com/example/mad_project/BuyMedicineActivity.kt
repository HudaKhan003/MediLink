package com.example.mad_project

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.OnClickAction
import android.widget.*


class BuyMedicineActivity : AppCompatActivity() {
    private lateinit var list: ListView
    private lateinit var backButton: Button
    private lateinit var array: ArrayList<HashMap<String, String>>
    private lateinit var adapter: SimpleAdapter


    private val medicines = arrayOf(
        arrayOf("Paracetamol","","","599"),
        arrayOf("Aspirin","","","850"),
        arrayOf("Ibuprofen","","","775"),
        arrayOf("Amoxicillin","","","1525"),
        arrayOf("Loratadine","","","1299"),
        arrayOf("Omeprazole","","","1050"),
        arrayOf("Cetirizine","","","675"),
        arrayOf("Atorvastatin","","","1899"),
        arrayOf("Hydrochlorothiazide","","","1425"),
        arrayOf("Metformin","","","949")
    )

    private val descriptions = arrayOf(
        "Relieves pain and reduces fever with minimal side effects\nAlleviate pain caused by headaches,toothaches and muscle aches\nLower body temperature\nManage discomfort associated with arthritis\nConsult a healthcare provider before using paracetamol concurrently with other medications to avoid potential drug interactions",
        "Acts as a pain reliever, anti-inflammatory, and helps prevent blood clots\nPrescribed for its antiplatelet effects, helping to reduce the risk of blood clot formation\n Anti-inflammatory properties make it suitable for conditions involving inflammation",
        "Alleviates pain, reduces inflammation, and lowers fever\nUsed to alleviate menstrual cramps and discomfort\nPrescribed for conditions like rheumatoid arthritis and osteoarthritis\n Long-term use or high doses may be associated with gastrointestinal side effects",
        "Antibiotic that treats various bacterial infections\nPrescribed for the treatment of bacterial infections caused by susceptible organisms\nEffective against bacterial infections of the skin, wounds, and soft tissues\nUsed to treat uncomplicated UTIs caused by susceptible bacteria",
        "Provides relief from allergies by blocking histamines\nUsed to alleviate symptoms of allergic rhinitis, commonly known as hay fever\nHelps reduce sneezing, runny or itchy nose, and itchy or watery eyes\nPrescribed for the treatment of chronic idiopathic urticaria\nUsed to relieve symptoms of allergic conjunctivitis",
        "Reduces stomach acid production, treating heartburn and acid reflux\n prescribed to treat GERD\nPromote the healing of peptic ulcers in the stomach or duodenum, and to prevent their recurrence\nTreat Zollinger-Ellison syndrome, a rare condition where the stomach produces too much acid",
        "Relieves allergy symptoms by blocking histamines\nTreat symptoms of allergic rhinitis, including sneezing, runny or stuffy nose, and itching\nEffective in managing the symptoms of chronic idiopathic urticaria (hives)\nUnlike some older antihistamines, cetirizine is known for causing less drowsiness, allowing individuals to remain alert while taking the medication",
        "Lowers cholesterol levels and reduces the risk of cardiovascular diseases\nPrescribed to lower elevated levels of total cholesterol, LDL cholesterol and triglycerides in the blood\nReduce the risk of cardiovascular events  such as heart attack and stroke, in individuals with existing cardiovascular conditions\nLowers stroke in at-risk individuals",
        "A diuretic that treats high blood pressure and edema\nTreat conditions involving fluid retention and high blood pressure\nReduce fluid retention (edema) often associated with conditions like heart failure and kidney dysfunction\nAssists in reducing edema and associated symptoms\nPrevents hypertension caused normally in adults for overage purposes",
        "Manages blood sugar levels and is commonly used to treat type 2 diabetes\nImproves the body's response to insulin and reducing the amount of glucose produced by the liver\nAddress insulin resistance associated with PCOS\nBy enhancing insulin sensitivity, metformin contributes to better glucose utilization and reduced insulin resistance"
    )

    private fun initializeMedicines() {
        array = ArrayList()
        for (i in medicines.indices) {
            val item = HashMap<String, String>()
            item["line1"] = medicines[i][0]
            item["line2"] = medicines[i][1]
            item["line3"] = medicines[i][2]
            item["line4"] = "Total Cost: ${medicines[i][3]}/-"
            array.add(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_medicine)
        list = findViewById(R.id.list_view)
        backButton=findViewById(R.id.back_btn)

        backButton.setOnClickListener{
            startActivity(Intent(this, HomeActivity::class.java))
        }
        initializeMedicines()
        adapter = SimpleAdapter(
            this@BuyMedicineActivity,
            array,
            R.layout.multi_lines,
            arrayOf("line1", "line2","line3","line4"),
            intArrayOf(R.id.line_a, R.id.line_b,R.id.line_c,R.id.line_d)
        )

        list.adapter = adapter
        list.setOnItemClickListener { _, _, i, _ ->
            val intent = Intent(this@BuyMedicineActivity, MedicineDetailsActivity::class.java)
            intent.putExtra("text1", medicines[i][0])
            intent.putExtra("text2", descriptions[i])
            intent.putExtra("text3", medicines[i][3])
            startActivity(intent)
        }
    }
}