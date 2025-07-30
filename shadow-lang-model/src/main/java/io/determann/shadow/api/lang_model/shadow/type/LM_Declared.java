package io.determann.shadow.api.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.shadow.*;
import io.determann.shadow.api.lang_model.shadow.modifier.LM_AccessModifiable;
import io.determann.shadow.api.lang_model.shadow.modifier.LM_StrictfpModifiable;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Field;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Method;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Package;
import io.determann.shadow.api.shadow.C_NestingKind;
import io.determann.shadow.api.shadow.type.C_Declared;
import io.determann.shadow.api.shadow.type.C_Type;

import java.util.List;
import java.util.Set;

import static io.determann.shadow.api.Operations.NAMEABLE_GET_NAME;
import static io.determann.shadow.api.Operations.QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME;
import static io.determann.shadow.api.Provider.requestOrThrow;

/**
 * Anything that can be a file.
 */
public sealed interface LM_Declared

      extends C_Declared,
              LM_Annotationable,
              LM_AccessModifiable,
              LM_StrictfpModifiable,
              LM_ReferenceType,
              LM_Nameable,
              LM_QualifiedNameable,
              LM_ModuleEnclosed,
              LM_Documented

      permits LM_Annotation,
              LM_Class,
              LM_Enum,
              LM_Interface,
              LM_Record
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

   default LM_Field getFieldOrThrow(String simpleName)
   {
      return getFields().stream().filter(field -> requestOrThrow(field, NAMEABLE_GET_NAME).equals(simpleName)).findAny().orElseThrow();
   }

   List<LM_Field> getFields();

   default List<LM_Method> getMethods(String simpleName)
   {
      return getMethods().stream().filter(field -> requestOrThrow(field, NAMEABLE_GET_NAME).equals(simpleName)).toList();
   }

   List<LM_Method> getMethods();

   /**
    * returns the parentClass including interfaces
    */
   List<LM_Declared> getDirectSuperTypes();

   /**
    * returns all distinct supertypes including interfaces
    */
   Set<LM_Declared> getSuperTypes();

   List<LM_Interface> getInterfaces();

   default LM_Interface getInterfaceOrThrow(String qualifiedName)
   {
      return getInterfaces().stream()
                            .filter(anInterface -> requestOrThrow(anInterface, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME).equals(qualifiedName))
                            .findAny()
                            .orElseThrow();
   }

   List<LM_Interface> getDirectInterfaces();


   default LM_Interface getDirectInterfaceOrThrow(String qualifiedName)
   {
      return getDirectInterfaces().stream()
                                  .filter(anInterface -> requestOrThrow(anInterface,
                                                                        QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME).equals(qualifiedName))
                                  .findAny()
                                  .orElseThrow();
   }

   LM_Package getPackage();

   /**
    * part of the java language specification:
    * <p>
    * The binary name of a top level type is its canonical name. (qualified name)
    * The binary name of a member type consists of the binary name of its immediately enclosing type, followed by $, followed by the simple name of the member.
    */
   String getBinaryName();

   /**
    * String -&gt; String[]
    */
   LM_Array asArray();

   LM_Wildcard asExtendsWildcard();

   LM_Wildcard asSuperWildcard();
}
