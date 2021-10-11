package com.example.android.manifestproject.data

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface ManifestoDatabaseDao {


    //Adds a new guest to the database
    @Insert
    suspend fun insert(guest: GuestEntity)

    //Updates the information of a guest
    @Update
    suspend fun update(guest: GuestEntity)

    //Deletes guest from table
    @Delete
    fun delete(guest: GuestEntity)

    @Query("SELECT * from guest_list WHERE guestID = :key")
    suspend fun get(key: Long): GuestEntity?

    //Gets all guests by order of ID
    @Query("SELECT * FROM guest_list ORDER BY guestID DESC")
    fun getAllGuests(): LiveData<List<GuestEntity>>

    //Gets the guest with the corresponding ID
    @Query("SELECT * from guest_list WHERE guestID = :key")
    fun getGuestWithId(key: Long): LiveData<GuestEntity>

    @Query("SELECT * FROM guest_list ORDER BY guestID DESC LIMIT 1")
    fun getGuest(): GuestEntity?

    @Query("SELECT * FROM guest_list")
    fun getAll(): List<GuestEntity>
}