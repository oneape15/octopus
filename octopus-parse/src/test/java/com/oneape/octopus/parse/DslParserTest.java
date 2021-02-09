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
            "<if test=\"#{useType} nb\">\n" +
            "   AND useType = 1 AND sex = #{sex} AND user_type = #{useType}\n" +
            "  <elseif test = \"#{useType} eq 2\">\n" +
            "  AND useType = 2\n" +
            "  <else>\n" +
            "    <if test=\"#{sex} eq 0\">\n" +
            "       AND useType < 3\n" +
            "      <else>\n" +
            "       AND userType > 3\n" +
            "    <fi>\n" +
            "<fi>\n" +
            "order by sex\n" +
            "LIMIT @{start}, @{end}";


    public static void main(String[] args) {
        Map<String, Value> map = new HashMap<>();
        Value val1 = new Value(new Integer[]{1, 3}, DataType.INTEGER);
        val1.setRange(true);
        map.put("useType", val1);
        Value val2 = new Value(new Integer[]{1, 3, 4, 6, 8}, DataType.INTEGER);
        val2.setMulti(true);
        map.put("sex", val2);
        map.put("start", new Value(10, DataType.INTEGER));
        map.put("end", new Value(20, DataType.INTEGER));

        DslParser dp = new DslParser(dslSql, map);

        System.out.println(dp.getRawSql());
    }
}
