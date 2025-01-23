package com.cariniana.appalurachallengebooks.repository;

import com.cariniana.appalurachallengebooks.dto.BookDTO;
import com.cariniana.appalurachallengebooks.model.Author;
import com.cariniana.appalurachallengebooks.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    Book findByTitle(String title);

    Book findByAuthors(List<Author> authors);

    List<Book> findByTitleContainingIgnoreCase(String bookName);

    List<Book> findByLanguageIgnoreCase(String language);


    boolean existsBooksByTitle(String title);

    @Query("SELECT DISTINCT b.language FROM Book b")
    List<String> getDistinctByLanguageList();
}
