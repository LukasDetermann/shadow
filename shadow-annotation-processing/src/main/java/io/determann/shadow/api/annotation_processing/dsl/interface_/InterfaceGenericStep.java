package io.determann.shadow.api.annotation_processing.dsl.interface_;

import io.determann.shadow.api.annotation_processing.dsl.TypeRenderable;
import io.determann.shadow.api.annotation_processing.dsl.generic.GenericRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface InterfaceGenericStep
      extends InterfaceExtendsStep
{
   @Contract(value = "_ -> new", pure = true)
   InterfaceGenericStep genericDeclaration(String... genericDeclarations);

   @Contract(value = "_ -> new", pure = true)
   default InterfaceGenericStep genericDeclaration(GenericRenderable... genericDeclarations)
   {
      return genericDeclaration(Arrays.asList(genericDeclarations));
   }

   @Contract(value = "_ -> new", pure = true)
   InterfaceGenericStep genericDeclaration(List<? extends GenericRenderable> genericDeclarations);

   @Contract(value = "_ -> new", pure = true)
   InterfaceGenericStep genericUsage(String... genericUsages);

   @Contract(value = "_ -> new", pure = true)
   default InterfaceGenericStep genericUsage(TypeRenderable... genericUsages)
   {
      return genericUsage(Arrays.asList(genericUsages));
   }

   @Contract(value = "_ -> new", pure = true)
   InterfaceGenericStep genericUsage(List<? extends TypeRenderable> genericUsages);
}
