package telran.java51.book.dao;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import telran.java51.book.model.Publisher;

@Repository
@RequiredArgsConstructor
public class PublisherRepositoryImpl implements PublisherRepository {

	final AuthorRepository authorRepository;
	
	@PersistenceContext
	EntityManager em;
	
	@Override
	public Stream<Publisher> findDistinctByBooksAuthorsName(String authorName) {
//		return authorRepository.findById(authorName)
//				.orElseThrow(EntityNotFoundException::new)
//				.getBooks()
//				.stream()
//				.map(b -> b.getPublisher())
//				.distinct();
	TypedQuery<Publisher> query = em.createQuery("select distinct p from Book b join b.authors a join b.publisher p where a.name=?1", Publisher.class);
	query.setParameter(1, authorName);
	return query.getResultStream();
	
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
