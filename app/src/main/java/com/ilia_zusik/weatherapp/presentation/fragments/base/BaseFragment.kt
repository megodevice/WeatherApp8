package com.ilia_zusik.weatherapp.presentation.fragments.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

typealias inflater<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<
        VB : ViewBinding,
        VM : ViewModel
        >(
    private val inflate: inflater<VB>
) : Fragment() {

    protected abstract val viewModel: VM

    private var _binding: VB? = null
    protected val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
        observe()
    }

    open fun observe() {

    }

    open fun initialize() {

    }
}