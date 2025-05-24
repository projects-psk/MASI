package com.proj.masi.util;

import com.proj.masi.dto.structure.ParallelDto;
import com.proj.masi.dto.structure.SequenceDto;
import com.proj.masi.dto.structure.TermDto;

import java.util.ArrayList;
import java.util.List;

public final class ASTUtils {
    private ASTUtils() { }

    public static TermDto replaceAt(TermDto root, List<Integer> path, TermDto replacement) {
        int idx = path.getFirst();

        if (path.size() == 1) {
            if (root instanceof SequenceDto(List<TermDto> children, String sep)) {
                var newChildren = new ArrayList<>(children);
                newChildren.set(idx, replacement);
                return new SequenceDto(newChildren, sep);
            }
            if (root instanceof ParallelDto(List<TermDto> children, String sep)) {
                var newChildren = new ArrayList<>(children);
                newChildren.set(idx, replacement);
                return new ParallelDto(newChildren, sep);
            }
            throw new IllegalArgumentException("Cannot replace child on leaf: " + root);
        }

        var subPath = path.subList(1, path.size());
        if (root instanceof SequenceDto(List<TermDto> children, String sep)) {
            var newChildren = new ArrayList<>(children);
            newChildren.set(idx,
                    replaceAt(children.get(idx), subPath, replacement)
            );
            return new SequenceDto(newChildren, sep);
        }
        if (root instanceof ParallelDto(List<TermDto> children, String sep)) {
            var newChildren = new ArrayList<>(children);
            newChildren.set(idx,
                    replaceAt(children.get(idx), subPath, replacement)
            );
            return new ParallelDto(newChildren, sep);
        }

        throw new IllegalArgumentException("Cannot traverse into leaf: " + root);
    }
}
