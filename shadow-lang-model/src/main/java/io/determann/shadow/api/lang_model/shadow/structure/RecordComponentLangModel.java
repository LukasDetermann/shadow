package io.determann.shadow.api.lang_model.shadow.structure;

import io.determann.shadow.api.lang_model.shadow.AnnotationableLangModel;
import io.determann.shadow.api.lang_model.shadow.ModuleEnclosedLangModel;
import io.determann.shadow.api.lang_model.shadow.NameableLangModel;
import io.determann.shadow.api.lang_model.shadow.type.RecordLangModel;
import io.determann.shadow.api.lang_model.shadow.type.ShadowLangModel;
import io.determann.shadow.api.shadow.structure.RecordComponent;
import io.determann.shadow.api.shadow.type.Shadow;

public interface RecordComponentLangModel extends RecordComponent,
                                                  AnnotationableLangModel,
                                                  NameableLangModel,
                                                  ModuleEnclosedLangModel
{
   /**
    * returns true if this can be cast to that.
    * This can be useful if you want to check if a shadow implements for example a
    * {@link java.util.Collection} {@snippet file = "GenericUsageTest.java" region = "GenericUsage.isSubtypeOf"}
    */
   boolean isSubtypeOf(Shadow shadow);

   /**
    * Equivalent to {@link #isSubtypeOf(Shadow)} except for primitives.
    * if one is a primitive and the other is not it tries to convert them
    */
   boolean isAssignableFrom(Shadow shadow);

   /**
    * returns the record this is a port of
    */
   RecordLangModel getRecord();

   ShadowLangModel getType();

   MethodLangModel getGetter();

   PackageLangModel getPackage();
}
