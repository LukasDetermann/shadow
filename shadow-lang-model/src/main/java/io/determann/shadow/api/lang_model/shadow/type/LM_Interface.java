package io.determann.shadow.api.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.shadow.modifier.LM_AbstractModifiable;
import io.determann.shadow.api.lang_model.shadow.modifier.LM_Sealable;
import io.determann.shadow.api.lang_model.shadow.modifier.LM_StaticModifiable;
import io.determann.shadow.api.shadow.type.C_Interface;

import java.util.List;

public interface LM_Interface extends C_Interface,
                                      LM_Declared,
                                      LM_AbstractModifiable,
                                      LM_StaticModifiable,
                                      LM_Sealable
{
   boolean isFunctional();

   /**
    * {@snippet file = "GenericUsageTest.java" region = "GenericUsage.getGenericTypes"}
    */
   List<LM_Type> getGenericTypes();

   /**
    * {@snippet file = "GenericUsageTest.java" region = "GenericUsage.getGenerics"}
    */
   List<LM_Generic> getGenerics();
}
