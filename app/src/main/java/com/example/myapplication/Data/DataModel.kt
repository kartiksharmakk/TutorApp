package com.example.myapplication.Data

object DataModel{
    data class TeacherModel(
        val name: String,
        val dob: String,
        val gender: String,
        val type: String = "Teacher"
    )

    data class UserCredentials(
        val uid: String,
        val email: String,
        val countryCode: String,
        val phone: String,
        var image: String,
        val isVerified: Boolean
    )

    data class StudentModel(
        val name: String,
        val dob: String,
        var standard: String,
        val type: String = "Student"
    )
}