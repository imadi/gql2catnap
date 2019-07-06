package com.adi.gql2catpnap.utils

import com.adi.gql2catnap.models.Author
import com.adi.gql2catnap.models.Book
import com.adi.gql2catnap.models.Person
import spock.lang.Specification
import spock.lang.Unroll

import static com.adi.gql2catnap.utils.QueryUtil.convertGql2Catnap

@Unroll
class QueryUtilSpec extends Specification {

    static List<Person> personList = []

    def setupSpec() {
        Book book = new Book("book1", "naruto")
        book.setAuthors(Collections.singletonList(new Author("author1", "kishimoto")))
        Person person1 = new Person()
        person1.setName("adi")
        person1.setBooks(Collections.singletonList(book))
        personList.add(person1)
        Person person2 = new Person()
        person2.setName("vamsi")
        book = new Book("book2", "onepiece")
        book.setAuthors(Collections.singletonList(new Author("author2", "oda")))
        person2.setBooks(List.of(book))
        personList.add(person2)
    }

    def "test get fields when query is #query"() {
        expect:
        fields == convertGql2Catnap(query);
        where:
        query                                                     || fields
        "{persons{name,books{bookId,bookName,authors{id,name}}}}" || "name,books(bookId,bookName,authors(id,name))"
        "{\n" +
                "  persons {\n" +
                "    name\n" +
                "    books {\n" +
                "      bookId\n" +
                "      bookName\n" +
                "      authors {\n" +
                "        id\n" +
                "        name\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}"                                               || "name,books(bookId,bookName,authors(id,name))"

    }

}
