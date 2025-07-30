package io.determann.shadow.api.dsl.interface_;

import io.determann.shadow.api.dsl.generic.GenericRenderable;

import java.util.Arrays;
import java.util.List;

public interface InterfaceGenericStep
      extends InterfaceExtendsStep
{
   InterfaceGenericStep generic(String... generics);

   default InterfaceGenericStep generic(GenericRenderable... generics)
   {
      return generic(Arrays.asList(generics));
   }

   InterfaceGenericStep generic(List<? extends GenericRenderable> generics);
}
