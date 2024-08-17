package io.determann.shadow.api.lang_model.shadow.structure;

import io.determann.shadow.api.lang_model.shadow.AnnotationableLangModel;
import io.determann.shadow.api.lang_model.shadow.DocumentedLangModel;
import io.determann.shadow.api.lang_model.shadow.NameableLangModel;
import io.determann.shadow.api.lang_model.shadow.QualifiedNameableLamgModel;
import io.determann.shadow.api.lang_model.shadow.directive.DirectiveLangModel;
import io.determann.shadow.api.lang_model.shadow.type.*;
import io.determann.shadow.api.shadow.TypeKind;
import io.determann.shadow.api.shadow.structure.Module;

import java.util.List;
import java.util.Optional;

import static io.determann.shadow.api.shadow.Operations.SHADOW_GET_KIND;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;

public interface ModuleLangModel extends Module,
                                         AnnotationableLangModel,
                                         ShadowLangModel,
                                         NameableLangModel,
                                         QualifiedNameableLamgModel,
                                         DocumentedLangModel
{
   List<PackageLangModel> getPackages();

   /**
    * can everybody use reflection on this module?
    */
   boolean isOpen();

   boolean isUnnamed();

   boolean isAutomatic();

   /**
    * Relations between modules
    */
   List<DirectiveLangModel> getDirectives();

   List<DeclaredLangModel> getDeclared();

   Optional<DeclaredLangModel> getDeclared(String qualifiedName);

   default DeclaredLangModel getDeclaredOrThrow(String qualifiedName)
   {
      return getDeclared(qualifiedName).orElseThrow();
   }

   default List<AnnotationableLangModel> getAnnotations()
   {
      return getDeclared().stream()
                          .filter(declared -> TypeKind.ANNOTATION.equals(requestOrThrow(declared, SHADOW_GET_KIND)))
                          .map(declared -> ((AnnotationableLangModel) declared))
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
