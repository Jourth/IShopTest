package ru.juxlab.tt.ishoptest.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.support.v4.app.Fragment
import android.arch.lifecycle.ViewModelProvider
import ru.juxlab.tt.ishoptest.R


class CartFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val cartViewModel =
            ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
                CartViewModel::class.java
            )


        val root = inflater.inflate(R.layout.fragment_cart, container, false)


        return root
    }


}