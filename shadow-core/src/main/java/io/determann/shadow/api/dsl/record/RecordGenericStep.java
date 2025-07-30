package io.determann.shadow.api.dsl.record;

import io.determann.shadow.api.dsl.generic.GenericRenderable;

import java.util.Arrays;
import java.util.List;

public interface RecordGenericStep
      extends RecordImplementsStep
{
   RecordGenericStep generic(String... generics);

   default RecordGenericStep generic(GenericRenderable... generics)
   {
      return generic(Arrays.asList(generics));
   }

   RecordGenericStep generic(List<? extends GenericRenderable> generics);
}
