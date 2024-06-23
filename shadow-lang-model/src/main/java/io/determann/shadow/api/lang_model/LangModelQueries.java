package io.determann.shadow.api.lang_model;

import io.determann.shadow.api.*;
import io.determann.shadow.api.lang_model.query.*;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Enum;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.Record;
import io.determann.shadow.api.shadow.*;

import java.util.Objects;

import static io.determann.shadow.internal.lang_model.LangModelProvider.IMPLEMENTATION_NAME;
import static java.util.Objects.requireNonNull;

public interface LangModelQueries
{
   public static NameableLangModel query(Nameable nameable)
   {
      return ((NameableLangModel) validate(nameable));
   }

   public static QualifiedNameableLamgModel query(QualifiedNameable qualifiedNameable)
   {
      return ((QualifiedNameableLamgModel) validate(qualifiedNameable));
   }

   public static WildcardLangModel query(Wildcard wildcard)
   {
      return ((WildcardLangModel) validate(wildcard));
   }

   public static PrimitiveLangModel query(Primitive primitive)
   {
      return ((PrimitiveLangModel) validate(primitive));
   }

   public static ShadowLangModel query(Shadow shadow)
   {
      return ((ShadowLangModel) validate(shadow));
   }

   public static PackageLangModel query(Package aPackage)
   {
      return (PackageLangModel) validate(aPackage);
   }

   public static ModuleEnclosedLangModel query(ModuleEnclosed moduleEnclosed)
   {
      return ((ModuleEnclosedLangModel) validate(moduleEnclosed));
   }

   public static DocumentedLangModel query(Documented documented)
   {
      return (DocumentedLangModel) validate(documented);
   }

   public static DeclaredLangModel query(Declared declared)
   {
      return (DeclaredLangModel) validate(declared);
   }

   public static EnumLangModel query(Enum anEnum)
   {
      return (EnumLangModel) validate(anEnum);
   }

   public static InterfaceLangModel query(Interface anInterface)
   {
      return (InterfaceLangModel) validate(anInterface);
   }

   public static RecordLangModel query(Record record)
   {
      return (RecordLangModel) validate(record);
   }

   public static ClassLangModel query(Class aClass)
   {
      return (ClassLangModel) validate(aClass);
   }

   public static ArrayLangModel query(Array array)
   {
      return (ArrayLangModel) validate(array);
   }

   public static EnumConstantLangModel query(EnumConstant enumConstant)
   {
      return (EnumConstantLangModel) validate(enumConstant);
   }

   public static FieldLangModel query(Field field)
   {
      return (FieldLangModel) validate(field);
   }

   public static ParameterLangModel query(Parameter parameter)
   {
      return (ParameterLangModel) validate(parameter);
   }

   public static VariableLangModel query(Variable variable)
   {
      return (VariableLangModel) validate(variable);
   }

   public static ConstructorLangModel query(Constructor constructor)
   {
      return (ConstructorLangModel) validate(constructor);
   }

   public static ExecutableLangModel query(Executable executable)
   {
      return (ExecutableLangModel) validate(executable);
   }

   public static MethodLangMethod query(Method method)
   {
      return (MethodLangMethod) validate(method);
   }

   public static RecordComponentLangModel query(RecordComponent recordComponent)
   {
      return (RecordComponentLangModel) validate(recordComponent);
   }

   public static ReturnLangModel query(Return aReturn)
   {
      return (ReturnLangModel) validate(aReturn);
   }

   public static ReceiverLangModel query(Receiver receiver)
   {
      return (ReceiverLangModel) validate(receiver);
   }

   public static IntersectionLangModel query(Intersection intersection)
   {
      return (IntersectionLangModel) validate(intersection);
   }

   public static GenericLangModel query(Generic generic)
   {
      return (GenericLangModel) validate(generic);
   }

   public static AnnotationUsageLangModel query(AnnotationUsage annotationUsage)
   {
      return (AnnotationUsageLangModel) validate(annotationUsage);
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
