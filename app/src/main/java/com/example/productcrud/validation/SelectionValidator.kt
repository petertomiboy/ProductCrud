package com.example.productcrud.validation

class SelectionValidator(private val fieldName: String) : FieldValidator<Any?> {
    override fun validate(value: Any?): ValidationResult {
        return if (value == null) {
            ValidationResult.Error("$fieldName kötelező választani")
        } else {
            ValidationResult.Success
        }
    }
}