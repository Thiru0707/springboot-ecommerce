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

    // ✅ Admin Dashboard
    @GetMapping
    public String adminDashboard() {
        return "admin"; // templates/admin.html
    }

    // ✅ Show Add Product Form
    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "addProduct"; // templates/addProduct.html
    }

    // ✅ Handle Add Product Submission with Image
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

        // Handle image upload
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                Path filePath = Paths.get(UPLOAD_DIR + fileName);
                Files.createDirectories(filePath.getParent());
                imageFile.transferTo(filePath.toFile());

                product.setImagePath("/uploads/" + fileName);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to save image");
            }
        }

        productService.saveProduct(product);
        return "redirect:/products";
    }

    // ✅ Show Edit Form
    @GetMapping("/edit/{id}")
    public String showEditProductForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "edit_product"; // templates/edit_product.html
    }

    // ✅ Handle Edit Submission
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
                e.printStackTrace();
                throw new RuntimeException("Failed to save image");
            }
        }

        productService.saveProduct(product);
        return "redirect:/products";
    }
}

