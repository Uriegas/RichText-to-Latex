package com.upv.pm_2022;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Converts HTML to Latex
 */
public class HTMLParser {

    public class HTML2LatexException extends RuntimeException{};
    private static final Map<String, Pattern> regex;
    private static final String DOCUMENT_TYPE = "article";
    private static final String DOCUMENT = "\\documentclass{" + DOCUMENT_TYPE  + "}";
    private static final String PACKAGES = "\\usepackage{xcolor, soul}";
    private static final String HEADER = "\\begin{document}";
    private static final String FOOTER = "\\end{document}";

    static {
        regex = new HashMap<>();
        regex.put("\\\\textbf{",            Pattern.compile("<b>"));
        regex.put("\\\\textit{",            Pattern.compile("<i>"));
        regex.put("\\\\st{",                Pattern.compile("<strike>"));
        regex.put("\\\\underline{",         Pattern.compile("<u>"));
        regex.put("\\\\section{",           Pattern.compile("<h1>"));
        regex.put("\\\\subsection{",        Pattern.compile("<h2>"));
        regex.put("\\\\subsubsection{",     Pattern.compile("<h3>"));
        regex.put("{\\\\color{red} ",       Pattern.compile("<font color=\"#ff0000\">"));
        regex.put("\\\\begin{enumerate}",   Pattern.compile("<ol>"));
        regex.put("\\\\begin{itemize}",     Pattern.compile("<ul>"));
        regex.put("\\\\end{enumerate}",     Pattern.compile("</ol>"));
        regex.put("\\\\end{itemize}",       Pattern.compile("</ul>"));
        regex.put("\\\\item ",              Pattern.compile("<li>"));
        regex.put(" ",                      Pattern.compile("</li>"));
        regex.put("\\\\begin{quote}",       Pattern.compile("<quote>"));
        regex.put("\\\\end{quote}",         Pattern.compile("</quote>"));
        // NOTE: Quick fix for divs since group requires a dependency:
        // \begin{group} -> {\color{black}
        regex.put("{\\\\color{black} ",     Pattern.compile("<div>"));
//        regex.put("\\\\end{group}",         Pattern.compile("</div>"));
        regex.put("\\\\newline",            Pattern.compile("<br>"));
        regex.put("}",                      Pattern.compile("</.*?>"));
    }

    /**
     * Static function to convert html to latex
     * @param html
     * @return a string using LATEX format
     * TODO: Add headers and footer to file
     */
    public static String toLatex(String html) {
        for (Map.Entry<String, Pattern> reg : regex.entrySet())
            html = reg.getValue().matcher(html).replaceAll(reg.getKey());
        html = DOCUMENT + PACKAGES + HEADER + html + FOOTER; // Insert latex required commands
        return html;
    }
}
