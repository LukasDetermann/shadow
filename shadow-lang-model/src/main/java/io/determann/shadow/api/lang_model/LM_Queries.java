package io.determann.shadow.api.lang_model;

import io.determann.shadow.api.ImplementationDefined;
import io.determann.shadow.api.lang_model.shadow.*;
import io.determann.shadow.api.lang_model.shadow.directive.*;
import io.determann.shadow.api.lang_model.shadow.modifier.*;
import io.determann.shadow.api.lang_model.shadow.structure.*;
import io.determann.shadow.api.lang_model.shadow.type.*;
import io.determann.shadow.api.lang_model.shadow.type.primitive.LM_Primitive;
import io.determann.shadow.api.shadow.*;
import io.determann.shadow.api.shadow.directive.*;
import io.determann.shadow.api.shadow.modifier.*;
import io.determann.shadow.api.shadow.structure.*;
import io.determann.shadow.api.shadow.type.*;
import io.determann.shadow.api.shadow.type.primitive.C_Primitive;

import java.util.Objects;

import static io.determann.shadow.internal.lang_model.LangModelContextImpl.IMPLEMENTATION_NAME;
import static java.util.Objects.requireNonNull;

public interface LM_Queries
{
   static LM_Nameable query(C_Nameable nameable)
   {
      return ((LM_Nameable) validate(nameable));
   }

   static LM_QualifiedNameable query(C_QualifiedNameable qualifiedNameable)
   {
      return ((LM_QualifiedNameable) validate(qualifiedNameable));
   }

   static LM_Wildcard query(C_Wildcard wildcard)
   {
      return ((LM_Wildcard) validate(wildcard));
   }

   static LM_Primitive query(C_Primitive primitive)
   {
      return ((LM_Primitive) validate(primitive));
   }

   static LM_Type query(C_Type type)
   {
      return ((LM_Type) validate(type));
   }

   static LM_Package query(C_Package aPackage)
   {
      return (LM_Package) validate(aPackage);
   }

   static LM_ModuleEnclosed query(C_ModuleEnclosed moduleEnclosed)
   {
      return ((LM_ModuleEnclosed) validate(moduleEnclosed));
   }

   static LM_Documented query(C_Documented documented)
   {
      return (LM_Documented) validate(documented);
   }

   static LM_Declared query(C_Declared declared)
   {
      return (LM_Declared) validate(declared);
   }

   static LM_Enum query(C_Enum anEnum)
   {
      return (LM_Enum) validate(anEnum);
   }

   static LM_Interface query(C_Interface anInterface)
   {
      return (LM_Interface) validate(anInterface);
   }

   static LM_Record query(C_Record record)
   {
      return (LM_Record) validate(record);
   }

   static LM_Class query(C_Class aClass)
   {
      return (LM_Class) validate(aClass);
   }

   static LM_Array query(C_Array array)
   {
      return (LM_Array) validate(array);
   }

   static LM_EnumConstant query(C_EnumConstant enumConstant)
   {
      return (LM_EnumConstant) validate(enumConstant);
   }

   static LM_Field query(C_Field field)
   {
      return (LM_Field) validate(field);
   }

   static LM_Parameter query(C_Parameter parameter)
   {
      return (LM_Parameter) validate(parameter);
   }

   static LM_Variable query(C_Variable variable)
   {
      return (LM_Variable) validate(variable);
   }

   static LM_Constructor query(C_Constructor constructor)
   {
      return (LM_Constructor) validate(constructor);
   }

   static LM_Executable query(C_Executable executable)
   {
      return (LM_Executable) validate(executable);
   }

   static LM_Method query(C_Method method)
   {
      return (LM_Method) validate(method);
   }

   static LM_RecordComponent query(C_RecordComponent recordComponent)
   {
      return (LM_RecordComponent) validate(recordComponent);
   }

   static LM_Result query(C_Result aReturn)
   {
      return (LM_Result) validate(aReturn);
   }

   static LM_Receiver query(C_Receiver receiver)
   {
      return (LM_Receiver) validate(receiver);
   }

   static LM_Intersection query(C_Intersection intersection)
   {
      return (LM_Intersection) validate(intersection);
   }

   static LM_Generic query(C_Generic generic)
   {
      return (LM_Generic) validate(generic);
   }

   static LM_AnnotationUsage query(C_AnnotationUsage annotationUsage)
   {
      return (LM_AnnotationUsage) validate(annotationUsage);
   }

   static LM_Module query(C_Module module)
   {
      return (LM_Module) validate(module);
   }

   static LM_Annotation query(C_Annotation annotation)
   {
      return (LM_Annotation) validate(annotation);
   }

   static LM_Modifiable query(C_Modifiable modifiable)
   {
      return (LM_Modifiable) validate(modifiable);
   }

   static LM_AbstractModifiable query(C_AbstractModifiable abstractModifiable)
   {
      return (LM_AbstractModifiable) validate(abstractModifiable);
   }

   static LM_AccessModifiable query(C_AccessModifiable accessModifiable)
   {
      return (LM_AccessModifiable) validate(accessModifiable);
   }

   static LM_DefaultModifiable query(C_DefaultModifiable defaultModifiable)
   {
      return (LM_DefaultModifiable) validate(defaultModifiable);
   }

   static LM_FinalModifiable query(C_FinalModifiable finalModifiable)
   {
      return (LM_FinalModifiable) validate(finalModifiable);
   }

   static LM_NativeModifiable query(C_NativeModifiable nativeModifiable)
   {
      return (LM_NativeModifiable) validate(nativeModifiable);
   }

   static LM_Sealable query(C_Sealable sealable)
   {
      return (LM_Sealable) validate(sealable);
   }

   static LM_StaticModifiable query(C_StaticModifiable staticModifiable)
   {
      return (LM_StaticModifiable) validate(staticModifiable);
   }

   static LM_StrictfpModifiable query(C_StrictfpModifiable strictfpModifiable)
   {
      return (LM_StrictfpModifiable) validate(strictfpModifiable);
   }

   static LM_Exports query(C_Exports exports)
   {
      return (LM_Exports) validate(exports);
   }

   static LM_Opens query(C_Opens opens)
   {
      return (LM_Opens) validate(opens);
   }

   static LM_Provides query(C_Provides provides)
   {
      return (LM_Provides) validate(provides);
   }

   static LM_Requires query(C_Requires requires)
   {
      return (LM_Requires) validate(requires);
   }

   static LM_Uses query(C_Uses uses)
   {
      return (LM_Uses) validate(uses);
   }

   static LM_Annotationable query(C_Annotationable annotationable)
   {
      return (LM_Annotationable) validate(annotationable);
   }

   static LM_AnnotationValue query(C_AnnotationValue annotationValue)
   {
      return (LM_AnnotationValue) validate(annotationValue);
   }

   static LM_Property query(C_Property property)
   {
      return (LM_Property) validate(property);
   }

   private static <T extends ImplementationDefined> T validate(T toValidate)
   {
      if (!Objects.equals(requireNonNull(toValidate.getImplementation().getName()), IMPLEMENTATION_NAME))
      {
         throw new IllegalArgumentException("Tried to use \"" +
                                            IMPLEMENTATION_NAME +
                                            " \" to query \"" +
                                            toValidate +
                                            "\" based on \"" +
                                            toValidate.getImplementation() +
                                            "\"");
      }
      return toValidate;
   }
}
