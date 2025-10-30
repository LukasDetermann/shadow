package io.determann.shadow.api.dsl.class_;

import io.determann.shadow.api.dsl.TypeRenderable;
import io.determann.shadow.api.dsl.generic.GenericRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface ClassGenericStep
      extends ClassExtendsStep
{
   @Contract(value = "_ -> new", pure = true)
   ClassGenericStep genericDeclaration(String... genericDeclarations);

   @Contract(value = "_ -> new", pure = true)
   default ClassGenericStep genericDeclaration(GenericRenderable... genericDeclarations)
   {
      return genericDeclaration(Arrays.asList(genericDeclarations));
   }

   @Contract(value = "_ -> new", pure = true)
   ClassGenericStep genericDeclaration(List<? extends GenericRenderable> genericDeclarations);

   @Contract(value = "_ -> new", pure = true)
   ClassGenericStep genericUsage(String... genericUsages);

   @Contract(value = "_ -> new", pure = true)
   default ClassGenericStep genericUsage(TypeRenderable... genericUsages)
   {
      return genericUsage(Arrays.asList(genericUsages));
   }

   @Contract(value = "_ -> new", pure = true)
   ClassGenericStep genericUsage(List<? extends TypeRenderable> genericUsages);
}
