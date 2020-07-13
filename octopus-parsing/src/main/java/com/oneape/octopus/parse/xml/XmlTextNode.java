package com.oneape.octopus.parse.xml;

import lombok.Data;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-07-13 11:17.
 * Modify:
 */
@Data
public class XmlTextNode extends XmlNode {
    // The text node content.
    private String content;

    public XmlTextNode(String content) {
        super(NodeName.TEXT);
        this.content = content;
    }
}
