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
        // Simulate fetching data from a repsitory
        viewModelScope.launch {o
            val items = listOf(
                ClothingItem(1, "T-Shirt", "url_to_image_1", 19.99),
                ClothingItem(2, "Jeans", "url_to_image_2", 49.99),
                ClothingItem(3, "Jacket", "url_to_image_3", 89.99),
                ClothingItem(4, "Sneakers", "url_to_image_4", 69.99)
            )
            _clothingItems.value = items
        }
    }
}