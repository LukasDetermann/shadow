package com.derivandi.api.dsl.record;

import com.derivandi.api.dsl.TypeRenderable;
import com.derivandi.api.dsl.generic.GenericRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface RecordGenericStep
      extends RecordImplementsStep
{
   @Contract(value = "_ -> new", pure = true)
   RecordGenericStep genericDeclaration(String... genericDeclarations);

   @Contract(value = "_ -> new", pure = true)
   default RecordGenericStep genericDeclaration(GenericRenderable... genericDeclarations)
   {
      return genericDeclaration(Arrays.asList(genericDeclarations));
   }

   @Contract(value = "_ -> new", pure = true)
   RecordGenericStep genericDeclaration(List<? extends GenericRenderable> genericDeclarations);

   @Contract(value = "_ -> new", pure = true)
   RecordGenericStep genericUsage(String... genericUsages);

   @Contract(value = "_ -> new", pure = true)
   default RecordGenericStep genericUsage(TypeRenderable... genericUsages)
   {
      return genericUsage(Arrays.asList(genericUsages));
   }

   @Contract(value = "_ -> new", pure = true)
   RecordGenericStep genericUsage(List<? extends TypeRenderable> genericUsages);
}
