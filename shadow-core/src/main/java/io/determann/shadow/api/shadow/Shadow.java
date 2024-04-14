package io.determann.shadow.api.shadow;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.converter.Converter;

/**
 * Represents sourceCode that is being complied. A {@link Shadow} can be converted only in one of its children using
 * {@link Converter#convert(Shadow)}. Every {@link Shadow} has a {@link TypeKind} indicating its Type.
 * {@link TypeKind#ARRAY} -&gt; {@link Array}. To check for equality use {@link #representsSameType(Shadow)}.
 * <br><br>
 * <b>subtypes</b>
 * <ul>
 *     <li>{@link Declared} anything that can be a file
 *       <ul>
 *          <li>{@link Annotation}</li>
 *          <li>{@link Class}</li>
 *          <li>{@link Enum}</li>
 *          <li>{@link Interface}</li>
 *          <li>{@link Record}</li>
 *       </ul>
 *     </li>
 *     <li>{@link Array}</li>
 *     <li>{@link Intersection} {@code T extends} <b> Collection &amp; Serializable</b>{@code >}</li>
 *     <li>{@link Void}</li>
 *     <li>{@link Module}</li>
 *     <li>{@link Package}</li>
 *     <li>{@link RecordComponent}</li>
 *     <li>{@link Null}</li>
 *     <li>{@link Primitive}</li>
 *     <li>{@link Generic} Generics</li>
 *     <li>{@link Wildcard} {@code List<}<b>? extends Number</b>{@code >}</li>
 *     <li>{@link Variable}
 *       <ul>
 *          <li>{@link EnumConstant}</li>
 *          <li>{@link Field}</li>
 *          <li>{@link Parameter} of a Method or Constructor</li>
 *       </ul>>
 *     </li>
 * </ul>
 */
public interface Shadow
{
   TypeKind getKind();

   default boolean isKind(TypeKind typeKind)
   {
      return getKind().equals(typeKind);
   }

   /**
    * type equals from the compiler perspective. for example ? does not equal ? for the compiler
    */
   boolean representsSameType(Shadow shadow);
}
