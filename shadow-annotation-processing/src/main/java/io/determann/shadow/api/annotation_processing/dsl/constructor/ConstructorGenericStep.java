package io.determann.shadow.api.annotation_processing.dsl.constructor;

import io.determann.shadow.api.annotation_processing.dsl.generic.GenericRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface ConstructorGenericStep
      extends ConstructorTypeStep
{
   @Contract(value = "_ -> new", pure = true)
   ConstructorGenericStep genericDeclaration(String... genericDeclarations);

   @Contract(value = "_ -> new", pure = true)
   default ConstructorGenericStep genericDeclaration(GenericRenderable... genericDeclarations)
   {
      return genericDeclaration(Arrays.asList(genericDeclarations));
   }

   @Contract(value = "_ -> new", pure = true)
   ConstructorGenericStep genericDeclaration(List<? extends GenericRenderable> genericDeclarations);
}
