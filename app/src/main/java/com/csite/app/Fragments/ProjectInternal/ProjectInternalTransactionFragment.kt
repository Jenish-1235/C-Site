package com.csite.app.Fragments.ProjectInternal

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cmpte.app.Objects.TransactionMaterialPurchase
import com.csite.app.Activites.ProjectFeatures.TransactionTab.NewPaymentInTransactionActivity
import com.csite.app.Activites.ProjectFeatures.TransactionTab.NewPaymentOutTransactionActivity
import com.csite.app.DialogFragments.MoreTransactionDialogFragment
import com.csite.app.FirebaseOperations.FirebaseOperationsForProjectInternalTransactions
import com.csite.app.Objects.TransactionOtherExpense
import com.csite.app.Objects.TransactionPaymentIn
import com.csite.app.Objects.TransactionPaymentOut
import com.csite.app.Objects.TransactionSalesInvoice
import com.csite.app.R
import com.csite.app.RecyclerViewListAdapters.TransactionMaterialPurchaseListAdapter
import com.csite.app.RecyclerViewListAdapters.TransactionOtherExpenseListAdapter
import com.csite.app.RecyclerViewListAdapters.TransactionPaymentInListAdapter
import com.csite.app.RecyclerViewListAdapters.TransactionPaymentOutListAdapter
import com.csite.app.RecyclerViewListAdapters.TransactionSalesInvoiceListAdapter
import com.csite.app.databinding.FragmentProjectInternalTransactionBinding
import com.google.android.material.tabs.TabLayout

class ProjectInternalTransactionFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_project_internal_transaction, container, false)

        val binding = FragmentProjectInternalTransactionBinding.bind(view)

        val bundle = getArguments()
        var projectId = bundle?.getString("projectId")
        val memberAccess = bundle?.getString("memberAccess")

        if (!memberAccess.equals("manager")){
            binding.projectPaymentInButton.setOnClickListener{
                val newPaymentInTransactionIntent = Intent(activity, NewPaymentInTransactionActivity::class.java)
                newPaymentInTransactionIntent.putExtra("projectId", projectId)
                startActivity(newPaymentInTransactionIntent)
            }
            binding.projectPaymentOutButton.setOnClickListener{
                val newPaymentOutTransactionIntent = Intent(activity, NewPaymentOutTransactionActivity::class.java)
                newPaymentOutTransactionIntent.putExtra("projectId", projectId)
                startActivity(newPaymentOutTransactionIntent)
            }

            binding.projectMoreTransactionButton.setOnClickListener{
                val moreTransactionDialogFragment = MoreTransactionDialogFragment()
                val bundle = Bundle()
                bundle.putString("projectId", projectId)
                moreTransactionDialogFragment.arguments = bundle
                moreTransactionDialogFragment.show(childFragmentManager, "moreTransactionDialogFragment")
            }
            transactionTabLayout(view, projectId!!)



        }else{
            // Handle Manager Role
        }



        return view
    }


    fun transactionTabLayout(view:View, projectId:String){
        val transactionTabLayout:TabLayout = view.findViewById(R.id.projectTransactionFilterTabLayout)

        transactionTabLayout.addTab(transactionTabLayout.newTab().setText("Payment In"))
        transactionTabLayout.addTab(transactionTabLayout.newTab().setText("Payment Out"))
        transactionTabLayout.addTab(transactionTabLayout.newTab().setText("Sales Invoice"))
        transactionTabLayout.addTab(transactionTabLayout.newTab().setText("Material Purchase"))
        transactionTabLayout.addTab(transactionTabLayout.newTab().setText("Other Expense"))
        val transactionRecyclerView = view.findViewById<RecyclerView>(R.id.projectTransactionRecyclerView)
        FirebaseOperationsForProjectInternalTransactions().fetchPaymentInTransactions(projectId, object : FirebaseOperationsForProjectInternalTransactions.OnPaymentInTransactionsReceived{
            override fun onPaymentInTransactionsReceived(paymentInTransactions: MutableList<TransactionPaymentIn>) {
                transactionRecyclerView.adapter = TransactionPaymentInListAdapter(paymentInTransactions)
                transactionRecyclerView.adapter?.notifyDataSetChanged()
                transactionRecyclerView.layoutManager = LinearLayoutManager(activity)
            }
        })
        transactionTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    when (tab.position) {
                        0 -> {
                            val transactionRecyclerView = view.findViewById<RecyclerView>(R.id.projectTransactionRecyclerView)
                            FirebaseOperationsForProjectInternalTransactions().fetchPaymentInTransactions(projectId, object : FirebaseOperationsForProjectInternalTransactions.OnPaymentInTransactionsReceived{
                                override fun onPaymentInTransactionsReceived(paymentInTransactions: MutableList<TransactionPaymentIn>) {
                                    transactionRecyclerView.adapter = TransactionPaymentInListAdapter(paymentInTransactions)
                                    transactionRecyclerView.adapter?.notifyDataSetChanged()
                                    transactionRecyclerView.layoutManager = LinearLayoutManager(activity)
                                }
                            })
                        }
                        1 -> {
                            val transactionRecyclerView = view.findViewById<RecyclerView>(R.id.projectTransactionRecyclerView)
                            FirebaseOperationsForProjectInternalTransactions().fetchPaymentOutTransactions(projectId, object : FirebaseOperationsForProjectInternalTransactions.OnPaymentOutTransactionsReceived{
                                override fun onPaymentOutTransactionsReceived(paymentOutTransactions: MutableList<TransactionPaymentOut>) {
                                    transactionRecyclerView.adapter = TransactionPaymentOutListAdapter(paymentOutTransactions)
                                    transactionRecyclerView.adapter?.notifyDataSetChanged()
                                    transactionRecyclerView.layoutManager = LinearLayoutManager(activity)
                                }
                            })
                        }
                        2 -> {
                            val transactionRecyclerView = view.findViewById<RecyclerView>(R.id.projectTransactionRecyclerView)
                            FirebaseOperationsForProjectInternalTransactions().fetchSalesInvoiceTransactions(projectId, object : FirebaseOperationsForProjectInternalTransactions.OnSalesInvoiceTransactionsReceived{
                                override fun onSalesInvoiceTransactionsReceived(
                                    salesInvoiceTransactions: MutableList<TransactionSalesInvoice>
                                ) {
                                    transactionRecyclerView.adapter = TransactionSalesInvoiceListAdapter(salesInvoiceTransactions)
                                    transactionRecyclerView.adapter?.notifyDataSetChanged()
                                    transactionRecyclerView.layoutManager = LinearLayoutManager(activity)
                                }
                            })
                        }
                        3 -> {
                            val transactionRecyclerView = view.findViewById<RecyclerView>(R.id.projectTransactionRecyclerView)
                            FirebaseOperationsForProjectInternalTransactions().fetchMaterialPurchaseTransactions(projectId, object : FirebaseOperationsForProjectInternalTransactions.OnMaterialPurchaseTransactionsReceived{
                                override fun onMaterialPurchaseTransactionsReceived(materialPurchaseTransactionList: MutableList<TransactionMaterialPurchase>){
                                    transactionRecyclerView.adapter = TransactionMaterialPurchaseListAdapter(materialPurchaseTransactionList)
                                    transactionRecyclerView.adapter?.notifyDataSetChanged()
                                    transactionRecyclerView.layoutManager = LinearLayoutManager(activity)
                                }
                            })
                        }
                        4 -> {
                            val transactionRecyclerView = view.findViewById<RecyclerView>(R.id.projectTransactionRecyclerView)
                            FirebaseOperationsForProjectInternalTransactions().fetchOtherExpenseTransactions(projectId, object : FirebaseOperationsForProjectInternalTransactions.OnOtherExpenseTransactionsReceived{
                                override fun onOtherExpenseTransactionsReceived(
                                    otherExpenseTransactions: MutableList<TransactionOtherExpense>
                                ) {
                                    transactionRecyclerView.adapter = TransactionOtherExpenseListAdapter(otherExpenseTransactions)
                                    transactionRecyclerView.adapter?.notifyDataSetChanged()
                                    transactionRecyclerView.layoutManager = LinearLayoutManager(activity)
                                }
                            })
                        }

                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })

        transactionTabLayout.selectTab(transactionTabLayout.getTabAt(0))

    }

}