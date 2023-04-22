package io.determann.shadow.api.converter;

import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Enum;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.Void;
import io.determann.shadow.api.shadow.*;

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

   Interface toInterfaceOrThrow();

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

   Generic toGenericOrThrow();

   Optional<Generic> toGeneric();

   Variable<Shadow<TypeMirror>> toVariableOrThrow();

   Optional<Variable<Shadow<TypeMirror>>> toVariable();

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
