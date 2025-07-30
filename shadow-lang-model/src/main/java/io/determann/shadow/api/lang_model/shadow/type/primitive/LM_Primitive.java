package io.determann.shadow.api.lang_model.shadow.type.primitive;

import io.determann.shadow.api.lang_model.shadow.LM_Nameable;
import io.determann.shadow.api.lang_model.shadow.type.LM_Array;
import io.determann.shadow.api.lang_model.shadow.type.LM_Class;
import io.determann.shadow.api.lang_model.shadow.type.LM_VariableType;
import io.determann.shadow.api.shadow.type.C_Type;
import io.determann.shadow.api.shadow.type.primitive.C_Primitive;

/**
 * represents primitive types, but not there wrapper classes. for example int, long, short
 */
public sealed interface LM_Primitive
      extends C_Primitive,
              LM_Nameable,
              LM_VariableType
      permits LM_boolean,
              LM_byte,
              LM_char,
              LM_double,
              LM_float,
              LM_int,
              LM_long,
              LM_short
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
   LM_Class asBoxed();

   /**
    * int -&gt; int[]
    */
   LM_Array asArray();
}
