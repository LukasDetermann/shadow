package io.determann.shadow.api.dsl.interface_;

import io.determann.shadow.api.dsl.declared.DeclaredRenderable;
import io.determann.shadow.api.dsl.field.FieldRenderable;
import io.determann.shadow.api.dsl.method.MethodRenderable;

import java.util.Arrays;
import java.util.List;

public interface InterfaceBodyStep
      extends InterfaceRenderable
{
   InterfaceRenderable body(String body);

   InterfaceBodyStep field(String... fields);

   default InterfaceBodyStep field(FieldRenderable... fields)
   {
      return field(Arrays.asList(fields));
   }

   InterfaceBodyStep field(List<? extends FieldRenderable> fields);

   InterfaceBodyStep method(String... methods);

   default InterfaceBodyStep method(MethodRenderable... methods)
   {
      return method(Arrays.asList(methods));
   }

   InterfaceBodyStep method(List<? extends MethodRenderable> methods);

   InterfaceBodyStep inner(String... inner);

   default InterfaceBodyStep inner(DeclaredRenderable... inner)
   {
      return inner(Arrays.asList(inner));
   }

   InterfaceBodyStep inner(List<? extends DeclaredRenderable> inner);
}