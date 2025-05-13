package io.github.kareiku;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IDiagramiser {
    @NotNull String diagramise(@NotNull Class<?> clazz);

    @NotNull String diagramise(@NotNull Class<?> clazz, @Nullable String stereotype, @Nullable String alias);
}
