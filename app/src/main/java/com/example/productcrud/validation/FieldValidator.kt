package com.example.productcrud.validation

interface FieldValidator<T> {
    fun validate(value: T): ValidationResult
}