package io.github.kareiku;

import java.util.Arrays;
import java.util.Objects;

public class GraphicalUML extends Diagramiser {
    @Override
    public String diagramise(Class<?> clazz, String stereotype, String alias) {
        StringBuilder sb = new StringBuilder();

        stereotype = Objects.isNull(stereotype) ? "" : String.format("<<%s>>\\n", stereotype);
        alias = Objects.isNull(alias) ? Long.toString(System.nanoTime()) : alias;

        sb.append("object \"").append(stereotype).append(clazz.getSimpleName()).append("\" as ").append(alias).append(" {").append(System.lineSeparator());

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

        sb.append('}');

        return sb.toString();
    }
}
