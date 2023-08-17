package edu.miami.bte324.midterm.hkim;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Scanner;

public abstract class Global {
	
	protected static void readData(ArrayList<Client> cList) {
		
		try {
			
			Scanner input = new Scanner(new File("Client.txt"));
			
			while(input.hasNextLine()) {
				String clientData = input.nextLine();
				loadClient(clientData, cList);
			}
			
			input.close();
			
		} catch (FileNotFoundException e) {e.printStackTrace();}
		
	}
	
	protected static void loadClient(String data, ArrayList<Client> cList) {
		
		String[] clientData = data.split(" ");
		
		String fullName = clientData[2] + " " + clientData[3];
		String SSN = clientData[1];
		Date birthDate = convertToDate(clientData[4]);
		
		Integer accountNumber = Integer.parseInt(clientData[0]);
		Integer savingsBalance = Integer.parseInt(clientData[5]);
		Integer checkingBalance = Integer.parseInt(clientData[6]);

		Client newClient = new ClientImpl(fullName, SSN, birthDate, checkingBalance, savingsBalance, accountNumber);
		cList.add(newClient);
		
	}
	
	protected static void sortClient(ArrayList<Client> cList) {
		
		Collections.sort(cList, new Comparator<Client>() {
			public int compare(Client client1, Client client2) {
				return client1.getBirthDate().compareTo(client2.getBirthDate());
			}
		});
		
	}
	
	protected static void printSortedClient(ArrayList<Client> cList) {
		
		String fileName = "SortedClientList.txt";
		
		try {
			
			File sortedList = new File(fileName);
			
			if(!sortedList.exists()) {
				sortedList.createNewFile();
			}
			
			PrintWriter list = new PrintWriter(sortedList);
			
			list.println("Sorted Client List by Date of Birth:");
			list.println();
			
			for(int i = 0; i < cList.size(); i++) {
				
				list.println(cList.get(i).toString());
				
			}
			
			list.close();
			
		} catch (IOException e) {e.printStackTrace();}
		
		
	}
	
	protected static void logIn(ArrayList<Client> cList) {
		
		Scanner last = new Scanner(System.in);
		Scanner account = new Scanner(System.in);
		
		int[] location = {0};
		
		System.out.println("-----Welcome to the ATM System-----");
		
		System.out.print("\nLast Name: ");
		String user = last.nextLine();
	
		System.out.print("Account Number: ");
		Integer password = account.nextInt();
				
		if (authenticate(user, password, location, cList)) {
			
			int userLocation = location[0];
			
			System.out.println("\nHello " + cList.get(userLocation).getFirstName() + ",");	
			
			Scanner option = new Scanner(System.in);
			char userChoice = ' ';
			
			do {
				
				System.out.println("\nPlease select an option.");
				System.out.println("\nCheck Balance (C/c)");
				System.out.println("Deposit Balance (D/d)");
				System.out.println("Withdraw Balance (W/w)");
				System.out.println("Exit (#)");
				System.out.print("\nOption: ");
				
				userChoice = option.next().charAt(0);
				
				if(userChoice == 'C' || userChoice == 'c')
					checkBalance(cList, userLocation, option);

				else if (userChoice == 'D' || userChoice == 'd')
					depositBalance(cList, userLocation, option);
			
				else if (userChoice == 'W' || userChoice == 'w')
					withdrawBalance(cList, userLocation, option);
				
				else if (userChoice == '#') {
					System.out.println("\nSuccessfully logged out.");
					break;
				}
				
				else {
					System.out.println("\nInvalid option.");
					System.out.print("Please choose another option.");
					System.out.println();
					continue;
				}
				
			} while (userChoice != '#');

			option.close();
			
		}
			
		else {
			
			System.out.println("\nIncorrect credentials.");
			System.out.println("Please try again.");
			System.out.println();
			
			logIn(cList);
			
		}
	
		last.close();
		account.close();
		
	}
	
