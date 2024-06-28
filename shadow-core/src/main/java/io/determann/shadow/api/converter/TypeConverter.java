package io.determann.shadow.api.converter;

import io.determann.shadow.api.shadow.structure.Module;
import io.determann.shadow.api.shadow.structure.Package;
import io.determann.shadow.api.shadow.structure.*;
import io.determann.shadow.api.shadow.type.Class;
import io.determann.shadow.api.shadow.type.Enum;
import io.determann.shadow.api.shadow.type.Record;
import io.determann.shadow.api.shadow.type.Void;
import io.determann.shadow.api.shadow.type.*;

import java.util.Optional;

public interface TypeConverter
{
   Annotation toAnnotationOrThrow();

   Optional<Annotation> toAnnotation();

   Array toArrayOrThrow();

   Optional<Array> toArray();

   Class toClassOrThrow();

   Optional<Class> toClass();

   Declared toDeclaredOrThrow();

   Optional<Declared> toDeclared();

   EnumConstant toEnumConstantOrThrow();

   Optional<EnumConstant> toEnumConstant();

   Enum toEnumOrThrow();

   Optional<Enum> toEnum();

   Field toFieldOrThrow();

   Optional<Field> toField();

   Interface toInterfaceOrThrow();

   Optional<Interface> toInterface();

   Intersection toIntersectionOrThrow();

   Optional<Intersection> toIntersection();

   Module toModuleOrThrow();

   Optional<Module> toModule();

   Void toVoidOrThrow();

   Optional<Void> toVoid();

   Null toNullOrThrow();

   Optional<Null> toNull();

   Package toPackageOrThrow();

   Optional<Package> toPackage();

   Parameter toParameterOrThrow();

   Optional<Parameter> toParameter();

   Primitive toPrimitiveOrThrow();

   Optional<Primitive> toPrimitive();

   RecordComponent toRecordComponentOrThrow();

   Optional<RecordComponent> toRecordComponent();

   Record toRecordOrThrow();

   Optional<Record> toRecord();

   Generic toGenericOrThrow();

   Optional<Generic> toGeneric();

   Variable toVariableOrThrow();

   Optional<Variable> toVariable();

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
}
