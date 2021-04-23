package com.example.googlelogin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
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

        val textView:TextView=findViewById(R.id.textView)
        val textGmail:TextView=findViewById(R.id.textViewGmail)
        val signOutButon: Button=findViewById(R.id.googlesignoutbtn)
        val signInButon: Button=findViewById(R.id.googleloginbtn)

        findViewById<Button>(R.id.mainPageButton).setOnClickListener{

        val intent = Intent(this,PaginaPrincipal::class.java)
            startActivity(intent)

        }

        ViewModel.textViewGmail.observe(this, Observer { textViewGmail -> textGmail.text=textViewGmail  })
        ViewModel.textoTextView.observe(this, Observer { textoTextView -> textView.text=textoTextView  })
        ViewModel.signInEnabled.observe(this, Observer { signInEnabled -> signInButon.isEnabled=signInEnabled })
        ViewModel.signOutEnabled.observe(this, Observer { signOutEnabled -> signOutButon.isEnabled=signOutEnabled })
        ViewModel.textViewEnabled.observe(this, Observer { textViewEnabled -> textView.isEnabled=textViewEnabled })

        signInButon.setOnClickListener {
            signIn()
        }

        signOutButon.setOnClickListener {
            signOut()
            textGmail.text=""
            signInButon.isEnabled=true
            textView.text=""
            signOutButon.isEnabled=false
        }

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