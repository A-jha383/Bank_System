package Bank;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

interface SavingsAccount {
	final double rate = 0.05, limit = 100000, limit1 = 300;

	void deposit(double n, Date d);

	void withdraw(double n, Date d);
}

class Customer implements SavingsAccount {

	private String username, name, password, address, phone;
	private double balance;
	ArrayList<String> transactions;

	// constructor

	public Customer(String username, String password, String name, String address, String phone, double balance,
			Date date) {
		this.username = username;
		this.password = password;
		this.name = name;
		this.address = address;
		this.phone = phone;
		this.balance = balance;
		this.transactions = new ArrayList<String>(5);
		addTransaction("Initial deposit - " + this.balance + " as on " + date);
		System.out.println("Account created successfully");

	}

	private void addTransaction(String detail) {
		// TODO Auto-generated method stub
		transactions.add(0, detail);
		if (transactions.size() > 5) {
			transactions.remove(5);
			transactions.trimToSize();
		}

	}

	@Override
	public void deposit(double amount, Date d) {
		// TODO Auto-generated method stub
		balance += amount;
		addTransaction(amount + " credited to your account. Balance - " + this.balance + " as on " + d);

	}

	@Override
	public void withdraw(double amount, Date d) {
		// TODO Auto-generated method stub
		if (amount > (balance -limit1 )&& amount<limit) {
			System.out.println("Insufficient balance.");
			System.out.println("RETRY");
		} else {
			balance -= amount;
			addTransaction(amount + " debited from your account. Balance - " + this.balance + " as on " + d);
		}
		System.out.println("Balance left :" + this.balance);

	}

	void update(Date date) {
		if (balance >= 2000) {
			balance += rate * balance;
		} else {
			balance -= (int) (balance / 100.0);
		}
		addTransaction("Account updated. Balance - " + this.balance + " as on " + date);
		System.out.println("CURRENT BALANCE IS: " + this.balance);
	}

	public boolean checkPass(String pass) {
		return this.password.equals(pass);
	}

	public void Details() {
		System.out.println("Username : " + this.username);
		System.out.println("Accountholder name : " + this.name);
		System.out.println("Accountholder address : " + this.address);
		System.out.println("Accountholder contact : " + this.phone);
		System.out.println("Balance: " + this.balance);
	}
}

public class BankSystem {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		HashMap<String, Customer> bank = new HashMap<String, Customer>();
		Scanner sc = new Scanner(System.in);
		Customer customer;
		String username, password;
		double amount;
		int choice;
		while (true) {
			System.out.println("\n-------------------");
			System.out.println("BANK    OF     JAVA");
			System.out.println("-------------------\n");
			System.out.println("1. Register account.");
			System.out.println("2. Login.");
			System.out.println("3. Update accounts.");
			System.out.println("4. Exit.");
			System.out.print("\nEnter your choice : ");
			choice = sc.nextInt();
			switch (choice) {
			case 1: {
				System.out.print("Enter name : ");
				sc.nextLine();
				String name = sc.nextLine();
				System.out.print("Enter address : ");
				String address = sc.nextLine();
				System.out.print("Enter contact number : ");
				String phone = sc.nextLine();
				System.out.println("Set username : ");
				username = sc.next();
				while (bank.containsKey(username)) {
					System.out.println("Username already exists. Set again : ");
					username = sc.next();
				}
				System.out.println(
						"Set a password (minimum 8 chars; minimum 1 digit, 1 lowercase, 1 uppercase, 1 special character[!@#$%^&*_]) :");
				password = sc.next();
				while (!password.matches((("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*_]).{8,}")))) {
					System.out.println("Invalid password condition. Set again :");
					password = sc.next();
				}
				System.out.print("Enter initial deposit : ");
				amount = sc.nextDouble();
				sc.nextLine();
				customer = new Customer(username, password, name, address, phone, amount, new Date());
				bank.put(username, customer);

				break;
			}
			case 2: {
				System.out.println("Enter username : ");
				username = sc.next();
				System.out.println("Enter password : ");
				password = sc.next();
				if (bank.containsKey(username)) {
					customer = bank.get(username);
					if (customer.checkPass(password)) {
						while (true) {
							System.out.println("\n-------------------");
							System.out.println("W  E  L  C  O  M  E");
							System.out.println("-------------------\n");
							System.out.println("1. Deposit.");
							System.out.println("2. Transfer.");
							System.out.println("3. Last 5 transactions.");
							System.out.println("4. User information.");
							System.out.println("5. Withdraw");
							System.out.println("6. Log out.");
							System.out.print("\nEnter your choice : ");
							choice = sc.nextInt();
							sc.nextLine();
							int flag = 0;
							switch (choice) {
							case 1:
								System.out.print("Enter amount : ");
								while (!sc.hasNextDouble()) {
									System.out.println("Invalid amount. Enter again :");
									sc.nextLine();
								}
								amount = sc.nextDouble();
								sc.nextLine();
								customer.deposit(amount, new Date());
								break;
							case 2:
								System.out.print("Enter payee username : ");
								username = sc.next();
								sc.nextLine();
								System.out.println("Enter amount : ");
								while (!sc.hasNextDouble()) {
									System.out.println("Invalid amount. Enter again :");
									sc.nextLine();
								}
								amount = sc.nextDouble();
								sc.nextLine();
								if (amount > 300000) {
									System.out.println("Transfer limit exceeded. Contact bank manager.");
									break;
								}
								if (bank.containsKey(username)) {
									Customer payee = bank.get(username);
									payee.deposit(amount, new Date());
									customer.withdraw(amount, new Date());
								} else {
									System.out.println("Username doesn't exist.");
								}
								break;
							case 3:
								for (String transactions : customer.transactions) {
									System.out.println(transactions);
								}
								break;
							case 4:
								customer.Details();
								break;
							case 5: {
								System.out.println("Enter withdrawal amount:");
								double value = sc.nextDouble();
								customer.withdraw(value, new Date());
								break;
							}
							case 6: {
								flag = 1;
								break;
							}
							default:
								System.out.println("Wrong choice !");
							}
							if (flag != 0) {
								break;
							}
						}
					} else {
						System.out.println("Wrong username/password.");
					}
				} else {
					System.out.println("Wrong username/password. 1");
				}
				break;
			}
			case 3: {
				System.out.println("Enter username : ");
				username = sc.next();
				if (bank.containsKey(username)) {
					bank.get(username).update(new Date());
				} else {
					System.out.println("Username doesn't exist.");
				}
				break;
			}
			case 4: {
				System.out.println("\nThank you for choosing Bank Of Java.");
				System.exit(1);
				break;
			}
			default:
				System.out.println("Wrong choice !");
			}

		}
	}

}
