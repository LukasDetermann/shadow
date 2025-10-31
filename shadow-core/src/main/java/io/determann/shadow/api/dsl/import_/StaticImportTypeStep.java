package io.determann.shadow.api.dsl.import_;

import io.determann.shadow.api.dsl.declared.DeclaredRenderable;
import io.determann.shadow.api.dsl.field.FieldRenderable;
import io.determann.shadow.api.dsl.method.MethodRenderable;
import org.jetbrains.annotations.Contract;

public interface StaticImportTypeStep
{
   @Contract(value = "_ -> new", pure = true)
   ImportRenderable declared(String name);

   @Contract(value = "_ -> new", pure = true)
   ImportRenderable declared(DeclaredRenderable declared);

   @Contract(value = "_, _ -> new", pure = true)
   ImportRenderable method(DeclaredRenderable declared, MethodRenderable method);

   @Contract(value = "_, _ -> new", pure = true)
   ImportRenderable field(DeclaredRenderable declared, FieldRenderable field);
}