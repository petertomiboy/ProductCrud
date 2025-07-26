package com.example.productcrud.validation

sealed class ValidationResult {
    object Success : ValidationResult()
    data class Error(val message: String) : ValidationResult()
}

val ValidationResult.errorMessage: String?
    get() = if (this is ValidationResult.Error) this.message else null

val ValidationResult.isValid: Boolean
    get() = this is ValidationResult.Success