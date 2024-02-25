package com.kien.petclub.utils

import com.google.firebase.database.DataSnapshot
import com.kien.petclub.domain.model.entity.Product
import io.mockk.every
import io.mockk.mockk

val goodsList = listOf(
    Product.Goods(
        id = "1",
        code = "G001",
        name = "Apple iPhone 12",
        type = "Electronics",
        brands = "Apple",
        sellingPrice = "999",
        buyingPrice = "800",
        stock = "50",
        weight = "0.5",
        location = "Warehouse A",
        description = "Latest Apple iPhone 12 model",
        note = "Best seller",
        photo = arrayListOf("url_to_photo1.jpg", "url_to_photo2.jpg"),
        minimumStock = "10",
        maximumStock = "100",
        updatedDate = 1640995200000L
    ),
    Product.Goods(
        id = "2",
        code = "G002",
        name = "Samsung Galaxy S21",
        type = "Electronics",
        brands = "Samsung",
        sellingPrice = "899",
        buyingPrice = "700",
        stock = "40",
        weight = "0.4",
        location = "Warehouse B",
        description = "Latest Samsung Galaxy model",
        note = "New arrival",
        photo = arrayListOf("url_to_photo3.jpg", "url_to_photo4.jpg"),
        minimumStock = "5",
        maximumStock = "50",
        updatedDate = 1641081600000L
    ),
    Product.Goods(
        id = "3",
        code = "G003",
        name = "Sony WH-1000XM4",
        type = "Accessories",
        brands = "Sony",
        sellingPrice = "350",
        buyingPrice = "250",
        stock = "30",
        weight = "0.3",
        location = "Warehouse C",
        description = "Noise Cancelling Headphones",
        note = "Top rated headphones",
        photo = arrayListOf("url_to_photo7.jpg"),
        minimumStock = "5",
        maximumStock = "40",
        updatedDate = 1641168000000L
    ),
    Product.Goods(
        id = "4",
        code = "G004",
        name = "Logitech MX Master 3",
        type = "Accessories",
        brands = "Logitech",
        sellingPrice = "100",
        buyingPrice = "80",
        stock = "60",
        weight = "0.25",
        location = "Warehouse D",
        description = "Advanced Wireless Mouse",
        note = "For professionals",
        photo = arrayListOf("url_to_photo5.jpg", "url_to_photo6.jpg"),
        minimumStock = "10",
        maximumStock = "100",
        updatedDate = 1641254400000L
    ),
    Product.Goods(
        id = "5",
        code = "G005",
        name = "Nike Air Force 1",
        type = "Footwear",
        brands = "Nike",
        sellingPrice = "90",
        buyingPrice = "60",
        stock = "70",
        weight = "1",
        location = "Warehouse E",
        description = "Classic sneakers",
        note = "Popular worldwide",
        photo = null,
        minimumStock = "20",
        maximumStock = "200",
        updatedDate = 1641340800000L
    )
)

fun mockDataSnapshotListWithGoods(): DataSnapshot {
    val parentSnapshot = mockk<DataSnapshot>(relaxed = true)
    val childSnapshots = goodsList.map { goods ->
        val childSnapshot = mockk<DataSnapshot>(relaxed = true)
        every { childSnapshot.child("id").getValue(String::class.java) } returns goods.id
        every { childSnapshot.child("code").getValue(String::class.java) } returns goods.code
        every { childSnapshot.child("name").getValue(String::class.java) } returns goods.name
        every { childSnapshot.child("type").getValue(String::class.java) } returns goods.type
        every { childSnapshot.child("brands").getValue(String::class.java) } returns goods.brands
        every { childSnapshot.child("sellingPrice").getValue(String::class.java) } returns goods.sellingPrice
        every { childSnapshot.child("buyingPrice").getValue(String::class.java) } returns goods.buyingPrice
        every { childSnapshot.child("stock").getValue(String::class.java) } returns goods.stock
        every { childSnapshot.child("weight").getValue(String::class.java) } returns goods.weight
        every { childSnapshot.child("location").getValue(String::class.java) } returns goods.location
        every { childSnapshot.child("description").getValue(String::class.java) } returns goods.description
        every { childSnapshot.child("note").getValue(String::class.java) } returns goods.note
        every { childSnapshot.child("minimumStock").getValue(String::class.java) } returns goods.minimumStock
        every { childSnapshot.child("maximumStock").getValue(String::class.java) } returns goods.maximumStock
        every { childSnapshot.child("updatedDate").getValue(Long::class.java) } returns goods.updatedDate
        val listPhotoSnapShot = goods.photo?.mapIndexed { index, photo ->
            val photoSnapshot = mockk<DataSnapshot>(relaxed = true)
            every { photoSnapshot.getValue(String::class.java) } returns photo
            photoSnapshot
        } ?: listOf()
        every { childSnapshot.child("photo").children } returns listPhotoSnapShot
        every { childSnapshot.key } returns goods.id
        childSnapshot
    }
    every { parentSnapshot.children } returns childSnapshots
    return parentSnapshot
}
