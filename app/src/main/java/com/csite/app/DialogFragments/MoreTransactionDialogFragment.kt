package com.csite.app.DialogFragments

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.csite.app.Activites.ProjectFeatures.TransactionTab.NewIPaidTransactionActivity
import com.csite.app.Activites.ProjectFeatures.TransactionTab.NewIReceivedTransactionActivity
import com.csite.app.Activites.ProjectFeatures.TransactionTab.NewMaterialPurchaseTransactionActivity
import com.csite.app.Activites.ProjectFeatures.TransactionTab.NewOtherExpenseTransactionActivity
import com.csite.app.Activites.ProjectFeatures.TransactionTab.NewPaymentInTransactionActivity
import com.csite.app.Activites.ProjectFeatures.TransactionTab.NewPaymentOutTransactionActivity
import com.csite.app.Activites.ProjectFeatures.TransactionTab.NewSalesInvoiceTransactionActivity
import com.csite.app.R
import com.csite.app.databinding.DialogFragmentMoreTransactionBinding


class MoreTransactionDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.dialog_fragment_more_transaction, container, false)
        val dialog = getDialog()
        if (dialog != null) {
            dialog.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent)
        }

        val projectId = arguments?.getString("projectId")
//        Toast.makeText(requireActivity(), "projectId: $projectId", Toast.LENGTH_SHORT).show()

        val binding = DialogFragmentMoreTransactionBinding.bind(view)
        binding.paymentInButton.setOnClickListener {
            val newPaymentInTransactionIntent = Intent(context, NewPaymentInTransactionActivity::class.java)
            newPaymentInTransactionIntent.putExtra("projectId", projectId)
            startActivity(newPaymentInTransactionIntent)
            dismiss()
        }

        binding.paymentOutButton.setOnClickListener {
            val newPaymentOutTransactionIntent = Intent(context, NewPaymentOutTransactionActivity::class.java)
            newPaymentOutTransactionIntent.putExtra("projectId", projectId)
            startActivity(newPaymentOutTransactionIntent)
            dismiss()
        }

        binding.salesInvoiceButton.setOnClickListener{
            val salesInvoiceIntent = Intent(context, NewSalesInvoiceTransactionActivity::class.java)
            salesInvoiceIntent.putExtra("projectId", projectId)
            startActivity(salesInvoiceIntent)
            dismiss()
        }

        binding.otherExpenseButton.setOnClickListener{
            val otherExpenseIntent = Intent(context, NewOtherExpenseTransactionActivity::class.java)
            otherExpenseIntent.putExtra("projectId", projectId)
            startActivity(otherExpenseIntent)
            dismiss()
        }

        binding.materialPurchaseButton.setOnClickListener{
            val materialPurchaseIntent = Intent(context, NewMaterialPurchaseTransactionActivity::class.java)
            materialPurchaseIntent.putExtra("projectId", projectId)
            startActivity(materialPurchaseIntent)
            dismiss()
        }

        binding.iPaidButton.setOnClickListener{
            val iPaidIntent = Intent(context, NewIPaidTransactionActivity::class.java)
            iPaidIntent.putExtra("projectId", projectId)
            startActivity(iPaidIntent)
            dismiss()
        }

        binding.iReceivedButton.setOnClickListener{
            val iReceivedIntent = Intent(context, NewIReceivedTransactionActivity::class.java)
            iReceivedIntent.putExtra("projectId", projectId)
            startActivity(iReceivedIntent)
            dismiss()
        }


        return view

    }

    // sets positioning of dialog fragment to bottom of screen.
    override fun onStart() {
        super.onStart()

        val dialog: Dialog? = getDialog()

        if (dialog != null){
            val window = dialog.getWindow()
            if (window != null){
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                window.setGravity(Gravity.BOTTOM)

                val params = window.getAttributes()
                params.width = WindowManager.LayoutParams.MATCH_PARENT
                params.height = WindowManager.LayoutParams.WRAP_CONTENT
                params.horizontalMargin = 0f;
                params.verticalMargin = 0f;

                window.setWindowAnimations(R.style.DialogAnimation)
            }
        }
    }

}