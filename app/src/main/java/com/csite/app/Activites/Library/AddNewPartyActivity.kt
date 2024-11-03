package com.csite.app.Activites.Library

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.csite.app.FirebaseOperations.FirebaseOperationsForLibrary
import com.csite.app.Objects.Party
import com.csite.app.R
import com.csite.app.databinding.ActivityAddNewPartyBinding

class AddNewPartyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNewPartyBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewPartyBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Toast.makeText(this, "Binding Successfull ${binding.addNewPartyTitle.text}" , Toast.LENGTH_SHORT).show()


        binding.partyHaveOpeningBalanceCheckBox.setOnCheckedChangeListener{
            _, isChecked ->
            if(isChecked){
                binding.partyOpeningBalanceAmountInput.setText("0")
                binding.partyOpeningBalanceDetailsRadioGroup.clearCheck()
                binding.partyOpeningBalanceLinearLayout.visibility = View.GONE
            }else{
                binding.partyOpeningBalanceLinearLayout.visibility = View.VISIBLE
            }
        }

        binding.partyHaveGSTCheckBox.setOnCheckedChangeListener{
                _, isChecked ->
            if (isChecked){
                binding.partyGSTNumberInput.setText("")
                binding.partyGSTLegalBusinessName.setText("")
                binding.partyGSTStateOfSupplyInput.setText("")
                binding.partyGSTBillingAddress.setText("")
                binding.partyGSTStateOfSupplyInput.setText("")
                binding.partyGSTDetailsLinearLayout.visibility = View.GONE

            }else{
                binding.partyGSTDetailsLinearLayout.visibility = View.VISIBLE
            }
        }

        binding.partyHaveBankDetailsCheckBox.setOnCheckedChangeListener{
            _, isChecked ->
            if (isChecked){
                binding.partyBankDetailsLinearLayout.visibility = View.GONE
                binding.partyUPIIdInput.setText("")
                binding.partyBankNameInput.setText("")
                binding.partyIFSCCodeInput.setText("")
                binding.partyBankAddresssInput.setText("")
                binding.partyIBANNumberInput.setText("")
                binding.partyAccountNumberInput.setText("")
                binding.partyAccountHolderNameInput.setText("")
            }else{
                binding.partyBankDetailsLinearLayout.visibility = View.VISIBLE
            }
        }


        val partyType: List<String> = mutableListOf(
            "Client",
            "Staff",
            "Labour",
            "Material Supplier",
            "Labour Contractor",
            "Equipment Supplier",
            "Sub Contractor"
        )
        binding.partyTypeInput.setAdapter<ArrayAdapter<String>>(
            ArrayAdapter<String>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                partyType
            )
        )

        val stateOfSupply: List<String> = mutableListOf(
            "Andhra Pradesh",
            "Arunachal Pradesh",
            "Assam",
            "Bihar",
            "Chhattisgarh",
            "Goa",
            "Gujarat",
            "Haryana",
            "Himachal Pradesh",
            "Jharkhand",
            "Karnataka",
            "Kerala",
            "Maharashtra",
            "Madhya Pradesh",
            "Manipur",
            "Meghalaya",
            "Mizoram",
            "Nagaland",
            "Odisha",
            "Punjab",
            "Rajasthan",
            "Sikkim",
            "Tamil Nadu",
            "Tripura",
            "Telangana",
            "Uttar Pradesh",
            "Uttarakhand",
            "West Bengal",
            "Andaman & Nicobar (UT)",
            "Chandigarh (UT)",
            "Dadra & Nagar Haveli and Daman & Diu (UT)",
            "Delhi [National Capital Territory (NCT)]",
            "Jammu & Kashmir (UT)",
            "Ladakh (UT)",
            "Lakshadweep (UT)",
            "Pondicherry (UT)"
        )
        binding.partyGSTStateOfSupplyInput.setAdapter<ArrayAdapter<String>>(
            ArrayAdapter<String>(
                this, android.R.layout.simple_dropdown_item_1line, stateOfSupply
            )
        )


        binding.savePartyButton.setOnClickListener {

            // partyCondition[0] == 0 -> party don't have opening balance
            // partyCondition[1] == 0 -> party don't have GST
            // partyCondition[2] == 0 -> party don't have bank details
            // partyCondition[0] == 1 -> party have opening balance and will pay or receive

            if (binding.partyNameInput.text.toString().isEmpty() || binding.partyTypeInput.text.toString().isEmpty() || binding.partyPhoneNumberInput.text.toString().isEmpty()){
                Toast.makeText(this, "Please Fill Basic Details", Toast.LENGTH_SHORT).show()
            }else{
                var partyName = binding.partyNameInput.text.toString()
                var partyType = binding.partyTypeInput.text.toString()
                var partyPhoneNumber = binding.partyPhoneNumberInput.text.toString()
                var partyConditionArray = arrayOf("0","0","0")

                // for opening balance
                var partyOpeningBalanceAmount = ""
                var partyOpeningBalanceDetails = ""

                // for GST
                var partyGSTNumber = ""
                var partyGSTStateOfSupply = ""
                var partyGSTBillingAddress = ""
                var partyGSTLegalBusinessName = ""

                // for bank details
                var partyBankName = ""
                var partyIFSCCode = ""
                var partyBankAddresss = ""
                var partyAccountNumber = ""
                var partyAccountHolderName = ""
                var partyUPIId = ""
                var partyIBANNumber = ""

                if (binding.partyHaveOpeningBalanceCheckBox.isChecked){
//                    Toast.makeText(this, "Party don't have opening balance", Toast.LENGTH_SHORT).show()
                    partyConditionArray[0] = "0"
                }else{
                    if (binding.partyOpeningBalanceAmountInput.text.toString().isEmpty() || binding.partyOpeningBalanceDetailsRadioGroup.checkedRadioButtonId == -1){
                        Toast.makeText(this, "Please Fill Opening Balance Details", Toast.LENGTH_SHORT).show()
                    }else{
                        if (binding.partyOpeningBalanceDetailsRadioGroup.checkedRadioButtonId == R.id.partyWillPay){
                            partyConditionArray[0] = "1"
                            partyOpeningBalanceDetails = "Will Pay"
                            partyOpeningBalanceAmount = binding.partyOpeningBalanceAmountInput.text.toString()
                        }else{
                            partyConditionArray[0] = "1"
                            partyOpeningBalanceDetails = "Will Receive"
                            partyOpeningBalanceAmount = binding.partyOpeningBalanceAmountInput.text.toString()
                        }
                    }
                }

                if (binding.partyHaveGSTCheckBox.isChecked){
//                    Toast.makeText(this, "Party don't have GST", Toast.LENGTH_SHORT).show()
                    partyConditionArray[1] = "0"

                }else{
                    if (binding.partyGSTNumberInput.text.toString().isEmpty() || binding.partyGSTStateOfSupplyInput.text.toString().isEmpty() || binding.partyGSTBillingAddress.text.toString().isEmpty() || binding.partyGSTLegalBusinessName.text.toString().isEmpty()){
                        Toast.makeText(this, "Please Fill GST Details", Toast.LENGTH_SHORT).show()
                    }else{
                        partyConditionArray[1] = "1"
                        partyGSTNumber = binding.partyGSTNumberInput.text.toString()
                        partyGSTStateOfSupply = binding.partyGSTStateOfSupplyInput.text.toString()
                        partyGSTBillingAddress = binding.partyGSTBillingAddress.text.toString()
                        partyGSTLegalBusinessName = binding.partyGSTLegalBusinessName.text.toString()
                    }
                }

                if (binding.partyHaveBankDetailsCheckBox.isChecked){
//                    Toast.makeText(this, "Party don't have bank details", Toast.LENGTH_SHORT).show()
                    partyConditionArray[2] = "0"
                }else{
                    if (binding.partyBankNameInput.text.toString().isEmpty() || binding.partyIFSCCodeInput.text.toString().isEmpty()|| binding.partyBankAddresssInput.text.toString().isEmpty() || binding.partyAccountNumberInput.text.toString().isEmpty() || binding.partyAccountHolderNameInput.text.toString().isEmpty() || binding.partyUPIIdInput.text.toString().isEmpty() || binding.partyIBANNumberInput.text.toString().isEmpty()){
                        Toast.makeText(this, "Please Fill Bank Details", Toast.LENGTH_SHORT).show()
                    }else{
                        partyConditionArray[2] = "1"
                        partyBankName = binding.partyBankNameInput.text.toString()
                        partyIFSCCode = binding.partyIFSCCodeInput.text.toString()
                        partyBankAddresss = binding.partyBankAddresssInput.text.toString()
                        partyAccountNumber = binding.partyAccountNumberInput.text.toString()
                        partyAccountHolderName = binding.partyAccountHolderNameInput.text.toString()
                        partyUPIId = binding.partyUPIIdInput.text.toString()
                        partyIBANNumber = binding.partyIBANNumberInput.text.toString()

                    }
                }

                val partyCondition = partyConditionArray.joinToString("")
                Toast.makeText(this, "Party Condition : $partyCondition", Toast.LENGTH_SHORT).show()
                lateinit var party: Party
                when (partyCondition){
                    "000" -> {
                        party = Party(partyName,partyPhoneNumber, partyType,partyCondition)
                    }
                    "100" -> {
                        party = Party(partyName,partyPhoneNumber, partyType,partyCondition,partyOpeningBalanceAmount,partyOpeningBalanceDetails)
                    }
                    "010" -> {
                        party = Party(partyName,partyPhoneNumber, partyType,partyCondition,partyGSTNumber,partyGSTLegalBusinessName,partyGSTStateOfSupply,partyGSTBillingAddress)
                    }
                    "110" -> {
                        party = Party(partyName,partyPhoneNumber, partyType,partyCondition,partyOpeningBalanceAmount,partyOpeningBalanceDetails,partyGSTNumber,partyGSTLegalBusinessName,partyGSTStateOfSupply,partyGSTBillingAddress)
                    }
                    "001" -> {
                        party = Party(partyName,partyPhoneNumber, partyType,partyCondition,partyAccountHolderName, partyAccountNumber,  partyIFSCCode, partyBankName, partyBankAddresss, partyIBANNumber, partyUPIId)
                    }
                    "101" -> {
                        party = Party(partyName,partyPhoneNumber, partyType,partyCondition,partyOpeningBalanceAmount,partyOpeningBalanceDetails,partyAccountHolderName, partyAccountNumber,  partyIFSCCode, partyBankName, partyBankAddresss, partyIBANNumber, partyUPIId)
                    }
                    "111" -> {
                        party = Party(partyName,partyPhoneNumber, partyType,partyCondition,partyOpeningBalanceAmount,partyOpeningBalanceDetails,partyGSTNumber,partyGSTLegalBusinessName,partyGSTStateOfSupply,partyGSTBillingAddress,partyAccountHolderName, partyAccountNumber,  partyIFSCCode, partyBankName, partyBankAddresss, partyIBANNumber, partyUPIId)
                    }
                    "011" -> {
                        party = Party(partyName,partyPhoneNumber, partyType,partyCondition, partyGSTNumber,partyGSTLegalBusinessName,partyGSTStateOfSupply,partyGSTBillingAddress,partyAccountHolderName, partyAccountNumber,  partyIFSCCode, partyBankName, partyBankAddresss, partyIBANNumber, partyUPIId)
                    }
                    else -> {
                        Toast.makeText(this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }

                val firebaseOperationsForLibrary = FirebaseOperationsForLibrary()
                firebaseOperationsForLibrary.addPartyToPartyLibrary(party)
                finish()
            }
        }


    }

    fun backButton(view: View){
        finish()
    }



}