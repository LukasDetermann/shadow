package io.determann.shadow.api.annotation_processing.dsl.method;

import io.determann.shadow.api.annotation_processing.dsl.generic.GenericRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface MethodGenericStep
      extends MethodResultStep
{
   @Contract(value = "_ -> new", pure = true)
   MethodGenericStep genericDeclaration(String... genericDeclarations);

   @Contract(value = "_ -> new", pure = true)
   default MethodGenericStep genericDeclaration(GenericRenderable... genericDeclarations)
   {
      return genericDeclaration(Arrays.asList(genericDeclarations));
   }

   @Contract(value = "_ -> new", pure = true)
   MethodGenericStep genericDeclaration(List<? extends GenericRenderable> genericDeclarations);
}