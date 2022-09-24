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

import javax.lang.model.type.PrimitiveType;
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

   public ConverterImpl(ShadowApi shadowApi, Shadow<? extends TypeMirror> shadow)
   {
      this.shadowApi = shadowApi;
      this.shadow = shadow;
   }

   @Override
   public Annotation toAnnotation()
   {
      return to(TypeKind.ANNOTATION);
   }

   @Override
   public Optional<Annotation> toOptionalAnnotation()
   {
      return toOptional(TypeKind.ANNOTATION);
   }

   @Override
   public Array toArray()
   {
      return to(TypeKind.ARRAY);
   }

   @Override
   public Optional<Array> toOptionalArray()
   {
      return toOptional(TypeKind.ARRAY);
   }

   @Override
   public Class toClass()
   {
      return to(TypeKind.CLASS);
   }

   @Override
   public Optional<Class> toOptionalClass()
   {
      return toOptional(TypeKind.CLASS);
   }

   @Override
   public Constructor toConstructor()
   {
      return to(TypeKind.CONSTRUCTOR);
   }

   @Override
   public Optional<Constructor> toOptionalConstructor()
   {
      return toOptional(TypeKind.CONSTRUCTOR);
   }

   @Override
   public Declared toDeclared()
   {
      return to(TypeKind::isDeclared);
   }

   @Override
   public Optional<Declared> toOptionalDeclared()
   {
      return toOptional(TypeKind::isDeclared);
   }

   @Override
   public EnumConstant toEnumConstant()
   {
      return to(TypeKind.ENUM_CONSTANT);
   }

   @Override
   public Optional<EnumConstant> toOptionalEnumConstant()
   {
      return toOptional(TypeKind.ENUM_CONSTANT);
   }

   @Override
   public Enum toEnum()
   {
      return to(TypeKind.ENUM);
   }

   @Override
   public Optional<Enum> toOptionalEnum()
   {
      return toOptional(TypeKind.ENUM);
   }

   @Override
   public Executable toExecutable()
   {
      return to(TypeKind::isExecutable);
   }

   @Override
   public Optional<Executable> toOptionalExecutable()
   {
      return toOptional(TypeKind::isExecutable);
   }

   @Override
   public Field toField()
   {
      return to(TypeKind.FIELD);
   }

   @Override
   public Optional<Field> toOptionalField()
   {
      return toOptional(TypeKind.FIELD);
   }

   @Override
   public Interface toInterface()
   {
      return to(TypeKind.INTERFACE);
   }

   @Override
   public Optional<Interface> toOptionalInterface()
   {
      return toOptional(TypeKind.INTERFACE);
   }

   @Override
   public Intersection toIntersection()
   {
      return to(TypeKind.INTERSECTION);
   }

   @Override
   public Optional<Intersection> toOptionalIntersection()
   {
      return toOptional(TypeKind.INTERSECTION);
   }

   @Override
   public Method toMethod()
   {
      return to(TypeKind.METHOD);
   }

   @Override
   public Optional<Method> toOptionalMethod()
   {
      return toOptional(TypeKind.METHOD);
   }

   @Override
   public Module toModule()
   {
      return to(TypeKind.MODULE);
   }

   @Override
   public Optional<Module> toOptionalModule()
   {
      return toOptional(TypeKind.MODULE);
   }

   @Override
   public Void toVoid()
   {
      return to(TypeKind.VOID);
   }

   @Override
   public Optional<Package> toOptionalPackage()
   {
      return toOptional(TypeKind.PACKAGE);
   }

   @Override
   public Parameter toParameter()
   {
      return to(TypeKind.PARAMETER);
   }

   @Override
   public Optional<Parameter> toOptionalParameter()
   {
      return toOptional(TypeKind.PARAMETER);
   }

   @Override
   public Primitive toPrimitive()
   {
      return to(TypeKind::isPrimitive);
   }

   @Override
   public Optional<Void> toOptionalVoid()
   {
      return toOptional(TypeKind.VOID);
   }

   @Override
   public Null toNull()
   {
      return to(TypeKind.NULL);
   }

   @Override
   public Optional<Null> toOptionalNull()
   {
      return toOptional(TypeKind.NULL);
   }

   @Override
   public Package toPackage()
   {
      return to(TypeKind.PACKAGE);
   }

   @Override
   public Optional<Primitive> toOptionalPrimitive()
   {
      return toOptional(TypeKind::isPrimitive);
   }

   @Override
   public Generic toGeneric()
   {
      return to(TypeKind.GENERIC);
   }

   @Override
   public Optional<Generic> toOptionalGeneric()
   {
      return toOptional(TypeKind.GENERIC);
   }

   @Override
   public Variable<Shadow<TypeMirror>> toVariable()
   {
      return to(TypeKind::isVariable);
   }

   @Override
   public Optional<Variable<Shadow<TypeMirror>>> toOptionalVariable()
   {
      return toOptional(TypeKind::isVariable);
   }

   @Override
   public Wildcard toWildcard()
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
   public Optional<Wildcard> toOptionalWildcard()
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
      toOptionalClass().ifPresent(consumer::classType);
      toOptionalInterface().ifPresent(consumer::interfaceType);
      toOptionalEnum().ifPresent(consumer::enumType);
      toOptionalAnnotation().ifPresent(consumer::annotationType);
   }

   @Override
   public <T> T mapper(DeclaredMapper<T> mapper)
   {
      return toOptionalClass().map(mapper::classType)
                      .orElse(toOptionalInterface()
                                    .map(mapper::interfaceType)
                                    .orElse(toOptionalEnum()
                                                  .map(mapper::enumType)
                                                  .orElse(toOptionalAnnotation()
                                                                .map(mapper::annotationType)
                                                                .orElse((null)))));
   }

   @Override
   public void consumer(ExecutableConsumer consumer)
   {
      toOptionalMethod().ifPresent(consumer::method);
      toOptionalConstructor().ifPresent(consumer::constructor);
   }

   @Override
   public <T> T mapper(ExecutableMapper<T> mapper)
   {
      return toOptionalMethod().map(mapper::method)
                               .orElse(toOptionalConstructor()
                                             .map(mapper::constructor)
                                             .orElse(null));
   }

   @Override
   public void consumer(VariableConsumer consumer)
   {
      toOptionalEnumConstant().ifPresent(consumer::enumConstant);
      toOptionalField().ifPresent(consumer::field);
      toOptionalParameter().ifPresent(consumer::parameter);
   }

   @Override
   public <T> T mapper(VariableMapper<T> mapper)
   {
      return toOptionalEnumConstant().map(mapper::enumConstant)
                                     .orElse(toOptionalField()
                                                   .map(mapper::field)
                                                   .orElse(toOptionalParameter()
                                                                 .map(mapper::parameter)
                                                                 .orElse(null)));
   }

   //conversion
   @Override
   public Wildcard asExtendsWildcard()
   {
      return shadowApi.getShadowFactory().shadowFromType(shadowApi.getJdkApiContext().types().getWildcardType(shadow.getMirror(), null));
   }

   @Override
   public Wildcard asSuperWildcard()
   {
      return shadowApi.getShadowFactory().shadowFromType(shadowApi.getJdkApiContext().types().getWildcardType(null, shadow.getMirror()));
   }

   @Override
   public Primitive asUnboxed()
   {
      return shadowApi.getShadowFactory().shadowFromType(shadowApi.getJdkApiContext().types().unboxedType(shadow.getMirror()));
   }

   @Override
   public Shadow<TypeMirror> asBoxed()
   {
      return shadowApi.getShadowFactory().shadowFromType(
            shadowApi.getJdkApiContext().types().boxedClass((PrimitiveType) shadow.getMirror()).asType());
   }

   @Override
   public Array asArray()
   {
      return shadowApi.getShadowFactory().shadowFromType(shadowApi.getJdkApiContext().types().getArrayType(shadow.getMirror()));
   }
}
