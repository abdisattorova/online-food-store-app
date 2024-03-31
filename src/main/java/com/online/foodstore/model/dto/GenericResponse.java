package com.online.foodstore.model.dto;

import com.online.foodstore.utils.ErrorMessages;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

import static com.online.foodstore.utils.ErrorCodes.ERROR;
import static com.online.foodstore.utils.ErrorCodes.SUCCESS;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class GenericResponse<T> implements Serializable {

    private String resultCode;

    private String resultMsg;

    @Valid
    @NotNull(message = "data " + ErrorMessages.SHOULDNT_BE_NULL)
    private T data;

    private PaginationData pagination;

    public static <T> GenericResponse<T> ok() {
        return GenericResponse.<T>builder()
                .resultCode(SUCCESS)
                .resultMsg("OK")
                .build();
    }

    public static <T> GenericResponse<T> ok(T data) {
        return GenericResponse.<T>builder()
                .resultCode(SUCCESS)
                .resultMsg("OK")
                .data(data)
                .build();
    }

    public static <T> GenericResponse<T> ok(T data, PaginationData pagination) {
        return GenericResponse.<T>builder()
                .resultCode(SUCCESS)
                .resultMsg("OK")
                .data(data)
                .pagination(pagination)
                .build();
    }

    public static <T> GenericResponse<T> error() {
        return GenericResponse.<T>builder()
                .resultCode(ERROR)
                .resultMsg("ERROR")
                .build();
    }

    public static <T> GenericResponse<T> error(String msg) {
        return GenericResponse.<T>builder()
                .resultCode(ERROR)
                .resultMsg(msg)
                .build();
    }

    public static <T> GenericResponse<T> error(String code, String msg) {
        return GenericResponse.<T>builder()
                .resultCode(code)
                .resultMsg(msg)
                .build();
    }
}