package com.csite.app.Fragments.ProjectInternal

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmpte.app.Objects.TransactionMaterialPurchase
import com.csite.app.Activites.ProjectFeatures.MaterialTab.NewMaterialReceivedActivity
import com.csite.app.Activites.ProjectFeatures.MaterialTab.NewMaterialRequestActivity
import com.csite.app.FirebaseOperations.FirebaseOperationsForProjectInternalMaterialTab
import com.csite.app.FirebaseOperations.FirebaseOperationsForProjectInternalTransactionsTab
import com.csite.app.Objects.InventoryItem
import com.csite.app.Objects.MaterialRequestOrReceived
import com.csite.app.Objects.TransactionIPaid
import com.csite.app.Objects.TransactionIReceived
import com.csite.app.Objects.TransactionOtherExpense
import com.csite.app.Objects.TransactionPaymentIn
import com.csite.app.Objects.TransactionPaymentOut
import com.csite.app.Objects.TransactionSalesInvoice
import com.csite.app.R
import com.csite.app.RecyclerViewListAdapters.InventoryListAdapter
import com.csite.app.RecyclerViewListAdapters.MaterialTabListAdapter
import com.google.android.material.tabs.TabLayout


class ProjectInternalMaterialFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_project_internal_material, container, false)

        val bundle = getArguments()
        var projectId = bundle?.getString("projectId")
        val memberAccess = bundle?.getString("memberAccess")

        val materialRequestButton = view.findViewById<TextView>(R.id.materialRequestButton)
        materialRequestButton.setOnClickListener {
            val newMaterialRequestIntent = Intent(activity, NewMaterialRequestActivity::class.java)
            newMaterialRequestIntent.putExtra("projectId",projectId)
            startActivity(newMaterialRequestIntent)
        }

        val materialReceivedButton = view.findViewById<TextView>(R.id.materialReceivedButton)
        materialReceivedButton.setOnClickListener {
            val newMaterialReceivedIntent = Intent(activity, NewMaterialReceivedActivity::class.java)
            newMaterialReceivedIntent.putExtra("projectId",projectId)
            startActivity(newMaterialReceivedIntent)
        }

        return view
    }

    fun formTabLayout(view: View,projectId:String){
        val tabLayout = view.findViewById<TabLayout>(R.id.materialTabLLayout)
        tabLayout.removeAllTabs()

        val inventoryTab = tabLayout.newTab()
        inventoryTab.setText("Inventory")
        tabLayout.addTab(inventoryTab)

        val requestTab = tabLayout.newTab()
        requestTab.setText("Request")
        tabLayout.addTab(requestTab)

        val receivedTab = tabLayout.newTab()
        receivedTab.setText("Received")
        tabLayout.addTab(receivedTab)


        val tabLayoutAddOnTabSelectedListener = object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val materialTabRecyclerView = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.materialTabRecyclerView)
                materialTabRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
                val firebaseOperationsForProjectInternalMaterialTab = FirebaseOperationsForProjectInternalMaterialTab()
                if(tab?.getPosition() == 1) {
                    firebaseOperationsForProjectInternalMaterialTab.fetchMaterialRequests(projectId,
                        object :
                            FirebaseOperationsForProjectInternalMaterialTab.OnMaterialRequestReceived {
                            override fun onMaterialRequestReceived(materialRequestList: ArrayList<MaterialRequestOrReceived>) {
                                val adapter = MaterialTabListAdapter(materialRequestList, projectId)
                                materialTabRecyclerView.adapter = adapter
                                adapter.notifyDataSetChanged()
                            }
                        })
                }
                else if(tab?.getPosition() == 2){
                    firebaseOperationsForProjectInternalMaterialTab.fetchMaterialReceived(projectId,
                        object : FirebaseOperationsForProjectInternalMaterialTab.OnMaterialReceivedReceived {
                            override fun onMaterialReceivedReceived(materialRequestList: ArrayList<MaterialRequestOrReceived>) {
                                val adapter = MaterialTabListAdapter(materialRequestList, projectId)
                                materialTabRecyclerView.adapter = adapter
                                adapter.notifyDataSetChanged()
                            }
                        })
                }
                else if(tab?.getPosition() == 0){
                    firebaseOperationsForProjectInternalMaterialTab.fetchInventory(projectId, object : FirebaseOperationsForProjectInternalMaterialTab.OnInventoryReceived {
                        override fun onInventoryReceived(inventoryList: ArrayList<InventoryItem>) {
                            val adapter = InventoryListAdapter(inventoryList, projectId)
                            materialTabRecyclerView.adapter = adapter
                            adapter.notifyDataSetChanged()
                        }
                    })
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                val materialTabRecyclerView = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.materialTabRecyclerView)
                materialTabRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
                val firebaseOperationsForProjectInternalMaterialTab = FirebaseOperationsForProjectInternalMaterialTab()

                if(tab?.getPosition() == 1) {
                    firebaseOperationsForProjectInternalMaterialTab.fetchMaterialRequests(projectId,
                        object :
                            FirebaseOperationsForProjectInternalMaterialTab.OnMaterialRequestReceived {
                            override fun onMaterialRequestReceived(materialRequestList: ArrayList<MaterialRequestOrReceived>) {
                                val adapter = MaterialTabListAdapter(materialRequestList, projectId)
                                materialTabRecyclerView.adapter = adapter
                                adapter.notifyDataSetChanged()
                            }
                        })
                }
                else if(tab?.getPosition() == 2) {
                    firebaseOperationsForProjectInternalMaterialTab.fetchMaterialReceived(projectId,
                        object :
                            FirebaseOperationsForProjectInternalMaterialTab.OnMaterialReceivedReceived {
                            override fun onMaterialReceivedReceived(materialRequestList: ArrayList<MaterialRequestOrReceived>) {
                                val adapter = MaterialTabListAdapter(materialRequestList, projectId)
                                materialTabRecyclerView.adapter = adapter
                                adapter.notifyDataSetChanged()
                            }
                        })
                }
                else if (tab?.position == 0){
                    firebaseOperationsForProjectInternalMaterialTab.fetchInventory(projectId, object : FirebaseOperationsForProjectInternalMaterialTab.OnInventoryReceived {
                        override fun onInventoryReceived(inventoryList: ArrayList<InventoryItem>) {
                            val adapter = InventoryListAdapter(inventoryList, projectId)
                            materialTabRecyclerView.adapter = adapter
                            adapter.notifyDataSetChanged()
                        }
                    })
                }
            }

        }
        tabLayout.addOnTabSelectedListener(tabLayoutAddOnTabSelectedListener)
        tabLayout.getTabAt(1)?.select()
        requestTab.select()

    }

    override fun onResume() {
        super.onResume()
        val bundle = getArguments()
        var projectId = bundle?.getString("projectId")
        if (projectId != null) {
            formTabLayout(requireView(), projectId )
            val materialRequestView = view?.findViewById<TextView>(R.id.materialRequestCountView)
            val materialReceivedView = view?.findViewById<TextView>(R.id.materialReceivedCountView)
            val firebaseOperationsForProjectInternalMaterialTab = FirebaseOperationsForProjectInternalMaterialTab()
            val list = ArrayList<MaterialRequestOrReceived>()
            firebaseOperationsForProjectInternalMaterialTab.fetchMaterialRequests(projectId.toString(), object : FirebaseOperationsForProjectInternalMaterialTab.OnMaterialRequestReceived {
                override fun onMaterialRequestReceived(materialRequestList: ArrayList<MaterialRequestOrReceived>) {
                    list.clear()
                    list.addAll(materialRequestList)
                    materialRequestView?.setText(list.size.toString())
                    list.clear()
                }
            })
            val list2 = ArrayList<MaterialRequestOrReceived>()
            firebaseOperationsForProjectInternalMaterialTab.fetchMaterialReceived(projectId.toString(), object : FirebaseOperationsForProjectInternalMaterialTab.OnMaterialReceivedReceived {
                override fun onMaterialReceivedReceived(materialRequestList: ArrayList<MaterialRequestOrReceived>) {
                    list2.clear()
                    list2.addAll(materialRequestList)
                    materialReceivedView?.setText(list2.size.toString())
                    list2.clear()
                }
            })

            val totalPurchasedView = view?.findViewById<TextView>(R.id.materialPurchaseCountView)
            val firebaseOperationsForProjectInternalTransactionsTab = FirebaseOperationsForProjectInternalTransactionsTab()
            firebaseOperationsForProjectInternalTransactionsTab.fetchTransactionsByType(projectId, object: FirebaseOperationsForProjectInternalTransactionsTab.allTransactionFetch{
                override fun onAllTransactionsFetched(
                    transactions: MutableList<TransactionPaymentIn>,
                    paymentOutTransaction: MutableList<TransactionPaymentOut>,
                    otherExpenseTransaction: MutableList<TransactionOtherExpense>,
                    salesInvoiceTransaction: MutableList<TransactionSalesInvoice>,
                    materialPurchaseTransaction: MutableList<TransactionMaterialPurchase>,
                    ipaidTransaction: MutableList<TransactionIPaid>,
                    ireceivedTransaction: MutableList<TransactionIReceived>
                ) {
                    totalPurchasedView?.setText(materialPurchaseTransaction.size.toString())
                }
            })



        }
    }
}