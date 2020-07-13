package com.oneape.octopus.parse.xml;

import lombok.Data;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-07-13 11:20.
 * Modify:
 */
@Data
public class XmlTestNode extends XmlNode {

    private String   exp1;
    private Operator op;
    private String   exp2;

    public XmlTestNode(NodeName nodeName) {
        super(nodeName);
    }
}
