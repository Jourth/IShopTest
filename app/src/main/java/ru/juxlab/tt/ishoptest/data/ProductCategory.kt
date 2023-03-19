package ru.juxlab.tt.ishoptest.data

import ru.juxlab.tt.ishoptest.R

data class ProductCategory (val nameId : Int, val iconId : Int) {
    companion object {
        fun getProductCategories():List<ProductCategory>{
            return listOf(
                ProductCategory(R.string.category_phones,     R.drawable.ic_category_phones),
                ProductCategory(R.string.category_headphones, R.drawable.ic_category_headphones),
                ProductCategory(R.string.category_games,      R.drawable.ic_category_games),
                ProductCategory(R.string.category_cars,       R.drawable.ic_category_cars),
                ProductCategory(R.string.category_furniture,  R.drawable.ic_category_furniture),
                ProductCategory(R.string.category_kids,       R.drawable.ic_category_kids)
            )
        }
    }
}