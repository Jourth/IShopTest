package ru.juxlab.tt.ishoptest.ui

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.text.HtmlCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import ru.juxlab.tt.ishoptest.IShopTestApplication
import ru.juxlab.tt.ishoptest.R
import ru.juxlab.tt.ishoptest.data.User
import ru.juxlab.tt.ishoptest.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        user = (application as IShopTestApplication).currentUser
        if (user == null)
        {
            val sighInIntent = Intent(this, SignInActivity::class.java)
            sighInIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(sighInIntent)

            this.finish()
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navViewSide: NavigationView = binding.navViewSide
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val toolbarCaptionView: TextView = findViewById(R.id.textView_toolbar_caption)

        val toolbar: android.support.v7.widget.Toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        navController = findNavController(R.id.nav_host_fragment_activity_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_likes, R.id.navigation_cart, R.id.navigation_chat, R.id.navigation_profile
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navViewSide.setupWithNavController(navController)

        var toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val text = "<font color=#00000>Trade by</font> <font color=#4E55D7>bata</font>"
        toolbarCaptionView.text = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)

        showUserPhoto()


//        val bottomMenuView = navView.getChildAt(0) as BottomNavigationMenuView
//        val view = bottomMenuView.getChildAt(0)
//        val itemView = view as BottomNavigationItemView
//
//        val viewCustom = LayoutInflater.from(this).inflate(R.layout.nav_button, bottomMenuView, false)
//        itemView.addView(viewCustom)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        //menuInflater.inflate(R.menu.menu_main, menu)
        //menuInflater.inflate(R.menu.bottom_nav_menu, menu)
        // menu.getItem(0).
        return false
    }

    override fun onSupportNavigateUp(): Boolean {

        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun showUserPhoto() {
        user!!.load(this)
        val imageViewPhoto : ImageView = findViewById(R.id.imageView_profile_image)
        val ba = user!!.getUserPhoto(this)
        if (!ba.isEmpty()) {
            val bt = BitmapFactory.decodeByteArray(ba, 0, ba.size)
            imageViewPhoto.setImageBitmap(bt)
        }
    }
}