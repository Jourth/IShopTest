package ru.juxlab.tt.ishoptest.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.juxlab.tt.ishoptest.R


class CartFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val cartViewModel =
            ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
                ViewModel::class.java
            )


        val root = inflater.inflate(R.layout.fragment_cart, container, false)


        return root
    }


}