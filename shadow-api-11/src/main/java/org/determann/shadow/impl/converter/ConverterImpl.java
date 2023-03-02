package org.determann.shadow.impl.converter;

import org.determann.shadow.api.ShadowApi;
import org.determann.shadow.api.TypeKind;
import org.determann.shadow.api.converter.*;
import org.determann.shadow.api.shadow.Class;
import org.determann.shadow.api.shadow.Enum;
import org.determann.shadow.api.shadow.Module;
import org.determann.shadow.api.shadow.Package;
import org.determann.shadow.api.shadow.Void;
import org.determann.shadow.api.shadow.*;

import javax.lang.model.type.TypeMirror;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ConverterImpl implements ShadowConverter,
                                      AnnotationConverter,
                                      ArrayConverter,
                                      ClassConverter,
                                      ConstructorConverter,
                                      DeclaredConverter,
                                      EnumConstantConverter,
                                      EnumConverter,
                                      ExecutableConverter,
                                      FieldConverter,
                                      InterfaceConverter,
                                      IntersectionConverter,
                                      MethodConverter,
                                      ModuleConverter,
                                      VoidConverter,
                                      NullConverter,
                                      PackageConverter,
                                      ParameterConverter,
                                      PrimitiveConverter,
                                      GenericConverter,
                                      VariableConverter,
                                      WildcardConverter
{
   private final ShadowApi shadowApi;
   private final Shadow<? extends TypeMirror> shadow;

   public ConverterImpl(Shadow<? extends TypeMirror> shadow)
   {
      this.shadowApi = shadow.getApi();
      this.shadow = shadow;
   }

   @Override
   public Annotation toAnnotationOrThrow()
   {
      return to(TypeKind.ANNOTATION);
   }

   @Override
   public Optional<Annotation> toAnnotation()
   {
      return toOptional(TypeKind.ANNOTATION);
   }

   @Override
   public Array toArrayOrThrow()
   {
      return to(TypeKind.ARRAY);
   }

   @Override
   public Optional<Array> toArray()
   {
      return toOptional(TypeKind.ARRAY);
   }

   @Override
   public Class toClassOrThrow()
   {
      return to(TypeKind.CLASS);
   }

   @Override
   public Optional<Class> toClass()
   {
      return toOptional(TypeKind.CLASS);
   }

   @Override
   public Constructor toConstructorOrThrow()
   {
      return to(TypeKind.CONSTRUCTOR);
   }

   @Override
   public Optional<Constructor> toConstructor()
   {
      return toOptional(TypeKind.CONSTRUCTOR);
   }

   @Override
   public Declared toDeclaredOrThrow()
   {
      return to(TypeKind::isDeclared);
   }

   @Override
   public Optional<Declared> toDeclared()
   {
      return toOptional(TypeKind::isDeclared);
   }

   @Override
   public EnumConstant toEnumConstantOrThrow()
   {
      return to(TypeKind.ENUM_CONSTANT);
   }

   @Override
   public Optional<EnumConstant> toEnumConstant()
   {
      return toOptional(TypeKind.ENUM_CONSTANT);
   }

   @Override
   public Enum toEnumOrThrow()
   {
      return to(TypeKind.ENUM);
   }

   @Override
   public Optional<Enum> toEnum()
   {
      return toOptional(TypeKind.ENUM);
   }

   @Override
   public Executable toExecutableOrThrow()
   {
      return to(TypeKind::isExecutable);
   }

   @Override
   public Optional<Executable> toExecutable()
   {
      return toOptional(TypeKind::isExecutable);
   }

   @Override
   public Field toFieldOrThrow()
   {
      return to(TypeKind.FIELD);
   }

   @Override
   public Optional<Field> toField()
   {
      return toOptional(TypeKind.FIELD);
   }

   @Override
   public Interface toInterfaceOrThrow()
   {
      return to(TypeKind.INTERFACE);
   }

   @Override
   public Optional<Interface> toInterface()
   {
      return toOptional(TypeKind.INTERFACE);
   }

   @Override
   public Intersection toIntersectionOrThrow()
   {
      return to(TypeKind.INTERSECTION);
   }

   @Override
   public Optional<Intersection> toIntersection()
   {
      return toOptional(TypeKind.INTERSECTION);
   }

   @Override
   public Method toMethodOrThrow()
   {
      return to(TypeKind.METHOD);
   }

   @Override
   public Optional<Method> toMethod()
   {
      return toOptional(TypeKind.METHOD);
   }

   @Override
   public Module toModuleOrThrow()
   {
      return to(TypeKind.MODULE);
   }

   @Override
   public Optional<Module> toModule()
   {
      return toOptional(TypeKind.MODULE);
   }

   @Override
   public Void toVoidOrThrow()
   {
      return to(TypeKind.VOID);
   }

   @Override
   public Optional<Package> toPackage()
   {
      return toOptional(TypeKind.PACKAGE);
   }

   @Override
   public Parameter toParameterOrThrow()
   {
      return to(TypeKind.PARAMETER);
   }

   @Override
   public Optional<Parameter> toParameter()
   {
      return toOptional(TypeKind.PARAMETER);
   }

   @Override
   public Primitive toPrimitiveOrThrow()
   {
      return to(TypeKind::isPrimitive);
   }

   @Override
   public Optional<Void> toVoid()
   {
      return toOptional(TypeKind.VOID);
   }

   @Override
   public Null toNullOrThrow()
   {
      return to(TypeKind.NULL);
   }

   @Override
   public Optional<Null> toNull()
   {
      return toOptional(TypeKind.NULL);
   }

   @Override
   public Package toPackageOrThrow()
   {
      return to(TypeKind.PACKAGE);
   }

   @Override
   public Optional<Primitive> toPrimitive()
   {
      return toOptional(TypeKind::isPrimitive);
   }

   @Override
   public Generic toGenericOrThrow()
   {
      return to(TypeKind.GENERIC);
   }

   @Override
   public Optional<Generic> toGeneric()
   {
      return toOptional(TypeKind.GENERIC);
   }

   @Override
   public Variable<Shadow<TypeMirror>> toVariableOrThrow()
   {
      return to(TypeKind::isVariable);
   }

   @Override
   public Optional<Variable<Shadow<TypeMirror>>> toVariable()
   {
      return toOptional(TypeKind::isVariable);
   }

   @Override
   public Wildcard toWildcardOrThrow()
   {
      return to(TypeKind.WILDCARD);
   }

   private <MIRROR extends TypeMirror, SHADOW extends Shadow<MIRROR>> SHADOW to(TypeKind typeKind)
   {
      //noinspection unchecked
      return (SHADOW) toOptional(typeKind).orElseThrow(() -> new IllegalStateException(shadow.getTypeKind() + " is not a " + typeKind));
   }

   private <MIRROR extends TypeMirror, SHADOW extends Shadow<MIRROR>> SHADOW to(Predicate<TypeKind> typeKindPredicate)
   {
      List<TypeKind> typeKinds = Arrays.stream(TypeKind.values()).filter(typeKindPredicate).collect(Collectors.toUnmodifiableList());
      //noinspection unchecked
      return (SHADOW) toOptional(typeKindPredicate)
            .orElseThrow(() -> new IllegalStateException(shadow.getTypeKind() + " is none of " + typeKinds));
   }

   private <MIRROR extends TypeMirror, SHADOW extends Shadow<MIRROR>> Optional<SHADOW> toOptional(TypeKind typeKind)
   {
      return toOptional(typeKind1 -> typeKind1.equals(typeKind));
   }

   private <MIRROR extends TypeMirror, SHADOW extends Shadow<MIRROR>> Optional<SHADOW> toOptional(Predicate<TypeKind> typeKindPredicate)
   {
      if (typeKindPredicate.test(shadow.getTypeKind()))
      {
         //noinspection unchecked
         return Optional.of((SHADOW) shadow);
      }
      return Optional.empty();
   }

   @Override
   public Optional<Wildcard> toWildcard()
   {
      if (shadow.getTypeKind().equals(TypeKind.WILDCARD))
      {
         return Optional.of((Wildcard) shadow);
      }
      return Optional.empty();
   }

   //mapper and adapter

   @Override
   public void consumer(DeclaredConsumer consumer)
   {
      toClass().ifPresent(consumer::classType);
      toInterface().ifPresent(consumer::interfaceType);
      toEnum().ifPresent(consumer::enumType);
      toAnnotation().ifPresent(consumer::annotationType);
   }

   @Override
   public <T> T mapper(DeclaredMapper<T> mapper)
   {
      return toClass().map(mapper::classType)
                      .orElse(toInterface()
                                    .map(mapper::interfaceType)
                                    .orElse(toEnum()
                                                  .map(mapper::enumType)
                                                  .orElse(toAnnotation()
                                                                .map(mapper::annotationType)
                                                                .orElse((null)))));
   }

   @Override
   public void consumer(ExecutableConsumer consumer)
   {
      toMethod().ifPresent(consumer::method);
      toConstructor().ifPresent(consumer::constructor);
   }

   @Override
   public <T> T mapper(ExecutableMapper<T> mapper)
   {
      return toMethod().map(mapper::method)
                               .orElse(toConstructor()
                                             .map(mapper::constructor)
                                             .orElse(null));
   }

   @Override
   public void consumer(VariableConsumer consumer)
   {
      toEnumConstant().ifPresent(consumer::enumConstant);
      toField().ifPresent(consumer::field);
      toParameter().ifPresent(consumer::parameter);
   }

   @Override
   public <T> T mapper(VariableMapper<T> mapper)
   {
      return toEnumConstant().map(mapper::enumConstant)
                                     .orElse(toField()
                                                   .map(mapper::field)
                                                   .orElse(toParameter()
                                                                 .map(mapper::parameter)
                                                                 .orElse(null)));
   }
}
