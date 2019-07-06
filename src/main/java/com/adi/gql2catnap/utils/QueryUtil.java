package com.adi.gql2catnap.utils;

import com.adi.gql2catnap.models.GraphQLRequestBody;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

@Slf4j
public class QueryUtil {

    private static final String OPEN_CURLY_BRACE_EXPRESSION = "{";
    private static final String CLOSE_CURLY_BRACE_EXPRESSION = "}";
    private static final String SUBQUERY_OPEN = "(";
    private static final String SUBQUERY_CLOSE = ")";
    private static final String SPACE_CHAR_REGEX = "\\s+([A-Za-z])";
    private static final String BRACE_REGEX = "([{}])\\s+";
    private static final String LINE_BREAK_REGEX = "[\\r\\n]|([\\r\\n])\\s+";

    private QueryUtil() { }

    public static String convertGql2Catnap(String query) {
        query = query.replaceAll(LINE_BREAK_REGEX, "");
        query = query.replaceAll(BRACE_REGEX, "$1");
        log.info("query {}", query);
        String fieldStr = query.substring(StringUtils.ordinalIndexOf(query, OPEN_CURLY_BRACE_EXPRESSION, 2) + 1,
                query.lastIndexOf(CLOSE_CURLY_BRACE_EXPRESSION) - 1);
        fieldStr = fieldStr
                .replaceAll("\\" + OPEN_CURLY_BRACE_EXPRESSION, SUBQUERY_OPEN)
                .replaceAll(CLOSE_CURLY_BRACE_EXPRESSION, SUBQUERY_CLOSE)
                .replaceAll(SPACE_CHAR_REGEX, ",$1");
        return fieldStr.replaceAll("\\s+", "");
    }

}
