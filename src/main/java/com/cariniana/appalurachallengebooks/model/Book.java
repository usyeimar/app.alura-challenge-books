package com.cariniana.appalurachallengebooks.model;

import com.cariniana.appalurachallengebooks.dto.BookDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String language;

    private Integer DownloadCount;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Author> authors = new ArrayList<>();

    public Book(BookDTO bookDTO) {
        this.title = bookDTO.title();
        this.language = bookDTO.languages().get(0);
        this.DownloadCount = bookDTO.downloadCount();
    }

    public void addAuthor(Author author) {
        authors.add(author);
        author.setBook(this);
    }

    public void removeAuthor(Author author) {
        authors.remove(author);
        author.setBook(null);
    }

}
