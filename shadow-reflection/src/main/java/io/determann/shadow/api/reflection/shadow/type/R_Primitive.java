package io.determann.shadow.api.reflection.shadow.type;

import io.determann.shadow.api.reflection.shadow.R_Nameable;
import io.determann.shadow.api.shadow.type.C_Primitive;
import io.determann.shadow.api.shadow.type.C_Shadow;

/**
 * represents primitive types, but not there wrapper classes. for example int, long, short
 */
public interface R_Primitive extends C_Primitive,
                                     R_Nameable
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
    * int -&gt; Integer<br>
    * long -&gt; Long<br>
    * etc...
    */
   R_Class asBoxed();

   /**
    * int -> int[]
    */
   R_Array asArray();
}
