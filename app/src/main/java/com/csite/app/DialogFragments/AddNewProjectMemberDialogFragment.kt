package com.csite.app.DialogFragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.csite.app.FirebaseOperations.FirebaseOperationsForProjects
import com.csite.app.Objects.Member
import com.csite.app.R

class AddNewProjectMemberDialogFragment : DialogFragment() {

    private lateinit var nameField: EditText
    private lateinit var mobileField: EditText
    private val PICK_CONTACT_REQUEST = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.dialog_fragment_add_new_project_member, container, false)
        val dialog = getDialog()
        if (dialog != null) {
            dialog.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent)
        }

        nameField = view.findViewById(R.id.memberNameInput)
        mobileField = view.findViewById(R.id.memberNumberInput)

        // Open contacts picker on mobile field click
        mobileField.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_CONTACTS), PICK_CONTACT_REQUEST)
            }
            val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
            startActivityForResult(intent, PICK_CONTACT_REQUEST)
        }

        val memberRoleView = view.findViewById<AutoCompleteTextView>(R.id.memberRoleView)
        val memberRoles = arrayOf("admin", "manager", "super admin")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, memberRoles)
        memberRoleView.setAdapter(adapter)

        val addMemberButton = view.findViewById<Button>(R.id.addMemberButton)
        addMemberButton.setOnClickListener {
            val name = nameField.text.toString()
            val mobile = mobileField.text.toString()
            val role = memberRoleView.text.toString()
            if (name.isNotEmpty() && mobile.isNotEmpty() && role.isNotEmpty()) {
                // Add member to project
                val firebaseOperationsForProjects = FirebaseOperationsForProjects()
                val member = Member(name, mobile, role)
                firebaseOperationsForProjects.addProjectMember(member, projectId = requireActivity().intent.getStringExtra("projectId").toString())
                dismiss()
            }
        }

        return view
    }

    @SuppressLint("Range")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_CONTACT_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.data?.let { contactUri ->
                // Get the contact name
                val cursor = requireActivity().contentResolver.query(contactUri, null, null, null, null)
                cursor?.use {
                    if (it.moveToFirst()) {
                        val nameIndex = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                        val contactName = it.getString(nameIndex)

                        // Get the contact ID to retrieve the mobile number
                        val idIndex = it.getColumnIndex(ContactsContract.Contacts._ID)

                        val contactId = it.getString(it.getColumnIndex(ContactsContract.Contacts._ID))
                        val phoneCursor = requireActivity().contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
                            arrayOf(contactId),
                            null
                        )

                        var contactNumber: String? = null
                        phoneCursor?.use { phone ->
                            if (phone.moveToFirst()) {
                                val numberIndex = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                                contactNumber = phone.getString(numberIndex)

                            }
                        }

                        // Update fields in dialog with selected contact info

                        nameField.setText(contactName)
                        if(contactNumber?.length!! > 10){
                            val finalNumber = contactNumber?.substring(3)

                            mobileField.setText(finalNumber ?: "No number found")
                        }
                        else{
                            mobileField.setText(contactNumber ?: "No number found")
                        }
                    }
                }
            }
        }
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