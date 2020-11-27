package com.sapp.userapp.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.ContactsContract
import android.transition.Explode
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sapp.userapp.R
import com.sapp.userapp.api.models.User
import com.sapp.userapp.viewmodel.DetailViewModel
import com.sapp.userapp.viewmodel.MainViewModel

class DetailActivity : AppCompatActivity() {

    lateinit var ivSetUserFavorite: ImageView
    lateinit var ivUserProfilePicture: ImageView
    lateinit var ivBackButton: ImageView
    lateinit var tvUserName: TextView
    lateinit var tvUserEmail: TextView
    lateinit var tvUserPhone: TextView
    lateinit var tvUserCell: TextView
    lateinit var fabSaveUser: FloatingActionButton


    private val detailModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val data: User = intent.getSerializableExtra("SELECTED-USER") as User

        setupViews(data)
        setupActionBar(supportActionBar)
        setupObservers()
        detailModel.isUserFavorite(data)
    }

    private fun setupActionBar(supportActionBar: ActionBar?) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupObservers() {
        detailModel.isFavorite.observe(this, {
            ivSetUserFavorite.setColorFilter(setColorToImageView(it))
        })
    }

    private fun setupViews(user: User) {
        ivUserProfilePicture = findViewById(R.id.ivUserProfilePicture)
        ivSetUserFavorite = findViewById(R.id.ivSetUserFavorite)
        ivBackButton = findViewById(R.id.ivBackButton)
        tvUserName = findViewById(R.id.tvUserNameValue)
        tvUserEmail = findViewById(R.id.tvUserEmailValue)
        tvUserPhone = findViewById(R.id.tvUserPhoneValue)
        tvUserCell = findViewById(R.id.tvUserCellValue)
        fabSaveUser = findViewById(R.id.fabSaveUser)

        tvUserName.text = user.fullName
        tvUserEmail.text = user.email
        tvUserPhone.text = user.phone
        tvUserCell.text = user.cell

        fabSaveUser.setOnClickListener { saveToPhone(user) }

        ivSetUserFavorite.setOnClickListener{ detailModel.removeOrAddToFavorite(user) }

        ivBackButton.setOnClickListener{ onBackPressed()}

        Glide.with(this).load(user.picture.large).placeholder(R.mipmap.ic_launcher_round)
            .into(ivUserProfilePicture)
    }

    private fun saveToPhone(user: User){
        val intent = Intent(Intent.ACTION_INSERT, ContactsContract.Contacts.CONTENT_URI).apply {
            type = ContactsContract.RawContacts.CONTENT_TYPE
            putExtra(ContactsContract.Intents.Insert.NAME,
                user.fullName)
            putExtra(ContactsContract.Intents.Insert.EMAIL,
                user.email)
            putExtra(ContactsContract.Intents.Insert.PHONE,
                user.phone)
            putExtra(ContactsContract.Intents.Insert.SECONDARY_PHONE,
                user.cell)
        }
        startActivity(intent)
    }

    private fun setColorToImageView(isFavorite: Boolean) : Int{
        if(isFavorite){
            return Color.YELLOW
        }
        return Color.TRANSPARENT
    }
}