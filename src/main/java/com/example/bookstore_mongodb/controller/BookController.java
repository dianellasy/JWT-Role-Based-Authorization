package com.example.bookstore_mongodb.controller;

import com.example.bookstore_mongodb.entity.Book;
import com.example.bookstore_mongodb.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @PostMapping
    public Book createBook(@RequestBody Book book) {
        return bookService.createBook(book);
    }

    // DELETE /api/books/{id}: DELETE endpoint that deletes a book by its ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable String id) {
        // Call the service layer to delete the book (true: book exists and was deleted; false: book does not exist)
        boolean book_is_deleted = bookService.deleteBook(id);

        if (book_is_deleted) {
            // Book exists and was deleted successfully
            return ResponseEntity.ok("Book ID exists and was deleted successfully.");
        } else {
            // Book does not exist
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book ID does not exist.");
        }
    }
}
