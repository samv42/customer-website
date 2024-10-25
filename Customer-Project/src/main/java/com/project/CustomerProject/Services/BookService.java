package com.project.CustomerProject.Services;

import com.project.CustomerProject.Book;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookService {
    List<Book> getAllBooks();

    Book saveBook(Book book);

    Book getBook(Long id);

    void deleteBook(Long id);

    List<Book> saveAllBook(List<Book> bookList);
}
