package io.github.kareiku;

public interface IDiagramiser {
    String diagramise(Class<?> clazz);

    String diagramise(Class<?> clazz, String stereotype, String alias);
}
