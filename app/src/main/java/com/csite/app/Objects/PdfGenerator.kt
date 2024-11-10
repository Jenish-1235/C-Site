package com.csite.app.Objects

import com.cmpte.app.Objects.TransactionMaterialPurchase
import com.itextpdf.io.font.constants.StandardFonts
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.AreaBreak
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import java.io.File
import java.io.FileOutputStream

object PdfGenerator {

    fun generateDPR(
        outputPath: String,
        materialRequests: List<MaterialRequestOrReceived>,
        projectName: String?,
        selectedDate: String
    ) {
        // Initialize a new document
        val pdfWriter = PdfWriter(FileOutputStream(File(outputPath)))

        // Create the PdfDocument
        val pdfDoc = PdfDocument(pdfWriter)

        // Create the Document (this is where the content will go)
        val document = Document(pdfDoc)

        document.add(Paragraph("Daily Progress Report")
            .setFont(com.itextpdf.kernel.font.PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
            .setFontSize(22F))

        document.add(Paragraph("Project Name : $projectName")
            .setFont(com.itextpdf.kernel.font.PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
            .setFontSize(24F))

        document.add(Paragraph("Date : $selectedDate")
            .setFont(com.itextpdf.kernel.font.PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
            .setFontSize(20F))


        // Add a title
        document.add(Paragraph("Material Request Report")
            .setFont(com.itextpdf.kernel.font.PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
            .setFontSize(18F))

        // Create a table with 7 columns (one for each property in the MaterialRequestOrReceived)
        val table = Table(7)

        // Add header row (column names)
        table.addCell(Cell().add(Paragraph("Type")))
        table.addCell(Cell().add(Paragraph("Date & Time")))
        table.addCell(Cell().add(Paragraph("Material Name")))
        table.addCell(Cell().add(Paragraph("Quantity")))
        table.addCell(Cell().add(Paragraph("Material ID")))
        table.addCell(Cell().add(Paragraph("Unit")))
        table.addCell(Cell().add(Paragraph("Category")))

        // Add data rows
        for (request in materialRequests) {
            table.addCell(Cell().add(Paragraph(request.type)))
            table.addCell(Cell().add(Paragraph(request.dateTimeStamp)))
            table.addCell(Cell().add(Paragraph(request.materialName)))
            table.addCell(Cell().add(Paragraph(request.materialQuantity)))
            table.addCell(Cell().add(Paragraph(request.materialId)))
            table.addCell(Cell().add(Paragraph(request.materialUnit)))
            table.addCell(Cell().add(Paragraph(request.materialCategory)))
        }

        // Add the table to the document
        document.add(table)

        document.add(Paragraph("Attendance Report").setFont(com.itextpdf.kernel.font.PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
            .setFontSize(18F))
        val attendanceTable = Table(8)
        attendanceTable.addCell(Cell().add(Paragraph("Name")))
        attendanceTable.addCell(Cell().add(Paragraph("Attendance")))
        attendanceTable.addCell(Cell().add(Paragraph("Salary Per Day")))
        attendanceTable.addCell(Cell().add(Paragraph("Overtime Pay")))
        attendanceTable.addCell(Cell().add(Paragraph("Late Fine")))
        attendanceTable.addCell(Cell().add(Paragraph("Deduction")))
        attendanceTable.addCell(Cell().add(Paragraph("Allowance")))
        attendanceTable.addCell(Cell().add(Paragraph("Total")))

        // todo: attendance

        // Close the document to finalize the PDF
        document.close()

        println("PDF created successfully at $outputPath")

    }

    fun generateTransactionReport(filePath: String, transactions: MutableList<TransactionPaymentIn>, paymentOut: MutableList<TransactionPaymentOut>, otherExpense: MutableList<TransactionOtherExpense>, salesInvoice: MutableList<TransactionSalesInvoice>, materialPurchase: MutableList<TransactionMaterialPurchase>,selectedDate: String, projectName: String) {
        val pdfWriter = PdfWriter(FileOutputStream(File(filePath)))
        val pdfDocument = PdfDocument(pdfWriter)
        val document = Document(pdfDocument)
        pdfDocument.defaultPageSize = com.itextpdf.kernel.geom.PageSize.A4.rotate()

        document.add(Paragraph("Daily Transaction Report")
            .setFont(com.itextpdf.kernel.font.PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
            .setFontSize(22F))

        document.add(Paragraph("Project Name : $projectName")
            .setFont(com.itextpdf.kernel.font.PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
            .setFontSize(24F))

        document.add(Paragraph("Date : $selectedDate")
            .setFont(com.itextpdf.kernel.font.PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
            .setFontSize(20F))

        document.add(Paragraph("Payment In Report")
            .setFont(com.itextpdf.kernel.font.PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
            .setFontSize(18F))

        // var transactionDate: String? = null
        //    var transactionParty: String? = null
        //    var transactionAmount: String? = null
        //    var paymentInTransactionPaymentMode: String? = null
        //    var transactionDescription: String? = null
        //    var paymentInTrasactionCostCode: String? = null
        //    var paymentInTransactionCategory:String? = null
        //    var transactionId = ""

        val table = Table(8)
        table.addCell(Cell().add(Paragraph("Transaction Id")))
        table.addCell(Cell().add(Paragraph("Transaction Date")))
        table.addCell(Cell().add(Paragraph("Transaction Party")))
        table.addCell(Cell().add(Paragraph("Transaction Amount")))
        table.addCell(Cell().add(Paragraph("Payment Mode")))
        table.addCell(Cell().add(Paragraph("Transaction Description")))
        table.addCell(Cell().add(Paragraph("Cost Code")))
        table.addCell(Cell().add(Paragraph("Category")))

        for (transaction in transactions) {

            table.addCell(Cell().add(Paragraph(transaction.transactionId)))
            table.addCell(Cell().add(Paragraph(transaction.transactionDate)))
            table.addCell(Cell().add(Paragraph(transaction.transactionParty)))
            table.addCell(Cell().add(Paragraph(transaction.transactionAmount)))
            table.addCell(Cell().add(Paragraph(transaction.paymentInTransactionPaymentMode)))
            table.addCell(Cell().add(Paragraph(transaction.transactionDescription)))
            table.addCell(Cell().add(Paragraph(transaction.paymentInTrasactionCostCode)))
            table.addCell(Cell().add(Paragraph(transaction.paymentInTransactionCategory)))
        }
        document.add(table)

        pdfDocument.addNewPage()
document.add(AreaBreak())


        document.add(Paragraph("Payment Out Report")
            .setFont(com.itextpdf.kernel.font.PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
            .setFontSize(18F))

        val table2 = Table(8)
        table2.addCell(Cell().add(Paragraph("Transaction Id")))
        table2.addCell(Cell().add(Paragraph("Transaction Date")))
        table2.addCell(Cell().add(Paragraph("Transaction Party")))
        table2.addCell(Cell().add(Paragraph("Transaction Amount")))
        table2.addCell(Cell().add(Paragraph("Payment Mode")))
        table2.addCell(Cell().add(Paragraph("Transaction Description")))
        table2.addCell(Cell().add(Paragraph("Cost Code")))
        table2.addCell(Cell().add(Paragraph("Category")))

        for (transaction in paymentOut) {
            table2.addCell(Cell().add(Paragraph(transaction.transactionId)))
            table2.addCell(Cell().add(Paragraph(transaction.transactionDate)))
            table2.addCell(Cell().add(Paragraph(transaction.transactionParty)))
            table2.addCell(Cell().add(Paragraph(transaction.transactionAmount)))
            table2.addCell(Cell().add(Paragraph(transaction.paymentOutTransactionPaymentMode)))
            table2.addCell(Cell().add(Paragraph(transaction.transactionDescription)))
            table2.addCell(Cell().add(Paragraph(transaction.paymentOutTrasactionCostCode)))
            table2.addCell(Cell().add(Paragraph(transaction.paymentOutTransactionCategory)))
        }
        document.add(table2)

        pdfDocument.addNewPage()
document.add(AreaBreak())

        document.add(Paragraph("Other Expense Report")
            .setFont(com.itextpdf.kernel.font.PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
            .setFontSize(18F))

        val table3 = Table(11)
        table3.addCell(Cell().add(Paragraph("Transaction Id")))
        table3.addCell(Cell().add(Paragraph("Transaction Date")))
        table3.addCell(Cell().add(Paragraph("Transaction Party")))
        table3.addCell(Cell().add(Paragraph("Quantity")))
        table3.addCell(Cell().add(Paragraph("Unit")))
        table3.addCell(Cell().add(Paragraph("Unit Price")))
        table3.addCell(Cell().add(Paragraph("Additional Charges")))
        table3.addCell(Cell().add(Paragraph("Discount")))
        table3.addCell(Cell().add(Paragraph("Amount")))
        table3.addCell(Cell().add(Paragraph("Category")))
        table3.addCell(Cell().add(Paragraph("Description")))
        for (transaction in otherExpense) {
            table3.addCell(Cell().add(Paragraph(transaction.transactionId)))
            table3.addCell(Cell().add(Paragraph(transaction.transactionDate)))
            table3.addCell(Cell().add(Paragraph(transaction.transactionParty)))
            table3.addCell(Cell().add(Paragraph(transaction.otherExpenseTransactionQuantity)))
            table3.addCell(Cell().add(Paragraph(transaction.otherExpenseTransactionUnit)))
            table3.addCell(Cell().add(Paragraph(transaction.otherExpenseTransactionUnitPrice)))
            table3.addCell(Cell().add(Paragraph(transaction.otherExpenseTransactionAdditionalCharges)))
            table3.addCell(Cell().add(Paragraph(transaction.otherExpenseTransactionDiscount)))
            table3.addCell(Cell().add(Paragraph(transaction.transactionAmount)))
            table3.addCell(Cell().add(Paragraph(transaction.otherExpenseTransactionCategory)))
            table3.addCell(Cell().add(Paragraph(transaction.transactionDescription)))
        }

        document.add(table3)

        pdfDocument.addNewPage()
document.add(AreaBreak())

        document.add(Paragraph("Sales Invoice Report")
            .setFont(com.itextpdf.kernel.font.PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
            .setFontSize(18F))


        val table4 = Table(9)
        table4.addCell(Cell().add(Paragraph("Transaction Id")))
        table4.addCell(Cell().add(Paragraph("Transaction Date")))
        table4.addCell(Cell().add(Paragraph("Transaction Party")))
        table4.addCell(Cell().add(Paragraph("Category")))
        table4.addCell(Cell().add(Paragraph("Additional Charges")))
        table4.addCell(Cell().add(Paragraph("Discount")))
        table4.addCell(Cell().add(Paragraph("Amount")))
        table4.addCell(Cell().add(Paragraph("Description")))
        table4.addCell(Cell().add(Paragraph("Items")))

        for (transaction in salesInvoice) {
            table4.addCell(Cell().add(Paragraph(transaction.transactionId)))
            table4.addCell(Cell().add(Paragraph(transaction.transactionDate)))
            table4.addCell(Cell().add(Paragraph(transaction.transactionParty)))
            table4.addCell(Cell().add(Paragraph(transaction.siCategory)))
            table4.addCell(Cell().add(Paragraph(transaction.siAdditionalCharge)))
            table4.addCell(Cell().add(Paragraph(transaction.siDiscount)))
            table4.addCell(Cell().add(Paragraph(transaction.transactionAmount)))
            table4.addCell(Cell().add(Paragraph(transaction.transactionDescription)))
            var items = ""
            for (item in transaction.siItems) {
                items += " ${item.value.materialName} : ${item.value.materialQuantity} ${item.value.materialUnit} \n"
            }
            table4.addCell(Cell().add(Paragraph(items)))
        }
        document.add(table4)

        pdfDocument.addNewPage()
document.add(AreaBreak())


        document.add(Paragraph("Material Purchase Report")
            .setFont(com.itextpdf.kernel.font.PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
            .setFontSize(18F))

        val table5 = Table(9)

        table5.addCell(Cell().add(Paragraph("Transaction Id")))
        table5.addCell(Cell().add(Paragraph("Transaction Date")))
        table5.addCell(Cell().add(Paragraph("Transaction Party")))
        table5.addCell(Cell().add(Paragraph("Category")))
        table5.addCell(Cell().add(Paragraph("Additional Charges")))
        table5.addCell(Cell().add(Paragraph("Discount")))
        table5.addCell(Cell().add(Paragraph("Amount")))
        table5.addCell(Cell().add(Paragraph("Description")))
        table5.addCell(Cell().add(Paragraph("Items")))

        for (transaction in materialPurchase) {
            table5.addCell(Cell().add(Paragraph(transaction.transactionId)))
            table5.addCell(Cell().add(Paragraph(transaction.transactionDate)))
            table5.addCell(Cell().add(Paragraph(transaction.transactionParty)))
            table5.addCell(Cell().add(Paragraph(transaction.mpCategory)))
            table5.addCell(Cell().add(Paragraph(transaction.mpAdditionalCharge)))
            table5.addCell(Cell().add(Paragraph(transaction.mpDiscount)))
            table5.addCell(Cell().add(Paragraph(transaction.transactionAmount)))
            table5.addCell(Cell().add(Paragraph(transaction.transactionDescription)))
            var items = ""
            for (item in transaction.mpItems) {
                items += " ${item.value.materialName} : ${item.value.materialQuantity} ${item.value.materialUnit} \n"
            }
            table5.addCell(Cell().add(Paragraph(items)))
        }
        document.add(table5)

        document.close()
        pdfDocument.close()
        pdfWriter.close()

        println("PDF created successfully at $filePath")

    }
}
