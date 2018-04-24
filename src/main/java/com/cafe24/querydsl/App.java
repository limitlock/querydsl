package com.cafe24.querydsl;

import static com.cafe24.querydsl.domain.QBook.book; //statc 변수 import해서 가져오깅
import static com.cafe24.querydsl.domain.QCategory.category;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.cafe24.querydsl.domain.Book;
import com.cafe24.querydsl.domain.Category;
import com.cafe24.querydsl.domain.Kind;
import com.cafe24.querydsl.domain.QBook;
import com.cafe24.querydsl.dto.BookDTO;
import com.mysema.query.SearchResults;
import com.mysema.query.Tuple;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.Projections;

public class App {
	public static void main(String[] args) {
		// 1. Entity Manager Factory 생성
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("querydsl"); // db

		// 2. Entity Manager 생성
		EntityManager em = emf.createEntityManager();

		// 3.Get TX
		EntityTransaction tx = em.getTransaction();

		// 4. TX Begins
		tx.begin();

		// 5. Business Logic
		try {

			/*
			 * testInsertCateroies(em); testInsertBooks(em);
			 */

			testJoin2(em);

		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}

		// 6. TX Commit
		tx.commit();

		// 7. Entity Manager 종료
		em.clear();

		// 8. Entity Manager Factory 종료
		emf.close();
	}
	
	public static void testJoin3(EntityManager em) {

		JPAQuery query = new JPAQuery(em);
		List<Book> list = 
				query.
				from(book).
				join(book.category, category).
				on(category.name.like("%자%")).
				list(book);
 
		for (Book book : list) {
			System.out.println(book);
		}
	}

	

	public static void testJoin2(EntityManager em) {

		JPAQuery query = new JPAQuery(em);
		List<Book> list = 
				query.
				from(book).
				join(book.category, category).
				on(category.name.like("%자%")).
				list(book);

		for (Book book : list) {
			System.out.println(book);
		}
	}

	public static void testJoin(EntityManager em) {

		JPAQuery query = new JPAQuery(em);
		List<Book> list = 
				query.
				from(book).
				join(book.category, category).
				list(book);

		for (Book book : list) {
			System.out.println(book);
		}
	}

	public static void testPaging2(EntityManager em) {
		int page = 1;
		JPAQuery query = new JPAQuery(em);
		SearchResults<Book> results = query.from(book).offset((page - 1) * 3)
				.orderBy(book.price.desc(), book.title.asc()).limit(3).listResults(book);

		Long totalCount = results.getTotal();
		Long offset = results.getOffset(); // 페이지에서 보여지는 첫번째 게시물의 번호값
		Long limit = results.getLimit(); // 페이지당 보여지는 게시물 수

		System.out.println(totalCount + ":" + offset + ":" + limit);
		for (Book book : results.getResults()) {
			System.out.println(book);
		}
	}

	public static void testPaging1(EntityManager em) {
		int page = 1;
		JPAQuery query = new JPAQuery(em);
		List<Book> list = query.from(book).offset((page - 1) * 3).limit(3).list(book);
		for (Book book2 : list) {
			System.out.println(book2);
		}
	}

	public static void testProjectionByConstructor(EntityManager em) {
		JPAQuery query = new JPAQuery(em);
		List<BookDTO> list = query.from(book)
				.list(Projections.constructor(BookDTO.class, book.no.as("no"), book.title));
		for (BookDTO dto : list) {
			System.out.println(dto);
		}

	}

	public static void testProjectionByFields(EntityManager em) {
		JPAQuery query = new JPAQuery(em);
		List<BookDTO> list = query.from(book).list(Projections.fields(BookDTO.class, book.no.as("no"), book.title));
		for (BookDTO dto : list) {
			System.out.println(dto);
		}

	}

	public static void testProjectionByProperty(EntityManager em) {
		JPAQuery query = new JPAQuery(em);
		List<BookDTO> list = query.from(book).list(Projections.bean(BookDTO.class, book.no.as("no"), book.title));
		for (BookDTO dto : list) {
			System.out.println(dto);
		}

	}

	public static void testProjectionOne(EntityManager em) {
		JPAQuery query = new JPAQuery(em);
		List<String> list = query.from(book).list(book.title);
		for (String title : list) {
			System.out.println(title);
		}

	}

