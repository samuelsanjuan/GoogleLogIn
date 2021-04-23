package com.example.googlelogin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient

class MainActivity : AppCompatActivity() {

    lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val signOutButon: Button=findViewById(R.id.googlesignoutbtn)
        val loginButon: Button=findViewById(R.id.googleloginbtn)

        loginButon.setOnClickListener {signIn()}
        signOutButon.setOnClickListener {signOut()}

        ViewModel.textoToast.observe(this, Observer { textoToast-> val mensaje=textoToast
            Toast.makeText(applicationContext,mensaje,Toast.LENGTH_SHORT).show()})

        mGoogleSignInClient = GoogleSignIn.getClient(this, ViewModel.gso)
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun signOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this) {}
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task =GoogleSignIn.getSignedInAccountFromIntent(data)
            ViewModel.handleSignInResult(task,mGoogleSignInClient)
        }
    }
}