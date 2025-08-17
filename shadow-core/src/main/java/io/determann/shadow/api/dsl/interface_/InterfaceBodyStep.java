package io.determann.shadow.api.dsl.interface_;

import io.determann.shadow.api.dsl.declared.DeclaredRenderable;
import io.determann.shadow.api.dsl.field.FieldRenderable;
import io.determann.shadow.api.dsl.method.MethodRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface InterfaceBodyStep
      extends InterfaceRenderable
{
   @Contract(value = "_ -> new", pure = true)
   InterfaceRenderable body(String body);

   @Contract(value = "_ -> new", pure = true)
   InterfaceBodyStep field(String... fields);

   @Contract(value = "_ -> new", pure = true)
   default InterfaceBodyStep field(FieldRenderable... fields)
   {
      return field(Arrays.asList(fields));
   }

   @Contract(value = "_ -> new", pure = true)
   InterfaceBodyStep field(List<? extends FieldRenderable> fields);

   @Contract(value = "_ -> new", pure = true)
   InterfaceBodyStep method(String... methods);

   @Contract(value = "_ -> new", pure = true)
   default InterfaceBodyStep method(MethodRenderable... methods)
   {
      return method(Arrays.asList(methods));
   }

   @Contract(value = "_ -> new", pure = true)
   InterfaceBodyStep method(List<? extends MethodRenderable> methods);

   @Contract(value = "_ -> new", pure = true)
   InterfaceBodyStep inner(String... inner);

   @Contract(value = "_ -> new", pure = true)
   default InterfaceBodyStep inner(DeclaredRenderable... inner)
   {
      return inner(Arrays.asList(inner));
   }

   @Contract(value = "_ -> new", pure = true)
   InterfaceBodyStep inner(List<? extends DeclaredRenderable> inner);
}