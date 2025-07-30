package io.determann.shadow.api.dsl.constructor;

import io.determann.shadow.api.dsl.generic.GenericRenderable;

import java.util.Arrays;
import java.util.List;

public interface ConstructorGenericStep
      extends ConstructorTypeStep
{
   ConstructorGenericStep generic(String... generic);

   default ConstructorGenericStep generic(GenericRenderable... generic)
   {
      return generic(Arrays.asList(generic));
   }

   ConstructorGenericStep generic(List<? extends GenericRenderable> generic);
}
