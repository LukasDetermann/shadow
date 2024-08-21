package io.determann.shadow.api.reflection.shadow.type;

import io.determann.shadow.api.reflection.shadow.AnnotationableReflection;
import io.determann.shadow.api.reflection.shadow.NameableReflection;
import io.determann.shadow.api.reflection.shadow.QualifiedNameableReflection;
import io.determann.shadow.api.reflection.shadow.modifier.AccessModifiableReflection;
import io.determann.shadow.api.reflection.shadow.modifier.StrictfpModifiableReflection;
import io.determann.shadow.api.reflection.shadow.structure.*;
import io.determann.shadow.api.shadow.NestingKind;
import io.determann.shadow.api.shadow.type.Annotation;
import io.determann.shadow.api.shadow.type.Declared;
import io.determann.shadow.api.shadow.type.Interface;
import io.determann.shadow.api.shadow.type.Shadow;

import java.util.List;
import java.util.Set;

import static io.determann.shadow.api.Operations.NAMEABLE_GET_NAME;
import static io.determann.shadow.api.Operations.QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME;
import static io.determann.shadow.api.Provider.requestOrThrow;

/**
 * Anything that can be a file.
 * <ul>
 *    <li>{@link Annotation}</li>
 *    <li>{@link Class}</li>
 *    <li>{@link Enum}</li>
 *    <li>{@link Interface}</li>
 *    <li>{@link Record}</li>
 * </ul>
 */
public interface DeclaredReflection extends Declared,
                                            AnnotationableReflection,
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

   default FieldReflection getFieldOrThrow(String simpleName)
   {
      return getFields().stream().filter(field -> requestOrThrow(field, NAMEABLE_GET_NAME).equals(simpleName)).findAny().orElseThrow();
   }

   List<FieldReflection> getFields();

   default List<MethodReflection> getMethods(String simpleName)
   {
      return getMethods().stream().filter(field -> requestOrThrow(field, NAMEABLE_GET_NAME).equals(simpleName)).toList();
   }

   List<MethodReflection> getMethods();

   List<ConstructorReflection> getConstructors();

   /**
    * returns the parentClass including interfaces
    */
   List<DeclaredReflection> getDirectSuperTypes();

   /**
    * returns all distinct supertypes including interfaces
    */
   Set<DeclaredReflection> getSuperTypes();

   List<InterfaceReflection> getInterfaces();

   default InterfaceReflection getInterfaceOrThrow(String qualifiedName)
   {
      return getInterfaces().stream()
                            .filter(anInterface -> requestOrThrow(anInterface, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME).equals(qualifiedName))
                            .findAny()
                            .orElseThrow();
   }

   List<InterfaceReflection> getDirectInterfaces();


   default InterfaceReflection getDirectInterfaceOrThrow(String qualifiedName)
   {
      return getDirectInterfaces().stream()
                                  .filter(anInterface -> requestOrThrow(anInterface,
                                                                        QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME).equals(qualifiedName))
                                  .findAny()
                                  .orElseThrow();
   }

   PackageReflection getPackage();

   /**
    * part of the java language specification:
    * <p>
    * The binary name of a top level type is its canonical name. (qualified name)
    * The binary name of a member type consists of the binary name of its immediately enclosing type, followed by $, followed by the simple name of the member.
    */
   String getBinaryName();
}
