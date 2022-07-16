package io.github.emfsilva.api.library.repository;

import io.github.emfsilva.api.library.model.entity.Book;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
class BookRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    BookRepository repository;

    @Test
    @DisplayName("Deve retornar verdadeiro quando existir um liveo na base com o isbn informado")
    void shouldReturnTrueWhenIsbnExist() {

        // cenario

        Book book = createNewBook();

        entityManager.persist(book);

        // execucao
        boolean exists = repository.existsByIsbn(book.getIsbn());

        //verificacao
        Assertions.assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Deve retornar falso quando não existir um liveo na base com o isbn informado")
    void shouldReturnFalseWhenIsbnDoesntExist() {

        // cenario
        Book book = createNewBook();

        // execucao
        boolean exists = repository.existsByIsbn(book.getIsbn());

        //verificacao
        Assertions.assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Deve obter um livro por ID")
    void findByIdTest() {
        // cenario
       Book book =  createNewBook();
       entityManager.persist(book);

       // execução
        Optional<Book> foundBook = repository.findById(book.getId());

        //verificação
        Assertions.assertThat(foundBook.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Deve salvar um livro")
    void saveBookTest() {
        Book book = createNewBook();

        Book savedBook = repository.save(book);

        Assertions.assertThat(savedBook.getId()).isNotNull();
    }

    @Test
    @DisplayName("Deve deletar um livro")
    void deleteBookTest() {
        Book book = createNewBook();
        entityManager.persist(book);

        Book foundBook = entityManager.find(Book.class, book.getId());
        repository.delete(foundBook);

        Book deleteBook = entityManager.find(Book.class, book.getId());
        Assertions.assertThat(deleteBook).isNull();


    }

    private Book createNewBook() {
        return Book.builder().author("Emerson").title("As Aventuras").isbn("123").build();
    }
}