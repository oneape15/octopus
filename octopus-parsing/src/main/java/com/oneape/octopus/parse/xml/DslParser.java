package com.oneape.octopus.parse.xml;

import com.oneape.octopus.commons.dto.Value;
import com.oneape.octopus.commons.value.Pair;
import com.oneape.octopus.data.SyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-07-09 09:55.
 * Modify:
 */
@Slf4j
public class DslParser {
    private final static String LINE_COMMENT       = "--";
    private static final String SEQ_COMMA_START    = "/*";
    private static final String SEQ_COMMA_END      = "*/";
    private static final int    SEQ_COMMA_CHAR_LEN = SEQ_COMMA_END.length();

    // The node stack
    private       Stack<XmlNode>     stack;
    private final Map<String, Value> paramMap;

    private String      rawSql;
    private List<Value> args;
    // The dsl grammar is right ?
    private boolean grammarStatus = false;

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
        log.debug("The raw dsl String: {}", neatDslString);
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
        this.grammarStatus = true;
    }

    /**
     * Run expression.
     * 1. Replace the placeholder "#{xxx}" with "?"
     * 2. Replace the placeholder "@{xxx}" with the real value.
     */
    private void runExpression() {
        if (stack.isEmpty()) return;

        Pair<Integer, XmlNode> valPair = testIfNode(stack, 0, true);
        if (valPair == null || valPair.getRight() == null) {
            throw new SyntaxException("Run expression Error, The result is null.");
        }

        String dslString;
        XmlNode node = valPair.getRight();
        if (node instanceof XmlTextNode) {
            dslString = ((XmlTextNode) node).getContent();
        } else {
            throw new SyntaxException("Run expression Error, the result node type error. ");
        }

        log.debug("after test if , dsl String: {}", dslString);

        List<Value> params = new ArrayList<>();
        char[] chars = dslString.toCharArray();
        int len = chars.length;

        List<Character> list = new ArrayList<>(len);
        for (int i = 0; i < len; ) {
            if (chars[i] != '#' && chars[i] != '@') {
                list.add(chars[i++]);
                continue;
            }

            // Is the use real value tag ?
            boolean isUseRealValue = chars[i] == '@';

            if (i + 1 < len && chars[i + 1] == '{') {
                boolean hasFound = false;
                int j = i + 1;
                for (; j < len; ) {
                    if (chars[j++] == '}') {
                        hasFound = true;
                        break;
                    }
                }
                if (hasFound) {
                    // Only the string of fields between ("#{", "@{") and "}" is needed
                    char[] valTag = new char[j - i - 3];
                    System.arraycopy(chars, i + 2, valTag, 0, valTag.length);
                    String tagName = new String(valTag);
                    Value value = paramMap.get(tagName);
                    if (value == null) {
                        throw new SyntaxException("Run expression Error, [" + tagName + "] No input parameter");
                    }

                    // Determines whether it is a set
                    if (value.isMulti() || value.isRange()) {
                        Object valObj = value.getValue();
                        if (valObj == null) {
                            throw new RuntimeException("The range parameter: [" + tagName + "] cannot be null.");
                        }
                        List<Object> arrVal = new ArrayList<>();
                        if (valObj.getClass().isArray()) {
                            if (valObj instanceof List) {
                                arrVal.addAll((List) valObj);
                            } else {
                                Object[] arr = (Object[]) valObj;
                                arrVal.addAll(Arrays.asList(arr));
                            }
                        } else {
                            arrVal.add(valObj);
                        }

                        if (value.isMulti()) {
                            // Erase the last equal.
                            eraseTheLastEqual(list);

                            addChar2List(list, false, " IN ( ", null);
                            for (int index = 0; index < arrVal.size(); index++) {
                                Object tmp = arrVal.get(index);
                                if (!isUseRealValue) {
                                    params.add(new Value(tmp, value.getDataType()));
                                }
                                if (index > 0) {
                                    list.add(',');
                                }
                                addChar2List(list, isUseRealValue, "?", tmp != null ? tmp.toString() : "");
                            }
                            addChar2List(list, false, " ) ", null);
                        } else {
                            Object minVal = arrVal.get(0);
                            Object maxVal = arrVal.size() == 1 ? arrVal.get(0) : arrVal.get(1);
                            if (!isUseRealValue) {
                                params.add(new Value(minVal, value.getDataType()));
                                params.add(new Value(maxVal, value.getDataType()));
                            }

                            // Erase the last equal.
                            eraseTheLastEqual(list);

                            addChar2List(list,
                                    isUseRealValue,
                                    " BETWEEN ? AND ? ",
                                    " BETWEEN " + minVal + " AND " + maxVal + " "
                            );
                        }

                    } else {
                        // normal param
                        if (!isUseRealValue) {
                            params.add(value);
                        }
                        addChar2List(list, isUseRealValue, "?", value.getValue() != null ? value.getValue().toString() : "");
                    }
                    i = j;
                    continue;
                }
            }

            // No match was found
            list.add(chars[i++]);
        }

        char[] tmp = new char[list.size()];
        for (int i = 0; i < list.size(); i++) {
            tmp[i] = list.get(i);
        }

        this.rawSql = StringUtils.trimToEmpty(new String(tmp));

        // If you end with English semicolon or Chinese semicolon, then remove.
        if (StringUtils.endsWith(this.rawSql, ";") || StringUtils.endsWith(this.rawSql, "；")) {
            this.rawSql = StringUtils.substring(this.rawSql, 0, this.rawSql.length() - 1);
        }

        this.args = params;
    }

    /**
     * Add the value to the list.
     *
     * @param characters     List
     * @param isUseRealValue boolean
     * @param replaceValue   String
     * @param realValue      String
     */
    private void addChar2List(List<Character> characters, Boolean isUseRealValue, String replaceValue, String realValue) {
        char[] arr;
        if (isUseRealValue) {
            if (StringUtils.isBlank(realValue)) return;

            arr = realValue.toCharArray();
        } else {
            if (StringUtils.isBlank(replaceValue)) return;
            arr = replaceValue.toCharArray();
        }

        for (char tmp : arr) {
            characters.add(tmp);
        }
    }

    /**
     * Erase the last equal.
     *
     * @param characters List
     */
    private void eraseTheLastEqual(List<Character> characters) {
        if (CollectionUtils.isEmpty(characters)) return;

        int index = characters.size() - 1;
        for (; index > -1; index--) {
            if (characters.get(index) == ' ') {
                continue;
            }
            if (characters.get(index) == '=') {
                break;
            }

            // If it is another character, it pops out.
            return;
        }
        // remove the equal char.
        characters.remove(index);
    }

    /**
     * Test the if node
     */
    private Pair<Integer, XmlNode> testIfNode(Stack<XmlNode> stack, int fromIndex, boolean ifTest) {
        Stack<XmlNode> retStack = new Stack<>();
        int curIndex = fromIndex;
        for (int i = fromIndex; i < stack.size(); ) {
            XmlNode node = stack.get(i);
            if (node.getNodeName() == NodeName.IF) {
                Pair<Integer, XmlNode> tmp = testIfNode(stack, i + 1, testNode((XmlTestNode) node));
                retStack.push(tmp.getRight());
                i = tmp.getLeft();
                continue;
            }
            if (node.getNodeName() == NodeName.FI) {
                curIndex = i + 1;
                break;
            }
            i++;
            retStack.push(node);
        }

        String text = "";
        boolean testStatus = ifTest;
        for (XmlNode node : retStack) {
            if (node.getNodeName() == NodeName.TEXT) {
                if (testStatus) {
                    text += " " + ((XmlTextNode) node).getContent();
                }
            } else if (node.getNodeName() == NodeName.ELSEIF) {
                testStatus = testNode((XmlTestNode) node);
            } else if (node.getNodeName() == NodeName.ELSE) {
                testStatus = !ifTest;
            }
        }
        return new Pair<>(curIndex, new XmlTextNode(text));
    }

    private boolean testNode(XmlTestNode testNode) {
        Object val1 = paramMap.containsKey(testNode.getExp1()) ? paramMap.get(testNode.getExp1()).getValue() : testNode.getExp1();
        Object val2 = paramMap.containsKey(testNode.getExp2()) ? paramMap.get(testNode.getExp2()).getValue() : testNode.getExp2();

        return testNode.getOp().compare(val1, val2);
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
                || chars.length < endPos
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
    public static String clearDslSqlString(String rawSql) {
        // Remove comments and line breaks and non-characters
        Charset charset = Charset.forName("utf8");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(rawSql.getBytes(charset)), charset));

            String line;
            int index = 0;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                line = StringUtils.trimToEmpty(line);

                // Discard information such as blank line comments
                if (StringUtils.isBlank(line) || StringUtils.startsWith(line, LINE_COMMENT)) {
                    continue;
                }

                // Delete the segment comment block that in one line.
                int startIndex = StringUtils.indexOf(line, SEQ_COMMA_START);
                int endIndex = StringUtils.indexOf(line, SEQ_COMMA_END);
                if (startIndex > -1 && endIndex > 0 && startIndex < endIndex) {
                    line = StringUtils.substring(line, 0, startIndex) + StringUtils.substring(line, endIndex + SEQ_COMMA_CHAR_LEN);
                }

                if (index++ > 0) sb.append(" ");

                sb.append(line);
            }

            // Delete the segment comment block.
            return removeSegmentComment(sb.toString());
        } catch (Exception e) {
            log.error("read the raw sql error", e);
            throw new SyntaxException("read the raw sql error.", e);
        }
    }

    /**
     * Delete the segment comment block.
     *
     * @param sql String
     * @return String
     */
    private static String removeSegmentComment(String sql) {
        if (StringUtils.isBlank(sql) || !StringUtils.contains(sql, SEQ_COMMA_START)) {
            return sql;
        }

        int start = StringUtils.indexOf(sql, SEQ_COMMA_START);
        int end = StringUtils.indexOf(sql, SEQ_COMMA_END);

        if (start > 0 && end > 0 && start < end) {
            String tmp = StringUtils.substring(sql, 0, start) + StringUtils.substring(sql, end + SEQ_COMMA_CHAR_LEN);
            return removeSegmentComment(tmp);
        }

        return sql;
    }


    public String getRawSql() {
        return rawSql;
    }

    public List<Value> getArgs() {
        return args;
    }

    public boolean isGrammarStatus() {
        return grammarStatus;
    }
}
