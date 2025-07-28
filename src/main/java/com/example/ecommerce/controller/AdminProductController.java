package com.example.ecommerce.controller;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;
    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    /**
     * Admin dashboard: list all products
     */
    @GetMapping
    public String adminDashboard(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "admin_products"; // templates/admin_products.html
    }

    /**
     * Show form to add new product
     */
    @GetMapping("/add")
    public String showAddProductForm() {
        return "add_product";
    }

    /**
     * Handle add product POST
     */
    @PostMapping("/add")
    public String handleAddProduct(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam Double price,
            @RequestParam("image") MultipartFile imageFile
    ) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(BigDecimal.valueOf(price));

        // Save image
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                Path filePath = Paths.get(UPLOAD_DIR + fileName);
                Files.createDirectories(filePath.getParent());
                imageFile.transferTo(filePath.toFile());
                product.setImagePath("/uploads/" + fileName);
            } catch (IOException e) {
                throw new RuntimeException("Failed to save image", e);
            }
        }

        productService.saveProduct(product);
        return "redirect:/admin";
    }

    /**
     * Show edit form for product
     */
    @GetMapping("/edit/{id}")
    public String showEditProductForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "edit_product";
    }

    /**
     * Handle update product POST
     */
    @PostMapping("/edit/{id}")
    public String updateProduct(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam Double price,
            @RequestParam(value = "image", required = false) MultipartFile imageFile
    ) {
        Product product = productService.getProductById(id);
        product.setName(name);
        product.setDescription(description);
        product.setPrice(BigDecimal.valueOf(price));

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                Path filePath = Paths.get(UPLOAD_DIR + fileName);
                Files.createDirectories(filePath.getParent());
                imageFile.transferTo(filePath.toFile());
                product.setImagePath("/uploads/" + fileName);
            } catch (IOException e) {
                throw new RuntimeException("Failed to save image", e);
            }
        }

        productService.saveProduct(product);
        return "redirect:/admin";
    }

    /**
     * Delete product by ID
     */
    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id); // use correct method name
        return "redirect:/admin";
    }
}
