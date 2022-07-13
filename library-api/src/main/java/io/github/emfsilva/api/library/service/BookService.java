package io.github.emfsilva.api.library.service;

import io.github.emfsilva.api.library.model.entity.Book;

import java.util.Optional;

public interface BookService {
    Book save(Book book);

    Optional<Book> getById(Long id);

}
