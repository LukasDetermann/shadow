package org.determann.shadow.api.shadow;

import org.determann.shadow.api.ShadowApi;
import org.determann.shadow.api.modifier.AbstractModifiable;
import org.determann.shadow.api.modifier.FinalModifiable;
import org.determann.shadow.api.modifier.Sealable;
import org.determann.shadow.api.modifier.StaticModifiable;
import org.determann.shadow.api.wrapper.Property;
import org.jetbrains.annotations.UnmodifiableView;

import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Optional;

public interface Class extends Declared,
                               AbstractModifiable,
                               StaticModifiable,
                               Sealable,
                               FinalModifiable
{
   Class getSuperClass();

   @UnmodifiableView List<Class> getPermittedSubClasses();

   @UnmodifiableView List<Property> getProperties();

   /**
    * Equivalent to {@link #isSubtypeOf(Shadow)} except for primitives.
    * if one is a primitive and the other is not it tries to convert them
    */
   boolean isAssignableFrom(Shadow<? extends TypeMirror> shadow);

   /**
    * returns the outer type for not static classes
    */
   Optional<Shadow<TypeMirror>> getOuterType();

   /**
    * {@code shadowApi.getDeclaredOrThrow("java.util.List")} represents {@code List}
    * {@code shadowApi.getDeclaredOrThrow("java.util.List").withGenerics(shadowApi.getDeclaredOrThrow("java.lang.String"))} represents {@code List<String>}
    */
   @SuppressWarnings("unchecked")
   Class withGenerics(Shadow<? extends TypeMirror>... generics);

   /**
    * like {@link #withGenerics(Shadow[])} but resolves the names using {@link ShadowApi#getDeclaredOrThrow(String)}
    */
   Class withGenerics(String... qualifiedGenerics);

   /**
    * {@code List<}<b>String</b>{@code >}
    */
   @UnmodifiableView List<Shadow<TypeMirror>> getGenerics();

   /**
    * {@code List<}<b>T</b>{@code >}
    */
   @UnmodifiableView List<Generic> getFormalGenerics();

   /**
    * Used when constructing types to compare to at compile time that contain multiple, on each other depended, generics.
    * <p>
    * it answers the question: given {@code public class MyClass<A extends Comparable<B>, B extends Comparable<A>> {}} and A being {@code String}
    * what can B be by returning the "simplest" possible answer. in this case String
    * <p>
    * The code for the example
    * <pre>{@code
    *       Class declared = shadowApi.getClassOrThrow("org.determann.shadow.example.processed.MyClass")
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
    * }<pre/>
    * Note the use of the unboundWildcardConstant witch gets replaced by calling {@code capture()} with the result
    */
   Class interpolateGenerics();

   /**
    * be careful using this equals
    *
    * @see #representsSameType(Shadow)
    */
   @Override
   boolean equals(Object obj);
}
