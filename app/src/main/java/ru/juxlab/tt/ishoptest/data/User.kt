package ru.juxlab.tt.ishoptest.data

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.text.TextUtils
import android.util.Patterns
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


data class User(var id: Int) {

    var name: String = ""
    var lastName: String = ""
    var email: String = ""
    private var imageId: Int = -1

    companion object{
        @SuppressLint("Range")
        fun findUser(context: Context, name: String, lastName: String, email: String) : User?{
            var db = LocalDB(context)
            val c = db.findUser(name)
            if (c.moveToFirst()){
                val id                = c.getInt(   c.getColumnIndex("id"))
                val user = User(id)
                user.load(context)
                return user
            }
            return null
        }
        @SuppressLint("Range")
        fun loginUser(context: Context, name: String, password: String): User?{
            val passwordHash = md5(password) ?: ""

            val db = LocalDB(context)
            val c = db.findUserPasswordHash(name, passwordHash)
            if (c.moveToFirst()){
                val id                = c.getInt(   c.getColumnIndex("user_id"))
                val user = User(id)
                user.load(context)
                return user
            }


            return null
        }
        fun createUser(context: Context, name: String, lastName: String, email: String, password: String): User?{

            try {
                val passwordHash = md5(password)

                var db = LocalDB(context)

                var cv = ContentValues()

                cv.put("name", name)
                cv.put("last_name", lastName)
                cv.put("email", email)
                cv.put("photo_image_id", -1)

                val userId = db.writeNewUser(cv).toInt()

                var cvp = ContentValues()
                cvp.put("login", name)
                cvp.put("password_hash", passwordHash)
                cvp.put("user_id", userId)

                db.writeNewUserPasswordHash(cvp)

                val user = User(userId)
                user.load(context)
                return user
            }
            catch (e:Exception){
                return null
            }

            return null
        }

        fun isValidEmail(target: String?): Boolean {
            return if (TextUtils.isEmpty(target)) {
                false
            } else {
                Patterns.EMAIL_ADDRESS.matcher(target).matches()
            }
        }
        fun isValidName(target: String?): Boolean {
            return !TextUtils.isEmpty(target)
        }

        private fun md5(s: String): String? {
            try {
                // Create MD5 Hash
                val digest: MessageDigest = MessageDigest.getInstance("MD5")
                digest.update(s.toByteArray())
                val messageDigest: ByteArray = digest.digest()

                // Create Hex String
                val hexString = StringBuffer()
                for (i in messageDigest.indices) hexString.append(
                    Integer.toHexString(
                        0xFF and messageDigest[i]
                            .toInt()
                    )
                )
                return hexString.toString()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }
            return ""
        }
    }

    @SuppressLint("Range")
    fun load(context: Context){
        var db = LocalDB(context)
        val c = db.getUserData(id)
        if (c.moveToFirst()){

            id                = c.getInt(   c.getColumnIndex("id"))

            name              = c.getString(c.getColumnIndex("name"))
            lastName          = c.getString(c.getColumnIndex("last_name"))
            email             = c.getString(c.getColumnIndex("email"))
            imageId           = c.getInt(   c.getColumnIndex("photo_image_id"))


        }
    }

    fun save(context: Context){
        var db = LocalDB(context)

        var cv = ContentValues()
        cv.put("id", this.id)
        cv.put("name", this.name)
        cv.put("last_name", this.lastName)
        cv.put("email", this.email)
        cv.put("photo_image_id", this.imageId)



        db.updateUser(cv, this.id)
    }
    fun updateUserPhoto(context: Context, bytes: ByteArray) : Int{

        var db = LocalDB(context)

        if (imageId >= 0) {
            var cvi = ContentValues()
            cvi.put("image", bytes)
            db.updateUserPhoto(cvi, imageId)
            return imageId
        } else {
            var cvi = ContentValues()
            cvi.put("image", bytes)
            imageId = db.writeNewUserPhoto(cvi).toInt()
            save(context)
            return imageId
        }

    }

    fun getUserPhoto(context: Context) : ByteArray {
        if (imageId < 0) return ByteArray(0)
        var db = LocalDB(context)
        return db.getUserPhoto(imageId)
    }



}