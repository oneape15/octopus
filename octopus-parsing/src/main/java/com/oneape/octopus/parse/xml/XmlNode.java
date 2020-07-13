package com.oneape.octopus.parse.xml;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-07-09 08:51.
 * Modify:
 */
@Data
public class XmlNode {
    // The xml node name.
    private NodeName nodeName;
    // A condition
    private String   test;
    // text content
    private String   content;

    enum NodeName {
        // inner node
        TEXT,

        // normal node
        IF,
        ELSEIF,
        ELSE,
        FI;
    }

    public final static List<NodeName> nodeNameList;

    static {
        nodeNameList = new ArrayList<>();
        nodeNameList.add(NodeName.IF);
        nodeNameList.add(NodeName.ELSEIF);
        nodeNameList.add(NodeName.ELSE);
        nodeNameList.add(NodeName.FI);
    }

    public XmlNode(NodeName nodeName) {
        this.nodeName = nodeName;
    }

}
