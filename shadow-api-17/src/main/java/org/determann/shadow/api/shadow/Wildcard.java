package org.determann.shadow.api.shadow;

import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.WildcardType;
import java.util.Optional;

/**
 * {@code List<}<b>? extends Number</b>{@code >}
 */
public interface Wildcard extends Shadow<WildcardType>
{
   /**
    * {@code List<}<b>? extends Number</b>{@code >}
    */
   Optional<Shadow<TypeMirror>> getExtends();

   /**
    * {@code List<}<b>? super Number</b>{@code >}
    */
   Optional<Shadow<TypeMirror>> getSuper();

   /**
    * <pre>{@code
    *    shadowApi.convert(shadowApi.getClass("java.lang.Number"))
    *          .asExtendsWildcard()
    *          .contains(shadowApi.getDeclared("java.lang.Long"));
    * }</pre>
    */
   boolean contains(Shadow<? extends TypeMirror> shadow);

   /**
    * be careful using this equals
    *
    * @see #representsSameType(Shadow)
    */
   @Override
   boolean equals(Object obj);
}
