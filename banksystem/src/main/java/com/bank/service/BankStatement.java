package com.bank.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Component;

import com.bank.dto.EmailDetails;
import com.bank.pojos.Transaction;
import com.bank.pojos.User;
import com.bank.repository.TransactionRepository;
import com.bank.repository.UserRepository;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class BankStatement {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private static final String FILE = "/Users/sanjana/Desktop/Last/BankStatement.pdf";
    
    /**
     * Generates a bank statement PDF for the given account number between startDate and endDate,
     * emails the PDF to the user, and returns the list of transactions.
     */
    public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate) {
        // Parse the input dates (expected in ISO format, e.g. "2025-02-09")
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);
        
        // Retrieve transactions for the account and filter by date range (inclusive)
        List<Transaction> transactionList = transactionRepository.findAll().stream()
                .filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
                .filter(transaction -> !transaction.getCreatedAt().isBefore(start) && !transaction.getCreatedAt().isAfter(end))
                .toList();
        
        // Retrieve the user information
        User user = userRepository.findByAccountNumber(accountNumber);
        System.out.println("User Retrieved");
        if (user == null) {
            log.error("No user found with account number: {}", accountNumber);
            throw new RuntimeException("User not found for account number: " + accountNumber);
        }
        
        String customerName = user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName();
        
        // Create a PDF document with custom page size
        Rectangle statementSize = new Rectangle(PageSize.A4);
        Document document = new Document(statementSize);
        log.info("Setting size of Document");
        
        try {
            // Ensure that the target directory exists
            File file = new File(FILE);
            File parentDir = file.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }
            
            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                PdfWriter.getInstance(document, outputStream);
                document.open();
                
                // Define fonts for headers and normal text
                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.WHITE);
                Font subHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
                Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
                Font tableHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BaseColor.WHITE);
                
                // --- Bank Info Table ---
                PdfPTable bankInfoTable = new PdfPTable(1);
                bankInfoTable.setWidthPercentage(100);
                PdfPCell bankNameCell = new PdfPCell(new Phrase("Apna Bank", headerFont));
                bankNameCell.setBorder(0);
                bankNameCell.setBackgroundColor(new BaseColor(0, 102, 204)); // deep blue
                bankNameCell.setPadding(10f);
                bankNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell bankAddressCell = new PdfPCell(new Phrase("Akurdi, Pune, Maharashtra", normalFont));
                bankAddressCell.setBorder(0);
                bankAddressCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                bankInfoTable.addCell(bankNameCell);
                bankInfoTable.addCell(bankAddressCell);
                
                // --- Statement Info Table ---
                PdfPTable statementInfo = new PdfPTable(2);
                statementInfo.setWidthPercentage(100);
                PdfPCell statementCell = new PdfPCell(new Phrase("Statement Of Account", subHeaderFont));
                statementCell.setBorder(0);
                statementCell.setBackgroundColor(new BaseColor(0, 153, 76)); // greenish color
                statementCell.setPadding(8f);
                statementCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                PdfPCell nameCell = new PdfPCell(new Phrase("Customer Name: " + customerName, normalFont));
                nameCell.setBorder(0);
                nameCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                statementInfo.addCell(statementCell);
                statementInfo.addCell(nameCell);
                PdfPCell emptyCell = new PdfPCell(new Phrase(" "));
                emptyCell.setBorder(0);
                PdfPCell addressCell = new PdfPCell(new Phrase("Customer Address: " + user.getAddress(), normalFont));
                addressCell.setBorder(0);
                statementInfo.addCell(emptyCell);
                statementInfo.addCell(addressCell);
                
                // --- Transactions Table ---
                PdfPTable transactionsTable = new PdfPTable(4);
                transactionsTable.setWidthPercentage(100);
                transactionsTable.setWidths(new float[] {2, 4, 3, 2});
                
                // Define header cells with attractive colors and alignment
                PdfPCell dateHeader = new PdfPCell(new Phrase("Date", tableHeaderFont));
                dateHeader.setBackgroundColor(new BaseColor(0, 102, 204));
                dateHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
                dateHeader.setPadding(5f);
                dateHeader.setBorder(0);
                
                PdfPCell typeHeader = new PdfPCell(new Phrase("Transaction Type", tableHeaderFont));
                typeHeader.setBackgroundColor(new BaseColor(0, 102, 204));
                typeHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
                typeHeader.setPadding(5f);
                typeHeader.setBorder(0);
                
                PdfPCell amountHeader = new PdfPCell(new Phrase("Transaction Amount", tableHeaderFont));
                amountHeader.setBackgroundColor(new BaseColor(0, 102, 204));
                amountHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
                amountHeader.setPadding(5f);
                amountHeader.setBorder(0);
                
                PdfPCell statusHeader = new PdfPCell(new Phrase("Status", tableHeaderFont));
                statusHeader.setBackgroundColor(new BaseColor(0, 102, 204));
                statusHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
                statusHeader.setPadding(5f);
                statusHeader.setBorder(0);
                
                transactionsTable.addCell(dateHeader);
                transactionsTable.addCell(typeHeader);
                transactionsTable.addCell(amountHeader);
                transactionsTable.addCell(statusHeader);
                
                // Add each transaction as a row in the table with alternating row colors
                boolean alternate = false;
                BaseColor rowColor1 = BaseColor.WHITE;
                BaseColor rowColor2 = new BaseColor(224, 224, 224); // light gray
                
                for (Transaction transaction : transactionList) {
                    BaseColor rowColor = alternate ? rowColor2 : rowColor1;
                    
                    PdfPCell dateCell = new PdfPCell(new Phrase(transaction.getCreatedAt().toString(), normalFont));
                    dateCell.setBackgroundColor(rowColor);
                    dateCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    dateCell.setPadding(5f);
                    dateCell.setBorder(0);
                    transactionsTable.addCell(dateCell);
                    
                    PdfPCell typeCell = new PdfPCell(new Phrase(transaction.getTransactionType(), normalFont));
                    typeCell.setBackgroundColor(rowColor);
                    typeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    typeCell.setPadding(5f);
                    typeCell.setBorder(0);
                    transactionsTable.addCell(typeCell);
                    
                    String amountStr = transaction.getTransactionAmount() != null 
                            ? transaction.getTransactionAmount().toString() 
                            : "0.00";
                    PdfPCell amountCell = new PdfPCell(new Phrase(amountStr, normalFont));
                    amountCell.setBackgroundColor(rowColor);
                    amountCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    amountCell.setPadding(5f);
                    amountCell.setBorder(0);
                    transactionsTable.addCell(amountCell);
                    
                    PdfPCell statusCell = new PdfPCell(new Phrase(transaction.getStatus(), normalFont));
                    statusCell.setBackgroundColor(rowColor);
                    statusCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    statusCell.setPadding(5f);
                    statusCell.setBorder(0);
                    transactionsTable.addCell(statusCell);
                    
                    alternate = !alternate;
                }
                
                // Add all tables to the document with some spacing in between
                document.add(bankInfoTable);
                document.add(new Paragraph(" "));
                document.add(statementInfo);
                document.add(new Paragraph(" "));
                document.add(transactionsTable);
                
                // Close the document; this finalizes the PDF.
                document.close();
            }
        } catch (FileNotFoundException e) {
            log.error("File not found: " + FILE, e);
            throw new RuntimeException("Error writing PDF file", e);
        } catch (DocumentException e) {
            log.error("Document exception occurred", e);
            throw new RuntimeException("Error generating PDF document", e);
        } catch (Exception e) {
            log.error("An error occurred while generating the PDF", e);
            throw new RuntimeException("Error generating PDF", e);
        }
        
        // Build the email details with the PDF attachment
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(user.getEmail())
                .subject("Statement Of Account")
                .messageBody("Kindly find your requested account statement attached!")
                .attachment(FILE)
                .build();
        
        // Send the email with the PDF attached
        emailService.sendEmailWithAttachment(emailDetails);
        
        return transactionList;
    }
}
