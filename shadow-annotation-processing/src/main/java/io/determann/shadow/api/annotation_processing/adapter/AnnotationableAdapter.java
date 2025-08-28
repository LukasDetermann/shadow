package io.determann.shadow.api.annotation_processing.adapter;

import io.determann.shadow.api.annotation_processing.AP;

import javax.lang.model.element.Element;

public class AnnotationableAdapter
{
   private final AP.Annotationable annotationable;

   AnnotationableAdapter(AP.Annotationable annotationable)
   {
      this.annotationable = annotationable;
   }

   public Element toElement()
   {
      if (annotationable instanceof AP.Declared declared)
      {
         return new DeclaredAdapter(declared).toTypeElement();
      }
      if (annotationable instanceof AP.Executable executable)
      {
         return new ExecutableAdapter(executable).toExecutableElement();
      }
      if (annotationable instanceof AP.Generic generic)
      {
         return new GenericAdapter(generic).toTypeParameterElement();
      }
      if (annotationable instanceof AP.Module module)
      {
         return new ModuleAdapter(module).toModuleElement();
      }
      if (annotationable instanceof AP.Package aPackage)
      {
         return new PackageAdapter(aPackage).toPackageElement();
      }
      if (annotationable instanceof AP.RecordComponent recordComponent)
      {
         return new RecordComponentAdapter(recordComponent).toRecordComponentElement();
      }
      if (annotationable instanceof AP.Variable variable)
      {
         return new VariableAdapter(variable).toVariableElement();
      }
      throw new IllegalArgumentException();
   }
}
