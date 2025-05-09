package com.nepreconsultintg.edigital

import com.nepreconsultintg.edigital.models.User

object storage {
    private var user : User? = null
    private var token : String? = null

    fun setUser(mUser: User?, mToken: String){
        user = mUser
        token = mToken
    }
}
