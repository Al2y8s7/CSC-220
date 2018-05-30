// DO NOT ADD NEW METHODS OR DATA FIELDS!
// DO NOT MODIFY METHODS OR DATA FIELDS!

package pj3;

class Customer
{
    private int customerID;
    private int transactionTime;
    private int arrivalTime;
    private int finishTime;

    // default constructor
    Customer()
    {
    }

    // constructor to set customerID, transactionTime, arrivalTime
    // and compute finishTime
    Customer(int customerid, int transactiontime, int arrivaltime)
    {
  	customerID       = customerid;
  	transactionTime  = transactiontime;
  	arrivalTime      = arrivaltime;
    }

    int getTransactionTime() 
    {
  	return transactionTime; 
    }

    int getArrivalTime() 
    {
  	return arrivalTime; 
    }

    int getFinishTime() 
    {
  	return finishTime; 
    }

    int getCustomerID() 
    {
  	return customerID; 
    }

    void setFinishTime(int finishtime) 
    {
  	finishTime=finishtime; 
    }

    public String toString()
    {
        return "customerID="+customerID+":transactionTime="+
               transactionTime+":arrivalTime="+arrivalTime+
               ":finishTime="+finishTime;
    }

    public static void main(String[] args) {
        // quick check!
	Customer mycustomer = new Customer(1,5,18);
	mycustomer.setFinishTime(28);
	System.out.println("Customer Info:"+mycustomer);

    }
}
