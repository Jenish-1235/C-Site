package com.csite.app.Activites.Library

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.DialogFragment
import com.csite.app.DialogFragments.AddNewWorkforceDialogFragment
import com.csite.app.R

class WorkforceLibraryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workforce_library)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val addNewWorkforceButton: Button = findViewById(R.id.addNewWorkforceButton)
        addNewWorkforceButton.setOnClickListener{
            val addNewWorkforceDialogFragment: DialogFragment = AddNewWorkforceDialogFragment()
            addNewWorkforceDialogFragment.show(supportFragmentManager, "AddNewWorkforceDialogFragment")
        }

    }

    fun backButton(view: View){
        finish()
    }
}