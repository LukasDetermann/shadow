package io.determann.shadow.api.reflection.shadow.type;

import io.determann.shadow.api.shadow.type.C_Array;
import io.determann.shadow.api.shadow.type.C_Intersection;
import io.determann.shadow.api.shadow.type.C_Type;

import java.util.List;

public interface R_Array extends C_Array
{
   /**
    * returns true if this can be cast to that.
    * This can be useful if you want to check if a type implements for example a
    * {@link java.util.Collection} {@snippet file= "GenericUsageTest.java" region="GenericUsage.isSubtypeOf"}
    */
   boolean isSubtypeOf(C_Type type);

   /**
    * {@snippet :
    *  String[]//@highlight substring="String"
    * }
    */
   R_Type getComponentType();

   /**
    * returns Object[] for declared Arrays and an {@link C_Intersection} of {@code java.io.Serializable&java.lang.Cloneable}
    * for primitive Arrays
    */
   List<R_Type> getDirectSuperTypes();

   /**
    * [] -> [][]
    */
   R_Array asArray();
}
