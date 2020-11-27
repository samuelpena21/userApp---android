package com.sapp.userapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.sapp.userapp.api.models.User
import com.sapp.userapp.repository.UserRepository
import com.sapp.userapp.storage.SharedPrefHelper
import kotlinx.coroutines.flow.Flow
import java.util.*

class MainViewModel : ViewModel() {

    private val userRepository = UserRepository()
    var isLoading = userRepository.isLoading
    var isSearchBarClosed = MutableLiveData(false)
    var fetchedUserList = userRepository.fetchedUserList
    var favUserList = userRepository.favUserList
    var suggestedUserList = userRepository.suggestedUserList

    fun callApiWithPagination(page: Int, result: Int) {
        isLoading.postValue(true)
        userRepository.getUsersFromApi(page, result)
        isLoading.postValue(false)
    }

    fun getIsSearchBarClosed(): MutableLiveData<Boolean>{
        return isSearchBarClosed
    }

    fun getFetchedUsers(): MutableLiveData<List<User>> {
        return fetchedUserList
    }

    fun getFavUsers(): MutableLiveData<List<User>>{
        getFavDataFromShared()
        return favUserList
    }

    fun getSuggestedUsers(): MutableLiveData<List<User>>{
        return suggestedUserList
    }

    fun updateSuggestedText(newSearch: String?){
        val list = mutableListOf<User>()
        val fetchedUserList = fetchedUserList.value as MutableList<User>

        if(newSearch!!.isNotEmpty()){
            isLoading.value = true
            val search = newSearch.toLowerCase(Locale.getDefault())
            fetchedUserList.forEach{
                if(it.fullName.toLowerCase(Locale.getDefault()).contains(search) ||
                    it.email.toLowerCase(Locale.getDefault()).contains(search)){
                    list.add(it)
                }
            }
            suggestedUserList.value = list

        }else{
            suggestedUserList.value = fetchedUserList
        }
    }

    private fun getFavDataFromShared(){
        val result = SharedPrefHelper.favoriteUserList
        favUserList.postValue(result)
    }
}