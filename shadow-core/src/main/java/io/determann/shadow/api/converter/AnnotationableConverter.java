package io.determann.shadow.api.converter;

import io.determann.shadow.api.shadow.structure.Module;
import io.determann.shadow.api.shadow.structure.Package;
import io.determann.shadow.api.shadow.structure.*;
import io.determann.shadow.api.shadow.type.Class;
import io.determann.shadow.api.shadow.type.Enum;
import io.determann.shadow.api.shadow.type.Record;
import io.determann.shadow.api.shadow.type.*;

import java.util.Optional;

public interface AnnotationableConverter
{
   Annotation toAnnotationOrThrow();

   Optional<Annotation> toAnnotation();

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

   Generic toGenericOrThrow();

   Optional<Generic> toGeneric();

   Interface toInterfaceOrThrow();

   Optional<Interface> toInterface();

   Method toMethodOrThrow();

   Optional<Method> toMethod();

   Module toModuleOrThrow();

   Optional<Module> toModule();

   Package toPackageOrThrow();

   Optional<Package> toPackage();

   Parameter toParameterOrThrow();

   Optional<Parameter> toParameter();

   Receiver toReceiverOrThrow();

   Optional<Receiver> toReceiver();

   Record toRecordOrThrow();

   Optional<Record> toRecord();

   RecordComponent toRecordComponentOrThrow();

   Optional<RecordComponent> toRecordComponent();

   Return toReturnOrThrow();

   Optional<Return> toReturn();

   Variable toVariableOrThrow();

   Optional<Variable> toVariable();
}
