package com.oneape.octopus.parse.xml;

import lombok.Data;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-07-09 08:51.
 * Modify:
 */
@Data
public class XmlNode {
    // The xml node name.
    private NodeName nodeName;

    public XmlNode(NodeName nodeName) {
        this.nodeName = nodeName;
    }

}
