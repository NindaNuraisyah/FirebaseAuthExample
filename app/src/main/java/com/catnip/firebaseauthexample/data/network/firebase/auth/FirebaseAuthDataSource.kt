package com.catnip.firebaseauthexample.data.network.firebase.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

interface FirebaseAuthDataSource {
    fun isLoggedIn(): Boolean
    fun getCurrentUser(): FirebaseUser?
    fun doLogout(): Boolean
    suspend fun doRegister(fullName: String, email: String, password: String): Boolean
    suspend fun doLogin(email: String, password: String) : Boolean
}

class  FirebaseAuntDataSourceImpl(private val firebaseAuth: FirebaseAuth) : FirebaseAuthDataSource {
    override fun isLoggedIn(): Boolean {
        //currentUser == User yang sedang login pada saat tersebut
        //current user = null, not login
        return firebaseAuth.currentUser != null
    }

    override fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    override fun doLogout(): Boolean {
        Firebase.auth.signOut()
        return true
    }

    override suspend fun doRegister(fullName: String, email: String, password: String): Boolean {
        val registerResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        registerResult.user?.updateProfile(
            userProfileChangeRequest {
                displayName = fullName
//                photoUri = "https://unair.ac.id/wp-content/uploads/2022/08/kucing.jpg".toUri()
            }
        )?.await()
        return registerResult.user != null
    }

    override suspend fun doLogin(email: String, password: String): Boolean {
        val loginResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        return loginResult.user != null
    }

}