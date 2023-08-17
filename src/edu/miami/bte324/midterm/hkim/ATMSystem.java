package edu.miami.bte324.midterm.hkim;

import java.util.ArrayList;

public class ATMSystem extends Global {

	public static void main(String[] args) {
			
		ATMSystem ATM = new ATMSystem();
		ATM.run();

	}
		
	public void run() {
		
		ArrayList<Client> clientList = new ArrayList<Client>();
		
		readData(clientList);
		sortClient(clientList);
		printSortedClient(clientList);
		logIn(clientList);
		
	}

}
