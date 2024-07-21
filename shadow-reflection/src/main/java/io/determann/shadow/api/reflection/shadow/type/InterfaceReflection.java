package io.determann.shadow.api.reflection.shadow.type;

import io.determann.shadow.api.reflection.shadow.modifier.AbstractModifiableReflection;
import io.determann.shadow.api.reflection.shadow.modifier.SealableReflection;
import io.determann.shadow.api.reflection.shadow.modifier.StaticModifiableReflection;
import io.determann.shadow.api.shadow.type.Generic;
import io.determann.shadow.api.shadow.type.Interface;
import io.determann.shadow.api.shadow.type.Shadow;

import java.util.List;

public interface InterfaceReflection extends Interface,
                                             DeclaredReflection,
                                             AbstractModifiableReflection,
                                             StaticModifiableReflection,
                                             SealableReflection
{
   boolean isFunctional();

   /**
    * {@snippet file = "GenericUsageTest.java" region = "GenericUsage.getGenericTypes"}
    */
   List<Shadow> getGenericTypes();

   /**
    * {@snippet file = "GenericUsageTest.java" region = "GenericUsage.getGenerics"}
    */
   List<Generic> getGenerics();
}
