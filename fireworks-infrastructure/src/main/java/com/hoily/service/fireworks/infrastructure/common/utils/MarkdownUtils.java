package com.hoily.service.fireworks.infrastructure.common.utils;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

/**
 * Markdown utility
 *
 * @author vyckey
 */
public abstract class MarkdownUtils {
    public static String markdownToHtml(String markdownText) {
        try {
            Parser parser = Parser.builder().build();
            Node document = parser.parse(markdownText);
            HtmlRenderer htmlRenderer = HtmlRenderer.builder().build();
            return htmlRenderer.render(document);
        } catch (Exception e) {
            throw new IllegalArgumentException("convert markdown to html fail, markdown text:" + markdownText, e);
        }
    }
}