	private static boolean authenticate(String u, Integer p, int[] loc, ArrayList<Client> cList) {
		
		for(int i = 0; i < cList.size(); i++) {
			
			if(p.equals(cList.get(i).getAccountNumber()) && u.contentEquals(cList.get(i).getLastName())) {
				loc[0] = i;
				return true;
			}
			
		}
		
		return false;
		
	}
		
	private static void checkBalance(ArrayList<Client> cList, int userLoc, Scanner opt) {
		
		Date today = Calendar.getInstance().getTime();
		Format date = new SimpleDateFormat("MMMMM dd, yyyy");
		String formattedDate = date.format(today);
		
		Format time = new SimpleDateFormat("(HH:mm)");
		String formattedTime = time.format(today);
		
		System.out.println("\n---------------------------------");
		System.out.println("\nCURRENT BALANCE");
		System.out.print("\nClient ID: ");
		System.out.print(cList.get(userLoc).getClientID());
		System.out.print("\nDate: ");
		System.out.print(formattedDate + " " + formattedTime);
		System.out.print("\n\tSavings Balance: ");
		System.out.print("$" + cList.get(userLoc).getSavingsBalance());
		System.out.print("\n\tChecking Balance: ");
		System.out.println("$" + cList.get(userLoc).getCheckingBalance());
		System.out.println("\n---------------------------------");
		
		System.out.print("\nPrint receipt (P/p): ");
		char userChoice = opt.next().charAt(0);
		
		if(userChoice == 'P' || userChoice == 'p') {
			System.out.println("\nReceipt printed.");
			printBalanceReceipt(cList, userLoc, formattedDate, formattedTime);
		}
	
	}
	
	private static void printBalanceReceipt(ArrayList<Client> cList, int userLoc, String fDate, String fTime) {
		
		String receiptName = fDate + "_" + cList.get(userLoc).getLastName() + "_Balance.txt";
		
		try {
			
			File balanceReceipt = new File(receiptName);
			
			if(!balanceReceipt.exists()) {
				balanceReceipt.createNewFile();
			}
			
			PrintWriter receipt = new PrintWriter(balanceReceipt);
			
			receipt.println("\n---------------------------------");
			receipt.println("\nCURRENT BALANCE RECEIPT");
			receipt.print("\nClient ID: ");
			receipt.print(cList.get(userLoc).getClientID());
			receipt.print("\nDate: ");
			receipt.print(fDate + " " + fTime);
			receipt.print("\n\tSavings Balance: ");
			receipt.print("$" + cList.get(userLoc).getSavingsBalance());
			receipt.print("\n\tChecking Balance: ");
			receipt.println("$" + cList.get(userLoc).getCheckingBalance());
			receipt.println("\n---------------------------------");
			receipt.close();
			
		} catch (IOException e) {e.printStackTrace();}
		
	}
		
