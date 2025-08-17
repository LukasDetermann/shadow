package io.determann.shadow.api.dsl.record;

import io.determann.shadow.api.dsl.generic.GenericRenderable;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;

public interface RecordGenericStep
      extends RecordImplementsStep
{
   @Contract(value = "_ -> new", pure = true)
   RecordGenericStep generic(String... generics);

   @Contract(value = "_ -> new", pure = true)
   default RecordGenericStep generic(GenericRenderable... generics)
   {
      return generic(Arrays.asList(generics));
   }

   @Contract(value = "_ -> new", pure = true)
   RecordGenericStep generic(List<? extends GenericRenderable> generics);
}
