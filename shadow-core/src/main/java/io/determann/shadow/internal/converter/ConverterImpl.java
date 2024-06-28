package io.determann.shadow.internal.converter;

import io.determann.shadow.api.converter.*;
import io.determann.shadow.api.shadow.structure.Module;
import io.determann.shadow.api.shadow.structure.Package;
import io.determann.shadow.api.shadow.structure.*;
import io.determann.shadow.api.shadow.type.Class;
import io.determann.shadow.api.shadow.type.Enum;
import io.determann.shadow.api.shadow.type.Record;
import io.determann.shadow.api.shadow.type.Void;
import io.determann.shadow.api.shadow.type.*;

import java.util.Optional;

public class ConverterImpl implements AnnotationableConverter,
                                      TypeConverter,
                                      AnnotationConverter,
                                      ArrayConverter,
                                      ClassConverter,
                                      ConstructorConverter,
                                      DeclaredConverter,
                                      EnumConstantConverter,
                                      EnumConverter,
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
   private final Object object;

   public ConverterImpl(Object type)
   {
      this.object = type;
   }

   @Override
   public Annotation toAnnotationOrThrow()
   {
      return to(Annotation.class);
   }

   @Override
   public Optional<Annotation> toAnnotation()
   {
      return toOptional(Annotation.class);
   }

   @Override
   public Array toArrayOrThrow()
   {
      return to(Array.class);
   }

   @Override
   public Optional<Array> toArray()
   {
      return toOptional(Array.class);
   }

   @Override
   public Class toClassOrThrow()
   {
      return to(Class.class);
   }

   @Override
   public Optional<Class> toClass()
   {
      return toOptional(Class.class);
   }

   @Override
   public Constructor toConstructorOrThrow()
   {
      return to(Constructor.class);
   }

   @Override
   public Optional<Constructor> toConstructor()
   {
      return toOptional(Constructor.class);
   }

   @Override
   public Declared toDeclaredOrThrow()
   {
      return to(Declared.class);
   }

   @Override
   public Optional<Declared> toDeclared()
   {
      return toOptional(Declared.class);
   }

   @Override
   public EnumConstant toEnumConstantOrThrow()
   {
      return to(EnumConstant.class);
   }

   @Override
   public Optional<EnumConstant> toEnumConstant()
   {
      return toOptional(EnumConstant.class);
   }

   @Override
   public Enum toEnumOrThrow()
   {
      return to(Enum.class);
   }

   @Override
   public Optional<Enum> toEnum()
   {
      return toOptional(Enum.class);
   }

   @Override
   public Executable toExecutableOrThrow()
   {
      return to(Executable.class);
   }

   @Override
   public Optional<Executable> toExecutable()
   {
      return toOptional(Executable.class);
   }

   @Override
   public Field toFieldOrThrow()
   {
      return to(Field.class);
   }

   @Override
   public Optional<Field> toField()
   {
      return toOptional(Field.class);
   }

   @Override
   public Interface toInterfaceOrThrow()
   {
      return to(Interface.class);
   }

   @Override
   public Optional<Interface> toInterface()
   {
      return toOptional(Interface.class);
   }

   @Override
   public Intersection toIntersectionOrThrow()
   {
      return to(Intersection.class);
   }

   @Override
   public Optional<Intersection> toIntersection()
   {
      return toOptional(Intersection.class);
   }

   @Override
   public Method toMethodOrThrow()
   {
      return to(Method.class);
   }

   @Override
   public Optional<Method> toMethod()
   {
      return toOptional(Method.class);
   }

   @Override
   public Module toModuleOrThrow()
   {
      return to(Module.class);
   }

   @Override
   public Optional<Module> toModule()
   {
      return toOptional(Module.class);
   }

   @Override
   public Void toVoidOrThrow()
   {
      return to(Void.class);
   }

   @Override
   public Optional<Package> toPackage()
   {
      return toOptional(Package.class);
   }

   @Override
   public Parameter toParameterOrThrow()
   {
      return to(Parameter.class);
   }

   @Override
   public Optional<Parameter> toParameter()
   {
      return toOptional(Parameter.class);
   }

   @Override
   public Receiver toReceiverOrThrow()
   {
      return to(Receiver.class);
   }

   @Override
   public Optional<Receiver> toReceiver()
   {
      return toOptional(Receiver.class);
   }

   @Override
   public Primitive toPrimitiveOrThrow()
   {
      return to(Primitive.class);
   }

   @Override
   public Optional<Void> toVoid()
   {
      return toOptional(Void.class);
   }

   @Override
   public Null toNullOrThrow()
   {
      return to(Null.class);
   }

   @Override
   public Optional<Null> toNull()
   {
      return toOptional(Null.class);
   }

   @Override
   public Package toPackageOrThrow()
   {
      return to(Package.class);
   }

   @Override
   public Optional<Primitive> toPrimitive()
   {
      return toOptional(Primitive.class);
   }

   @Override
   public RecordComponent toRecordComponentOrThrow()
   {
      return to(RecordComponent.class);
   }

   @Override
   public Optional<RecordComponent> toRecordComponent()
   {
      return toOptional(RecordComponent.class);
   }

   @Override
   public Return toReturnOrThrow()
   {
      return to(Return.class);
   }

   @Override
   public Optional<Return> toReturn()
   {
      return toOptional(Return.class);
   }

   @Override
   public Record toRecordOrThrow()
   {
      return to(Record.class);
   }

   @Override
   public Optional<Record> toRecord()
   {
      return toOptional(Record.class);
   }

   @Override
   public Generic toGenericOrThrow()
   {
      return to(Generic.class);
   }

   @Override
   public Optional<Generic> toGeneric()
   {
      return toOptional(Generic.class);
   }

   @Override
   public Variable toVariableOrThrow()
   {
      return to(Variable.class);
   }

   @Override
   public Optional<Variable> toVariable()
   {
      return toOptional(Variable.class);
   }

   @Override
   public Wildcard toWildcardOrThrow()
   {
      return to(Wildcard.class);
   }

   @Override
   public Optional<Wildcard> toWildcard()
   {
      return toOptional(Wildcard.class);
   }

   private <T> T to(java.lang.Class<T> aClass)
   {
      if (object == null)
      {
         return null;
      }
      if (!(aClass.isAssignableFrom(object.getClass())))
      {
         throw new IllegalStateException(object + " is not a subtype of "+ aClass);
      }
      return aClass.cast(object);
   }

   private <T>  Optional<T> toOptional(java.lang.Class<T> aClass)
   {
      if (object == null)
      {
         return Optional.empty();
      }
      if (!(aClass.isAssignableFrom(object.getClass())))
      {
         return Optional.empty();
      }
      return Optional.of(aClass.cast(object));
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
