package io.github.emfsilva.api.library.exception;

import io.github.emfsilva.api.library.exception.business.BusinessException;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.validation.BindingResult;

import javax.naming.Binding;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApiErrors {

    private final List<String> errors;

    public ApiErrors(BindingResult bindingResult) {
        this.errors = new ArrayList<>();
        bindingResult.getAllErrors().forEach(errors -> this.errors.add(errors.getDefaultMessage()));
    }

    public ApiErrors(BusinessException ex) {
        this.errors = List.of(ex.getMessage());
    }

    public List<String> getErrors() {
        return errors;
    }
}
