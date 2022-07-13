package io.github.emfsilva.api.library.exception.business;

import io.github.emfsilva.api.library.model.entity.Book;

public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
