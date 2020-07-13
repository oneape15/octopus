package com.oneape.octopus.parse.xml;

import com.oneape.octopus.data.SyntaxException;
import jdk.internal.org.objectweb.asm.tree.analysis.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
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

    // The node stack
    private       Stack<XmlNode>     stack;
    private final Map<String, Value> paramMap;

    public DslParser(String rawSql) {
        this(rawSql, null, true);
    }

    public DslParser(String rawSql, Map<String, Value> params) {
        this(rawSql, params, false);
    }

    private DslParser(String rawSql, Map<String, Value> params, boolean onlyCheck) {
        this.stack = new Stack<>();
        this.paramMap = params == null ? new HashMap<>() : params;

        // parse the raw sql
        parsing(rawSql);

        // Grammatical correctness detection.
        // Check if-elseif-else-fi
        checkGrammar();

        // if only check then return.
        if (onlyCheck) return;

        // Run expression.
        // Replace the placeholder "#{xxx}" with "?"
        runExpression();
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

        // Convert to an array of characters.
        char[] chars = neatDslString.toCharArray();

        int prevIndex = 0;
        for (int i = 0; i < dslLen; ) {
            if (chars[i] != '<') {
                i += 1;
                continue;
            }

            // Matches to the string "<IF "
            if (i + 3 < dslLen
                    && (chars[i + 1] == 'I' || chars[i + 1] == 'i')
                    && (chars[i + 2] == 'F' || chars[i + 2] == 'f')
                    && chars[i + 3] == ' ') {
                boolean foundEndTag = false;
                // Look for the nearest character ">"
                for (int j = i + 3; j < dslLen; j++) {
                    if (chars[j] == '>') {
                        foundEndTag = true;
                        // Get the text information before the <IF> node
                        XmlNode textNode = getTextNode(chars, prevIndex, i);
                        if (textNode != null) {
                            stack.push(textNode);
                        }

                        XmlNode ifNode = getTestNode(NodeName.IF, chars, i + 3, j);

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
            else if (i + 3 < dslLen
                    && (chars[i + 1] == 'F' || chars[i + 1] == 'f')
                    && (chars[i + 2] == 'I' || chars[i + 2] == 'i')
                    && chars[i + 3] == '>') {
                // Get the text information before the <FI> node
                XmlNode textNode = getTextNode(chars, prevIndex, i);
                if (textNode != null) {
                    stack.push(textNode);
                }

                // The index move 3 step.
                i += 3;
                stack.push(new XmlNode(NodeName.FI));
                prevIndex = i + 1;
            }
            // Matches to the string "<ELSE>"
            else if (i + 5 < dslLen
                    && (chars[i + 1] == 'E' || chars[i + 1] == 'e')
                    && (chars[i + 2] == 'L' || chars[i + 2] == 'l')
                    && (chars[i + 3] == 'S' || chars[i + 3] == 's')
                    && (chars[i + 4] == 'E' || chars[i + 4] == 'e')
                    && chars[i + 5] == '>') {
                // Get the text information before the <ELSE> node
                XmlNode textNode = getTextNode(chars, prevIndex, i);
                if (textNode != null) {
                    stack.push(textNode);
                }

                // The index move 5 step.
                i += 5;
                stack.push(new XmlNode(NodeName.ELSE));
                prevIndex = i + 1;
            }
            //  Matches to the string "<ELSEIF "
            else if (i + 7 < dslLen
                    && (chars[i + 1] == 'E' || chars[i + 1] == 'e')
                    && (chars[i + 2] == 'L' || chars[i + 2] == 'l')
                    && (chars[i + 3] == 'S' || chars[i + 3] == 's')
                    && (chars[i + 4] == 'E' || chars[i + 4] == 'e')
                    && (chars[i + 5] == 'I' || chars[i + 5] == 'i')
                    && (chars[i + 6] == 'F' || chars[i + 6] == 'f')
                    && chars[i + 7] == ' ') {
                boolean foundEndTag = false;
                // Look for the nearest character ">"
                for (int j = i + 7; j < dslLen; j++) {
                    if (chars[j] == '>') {
                        foundEndTag = true;

                        // Get the text information before the <ELSEIF> node
                        XmlNode textNode = getTextNode(chars, prevIndex, i);
                        if (textNode != null) {
                            stack.push(textNode);
                        }

                        XmlNode elseIfNode = getTestNode(NodeName.ELSEIF, chars, i + 7, j);
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
            } else {
                i++;
            }
        }

        // The end string
        if (prevIndex < dslLen) {
            XmlNode textNode = getTextNode(chars, prevIndex, dslLen);
            if (textNode != null) {
                stack.push(textNode);
            }
        }
    }

    /**
     * Grammatical correctness detection.
     */
    private void checkGrammar() {
        Stack<XmlNode> copyStack = new Stack<>();
        copyStack.addAll(stack);

        Stack<XmlNode> tmpStack = new Stack<>();
        while (!copyStack.empty()) {
            XmlNode node = copyStack.pop();

            if (node.getNodeName() == NodeName.IF) {
                if (!tmpStack.isEmpty() && tmpStack.peek().getNodeName() == NodeName.FI) {
                    tmpStack.pop();
                } else {
                    tmpStack.push(node);
                }
                continue;
            }

            if (node.getNodeName() == NodeName.TEXT) {
                continue;
            }

            // Determines if the node <else> and <elseif> are inside the <if> node.
            if (node.getNodeName() == NodeName.ELSE || node.getNodeName() == NodeName.ELSEIF) {
                if (tmpStack.isEmpty() || tmpStack.peek().getNodeName() != NodeName.FI) {
                    throw new SyntaxException("Nodes <else> and <elseif> must be inside the <if> node");
                }
                continue;
            }

            if (node.getNodeName() == NodeName.FI) {
                if (!tmpStack.isEmpty() && tmpStack.peek().getNodeName() == NodeName.IF) {
                    tmpStack.pop();
                } else {
                    tmpStack.push(node);
                }
            }
        }

        // Check the tmp stack whether is empty.
        if (!tmpStack.isEmpty()) {
            NodeName nodeName = tmpStack.peek().getNodeName();
            if (nodeName == NodeName.IF) {
                throw new SyntaxException("The node <" + nodeName + "> has not end.");
            } else {
                throw new SyntaxException("The node <" + nodeName + "> has not start.");
            }
        }
    }

    /**
     * Run expression.
     * Replace the placeholder "#{xxx}" with "?"
     */
    private void runExpression() {
        if (stack.isEmpty()) return;

        Stack<XmlNode> tmpStack = new Stack<>();
        for (XmlNode node : stack) {
            if (node.getNodeName() == NodeName.TEXT) {
                tmpStack.push(node);
            }
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
    private XmlTextNode getTextNode(char[] chars, int startPos, int endPos) {
        if (chars == null
                || startPos >= endPos
                || chars.length <= startPos
                || chars.length <= endPos
                ) {
            return null;
        }

        char[] value = new char[endPos - startPos];
        System.arraycopy(chars, startPos, value, 0, value.length);
        String content = new String(value);
        if (StringUtils.isBlank(content)) {
            return null;
        }

        return new XmlTextNode(content);
    }

    /**
     * Gets the test content of <if> and <elseif> node.
     *
     * @param chars    The char Array.
     * @param startPos The information starting position
     * @param endPos   The information ending position
     * @return XmlNode
     */
    private XmlTestNode getTestNode(NodeName nodeName, char[] chars, int startPos, int endPos) {
        char[] value = new char[endPos - startPos];
        System.arraycopy(chars, startPos, value, 0, value.length);
        String testContent = StringUtils.trimToEmpty(new String(value));
        if (!StringUtils.startsWithIgnoreCase(testContent, "TEST")
                || !StringUtils.contains(testContent, "=")) {
            throw new SyntaxException("There is an error near the string [" + new String(value) + "]");
        }

        String[] arr = StringUtils.split(testContent, "=");
        if (arr == null || arr.length != 2) {
            throw new SyntaxException("There is an error near the string [" + new String(value) + "]");
        }
        String content = StringUtils.trimToEmpty(arr[1]);

        // Remove the before and after quotes.
        if (StringUtils.startsWith(content, "\"") && StringUtils.endsWith(content, "\"")) {
            content = StringUtils.substring(content, 1, content.length() - 1);
        }
        // Remove the before and after quotation marks
        if (StringUtils.startsWith(content, "'") && StringUtils.endsWith(content, "'")) {
            content = StringUtils.substring(content, 1, content.length() - 1);
        }

        // Checks if the expression is correct
        int index = StringUtils.indexOf(content, " ");
        if (index <= -1) {
            throw new SyntaxException("The expression [" + new String(value) + "] is incorrect.");
        }
        String exp1 = StringUtils.substring(content, 0, index);

        content = StringUtils.trimToEmpty(StringUtils.substring(content, index));
        if (StringUtils.isBlank(content)) {
            throw new SyntaxException("The expression [" + new String(value) + "] is incorrect.");
        }

        index = StringUtils.indexOf(content, " ");

        String op, exp2;
        if (index <= -1) {
            op = content;
            exp2 = null;
        } else {
            op = StringUtils.trimToEmpty(StringUtils.substring(content, 0, index));
            exp2 = StringUtils.trimToEmpty(StringUtils.substring(content, index));
        }

        Operator operator = Operator.instance(op);
        if (operator == null) {
            throw new SyntaxException("The operator [ " + op + " ] is incorrect.");
        }

        XmlTestNode node = new XmlTestNode(nodeName);
        node.setExp1(exp1);
        node.setExp2(exp2);
        node.setOp(operator);

        return node;
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
