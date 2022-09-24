package org.determann.shadow.api.converter;

import org.determann.shadow.api.shadow.Class;
import org.determann.shadow.api.shadow.Enum;
import org.determann.shadow.api.shadow.Module;
import org.determann.shadow.api.shadow.Package;
import org.determann.shadow.api.shadow.Void;
import org.determann.shadow.api.shadow.*;

import javax.lang.model.type.TypeMirror;
import java.util.Optional;

public interface ShadowConverter
{
   Annotation toAnnotation();

   Optional<Annotation> toOptionalAnnotation();

   Array toArray();

   Optional<Array> toOptionalArray();

   Class toClass();

   Optional<Class> toOptionalClass();

   Constructor toConstructor();

   Optional<Constructor> toOptionalConstructor();

   Declared toDeclared();

   Optional<Declared> toOptionalDeclared();

   EnumConstant toEnumConstant();

   Optional<EnumConstant> toOptionalEnumConstant();

   Enum toEnum();

   Optional<Enum> toOptionalEnum();

   Executable toExecutable();

   Optional<Executable> toOptionalExecutable();

   Field toField();

   Optional<Field> toOptionalField();

   Interface toInterface();

   Optional<Interface> toOptionalInterface();

   Intersection toIntersection();

   Optional<Intersection> toOptionalIntersection();

   Method toMethod();

   Optional<Method> toOptionalMethod();

   Module toModule();

   Optional<Module> toOptionalModule();

   Void toVoid();

   Optional<Void> toOptionalVoid();

   Null toNull();

   Optional<Null> toOptionalNull();

   Package toPackage();

   Optional<Package> toOptionalPackage();

   Parameter toParameter();

   Optional<Parameter> toOptionalParameter();

   Primitive toPrimitive();

   Optional<Primitive> toOptionalPrimitive();

   Generic toGeneric();

   Optional<Generic> toOptionalGeneric();

   Variable<Shadow<TypeMirror>> toVariable();

   Optional<Variable<Shadow<TypeMirror>>> toOptionalVariable();

   Wildcard toWildcard();

   Optional<Wildcard> toOptionalWildcard();
}
