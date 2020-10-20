package com.example.liveattendanceapp.views.login

import com.google.gson.annotations.SerializedName

data class LoginRequest(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("device_name")
	val deviceName: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
