package io.determann.shadow.api.dsl.method;

import io.determann.shadow.api.dsl.generic.GenericRenderable;

import java.util.Arrays;
import java.util.List;

public interface MethodGenericStep
      extends MethodResultStep
{
   MethodGenericStep generic(String... generic);

   default MethodGenericStep generic(GenericRenderable... generic)
   {
      return generic(Arrays.asList(generic));
   }

   MethodGenericStep generic(List<? extends GenericRenderable> generic);
}