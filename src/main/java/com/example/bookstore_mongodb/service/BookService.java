package com.example.bookstore_mongodb.service;

import com.example.bookstore_mongodb.entity.Book;
import com.example.bookstore_mongodb.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    // create a book
    public Book createBook(Book book) {
        book.setCreatedAt(new Date());
        return bookRepository.save(book);
    }

    // get all books
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Delete a book by its ID
    public boolean deleteBook(String id) {
        // Check if the book exists
        Optional<Book> book = bookRepository.findById(id);

        // If the book exists, delete it and return true so the controller can send a 200 OK response
        if (book.isPresent()) {
            bookRepository.deleteById(id);
            return true;
        }

        // Book does not exist, so return false so the controller can send a 404 Not Found response
        return false;
    }
}
