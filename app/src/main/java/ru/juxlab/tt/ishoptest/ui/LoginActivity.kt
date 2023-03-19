package ru.juxlab.tt.ishoptest.ui

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.inputmethod.InputMethodManager
import android.widget.*
import ru.juxlab.tt.ishoptest.IShopTestApplication
import ru.juxlab.tt.ishoptest.R
import ru.juxlab.tt.ishoptest.data.User

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val textViewLogin     = findViewById<EditText>(R.id.textView_login)
        val textViewPassword  = findViewById<EditText>(R.id.textView_password)

        val checkBoxPasswordVisibility = findViewById<CheckBox>(R.id.checkBox_password_visibility)
        checkBoxPasswordVisibility.setOnClickListener{
            if (!checkBoxPasswordVisibility.isChecked) {
                textViewPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                textViewPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }

        }

        val buttonLogin = findViewById<Button>(R.id.buttonLogIn)
        buttonLogin.setOnClickListener{
            val login     = textViewLogin.text.toString()
            val password  = textViewPassword.text.toString()

            var user = User.loginUser(this, login, password)
            if (user == null) {
                Toast.makeText(this, R.string.login_failed_message, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val iShopTestApplication = application as IShopTestApplication
            iShopTestApplication.currentUser = user

            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
        }
    }

    override fun onResume() {
        super.onResume()
//        val enterLogin = findViewById<EditText>(R.id.enterLogin)
//        enterLogin.requestFocus()
//        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.showSoftInput(enterLogin, InputMethodManager.SHOW_FORCED);
    }
}