package io.determann.shadow.api.reflection.query;

import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Primitive;
import io.determann.shadow.api.shadow.Shadow;

/**
 * represents primitive types, but not there wrapper classes. for example int, long, short
 */
public interface PrimitiveReflection extends Primitive
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
    * int -&gt; Integer<br>
    * long -&gt; Long<br>
    * etc...
    */
   Class asBoxed();
}
