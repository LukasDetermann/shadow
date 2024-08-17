package io.determann.shadow.api.reflection.shadow.type;

import io.determann.shadow.api.reflection.shadow.modifier.AbstractModifiableReflection;
import io.determann.shadow.api.reflection.shadow.modifier.FinalModifiableReflection;
import io.determann.shadow.api.reflection.shadow.modifier.SealableReflection;
import io.determann.shadow.api.reflection.shadow.modifier.StaticModifiableReflection;
import io.determann.shadow.api.reflection.shadow.structure.PropertyReflection;
import io.determann.shadow.api.shadow.type.Class;
import io.determann.shadow.api.shadow.type.Shadow;

import java.util.List;
import java.util.Optional;

public interface ClassReflection extends Class,
                                         DeclaredReflection,
                                         AbstractModifiableReflection,
                                         StaticModifiableReflection,
                                         SealableReflection,
                                         FinalModifiableReflection
{
   /**
    * reruns the super class of this class. calling {@code getSuperClass())} on {@link Integer} will return {@link Number}.
    * For {@link Object} null will be returned
    */
   ClassReflection getSuperClass();

   List<ClassReflection> getPermittedSubClasses();

   List<PropertyReflection> getProperties();

   /**
    * Equivalent to {@link #isSubtypeOf(Shadow)} except for primitives.
    * if one is a primitive and the other is not it tries to convert them
    */
   boolean isAssignableFrom(Shadow shadow);

   /**
    * returns the outer type for not static classes
    */
   Optional<DeclaredReflection> getOuterType();

   /**
    * {@snippet file = "GenericUsageTest.java" region = "GenericUsage.getGenericTypes"}
    */
   List<ShadowReflection> getGenericTypes();

   /**
    * {@snippet file = "GenericUsageTest.java" region = "GenericUsage.getGenerics"}
    */
   List<GenericReflection> getGenerics();

   /**
    * Integer -&gt; int<br>
    * Long -&gt; long<br>
    * etc...
    */
   PrimitiveReflection asUnboxed();
}
