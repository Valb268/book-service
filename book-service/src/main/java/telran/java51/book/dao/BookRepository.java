package telran.java51.book.dao;

import java.util.Optional;
import java.util.stream.Stream;

import telran.java51.book.model.Book;
import telran.java51.book.model.Publisher;

public interface BookRepository  {

	Stream<Book> findBooksByAuthorsName(String author);

	Stream<Book> findBooksByPublisher(Publisher publisher);

	boolean existsById(String isbn);

	Book save(Book book);

	Optional<Book> findById(String isbn);

	void deleteById(String isbn);
}
