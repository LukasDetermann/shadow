package org.determann.shadow.impl.converter;

import org.determann.shadow.api.ShadowApi;
import org.determann.shadow.api.TypeKind;
import org.determann.shadow.api.converter.*;
import org.determann.shadow.api.shadow.Class;
import org.determann.shadow.api.shadow.Enum;
import org.determann.shadow.api.shadow.Module;
import org.determann.shadow.api.shadow.Package;
import org.determann.shadow.api.shadow.Record;
import org.determann.shadow.api.shadow.Void;
import org.determann.shadow.api.shadow.*;

import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import java.util.Optional;

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
                                      RecordComponentConverter,
                                      RecordConverter,
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

   //other representation
   @Override
   public Optional<Annotation> toOptionalAnnotation()
   {
      if (shadow.isTypeKind(TypeKind.ANNOTATION))
      {
         return Optional.of((Annotation) shadow);
      }
      return Optional.empty();
   }

   @Override
   public Optional<Array> toOptionalArray()
   {
      if (shadow.getTypeKind().equals(TypeKind.ARRAY))
      {
         return Optional.of((Array) shadow);
      }
      return Optional.empty();
   }

   @Override
   public Optional<Class> toOptionalClass()
   {
      if (shadow.isTypeKind(TypeKind.CLASS))
      {
         return Optional.of((Class) shadow);
      }
      return Optional.empty();
   }

   @Override
   public Optional<Constructor> toOptionalConstructor()
   {
      if (shadow.isTypeKind(TypeKind.CONSTRUCTOR))
      {
         return Optional.of((Constructor) shadow);
      }
      return Optional.empty();
   }

   @Override
   public Optional<Declared> toOptionalDeclared()
   {
      if (shadow.getTypeKind().isDeclared())
      {
         return Optional.of((Declared) shadow);
      }
      return Optional.empty();
   }

   @Override
   public Optional<EnumConstant> toOptionalEnumConstant()
   {
      if (shadow.getTypeKind().equals(TypeKind.ENUM_CONSTANT))
      {
         return Optional.of((EnumConstant) shadow);
      }
      return Optional.empty();
   }

   @Override
   public Optional<Enum> toOptionalEnum()
   {
      if (shadow.isTypeKind(TypeKind.ENUM))
      {
         return Optional.of((Enum) shadow);
      }
      return Optional.empty();
   }

   @Override
   public Optional<Executable> toOptionalExecutable()
   {
      if (shadow.getTypeKind().isExecutable())
      {
         return Optional.of((Executable) shadow);
      }
      return Optional.empty();
   }

   @Override
   public Optional<Field> toOptionalField()
   {
      if (shadow.getTypeKind().equals(TypeKind.FIELD))
      {
         return Optional.of((Field) shadow);
      }
      return Optional.empty();
   }

   @Override
   public Optional<Interface> toOptionalInterface()
   {
      if (shadow.isTypeKind(TypeKind.INTERFACE))
      {
         return Optional.of((Interface) shadow);
      }
      return Optional.empty();
   }

   @Override
   public Optional<Intersection> toOptionalIntersection()
   {
      if (shadow.getTypeKind().equals(TypeKind.INTERSECTION))
      {
         return Optional.of((Intersection) shadow);
      }
      return Optional.empty();
   }

   @Override
   public Optional<Method> toOptionalMethod()
   {
      if (shadow.isTypeKind(TypeKind.METHOD))
      {
         return Optional.of((Method) shadow);
      }
      return Optional.empty();
   }

   @Override
   public Optional<Module> toOptionalModule()
   {
      if (shadow.isTypeKind(TypeKind.MODULE))
      {
         return Optional.of((Module) shadow);
      }
      return Optional.empty();
   }

   @Override
   public Optional<Package> toOptionalPackage()
   {
      if (shadow.isTypeKind(TypeKind.PACKAGE))
      {
         return Optional.of((Package) shadow);
      }
      return Optional.empty();
   }

   @Override
   public Optional<Parameter> toOptionalParameter()
   {
      if (shadow.getTypeKind().equals(TypeKind.PARAMETER))
      {
         return Optional.of((Parameter) shadow);
      }
      return Optional.empty();
   }

   @Override
   public Optional<Void> toOptionalVoid()
   {
      if (shadow.getTypeKind().equals(TypeKind.VOID))
      {
         return Optional.of((Void) shadow);
      }
      return Optional.empty();
   }

   @Override
   public Optional<Null> toOptionalNull()
   {
      if (shadow.getTypeKind().equals(TypeKind.NULL))
      {
         return Optional.of((Null) shadow);
      }
      return Optional.empty();
   }

   @Override
   public Optional<Primitive> toOptionalPrimitive()
   {
      if (shadow.getTypeKind().isPrimitive())
      {
         return Optional.of((Primitive) shadow);
      }
      return Optional.empty();
   }

   @Override
   public Optional<RecordComponent> toOptionalRecordComponent()
   {
      if (shadow.isTypeKind(TypeKind.RECORD_COMPONENT))
      {
         return Optional.of((RecordComponent) shadow);
      }
      return Optional.empty();
   }

   @Override
   public Optional<Record> toOptionalRecord()
   {
      if (shadow.isTypeKind(TypeKind.RECORD))
      {
         return Optional.of((Record) shadow);
      }
      return Optional.empty();
   }

   @Override
   public Optional<Generic> toOptionalGeneric()
   {
      if (shadow.getTypeKind().equals(TypeKind.GENERIC_TYPE))
      {
         return Optional.of((Generic) shadow);
      }
      return Optional.empty();
   }

   @Override
   public Optional<Variable<Shadow<TypeMirror>>> toOptionalVariable()
   {
      if (shadow.getTypeKind().isVariable())
      {
         //noinspection unchecked
         return Optional.of((Variable<Shadow<TypeMirror>>) shadow);
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
      toOptionalRecord().ifPresent(consumer::recordType);
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
                                                                .orElse(toOptionalRecord()
                                                                              .map(mapper::recordType)
                                                                              .orElse((null))))));
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
