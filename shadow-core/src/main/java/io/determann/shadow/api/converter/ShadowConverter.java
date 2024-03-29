package io.determann.shadow.api.converter;

import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Enum;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.Record;
import io.determann.shadow.api.shadow.Void;
import io.determann.shadow.api.shadow.*;

import java.util.Optional;

public interface ShadowConverter
{
   Annotation toAnnotationOrThrow();

   Optional<Annotation> toAnnotation();

   Array toArrayOrThrow();

   Optional<Array> toArray();

   io.determann.shadow.api.shadow.Class toClassOrThrow();

   Optional<Class> toClass();

   Constructor toConstructorOrThrow();

   Optional<Constructor> toConstructor();

   Declared toDeclaredOrThrow();

   Optional<Declared> toDeclared();

   EnumConstant toEnumConstantOrThrow();

   Optional<EnumConstant> toEnumConstant();

   Enum toEnumOrThrow();

   Optional<Enum> toEnum();

   Executable toExecutableOrThrow();

   Optional<Executable> toExecutable();

   Field toFieldOrThrow();

   Optional<Field> toField();

   Interface toInterfaceOrThrow();

   Optional<Interface> toInterface();

   Intersection toIntersectionOrThrow();

   Optional<Intersection> toIntersection();

   Method toMethodOrThrow();

   Optional<Method> toMethod();

   io.determann.shadow.api.shadow.Module toModuleOrThrow();

   Optional<Module> toModule();

   io.determann.shadow.api.shadow.Void toVoidOrThrow();

   Optional<Void> toVoid();

   Null toNullOrThrow();

   Optional<Null> toNull();

   io.determann.shadow.api.shadow.Package toPackageOrThrow();

   Optional<Package> toPackage();

   Parameter toParameterOrThrow();

   Optional<Parameter> toParameter();

   Primitive toPrimitiveOrThrow();

   Optional<Primitive> toPrimitive();

   RecordComponent toRecordComponentOrThrow();

   Optional<RecordComponent> toRecordComponent();

   io.determann.shadow.api.shadow.Record toRecordOrThrow();

   Optional<Record> toRecord();

   Generic toGenericOrThrow();

   Optional<Generic> toGeneric();

   Variable<Shadow> toVariableOrThrow();

   Optional<Variable<Shadow>> toVariable();

   Wildcard toWildcardOrThrow();

   Optional<Wildcard> toWildcard();

   /**
    * consumes all leafs of {@link Declared}
    */
   void consume(DeclaredConsumer adapter);

   /**
    * consumes all leafs of {@link Declared}
    */
   <T> T map(DeclaredMapper<T> mapper);

   /**
    * consumes all leafs of {@link Variable}
    */
   void consume(VariableConsumer consumer);

   /**
    * maps all leafs of {@link Variable}
    */
   <T> T map(VariableMapper<T> mapper);

   /**
    * consumes all leafs of {@link Executable}
    */
   void consume(ExecutableConsumer adapter);

   /**
    * consumes all leafs of {@link Executable}
    */
   <T> T map(ExecutableMapper<T> mapper);
}
