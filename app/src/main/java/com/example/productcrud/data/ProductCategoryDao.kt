package com.example.productcrud.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.productcrud.model.ProductCategory
import kotlinx.coroutines.flow.Flow

/*Room általában az alábbi visszatérési típusokat támogatja @Insert esetén:
Long → egy rekord beszúrása
List<Long> → több rekord
Unit (ha nem érdekel az ID)
*/

@Dao
interface ProductCategoryDao {
    @Query("SELECT * FROM product_categories WHERE deleted = 0 ORDER BY id")
    fun getAll(): Flow<List<ProductCategory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: ProductCategory):Long

    @Query("SELECT * FROM product_categories WHERE name = :name LIMIT 1")
    suspend fun getByName(name: String): ProductCategory?

    @Query("SELECT * FROM product_categories WHERE deleted = 0 ORDER BY id")
    suspend fun getAllOnce(): List<ProductCategory>

    @Update
    suspend fun updateCategory(category: ProductCategory)

    @Query("UPDATE product_categories SET deleted = 1 WHERE id = :id")
    suspend fun softDeleteCategory(id: Int)
}