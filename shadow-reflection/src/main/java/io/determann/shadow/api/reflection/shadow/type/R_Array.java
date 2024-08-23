package io.determann.shadow.api.reflection.shadow.type;

import io.determann.shadow.api.shadow.type.C_Array;
import io.determann.shadow.api.shadow.type.C_Intersection;
import io.determann.shadow.api.shadow.type.C_Shadow;

import java.util.List;

public interface R_Array extends C_Array
{
   /**
    * returns true if this can be cast to that.
    * This can be useful if you want to check if a shadow implements for example a
    * {@link java.util.Collection} {@snippet file= "GenericUsageTest.java" region="GenericUsage.isSubtypeOf"}
    */
   boolean isSubtypeOf(C_Shadow shadow);

   /**
    * {@snippet :
    *  String[]//@highlight substring="String"
    * }
    */
   R_Shadow getComponentType();

   /**
    * returns Object[] for declared Arrays and an {@link C_Intersection} of {@code java.io.Serializable&java.lang.Cloneable}
    * for primitive Arrays
    */
   List<R_Shadow> getDirectSuperTypes();

   /**
    * [] -> [][]
    */
   R_Array asArray();
}
