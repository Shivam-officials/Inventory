package com.example.inventory
import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.inventory.data.InventoryDatabase
import com.example.inventory.data.Item
import com.example.inventory.data.ItemDao
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
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


    /**
     * testing the update item function
     */
    @Test
    fun daoItemUpdates_updatesItemsInDb() = runBlocking {
        // Add two items to the database.
        addTwoItemsToDb()

        // Update the item by replacing an existing item.
        itemDao.update(Item(1, "Apples", 15.0, 25))
        itemDao.update(Item(2, "Bananas", 5.0, 50))

        // Get updated items.
        val allItems = itemDao.getAllItem().first()

        //asserting the items in the database have been updated correctly
        assertEquals(allItems[0],Item(1, "Apples", 15.0, 25) )
        assertEquals(allItems[1],Item(2, "Bananas", 5.0, 50))

    }

    /**
     * testing the delete dao function
     */
    @Test
    fun daoItemDelete_deletesItemFromDb() = runBlocking {
        // Add two items to the database.
        addTwoItemsToDb()

        //delete those two items
        itemDao.delete(item1)
        itemDao.delete(item2)

        // get all the items
        val allItems = itemDao.getAllItem().first()

        // asserting that the items has been deleted and list is empty
        assertTrue(allItems.isEmpty())
    }

    /**
     * testing the get item function
     */
    @Test
    fun daoGetItem_returnsItemFromDb() = runBlocking {
        // Add two items to the database.
        addOneItemToDb()
        val item = itemDao.getItem(1).first()
        assertEquals(item, item1)
    }

}