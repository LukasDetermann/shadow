package io.determann.shadow.api.shadow;

import io.determann.shadow.api.modifier.AbstractModifiable;
import io.determann.shadow.api.modifier.FinalModifiable;
import io.determann.shadow.api.modifier.Sealable;
import io.determann.shadow.api.modifier.StaticModifiable;
import io.determann.shadow.api.property.ImmutableProperty;
import io.determann.shadow.api.property.MutableProperty;
import io.determann.shadow.api.property.Property;

import java.util.List;
import java.util.Optional;

public interface Class extends Declared,
                               AbstractModifiable,
                               StaticModifiable,
                               Sealable,
                               FinalModifiable
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
    * {@snippet file="GenericUsageTest.java" region="GenericUsage.getGenericTypes"}
    */
   List<Shadow> getGenericTypes();

   /**
    * {@snippet file="GenericUsageTest.java" region="GenericUsage.getGenerics"}
    */
   List<Generic> getGenerics();

   /**
    * Integer -&gt; int<br>
    * Long -&gt; long<br>
    * etc...
    */
   Primitive asUnboxed();
}
