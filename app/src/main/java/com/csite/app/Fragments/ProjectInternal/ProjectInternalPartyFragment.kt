package com.csite.app.Fragments.ProjectInternal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csite.app.FirebaseOperations.FirebaseOperationsForLibrary
import com.csite.app.FirebaseOperations.FirebaseOperationsForProjectInternalTransactions
import com.csite.app.Objects.CommonTransaction
import com.csite.app.Objects.Party
import com.csite.app.R
import com.csite.app.RecyclerViewListAdapters.PaymentsListAdapter
import java.util.HashMap

class ProjectInternalPartyFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_project_internal_party, container, false)

        val bundle = getArguments()
        var projectId = bundle?.getString("projectId")
        val memberAccess = bundle?.getString("memberAccess")

        val partyRecyclerView = view.findViewById<RecyclerView>(R.id.partiesInternalRecyclerView)
        partyRecyclerView.layoutManager = LinearLayoutManager(requireActivity())

        val firebaseOperationsForLibrary = FirebaseOperationsForLibrary()


        firebaseOperationsForLibrary.fetchPartyFromPartyLibrary(object : FirebaseOperationsForLibrary.onPartyListReceived {
            override fun onPartyListReceived(partyList: ArrayList<Party>) {
                val paymentsHashMap = HashMap<String, Double>()
                val firebaseOperationsForProjectInternalTransactions =
                    FirebaseOperationsForProjectInternalTransactions()
                if (projectId != null) {
                    firebaseOperationsForProjectInternalTransactions.fetchAllTransactions(
                        projectId,
                        object :
                            FirebaseOperationsForProjectInternalTransactions.OnTransactionsFetched {
                            override fun onTransactionsFetched(transactions: MutableList<CommonTransaction>) {
                                for (party in partyList) {
                                    if (transactions.size > 0) {
                                        for (transaction in transactions) {
                                            if (transaction.transactionParty == party.partyName) {
                                                if (paymentsHashMap.containsKey(party.partyName)) {
                                                    paymentsHashMap.put(
                                                        party.partyName,
                                                        paymentsHashMap.get(party.partyName)!! + transaction.transactionAmount.toDouble()
                                                    )
                                                } else {
                                                    paymentsHashMap.put(
                                                        party.partyName,
                                                        transaction.transactionAmount.toDouble()
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                val partyAdapter = PaymentsListAdapter(paymentsHashMap)
                                partyRecyclerView.adapter = partyAdapter
                                partyAdapter.notifyDataSetChanged()

                            }
                        },
                        object : FirebaseOperationsForProjectInternalTransactions.OnCalculated {
                            override fun onCalculated(calculations: ArrayList<String>) {

                            }
                        })
                }
            }
        })
        return view
    }

}