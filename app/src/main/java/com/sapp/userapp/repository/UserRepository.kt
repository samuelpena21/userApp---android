package com.sapp.userapp.repository

import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import com.sapp.userapp.api.ApiClient
import com.sapp.userapp.api.ServiceGenerator
import com.sapp.userapp.api.models.User
import com.sapp.userapp.api.response.UserResponse
import com.sapp.userapp.storage.SharedPrefHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class UserRepository () {

    private val service = ServiceGenerator.createService(ApiClient::class.java)

    var isLoading = MutableLiveData<Boolean>(false)
    var fetchedUserList: MutableLiveData<List<User>> = MutableLiveData()
    var favUserList: MutableLiveData<List<User>> = MutableLiveData()
    var suggestedUserList: MutableLiveData<List<User>> = MutableLiveData()

    fun getUsersFromApi(page: Int, result: Int) {
        isLoading.value = true

        service.getUserListPaginated(page, result, "")?.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.code() == 200) {
                    val results = response.body()?.results!!
                    fetchedUserList.postValue(results)
                    isLoading.postValue(false)
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                isLoading.postValue(false)
            }
        })
    }

    fun saveUser(user: User){
        if(!isUserFavorite(user)){
            SharedPrefHelper.saveUser( user)
        }
    }

    fun deleteUser(user: User){
        if(isUserFavorite(user)) {
            SharedPrefHelper.deleteUserIfExist(user.fullName)
        }
    }

    private fun isUserFavorite(user: User):Boolean{
        return (SharedPrefHelper.getUserIfExist( user.fullName) != null)
    }
}