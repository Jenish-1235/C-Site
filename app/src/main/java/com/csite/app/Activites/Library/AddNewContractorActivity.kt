package com.csite.app.Activites.Library

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.csite.app.DialogFragments.CreateNewWorkforceDialogFragment
import com.csite.app.R

class AddNewContractorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_contractor)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val newWorkforceButton:Button = findViewById(R.id.addNewWorkforceButton)
        newWorkforceButton.setOnClickListener {
            val newWorkforceDialogFragment = CreateNewWorkforceDialogFragment()
            newWorkforceDialogFragment.show(supportFragmentManager, "CreateNewWorkforceDialogFragment")
        }


        val saveContractorButton:Button = findViewById(R.id.saveContractorButton)
        saveContractorButton.setOnClickListener{
            finish()
        }
    }

    fun backButton(view: View){
        finish()
    }
}