package com.oneape.octopus.parse;

import com.oneape.octopus.commons.dto.Value;
import com.oneape.octopus.parse.data.ParseResult;
import com.oneape.octopus.parse.data.SyntaxException;

import java.util.Map;

/**
 * Parser the dsl language
 */
public interface ParsingFactory {

    /**
     * parser the dsl language to sql.
     *
     * @param dslSql The dsl language text.
     * @param params The deal param list.
     * @return Parse result
     * @throws SyntaxException Syntax exception is thrown incorrectly
     */
    ParseResult parse(String dslSql, Map<String, Value> params) throws SyntaxException;

    /**
     * Remove the SQL comment information.
     *
     * @param dslSql The dsl language text.
     * @return String
     */
    String clearCommaInfo(String dslSql);

    /**
     * Validate the DSL syntax
     *
     * @param dslSql The dsl language text.
     * @return true - correct; false - wrong grammar
     * @throws SyntaxException Syntax exception is thrown incorrectly
     */
    boolean detection(String dslSql) throws SyntaxException;
}
