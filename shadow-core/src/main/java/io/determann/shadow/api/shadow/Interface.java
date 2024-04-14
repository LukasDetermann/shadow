package io.determann.shadow.api.shadow;

import io.determann.shadow.api.modifier.AbstractModifiable;
import io.determann.shadow.api.modifier.Sealable;
import io.determann.shadow.api.modifier.StaticModifiable;

import java.util.List;

public interface Interface extends Declared,
                                   AbstractModifiable,
                                   StaticModifiable,
                                   Sealable
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