	private static void depositBalance(ArrayList<Client> cList, int userLoc, Scanner opt) {
		
		Date today = Calendar.getInstance().getTime();
		Format date = new SimpleDateFormat("MMMMM dd, yyyy");
		String formattedDate = date.format(today);
		
		Format time = new SimpleDateFormat("(HH:mm)");
		String formattedTime = time.format(today);
		
		System.out.println("\nDEPOSIT BALANCE");
		System.out.print("Amount: ");
		int deposit = opt.nextInt();
		
		System.out.print("Savings (S/s) / Checking (C/c): ");
		char balance = opt.next().charAt(0);
		
		if(balance == 'S' || balance == 's') {
			
			int sBalance = cList.get(userLoc).getSavingsBalance();
			cList.get(userLoc).setSavingsBalance(sBalance + deposit);
			
			System.out.println("\n---------------------------------------");
			System.out.println("\nDEPOSIT (SAVINGS) RECEIPT");
			System.out.print("\nClient ID: ");
			System.out.print(cList.get(userLoc).getClientID());
			System.out.print("\nDate: ");
			System.out.print(formattedDate + " " + formattedTime);
			System.out.print("\n\tDeposit Amount: ");
			System.out.print("$" + deposit);
			System.out.println("\n\tSavings Balance: ");
			System.out.print("\t\tBefore Deposit: ");
			System.out.print("$" + (cList.get(userLoc).getSavingsBalance() - deposit));
			System.out.print("\n\t\tAfter Deposit: ");
			System.out.print("$" + (cList.get(userLoc).getSavingsBalance()));
			System.out.println("\n---------------------------------------");
			
			System.out.print("\nPrint receipt (P/p): ");
			char userChoice = opt.next().charAt(0);
			
			if(userChoice == 'P' || userChoice == 'p') {
				System.out.println("\nReceipt printed.");
				printDepositReceiptSavings(cList, userLoc, deposit, formattedDate, formattedTime);
			}
			
		}
		
		else if(balance == 'C' || balance == 'c') {
			
			int cBalance = cList.get(userLoc).getCheckingBalance();
			cList.get(userLoc).setCheckingBalance(cBalance + deposit);
			
			System.out.println("\n-----------------------------------------");
			System.out.println("\nDEPOSIT (CHECKING) RECEIPT");
			System.out.print("Client ID: ");
			System.out.print(cList.get(userLoc).getClientID());
			System.out.print("\nDate: ");
			System.out.print(formattedDate + " " + formattedTime);
			System.out.print("\n\tDeposit Amount: ");
			System.out.print("$" + deposit);
			System.out.println("\n\tChecking Balance: ");
			System.out.print("\t\tBefore Deposit: ");
			System.out.print("$" + (cList.get(userLoc).getCheckingBalance() - deposit));
			System.out.print("\n\t\tAfter Deposit: ");
			System.out.print("$" + (cList.get(userLoc).getCheckingBalance()));
			System.out.println("\n-----------------------------------------");
			
			System.out.print("\nPrint receipt (P/p): ");
			char userChoice = opt.next().charAt(0);
			
			if(userChoice == 'P' || userChoice == 'p') {
				System.out.println("\nReceipt printed.");
				printDepositReceiptChecking(cList, userLoc, deposit, formattedDate, formattedTime);
			}
			
		}
		
		else {
			System.out.println("\nInvalid option.");
			System.out.println("Transaction failed.");
		}
		
	}
	
	private static void printDepositReceiptSavings(ArrayList<Client> cList, int userLoc, int deposit, String fDate, String fTime) {
		
		String receiptName = fDate + "_" + cList.get(userLoc).getLastName() + "_Deposit(Savings).txt";
		
		try {
			
			File balanceReceipt = new File(receiptName);
			
			if(!balanceReceipt.exists()) {
				balanceReceipt.createNewFile();
			}
			
			PrintWriter receipt = new PrintWriter(balanceReceipt);
			
			receipt.println("\n---------------------------------------");
			receipt.println("\nDEPOSIT (SAVINGS) RECEIPT");
			receipt.print("\nClient ID: ");
			receipt.print(cList.get(userLoc).getClientID());
			receipt.print("\nDate: ");
			receipt.print(fDate + " " + fTime);
			receipt.print("\n\tDeposit Amount: ");
			receipt.print("$" + deposit);
			receipt.println("\n\tSavings Balance: ");
			receipt.print("\t\tBefore Deposit: ");
			receipt.print("$" + (cList.get(userLoc).getSavingsBalance() - deposit));
			receipt.print("\n\t\tAfter Deposit: ");
			receipt.print("$" + (cList.get(userLoc).getSavingsBalance()));
			receipt.println("\n---------------------------------------");
			receipt.close();
			
		} catch (IOException e) {e.printStackTrace();}
		
	}
	
