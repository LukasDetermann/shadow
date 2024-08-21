package io.determann.shadow.api.lang_model;

import io.determann.shadow.api.ImplementationDefined;
import io.determann.shadow.api.lang_model.shadow.*;
import io.determann.shadow.api.lang_model.shadow.directive.*;
import io.determann.shadow.api.lang_model.shadow.modifier.*;
import io.determann.shadow.api.lang_model.shadow.structure.*;
import io.determann.shadow.api.lang_model.shadow.type.*;
import io.determann.shadow.api.shadow.*;
import io.determann.shadow.api.shadow.directive.*;
import io.determann.shadow.api.shadow.modifier.*;
import io.determann.shadow.api.shadow.structure.*;
import io.determann.shadow.api.shadow.type.*;

import java.util.Objects;

import static io.determann.shadow.internal.lang_model.LangModelProvider.IMPLEMENTATION_NAME;
import static java.util.Objects.requireNonNull;

public interface LM_Queries
{
   public static LM_Nameable query(C_Nameable nameable)
   {
      return ((LM_Nameable) validate(nameable));
   }

   public static LM_QualifiedNameable query(C_QualifiedNameable qualifiedNameable)
   {
      return ((LM_QualifiedNameable) validate(qualifiedNameable));
   }

   public static LM_Wildcard query(C_Wildcard wildcard)
   {
      return ((LM_Wildcard) validate(wildcard));
   }

   public static LM_Primitive query(C_Primitive primitive)
   {
      return ((LM_Primitive) validate(primitive));
   }

   public static LM_Shadow query(C_Shadow shadow)
   {
      return ((LM_Shadow) validate(shadow));
   }

   public static LM_Package query(C_Package aPackage)
   {
      return (LM_Package) validate(aPackage);
   }

   public static LM_ModuleEnclosed query(C_ModuleEnclosed moduleEnclosed)
   {
      return ((LM_ModuleEnclosed) validate(moduleEnclosed));
   }

   public static LM_Documented query(C_Documented documented)
   {
      return (LM_Documented) validate(documented);
   }

   public static LM_Declared query(C_Declared declared)
   {
      return (LM_Declared) validate(declared);
   }

   public static LM_Enum query(C_Enum anEnum)
   {
      return (LM_Enum) validate(anEnum);
   }

   public static LM_Interface query(C_Interface anInterface)
   {
      return (LM_Interface) validate(anInterface);
   }

   public static LM_Record query(C_Record record)
   {
      return (LM_Record) validate(record);
   }

   public static LM_Class query(C_Class aClass)
   {
      return (LM_Class) validate(aClass);
   }

   public static LM_Array query(C_Array array)
   {
      return (LM_Array) validate(array);
   }

   public static LM_EnumConstant query(C_EnumConstant enumConstant)
   {
      return (LM_EnumConstant) validate(enumConstant);
   }

   public static LM_Field query(C_Field field)
   {
      return (LM_Field) validate(field);
   }

   public static LM_Parameter query(C_Parameter parameter)
   {
      return (LM_Parameter) validate(parameter);
   }

   public static LM_Variable query(C_Variable variable)
   {
      return (LM_Variable) validate(variable);
   }

   public static LM_Constructor query(C_Constructor constructor)
   {
      return (LM_Constructor) validate(constructor);
   }

   public static LM_Executable query(C_Executable executable)
   {
      return (LM_Executable) validate(executable);
   }

   public static LM_Method query(C_Method method)
   {
      return (LM_Method) validate(method);
   }

   public static LM_RecordComponent query(C_RecordComponent recordComponent)
   {
      return (LM_RecordComponent) validate(recordComponent);
   }

   public static LM_Return query(C_Return aReturn)
   {
      return (LM_Return) validate(aReturn);
   }

   public static LM_Receiver query(C_Receiver receiver)
   {
      return (LM_Receiver) validate(receiver);
   }

   public static LM_Intersection query(C_Intersection intersection)
   {
      return (LM_Intersection) validate(intersection);
   }

   public static LM_Generic query(C_Generic generic)
   {
      return (LM_Generic) validate(generic);
   }

   public static LM_AnnotationUsage query(C_AnnotationUsage annotationUsage)
   {
      return (LM_AnnotationUsage) validate(annotationUsage);
   }

   public static LM_Module query(C_Module module)
   {
      return (LM_Module) validate(module);
   }

   public static LM_Annotation query(C_Annotation annotation)
   {
      return (LM_Annotation) validate(annotation);
   }

   public static LM_Modifiable query(C_Modifiable modifiable)
   {
      return (LM_Modifiable) validate(modifiable);
   }

   public static LM_AbstractModifiable query(C_AbstractModifiable abstractModifiable)
   {
      return (LM_AbstractModifiable) validate(abstractModifiable);
   }

   public static LM_AccessModifiable query(C_AccessModifiable accessModifiable)
   {
      return (LM_AccessModifiable) validate(accessModifiable);
   }

   public static LM_DefaultModifiable query(C_DefaultModifiable defaultModifiable)
   {
      return (LM_DefaultModifiable) validate(defaultModifiable);
   }

   public static LM_FinalModifiable query(C_FinalModifiable finalModifiable)
   {
      return (LM_FinalModifiable) validate(finalModifiable);
   }

   public static LM_NativeModifiable query(C_NativeModifiable nativeModifiable)
   {
      return (LM_NativeModifiable) validate(nativeModifiable);
   }

   public static LM_Sealable query(C_Sealable sealable)
   {
      return (LM_Sealable) validate(sealable);
   }

   public static LM_StaticModifiable query(C_StaticModifiable staticModifiable)
   {
      return (LM_StaticModifiable) validate(staticModifiable);
   }

   public static LM_StrictfpModifiable query(C_StrictfpModifiable strictfpModifiable)
   {
      return (LM_StrictfpModifiable) validate(strictfpModifiable);
   }

   public static LM_Exports query(C_Exports exports)
   {
      return (LM_Exports) validate(exports);
   }

   public static LM_Opens query(C_Opens opens)
   {
      return (LM_Opens) validate(opens);
   }

   public static LM_Provides query(C_Provides provides)
   {
      return (LM_Provides) validate(provides);
   }

   public static LM_Requires query(C_Requires requires)
   {
      return (LM_Requires) validate(requires);
   }

   public static LM_Uses query(C_Uses uses)
   {
      return (LM_Uses) validate(uses);
   }

   public static LM_Annotationable query(C_Annotationable annotationable)
   {
      return (LM_Annotationable) validate(annotationable);
   }

   public static LM_AnnotationValue query(C_AnnotationValue annotationValue)
   {
      return (LM_AnnotationValue) validate(annotationValue);
   }

   public static LM_Property query(C_Property property)
   {
      return (LM_Property) validate(property);
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
