package io.determann.shadow.api.lang_model.shadow.structure;

import io.determann.shadow.api.lang_model.shadow.*;
import io.determann.shadow.api.lang_model.shadow.type.*;
import io.determann.shadow.api.shadow.TypeKind;
import io.determann.shadow.api.shadow.structure.Package;

import java.util.List;
import java.util.Optional;

import static io.determann.shadow.api.Operations.SHADOW_GET_KIND;
import static io.determann.shadow.api.Provider.requestOrThrow;

public interface PackageLangModel extends Package,
                                          AnnotationableLangModel,
                                          NameableLangModel,
                                          QualifiedNameableLamgModel,
                                          ModuleEnclosedLangModel,
                                          DocumentedLangModel
{
   /**
    * Unnamed packages are intend for small snips of code like jShell and not seen in regular projects
    */
   boolean isUnnamed();

   List<DeclaredLangModel> getDeclared();

   Optional<DeclaredLangModel> getDeclared(String qualifiedName);

   default DeclaredLangModel getDeclaredOrThrow(String qualifiedName)
   {
      return getDeclared(qualifiedName).orElseThrow();
   }

   default List<AnnotationLangModel> getAnnotations()
   {
      return getDeclared().stream()
                          .filter(declared -> TypeKind.ANNOTATION.equals(requestOrThrow(declared, SHADOW_GET_KIND)))
                          .map(declared -> ((AnnotationLangModel) declared))
                          .toList();
   }

   default Optional<AnnotationLangModel> getAnnotation(String qualifiedName)
   {
      return getDeclared(qualifiedName).map(AnnotationLangModel.class::cast);
   }

   default AnnotationLangModel getAnnotationOrThrow(String qualifiedName)
   {
      return ((AnnotationLangModel) getDeclaredOrThrow(qualifiedName));
   }

   default List<ClassLangModel> getClasses()
   {
      return getDeclared().stream()
                          .filter(declared -> TypeKind.CLASS.equals(requestOrThrow(declared, SHADOW_GET_KIND)))
                          .map(declared -> ((ClassLangModel) declared))
                          .toList();
   }

   default Optional<ClassLangModel> getClass(String qualifiedName)
   {
      return getDeclared(qualifiedName).map(ClassLangModel.class::cast);
   }

   default ClassLangModel getClassOrThrow(String qualifiedName)
   {
      return ((ClassLangModel) getDeclaredOrThrow(qualifiedName));
   }

   default List<EnumLangModel> getEnums()
   {
      return getDeclared().stream()
                          .filter(declared -> TypeKind.ENUM.equals(requestOrThrow(declared, SHADOW_GET_KIND)))
                          .map(declared -> ((EnumLangModel) declared))
                          .toList();
   }

   default Optional<EnumLangModel> getEnum(String qualifiedName)
   {
      return getDeclared(qualifiedName).map(EnumLangModel.class::cast);
   }

   default EnumLangModel getEnumOrThrow(String qualifiedName)
   {
      return ((EnumLangModel) getDeclaredOrThrow(qualifiedName));
   }

   default List<InterfaceLangModel> getInterfaces()
   {
      return getDeclared().stream()
                          .filter(declared -> TypeKind.INTERFACE.equals(requestOrThrow(declared, SHADOW_GET_KIND)))
                          .map(declared -> ((InterfaceLangModel) declared))
                          .toList();
   }

   default Optional<InterfaceLangModel> getInterface(String qualifiedName)
   {
      return getDeclared(qualifiedName).map(InterfaceLangModel.class::cast);
   }

   default InterfaceLangModel getInterfaceOrThrow(String qualifiedName)
   {
      return ((InterfaceLangModel) getDeclaredOrThrow(qualifiedName));
   }

   default List<RecordLangModel> getRecords()
   {
      return getDeclared().stream()
                          .filter(declared -> TypeKind.RECORD.equals(requestOrThrow(declared, SHADOW_GET_KIND)))
                          .map(declared -> ((RecordLangModel) declared))
                          .toList();
   }

   default Optional<RecordLangModel> getRecord(String qualifiedName)
   {
      return getDeclared(qualifiedName).map(RecordLangModel.class::cast);
   }

   default RecordLangModel getRecordOrThrow(String qualifiedName)
   {
      return ((RecordLangModel) getDeclaredOrThrow(qualifiedName));
   }
}
