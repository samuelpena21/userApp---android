package com.sapp.userapp.ui

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.transition.Explode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.Window
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sapp.userapp.R
import com.sapp.userapp.api.models.User
import com.sapp.userapp.viewmodel.MainViewModel


class MainActivity : AppCompatActivity() {

    val model: MainViewModel by viewModels()

    private val gridLayoutManager = GridLayoutManager(this@MainActivity, 4, RecyclerView.VERTICAL, false)
    private val savedUserLayoutManager = GridLayoutManager(this@MainActivity, 1, RecyclerView.HORIZONTAL, false)
    private val userFetchAdapter = UserFetchAdapter(this@MainActivity, mutableListOf()) { user ->
        detailActivity(user)
    }
    private val savedUserAdapter = SavedUserAdapter(this@MainActivity, mutableListOf() ) { user ->
        detailActivity(user)
    }

    private val lastVisibleItemPosition: Int
        get() = gridLayoutManager.findLastVisibleItemPosition()
    private var page = 0

    private lateinit var rvFetchedUsers: RecyclerView
    private lateinit var rvSavedUsers: RecyclerView
    private lateinit var cvSavedUsers: CardView
    private lateinit var vSeparator: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        model.callApiWithPagination(page = page , result = 50)

        setupObservers()
        setupViews()
    }

    private fun setupViews() {
        rvFetchedUsers = findViewById(R.id.rvUserFetch)
        rvSavedUsers = findViewById(R.id.rvSavedUser)
        cvSavedUsers = findViewById(R.id.cvSavedUsers)
        vSeparator = findViewById(R.id.vSeparator)

        rvFetchedUsers.adapter = userFetchAdapter
        rvFetchedUsers.layoutManager = gridLayoutManager
        rvSavedUsers.adapter = savedUserAdapter
        rvSavedUsers.layoutManager = savedUserLayoutManager

        rvFetchedUsers.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount =  recyclerView.layoutManager!!.itemCount

                if (totalItemCount == lastVisibleItemPosition + 1 && !model.isLoading.value!!) {
                    model.callApiWithPagination(page++, 50)
                }
            }
        })
    }

    private fun setupObservers() {
        model.getFetchedUsers().observe(this, { userList ->
            userFetchAdapter.updateUserList(userList)
        })
        model.getFavUsers().observe(this, { userList ->
            if(userList.isNullOrEmpty()) isFavoriteUsersVisible(false) else isFavoriteUsersVisible(true)
            savedUserAdapter.updateUserList(userList)
        })
        model.getSuggestedUsers().observe(this, { userList ->
            userFetchAdapter.updateAndClearUserList(userList)
            rvFetchedUsers.adapter!!.notifyDataSetChanged()
        })
        model.getIsSearchBarClosed().observe(this, {
            isFavoriteUsersVisible(it)
        })
    }

    private fun isFavoriteUsersVisible(visibility: Boolean){
        if(visibility){
            cvSavedUsers.visibility = VISIBLE
            vSeparator.visibility = VISIBLE
        }else{
            cvSavedUsers.visibility = GONE
            vSeparator.visibility = GONE
        }

    }

    private fun detailActivity( user: User){
        val intent = Intent(this@MainActivity, DetailActivity::class.java)
        intent.putExtra("SELECTED-USER", user)
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
    }

    override fun onResume() {
        super.onResume()
        model.getFavUsers()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        setupInfiniteScroll(menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun setupInfiniteScroll(menu: Menu?) {
        menuInflater.inflate(R.menu.main_menu, menu)
        val menuItem = menu!!.findItem(R.id.action_search)

        if(menuItem != null){
            val searchView = menuItem.actionView as SearchView
            val editText = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
            editText?.hint = "Search...."

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    model.updateSuggestedText(newText)
                    return true
                }
            })

            menuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener{
                override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                    model.isSearchBarClosed.value = false
                    return true
                }

                override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                    model.isLoading.value = false
                    model.isSearchBarClosed.value = true
                    return true
                }
            })
        }
    }

}