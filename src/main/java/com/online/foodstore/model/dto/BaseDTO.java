package com.online.foodstore.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
public abstract class BaseDTO implements Serializable {
    protected Long id;

    protected Timestamp createdAt;

    protected Timestamp modifiedAt;

    protected Long createdBy;

    protected Long modifiedBy;
}
