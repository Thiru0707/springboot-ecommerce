package com.example.ecommerce.dto;

import lombok.*;

import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RazorpayOrderRequest {

    @NotNull(message = "Amount is required")
    @Min(value = 1, message = "Amount must be greater than zero")
    private Integer amount;

    @NotBlank(message = "Currency is required")
    private String currency;

    @NotBlank(message = "Receipt is required")
    private String receipt;
}
