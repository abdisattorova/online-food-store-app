package com.online.foodstore.model.dto;

import com.online.foodstore.model.entity.EProductStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

import static com.online.foodstore.utils.ErrorMessages.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO extends BaseDTO {
    @NotNull(message = "name" + SHOULDNT_BE_NULL)
    @NotEmpty(message = "name" + SHOULDNT_BE_EMPTY)
    private String name;

    private String categoryName;

    @NotNull(message = "name" + SHOULDNT_BE_NULL)
    private Long categoryId;

    @NotNull(message = "name" + SHOULDNT_BE_NULL)
    private EProductStatus status;

    @NotNull(message = "name" + SHOULDNT_BE_NULL)
    @Min(value = 0,message = "quantity" + SHOULD_NOT_BE_NEGATIVE)
    private Integer quantity;

    private LocalDate expireDate;
}
