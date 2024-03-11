package telran.java51.book.service;

import telran.java51.book.dto.AuthorDto;
import telran.java51.book.dto.BookDto;

public interface BookService {

	boolean addBook(BookDto bookDto);
	
	BookDto findBookByIsbn(String isbn);
	
	BookDto removeBookByIsbn(String isbn);
	
	BookDto updateBookTitle(String isbn, String title);

	Iterable<BookDto> findBooksByAuthor(String author);

	Iterable<BookDto> findBooksByPublisher(String publisher);

	Iterable<AuthorDto> findAuthorsByBook(String isbn);

	Iterable<String> findPublishersByAuthor(String author);

	AuthorDto removeAuthor(String author);
	
	
}
