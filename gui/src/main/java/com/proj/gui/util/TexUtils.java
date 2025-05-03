package com.proj.gui.util;

import com.proj.masi.dto.structure.*;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public final class TexUtils {

    private TexUtils() {}

    private static final Map<Character,String> ESC = Map.of(
            '_', "\\\\_",
            '{', "\\\\{",
            '}', "\\\\}",
            '#', "\\\\#",
            '%', "\\\\%",
            '&', "\\\\&"
    );

    public static String toTex(TermDto node) {
        return switch (node) {
            case UnitermDto u       -> esc(u.name());
            case SequenceDto s      -> renderSequence(s);
            case ParallelDto p      -> renderParallel(p);
            case CustomDto   c      -> toTex(c.root());
            default -> throw new IllegalArgumentException("Unknown TermDto: " + node);
        };
    }


    private static String renderSequence(SequenceDto s) {
        String sep = Objects.requireNonNullElse(s.separator(), ";");
        return "\\overgroup{" +
                s.children().stream()
                        .map(TexUtils::toTex)
                        .collect(Collectors.joining(" " + esc(sep) + " ")) +
                "}";
    }

    private static String renderParallel(ParallelDto p) {
        String sep = Objects.requireNonNullElse(p.separator(), ";");
        String rowJoiner = " \\\\ " + esc(sep) + " \\\\ ";

        return "\\left[\\begin{array}{l}" +
                p.children().stream()
                        .map(TexUtils::toTex)
                        .collect(Collectors.joining(rowJoiner)) +
                "\\end{array}\\right.";
    }

    private static String esc(String in) {
        var sb = new StringBuilder(in.length());
        for (char ch : in.toCharArray()) {
            sb.append(ESC.getOrDefault(ch, String.valueOf(ch)));
        }
        return sb.toString();
    }
}
