package io.determann.shadow.api.shadow;

import io.determann.shadow.api.*;
import io.determann.shadow.api.converter.Converter;
import io.determann.shadow.api.modifier.AccessModifiable;
import io.determann.shadow.api.modifier.StrictfpModifiable;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

/**
 * Anything that can be a file. Can be converted into the following using {@link Converter#convert(Declared)}
 * <ul>
 *    <li>{@link Annotation}</li>
 *    <li>{@link Class}</li>
 *    <li>{@link Enum}</li>
 *    <li>{@link Interface}</li>
 * </ul>
 */
public interface Declared extends Shadow,
                                  Annotationable,
                                  AccessModifiable,
                                  StrictfpModifiable,
                                  Nameable,
                                  QualifiedNameable,
                                  Documented
{
   /**
    * returns true if this can be cast to that.
    * This can be useful if you want to check if a shadow implements for example a {@link java.util.Collection}
    * {@code shadowToTest.erasure().isSubtypeOf(shadowApi.getDeclaredOrThrow("java.util.Collection").erasure())}
    */
   boolean isSubtypeOf(Shadow shadow);

   /**
    * is it an outer or inner class? etc.
    */
   NestingKind getNesting();

   default Field getFieldOrThrow(String simpleName)
   {
      return getFields().stream().filter(field -> field.getName().equals(simpleName)).findAny().orElseThrow(NoSuchElementException::new);
   }

   List<Field> getFields();

   default List<Method> getMethods(String simpleName)
   {
      return getMethods().stream()
                         .filter(field -> field.getName().equals(simpleName))
                         .collect(collectingAndThen(toList(), Collections::unmodifiableList));
   }

   List<Method> getMethods();

   List<Constructor> getConstructors();

   /**
    * returns the parentClass including interfaces
    */
   List<Declared> getDirectSuperTypes();

   /**
    * returns all distinct supertypes including interfaces
    */
   Set<Declared> getSuperTypes();

   List<Interface> getInterfaces();

   default Interface getInterfaceOrThrow(String qualifiedName)
   {
      return getInterfaces().stream()
                            .filter(anInterface -> anInterface.getQualifiedName().equals(qualifiedName))
                            .findAny()
                            .orElseThrow(NoSuchElementException::new);
   }

   List<Interface> getDirectInterfaces();


   default Interface getDirectInterfaceOrThrow(String qualifiedName)
   {
      return getDirectInterfaces().stream()
                                  .filter(anInterface -> anInterface.getQualifiedName().equals(qualifiedName))
                                  .findAny()
                                  .orElseThrow(NoSuchElementException::new);
   }

   Package getPackage();

   /**
    * part of the java language specification:
    * <p>
    * The binary name of a top level type is its canonical name. (qualified name)
    * The binary name of a member type consists of the binary name of its immediately enclosing type, followed by $, followed by the simple name of the member.
    */
   String getBinaryName();

   Wildcard asExtendsWildcard();

   Wildcard asSuperWildcard();

   /**
    * String -&gt; String[]
    */
   Array asArray();
}
