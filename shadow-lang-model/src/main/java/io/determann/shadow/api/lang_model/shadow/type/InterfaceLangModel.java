package io.determann.shadow.api.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.shadow.modifier.AbstractModifiableLangModel;
import io.determann.shadow.api.lang_model.shadow.modifier.SealableLangModel;
import io.determann.shadow.api.lang_model.shadow.modifier.StaticModifiableLangModel;
import io.determann.shadow.api.shadow.type.Generic;
import io.determann.shadow.api.shadow.type.Interface;
import io.determann.shadow.api.shadow.type.Shadow;

import java.util.List;

public interface InterfaceLangModel extends Interface,
                                            DeclaredLangModel,
                                            AbstractModifiableLangModel,
                                            StaticModifiableLangModel,
                                            SealableLangModel
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
