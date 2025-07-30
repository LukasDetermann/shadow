package io.determann.shadow.api.reflection;

import io.determann.shadow.api.C;
import io.determann.shadow.api.query.ImplementationDefined;

import java.util.Objects;

import static io.determann.shadow.api.reflection.Adapter.IMPLEMENTATION;
import static java.util.Objects.requireNonNull;

public interface Queries
{
   static R.Nameable query(C.Nameable nameable)
   {
      return ((R.Nameable) validate(nameable));
   }

   static R.QualifiedNameable query(C.QualifiedNameable qualifiedNameable)
   {
      return ((R.QualifiedNameable) validate(qualifiedNameable));
   }

   static R.Wildcard query(C.Wildcard wildcard)
   {
      return ((R.Wildcard) validate(wildcard));
   }

   static R.Primitive query(C.Primitive primitive)
   {
      return ((R.Primitive) validate(primitive));
   }

   static R.Type query(C.Type type)
   {
      return ((R.Type) validate(type));
   }

   static R.Package query(C.Package aPackage)
   {
      return (R.Package) validate(aPackage);
   }

   static R.ModuleEnclosed query(C.ModuleEnclosed moduleEnclosed)
   {
      return (R.ModuleEnclosed) validate(moduleEnclosed);
   }

   static R.Declared query(C.Declared declared)
   {
      return (R.Declared) validate(declared);
   }

   static R.Enum query(C.Enum anEnum)
   {
      return (R.Enum) validate(anEnum);
   }

   static R.Interface query(C.Interface anInterface)
   {
      return (R.Interface) validate(anInterface);
   }

   static R.Record query(C.Record record)
   {
      return (R.Record) validate(record);
   }

   static R.Class query(C.Class aClass)
   {
      return (R.Class) validate(aClass);
   }

   static R.Array query(C.Array array)
   {
      return (R.Array) validate(array);
   }

   static R.EnumConstant query(C.EnumConstant enumConstant)
   {
      return (R.EnumConstant) validate(enumConstant);
   }

   static R.Field query(C.Field field)
   {
      return (R.Field) validate(field);
   }

   static R.Parameter query(C.Parameter parameter)
   {
      return (R.Parameter) validate(parameter);
   }

   static R.Variable query(C.Variable variable)
   {
      return (R.Variable) validate(variable);
   }

   static R.Constructor query(C.Constructor constructor)
   {
      return (R.Constructor) validate(constructor);
   }

   static R.Executable query(C.Executable executable)
   {
      return (R.Executable) validate(executable);
   }

   static R.Method query(C.Method method)
   {
      return (R.Method) validate(method);
   }

   static R.RecordComponent query(C.RecordComponent recordComponent)
   {
      return (R.RecordComponent) validate(recordComponent);
   }

   static R.Result query(C.Result aReturn)
   {
      return (R.Result) validate(aReturn);
   }

   static R.Receiver query(C.Receiver receiver)
   {
      return (R.Receiver) validate(receiver);
   }

   static R.Generic query(C.Generic generic)
   {
      return (R.Generic) validate(generic);
   }

   static R.AnnotationUsage query(C.AnnotationUsage annotationUsage)
   {
      return (R.AnnotationUsage) validate(annotationUsage);
   }

   static R.Module query(C.Module module)
   {
      return (R.Module) validate(module);
   }

   static R.Annotation query(C.Annotation annotation)
   {
      return (R.Annotation) validate(annotation);
   }

   static R.Modifiable query(C.Modifiable modifiable)
   {
      return (R.Modifiable) validate(modifiable);
   }

   static R.AbstractModifiable query(C.AbstractModifiable abstractModifiable)
   {
      return (R.AbstractModifiable) validate(abstractModifiable);
   }

   static R.Constructor query(C.AccessModifiable accessModifiable)
   {
      return (R.Constructor) validate(accessModifiable);
   }

   static R.DefaultModifiable query(C.DefaultModifiable defaultModifiable)
   {
      return (R.DefaultModifiable) validate(defaultModifiable);
   }

   static R.FinalModifiable query(C.FinalModifiable finalModifiable)
   {
      return (R.FinalModifiable) validate(finalModifiable);
   }

   static R.NativeModifiable query(C.NativeModifiable nativeModifiable)
   {
      return (R.NativeModifiable) validate(nativeModifiable);
   }

   static R.Sealable query(C.Sealable sealable)
   {
      return (R.Sealable) validate(sealable);
   }

   static R.StaticModifiable query(C.StaticModifiable staticModifiable)
   {
      return (R.StaticModifiable) validate(staticModifiable);
   }

   static R.StrictfpModifiable query(C.StrictfpModifiable strictfpModifiable)
   {
      return (R.StrictfpModifiable) validate(strictfpModifiable);
   }

   static R.Exports query(C.Exports exports)
   {
      return (R.Exports) validate(exports);
   }

   static R.Opens query(C.Opens opens)
   {
      return (R.Opens) validate(opens);
   }

   static R.Provides query(C.Provides provides)
   {
      return (R.Provides) validate(provides);
   }

   static R.Requires query(C.Requires requires)
   {
      return (R.Requires) validate(requires);
   }

   static R.Uses query(C.Uses uses)
   {
      return (R.Uses) validate(uses);
   }

   static R.Annotationable query(C.Annotationable annotationable)
   {
      return (R.Annotationable) validate(annotationable);
   }

   static R.AnnotationValue query(C.AnnotationValue annotationValue)
   {
      return (R.AnnotationValue) validate(annotationValue);
   }

   static R.Property query(C.Property property)
   {
      return (R.Property) validate(property);
   }

   private static <T extends ImplementationDefined> T validate(T toValidate)
   {
      if (!Objects.equals(requireNonNull(toValidate.getImplementation()), IMPLEMENTATION))
      {
         throw new IllegalArgumentException("Tried to use \"" +
                                            IMPLEMENTATION + " \" to query \"" + toValidate + "\" based on \"" + toValidate.getImplementation() + "\"");
      }
      return toValidate;
   }
}
