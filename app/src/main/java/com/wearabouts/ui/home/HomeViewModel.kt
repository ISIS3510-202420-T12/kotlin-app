package com.wearabouts.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.wearabouts.models.ClothingItem

class HomeViewModel : ViewModel() {

    private val _clothingItems = MutableStateFlow<List<ClothingItem>>(emptyList())
    val clothingItems: StateFlow<List<ClothingItem>> = _clothingItems

    init {
        fetchClothingItems()
    }

    private fun fetchClothingItems() {
        // Simulate fetching data from a repository
        viewModelScope.launch {
            val items = listOf(
                ClothingItem(1, "T-Shirt", "https://www.therange.co.uk/media/2/5/1654518853_12_1005.jpg", 19.99),
                ClothingItem(2, "Jeans", "https://img1.exportersindia.com/product_images/bc-full/2019/1/5450192/mens-funny-look-jeans-1547451409-4644396.jpeg", 49.99),
                ClothingItem(3, "Jacket", "https://m.media-amazon.com/images/I/71zaJkhWPCL._AC_UY1000_.jpg", 89.99),
                ClothingItem(4, "Skirt", "https://m.media-amazon.com/images/I/71ZJF42-4UL._AC_SX569_.jpg", 69.99),
                ClothingItem(5, "Duck Shoes", "https://i.pinimg.com/originals/77/83/62/778362991f15bcc6211a3cd3e9e41533.jpg", 69.99),
                ClothingItem(6, "Space Pants", "https://canary.contestimg.wish.com/api/webimage/5e981c690ca0dc55df360cfd-2-large.jpg", 69.99),
                ClothingItem(7, "Skirt", "https://m.media-amazon.com/images/I/71ZJF42-4UL._AC_SX569_.jpg", 69.99)



            )
            _clothingItems.value = items
        }
    }
}