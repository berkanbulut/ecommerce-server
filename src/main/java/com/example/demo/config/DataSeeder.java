package com.example.demo.config;

import com.example.demo.brand.entity.Brand;
import com.example.demo.brand.repository.BrandRepository;
import com.example.demo.category.entity.Category;
import com.example.demo.category.repository.CategoryRepository;
import com.example.demo.permission.entity.Permission;
import com.example.demo.permission.repository.PermissionRepository;
import com.example.demo.product.entity.Product;
import com.example.demo.product.repository.ProductRepository;
import com.example.demo.role.entity.Role;
import com.example.demo.role.repository.RoleRepository;
import com.example.demo.user.entity.User;
import com.example.demo.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Configuration
public class DataSeeder {

    private final Random random = new Random();

    @Bean
    CommandLineRunner seed(
            CategoryRepository categoryRepository,
            BrandRepository brandRepository,
            ProductRepository productRepository,
            UserRepository userRepository,
            RoleRepository roleRepository,
            PermissionRepository permissionRepository,
            PasswordEncoder passwordEncoder
    ) {

        return args -> {

            /*
             =========================================================
             PERMISSIONS
             =========================================================
            */

            List<String> permissionNames = List.of(

                    // CATEGORY
                    "category:create",
                    "category:update",
                    "category:delete",

                    // BRAND
                    "brand:create",
                    "brand:update",
                    "brand:delete",

                    // PRODUCT
                    "product:create",
                    "product:update",
                    "product:delete",

                    // USER
                    "user:read",
                    "user:create",
                    "user:update",
                    "user:delete",

                    // ROLE
                    "role:manage",

                    // PERMISSION
                    "permission:manage",

                    // IMAGE
                    "image:upload",

                    //Order
                    "order:create",
                    "order:read",
                    "order:update"

            );

            permissionNames.forEach(permissionName -> {
                permissionRepository.findByName(permissionName)
                        .orElseGet(() ->
                                permissionRepository.save(
                                        new Permission(
                                                permissionName,
                                                permissionName + " permission"
                                        )
                                )
                        );
            });

            List<Permission> allPermissions = permissionRepository.findAll();

            /*
             =========================================================
             ROLES
             =========================================================
            */

            Role roleAdmin = roleRepository.findByName("ROLE_ADMIN")
                    .orElseGet(() ->
                            roleRepository.save(
                                    new Role(
                                            "ROLE_ADMIN",
                                            "Administrator"
                                    )
                            )
                    );

            Role roleUser = roleRepository.findByName("ROLE_USER")
                    .orElseGet(() ->
                            roleRepository.save(
                                    new Role(
                                            "ROLE_USER",
                                            "Standard user"
                                    )
                            )
                    );

            /*
             =========================================================
             ROLE PERMISSIONS
             =========================================================
            */

            allPermissions.forEach(roleAdmin::addPermission);

            roleRepository.save(roleAdmin);
            roleRepository.save(roleUser);

            /*
             =========================================================
             USERS
             =========================================================
            */

            createUserIfNotExists(
                    userRepository,
                    passwordEncoder,
                    roleAdmin,
                    "admin",
                    "admin@gmail.com"
            );

            createUserIfNotExists(
                    userRepository,
                    passwordEncoder,
                    roleUser,
                    "user",
                    "user@gmail.com"
            );

            /*
             =========================================================
             CATEGORIES
             =========================================================
            */

            List<String> categoryNames = List.of(
                    "Electronics",
                    "Computers",
                    "Smart Phones",
                    "Gaming",
                    "Home & Kitchen",
                    "Sports",
                    "Fashion",
                    "Books",
                    "Health",
                    "Automotive"
            );

            List<Category> categories = new ArrayList<>();

            categoryNames.forEach(name -> {

                Category category = categoryRepository.findByName(name)
                        .orElseGet(() ->
                                categoryRepository.save(
                                        new Category(
                                                name,
                                                name + " category"
                                        )
                                )
                        );

                categories.add(category);
            });

            /*
             =========================================================
             BRANDS
             =========================================================
            */

            List<String> brandNames = List.of(
                    "Apple",
                    "Samsung",
                    "Nike",
                    "Adidas",
                    "Sony",
                    "Logitech",
                    "Asus",
                    "Dell",
                    "Puma",
                    "Xiaomi"
            );

            List<Brand> brands = new ArrayList<>();

            brandNames.forEach(name -> {

                Brand brand = brandRepository.findByName(name)
                        .orElseGet(() ->
                                brandRepository.save(
                                        new Brand(
                                                name,
                                                name + " brand"
                                        )
                                )
                        );

                brands.add(brand);
            });

            /*
             =========================================================
             PRODUCTS
             =========================================================
            */

            if (productRepository.count() == 0) {

                List<String> productNames = List.of(
                        "iPhone 15 Pro",
                        "Galaxy S24 Ultra",
                        "Macbook Pro M3",
                        "Gaming Mouse",
                        "Mechanical Keyboard",
                        "Running Shoes",
                        "Basketball Shoes",
                        "Gaming Laptop",
                        "Wireless Headphones",
                        "Smart Watch",
                        "Coffee Machine",
                        "4K Monitor",
                        "Bluetooth Speaker",
                        "Fitness Tracker",
                        "Office Chair",
                        "Air Fryer",
                        "Protein Powder",
                        "Electric Scooter",
                        "Tablet Pro",
                        "VR Headset",
                        "Gaming Chair",
                        "SSD 2TB",
                        "Portable Charger",
                        "Smart TV",
                        "DSLR Camera",
                        "Wireless Earbuds",
                        "Graphic Tablet",
                        "Gaming Controller",
                        "Robot Vacuum",
                        "Smart Lamp"
                );

                for (int i = 0; i < 30; i++) {

                    String productName = productNames.get(i);

                    Category category =
                            categories.get(random.nextInt(categories.size()));

                    Brand brand =
                            brands.get(random.nextInt(brands.size()));

                    String slug = generateSlug(productName) + "-" + (i + 1);

                    String sku = "SKU-" + (1000 + i);

                    int stock = random.nextInt(200);

                    BigDecimal price =
                            BigDecimal.valueOf(50 + random.nextInt(5000));

                    BigDecimal salePrice =
                            BigDecimal.valueOf(price.doubleValue() - random.nextInt(30));

                    String image =
                            "https://picsum.photos/seed/product"
                                    + (i + 1)
                                    + "/600/600";

                    Product product = new Product(
                            productName,
                            slug,
                            sku,
                            productName + " short description",
                            productName + " full description for ecommerce demo project.",
                            price,
                            salePrice,
                            "USD",
                            stock,
                            true,
                            random.nextBoolean(),
                            image,
                            category,
                            brand
                    );

                    product.addImage(
                            "https://picsum.photos/seed/product-gallery-"
                                    + (i + 1)
                                    + "-1/800/800",
                            1
                    );

                    product.addImage(
                            "https://picsum.photos/seed/product-gallery-"
                                    + (i + 1)
                                    + "-2/800/800",
                            2
                    );

                    product.addImage(
                            "https://picsum.photos/seed/product-gallery-"
                                    + (i + 1)
                                    + "-3/800/800",
                            3
                    );

                    productRepository.save(product);
                }
            }

            System.out.println("=================================================");
            System.out.println("DATABASE SEEDED SUCCESSFULLY");
            System.out.println("=================================================");
        };
    }

    /*
     =========================================================
     HELPERS
     =========================================================
    */

    private void createUserIfNotExists(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            Role role,
            String username,
            String email
    ) {

        if (!userRepository.existsByUsername(username)) {

            User user = new User(
                    username,
                    email,
                    passwordEncoder.encode("12345678")
            );

            user.addRole(role);

            userRepository.save(user);
        }
    }

    private String generateSlug(String value) {

        return value
                .toLowerCase()
                .replace(" ", "-")
                .replace("&", "")
                .replace("--", "-");
    }
}