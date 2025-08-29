package io.determann.shadow.api.annotation_processing.adapter;

import io.determann.shadow.api.annotation_processing.Ap;

import javax.lang.model.element.Element;

public class AnnotationableAdapter
{
   private final Ap.Annotationable annotationable;

   AnnotationableAdapter(Ap.Annotationable annotationable)
   {
      this.annotationable = annotationable;
   }

   public Element toElement()
   {
      if (annotationable instanceof Ap.Declared declared)
      {
         return new DeclaredAdapter(declared).toTypeElement();
      }
      if (annotationable instanceof Ap.Executable executable)
      {
         return new ExecutableAdapter(executable).toExecutableElement();
      }
      if (annotationable instanceof Ap.Generic generic)
      {
         return new GenericAdapter(generic).toTypeParameterElement();
      }
      if (annotationable instanceof Ap.Module module)
      {
         return new ModuleAdapter(module).toModuleElement();
      }
      if (annotationable instanceof Ap.Package aPackage)
      {
         return new PackageAdapter(aPackage).toPackageElement();
      }
      if (annotationable instanceof Ap.RecordComponent recordComponent)
      {
         return new RecordComponentAdapter(recordComponent).toRecordComponentElement();
      }
      if (annotationable instanceof Ap.Variable variable)
      {
         return new VariableAdapter(variable).toVariableElement();
      }
      throw new IllegalArgumentException();
   }
}
