package com.example.productcrud.validation

class PositiveNumberValidator(private val fieldName: String) : FieldValidator<String> {
    override fun validate(value: String): ValidationResult {
        val number = value.toDoubleOrNull()
        return if (number == null || number <= 0) {
            ValidationResult.Error("$fieldName csak pozitív szám lehet")
        } else ValidationResult.Success
    }
}