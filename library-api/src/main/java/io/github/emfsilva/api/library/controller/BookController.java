package io.github.emfsilva.api.library.controller;

import io.github.emfsilva.api.library.exception.ApiErrors;
import io.github.emfsilva.api.library.exception.business.BusinessException;
import io.github.emfsilva.api.library.model.dto.BookDTO;
import io.github.emfsilva.api.library.model.entity.Book;
import io.github.emfsilva.api.library.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService service;
    private final ModelMapper modelMapper;

    public BookController(BookService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<BookDTO> create(@RequestBody @Valid BookDTO dto) {
        Book entity = modelMapper.map(dto, Book.class);
        entity = service.save(entity);
        BookDTO returnDTO = modelMapper.map(entity, BookDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(returnDTO);
    }


    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getById(@PathVariable Long id){
       Book book = service.getById(id).get();
        BookDTO returnDTO =  modelMapper.map(book, BookDTO.class);
        return ResponseEntity.status(HttpStatus.OK).body(returnDTO);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        return new ApiErrors(bindingResult);
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleBusinessExceptions(BusinessException ex) {
        return new ApiErrors(ex);
    }

}
