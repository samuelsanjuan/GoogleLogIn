package com.example.googlelogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import org.jetbrains.anko.doAsync
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("470320870749-l0ct9p1dsvgk1hms2r7vfa42b9r4guuq.apps.googleusercontent.com")
                .requestEmail()
                .build()


        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        val loginButon: Button=findViewById(R.id.googleloginbtn)
        loginButon.setOnClickListener {
            signIn()
        }

        val signOutButon: Button=findViewById(R.id.googlesignoutbtn)
        signOutButon.setOnClickListener {
            signOut()
        }
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this) {
                }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task =
                    GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl("https://serviciosapps.grupomaviva.es/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    private fun ComprobarDatosRest(correo:String){

        //aun no esta implementado

        if (correo=="sansamuelandres@gmail.com") {
            Toast.makeText(applicationContext, "Correo Valido", Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(applicationContext,"Correo No Valido",Toast.LENGTH_LONG).show()
            signOut()
        }
    }



    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(
                    ApiException::class.java
            )
            // Signed in successfully
            val googleId = account?.id ?: ""
            Log.i("Google ID",googleId)

            val googleFirstName = account?.givenName ?: ""
            Log.i("Google First Name", googleFirstName)

            val googleLastName = account?.familyName ?: ""
            Log.i("Google Last Name", googleLastName)

            val googleEmail = account?.email ?: ""
            Log.i("Google Email", googleEmail)

            ComprobarDatosRest(googleEmail)


            val googleProfilePicURL = account?.photoUrl.toString()
            Log.i("Google Profile Pic URL", googleProfilePicURL)

            val googleIdToken = account?.idToken ?: ""
            Log.i("Google ID Token", googleIdToken)

        } catch (e: ApiException) {
            // Sign in was unsuccessful
            Log.e("failed code=", e.statusCode.toString())
        }
    }

    private fun validarCorreoAsic(gmail: String) {
        doAsync {
            var gmail = gmail
            var app = "appEntregasPsa"
            var version = "1.0.0"
            getRetrofit().create(WebService::class.java)
                    .getDispositivo("https://serviciosapps.grupomaviva.es/dispositivo.php?id_cuenta_ok=" + gmail + app + version)
                    .execute()
                    .body()
        }
    }
}