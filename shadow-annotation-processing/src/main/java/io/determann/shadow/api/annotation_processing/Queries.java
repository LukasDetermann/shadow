package io.determann.shadow.api.annotation_processing;

import io.determann.shadow.api.C;
import io.determann.shadow.api.query.ImplementationDefined;

import java.util.Objects;

import static io.determann.shadow.internal.annotation_processing.ApContextImpl.IMPLEMENTATION_NAME;
import static java.util.Objects.requireNonNull;

public interface Queries
{
   static AP.Nameable query(C.Nameable nameable)
   {
      return ((AP.Nameable) validate(nameable));
   }

   static AP.QualifiedNameable query(C.QualifiedNameable qualifiedNameable)
   {
      return ((AP.QualifiedNameable) validate(qualifiedNameable));
   }

   static AP.Wildcard query(C.Wildcard wildcard)
   {
      return ((AP.Wildcard) validate(wildcard));
   }

   static AP.Primitive query(C.Primitive primitive)
   {
      return ((AP.Primitive) validate(primitive));
   }

   static AP.Type query(C.Type type)
   {
      return ((AP.Type) validate(type));
   }

   static AP.Package query(C.Package aPackage)
   {
      return (AP.Package) validate(aPackage);
   }

   static AP.ModuleEnclosed query(C.ModuleEnclosed moduleEnclosed)
   {
      return ((AP.ModuleEnclosed) validate(moduleEnclosed));
   }

   static AP.Documented query(C.Documented documented)
   {
      return (AP.Documented) validate(documented);
   }

   static AP.Declared query(C.Declared declared)
   {
      return (AP.Declared) validate(declared);
   }

   static AP.Enum query(C.Enum anEnum)
   {
      return (AP.Enum) validate(anEnum);
   }

   static AP.Interface query(C.Interface anInterface)
   {
      return (AP.Interface) validate(anInterface);
   }

   static AP.Record query(C.Record record)
   {
      return (AP.Record) validate(record);
   }

   static AP.Class query(C.Class aClass)
   {
      return (AP.Class) validate(aClass);
   }

   static AP.Array query(C.Array array)
   {
      return (AP.Array) validate(array);
   }

   static AP.EnumConstant query(C.EnumConstant enumConstant)
   {
      return (AP.EnumConstant) validate(enumConstant);
   }

   static AP.Field query(C.Field field)
   {
      return (AP.Field) validate(field);
   }

   static AP.Parameter query(C.Parameter parameter)
   {
      return (AP.Parameter) validate(parameter);
   }

   static AP.Variable query(C.Variable variable)
   {
      return (AP.Variable) validate(variable);
   }

   static AP.Constructor query(C.Constructor constructor)
   {
      return (AP.Constructor) validate(constructor);
   }

   static AP.Executable query(C.Executable executable)
   {
      return (AP.Executable) validate(executable);
   }

   static AP.Method query(C.Method method)
   {
      return (AP.Method) validate(method);
   }

   static AP.RecordComponent query(C.RecordComponent recordComponent)
   {
      return (AP.RecordComponent) validate(recordComponent);
   }

   static AP.Result query(C.Result aReturn)
   {
      return (AP.Result) validate(aReturn);
   }

   static AP.Receiver query(C.Receiver receiver)
   {
      return (AP.Receiver) validate(receiver);
   }

   static AP.Generic query(C.Generic generic)
   {
      return (AP.Generic) validate(generic);
   }

   static AP.AnnotationUsage query(C.AnnotationUsage annotationUsage)
   {
      return (AP.AnnotationUsage) validate(annotationUsage);
   }

   static AP.Module query(C.Module module)
   {
      return (AP.Module) validate(module);
   }

   static AP.Annotation query(C.Annotation annotation)
   {
      return (AP.Annotation) validate(annotation);
   }

   static AP.Modifiable query(C.Modifiable modifiable)
   {
      return (AP.Modifiable) validate(modifiable);
   }

   static AP.AbstractModifiable query(C.AbstractModifiable abstractModifiable)
   {
      return (AP.AbstractModifiable) validate(abstractModifiable);
   }

   static AP.AccessModifiable query(C.AccessModifiable accessModifiable)
   {
      return (AP.AccessModifiable) validate(accessModifiable);
   }

   static AP.DefaultModifiable query(C.DefaultModifiable defaultModifiable)
   {
      return (AP.DefaultModifiable) validate(defaultModifiable);
   }

   static AP.FinalModifiable query(C.FinalModifiable finalModifiable)
   {
      return (AP.FinalModifiable) validate(finalModifiable);
   }

   static AP.NativeModifiable query(C.NativeModifiable nativeModifiable)
   {
      return (AP.NativeModifiable) validate(nativeModifiable);
   }

   static AP.Sealable query(C.Sealable sealable)
   {
      return (AP.Sealable) validate(sealable);
   }

   static AP.StaticModifiable query(C.StaticModifiable staticModifiable)
   {
      return (AP.StaticModifiable) validate(staticModifiable);
   }

   static AP.StrictfpModifiable query(C.StrictfpModifiable strictfpModifiable)
   {
      return (AP.StrictfpModifiable) validate(strictfpModifiable);
   }

   static AP.Exports query(C.Exports exports)
   {
      return (AP.Exports) validate(exports);
   }

   static AP.Opens query(C.Opens opens)
   {
      return (AP.Opens) validate(opens);
   }

   static AP.Provides query(C.Provides provides)
   {
      return (AP.Provides) validate(provides);
   }

   static AP.Requires query(C.Requires requires)
   {
      return (AP.Requires) validate(requires);
   }

   static AP.Uses query(C.Uses uses)
   {
      return (AP.Uses) validate(uses);
   }

   static AP.Annotationable query(C.Annotationable annotationable)
   {
      return (AP.Annotationable) validate(annotationable);
   }

   static AP.AnnotationValue query(C.AnnotationValue annotationValue)
   {
      return (AP.AnnotationValue) validate(annotationValue);
   }

   static AP.Property query(C.Property property)
   {
      return (AP.Property) validate(property);
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
