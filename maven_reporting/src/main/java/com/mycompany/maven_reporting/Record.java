package com.mycompany.maven_reporting;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author chinlokchan
 */
public class Record {
    private String Employee;
    private int Retail;
    private int Services;
    private float Collected;
    private float EFT;
    private float Cash;
    private boolean Balance;
    private float Difference;

    
    public Record( String Employee, int Retail, int Services, float Collected, float EFT, float Cash, boolean Balance, float Difference){
        this.Employee = Employee;
        this.Retail = Retail;
        this.Services = Services;
        this.Collected = Collected;
        this.EFT = EFT;
        this.Cash = Cash;
        this.Balance = Balance;
        this.Difference = Difference;
    }
    
    public Record( String Employee, int Retail, int Services, float Collected, float EFT, float Cash, boolean Balance){
        this.Employee = Employee;
        this.Retail = Retail;
        this.Services = Services;
        this.Collected = Collected;
        this.EFT = EFT;
        this.Cash = Cash;
        this.Balance = Balance;
        this.Difference = 0.0f;
    }
    
    public Record(){
        this.Employee = null;
        this.Retail = 0;
        this.Services = 0;
        this.Collected = 0;
        this.EFT = 0;
        this.Cash = 0;
        this.Balance = false;
        this.Difference = 0;
    }
    
    public String getEmployee(){
        return Employee;
    }
    
    public void setEmployee(String Employee){
        this.Employee = Employee;
    }
    
    public int getRetail(){
        return Retail;
    }
    
    public void setRetail(int Retail){
        this.Retail = Retail;
    }
    
    public int getServices(){
        return Services;
    }
    
    public void setServices(int Services){
        this.Services = Services;
    }
    
    public float getCollected(){
        return Collected;        
    }
    
    public void setCollected(float Collected){
        this.Collected = Collected;
    }
    
    public float getEFT(){
        return EFT;
    }
    
    public void setEFT(float EFT){
        this.EFT = EFT;
    }
    
    public float getCash(){
        return Cash;
    }
    
    public void setCash(float Cash){
        this.Cash = Cash;
    }
    
    public boolean getBalance(){
        return Balance;
    }
    
    public void setBalance(boolean Balance){
        this.Balance = Balance;
    }
    
    public float getDifference(){
        return Difference;
    }
    
    @Override
    public String toString() {
        return Employee + "\t\t\t" + Retail + "\t\t\t" + Services  + "\t\t\t" + Collected  + "\t\t\t" + EFT + "\t\t\t" + Cash + "\t\t\t" + Balance + "\t\t\t" + Difference;
    };
}
