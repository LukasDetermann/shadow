package io.determann.shadow.api.reflection.shadow.type.primitive;

import io.determann.shadow.api.reflection.shadow.R_Nameable;
import io.determann.shadow.api.reflection.shadow.type.R_Array;
import io.determann.shadow.api.reflection.shadow.type.R_Class;
import io.determann.shadow.api.reflection.shadow.type.R_Type;
import io.determann.shadow.api.shadow.type.C_Type;
import io.determann.shadow.api.shadow.type.primitive.C_Primitive;

/**
 * represents primitive types, but not there wrapper classes. for example int, long, short
 */
public sealed interface R_Primitive
      extends C_Primitive,
              R_Type,
              R_Nameable
      permits R_boolean,
              R_byte,
              R_char,
              R_double,
              R_float,
              R_int,
              R_long,
              R_short
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