	private static void printDepositReceiptChecking(ArrayList<Client> cList, int userLoc, int deposit, String fDate, String fTime) {
		
		String receiptName = fDate + "_" + cList.get(userLoc).getLastName() + "_Deposit(Checking).txt";
		
		try {
			
			File balanceReceipt = new File(receiptName);
			
			if(!balanceReceipt.exists()) {
				balanceReceipt.createNewFile();
			}
			
			PrintWriter receipt = new PrintWriter(balanceReceipt);
			
			receipt.println("\n---------------------------------------");
			receipt.println("\nDEPOSIT (CHECKING) RECEIPT");
			receipt.print("\nClient ID: ");
			receipt.print(cList.get(userLoc).getClientID());
			receipt.print("\nDate: ");
			receipt.print(fDate + " " + fTime);
			receipt.print("\n\tDeposit Amount: ");
			receipt.print("$" + deposit);
			receipt.println("\n\tChecking Balance: ");
			receipt.print("\t\tBefore Deposit: ");
			receipt.print("$" + (cList.get(userLoc).getCheckingBalance() - deposit));
			receipt.print("\n\t\tAfter Deposit: ");
			receipt.print("$" + (cList.get(userLoc).getCheckingBalance()));
			receipt.println("\n---------------------------------------");
			receipt.close();
			
		} catch (IOException e) {e.printStackTrace();}
		
	}
	
	private static void withdrawBalance(ArrayList<Client> cList, int userLoc, Scanner opt) {
		
		Date today = Calendar.getInstance().getTime();
		Format date = new SimpleDateFormat("MMMMM dd, yyyy");
		String formattedDate = date.format(today);
		
		Format time = new SimpleDateFormat("(HH:mm)");
		String formattedTime = time.format(today);
		
		System.out.println("\nWITHDRAW BALANCE");
		System.out.print("Amount: ");
		int withdrawal = opt.nextInt();
		
		System.out.print("Savings (S/s) / Checking (C/c): ");
		char balance = opt.next().charAt(0);
		
		if(balance == 'S' || balance == 's') {
			
			if (withdrawal <= cList.get(userLoc).getSavingsBalance()) {
			
				int sBalance = cList.get(userLoc).getSavingsBalance();
				cList.get(userLoc).setSavingsBalance(sBalance - withdrawal);
		
				System.out.println("\n-----------------------------------------");
				System.out.println("\nWITHDRAWAL (SAVINGS) RECEIPT");
				System.out.print("\nClient ID: ");
				System.out.print(cList.get(userLoc).getClientID());
				System.out.print("\nDate: ");
				System.out.print(formattedDate + " " + formattedTime);
				System.out.print("\n\tDeposit Amount: ");
				System.out.print("$" + withdrawal);
				System.out.println("\n\tSavings Balance: ");
				System.out.print("\t\tBefore Withdrawal: ");
				System.out.print("$" + (cList.get(userLoc).getSavingsBalance() + withdrawal));
				System.out.print("\n\t\tAfter Withdrawal: ");
				System.out.print("$" + (cList.get(userLoc).getSavingsBalance()));
				System.out.println("\n-----------------------------------------");
			
				System.out.print("\nPrint receipt (P/p): ");
				char userChoice = opt.next().charAt(0);
			
				if(userChoice == 'P' || userChoice == 'p') {
					System.out.println("\nReceipt printed.");
					printWithdrawalReceiptSavings(cList, userLoc, withdrawal, formattedDate, formattedTime);	
				}
			
			}
			
			else {
				System.out.println("\nInsufficient funds.");
				System.out.println("Transaction failed.");
			}
			
		}
		
		else if(balance == 'C' || balance == 'c') {
			
			if (withdrawal <= cList.get(userLoc).getCheckingBalance()) {
			
				int cBalance = cList.get(userLoc).getCheckingBalance();
				cList.get(userLoc).setCheckingBalance(cBalance - withdrawal);
			
				System.out.println("\n-----------------------------------------");
				System.out.println("\nWITHDRAWAL (CHECKING) RECEIPT");
				System.out.print("\nClient ID: ");
				System.out.print(cList.get(userLoc).getClientID());
				System.out.print("\nDate: ");
				System.out.print(formattedDate + " " + formattedTime);
				System.out.print("\n\tDeposit Amount: ");
				System.out.print("$" + withdrawal);
				System.out.println("\n\tChecking Balance: ");
				System.out.print("\t\tBefore Withdrawal: ");
				System.out.print("$" + (cList.get(userLoc).getCheckingBalance() + withdrawal));
				System.out.print("\n\t\tAfter Withdrawal: ");
				System.out.print("$" + (cList.get(userLoc).getCheckingBalance()));
				System.out.println("\n-----------------------------------------");
			
				System.out.print("\nPrint receipt (P/p): ");
				char userChoice = opt.next().charAt(0);
			
				if(userChoice == 'P' || userChoice == 'p') {
					System.out.println("\nReceipt printed.");
					printWithdrawalReceiptChecking(cList, userLoc, withdrawal, formattedDate, formattedTime);	
				}
			
			}
			
			else {
				System.out.println("\nInsufficient funds.");
				System.out.println("Transaction failed.");
			}
			
		}
		
		else {
			System.out.println("\nInvalid option.");
			System.out.println("Transaction failed.");
		}
		
	}
	
