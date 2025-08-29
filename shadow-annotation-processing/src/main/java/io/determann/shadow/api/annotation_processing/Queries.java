package io.determann.shadow.api.annotation_processing;

import io.determann.shadow.api.C;
import io.determann.shadow.api.query.ImplementationDefined;

import java.util.Objects;

import static io.determann.shadow.internal.annotation_processing.ApContextImpl.IMPLEMENTATION_NAME;
import static java.util.Objects.requireNonNull;

public interface Queries
{
   static Ap.Nameable query(C.Nameable nameable)
   {
      return ((Ap.Nameable) validate(nameable));
   }

   static Ap.QualifiedNameable query(C.QualifiedNameable qualifiedNameable)
   {
      return ((Ap.QualifiedNameable) validate(qualifiedNameable));
   }

   static Ap.Wildcard query(C.Wildcard wildcard)
   {
      return ((Ap.Wildcard) validate(wildcard));
   }

   static Ap.Primitive query(C.Primitive primitive)
   {
      return ((Ap.Primitive) validate(primitive));
   }

   static Ap.Type query(C.Type type)
   {
      return ((Ap.Type) validate(type));
   }

   static Ap.Package query(C.Package aPackage)
   {
      return (Ap.Package) validate(aPackage);
   }

   static Ap.ModuleEnclosed query(C.ModuleEnclosed moduleEnclosed)
   {
      return ((Ap.ModuleEnclosed) validate(moduleEnclosed));
   }

   static Ap.Documented query(C.Documented documented)
   {
      return (Ap.Documented) validate(documented);
   }

   static Ap.Declared query(C.Declared declared)
   {
      return (Ap.Declared) validate(declared);
   }

   static Ap.Enum query(C.Enum anEnum)
   {
      return (Ap.Enum) validate(anEnum);
   }

   static Ap.Interface query(C.Interface anInterface)
   {
      return (Ap.Interface) validate(anInterface);
   }

   static Ap.Record query(C.Record record)
   {
      return (Ap.Record) validate(record);
   }

   static Ap.Class query(C.Class aClass)
   {
      return (Ap.Class) validate(aClass);
   }

   static Ap.Array query(C.Array array)
   {
      return (Ap.Array) validate(array);
   }

   static Ap.EnumConstant query(C.EnumConstant enumConstant)
   {
      return (Ap.EnumConstant) validate(enumConstant);
   }

   static Ap.Field query(C.Field field)
   {
      return (Ap.Field) validate(field);
   }

   static Ap.Parameter query(C.Parameter parameter)
   {
      return (Ap.Parameter) validate(parameter);
   }

   static Ap.Variable query(C.Variable variable)
   {
      return (Ap.Variable) validate(variable);
   }

   static Ap.Constructor query(C.Constructor constructor)
   {
      return (Ap.Constructor) validate(constructor);
   }

   static Ap.Executable query(C.Executable executable)
   {
      return (Ap.Executable) validate(executable);
   }

   static Ap.Method query(C.Method method)
   {
      return (Ap.Method) validate(method);
   }

   static Ap.RecordComponent query(C.RecordComponent recordComponent)
   {
      return (Ap.RecordComponent) validate(recordComponent);
   }

   static Ap.Result query(C.Result aReturn)
   {
      return (Ap.Result) validate(aReturn);
   }

   static Ap.Receiver query(C.Receiver receiver)
   {
      return (Ap.Receiver) validate(receiver);
   }

   static Ap.Generic query(C.Generic generic)
   {
      return (Ap.Generic) validate(generic);
   }

   static Ap.AnnotationUsage query(C.AnnotationUsage annotationUsage)
   {
      return (Ap.AnnotationUsage) validate(annotationUsage);
   }

   static Ap.Module query(C.Module module)
   {
      return (Ap.Module) validate(module);
   }

   static Ap.Annotation query(C.Annotation annotation)
   {
      return (Ap.Annotation) validate(annotation);
   }

   static Ap.Modifiable query(C.Modifiable modifiable)
   {
      return (Ap.Modifiable) validate(modifiable);
   }

   static Ap.AbstractModifiable query(C.AbstractModifiable abstractModifiable)
   {
      return (Ap.AbstractModifiable) validate(abstractModifiable);
   }

   static Ap.AccessModifiable query(C.AccessModifiable accessModifiable)
   {
      return (Ap.AccessModifiable) validate(accessModifiable);
   }

   static Ap.DefaultModifiable query(C.DefaultModifiable defaultModifiable)
   {
      return (Ap.DefaultModifiable) validate(defaultModifiable);
   }

   static Ap.FinalModifiable query(C.FinalModifiable finalModifiable)
   {
      return (Ap.FinalModifiable) validate(finalModifiable);
   }

   static Ap.NativeModifiable query(C.NativeModifiable nativeModifiable)
   {
      return (Ap.NativeModifiable) validate(nativeModifiable);
   }

   static Ap.Sealable query(C.Sealable sealable)
   {
      return (Ap.Sealable) validate(sealable);
   }

   static Ap.StaticModifiable query(C.StaticModifiable staticModifiable)
   {
      return (Ap.StaticModifiable) validate(staticModifiable);
   }

   static Ap.StrictfpModifiable query(C.StrictfpModifiable strictfpModifiable)
   {
      return (Ap.StrictfpModifiable) validate(strictfpModifiable);
   }

   static Ap.Exports query(C.Exports exports)
   {
      return (Ap.Exports) validate(exports);
   }

   static Ap.Opens query(C.Opens opens)
   {
      return (Ap.Opens) validate(opens);
   }

   static Ap.Provides query(C.Provides provides)
   {
      return (Ap.Provides) validate(provides);
   }

   static Ap.Requires query(C.Requires requires)
   {
      return (Ap.Requires) validate(requires);
   }

   static Ap.Uses query(C.Uses uses)
   {
      return (Ap.Uses) validate(uses);
   }

   static Ap.Annotationable query(C.Annotationable annotationable)
   {
      return (Ap.Annotationable) validate(annotationable);
   }

   static Ap.AnnotationValue query(C.AnnotationValue annotationValue)
   {
      return (Ap.AnnotationValue) validate(annotationValue);
   }

   static Ap.Property query(C.Property property)
   {
      return (Ap.Property) validate(property);
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
