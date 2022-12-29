package com.example.nmedia.activities

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.nmedia.R
import com.example.nmedia.databinding.FragmentSignInBinding
import com.example.nmedia.viewmodel.SignInViewModel
import com.google.android.material.snackbar.Snackbar

class SignInFragment : Fragment() {

    companion object {
        fun newInstance() = SignInFragment()
    }

    private val viewModel: SignInViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSignInBinding.inflate(inflater, container, false)

        binding.loginBtn.setOnClickListener {
            if (binding.loginEditText.text.toString().isNotEmpty() && binding.passwordEditText.text.toString().isNotEmpty() ){
                val login = binding.loginEditText.text.toString()
                val pass = binding.passwordEditText.text.toString()
                viewModel.updateUser(login,pass)

            } else Snackbar.make(binding.root, "Заполните все поля", Snackbar.LENGTH_LONG).show()
        }
        viewModel.tokenReceived.observe(viewLifecycleOwner) {
            if (it == 0){
                findNavController().navigateUp()
            } else {
                Snackbar.make(binding.root, "Неверный пароль или логин", Snackbar.LENGTH_LONG).show()
            }
        }
        return binding.root
    }
}