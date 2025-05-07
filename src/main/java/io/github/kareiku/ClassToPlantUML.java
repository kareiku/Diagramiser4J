package io.github.kareiku;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Objects;

public class ClassToPlantUML {
    private static ClassToPlantUML instance;

    private ClassToPlantUML() {
    }

    public static synchronized ClassToPlantUML getInstance() {
        if (instance == null) {
            instance = new ClassToPlantUML();
        }
        return instance;
    }

    public @NotNull String diagramise(@NotNull Class<?> clazz) {
        return this.diagramise(clazz, null, null);
    }

    public @NotNull String diagramise(@NotNull Class<?> clazz, @Nullable String stereotype, @Nullable String alias) {
        StringBuilder sb = new StringBuilder();

        stereotype = Objects.isNull(stereotype) ? "" : String.format("<<%s>>\\n", stereotype);
        alias = Objects.isNull(alias) ? Long.toString(System.nanoTime()) : alias;

        sb.append("object \"").append(stereotype).append(clazz.getSimpleName()).append("\" as ").append(alias).append(" {\n");

        Arrays.stream(clazz.getDeclaredFields())
                .filter((field) -> !field.isSynthetic())
                .forEach((field) -> sb.append(this.formatField(field)));
        sb.append("    ---\n");

        Arrays.stream(clazz.getDeclaredConstructors())
                .filter((constructor) -> !constructor.isSynthetic())
                .forEach((constructor) -> sb.append(this.formatConstructor(constructor)));

        Arrays.stream(clazz.getDeclaredMethods())
                .filter((method) -> !method.isSynthetic())
                .forEach((method) -> sb.append(this.formatMethod(method)));

        sb.append("}");

        return sb.toString();
    }

    private char getModifierSymbol(int modifiers) {
        if (Modifier.isPublic(modifiers)) return '+';
        else if (Modifier.isProtected(modifiers)) return '#';
        else if (Modifier.isPrivate(modifiers)) return '-';
        else return '~';
    }

    private @NotNull String getStaticOrAbstract(int modifiers) {
        if (Modifier.isStatic(modifiers)) return "{static}";
        else if (Modifier.isAbstract(modifiers)) return "{abstract}";
        else return "";
    }

    private @NotNull String formatField(@NotNull Field field) {
        return String.format("    %c%s%s: %s\n",
                this.getModifierSymbol(field.getModifiers()),
                this.getStaticOrAbstract(field.getModifiers()),
                field.getName(),
                field.getType().getSimpleName());
    }

    private @NotNull String formatConstructor(@NotNull Constructor<?> constructor) {
        return String.format("    %c%s%s(%s)\n",
                this.getModifierSymbol(constructor.getModifiers()),
                this.getStaticOrAbstract(constructor.getModifiers()),
                constructor.getName(),
                this.formatParameters(constructor));
    }

    private @NotNull String formatMethod(@NotNull Method method) {
        String returnType = method.getReturnType().equals(Void.TYPE) ? ": " + method.getReturnType().getSimpleName() : "";

        return String.format("    %c%s%s(%s)%s\n",
                this.getModifierSymbol(method.getModifiers()),
                this.getStaticOrAbstract(method.getModifiers()),
                method.getName(),
                this.formatParameters(method),
                returnType);
    }

    private @NotNull String formatParameters(@NotNull Executable executable) {
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
