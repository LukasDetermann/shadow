package io.determann.shadow.api.dsl.class_;

import io.determann.shadow.api.dsl.generic.GenericRenderable;

import java.util.Arrays;
import java.util.List;

public interface ClassGenericStep extends ClassExtendsStep
{
   ClassGenericStep generic(String... generics);

   default ClassGenericStep generic(GenericRenderable... generics)
   {
      return generic(Arrays.asList(generics));
   }

   ClassGenericStep generic(List<? extends GenericRenderable> generics);
}
