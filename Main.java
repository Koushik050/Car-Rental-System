import java.io.*;
import java.util.*;

// ---------------- Car Class ----------------
class Car {
    private String carId;
    private String brand;
    private String model;
    private double basePricePerDay;
    private boolean isAvailable;

    public Car(String carId, String brand, String model, double basePricePerDay) {
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.basePricePerDay = basePricePerDay;
        this.isAvailable = true;
    }

    public String getCarId() {
        return carId;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void rent() {
        isAvailable = false;
    }

    public void returnCar() {
        isAvailable = true;
    }

    // Discount: 10% off for rentals > 7 days
    public double calculatePrice(int rentalDays) {
        double price = basePricePerDay * rentalDays;
        if (rentalDays > 7) {
            price *= 0.9; // 10% discount
        }
        return price;
    }

    @Override
    public String toString() {
        return carId + "," + brand + "," + model + "," + basePricePerDay + "," + isAvailable;
    }

    public static Car fromString(String line) {
        String[] parts = line.split(",");
        Car car = new Car(parts[0], parts[1], parts[2], Double.parseDouble(parts[3]));
        if (!Boolean.parseBoolean(parts[4])) {
            car.rent();
        }
        return car;
    }
}

// ---------------- Customer Class ----------------
class Customer {
    private String customerId;
    private String name;

    public Customer(String customerId, String name) {
        this.customerId = customerId;
        this.name = name;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return customerId + "," + name;
    }

    public static Customer fromString(String line) {
        String[] parts = line.split(",");
        return new Customer(parts[0], parts[1]);
    }
}

// ---------------- Rental Class ----------------
class Rental {
    private Car car;
    private Customer customer;
    private int days;

    public Rental(Car car, Customer customer, int days) {
        this.car = car;
        this.customer = customer;
        this.days = days;
    }

    public Car getCar() {
        return car;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getDays() {
        return days;
    }

    @Override
    public String toString() {
        return car.getCarId() + "," + customer.getCustomerId() + "," + days;
    }
}

// ---------------- CarRentalSystem Class ----------------
class CarRentalSystem {
    private List<Car> cars;
    private List<Customer> customers;
    private List<Rental> rentals;
    private Scanner scanner;

    public CarRentalSystem() {
        cars = new ArrayList<>();
        customers = new ArrayList<>();
        rentals = new ArrayList<>();
        scanner = new Scanner(System.in);

        loadCars();
        loadCustomers();
    }

