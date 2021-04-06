package com.oneape.octopus.parse.xml;

import com.oneape.octopus.commons.dto.Value;
import com.oneape.octopus.parse.ParseFactory;
import com.oneape.octopus.parse.data.ParseResult;
import com.oneape.octopus.parse.data.SyntaxException;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Parse the DSL in XML mode
 * Created by oneape<oneape15@163.com>
 * Created 2020-07-08 17:27.
 * Modify:
 */
public class XmlParseFactory implements ParseFactory {

    /**
     * parser the dsl language to sql.
     *
     * @param dslSql The dsl language text.
     * @param params The deal param list.
     * @return Parse result
     * @throws SyntaxException Syntax exception is thrown incorrectly
     */
    @Override
    public ParseResult parse(String dslSql, Map<String, Value> params) throws SyntaxException {

        // check the dsl sql.
        DslParser dslParser = new DslParser(dslSql, params);
        if (!dslParser.isGrammarStatus()) {
            throw new SyntaxException("There is an error in the DSL syntax.");
        }

        ParseResult pr = new ParseResult();
        pr.setRawSql(dslParser.getRawSql());
        pr.setValues(dslParser.getArgs());

        return pr;
    }

    /**
     * Validate the DSL syntax
     *
     * @param dslSql The dsl language text.
     * @return true - correct; false - wrong grammar
     * @throws SyntaxException Syntax exception is thrown incorrectly
     */
    @Override
    public boolean detection(String dslSql) throws SyntaxException {
        if (StringUtils.isBlank(dslSql)) return true;

        // Just check the grammar
        DslParser dslParser = new DslParser(dslSql);

        return dslParser.isGrammarStatus();
    }
}
