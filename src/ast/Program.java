package ast;

import ast.folder.AbstractFolder;
import libs.Node;

import java.util.List;
import java.util.Objects;

public class Program extends Node{
    private final List<Macro> macros;
    private final List<AbstractFolder> folders;

    public Program(List<Macro> macros, List<AbstractFolder> folders) {
        this.macros = macros;
        this.folders = folders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Program program = (Program) o;
        return Objects.equals(macros, program.macros) && Objects.equals(folders, program.folders);
    }

}
