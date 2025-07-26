package com.example.productcrud.data

import android.util.Log
import com.example.productcrud.dto.ProductDto
import com.example.productcrud.mapper.toDto
import com.example.productcrud.mapper.toEntity
import com.example.productcrud.model.Product
import com.example.productcrud.model.ProductCategory
import com.example.productcrud.model.VatRate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    private val productDao: ProductDao,
    private val productCategoryDao: ProductCategoryDao,
    private val vatRateDao: VatRateDao
) : IProductRepository {

    // --- Product CRUD ---
    override fun getAllProducts(): Flow<List<ProductDto>> = flow {
        val products = productDao.getAll().first()
        val categories = productCategoryDao.getAll().first()
        val vatRates = vatRateDao.getAll().first()

        val dtoList = products.map { product ->
            val categoryName = categories.find { it.id == product.categoryId }?.name ?: "Ismeretlen"
            val vatPercentage = vatRates.find { it.id == product.vatId }?.percentage ?: 0
            product.toDto(categoryName, vatPercentage)
        }
        emit(dtoList)
    }

    override suspend fun insertProduct(productDto: ProductDto) {
        // Megkeressük a kapcsolódó categoryId-t és vatId-t a név és érték alapján
        val category = productCategoryDao.getByName(productDto.categoryName)
        //val vatRate = vatRateDao.getByPercentage(productDto.vatValue)

        // DTO → Entity konverzió mapperrel
        val entity = productDto.toEntity(
            categoryId = category?.id ?: 0,
            vatId = productDto.vatValue
        )
        Log.d("Insert", "Insert termék ID (entity): ${entity.id}")
        Log.d("Insert", "Insert termék minden adat: ${entity}")
        // Log.d("EDIT", "Insert termék VatRate értéke: ${vatRate}")
        Log.d("Insert", "Insert termék productDto.vatValue: ${ productDto.vatValue}")
        productDao.insert(entity)
    }

    override suspend fun updateProduct(productDto: ProductDto) {
        val category = productCategoryDao.getByName(productDto.categoryName)
        //val vatRate = vatRateDao.getByPercentage(productDto.vatValue)

        val entity = productDto.toEntity(
            categoryId = category?.id ?: 0,
            vatId = productDto.vatValue
        )
        Log.d("EDIT", "Szerkesztésre kiválasztott termék ID (entity): ${entity.id}")
        Log.d("EDIT", "Szerkesztésre kiválasztott termék minden adat: ${entity}")
        productDao.update(entity)
    }

    override suspend fun deleteProduct(productDto: ProductDto) {
        val category = productCategoryDao.getByName(productDto.categoryName)
        //val vatRate = vatRateDao.getByPercentage(productDto.vatValue)

        val entity = productDto.toEntity(
            categoryId = category?.id ?: 0,
            vatId = productDto.vatValue
        )
        productDao.delete(entity)
    }

    override suspend fun getProductById(id: Int): ProductDto? {
        val product = productDao.getById(id) ?: return null
        val category = productCategoryDao.getAll().first().find { it.id == product.categoryId }
        val vatRate = vatRateDao.getAll().first().find { it.id == product.vatId }

        return product.toDto(
            categoryName = category?.name ?: "Ismeretlen",
            vatPercentage = vatRate?.percentage ?: 0
        )
    }

    // --- Category / VAT lekérés ---
    override fun getAllCategories(): Flow<List<ProductCategory>> = productCategoryDao.getAll()

    override fun getAllVatRates(): Flow<List<VatRate>> = vatRateDao.getAll()

    // --- Alapértelmezett adatok feltöltése (ViewModel-ben hívjuk initkor) ---
    override suspend fun populateDefaultsIfNeeded() {
        val categories = productCategoryDao.getAll().first()
        val vatRates = vatRateDao.getAll().first()
        val products = productDao.getAll().first()

        if (categories.isEmpty() && vatRates.isEmpty() && products.isEmpty()) {
            // Kategóriák feltöltése
            val categoryEntities = listOf(
                ProductCategory(name = "Asztali PC"),
                ProductCategory(name = "Notebook"),
                ProductCategory(name = "Periféria")
            )

            val categoryIds = categoryEntities.map { category ->
                productCategoryDao.insert(category).toInt()
            }

            // ÁFA kulcsok feltöltése
            val vatIds = listOf(
                vatRateDao.insert(VatRate(percentage = 0)).toInt(),
                vatRateDao.insert(VatRate(percentage = 12)).toInt(),
                vatRateDao.insert(VatRate(percentage = 27)).toInt()
            )

            // Dto lista, amit map-elni fogunk Product-ra
            val sampleDtos = listOf(
                ProductDto(0, "HP EliteDesk", "HP", "800 G6", 120000.0, vatIds[2], 10, "Asztali PC", true),
                ProductDto(0, "Dell Inspiron", "Dell", "15 3511", 160000.0, vatIds[2], 5, "Notebook", true),
                ProductDto(0, "Logitech Egér", "Logitech", "M185", 3500.0, vatIds[1], 50, "Periféria", true),
                ProductDto(0, "Lenovo ThinkCentre", "Lenovo", "M75s", 130000.0, vatIds[2], 8, "Asztali PC", false),
                ProductDto(0, "Asus VivoBook", "Asus", "D515", 150000.0, vatIds[2], 12, "Notebook", true)
            )

            val sampleProducts = sampleDtos.map { dto ->
                val categoryId = categoryEntities
                    .find { it.name == dto.categoryName }?.let { inserted ->
                        categoryIds[categoryEntities.indexOf(inserted)]
                    }
                dto.toEntity(categoryId, dto.vatValue)
            }
            sampleProducts.forEach { productDao.insert(it) }
        }
    }
}