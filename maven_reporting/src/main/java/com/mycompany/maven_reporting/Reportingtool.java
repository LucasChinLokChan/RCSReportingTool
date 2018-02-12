package com.mycompany.maven_reporting;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.squareup.connect.ApiClient;
import com.squareup.connect.ApiException;
import com.squareup.connect.Configuration;
import com.squareup.connect.api.LocationsApi;
import com.squareup.connect.auth.OAuth;
import com.squareup.connect.models.Location;
import com.squareup.connect.api.V1EmployeesApi;
import com.squareup.connect.models.V1Employee;
import com.squareup.connect.api.V1TransactionsApi;
import com.squareup.connect.models.V1Payment;
import com.squareup.connect.models.V1PaymentItemization;

import java.util.*;
import javax.mail.*;
import javax.mail.Session;
import javax.mail.internet.*;

import com.squareup.connect.models.V1Tender;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map.Entry;



/**
 *
 * @author chinlokchan
 */
public class Reportingtool {
    
    
    
    private static String AccessToken = "sq0atp-KxaslS376n010ss7twQM4A";
    private static ApiClient apiClient;
    private Hashtable<String, Record> ReportTable = new Hashtable<String, Record>();
    public String date;
    public String reportString;

    //Get employees data from database
    public List<V1Employee> getEmployees(){
        V1EmployeesApi V1EmployeesApi = new V1EmployeesApi();
        V1EmployeesApi.setApiClient(apiClient);
        List<V1Employee> Employees = null;
        try{
            Employees = V1EmployeesApi.listEmployees(null,null,null,null,null,null,null,null,null);
        } catch (ApiException e) {
            System.err.println("Failed to get employees");
            e.printStackTrace();
        }
        return Employees;
    };
    
    //Get Transaction List from databasev V1 **data incorrect**
    public List<V1Payment> getTransactionList(){
        LocationsApi locationsApi = new LocationsApi();
        locationsApi.setApiClient(apiClient);
        
        V1TransactionsApi V1TransactionsApi = new V1TransactionsApi();
        V1TransactionsApi.setApiClient(apiClient);
        
 
        List<V1Payment> TransactionList = null;
        try{
            for(Location a:locationsApi.listLocations().getLocations()){
                TransactionList = V1TransactionsApi.listPayments(a.getId(), null, date+"T00:00:00Z", date+"T23:59:59Z", Integer.valueOf(200), null);
            }
        } catch (ApiException e) {
            System.err.println("Failed to get Transaction List");
            e.printStackTrace();
        }
        return TransactionList;
    };
    
    
    //Add new record to hashtable
    public void addRecord(String EmpolyeeId,String Empolyee, int Retail, int Services, float Collected, float EFT, float Cash, boolean Balance){
        if(ReportTable.get(EmpolyeeId) != null){
            ReportTable.replace(EmpolyeeId, new Record(Empolyee, Retail, Services, Collected, EFT, Cash, Balance));
        }else{
            ReportTable.put(EmpolyeeId, new Record(Empolyee, Retail, Services, Collected, EFT, Cash, Balance));
        }
    }
    
    //create new record in the report
    public Record newRecord(String EmpolyeeId,String Empolyee){
        if(checkRecordExist(EmpolyeeId)){
            ReportTable.replace(EmpolyeeId, new Record());
        }else{
            ReportTable.put(EmpolyeeId, new Record());
        }
        ReportTable.get(EmpolyeeId).setEmployee(Empolyee);
        return ReportTable.get(EmpolyeeId);
    } 
    
    
    //check the record exist in the report or not
    public boolean checkRecordExist(String EmpolyeeId){
        if(ReportTable.get(EmpolyeeId) != null){
            return true;
        }else{
            return false;
        }
    }
    
