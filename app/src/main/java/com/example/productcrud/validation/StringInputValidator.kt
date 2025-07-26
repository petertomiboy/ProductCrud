package com.example.productcrud.validation

class StringInputValidator(
    private val fieldName: String,
    private val minLength: Int = 1
) : FieldValidator<String> {

    override fun validate(value: String): ValidationResult {
        return when {
            value.isBlank() -> ValidationResult.Error("$fieldName nem lehet üres")
            value.length < minLength -> ValidationResult.Error("$fieldName legalább $minLength karakter kell legyen")
            else -> ValidationResult.Success
        }
    }
}