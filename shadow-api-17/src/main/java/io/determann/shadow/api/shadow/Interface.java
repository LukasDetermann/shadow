package io.determann.shadow.api.shadow;

import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.modifier.AbstractModifiable;
import io.determann.shadow.api.modifier.Sealable;
import io.determann.shadow.api.modifier.StaticModifiable;

import java.util.Arrays;
import java.util.List;

public interface Interface extends Declared,
                                   AbstractModifiable,
                                   StaticModifiable,
                                   Sealable
{
   boolean isFunctional();

   /**
    * {@code shadowApi.getDeclaredOrThrow("java.util.List")} represents {@code List}
    * {@code shadowApi.getDeclaredOrThrow("java.util.List").withGenerics(shadowApi.getDeclaredOrThrow("java.lang.String"))} represents {@code List<String>}
    */
   Interface withGenerics(Shadow... generics);

   /**
    * like {@link #withGenerics(Shadow[])} but resolves the names using {@link ShadowApi#getDeclaredOrThrow(String)}
    */
   default Interface withGenerics(String... qualifiedGenerics)
   {
      return withGenerics(Arrays.stream(qualifiedGenerics)
                                .map(qualifiedName -> getApi().getDeclaredOrThrow(qualifiedName))
                                .toArray(Shadow[]::new));
   }

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
   Interface interpolateGenerics();

   /**
    * Information regarding generics is lost after the compilation. For Example {@code List<String>} becomes {@code List}. This method Does the same.
    * This can be useful if you want to check if a shadow implements for example {@link java.util.Collection}
    * {@code shadowToTest.erasure().isSubtypeOf(shadowApi.getDeclaredOrThrow("java.util.Collection").erasure())}
    * <p>
    * for {@link Interface}s this means for example {@code interface MyInterface<T>{}} -&gt; {@code interface MyInterface{}}
    */
   Interface erasure();

   /**
    * be careful using this equals
    *
    * @see #representsSameType(Shadow)
    */
   @Override
   boolean equals(Object obj);
}
