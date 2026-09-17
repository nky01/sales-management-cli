package com.nkydev;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {

    private final Scanner scanner = new Scanner(System.in);
    private final List<Customer> customers = new ArrayList<>();
    private final List<Product> products = new ArrayList<>();
    private final List<Order> orders = new ArrayList<>();

    public void showMenu() {
        int option = 0;

        while (option != 8) {
            System.out.println("\n===================================");
            System.out.println("       Sales Management System     ");
            System.out.println("===================================");
            System.out.println("1. Create customer");
            System.out.println("2. Show customers");
            System.out.println("3. Create product");
            System.out.println("4. Show products");
            System.out.println("5. Create order");
            System.out.println("6. Show orders");
            System.out.println("7. Add product to order");
            System.out.println("8. Exit");
            System.out.println("===================================");

            option = leerEntero(scanner, "Select an option: ");

            switch (option) {
                case 1 -> createCustomer();
                case 2 -> showCustomers();
                case 3 -> createProduct();
                case 4 -> showProducts();
                case 5 -> createOrder();
                case 6 -> showOrders();
                case 7 -> addProductToOrder();
                case 8 -> System.out.println("Thank you & goodbye!");
                default -> System.out.println("Incorrect option. Try again...");
            }
        }
    }

    private int leerEntero(Scanner scanner, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Error: Must enter a valid integer. Try again.");
            }
        }
    }

    private float leerFloat(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Float.parseFloat(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Error: Must enter a valid decimal number. Try again.");
            }
        }
    }

    private String leerTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine().trim();
            if (!entrada.isEmpty()) {
                return entrada;
            }
            System.out.println("Error: Field cannot be empty.");
        }
    }

    public void createCustomer() {
        System.out.println("\n========= Create customer =========");
        int id = leerEntero("Id: ");
        String name = leerTexto("Name: ");
        String email = leerTexto("Email: ");
        String phone = leerTexto("Phone number: ");

        customers.add(new Customer(id, name, email, phone));
        System.out.println("Great! Customer was created successfully.");
    }

    public void showCustomers() {
        System.out.println("\n========= Customer List =========");
        if (customers.isEmpty()) {
            System.out.println("No customers found. Select option 1 to create one.");
            return;
        }
        customers.forEach(System.out::println);
    }

    public void createProduct() {
        System.out.println("\n========= Create product =========");
        int id = leerEntero("Id: ");
        String name = leerTexto("Name: ");
        float price = leerFloat("Price: ");
        int stock = leerEntero("Stock: ");

        Category category = null;
        while (category == null) {
            String inputCategory = leerTexto("Category (ELECTRONICS / HOME / SPORTS / CLOTHING): ");
            try {
                category = Category.valueOf(inputCategory.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid category. Please select a valid option.");
            }
        }

        products.add(new Product(id, name, price, stock, category));
        System.out.println("Great! Product was created successfully.");
    }

    public void showProducts() {
        System.out.println("\n========= Product List =========");
        if (products.isEmpty()) {
            System.out.println("No products found. Select option 3 to create one.");
            return;
        }
        products.forEach(System.out::println);
    }

    public void createOrder() {
        System.out.println("\n========= Create order =========");
        int id = leerEntero("Id: ");
        String customerName = leerTexto("Customer name: ");

        Customer customer = customers.stream()
                .filter(c -> c.getName().equalsIgnoreCase(customerName))
                .findFirst()
                .orElse(null);

        if (customer == null) {
            System.out.println("Error: Customer not found. Create the customer first.");
            return;
        }

        LocalDate date = null;
        while (date == null) {
            String dateInput = leerTexto("Creation date (YYYY-MM-DD): ");
            try {
                date = LocalDate.parse(dateInput);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Use YYYY-MM-DD format.");
            }
        }

        Status status = null;
        while (status == null) {
            String statusInput = leerTexto("Status (PENDING / PAID / CANCELLED): ");
            try {
                status = Status.valueOf(statusInput.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid status option. Try again.");
            }
        }

        orders.add(new Order(id, customer, date, status, new ArrayList<>()));
        System.out.println("Great! Order was created successfully.");
    }

    public void showOrders() {
        System.out.println("\n========= Order List =========");
        if (orders.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }
        orders.forEach(System.out::println);
    }

    public void addProductToOrder() {
        System.out.println("\n========= Add product to order =========");
        if (orders.isEmpty() || products.isEmpty()) {
            System.out.println("Error: Both orders and products must exist first.");
            return;
        }

        int orderId = leerEntero("Order ID: ");
        int productId = leerEntero("Product ID: ");

        Order order = orders.stream()
                .filter(o -> o.getId() == orderId)
                .findFirst()
                .orElse(null);

        Product product = products.stream()
                .filter(p -> p.getId() == productId)
                .findFirst()
                .orElse(null);

        if (order == null) {
            System.out.println("Error: Order ID not found.");
        } else if (product == null) {
            System.out.println("Error: Product ID not found.");
        } else {
            order.getProducts().add(product);
            System.out.println("Product successfully added to the order.");
        }
    }
}
