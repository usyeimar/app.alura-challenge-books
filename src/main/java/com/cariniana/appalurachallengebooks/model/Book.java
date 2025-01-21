package com.cariniana.appalurachallengebooks.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "books")
@Getter
@Setter

public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "authors_id")
    private Author authors;

    private String language;

    private Integer DownloadCount;

}
