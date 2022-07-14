package io.github.emfsilva.api.library.service.impl;

import io.github.emfsilva.api.library.exception.business.BusinessException;
import io.github.emfsilva.api.library.model.entity.Book;
import io.github.emfsilva.api.library.repository.BookRepository;
import io.github.emfsilva.api.library.service.BookService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    String ISBN_CADASTRADO = "Isbn já cadastrado";
    private final BookRepository repository;

    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public Book save(Book book) {
        existByIsbn(book);
        return repository.save(book);
    }

    @Override
    public Optional<Book> getById(Long id) {
        return this.repository.findById(id);
    }

    @Override
    public void delete(Book book) {
        Book bookReturn = findById(book);
        repository.delete(bookReturn);
    }

    @Override
    public Book update(Book book) {
       Book bookReturn = findById(book);
       return  repository.save(bookReturn);
    }


    private void existByIsbn(Book book) {
        if(repository.existsByIsbn(book.getIsbn())) {
          throw new BusinessException(ISBN_CADASTRADO );
       }
    }

    private Book findById(Book book) {
        if(book.getId() == null) {
            throw new IllegalArgumentException("Book id cant be null");
        }

        return book;
    }
}
