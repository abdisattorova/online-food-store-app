package com.online.foodstore.config;

import com.online.foodstore.model.entity.*;
import com.online.foodstore.repository.CategoryRepository;
import com.online.foodstore.repository.ProductRepository;
import com.online.foodstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    public void run(String... args) {

        if (!isDefaultDataAlreadySaved()) {
            /* SAVING USERS */
            var manager = saveUsers();

            /* SAVING CATEGORIES & PRODUCTS*/
            saveCategoriesAndProducts(manager);
        }
    }

    private User saveUsers() {
        var manager = new User("Adminjon", EUserStatus.ACTIVE, ERole.MANAGER, "admin", "admin123");
        manager.setCreatedBy(0L).setModifiedBy(0L);
        manager = userRepository.save(manager);

        var sevinch = new User("Sevinch", EUserStatus.ACTIVE, ERole.EMPLOYEE, "sevinch", "sevinch123");
        sevinch.setCreatedBy(manager.getId()).setModifiedBy(manager.getId());
        userRepository.save(sevinch);
        return manager;
    }

    private void saveCategoriesAndProducts(User manager) {
        List<String> categoryNames = Arrays.asList("Meat", "Vegetables", "Fruits", "Dairy", "Beverages");

        // Define product names for each category
        List<List<String>> productNames = Arrays.asList(
                Arrays.asList("Beef", "Chicken"),
                Arrays.asList("Carrot", "Broccoli"),
                Arrays.asList("Apple", "Orange"),
                Arrays.asList("Milk", "Cheese"),
                Arrays.asList("Coffee", "Tea")
        );

        // Create categories and associated products
        for (int i = 0; i < categoryNames.size(); i++) {
            var category = new Category(categoryNames.get(i), ECategoryStatus.ACTIVE);
            category.setCreatedBy(manager.getId()).setModifiedBy(manager.getId());
            categoryRepository.save(category);

            var currentProductNames = productNames.get(i);
            for (var productName : currentProductNames) {
                var product = new Product(productName, category, category.getId(), EProductStatus.AVAILABLE, 10, LocalDate.of(2024, 12, 1));
                product.setCreatedBy(manager.getId()).setModifiedBy(manager.getId());
                productRepository.save(product);
            }
        }
    }

    private boolean isDefaultDataAlreadySaved() {
        // checks if default data already saved to db or not
        return userRepository.existsByUsername("admin");
    }
}
