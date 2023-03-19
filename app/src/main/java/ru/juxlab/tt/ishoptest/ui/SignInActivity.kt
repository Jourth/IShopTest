package ru.juxlab.tt.ishoptest.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import ru.juxlab.tt.ishoptest.IShopTestApplication
import ru.juxlab.tt.ishoptest.R
import ru.juxlab.tt.ishoptest.data.User

class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val textViewName =     findViewById<TextView>(R.id.textView_first_name)
        val textViewLastName = findViewById<TextView>(R.id.textView_last_name)
        val textViewEMail =    findViewById<TextView>(R.id.textView_email)

        val buttonLogIn = findViewById<TextView>(R.id.linkLogIn)
        buttonLogIn.setOnClickListener{
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }

        val buttonSignIn = findViewById<TextView>(R.id.buttonSignIn)
        buttonSignIn.setOnClickListener{

            val name     = textViewName.text.toString()
            val lastName = textViewLastName.text.toString()
            val email     = textViewEMail.text.toString()

            if (!User.isValidName(name)) {
                Toast.makeText(this, R.string.name_is_empty_message, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (!User.isValidName(lastName)) {
                Toast.makeText(this, R.string.name_is_empty_message, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (!User.isValidEmail(email)) {
                Toast.makeText(this, R.string.not_valid_email_message, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            var user = User.findUser(this, name, lastName, email)
            if (user != null) {
                Toast.makeText(this, R.string.user_is_registered_message, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            user = User.createUser(this, name, lastName, email, "12345")
            if (user == null) {
                Toast.makeText(this, R.string.cannot_create_user_message, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val iShopTestApplication = application as IShopTestApplication
            iShopTestApplication.currentUser = user

            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
        }
    }
}