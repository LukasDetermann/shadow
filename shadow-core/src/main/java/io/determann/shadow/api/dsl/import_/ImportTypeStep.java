package io.determann.shadow.api.dsl.import_;

import io.determann.shadow.api.dsl.declared.DeclaredRenderable;
import io.determann.shadow.api.dsl.field.FieldRenderable;
import io.determann.shadow.api.dsl.method.MethodRenderable;
import io.determann.shadow.api.dsl.package_.PackageRenderable;
import org.jetbrains.annotations.Contract;

public interface ImportTypeStep
{
   @Contract(value = "_ -> new", pure = true)
   ImportRenderable import_(String name);

   @Contract(value = "_ -> new", pure = true)
   ImportRenderable import_(DeclaredRenderable declared);

   @Contract(value = "_, _ -> new", pure = true)
   ImportRenderable import_(DeclaredRenderable declared, MethodRenderable method);

   @Contract(value = "_, _ -> new", pure = true)
   ImportRenderable import_(DeclaredRenderable declared, FieldRenderable field);

   @Contract(value = "_ -> new", pure = true)
   ImportRenderable importAll(String cPackage);

   @Contract(value = "_ -> new", pure = true)
   ImportRenderable importAll(PackageRenderable cPackage);

   @Contract(value = "_ -> new", pure = true)
   ImportRenderable importAll(DeclaredRenderable declared);
}