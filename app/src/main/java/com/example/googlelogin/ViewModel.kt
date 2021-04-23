package com.example.googlelogin

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import okhttp3.ResponseBody
import org.jetbrains.anko.doAsync
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ViewModel {

    val textViewEnabled= MutableLiveData<Boolean>()
    val signInEnabled= MutableLiveData<Boolean>()
    val signOutEnabled= MutableLiveData<Boolean>()
    val textoTextView= MutableLiveData<String>()
    val textViewGmail= MutableLiveData<String>()

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("470320870749-l0ct9p1dsvgk1hms2r7vfa42b9r4guuq.apps.googleusercontent.com")
        .requestEmail()
        .build()

    fun getRetrofit(): Retrofit {
    val retrofit= Retrofit.Builder()
        .baseUrl("https://serviciosapps.grupomaviva.es/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    return retrofit
}

    fun handleSignInResult(completedTask: Task<GoogleSignInAccount>,clienteGoogle: GoogleSignInClient) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            val googleId = account?.id ?: ""
            println(googleId)

            val googleFirstName = account?.givenName ?: ""
            val googleLastName = account?.familyName ?: ""
            println(googleFirstName+" "+googleLastName)

            val googleEmail = account?.email ?: ""
            println(googleEmail)

            val googleProfilePicURL = account?.photoUrl.toString()
            println(googleProfilePicURL)

            val googleIdToken = account?.idToken ?: ""
            println(googleIdToken)

            validarCorreoAsic(googleEmail,"appEntregasPsa","1.0.0",clienteGoogle)


        } catch (e: ApiException) {
            // Sign in was unsuccessful
            Log.e("failed code=", e.statusCode.toString())
        }
    }

    fun toastBooleanValidacion(boolean: Boolean,clienteGoogle:GoogleSignInClient){
        if (boolean==true){
            textViewEnabled.value=true
            textoTextView.value="\t           Correo Valido"
            signInEnabled.value=false
            signOutEnabled.value=true
        }else{
            clienteGoogle.signOut()
            textViewEnabled.value=true
            signInEnabled.value=true
            signOutEnabled.value=false
            textoTextView.value="\t          Correo Invalido\nAvisa al departamento de it \n     para que te den de alta"
        }
    }

    private fun validarCorreoAsic(gmail: String,app:String,version:String,clienteGoogle: GoogleSignInClient) {

        doAsync {

            val whereIdGmail = "id_cuenta_ok=${gmail}"
            val whereAplicacion = "&app_ok=${app}"
            val whereAppVersion = "&version_ok=${version}"

            val responde=getRetrofit().create(WebService::class.java)
                .myNetworkCall("https://serviciosapps.grupomaviva.es/dispositivo.php?" + whereIdGmail + whereAplicacion + whereAppVersion)

            responde.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(responde: Call<ResponseBody>,response: Response<ResponseBody>) {

                    val respuesta=response.body()?.string()

                    if (respuesta=="[\"OK\"]"){
                        toastBooleanValidacion(true,clienteGoogle)
                        textViewGmail.value=gmail
                    }else {
                        textViewGmail.value=""
                        toastBooleanValidacion(false,clienteGoogle)
                    }
                }

                override fun onFailure(responde: Call<ResponseBody>, t: Throwable) {
                    clienteGoogle.signOut()
                    signOutEnabled.value=false
                    signInEnabled.value=true
                    textViewEnabled.value=true
                    textViewGmail.value=""
                    textoTextView.value="\t                 ERROR"
                }
            }
            )
        }
    }
}