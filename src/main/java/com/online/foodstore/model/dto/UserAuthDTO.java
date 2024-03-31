package com.online.foodstore.model.dto;

import com.online.foodstore.utils.ErrorMessages;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthDTO {

    @NotNull(message = "username" + ErrorMessages.SHOULDNT_BE_NULL)
    @NotEmpty(message = "username" + ErrorMessages.SHOULDNT_BE_EMPTY)
    private String username;

    @NotNull(message = "password" + ErrorMessages.SHOULDNT_BE_NULL)
    @NotEmpty(message = "password" + ErrorMessages.SHOULDNT_BE_EMPTY)
    private String password;
}