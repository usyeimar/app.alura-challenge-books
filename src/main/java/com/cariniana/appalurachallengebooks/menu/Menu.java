package com.cariniana.appalurachallengebooks.menu;

import com.cariniana.appalurachallengebooks.dto.BookDTO;
import com.cariniana.appalurachallengebooks.dto.BookResponse;
import com.cariniana.appalurachallengebooks.model.Author;
import com.cariniana.appalurachallengebooks.model.Book;
import com.cariniana.appalurachallengebooks.repository.AuthorRepository;
import com.cariniana.appalurachallengebooks.repository.BookRepository;
import com.cariniana.appalurachallengebooks.services.API;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

@Component
public class Menu {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;


    public void showMenu() throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String menu = """
                    MENU PRINCIPAL
                    
                    1. Buscar libros por título
                    2. Buscar libros por autor
                    3. Listar autores registrados
                    4. Listar autores vivos en un determinado año
                    5. Listar libros por idioma
                    
                    0. Salir
                    
                    Elija una de las opciones:
                    """;
            System.out.println(menu);

            int option = -1;

            try {
                option = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                showInvalidOptionMessage();
                continue;
            }

            switch (option) {
                case 1 -> showSearchByTitleMenu(scanner);
                case 2 -> showSearchByAuthorMenu(scanner);
                case 3 -> showListAuthorsMenu();
                case 4 -> showListAuthorsByYearMenu(scanner);
                case 5 -> showListBooksByLanguageMenu(scanner);
                case 0 -> {
                    showExitMessage();
                    return;
                }
                default -> showInvalidOptionMessage();
            }
        }
    }

    private void showSearchByTitleMenu(Scanner scanner) {
        System.out.println("""
                BUSCAR LIBROS POR TÍTULO
                
                Ingrese el nombre del libro:
                """);

        String bookName = scanner.nextLine().trim();

        if (bookName.isEmpty()) {
            showEmptyBookNameMessage();
            return;
        }

        try {
            BookResponse response = API.fetch("https://gutendex.com/books/?search=" + bookName.replace(" ", "+"), BookResponse.class);

            response.results().forEach(book -> System.out.println("Título: " + book.title()));
        } catch (Throwable e) {
            System.out.println("No se encontraron libros con ese título");
        }

    }

    private void showSearchByAuthorMenu(Scanner scanner) {
        System.out.println("""
                BUSCAR LIBROS POR AUTOR
                
                Ingrese el nombre del autor:
                """);

        String authorName = scanner.nextLine().trim();

        if (authorName.isEmpty()) {
            showEmptyAuthorNameMessage();
            return;
        }

        try {
            // Utilizando el repositorio para buscar autores por nombre
            List<Author> authors = authorRepository.findByNameContainingIgnoreCase((authorName));

            if (authors.isEmpty()) {
                System.out.println("No se encontraron autores con ese nombre.");
                return;
            }
//
//            authors.forEach(author -> {
//                System.out.println("Autor: " + author.getName());
//                // Supongamos que tienes una relación entre Autor y Libros
////                author.getBooks().forEach(book -> System.out.println(" - Libro: " + book.getTitle()));
//            });
        } catch (Exception e) {
            System.out.println("Error al buscar autores: " + e.getMessage());
        }
    }

    private void showListAuthorsMenu() {
        System.out.println("LISTANDO AUTORES REGISTRADOS...");

//        try {
//            List<AuthorDTO> authors = authorRepository.findAll();
//            authors.forEach(author -> System.out.println("Autor: " + author.getName()));
//        } catch (Exception e) {
//            System.out.println("Error al listar autores: " + e.getMessage());
//        }
    }

    private void showListAuthorsByYearMenu(Scanner scanner) {
        System.out.println("""
                LISTAR AUTORES VIVOS EN UN DETERMINADO AÑO
                
                Ingrese el año:
                """);

//        String yearInput = scanner.nextLine().trim();
//
//        if (yearInput.isEmpty()) {
//            showEmptyYearMessage();
//            return;
//        }
//
//        try {
//            int year = Integer.parseInt(yearInput);
//            // Supongamos que tienes un método en AuthorRepository para esto
//            List<AuthorDTO> authors = authorRepository.findByBirthYearBeforeAndDeathYearAfter(year, year);
//
//            if (authors.isEmpty()) {
//                System.out.println("No hay autores vivos en el año " + year);
//                return;
//            }
//
//            authors.forEach(author -> System.out.println("Autor: " + author.getName()));
//        } catch (NumberFormatException e) {
//            System.out.println("Año inválido.");
//        } catch (Exception e) {
//            System.out.println("Error al listar autores: " + e.getMessage());
//        }
    }

    private void showListBooksByLanguageMenu(Scanner scanner) {
        System.out.println("""
                LISTAR LIBROS POR IDIOMA
                
                Ingrese el idioma:
                """);

        String language = scanner.nextLine().trim();

        if (language.isEmpty()) {
            System.out.println("Debe ingresar un idioma.");
            return;
        }

        try {
            // Supongamos que tienes un método en BookRepository para esto
            List<Book> books = bookRepository.findByLanguageIgnoreCase(language);

            if (books.isEmpty()) {
                System.out.println("No se encontraron libros en el idioma " + language);
                return;
            }

//            books.forEach(book -> System.out.println("Título: " + book.getTitle()));
        } catch (Exception e) {
            System.out.println("Error al listar libros: " + e.getMessage());
        }
    }

    private void showExitMessage() {
        System.out.println("Saliendo...");
    }

    private void showInvalidOptionMessage() {
        System.out.println("Opción no válida. Por favor, intente de nuevo.");
    }

    private void showEmptyBookNameMessage() {
        System.out.println("Debe ingresar un nombre de libro.");
    }

    private void showEmptyAuthorNameMessage() {
        System.out.println("Debe ingresar un nombre de autor.");
    }

    private void showEmptyYearMessage() {
        System.out.println("Debe ingresar un año.");
    }
}
