package io.determann.shadow.api.reflection.shadow.type;

import io.determann.shadow.api.reflection.shadow.modifier.AbstractModifiableReflection;
import io.determann.shadow.api.reflection.shadow.modifier.FinalModifiableReflection;
import io.determann.shadow.api.reflection.shadow.modifier.SealableReflection;
import io.determann.shadow.api.reflection.shadow.modifier.StaticModifiableReflection;
import io.determann.shadow.api.shadow.property.ImmutableProperty;
import io.determann.shadow.api.shadow.property.MutableProperty;
import io.determann.shadow.api.shadow.property.Property;
import io.determann.shadow.api.shadow.type.Class;
import io.determann.shadow.api.shadow.type.*;

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
   Class getSuperClass();

   List<Class> getPermittedSubClasses();

   List<Property> getProperties();

   List<MutableProperty> getMutableProperties();

   List<ImmutableProperty> getImmutableProperties();

   /**
    * Equivalent to {@link #isSubtypeOf(Shadow)} except for primitives.
    * if one is a primitive and the other is not it tries to convert them
    */
   boolean isAssignableFrom(Shadow shadow);

   /**
    * returns the outer type for not static classes
    */
   Optional<Declared> getOuterType();

   /**
    * {@snippet file = "GenericUsageTest.java" region = "GenericUsage.getGenericTypes"}
    */
   List<Shadow> getGenericTypes();

   /**
    * {@snippet file = "GenericUsageTest.java" region = "GenericUsage.getGenerics"}
    */
   List<Generic> getGenerics();

   /**
    * Integer -&gt; int<br>
    * Long -&gt; long<br>
    * etc...
    */
   Primitive asUnboxed();
}
