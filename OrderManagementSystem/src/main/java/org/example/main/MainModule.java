package org.example.main;

import org.example.dao.OrderProcessor;
import org.example.entity.*;
import java.util.*;

public class MainModule {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        OrderProcessor op = new OrderProcessor(); // Uses DBConnUtil internally

        while (true) {
            System.out.println("\n===== ORDER MANAGEMENT SYSTEM =====");
            System.out.println("1. Create User");
            System.out.println("2. Create Product");
            System.out.println("3. Create Order");
            System.out.println("4. Cancel Order");
            System.out.println("5. Get All Products");
            System.out.println("6. Get Orders by User");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            int ch = sc.nextInt();
            sc.nextLine(); // clear buffer

            switch (ch) {
                case 1 -> {
                    System.out.print("Enter User ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter Username: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Password: ");
                    String pass = sc.nextLine();
                    System.out.print("Enter Role (Admin/User): ");
                    String role = sc.nextLine();

                    User user = new User(id, name, pass, role);
                    op.createUser(user);
                    System.out.println("User created successfully.");
                }

                case 2 -> {
                    System.out.print("Enter Admin User ID: ");
                    int aid = sc.nextInt();
                    sc.nextLine();

                    User admin = new User(aid, "admin", "admin", "Admin");

                    System.out.print("Enter Product ID: ");
                    int pid = sc.nextInt(); sc.nextLine();
                    System.out.print("Enter Product Name: ");
                    String pname = sc.nextLine();
                    System.out.print("Enter Description: ");
                    String desc = sc.nextLine();
                    System.out.print("Enter Price: ");
                    double price = sc.nextDouble();
                    System.out.print("Enter Quantity in Stock: ");
                    int qty = sc.nextInt(); sc.nextLine();
                    System.out.print("Enter Type (Electronics/Clothing): ");
                    String type = sc.nextLine();

                    Product product;

                    if (type.equalsIgnoreCase("Electronics")) {
                        System.out.print("Enter Brand: ");
                        String brand = sc.nextLine();
                        System.out.print("Enter Warranty Period (months): ");
                        int warranty = sc.nextInt(); sc.nextLine();
                        product = new Electronics(pid, pname, desc, price, qty, type, brand, warranty);
                    } else if (type.equalsIgnoreCase("Clothing")) {
                        System.out.print("Enter Size: ");
                        String size = sc.nextLine();
                        System.out.print("Enter Color: ");
                        String color = sc.nextLine();
                        product = new Clothing(pid, pname, desc, price, qty, type, size, color);
                    } else {
                        System.out.println("Invalid type. Must be 'Electronics' or 'Clothing'.");
                        break;
                    }

                    op.createProduct(admin, product);
                    System.out.println("Product created successfully.");
                }

                case 3 -> {
                    System.out.print("Enter User ID: ");
                    int uid = sc.nextInt(); sc.nextLine();
                    User user = new User(uid, "user", "pass", "User");

                    List<Product> cart = new ArrayList<>();
                    System.out.print("Enter number of products to order: ");
                    int count = sc.nextInt();

                    for (int i = 0; i < count; i++) {
                        System.out.print("Enter Product ID: ");
                        int pid = sc.nextInt();
                        cart.add(new Product(pid, "", "", 0.0, 0, ""));
                    }

                    op.createOrder(user, cart);
                    System.out.println("Order placed successfully.");
                }

                case 4 -> {
                    System.out.print("Enter User ID: ");
                    int uid = sc.nextInt();
                    System.out.print("Enter Order ID to cancel: ");
                    int oid = sc.nextInt();

                    op.cancelOrder(uid, oid);
                    System.out.println("Order cancelled successfully.");
                }

                case 5 -> {
                    List<Product> products = op.getAllProducts();
                    System.out.println("\nAvailable Products:");
                    if (products.isEmpty()) {
                        System.out.println("No products found.");
                    } else {
                        for (Product p : products) {
                            System.out.printf("ID: %d | Name: %s | ₹%.2f | Type: %s | Stock: %d\n",
                                    p.getProductId(), p.getProductName(), p.getPrice(), p.getType(), p.getQuantityInStock());
                        }
                    }
                }

                case 6 -> {
                    System.out.print("Enter User ID: ");
                    int uid = sc.nextInt();
                    User user = new User(uid, "", "", "User");

                    List<Product> orders = op.getOrderByUser(user);
                    System.out.println("\n🛒 Orders by User:");
                    if (orders.isEmpty()) {
                        System.out.println("No orders found.");
                    } else {
                        for (Product p : orders) {
                            System.out.printf("Product ID: %d | Name: %s | Type: %s\n",
                                    p.getProductId(), p.getProductName(), p.getType());
                        }
                    }
                }

                case 7 -> {
                    System.out.println("Exiting... Bye!");
                    System.exit(0);
                }

                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
