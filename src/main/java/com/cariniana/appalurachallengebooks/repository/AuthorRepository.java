package com.cariniana.appalurachallengebooks.repository;


import com.cariniana.appalurachallengebooks.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Author findByName(String name);

    @Query("SELECT a FROM Author a WHERE a.birthYear = :year AND a.deathYear = :year")
    Author findAuthorLiveByYear(int year);

    List<Author> findByNameContainingIgnoreCase(String name);

    List<Author> findByBirthYearBeforeAndDeathYearAfter(int year, int year1);
}
