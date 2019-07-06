package com.adi.gql2catnap.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Book {

    @NonNull
    private String bookId;

    @NonNull
    private String bookName;

    private List<Author> authors;

}
