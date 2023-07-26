package com.example.firebaseloginkotlin

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.iecaandroid.databinding.ActivityDeleteAccountBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class DeleteAccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeleteAccountBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDeleteAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        binding.deleteAccountAppCompatButton.setOnClickListener {
            val password = binding.passwordEditText.text.toString()
            if (password.isNotEmpty()) {
                showConfirmationDialog(password)
            } else {
                Toast.makeText(
                    this, "Por favor ingresa tu contraseña para eliminar la cuenta.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun showConfirmationDialog(password: String) {
        AlertDialog.Builder(this)
            .setTitle("Confirmar Eliminación de Cuenta")
            .setMessage("¿Estás seguro de que deseas eliminar tu cuenta? Esta acción no se puede deshacer.")
            .setPositiveButton("Eliminar") { dialogInterface: DialogInterface, i: Int ->
                deleteAccount(password)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun deleteAccount(password: String) {
        val user = auth.currentUser

        if (user != null) {
            val email = user.email
            val credential = EmailAuthProvider
                .getCredential(email!!, password)

            user.reauthenticate(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        user.delete()
                            .addOnCompleteListener { taskDeleteAcount ->
                                if (taskDeleteAcount.isSuccessful) {
                                    Toast.makeText(
                                        this, "Se eliminó tu cuenta correctamente.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    signOut()
                                } else {
                                    Toast.makeText(
                                        this, "Ocurrió un error al eliminar la cuenta. Inténtalo nuevamente más tarde.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                    } else {
                        Toast.makeText(
                            this, "La contraseña ingresada es incorrecta.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun signOut() {
        auth.signOut()
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish() // Cerrar la actividad actual después de abrir la SignInActivity.
    }
}
