package com.sapp.userapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sapp.userapp.api.models.User
import com.sapp.userapp.repository.UserRepository
import com.sapp.userapp.storage.SharedPrefHelper

class DetailViewModel : ViewModel() {

    var isFavorite = MutableLiveData<Boolean>(false)
    var userRepository = UserRepository()

    private fun setUserAsFavorite(user: User){
        userRepository.saveUser(user)
    }

    private fun deleteUserAsFavorite(user: User){
        userRepository.deleteUser(user)
    }

    fun removeOrAddToFavorite(user: User){
        if(isUserFavorite(user)){
            deleteUserAsFavorite(user)
            isFavorite.value = false

        }else{
            setUserAsFavorite(user)
            isFavorite.value = true
        }
    }

    fun isUserFavorite(user: User):Boolean{
        isFavorite.value = (SharedPrefHelper.getUserIfExist( user.fullName) != null)
        return isFavorite.value!!
    }
}