package com.csite.app.Fragments.MainScreen

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.csite.app.Activites.MainScreen.NewQuotationActivity
import com.csite.app.R

class QuotationFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_quotation, container, false)
        val addQuotationButton : Button = view.findViewById(R.id.addQuotationButton)
        addQuotationButton.setOnClickListener {
            val intent = Intent(requireContext(), NewQuotationActivity::class.java)
            startActivity(intent)
        }



        return view
    }
}