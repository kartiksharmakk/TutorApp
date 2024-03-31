package com.example.myapplication.Data

object DataModel{
    data class TeacherModel(
        val name: String,
        val dob: String,
        val gender: String,
        val type: String = "Teacher"
    )

    data class UserCredentials(
        val username: String,
        val password: String,
        val phone: String,
    )

    data class StudentModel(
        val name: String,
        val dob: String,
        var standard: String,
        val type: String = "Student"
    )
}