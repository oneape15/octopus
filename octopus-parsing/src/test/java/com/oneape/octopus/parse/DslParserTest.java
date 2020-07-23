package com.oneape.octopus.parse;

import com.oneape.octopus.commons.dto.DataType;
import com.oneape.octopus.commons.dto.Value;
import com.oneape.octopus.parse.xml.DslParser;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-07-13 09:48.
 * Modify:
 */
public class DslParserTest {

    private static final String dslSql = "SELECT * FROM \n" +
            "tb_user\n" +
            "WHERE 1 = 1 \n" +
            "<if test=\"#{useType} eq 1\">\n" +
            "   AND useType = 1 AND sex = #{sex}\n" +
            "  <elseif test = \"#{useType} eq 2\">\n" +
            "  AND useType = 2\n" +
            "  <else>\n" +
            "    <if test=\"#{sex} eq 0\">\n" +
            "       AND useType < 3\n" +
            "      <else>\n" +
            "       AND userType > 3\n" +
            "    <fi>\n" +
            "<fi>\n" +
            "order by sex";


    public static void main(String[] args) {
        Map<String, Value> map = new HashMap<>();
        map.put("#{useType}", new Value(1, DataType.INTEGER));
        map.put("#{sex}", new Value(1, DataType.INTEGER));

        DslParser dp = new DslParser(dslSql, map);

        System.out.println(dp.getRawSql());
    }
}
