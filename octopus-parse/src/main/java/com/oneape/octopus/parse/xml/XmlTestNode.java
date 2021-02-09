package com.oneape.octopus.parse.xml;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Stack;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-07-13 11:20.
 * Modify:
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class XmlTestNode extends XmlNode {

    private String         exp1;
    private Operator       op;
    private String         exp2;

    private Stack<XmlNode> stack;

    public XmlTestNode(NodeName nodeName) {
        super(nodeName);
    }

    @Override
    public String toString() {
        return "XmlTestNode{" +
                "nodeName = " + getNodeName() + "" +
                ", exp1='" + exp1 + '\'' +
                ", op=" + op +
                ", exp2='" + exp2 + '\'' +
                '}';
    }
}
