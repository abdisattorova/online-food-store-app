package com.online.foodstore.model.dto;

import com.online.foodstore.model.entity.ECategoryStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.online.foodstore.utils.ErrorMessages.SHOULDNT_BE_EMPTY;
import static com.online.foodstore.utils.ErrorMessages.SHOULDNT_BE_NULL;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO extends BaseDTO {
    @NotNull(message = "name" + SHOULDNT_BE_NULL)
    @NotEmpty(message = "name" + SHOULDNT_BE_EMPTY)
    private String name;

    @NotNull(message = "name" + SHOULDNT_BE_NULL)
    private ECategoryStatus status;

    private int numberOfProducts;
}