	private static void printWithdrawalReceiptSavings(ArrayList<Client> cList, int userLoc, int withdrawal, String fDate, String fTime) {
		
		String receiptName = fDate + "_" + cList.get(userLoc).getLastName() + "_Withdrawal(Savings).txt";
		
		try {
			
			File balanceReceipt = new File(receiptName);
			
			if(!balanceReceipt.exists()) {
				balanceReceipt.createNewFile();
			}
			
			PrintWriter receipt = new PrintWriter(balanceReceipt);
			
			receipt.println("\n---------------------------------------");
			receipt.println("\nWITHDRAWAL (SAVINGS) RECEIPT");
			receipt.print("\nClient ID: ");
			receipt.print(cList.get(userLoc).getClientID());
			receipt.print("\nDate: ");
			receipt.print(fDate + " " + fTime);
			receipt.print("\n\tWithdrawal Amount: ");
			receipt.print("$" + withdrawal);
			receipt.println("\n\tSavings Balance: ");
			receipt.print("\t\tBefore Withdrawal: ");
			receipt.print("$" + (cList.get(userLoc).getSavingsBalance() - withdrawal));
			receipt.print("\n\t\tAfter Withdrawal: ");
			receipt.print("$" + (cList.get(userLoc).getSavingsBalance()));
			receipt.println("\n---------------------------------------");
			receipt.close();
			
		} catch (IOException e) {e.printStackTrace();}
		
	}
	
	private static void printWithdrawalReceiptChecking(ArrayList<Client> cList, int userLoc, int withdrawal, String fDate, String fTime) {
		
		String receiptName = fDate + "_" + cList.get(userLoc).getLastName() + "_Withdrawal(Checking).txt";
		
		try {
			
			File balanceReceipt = new File(receiptName);
			
			if(!balanceReceipt.exists()) {
				balanceReceipt.createNewFile();
			}
			
			PrintWriter receipt = new PrintWriter(balanceReceipt);
			
			receipt.println("\n---------------------------------------");
			receipt.println("\nWITHDRAWAL (CHECKING) RECEIPT");
			receipt.print("\nClient ID: ");
			receipt.print(cList.get(userLoc).getClientID());
			receipt.print("\nDate: ");
			receipt.print(fDate + " " + fTime);
			receipt.print("\n\tWithdrawal Amount: ");
			receipt.print("$" + withdrawal);
			receipt.println("\n\tChecking Balance: ");
			receipt.print("\t\tBefore Withdrawal: ");
			receipt.print("$" + (cList.get(userLoc).getCheckingBalance() - withdrawal));
			receipt.print("\n\t\tAfter Withdrawal: ");
			receipt.print("$" + (cList.get(userLoc).getCheckingBalance()));
			receipt.println("\n---------------------------------------");
			receipt.close();
			
		} catch (IOException e) {e.printStackTrace();}
		
	}
	
	private static Date convertToDate(String sDate) {
		
		SimpleDateFormat dateformat = new SimpleDateFormat("MM/dd/yyyy");
		Date convertedDate = new Date();
		
		try {convertedDate = dateformat.parse(sDate);} 
		catch(ParseException e) {e.printStackTrace();}
		
		return convertedDate;
		
	}
	
}
