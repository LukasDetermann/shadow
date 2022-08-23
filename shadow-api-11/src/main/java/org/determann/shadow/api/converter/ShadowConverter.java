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
   //other representation
   Optional<Annotation> toAnnotation();

   Optional<Array> toArray();

   Optional<Class> toClass();

   Optional<Constructor> toConstructor();

   Optional<Declared> toDeclared();

   Optional<EnumConstant> toEnumConstant();

   Optional<Enum> toEnum();

   Optional<Executable> toExecutable();

   Optional<Field> toField();

   Optional<Interface> toInterface();

   Optional<Intersection> toIntersection();

   Optional<Method> toMethod();

   Optional<Module> toModule();

   Optional<Void> toVoid();

   Optional<Null> toNull();

   Optional<Package> toPackage();

   Optional<Parameter> toParameter();

   Optional<Primitive> toPrimitive();

   Optional<Generic> toGeneric();

   Optional<Variable<Shadow<TypeMirror>>> toVariable();

   Optional<Wildcard> toWildcard();
}
