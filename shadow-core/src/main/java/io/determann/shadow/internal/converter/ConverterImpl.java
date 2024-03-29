package io.determann.shadow.internal.converter;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.converter.*;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Enum;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.Record;
import io.determann.shadow.api.shadow.Void;
import io.determann.shadow.api.shadow.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

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
   private final Shadow shadow;

   public ConverterImpl(Shadow shadow)
   {
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
   public RecordComponent toRecordComponentOrThrow()
   {
      return to(TypeKind.RECORD_COMPONENT);
   }

   @Override
   public Optional<RecordComponent> toRecordComponent()
   {
      return toOptional(TypeKind.RECORD_COMPONENT);
   }

   @Override
   public Record toRecordOrThrow()
   {
      return to(TypeKind.RECORD);
   }

   @Override
   public Optional<Record> toRecord()
   {
      return toOptional(TypeKind.RECORD);
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
   public Variable<Shadow> toVariableOrThrow()
   {
      return to(TypeKind::isVariable);
   }

   @Override
   public Optional<Variable<Shadow>> toVariable()
   {
      return toOptional(TypeKind::isVariable);
   }

   @Override
   public Wildcard toWildcardOrThrow()
   {
      return to(TypeKind.WILDCARD);
   }

   private <SHADOW extends Shadow> SHADOW to(TypeKind typeKind)
   {
      //noinspection unchecked
      return (SHADOW) toOptional(typeKind).orElseThrow(() -> new IllegalStateException(shadow.getTypeKind() + " is not a " + typeKind));
   }

   private <SHADOW extends Shadow> SHADOW to(Predicate<TypeKind> typeKindPredicate)
   {
      List<TypeKind> typeKinds = Arrays.stream(TypeKind.values()).filter(typeKindPredicate).toList();
      //noinspection unchecked
      return (SHADOW) toOptional(typeKindPredicate)
            .orElseThrow(() -> new IllegalStateException(shadow.getTypeKind() + " is none of " + typeKinds));
   }

   private <SHADOW extends Shadow> Optional<SHADOW> toOptional(TypeKind typeKind)
   {
      return toOptional(typeKind1 -> typeKind1.equals(typeKind));
   }

   private <SHADOW extends Shadow> Optional<SHADOW> toOptional(Predicate<TypeKind> typeKindPredicate)
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
   public void consume(DeclaredConsumer consumer)
   {
      toClass().ifPresent(consumer::classType);
      toInterface().ifPresent(consumer::interfaceType);
      toEnum().ifPresent(consumer::enumType);
      toAnnotation().ifPresent(consumer::annotationType);
      toRecord().ifPresent(consumer::recordType);
   }

   @Override
   public <T> T map(DeclaredMapper<T> mapper)
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
   public void consume(ExecutableConsumer consumer)
   {
      toMethod().ifPresent(consumer::method);
      toConstructor().ifPresent(consumer::constructor);
   }

   @Override
   public <T> T map(ExecutableMapper<T> mapper)
   {
      return toMethod().map(mapper::method)
                       .orElse(toConstructor()
                                     .map(mapper::constructor)
                                     .orElse(null));
   }

   @Override
   public void consume(VariableConsumer consumer)
   {
      toEnumConstant().ifPresent(consumer::enumConstant);
      toField().ifPresent(consumer::field);
      toParameter().ifPresent(consumer::parameter);
   }

   @Override
   public <T> T map(VariableMapper<T> mapper)
   {
      return toEnumConstant().map(mapper::enumConstant)
                             .orElse(toField()
                                           .map(mapper::field)
                                           .orElse(toParameter()
                                                         .map(mapper::parameter)
                                                         .orElse(null)));
   }
}
