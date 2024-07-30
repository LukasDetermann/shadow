package io.determann.shadow.api.reflection.shadow.structure;

import io.determann.shadow.api.reflection.shadow.NameableReflection;
import io.determann.shadow.api.shadow.structure.Method;
import io.determann.shadow.api.shadow.structure.Package;
import io.determann.shadow.api.shadow.structure.RecordComponent;
import io.determann.shadow.api.shadow.type.Record;
import io.determann.shadow.api.shadow.type.Shadow;

public interface RecordComponentReflection extends RecordComponent,
                                                   NameableReflection,
                                                   ModuleEnclosedReflection
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
   Record getRecord();

   Shadow getType();

   Method getGetter();

   Package getPackage();
}
