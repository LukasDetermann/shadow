package io.determann.shadow.api.reflection.shadow.type;

import io.determann.shadow.api.reflection.shadow.modifier.R_AbstractModifiable;
import io.determann.shadow.api.reflection.shadow.modifier.R_FinalModifiable;
import io.determann.shadow.api.reflection.shadow.modifier.R_Sealable;
import io.determann.shadow.api.reflection.shadow.modifier.R_StaticModifiable;
import io.determann.shadow.api.reflection.shadow.structure.R_Constructor;
import io.determann.shadow.api.reflection.shadow.structure.R_Property;
import io.determann.shadow.api.reflection.shadow.type.primitive.R_Primitive;
import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.api.shadow.type.C_Type;

import java.util.List;
import java.util.Optional;

public interface R_Class extends C_Class,
                                 R_Declared,
                                 R_AbstractModifiable,
                                 R_StaticModifiable,
                                 R_Sealable,
                                 R_FinalModifiable
{
   /**
    * reruns the super class of this class. calling {@code getSuperClass())} on {@link Integer} will return {@link Number}.
    * For {@link Object} null will be returned
    */
   R_Class getSuperClass();

   List<R_Class> getPermittedSubClasses();

   List<R_Property> getProperties();

   List<R_Constructor> getConstructors();

   /**
    * Equivalent to {@link #isSubtypeOf(C_Type)} except for primitives.
    * if one is a primitive and the other is not it tries to convert them
    */
   boolean isAssignableFrom(C_Type type);

   /**
    * returns the outer type for not static classes
    */
   Optional<R_Declared> getOuterType();

   /**
    * {@snippet file = "GenericUsageTest.java" region = "GenericUsage.getGenericTypes"}
    */
   List<R_Type> getGenericTypes();

   /**
    * {@snippet file = "GenericUsageTest.java" region = "GenericUsage.getGenerics"}
    */
   List<R_Generic> getGenerics();

   /**
    * Integer -&gt; int<br>
    * Long -&gt; long<br>
    * etc...
    */
   R_Primitive asUnboxed();
}