    // ---------------- Persistence ----------------
    private void loadCars() {
        try (BufferedReader br = new BufferedReader(new FileReader("cars.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                cars.add(Car.fromString(line));
            }
        } catch (IOException e) {
            System.out.println("No existing car data found, starting fresh.");
        }
    }

    private void saveCars() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("cars.txt"))) {
            for (Car car : cars) {
                bw.write(car.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving cars.");
        }
    }

    private void loadCustomers() {
        try (BufferedReader br = new BufferedReader(new FileReader("customers.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                customers.add(Customer.fromString(line));
            }
        } catch (IOException e) {
            System.out.println("No existing customer data found, starting fresh.");
        }
    }

    private void saveCustomers() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("customers.txt"))) {
            for (Customer customer : customers) {
                bw.write(customer.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving customers.");
        }
    }

    // ---------------- Core Logic ----------------
    public void addCar(Car car) {
        cars.add(car);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public void rentCar(Car car, Customer customer, int days) {
        if (car.isAvailable()) {
            car.rent();
            rentals.add(new Rental(car, customer, days));
            saveCars();
            saveCustomers();
        } else {
            System.out.println("Car is not available for rent.");
        }
    }

    public void returnCar(Car car) {
        car.returnCar();
        Rental rentalToRemove = null;
        for (Rental rental : rentals) {
            if (rental.getCar() == car) {
                rentalToRemove = rental;
                break;
            }
        }
        if (rentalToRemove != null) {
            rentals.remove(rentalToRemove);
            saveCars();
        }
    }

    public void viewActiveRentals() {
        if (rentals.isEmpty()) {
            System.out.println("No active rentals.");
        } else {
            System.out.println("\n== Active Rentals ==\n");
            for (Rental rental : rentals) {
                System.out.println("Car: " + rental.getCar().getBrand() + " " + rental.getCar().getModel() +
                        " | Customer: " + rental.getCustomer().getName() +
                        " | Days: " + rental.getDays());
            }
        }
    }

    // ---------------- Menu ----------------
    public void menu() {
        while (true) {
            System.out.println("\n===== Car Rental System =====");
            System.out.println("1. Rent a Car");
            System.out.println("2. Return a Car");
            System.out.println("3. View Active Rentals");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1 -> rentCarMenu();
                case 2 -> returnCarMenu();
                case 3 -> viewActiveRentals();
                case 4 -> {
                    saveCars();
                    saveCustomers();
                    System.out.println("Thank you for using the Car Rental System!");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void rentCarMenu() {
        System.out.print("Enter your name: ");
        String customerName = scanner.nextLine();

        // Check if customer exists
        Customer existingCustomer = null;
        for (Customer c : customers) {
            if (c.getName().equalsIgnoreCase(customerName)) {
                existingCustomer = c;
                break;
            }
        }
        Customer newCustomer = (existingCustomer != null) ?
                existingCustomer : new Customer("CUS" + (customers.size() + 1), customerName);

        if (existingCustomer == null) addCustomer(newCustomer);

        System.out.println("\nAvailable Cars:");
        for (Car car : cars) {
            if (car.isAvailable()) {
                System.out.println(car.getCarId() + " - " + car.getBrand() + " " + car.getModel());
            }
        }

        System.out.print("\nEnter the car ID you want to rent: ");
        String carId = scanner.nextLine();

        Car selectedCar = null;
        for (Car car : cars) {
            if (car.getCarId().equalsIgnoreCase(carId) && car.isAvailable()) {
                selectedCar = car;
                break;
            }
        }

        if (selectedCar == null) {
            System.out.println("Invalid car ID or car not available.");
            return;
        }

        System.out.print("Enter rental days: ");
        int rentalDays;
        try {
            rentalDays = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number of days.");
            return;
        }

        double totalPrice = selectedCar.calculatePrice(rentalDays);
        System.out.println("\n== Rental Information ==\n");
        System.out.println("Customer ID: " + newCustomer.getCustomerId());
        System.out.println("Customer Name: " + newCustomer.getName());
        System.out.println("Car: " + selectedCar.getBrand() + " " + selectedCar.getModel());
        System.out.println("Rental Days: " + rentalDays);
        System.out.printf("Total Price: $%.2f%n", totalPrice);

        System.out.print("\nConfirm rental (Y/N): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("Y")) {
            rentCar(selectedCar, newCustomer, rentalDays);
            System.out.println("Car rented successfully!");
        } else {
            System.out.println("Rental cancelled.");
        }
    }

    private void returnCarMenu() {
        System.out.print("Enter car ID to return: ");
        String carId = scanner.nextLine();

        Car carToReturn = null;
        for (Car car : cars) {
            if (car.getCarId().equalsIgnoreCase(carId) && !car.isAvailable()) {
                carToReturn = car;
                break;
            }
        }

        if (carToReturn == null) {
            System.out.println("Invalid car ID or car is not rented.");
            return;
        }

        Customer customer = null;
        for (Rental rental : rentals) {
            if (rental.getCar() == carToReturn) {
                customer = rental.getCustomer();
                break;
            }
        }

        if (customer != null) {
            returnCar(carToReturn);
            System.out.println("Car returned successfully by " + customer.getName());
        } else {
            System.out.println("Car rental record not found.");
        }
    }
}

// ---------------- Main Class ----------------
public class Main {
    public static void main(String[] args) {
        CarRentalSystem rentalSystem = new CarRentalSystem();

        if (rentalSystem == null) return;

        // Preload cars if file is empty
        if (new File("cars.txt").length() == 0) {
            rentalSystem.addCar(new Car("C001", "Toyota", "Camry", 60.0));
            rentalSystem.addCar(new Car("C002", "Honda", "Accord", 70.0));
            rentalSystem.addCar(new Car("C003", "Mahindra", "Thar", 150.0));
        }

        rentalSystem.menu();
    }
}

