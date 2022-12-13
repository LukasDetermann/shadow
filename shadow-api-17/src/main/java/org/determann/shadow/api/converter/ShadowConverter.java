package org.determann.shadow.api.converter;

import org.determann.shadow.api.shadow.Class;
import org.determann.shadow.api.shadow.Enum;
import org.determann.shadow.api.shadow.Module;
import org.determann.shadow.api.shadow.Package;
import org.determann.shadow.api.shadow.Record;
import org.determann.shadow.api.shadow.Void;
import org.determann.shadow.api.shadow.*;

import javax.lang.model.type.TypeMirror;
import java.util.Optional;

public interface ShadowConverter
{
   Annotation toAnnotationOrThrow();

   Optional<Annotation> toAnnotation();

   Array toArrayOrThrow();

   Optional<Array> toArray();

   Class toClassOrThrow();

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

   Interface toInterfaceThrow();

   Optional<Interface> toInterface();

   Intersection toIntersectionOrThrow();

   Optional<Intersection> toIntersection();

   Method toMethodOrThrow();

   Optional<Method> toMethod();

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

   RecordComponent toRecordOrThrowComponentOrThrow();

   Optional<RecordComponent> toRecordComponent();

   Record toRecordOrThrow();

   Optional<Record> toRecord();

   Generic toGenericOrThrow();

   Optional<Generic> toGeneric();

   Variable<Shadow<TypeMirror>> toVariableOrThrow();

   Optional<Variable<Shadow<TypeMirror>>> toVariable();

   Wildcard toWildcardOrThrow();

   Optional<Wildcard> toWildcard();
}
