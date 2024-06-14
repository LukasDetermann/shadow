package io.determann.shadow.api.reflection.query;

import io.determann.shadow.api.shadow.Array;
import io.determann.shadow.api.shadow.Intersection;
import io.determann.shadow.api.shadow.Shadow;

import java.util.List;

public interface ArrayReflection extends Array
{
   /**
    * returns true if this can be cast to that.
    * This can be useful if you want to check if a shadow implements for example a
    * {@link java.util.Collection} {@snippet file= "GenericUsageTest.java" region="GenericUsage.isSubtypeOf"}
    */
   boolean isSubtypeOf(Shadow shadow);

   /**
    * {@snippet :
    *  String[]//@highlight substring="String"
    * }
    */
   Shadow getComponentType();

   /**
    * returns Object[] for declared Arrays and an {@link Intersection} of {@code java.io.Serializable&java.lang.Cloneable}
    * for primitive Arrays
    */
   List<Shadow> getDirectSuperTypes();
}
