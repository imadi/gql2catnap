package com.adi.gql2catnap.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class Person {

    private String name;

    private List<Book> books;

}
