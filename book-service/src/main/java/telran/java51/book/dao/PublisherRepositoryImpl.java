package telran.java51.book.dao;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import telran.java51.book.dto.exceptions.EntityNotFoundException;
import telran.java51.book.model.Publisher;

@Repository
@RequiredArgsConstructor
public class PublisherRepositoryImpl implements PublisherRepository {

	final AuthorRepository authorRepository;
	
	@PersistenceContext
	EntityManager em;
	
	@Override
	public Stream<Publisher> findDistinctByBooksAuthorsName(String authorName) {
		return authorRepository.findById(authorName)
				.orElseThrow(EntityNotFoundException::new)
				.getBooks()
				.stream()
				.map(b -> b.getPublisher())
				.distinct();
	}

	@Override
	public Optional<Publisher> findById(String publisher) {
		return Optional.ofNullable(em.find(Publisher.class, publisher));
	}

	@Override
	public Publisher save(Publisher publisher) {
		em.persist(publisher);
		return publisher;
	}

}
