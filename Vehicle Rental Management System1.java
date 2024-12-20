// Online Java Compiler
// Use this editor to write, compile and run your Java code on}
import java.util.ArrayList;
import java.util.List;

// Abstract Base Class (Abstraction Principle)
abstract class Vehicle {
    private String vehicleId;
    private String model;
    private double baseRentalRate;
    private boolean isAvailable;

    public Vehicle(String vehicleId, String model, double baseRentalRate) {
        if (baseRentalRate <= 0) {
            throw new IllegalArgumentException("Base rental rate must be positive.");
        }
        this.vehicleId = vehicleId;
        this.model = model;
        this.baseRentalRate = baseRentalRate;
        this.isAvailable = true;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getModel() {
        return model;
    }

    public double getBaseRentalRate() {
        return baseRentalRate;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public abstract double calculateRentalCost(int days);
}

// Car Class (Inheritance & Polymorphism)
class Car extends Vehicle {
    private boolean hasGPS;

    public Car(String vehicleId, String model, double baseRentalRate, boolean hasGPS) {
        super(vehicleId, model, baseRentalRate);
        this.hasGPS = hasGPS;
    }

    @Override
    public double calculateRentalCost(int days) {
        double cost = getBaseRentalRate() * days;
        if (hasGPS) {
            cost += 10 * days; // Additional charge for GPS
        }
        return cost;
    }
}

// Motorcycle Class
class Motorcycle extends Vehicle {
    private boolean hasHelmet;

    public Motorcycle(String vehicleId, String model, double baseRentalRate, boolean hasHelmet) {
        super(vehicleId, model, baseRentalRate);
        this.hasHelmet = hasHelmet;
    }

    @Override
    public double calculateRentalCost(int days) {
        return getBaseRentalRate() * days;
    }
}

// Truck Class
class Truck extends Vehicle {
    private double loadCapacity;

    public Truck(String vehicleId, String model, double baseRentalRate, double loadCapacity) {
        super(vehicleId, model, baseRentalRate);
        if (loadCapacity <= 0) {
            throw new IllegalArgumentException("Load capacity must be positive.");
        }
        this.loadCapacity = loadCapacity;
    }

    @Override
    public double calculateRentalCost(int days) {
        return getBaseRentalRate() * days + 45 * loadCapacity; // Additional charge for load capacity
    }
}

// Customer Class
class Customer {
    private String name;
    private String email;
    private int rentalHistory;

    public Customer(String name, String email) {
        this.name = name;
        this.email = email;
        this.rentalHistory = 0; // Initialize rental history to zero
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getRentalHistory() {
        return rentalHistory;
    }

    public void incrementRentalHistory() {
        rentalHistory++;
    }
}

// RentalTransaction Class
class RentalTransaction {
    private Customer customer;
    private Vehicle vehicle;
    private int days;
    private double cost;

    public RentalTransaction(Customer customer, Vehicle vehicle, int days, double cost) {
        this.customer = customer;
        this.vehicle = vehicle;
        this.days = days;
        this.cost = cost;
    }

    @Override
    public String toString() {
        return String.format("Transaction: %s rented %s for %d days. Total Cost: %.2f",
                customer.getName(), vehicle.getModel(), days, cost);
    }
}

// RentalAgency Class
class RentalAgency {
    private List<Vehicle> fleet;
    private List<RentalTransaction> transactions;

    public RentalAgency() {
        this.fleet = new ArrayList<>();
        this.transactions = new ArrayList<>();
    }

    public void addVehicle(Vehicle vehicle) {
        fleet.add(vehicle);
    }

    public void rentVehicle(String vehicleId, Customer customer, int days) {
        for (Vehicle vehicle : fleet) {
            if (vehicle.getVehicleId().equals(vehicleId) && vehicle.isAvailable()) {
                vehicle.setAvailable(false);
                double cost = vehicle.calculateRentalCost(days);
                RentalTransaction transaction = new RentalTransaction(customer, vehicle, days, cost);
                transactions.add(transaction);
                customer.incrementRentalHistory();
                System.out.println(transaction); // Print transaction details
                return;
            }
        }
        throw new IllegalArgumentException("Vehicle not available for rental.");
    }

    public void returnVehicle(String vehicleId) {
        for (Vehicle vehicle : fleet) {
            if (vehicle.getVehicleId().equals(vehicleId)) {
                vehicle.setAvailable(true);
                System.out.println(vehicle.getModel() + " has been returned and is now available.");
                return;
            }
        }
        throw new IllegalArgumentException("Vehicle not found.");
    }
}

// Main Class for Testing
public class Main {
    public static void main(String[] args) {
        RentalAgency agency = new RentalAgency();

        // Add vehicles to the fleet
        agency.addVehicle(new Car("CAR111", "Toyota Corolla X", 45, true));
        agency.addVehicle(new Motorcycle("MOT8910", "Yamaha MT-18", 25, true));
        agency.addVehicle(new Truck("TRUCK566", "Ford F-160", 100, 2.5));

        // Create customer Cindy
        Customer cindy = new Customer("Cindy Etornam", "cindy123@gmail.com");

        try {
            // Process rentals
            agency.rentVehicle("CAR111", cindy, 5);
            agency.returnVehicle("CAR111");

            System.out.println("Rental process completed.");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}