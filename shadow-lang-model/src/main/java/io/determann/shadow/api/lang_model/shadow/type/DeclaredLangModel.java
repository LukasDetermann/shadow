package io.determann.shadow.api.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.shadow.*;
import io.determann.shadow.api.lang_model.shadow.modifier.AccessModifiableLangModel;
import io.determann.shadow.api.lang_model.shadow.modifier.StrictfpModifiableLangModel;
import io.determann.shadow.api.lang_model.shadow.structure.ConstructorLangModel;
import io.determann.shadow.api.lang_model.shadow.structure.FieldLangModel;
import io.determann.shadow.api.lang_model.shadow.structure.MethodLangModel;
import io.determann.shadow.api.lang_model.shadow.structure.PackageLangModel;
import io.determann.shadow.api.shadow.NestingKind;
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
 * Anything that can be a file.
 * <ul>
 *    <li>{@link Annotation}</li>
 *    <li>{@link Class}</li>
 *    <li>{@link Enum}</li>
 *    <li>{@link Interface}</li>
 *    <li>{@link Record}</li>
 * </ul>
 */
public interface DeclaredLangModel extends Declared,
                                           AnnotationableLangModel,
                                           AccessModifiableLangModel,
                                           StrictfpModifiableLangModel,
                                           ShadowLangModel,
                                           NameableLangModel,
                                           QualifiedNameableLamgModel,
                                           ModuleEnclosedLangModel,
                                           DocumentedLangModel
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

   default FieldLangModel getFieldOrThrow(String simpleName)
   {
      return getFields().stream().filter(field -> requestOrThrow(field, NAMEABLE_GET_NAME).equals(simpleName)).findAny().orElseThrow();
   }

   List<FieldLangModel> getFields();

   default List<MethodLangModel> getMethods(String simpleName)
   {
      return getMethods().stream().filter(field -> requestOrThrow(field, NAMEABLE_GET_NAME).equals(simpleName)).toList();
   }

   List<MethodLangModel> getMethods();

   List<ConstructorLangModel> getConstructors();

   /**
    * returns the parentClass including interfaces
    */
   List<DeclaredLangModel> getDirectSuperTypes();

   /**
    * returns all distinct supertypes including interfaces
    */
   Set<DeclaredLangModel> getSuperTypes();

   List<InterfaceLangModel> getInterfaces();

   default InterfaceLangModel getInterfaceOrThrow(String qualifiedName)
   {
      return getInterfaces().stream()
                            .filter(anInterface -> requestOrThrow(anInterface, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME).equals(qualifiedName))
                            .findAny()
                            .orElseThrow();
   }

   List<InterfaceLangModel> getDirectInterfaces();


   default InterfaceLangModel getDirectInterfaceOrThrow(String qualifiedName)
   {
      return getDirectInterfaces().stream()
                                  .filter(anInterface -> requestOrThrow(anInterface,
                                                                        QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME).equals(qualifiedName))
                                  .findAny()
                                  .orElseThrow();
   }

   PackageLangModel getPackage();

   /**
    * part of the java language specification:
    * <p>
    * The binary name of a top level type is its canonical name. (qualified name)
    * The binary name of a member type consists of the binary name of its immediately enclosing type, followed by $, followed by the simple name of the member.
    */
   String getBinaryName();
}
