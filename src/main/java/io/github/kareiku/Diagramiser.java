package io.github.kareiku;

import java.lang.reflect.*;

public abstract class Diagramiser implements IDiagramiser {
    @Override
    public String diagramise(Class<?> clazz) {
        return this.diagramise(clazz, null, null);
    }

    protected char getModifierSymbol(int modifiers) {
        if (Modifier.isPublic(modifiers)) return '+';
        else if (Modifier.isProtected(modifiers)) return '#';
        else if (Modifier.isPrivate(modifiers)) return '-';
        else return '~';
    }

    protected String getStaticOrAbstract(int modifiers) {
        if (Modifier.isStatic(modifiers)) return "{static}";
        else if (Modifier.isAbstract(modifiers)) return "{abstract}";
        else return "";
    }

    protected String formatField(Field field) {
        return String.format("    %c%s%s: %s\n",
                this.getModifierSymbol(field.getModifiers()),
                this.getStaticOrAbstract(field.getModifiers()),
                field.getName(),
                field.getType().getSimpleName());
    }

    protected String formatConstructor(Constructor<?> constructor) {
        return String.format("    %c%s%s(%s)\n",
                this.getModifierSymbol(constructor.getModifiers()),
                this.getStaticOrAbstract(constructor.getModifiers()),
                constructor.getName(),
                this.formatParameters(constructor));
    }

    protected String formatMethod(Method method) {
        String returnType = method.getReturnType().equals(Void.TYPE) ? ": " + method.getReturnType().getSimpleName() : "";

        return String.format("    %c%s%s(%s)%s\n",
                this.getModifierSymbol(method.getModifiers()),
                this.getStaticOrAbstract(method.getModifiers()),
                method.getName(),
                this.formatParameters(method),
                returnType);
    }

    protected String formatParameters(Executable executable) {
        StringBuilder sb = new StringBuilder();
        Parameter[] params = executable.getParameters();

        for (int i = 0; i < params.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(params[i].getName())
                    .append(": ")
                    .append(params[i].getType().getSimpleName());
        }

        return sb.toString();
    }
}
