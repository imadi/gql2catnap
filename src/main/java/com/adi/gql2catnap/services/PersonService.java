package com.adi.gql2catnap.services;

import com.adi.gql2catnap.models.Author;
import com.adi.gql2catnap.models.Book;
import com.adi.gql2catnap.models.Person;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PersonService {

    private static List<Person> personList = new ArrayList<>();

    public PersonService() {
        Book book = new Book("book1", "naruto");
        book.setAuthors(Collections.singletonList(new Author("author1", "kishimoto")));
        Person person1 = new Person();
        person1.setName("adi");
        person1.setBooks(Collections.singletonList(book));
        personList.add(person1);
        Person person2 = new Person();
        person2.setName("vamsi");
        book = new Book("book2", "onepiece");
        book.setAuthors(Collections.singletonList(new Author("author2", "oda")));
        person2.setBooks(List.of(book));
        personList.add(person2);
    }

    public List<Person> getPersonList() {
        return personList;
    }
}
