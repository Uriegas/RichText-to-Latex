package com.upv.pm_2022;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
// TODO: Prettify expected string
@RunWith(JUnit4.class)
public class HTMLParserUnitTest {
    @Test
    public void parsing() {
        String input =  "Hola <b>como </b>estan<u>?<br><strike>No se<br><br></strike></u><h1>" +
                        "Titulo 1</h1><h2>Subtitulo</h2><h3>Subsubtitulo</h3><div><font color" +
                        "=\"#ff0000\">a</font></div><div><br></div><ol><li>qw</li><li>123</li" +
                        "></ol><div><ul><li>qwdqw</li><li>qwdqw</li></ul></div>";

        String expected="\\documentclass{article}\\usepackage{xcolor, soul}\\begin{document}H" +
                        "ola \\textbf{como }estan\\underline{?\\newline\\st{No se\\newlin" +
                        "e\\newline}}\\section{Titulo 1}\\subsection{Subtitulo}\\subsubsec" +
                        "tion{Subsubtitulo}{\\color{black} {\\color{red} a}}{\\" +
                        "color{black} \\newline}\\begin{enumerate}\\item qw \\it" +
                        "em 123 \\end{enumerate}{\\color{black} \\begin{itemize}\\item qwdqw" +
                        " \\item qwdqw \\end{itemize}}\\end{document}";
        String actual = HTMLParser.toLatex(input);
        assertEquals(expected, actual);
    }
}
