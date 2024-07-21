package io.determann.shadow.api.reflection.shadow.type;

import io.determann.shadow.api.converter.Converter;
import io.determann.shadow.api.reflection.shadow.NameableReflection;
import io.determann.shadow.api.reflection.shadow.QualifiedNameableReflection;
import io.determann.shadow.api.reflection.shadow.modifier.AccessModifiableReflection;
import io.determann.shadow.api.reflection.shadow.modifier.StrictfpModifiableReflection;
import io.determann.shadow.api.reflection.shadow.structure.ModuleEnclosedReflection;
import io.determann.shadow.api.shadow.NestingKind;
import io.determann.shadow.api.shadow.structure.Constructor;
import io.determann.shadow.api.shadow.structure.Field;
import io.determann.shadow.api.shadow.structure.Method;
import io.determann.shadow.api.shadow.structure.Package;
import io.determann.shadow.api.shadow.type.Annotation;
import io.determann.shadow.api.shadow.type.Declared;
import io.determann.shadow.api.shadow.type.Interface;
import io.determann.shadow.api.shadow.type.Shadow;

import java.util.List;
import java.util.Set;

import static io.determann.shadow.api.shadow.Operations.NAMEABLE_GET_NAME;
import static io.determann.shadow.api.shadow.Operations.QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;

/**
 * Anything that can be a file. Can be converted into the following using {@link Converter#convert(DeclaredReflection)}
 * <ul>
 *    <li>{@link Annotation}</li>
 *    <li>{@link Class}</li>
 *    <li>{@link Enum}</li>
 *    <li>{@link Interface}</li>
 *    <li>{@link Record}</li>
 * </ul>
 */
public interface DeclaredReflection extends Declared,
                                            AccessModifiableReflection,
                                            StrictfpModifiableReflection,
                                            ShadowReflection,
                                            NameableReflection,
                                            QualifiedNameableReflection,
                                            ModuleEnclosedReflection
{
   /**
    * returns true if this can be cast to that.
    * This can be useful if you want to check if a shadow implements for example a {@link java.util.Collection}
    * {@snippet file = "GenericUsageTest.java" region = "GenericUsage.isSubtypeOf"}
    */
   boolean isSubtypeOf(Shadow shadow);

   /**
    * is it an outer or inner class? etc.
    */
   NestingKind getNesting();

   default Field getFieldOrThrow(String simpleName)
   {
      return getFields().stream().filter(field -> requestOrThrow(field, NAMEABLE_GET_NAME).equals(simpleName)).findAny().orElseThrow();
   }

   List<Field> getFields();

   default List<Method> getMethods(String simpleName)
   {
      return getMethods().stream().filter(field -> requestOrThrow(field, NAMEABLE_GET_NAME).equals(simpleName)).toList();
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
                            .filter(anInterface -> requestOrThrow(anInterface, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME).equals(qualifiedName))
                            .findAny()
                            .orElseThrow();
   }

   List<Interface> getDirectInterfaces();


   default Interface getDirectInterfaceOrThrow(String qualifiedName)
   {
      return getDirectInterfaces().stream()
                                  .filter(anInterface -> requestOrThrow(anInterface,
                                                                        QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME).equals(qualifiedName))
                                  .findAny()
                                  .orElseThrow();
   }

   Package getPackage();

   /**
    * part of the java language specification:
    * <p>
    * The binary name of a top level type is its canonical name. (qualified name)
    * The binary name of a member type consists of the binary name of its immediately enclosing type, followed by $, followed by the simple name of the member.
    */
   String getBinaryName();
}
