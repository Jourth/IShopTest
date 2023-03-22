package ru.juxlab.tt.ishoptest.ui.home


import androidx.lifecycle.ViewModel
import ru.juxlab.tt.ishoptest.data.ApiGetFlashSaleService
import ru.juxlab.tt.ishoptest.data.ApiGetLatestService
import ru.juxlab.tt.ishoptest.utils.lazyDeffered

class HomeFragmentViewModel : ViewModel() {

   val latestProductsList by lazyDeffered { ApiGetLatestService().getLatestProducts() }
   val flashSaleList by lazyDeffered { ApiGetFlashSaleService().getFlashSaleProducts() }

}