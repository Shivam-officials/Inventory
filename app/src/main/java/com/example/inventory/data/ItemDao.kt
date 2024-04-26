package com.example.inventory.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Dao for items table to centralise the crud operations
 */
@Dao
interface ItemDao {

    /**
     * adding suspend bcz we wand to make database operation on background thread and 
     * also we are not getting anything in return so we can launch them in bg thread so they run 
     * and get completed on their own in the case of [insert],[update],[delete] function
     */
    @Insert(onConflict = OnConflictStrategy.)
    suspend fun insert(item: Item)

    //update the item
    @Update
    suspend fun update(item: Item)

//    delete the item
    @Delete
    suspend fun  delete(item: Item)

    /**
     * Because of the Flow return type, Room also runs the query on the
     * background thread. You don't need to explicitly make it a suspend
     * function and call it inside a coroutine scope.
     */

    @Query("SELECT * FROM items ORDER BY name ASC")
    fun getAllItem(): Flow<List<Item>>

    /**
     * the [id] uses the colon notation in the query to reference arguments in the function.
     */
    @Query("SELECT * FROM items WHERE id = :id")
    fun getItem(id:Int): Flow<List<Item>>

}