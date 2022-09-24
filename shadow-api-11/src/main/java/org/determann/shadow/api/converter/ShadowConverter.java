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
   Optional<Annotation> toOptionalAnnotation();

   Optional<Array> toOptionalArray();

   Optional<Class> toOptionalClass();

   Optional<Constructor> toOptionalConstructor();

   Optional<Declared> toOptionalDeclared();

   Optional<EnumConstant> toOptionalEnumConstant();

   Optional<Enum> toOptionalEnum();

   Optional<Executable> toOptionalExecutable();

   Optional<Field> toOptionalField();

   Optional<Interface> toOptionalInterface();

   Optional<Intersection> toOptionalIntersection();

   Optional<Method> toOptionalMethod();

   Optional<Module> toOptionalModule();

   Optional<Void> toOptionalVoid();

   Optional<Null> toOptionalNull();

   Optional<Package> toOptionalPackage();

   Optional<Parameter> toOptionalParameter();

   Optional<Primitive> toOptionalPrimitive();

   Optional<Generic> toOptionalGeneric();

   Optional<Variable<Shadow<TypeMirror>>> toOptionalVariable();

   Optional<Wildcard> toOptionalWildcard();
}
