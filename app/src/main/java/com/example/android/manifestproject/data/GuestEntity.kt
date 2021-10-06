package com.example.android.manifestproject.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "guest_list")
data class GuestEntity(
    @PrimaryKey(autoGenerate = true)
    var guestID: Long = 0L,

    @ColumnInfo(name = "full_name")
    var fullName: String = "",

    @ColumnInfo(name = "phone_number")
    var phoneNumber: String = "",

    @ColumnInfo(name = "email")
    var email: String = "",

    @ColumnInfo(name = "emergency_phone_number")
    var emergencyPhoneNumber: String = "",

    @ColumnInfo(name = "emergency_name")
    var emergencyName: String = ""
)