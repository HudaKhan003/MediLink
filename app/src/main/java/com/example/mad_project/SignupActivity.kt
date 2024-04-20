package com.example.mad_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.content.Intent
import android.util.Patterns
import android.widget.Toast

class SignupActivity : AppCompatActivity()
{
    private lateinit var edUsername: EditText
    private lateinit var edPassword: EditText
    private lateinit var edEmail: EditText
    private lateinit var edConfirm: EditText
    private lateinit var btn: Button
    private lateinit var tv: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        edUsername = findViewById(R.id.editTextRegUsername)
        edPassword = findViewById(R.id.editTextRegPassword)
        edEmail = findViewById(R.id.editTextRegEmail)
        edConfirm = findViewById(R.id.editTextRegConfirmPassword)
        btn = findViewById(R.id.buttonRegister)
        tv = findViewById(R.id.textViewExistingUser)


        tv.setOnClickListener {
            val intent = Intent(this@SignupActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        btn.setOnClickListener {
            val username = edUsername.text.toString()
            val email = edEmail.text.toString()
            val password = edPassword.text.toString()
            val confirm = edConfirm.text.toString()
            val db = Database(applicationContext)

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                Toast.makeText(applicationContext, "Please fill all details", Toast.LENGTH_SHORT)
                    .show()
            } else if (!isValidEmail(email)) {
                Toast.makeText(applicationContext, "Invalid Email", Toast.LENGTH_SHORT).show()
            } else if (password != confirm) {
                Toast.makeText(
                    applicationContext,
                    "Password and Confirm password didn't match",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (!isValid(password)) {
                Toast.makeText(
                    applicationContext,
                    "Password must be at least 8 characters long and contain letters, digits, and special characters",
                    Toast.LENGTH_SHORT
                ).show()
            }  else {

                db.register(username, email, password)
                Toast.makeText(applicationContext, "Registered Successfully!", Toast.LENGTH_SHORT)
                    .show()
                val intent = Intent(this@SignupActivity, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValid(password: String): Boolean {
        var hasLetter = false
        var hasDigit = false
        var hasSpecialChar = false

        if (password.length < 8) {
            return false
        } else {
            for (p in password.indices) {
                if (Character.isLetter(password[p])) {
                    hasLetter = true
                } else if (Character.isDigit(password[p])) {
                    hasDigit = true
                } else if (password[p] in "!@#$%^&*()_-+=<>?/{}[].,;:'\"\\|") {
                    hasSpecialChar = true
                }
            }
        }
        return hasLetter && hasDigit && hasSpecialChar
    }
}
