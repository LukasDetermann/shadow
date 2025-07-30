package io.determann.shadow.api.lang_model;

import io.determann.shadow.api.C;
import io.determann.shadow.api.query.ImplementationDefined;

import java.util.Objects;

import static io.determann.shadow.internal.lang_model.LangModelContextImpl.IMPLEMENTATION_NAME;
import static java.util.Objects.requireNonNull;

public interface Queries
{
   static LM.Nameable query(C.Nameable nameable)
   {
      return ((LM.Nameable) validate(nameable));
   }

   static LM.QualifiedNameable query(C.QualifiedNameable qualifiedNameable)
   {
      return ((LM.QualifiedNameable) validate(qualifiedNameable));
   }

   static LM.Wildcard query(C.Wildcard wildcard)
   {
      return ((LM.Wildcard) validate(wildcard));
   }

   static LM.Primitive query(C.Primitive primitive)
   {
      return ((LM.Primitive) validate(primitive));
   }

   static LM.Type query(C.Type type)
   {
      return ((LM.Type) validate(type));
   }

   static LM.Package query(C.Package aPackage)
   {
      return (LM.Package) validate(aPackage);
   }

   static LM.ModuleEnclosed query(C.ModuleEnclosed moduleEnclosed)
   {
      return ((LM.ModuleEnclosed) validate(moduleEnclosed));
   }

   static LM.Documented query(C.Documented documented)
   {
      return (LM.Documented) validate(documented);
   }

   static LM.Declared query(C.Declared declared)
   {
      return (LM.Declared) validate(declared);
   }

   static LM.Enum query(C.Enum anEnum)
   {
      return (LM.Enum) validate(anEnum);
   }

   static LM.Interface query(C.Interface anInterface)
   {
      return (LM.Interface) validate(anInterface);
   }

   static LM.Record query(C.Record record)
   {
      return (LM.Record) validate(record);
   }

   static LM.Class query(C.Class aClass)
   {
      return (LM.Class) validate(aClass);
   }

   static LM.Array query(C.Array array)
   {
      return (LM.Array) validate(array);
   }

   static LM.EnumConstant query(C.EnumConstant enumConstant)
   {
      return (LM.EnumConstant) validate(enumConstant);
   }

   static LM.Field query(C.Field field)
   {
      return (LM.Field) validate(field);
   }

   static LM.Parameter query(C.Parameter parameter)
   {
      return (LM.Parameter) validate(parameter);
   }

   static LM.Variable query(C.Variable variable)
   {
      return (LM.Variable) validate(variable);
   }

   static LM.Constructor query(C.Constructor constructor)
   {
      return (LM.Constructor) validate(constructor);
   }

   static LM.Executable query(C.Executable executable)
   {
      return (LM.Executable) validate(executable);
   }

   static LM.Method query(C.Method method)
   {
      return (LM.Method) validate(method);
   }

   static LM.RecordComponent query(C.RecordComponent recordComponent)
   {
      return (LM.RecordComponent) validate(recordComponent);
   }

   static LM.Result query(C.Result aReturn)
   {
      return (LM.Result) validate(aReturn);
   }

   static LM.Receiver query(C.Receiver receiver)
   {
      return (LM.Receiver) validate(receiver);
   }

   static LM.Generic query(C.Generic generic)
   {
      return (LM.Generic) validate(generic);
   }

   static LM.AnnotationUsage query(C.AnnotationUsage annotationUsage)
   {
      return (LM.AnnotationUsage) validate(annotationUsage);
   }

   static LM.Module query(C.Module module)
   {
      return (LM.Module) validate(module);
   }

   static LM.Annotation query(C.Annotation annotation)
   {
      return (LM.Annotation) validate(annotation);
   }

   static LM.Modifiable query(C.Modifiable modifiable)
   {
      return (LM.Modifiable) validate(modifiable);
   }

   static LM.AbstractModifiable query(C.AbstractModifiable abstractModifiable)
   {
      return (LM.AbstractModifiable) validate(abstractModifiable);
   }

   static LM.AccessModifiable query(C.AccessModifiable accessModifiable)
   {
      return (LM.AccessModifiable) validate(accessModifiable);
   }

   static LM.DefaultModifiable query(C.DefaultModifiable defaultModifiable)
   {
      return (LM.DefaultModifiable) validate(defaultModifiable);
   }

   static LM.FinalModifiable query(C.FinalModifiable finalModifiable)
   {
      return (LM.FinalModifiable) validate(finalModifiable);
   }

   static LM.NativeModifiable query(C.NativeModifiable nativeModifiable)
   {
      return (LM.NativeModifiable) validate(nativeModifiable);
   }

   static LM.Sealable query(C.Sealable sealable)
   {
      return (LM.Sealable) validate(sealable);
   }

   static LM.StaticModifiable query(C.StaticModifiable staticModifiable)
   {
      return (LM.StaticModifiable) validate(staticModifiable);
   }

   static LM.StrictfpModifiable query(C.StrictfpModifiable strictfpModifiable)
   {
      return (LM.StrictfpModifiable) validate(strictfpModifiable);
   }

   static LM.Exports query(C.Exports exports)
   {
      return (LM.Exports) validate(exports);
   }

   static LM.Opens query(C.Opens opens)
   {
      return (LM.Opens) validate(opens);
   }

   static LM.Provides query(C.Provides provides)
   {
      return (LM.Provides) validate(provides);
   }

   static LM.Requires query(C.Requires requires)
   {
      return (LM.Requires) validate(requires);
   }

   static LM.Uses query(C.Uses uses)
   {
      return (LM.Uses) validate(uses);
   }

   static LM.Annotationable query(C.Annotationable annotationable)
   {
      return (LM.Annotationable) validate(annotationable);
   }

   static LM.AnnotationValue query(C.AnnotationValue annotationValue)
   {
      return (LM.AnnotationValue) validate(annotationValue);
   }

   static LM.Property query(C.Property property)
   {
      return (LM.Property) validate(property);
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
