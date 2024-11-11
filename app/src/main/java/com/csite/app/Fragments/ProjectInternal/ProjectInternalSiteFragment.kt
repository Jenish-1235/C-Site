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
import com.csite.app.FirebaseOperations.FirebaseOperationsForProjectInternalAttendanceTab
import com.csite.app.FirebaseOperations.FirebaseOperationsForProjectInternalMaterialTab
import com.csite.app.FirebaseOperations.FirebaseOperationsForProjectInternalTransactionsTab
import com.csite.app.Objects.Contractor
import com.csite.app.Objects.MaterialRequestOrReceived
import com.csite.app.Objects.PdfGenerator
import com.csite.app.Objects.TransactionIPaid
import com.csite.app.Objects.TransactionIReceived
import com.csite.app.Objects.TransactionOtherExpense
import com.csite.app.Objects.TransactionPaymentIn
import com.csite.app.Objects.TransactionPaymentOut
import com.csite.app.Objects.TransactionSalesInvoice
import com.csite.app.Objects.Workforce
import com.csite.app.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class ProjectInternalSiteFragment : Fragment(){


    private val firebaseOperationsForProjectInternalMaterialTab = FirebaseOperationsForProjectInternalMaterialTab()
    private val firebaseOperationsForProjectInternalTransactionsTab = FirebaseOperationsForProjectInternalTransactionsTab()

    var materialRequestListForSite = ArrayList<MaterialRequestOrReceived>()

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
            updateUI(view, projectId.toString())
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
                updateUI(view, projectId.toString())
            }
        }

        updateUI(view, projectId.toString())

        var count = 0
        createDprButton.setOnClickListener {
            materialRequestListForSite.clear()
            var selectedDate = selectedDateView.text.toString()
            var filepath = Environment.getExternalStorageDirectory().absolutePath.toString() + "/Download/DPR${selectedDate}_${count}.pdf"
            if (projectId != null) {
                firebaseOperationsForProjectInternalMaterialTab.fetchMaterialRequests(projectId, object : FirebaseOperationsForProjectInternalMaterialTab.OnMaterialRequestReceived{
                    override fun onMaterialRequestReceived(materialRequestList: ArrayList<MaterialRequestOrReceived>) {
                        // TODO: Send ATTENDANCE And Material DATA FOR DPR
                        val materialRequestListForSite = materialRequestList
                        val firebaseOperationsForProjectInternalAttendanceTab = FirebaseOperationsForProjectInternalAttendanceTab()
                        firebaseOperationsForProjectInternalAttendanceTab.fetchContractorListFromAttendance(projectId, selectedDate, object: FirebaseOperationsForProjectInternalAttendanceTab.getAttendanceContractorList{
                            override fun onAttendanceContractorListReceived(contractorList: HashMap<String, Contractor>) {
                                val workforceHashMap = HashMap<String, ArrayList<Workforce>>()
                                var size = 0
                                for (contractor in contractorList){
                                    val workforceList = ArrayList<Workforce>()
                                    for (workforce in contractor.value.contractorWorkforce.values){
                                        workforceList.add(workforce)
                                        size++
                                    }
                                    workforceHashMap[contractor.value.contractorName] = workforceList
                                }
                                PdfGenerator.generateDPR(filepath, materialRequestListForSite, workforceHashMap, projectName, selectedDate, size)
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
                firebaseOperationsForProjectInternalTransactionsTab.fetchTransactionsByType(projectId, object : FirebaseOperationsForProjectInternalTransactionsTab.allTransactionFetch{
                    override fun onAllTransactionsFetched(
                        transactions: MutableList<TransactionPaymentIn>,
                        paymentOutTransaction: MutableList<TransactionPaymentOut>,
                        otherExpenseTransaction: MutableList<TransactionOtherExpense>,
                        salesInvoiceTransaction: MutableList<TransactionSalesInvoice>,
                        materialPurchaseTransaction: MutableList<TransactionMaterialPurchase>,
                        iPaidTransaction: MutableList<TransactionIPaid>,
                        iReceiveTransaction: MutableList<TransactionIReceived>
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

    fun updateUI(view: View, projectId:String){
        val presentCount = view.findViewById<TextView>(R.id.labourCount)
        val materialReceivedView = view.findViewById<TextView>(R.id.recievedCount)
        val selectedDateView = view.findViewById<TextView>(R.id.currentSelectedDateView)

        firebaseOperationsForProjectInternalMaterialTab.fetchMaterialReceived(projectId, object : FirebaseOperationsForProjectInternalMaterialTab.OnMaterialReceivedReceived{
            override fun onMaterialReceivedReceived(materialReceivedList: ArrayList<MaterialRequestOrReceived>) {
                var count = 0
                for(material in materialReceivedList){
                    if(material.dateTimeStamp.substring(0, 10) == selectedDateView.text.toString()){
                        count++
                    }
                }
                materialReceivedView.text = count.toString()
            }
        })

        // TODO: update the labour count

    }

}