	public static void testProjectionTuple(EntityManager em) {
		JPAQuery query = new JPAQuery(em);
		List<Tuple> list = query.from(book).list(book.title, book.price);
		for (Tuple t : list) {
			System.out.println(t.get(book.title) + ":" + t.get(book.price));
		}

	}

	public static void testMaxBookPrice(EntityManager em) {
		JPAQuery query = new JPAQuery(em);
		Integer maxPrice = query.from(book).uniqueResult(book.price.max());
		System.out.println(maxPrice);

	}

	public static void testSearch1(EntityManager em) {
		JPAQuery query = new JPAQuery(em);

		List<Book> list = query.from(book).where(book.title.eq("자바의신").and(book.price.lt(30000L))).list(book);

		for (Book book : list) {
			System.out.println(book);
		}
	}

	public static void testSearch2(EntityManager em) {
		JPAQuery query = new JPAQuery(em);

		List<Book> list = query.from(book).where(book.title.eq("자바의신"), book.price.lt(30000L)).list(book);

		for (Book book : list) {
			System.out.println(book);
		}
	}

	public static void testQueryDSL4(EntityManager em) {
		JPAQuery query = new JPAQuery(em);

		List<Book> list = query.from(book).where(book.price.between(10000L, 30000L)).orderBy(book.price.desc())
				.list(book);

		for (Book book : list) {
			System.out.println(book);
		}
	}

	public static void testQueryDSL5(EntityManager em) {
		JPAQuery query = new JPAQuery(em);

		List<Book> list = query.from(book).where(book.title.contains("자바")).orderBy(book.price.desc()).list(book);

		for (Book book : list) {
			System.out.println(book);
		}

	}

	public static void testQueryDSL6(EntityManager em) {
		JPAQuery query = new JPAQuery(em);

		List<Book> list = query.from(book).where(book.title.startsWith("Spring")).orderBy(book.price.desc()).list(book);

		for (Book book : list) {
			System.out.println(book);
		}

	}

	public static void testQueryDSL1(EntityManager em) {
		JPAQuery query = new JPAQuery(em);
		QBook qBook = new QBook("book");

		List<Book> list = query.from(qBook).where(qBook.price.gt(10000L)).orderBy(qBook.price.desc()).list(qBook);

		for (Book book : list) {
			System.out.println(book);
		}

	}

	public static void testQueryDSL2(EntityManager em) {
		JPAQuery query = new JPAQuery(em);

		List<Book> list = query.from(QBook.book).where(QBook.book.price.gt(10000L)).orderBy(QBook.book.price.desc())
				.list(QBook.book);

		for (Book book : list) {
			System.out.println(book);
		}

	}

	public static void testQueryDSL3(EntityManager em) {
		JPAQuery query = new JPAQuery(em);

		List<Book> list = query.from(book).where(book.price.gt(10000L)).orderBy(book.price.desc()).list(book);

		for (Book book : list) {
			System.out.println(book);
		}

	}

	public static void testInsertBooks(EntityManager em) {
		Category category1 = em.find(Category.class, 1L);
		Book book1 = new Book();
		book1.setTitle("자바의신");
		book1.setPrice(10000);
		book1.setKind(Kind.A);
		book1.setRegDate(new Date());
		book1.setDescription("자바책입니다.");
		book1.setCategory(category1);

		em.persist(book1);

		Category category2 = em.find(Category.class, 2L);
		Book book2 = new Book();
		book2.setTitle("Spring in Action");
		book2.setPrice(20000);
		book2.setKind(Kind.B);
		book2.setRegDate(new Date());
		book2.setDescription("스프링책입니다.");
		book2.setCategory(category2);

		em.persist(book2);

		Category category3 = em.find(Category.class, 3L);
		Book book3 = new Book();
		book3.setTitle("C 프로그래밍");
		book3.setPrice(30000);
		book3.setKind(Kind.C);
		book3.setRegDate(new Date());
		book3.setDescription("C 책입니다.");
		book3.setCategory(category3);

		em.persist(book3);
	}

	public static void testInsertCateroies(EntityManager em) {

		// 1L
		Category category1 = new Category();
		category1.setName("자바 프로그래밍");
		em.persist(category1);

		// 2L
		Category category2 = new Category();
		category2.setName("스프링 프로그래밍");
		em.persist(category2);

		// 3L
		Category category3 = new Category();
		category3.setName("C 프로그래밍");
		em.persist(category3);
	}

}
