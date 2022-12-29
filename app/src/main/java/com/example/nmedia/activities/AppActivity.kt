package com.example.nmedia.activities

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.MenuProvider
import androidx.navigation.findNavController
import com.example.nmedia.R
import com.example.nmedia.activities.NewPostFragment.Companion.textArg
import com.example.nmedia.auth.AppAuth
import com.example.nmedia.viewmodel.AuthViewModel
import com.example.nmedia.viewmodel.PostViewModel
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AppActivity : AppCompatActivity(R.layout.activity_app) {

    @Inject
    lateinit var appAuth: AppAuth

    @Inject
    lateinit var firebaseMessaging: FirebaseMessaging

    @Inject
    lateinit var googleApiAvailability: GoogleApiAvailability

    val viewModel: AuthViewModel by viewModels()

    private val postViewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent?.let {
            if (it.action != Intent.ACTION_SEND) {
                return@let
            }

            val text = it.getStringExtra(Intent.EXTRA_TEXT)
            if (text?.isNotBlank() != true) {
                return@let
            }

            intent.removeExtra(Intent.EXTRA_TEXT)
            findNavController(R.id.nav_host_fragment)
                .navigate(
                    R.id.action_feedFragment_to_newPostFragment,
                    Bundle().apply {
                        textArg = text
                    }
                )
        }

        checkGoogleApiAvailability()


        var currentMenuProvider: MenuProvider? = null
        viewModel.data.observe(this) {
            currentMenuProvider?.also { removeMenuProvider(it) }
            addMenuProvider(object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.menu_main, menu)
                    val authorized = viewModel.authorized
                    menu.setGroupVisible(R.id.authorized, authorized)
                    menu.setGroupVisible(R.id.unAuthorized, !authorized)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean  =
                    when (menuItem.itemId) {
                        R.id.signIn -> {
                            findNavController(R.id.nav_host_fragment).navigate(R.id.action_feedFragment_to_signInFragment)
                            appAuth.setAuth(5,"x-token")
                            true
                        }
                        R.id.signUp -> {
                            findNavController(R.id.nav_host_fragment).navigate(R.id.action_feedFragment_to_registrationFragment)
                            appAuth.setAuth(5,"x-token")
                            true
                        }
                        R.id.logout -> {
                            showSignOutDialog()
                            postViewModel.refresh()
                            true
                        }
                        else -> false
                    }
            }.apply {
                currentMenuProvider = this
            })
        }
    }

    private fun showSignOutDialog(){
        val listener = DialogInterface.OnClickListener{ _, which->
            when(which) {
                DialogInterface.BUTTON_POSITIVE -> appAuth.removeAuth()
                DialogInterface.BUTTON_NEGATIVE -> Toast.makeText(this, "ну и ладненько...", Toast.LENGTH_SHORT).show()
            }
        }
        val dialog = AlertDialog.Builder(this)
            .setCancelable(false)
            .setTitle("Внимание!")
            .setMessage("Вы точно хотите выйти?")
            .setPositiveButton("Уверен!", listener)
            .setNegativeButton("Нет", listener)
            .create()

        dialog.show()
    }


    private fun checkGoogleApiAvailability() {
        with(googleApiAvailability) {
            val code = isGooglePlayServicesAvailable(this@AppActivity)
            if (code == ConnectionResult.SUCCESS) {
                return@with
            }
            if (isUserResolvableError(code)) {
                getErrorDialog(this@AppActivity, code, 9000)?.show()
                return
            }
            Toast.makeText(this@AppActivity, R.string.google_play_unavailable, Toast.LENGTH_LONG)
                .show()
        }

        firebaseMessaging.token.addOnSuccessListener {
            println(it)
        }
    }
}