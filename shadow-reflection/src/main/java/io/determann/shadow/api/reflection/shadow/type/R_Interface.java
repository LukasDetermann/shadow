package io.determann.shadow.api.reflection.shadow.type;

import io.determann.shadow.api.reflection.shadow.modifier.R_AbstractModifiable;
import io.determann.shadow.api.reflection.shadow.modifier.R_Sealable;
import io.determann.shadow.api.reflection.shadow.modifier.R_StaticModifiable;
import io.determann.shadow.api.shadow.type.C_Interface;

import java.util.List;

public interface R_Interface extends C_Interface,
                                     R_Declared,
                                     R_AbstractModifiable,
                                     R_StaticModifiable,
                                     R_Sealable
{
   boolean isFunctional();

   /**
    * {@snippet file = "GenericUsageTest.java" region = "GenericUsage.getGenericTypes"}
    */
   List<R_Shadow> getGenericTypes();

   /**
    * {@snippet file = "GenericUsageTest.java" region = "GenericUsage.getGenerics"}
    */
   List<R_Generic> getGenerics();
}
