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
   public Optional<Annotation> toAnnotation()
   {
      if (shadow.isTypeKind(TypeKind.ANNOTATION))
      {
         return Optional.of((Annotation) shadow);
      }
      return Optional.empty();
   }

   @Override
   public Optional<Array> toArray()
   {
      if (shadow.getTypeKind().equals(TypeKind.ARRAY))
      {
         return Optional.of((Array) shadow);
      }
      return Optional.empty();
   }

   @Override
   public Optional<Class> toClass()
   {
      if (shadow.isTypeKind(TypeKind.CLASS))
      {
         return Optional.of((Class) shadow);
      }
      return Optional.empty();
   }

   @Override
   public Optional<Constructor> toConstructor()
   {
      if (shadow.isTypeKind(TypeKind.CONSTRUCTOR))
      {
         return Optional.of((Constructor) shadow);
      }
      return Optional.empty();
   }

   @Override
   public Optional<Declared> toDeclared()
   {
      if (shadow.getTypeKind().isDeclared())
      {
         return Optional.of((Declared) shadow);
      }
      return Optional.empty();
   }

   @Override
   public Optional<EnumConstant> toEnumConstant()
   {
      if (shadow.getTypeKind().equals(TypeKind.ENUM_CONSTANT))
      {
         return Optional.of((EnumConstant) shadow);
      }
      return Optional.empty();
   }

   @Override
   public Optional<Enum> toEnum()
   {
      if (shadow.isTypeKind(TypeKind.ENUM))
      {
         return Optional.of((Enum) shadow);
      }
      return Optional.empty();
   }

   @Override
   public Optional<Executable> toExecutable()
   {
      if (shadow.getTypeKind().isExecutable())
      {
         return Optional.of((Executable) shadow);
      }
      return Optional.empty();
   }

   @Override
   public Optional<Field> toField()
   {
      if (shadow.getTypeKind().equals(TypeKind.FIELD))
      {
         return Optional.of((Field) shadow);
      }
      return Optional.empty();
   }

   @Override
   public Optional<Interface> toInterface()
   {
      if (shadow.isTypeKind(TypeKind.INTERFACE))
      {
         return Optional.of((Interface) shadow);
      }
      return Optional.empty();
   }

   @Override
   public Optional<Intersection> toIntersection()
   {
      if (shadow.getTypeKind().equals(TypeKind.INTERSECTION))
      {
         return Optional.of((Intersection) shadow);
      }
      return Optional.empty();
   }

   @Override
   public Optional<Method> toMethod()
   {
      if (shadow.isTypeKind(TypeKind.METHOD))
      {
         return Optional.of((Method) shadow);
      }
      return Optional.empty();
   }

   @Override
   public Optional<Module> toModule()
   {
      if (shadow.isTypeKind(TypeKind.MODULE))
      {
         return Optional.of((Module) shadow);
      }
      return Optional.empty();
   }

   @Override
   public Optional<Package> toPackage()
   {
      if (shadow.isTypeKind(TypeKind.PACKAGE))
      {
         return Optional.of((Package) shadow);
      }
      return Optional.empty();
   }

   @Override
   public Optional<Parameter> toParameter()
   {
      if (shadow.getTypeKind().equals(TypeKind.PARAMETER))
      {
         return Optional.of((Parameter) shadow);
      }
      return Optional.empty();
   }

   @Override
   public Optional<Void> toVoid()
   {
      if (shadow.getTypeKind().equals(TypeKind.VOID))
      {
         return Optional.of((Void) shadow);
      }
      return Optional.empty();
   }

   @Override
   public Optional<Null> toNull()
   {
      if (shadow.getTypeKind().equals(TypeKind.NULL))
      {
         return Optional.of((Null) shadow);
      }
      return Optional.empty();
   }

   @Override
   public Optional<Primitive> toPrimitive()
   {
      if (shadow.getTypeKind().isPrimitive())
      {
         return Optional.of((Primitive) shadow);
      }
      return Optional.empty();
   }

   @Override
   public Optional<RecordComponent> toRecordComponent()
   {
      if (shadow.isTypeKind(TypeKind.RECORD_COMPONENT))
      {
         return Optional.of((RecordComponent) shadow);
      }
      return Optional.empty();
   }

   @Override
   public Optional<Record> toRecord()
   {
      if (shadow.isTypeKind(TypeKind.RECORD))
      {
         return Optional.of((Record) shadow);
      }
      return Optional.empty();
   }

   @Override
   public Optional<Generic> toGeneric()
   {
      if (shadow.getTypeKind().equals(TypeKind.GENERIC_TYPE))
      {
         return Optional.of((Generic) shadow);
      }
      return Optional.empty();
   }

   @Override
   public Optional<Variable> toVariable()
   {
      if (shadow.getTypeKind().isVariable())
      {
         return Optional.of((Variable) shadow);
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
      toRecord().ifPresent(consumer::recordType);
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
                                                                .orElse(toRecord()
                                                                              .map(mapper::recordType)
                                                                              .orElse((null))))));
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
