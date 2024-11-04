package com.csite.app.Fragments.MainScreen

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csite.app.Activites.MainScreen.NewQuotationActivity
import com.csite.app.FirebaseOperations.FirebaseOperationsForQuotations
import com.csite.app.R
import com.csite.app.RecyclerViewListAdapters.QuotationListAdapter
import com.site.app.Objects.Quotation

class QuotationFragment : Fragment() {



    lateinit var quotationRecyclerView: RecyclerView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_quotation, container, false)
        val addQuotationButton : Button = view.findViewById(R.id.addQuotationButton)
        addQuotationButton.setOnClickListener {
            val intent = Intent(requireContext(), NewQuotationActivity::class.java)
            startActivity(intent)
        }
        quotationRecyclerView = view.findViewById(R.id.quotationRecyclerView)



        return view
    }

    override fun onResume() {
        super.onResume()
        var adapter = QuotationListAdapter(ArrayList<Quotation>())
        quotationRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        val firebaseOperationsForQuotations = FirebaseOperationsForQuotations()
        firebaseOperationsForQuotations.fetchQuotationForFirebase(object : FirebaseOperationsForQuotations.OnQuotationFetchedListener {
            override fun onQuotationFetched(quotationList: ArrayList<Quotation>) {
                adapter = QuotationListAdapter(quotationList)
                quotationRecyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        })

    }
}