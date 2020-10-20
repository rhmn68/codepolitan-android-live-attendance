package com.example.liveattendanceapp.views.forgotpass

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.example.liveattendanceapp.R
import com.example.liveattendanceapp.databinding.ActivityForgotPasswordBinding
import com.example.liveattendanceapp.dialog.MyDialog
import com.example.liveattendanceapp.model.ForgotPasswordResponse
import com.example.liveattendanceapp.networking.ApiServices
import com.example.liveattendanceapp.networking.RetrofitClient
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response
import java.io.IOException

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        onClick()
    }

    private fun onClick() {
        binding.tbForgotPassword.setNavigationOnClickListener {
            finish()
        }

        binding.btnForgotPassword.setOnClickListener {
            val email = binding.etEmailForgotPassword.text.toString()
            if (isFormValid(email)){
                forgotPassToServer(email)
            }
        }
    }

    private fun forgotPassToServer(email: String) {
        val forgotPasswordRequest = ForgotPasswordRequest(email = email)
        val forgotPasswordRequestString = Gson().toJson(forgotPasswordRequest)

        MyDialog.showProgressDialog(this)

        ApiServices.getLiveAttendanceServices()
            .forgotPasswordRequest(forgotPasswordRequestString)
            .enqueue(object : Callback<ForgotPasswordResponse>{
                override fun onResponse(
                    call: Call<ForgotPasswordResponse>,
                    response: Response<ForgotPasswordResponse>
                ) {
                    MyDialog.hideDialog()
                    if (response.isSuccessful){
                        val message = response.body()?.message
                        MyDialog.dynamicDialog(
                            this@ForgotPasswordActivity,
                            getString(R.string.success),
                            message.toString()
                        )
                        Handler(Looper.getMainLooper()).postDelayed({
                            MyDialog.hideDialog()
                            finish()
                        },2000)
                    }else{
                        val errorConverter: Converter<ResponseBody, ForgotPasswordResponse> =
                            RetrofitClient
                                .getClient()
                                .responseBodyConverter(
                                    ForgotPasswordResponse::class.java,
                                    arrayOfNulls<Annotation>(0)
                                )
                        var errorResponse: ForgotPasswordResponse?
                        try {
                            response.errorBody()?.let {
                                errorResponse = errorConverter.convert(it)
                                MyDialog.dynamicDialog(
                                    this@ForgotPasswordActivity,
                                    getString(R.string.failed),
                                    errorResponse?.message.toString()
                                )
                            }
                        }catch (e: IOException){
                            e.printStackTrace()
                            Log.e(TAG, "Error: ${e.message}")
                        }
                    }
                }

                override fun onFailure(call: Call<ForgotPasswordResponse>, t: Throwable) {
                    MyDialog.hideDialog()
                    Log.e(TAG, "Error: ${t.message}")
                }

            })
    }

    private fun isFormValid(email: String): Boolean {
        if (email.isEmpty()){
            binding.etEmailForgotPassword.error = getString(R.string.please_field_your_email)
            binding.etEmailForgotPassword.requestFocus()
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.etEmailForgotPassword.error = getString(R.string.please_use_valid_email)
            binding.etEmailForgotPassword.requestFocus()
        }else{
            binding.etEmailForgotPassword.error = null
            return true
        }
        return false
    }

    private fun init() {
        setSupportActionBar(binding.tbForgotPassword)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    companion object{
        private val TAG = ForgotPasswordActivity::class.java.simpleName
    }
}