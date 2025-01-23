package com.cariniana.appalurachallengebooks.menu;

import com.cariniana.appalurachallengebooks.dto.BookDTO;
import com.cariniana.appalurachallengebooks.dto.BookResponse;
import com.cariniana.appalurachallengebooks.model.Author;
import com.cariniana.appalurachallengebooks.model.Book;
import com.cariniana.appalurachallengebooks.repository.AuthorRepository;
import com.cariniana.appalurachallengebooks.repository.BookRepository;
import com.cariniana.appalurachallengebooks.services.api.GutendexAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class Menu {

    private final AuthorRepository authorRepository;

    private final BookRepository bookRepository;

    @Autowired
    public Menu(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }


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

            int option;

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

    private void showSearchByTitleMenu(Scanner scanner) throws IOException, InterruptedException {
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
            BookResponse response = GutendexAPI.findBookByTitle(bookName, BookResponse.class);

            List<BookDTO> books = response.results();

            if (books.isEmpty()) {
                throw new RuntimeException("No se encontraron libros con ese título.");
            }

            books.forEach(book -> {
                System.out.println("Título: " + book.title());
                System.out.println("Autor: " + book.authors().get(0).name());
                System.out.println("Idioma: " + book.languages().get(0));
                System.out.println("Descargas: " + book.downloadCount());

                boolean existingBook = bookRepository.existsBooksByTitle(book.title());

                if (!existingBook) {
                    Book bookEntity = new Book(book);
                    if (!book.authors().isEmpty()) {
                        book.authors().forEach(author -> {
                            Author authorEntity = new Author(author.name(), author.birthYear(), author.deathYear(), bookEntity);
                            bookEntity.addAuthor(authorEntity);
                        });
                    }

                    bookRepository.save(bookEntity);
                }
            });
        } catch (Throwable e) {
            System.out.println("Error al buscar libros: " + e.getMessage());
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
            List<Author> authors = authorRepository.findByNameContainingIgnoreCase((authorName));

            if (authors.isEmpty()) {
                throw new RuntimeException("No se encontraron autores con ese nombre.");
            }

            authors.forEach(author -> {
                System.out.println("Autor: " + author.getName());
                Optional<Book> book = Optional.ofNullable(author.getBook());
                book.ifPresent(value -> System.out.println("Libro: " + value.getTitle()));
            });

        } catch (Exception e) {
            System.out.println("Error al buscar autores: " + e.getMessage());
        }
    }

    private void showListAuthorsMenu() {
        System.out.println("LISTANDO AUTORES REGISTRADOS...");

        try {
            List<Author> authors = authorRepository.findAll();

            if (authors.isEmpty()) {
                throw new RuntimeException("No hay autores registrados.");
            }

            authors.forEach(author -> System.out.println("Autor: " + author.getName()));
        } catch (Exception e) {
            System.out.println("Error al listar autores: " + e.getMessage());
        }
    }

    private void showListAuthorsByYearMenu(Scanner scanner) {
        System.out.println("""
                LISTAR AUTORES VIVOS EN UN DETERMINADO AÑO
                
                Ingrese el año:
                """);

        String yearInput = scanner.nextLine().trim();

        if (yearInput.isEmpty()) {
            showEmptyYearMessage();
        }

        try {
            int year = Integer.parseInt(yearInput);
            List<Author> authors = authorRepository.findByBirthYearBeforeAndDeathYearAfter(year, year);

            if (authors.isEmpty()) {
                throw new RuntimeException("No hay autores vivos en el año " + year);
            }

            authors.forEach(author -> System.out.println("Autor: " + author.getName()));
        } catch (NumberFormatException e) {
            System.out.println("Año inválido.");
        } catch (Exception e) {
            System.out.println("Error al listar autores: " + e.getMessage());
        }
    }

    private void showListBooksByLanguageMenu(Scanner scanner) {
        List<String> languages = bookRepository.getDistinctByLanguageList();
        if (languages.isEmpty()) {
            System.out.println("No hay idiomas registrados.");
            return;
        }
        System.out.println("Idimas disponibles: ");
        for (int i = 0; i < languages.size(); i++) {
            System.out.println(i + 1 + ". " + languages.get(i));
        }

        System.out.println("\nIngrese el idioma:");
        String language = scanner.nextLine().trim();

        if (language.isEmpty()) {
            System.out.println("Debe ingresar un idioma.");
            return;
        }

        try {
            List<Book> books = bookRepository.findByLanguageIgnoreCase(language);

            if (books.isEmpty()) {
                throw new RuntimeException("No hay libros en ese idioma.");
            }

            System.out.println("Libros en " + language + ":");
            books.forEach(book -> {
                System.out.println("Título: " + book.getTitle());
                System.out.println("Descargas: " + book.getDownloadCount());
                System.out.println("Lenguaje: " + book.getLanguage());
            });
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
