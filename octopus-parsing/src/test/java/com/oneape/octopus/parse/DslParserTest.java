package com.oneape.octopus.parse;

import com.oneape.octopus.parse.xml.DslParser;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-07-13 09:48.
 * Modify:
 */
public class DslParserTest {

    private static final String dslSql = "SELECT * FROM \n" +
            "tb_user\n" +
            "WHERE 1 =1 \n" +
            "<if test=\"#{useType} eq 1\">\n" +
            "   AND useType = 1\n" +
            "  <elseif test=\"#{useType} eq 2\">\n" +
            "  AND useType = 2\n" +
            "  <else>\n" +
            "    AND useType = 3\n" +
            "<fi>\n" +
            "order by sex" ;

    public static void main(String[] args) {
        DslParser dp = new DslParser(dslSql);
    }
}
