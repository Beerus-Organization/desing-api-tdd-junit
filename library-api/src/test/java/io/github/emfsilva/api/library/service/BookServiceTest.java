package io.github.emfsilva.api.library.service;

import io.github.emfsilva.api.library.exception.business.BusinessException;
import io.github.emfsilva.api.library.model.entity.Book;
import io.github.emfsilva.api.library.repository.BookRepository;
import io.github.emfsilva.api.library.service.impl.BookServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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

    private Book createValidBook() {
        return Book.builder().isbn("123").author("Fulano").title("As Aventuras").build();
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
        Assertions.assertThat(expection).isInstanceOf(BusinessException.class)
                .hasMessage(ISBN_CADASTRADO);

        Mockito.verify(repository, Mockito.never()).save(book);

    }
}
