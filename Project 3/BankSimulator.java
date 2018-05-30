package pj3;

import java.util.*;
import java.io.*;

// You may add new functions or data in this class 
// You may modify any functions or data members here
// You must use Customer, Teller and ServiceArea classes
// to implement Bank simulator
class BankSimulator {

    // input parameters
    private int numTellers, customerQLimit;
    private int simulationTime, dataSource;
    private int chancesOfArrival, maxTransactionTime;

    // statistical data
    private int numGoaway, numServed, totalWaitingTime;

    // internal data
    private int customerIDCounter;   // customer ID counter
    private ServiceArea servicearea; // service area object
    private Scanner dataFile;	   // get customer data from file
    private Random dataRandom;	   // get customer data using random function
    //create teller object
    private Teller teller;

    // most recent customer arrival info, see getCustomerData()
    private Customer customer;
    private boolean anyNewArrival;
    private int transactionTime;

    //Create scanner object
    Scanner scanData = new Scanner(System.in);
    //String object to read file
    private String file;
    private int data1, data2;
    //create boolean variable for setupParameters
    private boolean status = false;
    //create variable to store user input
    private int tempData;

    // initialize data fields
    private BankSimulator() {
        // add statements
        numTellers = 0;
        customerQLimit = 0;
        simulationTime = 0;
        dataSource = 0;
        chancesOfArrival = 0;
        maxTransactionTime = 0;
        numGoaway = 0;
        numServed = 0;
        totalWaitingTime = 0;
        customerIDCounter = 0;
        dataRandom = new Random();
        transactionTime = 0;
    }

    private void setupParameters() {
        // read input parameters
        System.out.println("*** Get Simulation Parameters ***");
        System.out.print("Enter simulation time (positive integer) < 10000: ");
        //Conditions for out parameters out of bounds
        while (!status) {
            tempData = scanData.nextInt();
            if (tempData <= 10000 && tempData >= 0) {
                simulationTime = tempData;
                status = true;
            } else {
                System.out.print("Enter simulation time (positive integer) < 10000: ");
            }
        }
        //conditions for teller
        System.out.print("Enter the number of tellers (0-10): ");
        while (status) {
            tempData = scanData.nextInt();
            if (tempData <= 10 && tempData >= 0) {
                numTellers = tempData;
                status = false;
            } else {
                System.out.print("Enter the number of tellers (0-10): ");
            }
        }
        //conditions for customer arrival 
        System.out.print("Enter chances (0% < & <= 100%) of new customer: ");
        while (!status) {
            tempData = scanData.nextInt();
            if (tempData <= 100 && tempData >= 0) {
                chancesOfArrival = tempData + 1;
                status = true;
            } else {
                System.out.print("Enter chances (0% < & <= 100%) of new customer: ");
            }
        }
        //condition for transaction time
        System.out.print("Enter maximum transaction time of customers (1-500): ");
        while (status) {
            tempData = scanData.nextInt();
            if (tempData <= 500 && tempData > 0) {
                maxTransactionTime = tempData;
                status = false;
            } else {
                System.out.print("Enter maximum transaction time of customers (1-500): ");
            }
        }
        //condition for queue limit
        System.out.print("Enter customer queue limit (0-50): ");
        while (!status) {
            tempData = scanData.nextInt();
            if (tempData <= 50 && tempData >= 0) {
                customerQLimit = tempData;
                status = true;
            } else {
                System.out.print("Enter customer queue limit (0-50):");
            }
        }
        //prompt user to input random data or read file
        System.out.print("Enter 0/1 to get data from Random/file: ");
        while (status) {
            while (!scanData.hasNextInt()) {
                System.out.print("Enter 0/1 to get data from Random/file: ");
                scanData.nextInt();
                status = false;
            }
            tempData = scanData.nextInt();
            switch (dataSource) {
                case 1:
                    dataSource = 1;
                    System.out.print("Enter file name: ");
                    file = scanData.next();
                    getCustomerData();
                    status = false;
                    break;
                case 0:
                    dataSource = 0;
                    getCustomerData();
                    status = false;
                    break;
                default:
                    System.out.print("Enter 0/1 to get data from Random/file: ");
                    break;
            }
            scanData.close();
        }
 
    }

    // Refer to step 1 in doSimulation()
    private void getCustomerData() {
        //if input for dataSource is 0  
        switch (dataSource) {
            case 0:
                anyNewArrival = ((dataRandom.nextInt(100) + 1) <= chancesOfArrival);
                transactionTime = dataRandom.nextInt(maxTransactionTime) + 1;
                break;
        //if input for dataSource is 1
            case 1:
                try {
                    Scanner fileReader = new Scanner(new File(file));
                    if (fileReader.hasNextInt()) {
                        data1 = fileReader.nextInt();
                        System.out.println("**Data1: " + data1);
                        data2 = fileReader.nextInt();
                        System.out.println("**Data2: " + data2);
                        anyNewArrival = (((data1 % 100) + 1) <= chancesOfArrival);

                        transactionTime = (data2 % maxTransactionTime) + 1;
                        System.out.println("**transaction time: " + transactionTime);
                    }
                } catch (FileNotFoundException ex) {
                    System.out.print(ex);
                    System.exit(0);
                }
                break;
            default:
                System.out.print("Enter 0/1 to get data from Random/file:");
                break;
        }
    }

