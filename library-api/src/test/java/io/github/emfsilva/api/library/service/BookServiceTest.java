package io.github.emfsilva.api.library.service;

import io.github.emfsilva.api.library.exception.business.BusinessException;
import io.github.emfsilva.api.library.model.entity.Book;
import io.github.emfsilva.api.library.repository.BookRepository;
import io.github.emfsilva.api.library.service.impl.BookServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class BookServiceTest {

    String ISBN_CADASTRADO = "Isbn já cadastrado";
    BookService service;

    @MockBean
    BookRepository repository;

    @BeforeEach
    public void setUp() {
        this.service = new BookServiceImpl(repository);
    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBook() {

        //Cenario
        Book book = createValidBook();

        Mockito.when(repository.save(book))
                .thenReturn(Book.builder()
                        .id(1L)
                        .isbn("123")
                        .author("Fulano")
                        .title("As Aventuras")
                        .build());

        //Execução
        Book savedBook = service.save(book);
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(false);

        // Verificação
        Assertions.assertThat(savedBook.getId()).isNotNull();
        Assertions.assertThat(savedBook.getIsbn()).isEqualTo("123");
        Assertions.assertThat(savedBook.getTitle()).isEqualTo("As Aventuras");
        Assertions.assertThat(savedBook.getAuthor()).isEqualTo("Fulano");


    }

    @Test
    @DisplayName("Deve lançar erro de negocio ao tentar salvar um livro com isbn duplicado")
    public void shouldNotSaveBookWithDuplicatedISBN() {

        //cenario
        Book book = createValidBook();
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);

        // execucao
        Throwable expection = Assertions.catchThrowable(() -> service.save(book));

        // verificação
        Assertions.assertThat(expection).isInstanceOf(BusinessException.class).hasMessage(ISBN_CADASTRADO);
        Mockito.verify(repository, Mockito.never()).save(book);
    }

    @Test
    @DisplayName("Deve obter um livro por ID")
    void getByIdTest() {

        //cenario
        Long id = 1L;
        Book book = createValidBook();
        book.setId(id);
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(book));

        // execução
        Optional<Book> foundBook = service.getById(id);

        // verificação
        Assertions.assertThat(foundBook.isPresent()).isTrue();
        Assertions.assertThat(foundBook.get().getId()).isEqualTo(id);
        Assertions.assertThat(foundBook.get().getAuthor()).isEqualTo(book.getAuthor());
        Assertions.assertThat(foundBook.get().getTitle()).isEqualTo(book.getTitle());
        Assertions.assertThat(foundBook.get().getIsbn()).isEqualTo(book.getIsbn());
    }

    @Test
    @DisplayName("Deve retornar vazio obter um livro por ID quando não existir")
    void bookNotFoundByIdTest() {

        //cenario
        Long id = 1L;
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

        // execução
        Optional<Book> book = service.getById(id);

        // verificação
        Assertions.assertThat(book.isPresent()).isFalse();

    }

    @Test
    @DisplayName("Deve deletar um livro por ID")
    void deleteTest() {

        //cenario
        Long id = 1L;
        Book book = createValidBook();
        book.setId(id);
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(book));

        // execução
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> service.delete(book));

        // verificação
        Mockito.verify(repository, Mockito.times(1)).delete(book);
    }

    @Test
    @DisplayName("Deve ocorrer erro ao tentar deletar um livro inexistente")
    void deleteInvalidBook(){

        // cenario
        Book book = new Book();

        // execucao
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.delete(book));

        // verificação
        Mockito.verify(repository, Mockito.never()).delete(book);
    }

    @Test
    @DisplayName("Deve ocorrer erro ao tentar atualizar um livro inexistente")
    void updateInvalidBookTest(){

        // cenario
        Book book = new Book();

        // execucao
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.update(book));

        // verificação
        Mockito.verify(repository, Mockito.never()).save(book);
    }

    @Test
    @DisplayName("Deve atualizar um livro por ID")
    void updateBookTest() {

        //cenario
        Long id = 1L;

        Book updatingBook = createValidBook();
        updatingBook.setId(id);

        Book updateBook = createValidBook();
        updateBook.setId(id);

        Mockito.when(repository.save(updatingBook)).thenReturn(updateBook);

        // execução
        Book book = service.update(updatingBook);

        // verificação
        Assertions.assertThat(book.getId()).isEqualTo(updateBook.getId());
        Assertions.assertThat(book.getTitle()).isEqualTo(book.getTitle());
        Assertions.assertThat(book.getIsbn()).isEqualTo(updateBook.getIsbn());
        Assertions.assertThat(book.getAuthor()).isEqualTo(updateBook.getAuthor());
    }

    private Book createValidBook() {
        return Book.builder().isbn("123").author("Fulano").title("As Aventuras").build();
    }
}
