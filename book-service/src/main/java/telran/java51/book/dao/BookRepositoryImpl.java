package telran.java51.book.dao;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import telran.java51.book.dto.exceptions.EntityNotFoundException;
import telran.java51.book.model.Author;
import telran.java51.book.model.Book;
import telran.java51.book.model.Publisher;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepository {

	final AuthorRepository authorRepository;
	
	@PersistenceContext
	EntityManager em;
	
	@Override
	public Stream<Book> findBooksByAuthorsName(String authorName) {
		Author author = authorRepository.findById(authorName).orElseThrow(EntityNotFoundException::new);
		return author.getBooks().stream();
	}

	@Override
	public Stream<Book> findBooksByPublisher(Publisher publisher) {
		return publisher.getBooks().stream();
	}

	@Override
	public boolean existsById(String isbn) {
		return em.find(Book.class, isbn) != null;
	}

	@Override
	@Transactional
	public Book save(Book book) {
		em.merge(book);
		return book;
	}

	@Override
	public Optional<Book> findById(String isbn) {
		return Optional.ofNullable(em.find(Book.class, isbn));
	}

	@Override
	public void deleteById(String isbn) {
		Book book = findById(isbn).orElseThrow(EntityNotFoundException::new);
		em.remove(book);
	}

}
