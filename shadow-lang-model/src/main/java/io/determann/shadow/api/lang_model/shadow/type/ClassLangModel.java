package io.determann.shadow.api.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.shadow.modifier.AbstractModifiableLangModel;
import io.determann.shadow.api.lang_model.shadow.modifier.FinalModifiableLangModel;
import io.determann.shadow.api.lang_model.shadow.modifier.SealableLangModel;
import io.determann.shadow.api.lang_model.shadow.modifier.StaticModifiableLangModel;
import io.determann.shadow.api.shadow.structure.Property;
import io.determann.shadow.api.shadow.type.Class;
import io.determann.shadow.api.shadow.type.*;

import java.util.List;
import java.util.Optional;

public interface ClassLangModel extends Class,
                                        DeclaredLangModel,
                                        AbstractModifiableLangModel,
                                        StaticModifiableLangModel,
                                        SealableLangModel,
                                        FinalModifiableLangModel
{
   /**
    * reruns the super class of this class. calling {@code getSuperClass())} on {@link Integer} will return {@link Number}.
    * For {@link Object} null will be returned
    */
   Class getSuperClass();

   List<Class> getPermittedSubClasses();

   List<Property> getProperties();

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
