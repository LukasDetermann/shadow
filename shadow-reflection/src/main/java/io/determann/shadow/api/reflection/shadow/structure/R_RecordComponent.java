package io.determann.shadow.api.reflection.shadow.structure;

import io.determann.shadow.api.reflection.shadow.R_Annotationable;
import io.determann.shadow.api.reflection.shadow.R_Nameable;
import io.determann.shadow.api.reflection.shadow.type.R_Record;
import io.determann.shadow.api.reflection.shadow.type.R_Shadow;
import io.determann.shadow.api.shadow.structure.C_RecordComponent;
import io.determann.shadow.api.shadow.type.C_Shadow;

public interface R_RecordComponent extends C_RecordComponent,
                                           R_Annotationable,
                                           R_Nameable,
                                           R_ModuleEnclosed
{
   /**
    * returns true if this can be cast to that.
    * This can be useful if you want to check if a shadow implements for example a
    * {@link java.util.Collection} {@snippet file = "GenericUsageTest.java" region = "GenericUsage.isSubtypeOf"}
    */
   boolean isSubtypeOf(C_Shadow shadow);

   /**
    * Equivalent to {@link #isSubtypeOf(C_Shadow)} except for primitives.
    * if one is a primitive and the other is not it tries to convert them
    */
   boolean isAssignableFrom(C_Shadow shadow);

   /**
    * returns the record this is a port of
    */
   R_Record getRecord();

   R_Shadow getType();

   R_Method getGetter();

   R_Package getPackage();
}
