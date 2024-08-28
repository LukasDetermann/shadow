package io.determann.shadow.api.reflection;

import io.determann.shadow.api.ImplementationDefined;
import io.determann.shadow.api.reflection.shadow.*;
import io.determann.shadow.api.reflection.shadow.directive.*;
import io.determann.shadow.api.reflection.shadow.modifier.*;
import io.determann.shadow.api.reflection.shadow.structure.*;
import io.determann.shadow.api.reflection.shadow.type.*;
import io.determann.shadow.api.reflection.shadow.type.primitive.R_Primitive;
import io.determann.shadow.api.shadow.*;
import io.determann.shadow.api.shadow.directive.*;
import io.determann.shadow.api.shadow.modifier.*;
import io.determann.shadow.api.shadow.structure.*;
import io.determann.shadow.api.shadow.type.*;
import io.determann.shadow.api.shadow.type.primitive.C_Primitive;

import java.util.Objects;

import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;
import static java.util.Objects.requireNonNull;

public interface R_Queries
{
   public static R_Nameable query(C_Nameable nameable)
   {
      return ((R_Nameable) validate(nameable));
   }

   public static R_QualifiedNameable query(C_QualifiedNameable qualifiedNameable)
   {
      return ((R_QualifiedNameable) validate(qualifiedNameable));
   }

   public static R_Wildcard query(C_Wildcard wildcard)
   {
      return ((R_Wildcard) validate(wildcard));
   }

   public static R_Primitive query(C_Primitive primitive)
   {
      return ((R_Primitive) validate(primitive));
   }

   public static R_Type query(C_Type type)
   {
      return ((R_Type) validate(type));
   }

   public static R_Package query(C_Package aPackage)
   {
      return (R_Package) validate(aPackage);
   }

   public static R_ModuleEnclosed query(C_ModuleEnclosed moduleEnclosed)
   {
      return (R_ModuleEnclosed) validate(moduleEnclosed);
   }

   public static R_Declared query(C_Declared declared)
   {
      return (R_Declared) validate(declared);
   }

   public static R_Enum query(C_Enum anEnum)
   {
      return (R_Enum) validate(anEnum);
   }

   public static R_Interface query(C_Interface anInterface)
   {
      return (R_Interface) validate(anInterface);
   }

   public static R_Record query(C_Record record)
   {
      return (R_Record) validate(record);
   }

   public static R_Class query(C_Class aClass)
   {
      return (R_Class) validate(aClass);
   }

   public static R_Array query(C_Array array)
   {
      return (R_Array) validate(array);
   }

   public static R_EnumConstant query(C_EnumConstant enumConstant)
   {
      return (R_EnumConstant) validate(enumConstant);
   }

   public static R_Field query(C_Field field)
   {
      return (R_Field) validate(field);
   }

   public static R_Parameter query(C_Parameter parameter)
   {
      return (R_Parameter) validate(parameter);
   }

   public static R_Variable query(C_Variable variable)
   {
      return (R_Variable) validate(variable);
   }

   public static R_Constructor query(C_Constructor constructor)
   {
      return (R_Constructor) validate(constructor);
   }

   public static R_Executable query(C_Executable executable)
   {
      return (R_Executable) validate(executable);
   }

   public static R_Method query(C_Method method)
   {
      return (R_Method) validate(method);
   }

   public static R_RecordComponent query(C_RecordComponent recordComponent)
   {
      return (R_RecordComponent) validate(recordComponent);
   }

   public static R_Return query(C_Return aReturn)
   {
      return (R_Return) validate(aReturn);
   }

   public static R_Receiver query(C_Receiver receiver)
   {
      return (R_Receiver) validate(receiver);
   }

   public static R_Intersection query(C_Intersection intersection)
   {
      return (R_Intersection) validate(intersection);
   }

   public static R_Generic query(C_Generic generic)
   {
      return (R_Generic) validate(generic);
   }

   public static R_AnnotationUsage query(C_AnnotationUsage annotationUsage)
   {
      return (R_AnnotationUsage) validate(annotationUsage);
   }

   public static R_Module query(C_Module module)
   {
      return (R_Module) validate(module);
   }

   public static R_Annotation query(C_Annotation annotation)
   {
      return (R_Annotation) validate(annotation);
   }

   public static R_Modifiable query(C_Modifiable modifiable)
   {
      return (R_Modifiable) validate(modifiable);
   }

   public static R_AbstractModifiable query(C_AbstractModifiable abstractModifiable)
   {
      return (R_AbstractModifiable) validate(abstractModifiable);
   }

   public static R_AccessModifiable query(C_AccessModifiable accessModifiable)
   {
      return (R_AccessModifiable) validate(accessModifiable);
   }

   public static R_DefaultModifiable query(C_DefaultModifiable defaultModifiable)
   {
      return (R_DefaultModifiable) validate(defaultModifiable);
   }

   public static R_FinalModifiable query(C_FinalModifiable finalModifiable)
   {
      return (R_FinalModifiable) validate(finalModifiable);
   }

   public static R_NativeModifiable query(C_NativeModifiable nativeModifiable)
   {
      return (R_NativeModifiable) validate(nativeModifiable);
   }

   public static R_Sealable query(C_Sealable sealable)
   {
      return (R_Sealable) validate(sealable);
   }

   public static R_StaticModifiable query(C_StaticModifiable staticModifiable)
   {
      return (R_StaticModifiable) validate(staticModifiable);
   }

   public static R_StrictfpModifiable query(C_StrictfpModifiable strictfpModifiable)
   {
      return (R_StrictfpModifiable) validate(strictfpModifiable);
   }

   public static R_Exports query(C_Exports exports)
   {
      return (R_Exports) validate(exports);
   }

   public static R_Opens query(C_Opens opens)
   {
      return (R_Opens) validate(opens);
   }

   public static R_Provides query(C_Provides provides)
   {
      return (R_Provides) validate(provides);
   }

   public static R_Requires query(C_Requires requires)
   {
      return (R_Requires) validate(requires);
   }

   public static R_Uses query(C_Uses uses)
   {
      return (R_Uses) validate(uses);
   }

   public static R_Annotationable query(C_Annotationable annotationable)
   {
      return (R_Annotationable) validate(annotationable);
   }

   public static R_AnnotationValue query(C_AnnotationValue annotationValue)
   {
      return (R_AnnotationValue) validate(annotationValue);
   }

   public static R_Property query(C_Property property)
   {
      return (R_Property) validate(property);
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