    private void doSimulation() {
        // add statements
        System.out.println("\n\n--------------Start Simulation--------------\n\n");
        // Initialize ServiceArea
        servicearea = new ServiceArea(numTellers, customerQLimit);
        for (int i = 0; i < numTellers; i++) {
            servicearea.insertFreeTellerQ(teller = new Teller((i + 1)));
        }

        for (int currentTime = 0; currentTime < simulationTime; currentTime++) {
            System.out.println("---------------------------------------------\nTime: " + currentTime);
            //retrieve information 
            getCustomerData();
            // Step 1: any new customer enters the bank?
            if (anyNewArrival) {                                // Step 1.2: check customer waiting queue too long?
                customerIDCounter++;
                customer = new Customer(customerIDCounter, transactionTime, currentTime);
                System.out.println("\tcustomer # " + customerIDCounter + " arrives with transaction time " + customer.getTransactionTime());
                if (servicearea.isCustomerQTooLong()) {         //           if it is too long, update numGoaway
                    numGoaway++;                                //           else enter customer queue
                    System.out.println("\tcustomer # " + customer.getCustomerID() + " left because the line was too long");
                } else {
                    // Step 1.1: setup customer data
                    servicearea.insertCustomerQ(customer);
                    System.out.println("\tcustomer # " + customer.getCustomerID() + " wait in the line");
                }
            } else {
                System.out.println("\tNo new customer");
            }
            // Step 2: free busy tellers that are done at currentTime, add to freeTellerQ
            if (servicearea.emptyBusyTellerQ()) {
                //System.out.println("\tNo busy teller");
            } else if (servicearea.getFrontBusyTellerQ().getEndBusyTime() <= currentTime) {
                System.out.println("\tteller # " + servicearea.getFrontBusyTellerQ().getTellerID()
                        + " finished helping customer # " + servicearea.getFrontBusyTellerQ().getCustomer().getCustomerID());
                servicearea.getFrontBusyTellerQ().busyToFree();
                servicearea.insertFreeTellerQ(servicearea.removeBusyTellerQ());
                numServed++;
            }
            // Step 3: get free tellers to serve waiting customers at currentTime
            if (servicearea.emptyCustomerQ()) {
                System.out.println("\tNo customers in line");
            } else if (servicearea.emptyFreeTellerQ()) {
                System.out.println("\tNo teller available");
            } else {
                Teller nextTeller = servicearea.removeFreeTellerQ();
                Customer nextCustomer = servicearea.removeCustomerQ();
                System.out.println("\tcustomer # " + nextCustomer.getCustomerID() + " gets a teller");
                System.out.println("\tteller # " + nextTeller.getTellerID() + " starts serving customer # "
                        + nextCustomer.getCustomerID() + " for " + nextCustomer.getTransactionTime() + " units");
                nextTeller.freeToBusy(nextCustomer, currentTime);
                servicearea.insertBusyTellerQ(nextTeller);
            }
        } 
    }

    private void printStatistics() {
        System.out.println("----------------------------------------\n\nEnd of simulation report\n\n----------------------------------------");
        System.out.println("\t# total arrival customers  		: " + customerIDCounter);
        System.out.println("\t# customers turned-away        		: " + numGoaway);
        System.out.println("\t# customers served         		: " + numServed + "\n");
        System.out.println("\t*** Current Tellers Info ***\n\n");
        servicearea.printStatistics();
        System.out.println("\n\n\tTotal waiting time      : " + totalWaitingTime);
        System.out.println("\tAverage waiting time        : " + (double) totalWaitingTime / (customerIDCounter - numGoaway));
        System.out.println("\n\n\tBusy Tellers (" + servicearea.numBusyTellers() + ") Info\n\n");
        for (int i = 0; i < servicearea.numBusyTellers();) {
            Teller nextBusyTeller = servicearea.removeBusyTellerQ();
            nextBusyTeller.printStatistics();
        }
        System.out.println("\n\tFree Tellers (" + servicearea.numFreeTellers() + ") Info\n\n");
        for (int i = 0; i < servicearea.numFreeTellers();) {
            Teller nextFreeTeller = servicearea.removeFreeTellerQ();
            nextFreeTeller.printStatistics();
            System.out.println("\n");
        }
    }

    // *** main method to run simulation ****
    public static void main(String[] args) {
        BankSimulator runBankSimulator = new BankSimulator();
        runBankSimulator.setupParameters();
        runBankSimulator.doSimulation();
        runBankSimulator.printStatistics();
    }

}
