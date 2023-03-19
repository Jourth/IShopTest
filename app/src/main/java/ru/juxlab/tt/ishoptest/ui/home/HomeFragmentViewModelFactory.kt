package ru.juxlab.tt.ishoptest.ui.home

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

class HomeFragmentViewModelFactory : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeFragmentViewModel() as T

    }
}