package io.determann.shadow.api.converter;

import io.determann.shadow.api.converter.module.*;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Enum;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.Record;
import io.determann.shadow.api.shadow.Void;
import io.determann.shadow.api.shadow.*;
import io.determann.shadow.api.shadow.module.*;
import io.determann.shadow.internal.converter.ConverterImpl;
import io.determann.shadow.internal.converter.DirectiveConverterImpl;

public interface Converter
{

   static AnnotationConverter convert(Annotation annotationShadow)
   {
      return new ConverterImpl(annotationShadow);
   }

   static ArrayConverter convert(Array array)
   {
      return new ConverterImpl(array);
   }

   static ClassConverter convert(Class aClass)
   {
      return new ConverterImpl(aClass);
   }

   static ConstructorConverter convert(Constructor constructor)
   {
      return new ConverterImpl(constructor);
   }

   static DeclaredConverter convert(Declared declared)
   {
      return new ConverterImpl(declared);
   }

   static EnumConstantConverter convert(EnumConstant enumConstant)
   {
      return new ConverterImpl(enumConstant);
   }

   static EnumConverter convert(Enum enumShadow)
   {
      return new ConverterImpl(enumShadow);
   }

   static ExecutableConverter convert(Executable executable)
   {
      return new ConverterImpl(executable);
   }

   static FieldConverter convert(Field field)
   {
      return new ConverterImpl(field);
   }

   static InterfaceConverter convert(Interface interfaceShadow)
   {
      return new ConverterImpl(interfaceShadow);
   }

   static IntersectionConverter convert(Intersection intersection)
   {
      return new ConverterImpl(intersection);
   }

   static MethodConverter convert(Method methodShadow)
   {
      return new ConverterImpl(methodShadow);
   }

   static ModuleConverter convert(Module module)
   {
      return new ConverterImpl(module);
   }

   static VoidConverter convert(Void aVoid)
   {
      return new ConverterImpl(aVoid);
   }

   static NullConverter convert(Null aNull)
   {
      return new ConverterImpl(aNull);
   }

   static PackageConverter convert(Package packageShadow)
   {
      return new ConverterImpl(packageShadow);
   }

   static ParameterConverter convert(Parameter parameter)
   {
      return new ConverterImpl(parameter);
   }

   static PrimitiveConverter convert(Primitive primitive)
   {
      return new ConverterImpl(primitive);
   }

   static RecordComponentConverter convert(RecordComponent recordComponent)
   {
      return new ConverterImpl(recordComponent);
   }

   static RecordConverter convert(Record recordShadow)
   {
      return new ConverterImpl(recordShadow);
   }

   static ShadowConverter convert(Shadow shadow)
   {
      return new ConverterImpl(shadow);
   }

   static GenericConverter convert(Generic generic)
   {
      return new ConverterImpl(generic);
   }

   static VariableConverter convert(Variable<?> variable)
   {
      return new ConverterImpl(variable);
   }

   static WildcardConverter convert(Wildcard wildcard)
   {
      return new ConverterImpl(wildcard);
   }

   static DirectiveConverter convert(Directive directive)
   {
      return new DirectiveConverterImpl(directive);
   }

   static ExportsConverter convert(Exports exportsShadow)
   {
      return new DirectiveConverterImpl(exportsShadow);
   }

   static OpensConverter convert(Opens opensShadow)
   {
      return new DirectiveConverterImpl(opensShadow);
   }

   static ProvidesConverter convert(Provides providesShadow)
   {
      return new DirectiveConverterImpl(providesShadow);
   }

   static RequiresConverter convert(Requires requiresShadow)
   {
      return new DirectiveConverterImpl(requiresShadow);
   }

   static UsesConverter convert(Uses usesShadow)
   {
      return new DirectiveConverterImpl(usesShadow);
   }
}
