package io.determann.shadow.api.converter;

import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Enum;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.Record;
import io.determann.shadow.api.shadow.*;

import java.util.Optional;

public interface AnnotationableConverter
{
   Annotation toAnnotationOrThrow();

   Optional<Annotation> toAnnotation();

   io.determann.shadow.api.shadow.Class toClassOrThrow();

   Optional<Class> toClass();

   Constructor toConstructorOrThrow();

   Optional<Constructor> toConstructor();

   Declared toDeclaredOrThrow();

   Optional<Declared> toDeclared();

   EnumConstant toEnumConstantOrThrow();

   Optional<EnumConstant> toEnumConstant();

   io.determann.shadow.api.shadow.Enum toEnumOrThrow();

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
