package com.adi.gql2catnap.controllers;

import com.adi.gql2catnap.models.GraphQLRequestBody;
import com.adi.gql2catnap.models.Person;
import com.adi.gql2catnap.services.PersonService;
import com.adi.gql2catnap.utils.ModelUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.gregwhitaker.catnap.core.model.Model;
import com.github.gregwhitaker.catnap.core.query.parser.CatnapParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.adi.gql2catnap.utils.QueryUtil.convertGql2Catnap;

@RestController
@Slf4j
public class PersonController {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PersonService personService;

    @PostMapping(value = "/")
    public Object post(@RequestBody String body, @RequestHeader(value = "Content-Type") String contentType) throws IOException {
        log.info("contentType {}", contentType);
        String fields = getFieldsBasedOnContentType(body, contentType);
        log.info("fields {}", fields);
        List<Person> persons = personService.getPersonList();
        CatnapParser catnapParser = new CatnapParser();
        Model model = ModelUtil.build(persons, catnapParser.parse(fields));
        return model.getResult();
    }

    private String getFieldsBasedOnContentType(String body, String contentType) throws IOException {
        String fields;
        if (Objects.equals("application/graphql", contentType)) {
            fields = convertGql2Catnap(body);
        } else {
            GraphQLRequestBody graphQLRequestBody = objectMapper.readValue(body, GraphQLRequestBody.class);
            fields = convertGql2Catnap(graphQLRequestBody.getQuery());
        }
        return fields;
    }

}