    //send the email with smtp
    public void mailTo(){
       // Recipient's email ID needs to be mentioned.
      String to = "lucaschan.1995@gmail.com";

      // Sender's email ID needs to be mentioned
      String from = "";

      // Assuming you are sending email from localhost
      String host = "";

      // Get system properties
      Properties properties = System.getProperties();
      
      properties.setProperty("mail.smtp.host", "587");

      // Setup mail server
      properties.setProperty("mail.smtp.host", host);

      properties.setProperty("mail.smtp.auth", "true");
      
      
      //properties.setProperty("mail.smtp.ssl.enable", "true");
      
      // Get the default Session object.
      Session session = Session.getInstance(properties,
         new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
               return new PasswordAuthentication(
                  "T4D4CENBO2QWQ", "AI5G2FQTFFKLW");
            }
         });
      try {
         // Create a default MimeMessage object.
         MimeMessage message = new MimeMessage(session);

         // Set From: header field of the header.
         message.setFrom(new InternetAddress(from));

         // Set To: header field of the header.
         message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

         // Set Subject: header field
         message.setSubject("Daily Report");

         // Now set the actual message
         message.setText(reportString);

         // Send message
         Transport.send(message);
         System.out.println("Sent message successfully....");
      } catch (MessagingException mex) {
         mex.printStackTrace();
      }
    }
    
    
    //generate report
    public void generateReport(){
        reportString="";
        
        //add date
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = dateFormat.format(cal.getTime());
        reportString="Date: " +date + "\n";
    
        //get employees
        List<V1Employee> employeesList = getEmployees();
        
        //get today transaction
        List<V1Payment> transactionList;
        transactionList = getTransactionList();
        
        //loop all the employees
        for(V1Employee employee: employeesList){
            
            //create a record for the empolyee
            Record employeeRecord = newRecord(employee.getId(), employee.getFirstName() + " " + employee.getLastName());
            
            //loop all the transactions
            for(V1Payment T : transactionList){
                for(V1Tender tender:T.getTender()){
                    //if found the employee ID in the transactions
                    if(tender.getEmployeeId().equals(employee.getId())){

                        //loop all the item sold in this transaction
                        for(V1PaymentItemization item : T.getItemizations()){

                            //if merchandise found, retail +1
                            if(item.getItemDetail().getCategoryName().equals("Merchandise")){
                                employeeRecord.setRetail(employeeRecord.getRetail()+1);
                            }else{
                                //if other found ( haircut, shave, etc...), Services +1
                                employeeRecord.setServices(employeeRecord.getServices()+1);
                            }
                        }

                        //set collected
                        employeeRecord.setCollected(employeeRecord.getCollected()+(T.getTotalCollectedMoney().getAmount()/100));


                        //Cash
                        if(tender.getType().equals(V1Tender.TypeEnum.CASH)){
                            employeeRecord.setCash(employeeRecord.getCash()+(tender.getTotalMoney().getAmount()/100));

                        //EFT
                        }else if (tender.getType().equals(V1Tender.TypeEnum.CREDIT_CARD) || (tender.getType().equals(V1Tender.TypeEnum.SQUARE_WALLET))|| (tender.getType().equals(V1Tender.TypeEnum.THIRD_PARTY_CARD))){
                            employeeRecord.setEFT(employeeRecord.getEFT()+(tender.getTotalMoney().getAmount()/100));
                        }else{
                            //Other
                            //do nothing
                        }
                    }
                }
            }
        }
        //set title
        reportString += String.format("%-20s%-10s%-10s%10s%10s%10s%10s%15s%s", "Empolyee","Retail","Services","Collected","EFT","Cash","Balance","Difference","\n");
        for(Entry<String,Record> r:ReportTable.entrySet()){
            
            Record temp = r.getValue();
            //generate each employee report
            reportString += String.format("%-20s%-10s%-10s%10.2f%10.2f%10.2f%10s%15.2f%s", temp.getEmployee(), temp.getRetail(), temp.getServices(), temp.getCollected(), temp.getEFT(), temp.getCash(), temp.getBalance()?"Yes":"No",temp.getDifference(),"\n");
        }

        System.out.printf(reportString);
    }
    
    
    public static void main(String[] args) {
        Reportingtool reportingtool = new Reportingtool();
        
        apiClient = Configuration.getDefaultApiClient();

        // Configure OAuth2 access token for authorization: oauth2
        OAuth oauth2 = (OAuth) apiClient.getAuthentication("oauth2");
        oauth2.setAccessToken(AccessToken);
        
        reportingtool.generateReport();
        //reportingtool.mailTo();
    }
}
