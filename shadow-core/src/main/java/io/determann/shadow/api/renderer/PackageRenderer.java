package io.determann.shadow.api.renderer;

public interface PackageRenderer
{
   /// `package my.example;`
   /// @throws IllegalArgumentException when the package `isUnnamed`
   String declaration(RenderingContext renderingContext);
}
