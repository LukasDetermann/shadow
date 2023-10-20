package io.determann.shadow.api.shadow;

import io.determann.shadow.api.modifier.AbstractModifiable;
import io.determann.shadow.api.modifier.FinalModifiable;
import io.determann.shadow.api.modifier.StaticModifiable;
import io.determann.shadow.api.property.ImmutableProperty;
import io.determann.shadow.api.property.MutableProperty;
import io.determann.shadow.api.property.Property;

import java.util.List;
import java.util.Optional;

public interface Class extends Declared,
                               AbstractModifiable,
                               StaticModifiable,
                               FinalModifiable
{
   /**
    * reruns the super class of this class. calling {@code getSuperClass())} on {@link Integer} will return {@link Number}.
    * For {@link Object} null will be returned
    */
   Class getSuperClass();

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
    * {@code List<}<b>String</b>{@code >}
    */
   List<Shadow> getGenerics();

   /**
    * {@code List<}<b>T</b>{@code >}
    */
   List<Generic> getFormalGenerics();

   /**
    * Used when constructing types to compare to at compile time that contain multiple, on each other depended, generics.
    * <p>
    * it answers the question: given {@code public class MyClass<A extends Comparable<B>, B extends Comparable<A>> {}} and A being {@code String}
    * what can B be by returning the "simplest" possible answer. in this case String
    * <p>
    * The code for the example
    * <pre>{@code
    *       Class declared = shadowApi.getClassOrThrow("io.determann.shadow.example.processed.MyClass")
    *                                 .withGenerics(shadowApi.getDeclaredOrThrow("java.lang.String"),
    *                                                  shadowApi.getConstants().getUnboundWildcard());
    *       Class capture = declared.interpolateGenerics();
    *
    *       Shadow<TypeMirror> stringRep = convert(capture.getGenerics().get(1))
    *                                                      .toGeneric()
    *                                                      .map(Generic::getExtends)
    *                                                      .map(shadowApi::convert)
    *                                                      .flatMap(ShadowConverter::toClassOrThrow)
    *                                                      .map(Class::getGenerics)
    *                                                      .map(shadows -> shadows.get(0))
    *                                                      .orElseThrow();
    *
    *       System.out.println(stringRep.representsSameType(shadowApi.getDeclaredOrThrow("java.lang.String")));
    * }</pre>
    * Note the use of the unboundWildcardConstant witch gets replaced by calling {@code capture()} with the result
    */
   Class interpolateGenerics();

   /**
    * Integer -&gt; int<br>
    * Long -&gt; long
    * etc...
    */
   Primitive asUnboxed();

   /**
    * be careful using this equals
    *
    * @see #representsSameType(Shadow)
    */
   @Override
   boolean equals(Object obj);
}
