package io.determann.shadow.api.reflection.shadow.structure;

import io.determann.shadow.api.reflection.shadow.AnnotationableReflection;
import io.determann.shadow.api.reflection.shadow.NameableReflection;
import io.determann.shadow.api.reflection.shadow.QualifiedNameableReflection;
import io.determann.shadow.api.reflection.shadow.type.ShadowReflection;
import io.determann.shadow.api.shadow.TypeKind;
import io.determann.shadow.api.shadow.directive.Directive;
import io.determann.shadow.api.shadow.structure.Module;
import io.determann.shadow.api.shadow.structure.Package;
import io.determann.shadow.api.shadow.type.Class;
import io.determann.shadow.api.shadow.type.Enum;
import io.determann.shadow.api.shadow.type.Record;
import io.determann.shadow.api.shadow.type.*;

import java.util.List;
import java.util.Optional;

import static io.determann.shadow.api.shadow.Operations.SHADOW_GET_KIND;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;

public interface ModuleReflection extends Module,
                                          AnnotationableReflection,
                                          ShadowReflection,
                                          NameableReflection,
                                          QualifiedNameableReflection
{
   List<Package> getPackages();

   /**
    * can everybody use reflection on this module?
    */
   boolean isOpen();

   boolean isUnnamed();

   boolean isAutomatic();

   /**
    * Relations between modules
    */
   List<Directive> getDirectives();

   List<Declared> getDeclared();

   Optional<Declared> getDeclared(String qualifiedName);

   default Declared getDeclaredOrThrow(String qualifiedName)
   {
      return getDeclared(qualifiedName).orElseThrow();
   }

   default List<Annotation> getAnnotations()
   {
      return getDeclared().stream()
                          .filter(declared -> TypeKind.ANNOTATION.equals(requestOrThrow(declared, SHADOW_GET_KIND)))
                          .map(declared -> ((Annotation) declared))
                          .toList();
   }

   default Optional<Annotation> getAnnotation(String qualifiedName)
   {
      return getDeclared(qualifiedName).map(Annotation.class::cast);
   }

   default Annotation getAnnotationOrThrow(String qualifiedName)
   {
      return ((Annotation) getDeclaredOrThrow(qualifiedName));
   }

   default List<Class> getClasses()
   {
      return getDeclared().stream()
                          .filter(declared -> TypeKind.CLASS.equals(requestOrThrow(declared, SHADOW_GET_KIND)))
                          .map(declared -> ((Class) declared))
                          .toList();
   }

   default Optional<Class> getClass(String qualifiedName)
   {
      return getDeclared(qualifiedName).map(Class.class::cast);
   }

   default Class getClassOrThrow(String qualifiedName)
   {
      return ((Class) getDeclaredOrThrow(qualifiedName));
   }

   default List<Enum> getEnums()
   {
      return getDeclared().stream()
                          .filter(declared -> TypeKind.ENUM.equals(requestOrThrow(declared, SHADOW_GET_KIND)))
                          .map(declared -> ((Enum) declared))
                          .toList();
   }

   default Optional<Enum> getEnum(String qualifiedName)
   {
      return getDeclared(qualifiedName).map(Enum.class::cast);
   }

   default Enum getEnumOrThrow(String qualifiedName)
   {
      return ((Enum) getDeclaredOrThrow(qualifiedName));
   }

   default List<Interface> getInterfaces()
   {
      return getDeclared().stream()
                          .filter(declared -> TypeKind.INTERFACE.equals(requestOrThrow(declared, SHADOW_GET_KIND)))
                          .map(declared -> ((Interface) declared))
                          .toList();
   }

   default Optional<Interface> getInterface(String qualifiedName)
   {
      return getDeclared(qualifiedName).map(Interface.class::cast);
   }

   default Interface getInterfaceOrThrow(String qualifiedName)
   {
      return ((Interface) getDeclaredOrThrow(qualifiedName));
   }

   default List<Record> getRecords()
   {
      return getDeclared().stream()
                          .filter(declared -> TypeKind.RECORD.equals(requestOrThrow(declared, SHADOW_GET_KIND)))
                          .map(declared -> ((Record) declared))
                          .toList();
   }

   default Optional<Record> getRecord(String qualifiedName)
   {
      return getDeclared(qualifiedName).map(Record.class::cast);
   }

   default Record getRecordOrThrow(String qualifiedName)
   {
      return ((Record) getDeclaredOrThrow(qualifiedName));
   }
}
