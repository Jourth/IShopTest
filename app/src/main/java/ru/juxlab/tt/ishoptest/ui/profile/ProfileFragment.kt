package ru.juxlab.tt.ishoptest.ui.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import ru.juxlab.tt.ishoptest.IShopTestApplication
import ru.juxlab.tt.ishoptest.R
import ru.juxlab.tt.ishoptest.data.User
import ru.juxlab.tt.ishoptest.ui.MainActivity
import ru.juxlab.tt.ishoptest.ui.SignInActivity
import java.io.ByteArrayOutputStream

class ProfileFragment : Fragment() {
    companion object {
        const val PICK_IMAGE = 100
    }

    private var user: User? = null
    private lateinit var imageViewPhoto: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        user = (activity!!.application as IShopTestApplication).currentUser
        if (user == null){
            val sighInIntent = Intent(activity, SignInActivity::class.java)
            sighInIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(sighInIntent)

            activity!!.finish()
        }

        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        val textViewName = root.findViewById<TextView>(R.id.textView_profile_name)
        textViewName.text = user!!.name + " " + user!!.lastName

        imageViewPhoto = root.findViewById(R.id.imageView_profile_image)
        val ba = user!!.getUserPhoto(activity!!)
        if (!ba.isEmpty()) {
            val bt = BitmapFactory.decodeByteArray(ba, 0, ba.size)
            imageViewPhoto.setImageBitmap(bt)
        }

        val buttonChangePhoto = root.findViewById<TextView>(R.id.textView_change_photo)
        buttonChangePhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE)
        }

        val buttonLogOut = root.findViewById<TextView>(R.id.textView_log_out)
        buttonLogOut.setOnClickListener {
            val iShopTestApplication = activity!!.application as IShopTestApplication
            iShopTestApplication.currentUser = null

            val sighInIntent = Intent(activity, SignInActivity::class.java)
            sighInIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(sighInIntent)

            activity!!.finish()
        }
        return root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE){

            if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(activity, R.string.need_read_storage_permission, Toast.LENGTH_LONG).show()
            }
            else {
                val selectedImage = data?.data
                val bitmap = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, selectedImage)

                //val preview = Bitmap.createScaledBitmap(bitmap, 256, 256*bitmap.height/bitmap.width, false)
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val imageInByte = baos.toByteArray()
                user!!.updateUserPhoto(activity!!, imageInByte)

                imageViewPhoto.setImageBitmap(bitmap)
                (activity!! as MainActivity).showUserPhoto()

            }

        }
    }

}