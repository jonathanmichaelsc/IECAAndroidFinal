package com.example.firebaseloginkotlin

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.iecaandroid.databinding.ActivityAccountRecoveryBinding
import com.google.firebase.auth.FirebaseAuth

class AccountRecoveryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountRecoveryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAccountRecoveryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.senEmailAppCompatButton.setOnClickListener {
            val emailAddress = binding.emailEditText.text.toString()

            if (emailAddress.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
                FirebaseAuth.getInstance().sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val intent = Intent(this, SignInActivity::class.java)
                            startActivity(intent)
                            finish() // Terminar la actividad actual después de iniciar la actividad de inicio de sesión
                        } else {
                            Toast.makeText(
                                this,
                                "No se pudo enviar el correo de recuperación. Asegúrese de que el correo electrónico esté registrado.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(
                    this, "Ingrese un email válido.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }
}
