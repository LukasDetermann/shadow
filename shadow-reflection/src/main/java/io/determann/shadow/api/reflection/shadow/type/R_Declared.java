package io.determann.shadow.api.reflection.shadow.type;

import io.determann.shadow.api.reflection.shadow.R_Annotationable;
import io.determann.shadow.api.reflection.shadow.R_Nameable;
import io.determann.shadow.api.reflection.shadow.R_QualifiedNameable;
import io.determann.shadow.api.reflection.shadow.modifier.R_AccessModifiable;
import io.determann.shadow.api.reflection.shadow.modifier.R_StrictfpModifiable;
import io.determann.shadow.api.reflection.shadow.structure.R_Field;
import io.determann.shadow.api.reflection.shadow.structure.R_Method;
import io.determann.shadow.api.reflection.shadow.structure.R_ModuleEnclosed;
import io.determann.shadow.api.reflection.shadow.structure.R_Package;
import io.determann.shadow.api.shadow.C_NestingKind;
import io.determann.shadow.api.shadow.type.C_Annotation;
import io.determann.shadow.api.shadow.type.C_Declared;
import io.determann.shadow.api.shadow.type.C_Interface;
import io.determann.shadow.api.shadow.type.C_Type;

import java.util.List;
import java.util.Set;

import static io.determann.shadow.api.Operations.NAMEABLE_GET_NAME;
import static io.determann.shadow.api.Operations.QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME;
import static io.determann.shadow.api.Provider.requestOrThrow;

/**
 * Anything that can be a file.
 * <ul>
 *    <li>{@link C_Annotation}</li>
 *    <li>{@link Class}</li>
 *    <li>{@link Enum}</li>
 *    <li>{@link C_Interface}</li>
 *    <li>{@link Record}</li>
 * </ul>
 */
public interface R_Declared extends C_Declared,
                                    R_Annotationable,
                                    R_AccessModifiable,
                                    R_StrictfpModifiable,
                                    R_Type,
                                    R_Nameable,
                                    R_QualifiedNameable,
                                    R_ModuleEnclosed
{
   /**
    * returns true if this can be cast to that.
    * This can be useful if you want to check if a type implements for example a {@link java.util.Collection}
    * {@snippet file = "GenericUsageTest.java" region = "GenericUsage.isSubtypeOf"}
    */
   boolean isSubtypeOf(C_Type type);

   /**
    * is it an outer or inner class? etc.
    */
   C_NestingKind getNesting();

   default R_Field getFieldOrThrow(String simpleName)
   {
      return getFields().stream().filter(field -> requestOrThrow(field, NAMEABLE_GET_NAME).equals(simpleName)).findAny().orElseThrow();
   }

   List<R_Field> getFields();

   default List<R_Method> getMethods(String simpleName)
   {
      return getMethods().stream().filter(field -> requestOrThrow(field, NAMEABLE_GET_NAME).equals(simpleName)).toList();
   }

   List<R_Method> getMethods();

   /**
    * returns the parentClass including interfaces
    */
   List<R_Declared> getDirectSuperTypes();

   /**
    * returns all distinct supertypes including interfaces
    */
   Set<R_Declared> getSuperTypes();

   List<R_Interface> getInterfaces();

   default R_Interface getInterfaceOrThrow(String qualifiedName)
   {
      return getInterfaces().stream()
                            .filter(anInterface -> requestOrThrow(anInterface, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME).equals(qualifiedName))
                            .findAny()
                            .orElseThrow();
   }

   List<R_Interface> getDirectInterfaces();


   default R_Interface getDirectInterfaceOrThrow(String qualifiedName)
   {
      return getDirectInterfaces().stream()
                                  .filter(anInterface -> requestOrThrow(anInterface,
                                                                        QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME).equals(qualifiedName))
                                  .findAny()
                                  .orElseThrow();
   }

   R_Package getPackage();

   /**
    * part of the java language specification:
    * <p>
    * The binary name of a top level type is its canonical name. (qualified name)
    * The binary name of a member type consists of the binary name of its immediately enclosing type, followed by $, followed by the simple name of the member.
    */
   String getBinaryName();

   R_Array asArray();
}
