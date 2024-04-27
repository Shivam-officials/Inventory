package com.example.inventory.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


/**
 * making the database abstract class bcz room will create the implementation of this class
 * and also provide Dao  implementation to interacting with the entities(tables) for databases operation
 */
@Database(
    entities = [Item::class],  /** list of Entities (tables) */
    version = 1, /**  Whenever you change the schema of the database table, you have to increase the version number. */
    exportSchema = false /** setting false so not to keep schema version history backups. */
)
abstract class InventoryDatabase: RoomDatabase() {

    abstract fun itemDao(): ItemDao

    /**
     * companion object for getting the database object from the class directly
     */
    companion object {
        /** Volatile variable to ensure that changes to Instance are immediately visible to other threads */
        @Volatile
        private var Instance: InventoryDatabase? = null

        /**
         * Function to get an instance of the database
         */
        fun getDatabase(context: Context): InventoryDatabase {
            /**  Return the existing [Instance] if it is not null, otherwise create a new database instance */
            return Instance ?: synchronized(this) {

                /**
                 *  Inside the [synchronized] block, ensure that only one thread can enter the synchronised block
                 *  at a time to can create a new database instance.This prevents multiple threads from creating
                 *  multiple instances concurrently
                 */
                Room.databaseBuilder(context, InventoryDatabase::class.java, "item_database")
                    // Use Room's databaseBuilder to configure and build the database instance
                    .build()
                    /** Set the [Instance] variable to the newly created database instance */
                    .also { Instance = it }
            }
        }
    }
}
