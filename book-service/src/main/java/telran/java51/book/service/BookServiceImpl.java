package telran.java51.book.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import telran.java51.book.dao.AuthorRepository;
import telran.java51.book.dao.BookRepository;
import telran.java51.book.dao.PublisherRepository;
import telran.java51.book.dto.AuthorDto;
import telran.java51.book.dto.BookDto;
import telran.java51.book.dto.exceptions.EntityNotFoundException;
import telran.java51.book.model.Author;
import telran.java51.book.model.Book;
import telran.java51.book.model.Publisher;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

	final BookRepository bookRepository;
	final ModelMapper modelMapper;
	final PublisherRepository publisherRepository;
	final AuthorRepository authorRepository;

	@Override
	@Transactional
	public boolean addBook(BookDto bookDto) {
		if (bookRepository.existsById(bookDto.getIsbn())) {
			return false;
		}
		Set<Author> authors = bookDto.getAuthors().stream()
				.map(a -> authorRepository.findById(a.getName())
						.orElse(authorRepository.save(
								new Author(a.getName(), a.getBirthDate()))))
				.collect(Collectors.toSet());

		Publisher publisher = publisherRepository
				.findById(bookDto.getPublisher()).orElse(publisherRepository
						.save(new Publisher(bookDto.getPublisher())));
		Book book = new Book(bookDto.getIsbn(), bookDto.getTitle(), authors,
				publisher);
		bookRepository.save(book);

		return true;
	}

	@Override
	public BookDto findBookByIsbn(String isbn) {
		Book book = bookRepository.findById(isbn)
				.orElseThrow(EntityNotFoundException::new);
		return modelMapper.map(book, BookDto.class);
	}

	@Override
	@Transactional
	public BookDto removeBookByIsbn(String isbn) {
		Book book = bookRepository.findById(isbn)
				.orElseThrow(EntityNotFoundException::new);
		bookRepository.deleteById(isbn);
		return modelMapper.map(book, BookDto.class);
	}

	@Override
	@Transactional
	public BookDto updateBookTitle(String isbn, String title) {
		Book book = bookRepository.findById(isbn)
				.orElseThrow(EntityNotFoundException::new);
		book.setTitle(title);
		return modelMapper.map(book, BookDto.class);
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<BookDto> findBooksByAuthor(String author) {
		return bookRepository.findBooksByAuthorsName(author)
				.map(b -> modelMapper.map(b, BookDto.class)).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<BookDto> findBooksByPublisher(String publisher) {
		return bookRepository.findBooksByPublisher(new Publisher(publisher))
				.map(b -> modelMapper.map(b, BookDto.class)).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<AuthorDto> findAuthorsByBook(String isbn) {
		Book book = bookRepository.findById(isbn)
				.orElseThrow(EntityNotFoundException::new);
		return book.getAuthors().stream()
				.map(a -> modelMapper.map(a, AuthorDto.class)).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<String> findPublishersByAuthor(String author) {
		return bookRepository.findBooksByAuthorsName(author)
				.map(b -> b.getPublisher().toString()).distinct().toList();
	}

	@Override
	@Transactional
	public AuthorDto removeAuthor(String authorName) {
		Author author = authorRepository.findById(authorName)
				.orElseThrow(EntityNotFoundException::new);
		AuthorDto authorDto = modelMapper.map(author, AuthorDto.class);
		List<String> isbnToRemove = bookRepository
				.findBooksByAuthorsName(authorName).map(b -> b.getIsbn()).toList();
		isbnToRemove.forEach(bookRepository::deleteById);
		authorRepository.deleteById(authorName);
		return authorDto;
	}

}
