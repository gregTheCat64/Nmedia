package com.example.nmedia.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.nmedia.R
import com.example.nmedia.databinding.FragmentRegistrationBinding
import com.example.nmedia.viewmodel.RegistrationViewModel
import com.google.android.material.snackbar.Snackbar

class RegistrationFragment : Fragment() {

    companion object {
        fun newInstance() = RegistrationFragment()
    }

    private  val viewModel: RegistrationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentRegistrationBinding.inflate(inflater, container,false)

        binding.registerUserBtn.setOnClickListener {
            if (binding.loginEditText.text.isNotEmpty() &&
                    binding.passwordEditText.text.isNotEmpty()&&
                    binding.nameEditText.text.isNotEmpty()){
                val login = binding.loginEditText.text.toString()
                val password = binding.passwordEditText.text.toString()
                val name = binding.nameEditText.text.toString()
                viewModel.registerUser(login,password,name)
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