package com.csite.app.Fragments.ProjectInternal

import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.cmpte.app.Objects.TransactionMaterialPurchase
import com.csite.app.FirebaseOperations.FirebaseOperationsForProjectInternalAttendance
import com.csite.app.FirebaseOperations.FirebaseOperationsForProjectInternalMaterialTab
import com.csite.app.FirebaseOperations.FirebaseOperationsForProjectInternalTransactions
import com.csite.app.Objects.MaterialRequestOrReceived
import com.csite.app.Objects.PdfGenerator
import com.csite.app.Objects.ProjectWorker
import com.csite.app.Objects.TransactionOtherExpense
import com.csite.app.Objects.TransactionPaymentIn
import com.csite.app.Objects.TransactionPaymentOut
import com.csite.app.Objects.TransactionSalesInvoice
import com.csite.app.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class ProjectInternalSiteFragment : Fragment(){


    private val firebaseOperationsForProjectInternalMaterialTab = FirebaseOperationsForProjectInternalMaterialTab()
    private val firebaseOperationsForProjectInternalTransactions = FirebaseOperationsForProjectInternalTransactions()
    private val firebaseOperationsForProjectInternalAttendance = FirebaseOperationsForProjectInternalAttendance()

    var materialRequestListForSite = ArrayList<MaterialRequestOrReceived>()
    var attendanceList = ArrayList<ProjectWorker>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val bundle = getArguments()
        var projectId = bundle?.getString("projectId")
        val memberAccess = bundle?.getString("memberAccess")
        val projectName = bundle?.getString("projectName")

        val view =  inflater.inflate(R.layout.fragment_project_internal_site, container, false)

        val createDprButton = view.findViewById<View>(R.id.createDprButton)
        val createTransactionReportButton = view.findViewById<View>(R.id.createTransactionReportButton)

        val yesterdayDateButton = view.findViewById<ImageView>(R.id.yesterdayDateButton)
        val selectedDateView = view.findViewById<TextView>(R.id.currentSelectedDateView)
        val tommorowDateButton = view.findViewById<ImageView>(R.id.tomorrowDateButton)

        yesterdayDateButton.setOnClickListener {
            var selectedDate = selectedDateView.text.toString()
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val date = LocalDate.parse(selectedDate, formatter)
            val yesterday = date.minusDays(1)
            selectedDateView.text = yesterday.format(formatter)
        }

        selectedDateView.text = getTodayDate()

        tommorowDateButton.setOnClickListener {
            var selectedDate = selectedDateView.text.toString()
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val date = LocalDate.parse(selectedDate, formatter)
            val tommorow = date.plusDays(1)
            if(tommorow.isAfter(LocalDate.now())){
                Toast.makeText(context, "Can't select future date", Toast.LENGTH_SHORT).show()
            }else{
                selectedDateView.text = tommorow.format(formatter)
            }
        }


        var count = 0
        createDprButton.setOnClickListener {
            materialRequestListForSite.clear()
            var selectedDate = selectedDateView.text.toString()
            var filepath = Environment.getExternalStorageDirectory().absolutePath.toString() + "/Download/DPR${selectedDate}_${count}.pdf"
            if (projectId != null) {
                firebaseOperationsForProjectInternalMaterialTab.fetchMaterialRequests(projectId, object : FirebaseOperationsForProjectInternalMaterialTab.OnMaterialRequestReceived{
                    override fun onMaterialRequestReceived(materialRequestList: ArrayList<MaterialRequestOrReceived>) {

                        firebaseOperationsForProjectInternalAttendance.getWorkersForExistingAttendance(projectId, selectedDate, object : FirebaseOperationsForProjectInternalAttendance.OnAttendanceWorkerFetched{
                            override fun onAttendanceWorkerFetched(workersList: ArrayList<ProjectWorker>) {

                                for (materialRequest in materialRequestList){
                                    if (materialRequest.dateTimeStamp.substring(0, 10) == selectedDate){
                                        materialRequestListForSite.add(materialRequest)
                                    }
                                }
                                PdfGenerator.generateDPR(filepath, materialRequestListForSite, workersList, projectName, selectedDate)
                                count++
                                Toast.makeText(context, "DPR created", Toast.LENGTH_SHORT).show()
                            }
                        })

                    }
                })
            }
        }

        createTransactionReportButton.setOnClickListener {
            var selectedDate = selectedDateView.text.toString()
            // create transaction report here.
            var count = 0
            var filepath = Environment.getExternalStorageDirectory().absolutePath.toString() + "/Download/TransactionReport${selectedDate}_${count}.pdf"
            if (projectId != null) {
                firebaseOperationsForProjectInternalTransactions.fetchTransactionsByType(projectId, object : FirebaseOperationsForProjectInternalTransactions.allTransactionFetch{
                    override fun onAllTransactionsFetched(
                        transactions: MutableList<TransactionPaymentIn>,
                        paymentOutTransaction: MutableList<TransactionPaymentOut>,
                        otherExpenseTransaction: MutableList<TransactionOtherExpense>,
                        salesInvoiceTransaction: MutableList<TransactionSalesInvoice>,
                        materialPurchaseTransaction: MutableList<TransactionMaterialPurchase>
                    ) {


                        if (projectName != null) {
                            PdfGenerator.generateTransactionReport(filepath, transactions, paymentOutTransaction, otherExpenseTransaction, salesInvoiceTransaction, materialPurchaseTransaction, selectedDate, projectName)
                        }
                        count++
                    }
                })
            }
        }
        return view
    }


    private fun getTodayDate():String {
        val current = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return current.format(formatter)
    }

}