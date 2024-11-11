package com.csite.app.Fragments.ProjectInternal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csite.app.FirebaseOperations.FirebaseOperationsForLibrary
import com.csite.app.FirebaseOperations.FirebaseOperationsForProjectInternalTransactionsTab
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
                val firebaseOperationsForProjectInternalTransactionsTab =
                    FirebaseOperationsForProjectInternalTransactionsTab()
                if (projectId != null) {
                    firebaseOperationsForProjectInternalTransactionsTab.fetchAllTransactions(
                        projectId,
                        object :
                            FirebaseOperationsForProjectInternalTransactionsTab.OnTransactionsFetched {
                            override fun onTransactionsFetched(transactions: MutableList<CommonTransaction>) {
                                for (party in partyList) {
                                    var amount = 0.0
                                    for (transaction in transactions) {
                                        if (transaction.transactionParty == party.partyName) {
                                            if (transaction.transactionType == "Payment In"){
                                                amount += transaction.transactionAmount.toDouble()
                                            }else if (transaction.transactionType == "Payment Out"){
                                                amount -= transaction.transactionAmount.toDouble()
                                            }else if (transaction.transactionType == "Other Expense"){
                                                amount -= transaction.transactionAmount.toDouble()
                                            }else if (transaction.transactionType == "Material Purchase"){
                                                amount -= transaction.transactionAmount.toDouble()
                                            }else if (transaction.transactionType == "Sales Invoice"){
                                                amount += transaction.transactionAmount.toDouble()
                                            }else if (transaction.transactionType == "I Paid") {
                                                amount += transaction.transactionAmount.toDouble()
                                            }else if (transaction.transactionType == "I Received") {
                                                amount -= transaction.transactionAmount.toDouble()
                                            }
                                        }
                                    }
                                    paymentsHashMap[party.partyName] = amount
                                    var adapter = PaymentsListAdapter(paymentsHashMap)
                                    partyRecyclerView.adapter = adapter
                                    partyRecyclerView.setHasFixedSize(true)
                                    partyRecyclerView.adapter?.notifyDataSetChanged()

                                    var count = 0
                                    for(amount in paymentsHashMap.values){
                                        if (amount != 0.0){
                                            count++
                                        }
                                    }
                                    val partyMemberCount = view.findViewById<TextView>(R.id.teamMembersValue)
                                    partyMemberCount.text = count.toString() + " Team Members"

                                }

                                val partyAdapter = PaymentsListAdapter(paymentsHashMap)
                                partyRecyclerView.adapter = partyAdapter
                                partyAdapter.notifyDataSetChanged()

                            }
                        },
                        object : FirebaseOperationsForProjectInternalTransactionsTab.OnCalculated {
                            override fun onCalculated(calculations: ArrayList<String>) {

                            }
                        })
                }
            }
        })
        return view
    }

}