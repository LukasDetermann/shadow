package io.determann.shadow.api.shadow;

import io.determann.shadow.api.modifier.FinalModifiable;
import io.determann.shadow.api.modifier.StaticModifiable;

import java.util.List;

public interface Record extends Declared,
                                StaticModifiable,
                                FinalModifiable
{
   default RecordComponent getRecordComponentOrThrow(String simpleName)
   {
      return getRecordComponents().stream().filter(field -> field.getSimpleName().equals(simpleName)).findAny().orElseThrow();
   }

   List<RecordComponent> getRecordComponents();

   /**
    * {@code MyRecord<}<b>String</b>{@code >}
    */
   List<Shadow> getGenerics();

   /**
    * {@code MyRecord<}<b>T</b>{@code >}
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
   Record interpolateGenerics();

   /**
    * be careful using this equals
    *
    * @see #representsSameType(Shadow)
    */
   @Override
   boolean equals(Object obj);
}
