package com.oneape.octopus.parse.xml;

import com.oneape.octopus.data.SyntaxException;
import jdk.internal.org.objectweb.asm.tree.analysis.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Stack;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-07-09 09:55.
 * Modify:
 */
@Slf4j
public class DslParser {
    private final static String LINE_COMMENT = "-- ";

    // Only syntax validity markers are checked
    private final boolean onlyCheck;

    // The node stack
    private Stack<XmlNode> stack;

    private final Map<String, Value> params;

    public DslParser(String rawSql) {
        this(rawSql, null, true);
    }

    public DslParser(String rawSql, Map<String, Value> params) {
        this(rawSql, params, false);
    }

    private DslParser(String rawSql, Map<String, Value> params, boolean onlyCheck) {
        this.onlyCheck = onlyCheck;
        this.stack = new Stack<>();
        this.params = params;

        // parse the raw sql
        parsing(rawSql);

        //
        for (XmlNode xn : stack) {
            System.out.println(xn.toString());
        }
    }

    /**
     * parse the raw sql
     *
     * @param rawSql String
     */
    private void parsing(String rawSql) {
        // Checks if the string is empty.
        if (StringUtils.isBlank(rawSql)) {
            log.warn("The rawSql is blank.");
            return;
        }

        String neatDslString = clearDslSqlString(rawSql);
        int dslLen = neatDslString.length();
        if (dslLen <= 0) {
            return;
        }

        // Convert to an array of characters, all char is upper case.
        char[] chars = StringUtils.upperCase(neatDslString).toCharArray();

        int prevIndex = 0;
        for (int i = 0; i < dslLen; ) {
            if (chars[i] != '<') {
                i += 1;
                continue;
            }

            // Matches to the string "<IF "
            if (i + 3 < dslLen && chars[i + 1] == 'I' && chars[i + 2] == 'F' && chars[i + 3] == ' ') {
                boolean foundEndTag = false;
                // Look for the nearest character ">"
                for (int j = i + 3; j < dslLen; j++) {
                    if (chars[j] == '>') {
                        foundEndTag = true;
                        // Get the text information before the <IF> node
                        stack.push(getTextNode(chars, prevIndex, i));

                        XmlNode ifNode = new XmlNode(XmlNode.NodeName.IF);
                        ifNode.setTest(getTestContent(chars, i + 3, j - 1));


                        // The index move to j;
                        i = j + 1;
                        stack.push(ifNode);
                        prevIndex = i;

                        // jump the inner loop.
                        break;
                    }
                }
                if (!foundEndTag) {
                    throw new SyntaxException("The tag '<IF' did not end properly.");
                }
            }
            // Matches to the string "<FI>"
            else if (i + 3 < dslLen && chars[i + 1] == 'F' && chars[i + 2] == 'I' && chars[i + 3] == '>') {
                // Get the text information before the <FI> node
                stack.push(getTextNode(chars, prevIndex, i));

                // The index move 3 step.
                i += 3;
                stack.push(new XmlNode(XmlNode.NodeName.FI));
                prevIndex = i + 1;
            }
            // Matches to the string "<ELSE>"
            else if (i + 5 < dslLen && chars[i + 1] == 'E' && chars[i + 2] == 'L' && chars[i + 3] == 'S' && chars[i + 4] == 'E' && chars[i + 5] == '>') {
                // Get the text information before the <ELSE> node
                stack.push(getTextNode(chars, prevIndex, i));

                // The index move 5 step.
                i += 5;
                stack.push(new XmlNode(XmlNode.NodeName.ELSE));
                prevIndex = i + 1;
            }
            //  Matches to the string "<ELSEIF "
            else if (i + 7 < dslLen && chars[i + 1] == 'E' && chars[i + 2] == 'L' && chars[i + 3] == 'S' && chars[i + 4] == 'E' && chars[i + 5] == 'I' && chars[i + 6] == 'F' && chars[i + 7] == ' ') {
                boolean foundEndTag = false;
                // Look for the nearest character ">"
                for (int j = i + 7; j < dslLen; j++) {
                    if (chars[j] == '>') {
                        foundEndTag = true;

                        // Get the text information before the <ELSEIF> node
                        stack.push(getTextNode(chars, prevIndex, i));


                        XmlNode elseIfNode = new XmlNode(XmlNode.NodeName.ELSEIF);
                        elseIfNode.setTest(getTestContent(chars, i + 7, j - 1));
                        stack.push(elseIfNode);

                        // The index move to j;
                        i = j + 1;
                        prevIndex = i;

                        // jump the inner loop.
                        break;
                    }
                }
                if (!foundEndTag) {
                    throw new SyntaxException("The tag '<ELSEIF' did not end properly.");
                }
            }
        }

        // The end string
        if (prevIndex < dslLen) {
            stack.push(getTextNode(chars, prevIndex, dslLen));
        }
    }

    /**
     * Gets the text information node content
     *
     * @param chars    The char Array.
     * @param startPos The information starting position
     * @param endPos   The information ending position
     * @return XmlNode
     */
    private XmlNode getTextNode(char[] chars, int startPos, int endPos) {
        XmlNode textNode = new XmlNode(XmlNode.NodeName.TEXT);

        char[] value = new char[endPos - startPos];
        System.arraycopy(chars, startPos, value, 0, value.length);
        textNode.setContent(new String(value));

        return textNode;
    }

    /**
     * Gets the test content of <if> and <elseif> node.
     *
     * @param chars    The char Array.
     * @param startPos The information starting position
     * @param endPos   The information ending position
     * @return XmlNode
     */
    private String getTestContent(char[] chars, int startPos, int endPos) {
        char[] value = new char[endPos - startPos];
        System.arraycopy(chars, startPos, value, 0, value.length);

        return new String(value);
    }

    /**
     * Remove comments and line breaks and non-characters.
     * <p>
     * 1. Remove line breaks
     * 2. Remove comments
     *
     * @param rawSql The dsl sql text
     * @return String
     */
    private String clearDslSqlString(String rawSql) {
        // Remove comments and line breaks and non-characters
        Charset charset = Charset.forName("utf8");
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(rawSql.getBytes(charset)), charset));
            String line;
            int index = 0;
            while ((line = br.readLine()) != null) {
                line = StringUtils.trimToEmpty(line);

                // Discard information such as blank line comments
                if (StringUtils.isBlank(line) || StringUtils.startsWith(line, LINE_COMMENT)) {
                    continue;
                }

                if (index++ > 0) sb.append(" ");

                sb.append(line);
            }
        } catch (Exception e) {
            log.error("read the raw sql error", e);
            throw new SyntaxException("read the raw sql error.", e);
        }

        return sb.toString();
    }


}
