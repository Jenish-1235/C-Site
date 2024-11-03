package com.csite.app.Fragments.ProjectInternal

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.csite.app.Activites.ProjectFeatures.TransactionTab.NewPaymentInTransactionActivity
import com.csite.app.Activites.ProjectFeatures.TransactionTab.NewPaymentOutTransactionActivity
import com.csite.app.DialogFragments.MoreTransactionDialogFragment
import com.csite.app.R
import com.csite.app.databinding.FragmentProjectInternalTransactionBinding

class ProjectInternalTransactionFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_project_internal_transaction, container, false)

        val binding = FragmentProjectInternalTransactionBinding.bind(view)

        val bundle = getArguments()
        val projectId = bundle?.getString("projectId")
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
        }else{
            // Handle Manager Role
        }



        return view
    }

}