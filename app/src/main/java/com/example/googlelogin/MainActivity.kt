package com.example.googlelogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task


class MainActivity : AppCompatActivity() {
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001
    val A ="AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        println(A+"on create")

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("470320870749-l0ct9p1dsvgk1hms2r7vfa42b9r4guuq.apps.googleusercontent.com")
                .requestEmail()
                .build()

        println(A+"build")

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        val loginButon: Button=findViewById(R.id.googleloginbtn)
        loginButon.setOnClickListener {
            println(A+"pulsado boton sing in")
            signIn()
        }
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
        println(A+"sing in")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task =
                    GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
        println(A+"onActivityResult")
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {

            println(A+"handleSingInResult")
            val account = completedTask.getResult(
                    ApiException::class.java
            )
            println(A+"Account:"+account)
            // Signed in successfully
            val googleId = account?.id ?: ""
            Log.i("Google ID",googleId)
            println(A+"Google Id :"+googleId)

            val googleFirstName = account?.givenName ?: ""
            Log.i("Google First Name", googleFirstName)
            println(A+"Google First Name:"+ googleFirstName)

            val googleLastName = account?.familyName ?: ""
            Log.i("Google Last Name", googleLastName)
            println(A+"Google Last Name:"+ googleLastName)

            val googleEmail = account?.email ?: ""
            Log.i("Google Email", googleEmail)
            println(A+"Google Email:"+ googleEmail)

            val googleProfilePicURL = account?.photoUrl.toString()
            Log.i("Google Profile Pic URL", googleProfilePicURL)
            println(A+"Google Profile Pic URL:"+ googleProfilePicURL)

            val googleIdToken = account?.idToken ?: ""
            Log.i("Google ID Token", googleIdToken)
            println(A+"Google ID Token:"+ googleIdToken)

        } catch (e: ApiException) {
            // Sign in was unsuccessful
            Log.e("failed code=", e.statusCode.toString())
            println(A+"Sign in was unsuccessful "+e.toString())
        }
    }
}