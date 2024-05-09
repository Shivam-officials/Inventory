package com.example.inventory
import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.inventory.data.InventoryDatabase
import com.example.inventory.data.Item
import com.example.inventory.data.ItemDao
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


/**
 * Room database testing class
 */
@RunWith(AndroidJUnit4::class)
class ItemDaoTest {

    private lateinit var itemDao: ItemDao
    private lateinit var inventoryDatabase: InventoryDatabase

    /**
     * create the room  database object and the dao
     */
    @Before
    fun createDb(){
        val context: Context = ApplicationProvider.getApplicationContext()
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        inventoryDatabase = Room.inMemoryDatabaseBuilder(context, InventoryDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        itemDao = inventoryDatabase.itemDao()
    }

    /**
     * close the database after the test
     */
    @After
    @Throws(IOException::class)
    fun closeDb(){
        inventoryDatabase.close()
    }

    /** items to be used in the test */
    private val item1 = Item(1, "Apples", 10.0, 20)
    private val item2 = Item(2, "Banana", 15.0, 97)

    /**
     * add one item to the database
     */
    private suspend fun addOneItemToDb(){
        itemDao.insert(item1)
    }

    /**
     * add two items to the database
     */
    private suspend fun addTwoItemsToDb(){
        itemDao.insert(item1)
        itemDao.insert(item2)
    }


    /**
     * testing the insert function
     */
    @Test
    @Throws(Exception::class)
    fun daoInsert_insertItemIntoDb() = runBlocking{
        addOneItemToDb()
        val allItems = itemDao.getAllItem().first()
        assertEquals(allItems[0], item1)
    }


    /**
     * testing the getAllItems function
     */
    @Test
    @Throws(Exception::class)
    fun daoGetAllItems_returnAllItemsFromDb() = runBlocking{
        addTwoItemsToDb()
        val allItems = itemDao.getAllItem().first()
        assertEquals(allItems[0], item1)
        assertEquals(allItems[1], item2)
        }
}