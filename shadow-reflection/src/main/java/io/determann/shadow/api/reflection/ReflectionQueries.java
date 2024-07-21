package io.determann.shadow.api.reflection;

import io.determann.shadow.api.ImplementationDefined;
import io.determann.shadow.api.reflection.shadow.AnnotationUsageReflection;
import io.determann.shadow.api.reflection.shadow.NameableReflection;
import io.determann.shadow.api.reflection.shadow.QualifiedNameableReflection;
import io.determann.shadow.api.reflection.shadow.modifier.*;
import io.determann.shadow.api.reflection.shadow.structure.*;
import io.determann.shadow.api.reflection.shadow.type.*;
import io.determann.shadow.api.shadow.ModuleEnclosed;
import io.determann.shadow.api.shadow.Nameable;
import io.determann.shadow.api.shadow.QualifiedNameable;
import io.determann.shadow.api.shadow.annotationusage.AnnotationUsage;
import io.determann.shadow.api.shadow.modifier.*;
import io.determann.shadow.api.shadow.structure.Module;
import io.determann.shadow.api.shadow.structure.Package;
import io.determann.shadow.api.shadow.structure.*;
import io.determann.shadow.api.shadow.type.Class;
import io.determann.shadow.api.shadow.type.Enum;
import io.determann.shadow.api.shadow.type.Record;
import io.determann.shadow.api.shadow.type.*;

import java.util.Objects;

import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;
import static java.util.Objects.requireNonNull;

public interface ReflectionQueries
{
   public static NameableReflection query(Nameable nameable)
   {
      return ((NameableReflection) validate(nameable));
   }

   public static QualifiedNameableReflection query(QualifiedNameable qualifiedNameable)
   {
      return ((QualifiedNameableReflection) validate(qualifiedNameable));
   }

   public static WildcardReflection query(Wildcard wildcard)
   {
      return ((WildcardReflection) validate(wildcard));
   }

   public static PrimitiveReflection query(Primitive primitive)
   {
      return ((PrimitiveReflection) validate(primitive));
   }

   public static ShadowReflection query(Shadow shadow)
   {
      return ((ShadowReflection) validate(shadow));
   }

   public static PackageReflection query(Package aPackage)
   {
      return (PackageReflection) validate(aPackage);
   }

   public static ModuleEnclosedReflection query(ModuleEnclosed moduleEnclosed)
   {
      return (ModuleEnclosedReflection) validate(moduleEnclosed);
   }

   public static DeclaredReflection query(Declared declared)
   {
      return (DeclaredReflection) validate(declared);
   }

   public static EnumReflection query(Enum anEnum)
   {
      return (EnumReflection) validate(anEnum);
   }

   public static InterfaceReflection query(Interface anInterface)
   {
      return (InterfaceReflection) validate(anInterface);
   }

   public static RecordReflection query(Record record)
   {
      return (RecordReflection) validate(record);
   }

   public static ClassReflection query(Class aClass)
   {
      return (ClassReflection) validate(aClass);
   }

   public static ArrayReflection query(Array array)
   {
      return (ArrayReflection) validate(array);
   }

   public static EnumConstantReflection query(EnumConstant enumConstant)
   {
      return (EnumConstantReflection) validate(enumConstant);
   }

   public static FieldReflection query(Field field)
   {
      return (FieldReflection) validate(field);
   }

   public static ParameterReflection query(Parameter parameter)
   {
      return (ParameterReflection) validate(parameter);
   }

   public static VariableReflection query(Variable variable)
   {
      return (VariableReflection) validate(variable);
   }

   public static ConstructorReflection query(Constructor constructor)
   {
      return (ConstructorReflection) validate(constructor);
   }

   public static ExecutableReflection query(Executable executable)
   {
      return (ExecutableReflection) validate(executable);
   }

   public static MethodReflection query(Method method)
   {
      return (MethodReflection) validate(method);
   }

   public static RecordComponentReflection query(RecordComponent recordComponent)
   {
      return (RecordComponentReflection) validate(recordComponent);
   }

   public static ReturnReflection query(Return aReturn)
   {
      return (ReturnReflection) validate(aReturn);
   }

   public static ReceiverReflection query(Receiver receiver)
   {
      return (ReceiverReflection) validate(receiver);
   }

   public static IntersectionReflection query(Intersection intersection)
   {
      return (IntersectionReflection) validate(intersection);
   }

   public static GenericReflection query(Generic generic)
   {
      return (GenericReflection) validate(generic);
   }

   public static AnnotationUsageReflection query(AnnotationUsage annotationUsage)
   {
      return (AnnotationUsageReflection) validate(annotationUsage);
   }

   public static ModuleReflection query(Module module)
   {
      return (ModuleReflection) validate(module);
   }

   public static AnnotationReflection query(Annotation annotation)
   {
      return (AnnotationReflection) validate(annotation);
   }

   public static ModifiableReflection query(Modifiable modifiable)
   {
      return (ModifiableReflection) validate(modifiable);
   }

   public static AbstractModifiableReflection query(AbstractModifiable abstractModifiable)
   {
      return (AbstractModifiableReflection) validate(abstractModifiable);
   }

   public static AccessModifiableReflection query(AccessModifiable accessModifiable)
   {
      return (AccessModifiableReflection) validate(accessModifiable);
   }

   public static DefaultModifiableReflection query(DefaultModifiable defaultModifiable)
   {
      return (DefaultModifiableReflection) validate(defaultModifiable);
   }

   public static FinalModifiableReflection query(FinalModifiable finalModifiable)
   {
      return (FinalModifiableReflection) validate(finalModifiable);
   }

   public static NativeModifiableReflection query(NativeModifiable nativeModifiable)
   {
      return (NativeModifiableReflection) validate(nativeModifiable);
   }

   public static SealableReflection query(Sealable sealable)
   {
      return (SealableReflection) validate(sealable);
   }

   public static StaticModifiableReflection query(StaticModifiable staticModifiable)
   {
      return (StaticModifiableReflection) validate(staticModifiable);
   }

   public static StrictfpModifiableReflection query(StrictfpModifiable strictfpModifiable)
   {
      return (StrictfpModifiableReflection) validate(strictfpModifiable);
   }

   private static <T extends ImplementationDefined> T validate(T toValidate)
   {
      if (!Objects.equals(requireNonNull(toValidate.getImplementationName()), IMPLEMENTATION_NAME))
      {
         throw new IllegalArgumentException("Tried to use \"" + IMPLEMENTATION_NAME + " \" to query \"" + toValidate + "\" based on \"" + toValidate.getImplementationName() + "\"");
      }
      return toValidate;
   }
}
