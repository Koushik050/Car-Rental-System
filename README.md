🚗 Car Rental System – Java Console Application
📌 Overview

The Car Rental System is a console-based Java application that simulates a real-world car rental service.
It allows users to rent cars, return them, view active rentals, and calculate rental costs with discounts.
Data persistence is handled via text files (cars.txt, customers.txt) to store car and customer information across sessions.

This project demonstrates Java OOP concepts, file handling, collections, and user interaction – making it an excellent mini-project for learning and showcasing Java development skills.

⚙️ Features

Car Management

View available cars

Rent and return cars

Track car availability

Customer Management

Add new customers or reuse existing ones

Assign rentals to specific customers

Rental System

Calculate rental cost based on number of days

Apply 10% discount for rentals longer than 7 days

View active rentals

Persistence

Saves data in text files (cars.txt, customers.txt)

Reloads data when the system restarts

User Interaction

Menu-driven interface

Input validation to avoid crashes

🛠️ Tech Stack

Language: Java (JDK 8 or above)

Concepts Used:

Object-Oriented Programming (OOP)

Collections (ArrayList)

File Handling (I/O Streams)

Exception Handling

Menu-driven console UI

🚀 Getting Started
1. Clone or Download
git clone https://github.com/your-username/car-rental-system-java.git
cd car-rental-system-java

2. Compile
javac Main.java

3. Run
java Main

📂 Project Structure
CarRentalSystem/
│── Main.java              # Entry point
│── cars.txt               # Stores car data
│── customers.txt          # Stores customer data
│── README.md              # Documentation

🎮 Sample Run
===== Car Rental System =====
1. Rent a Car
2. Return a Car
3. View Active Rentals
4. Exit
Enter your choice: 1

Enter your name: Koushik

Available Cars:
C001 - Toyota Camry
C002 - Honda Accord
C003 - Mahindra Thar

Enter the car ID you want to rent: C003
Enter rental days: 10

== Rental Information ==
Customer ID: CUS1
Customer Name: Koushik
Car: Mahindra Thar
Rental Days: 10
Total Price: $1350.00   (10% discount applied)

Confirm rental (Y/N): Y
Car rented successfully!

📌 Future Enhancements

✅ Replace text file storage with MySQL (JDBC)

✅ Add a Graphical User Interface (GUI) using JavaFX or Swing

✅ Build a REST API with Spring Boot for full-stack integration

✅ Add late fee system, loyalty points, and admin panel

👨‍💻 Author

Koushik Rangu
📧 koushikrangu@gmail.com

💼 LinkedIn

💻 GitHub

