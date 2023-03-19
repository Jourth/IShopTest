package ru.juxlab.tt.ishoptest.ui.home

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import ru.juxlab.tt.ishoptest.data.ApiGetFlashSaleService
import ru.juxlab.tt.ishoptest.data.ApiGetLatestService
import ru.juxlab.tt.ishoptest.utils.lazyDeffered

class HomeFragmentViewModel : ViewModel() {

   val latestProductsList by lazyDeffered { ApiGetLatestService().getLatestProducts() }
   val flashSaleList by lazyDeffered { ApiGetFlashSaleService().getFlashSaleProducts() }

}