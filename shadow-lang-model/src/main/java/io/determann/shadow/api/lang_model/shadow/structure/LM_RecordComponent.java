package io.determann.shadow.api.lang_model.shadow.structure;

import io.determann.shadow.api.lang_model.shadow.LM_Annotationable;
import io.determann.shadow.api.lang_model.shadow.LM_ModuleEnclosed;
import io.determann.shadow.api.lang_model.shadow.LM_Nameable;
import io.determann.shadow.api.lang_model.shadow.type.LM_Record;
import io.determann.shadow.api.lang_model.shadow.type.LM_Type;
import io.determann.shadow.api.shadow.structure.C_RecordComponent;
import io.determann.shadow.api.shadow.type.C_Type;

public interface LM_RecordComponent

      extends C_RecordComponent,
              LM_Annotationable,
              LM_Nameable,
              LM_ModuleEnclosed
{
   /**
    * returns true if this can be cast to that.
    * This can be useful if you want to check if a type implements for example a
    * {@link java.util.Collection} {@snippet file = "GenericUsageTest.java" region = "GenericUsage.isSubtypeOf"}
    */
   boolean isSubtypeOf(C_Type type);

   /**
    * Equivalent to {@link #isSubtypeOf(C_Type)} except for primitives.
    * if one is a primitive and the other is not it tries to convert them
    */
   boolean isAssignableFrom(C_Type type);

   /**
    * returns the record this is a port of
    */
   LM_Record getRecord();

   LM_Type getType();

   LM_Method getGetter();
}
