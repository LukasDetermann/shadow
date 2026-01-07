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
      return switch (annotationable)
      {
         case Ap.Declared declared -> new DeclaredAdapter(declared).toTypeElement();
         case Ap.Executable executable -> new ExecutableAdapter(executable).toExecutableElement();
         case Ap.Generic generic -> new GenericAdapter(generic).toTypeParameterElement();
         case Ap.Module module -> new ModuleAdapter(module).toModuleElement();
         case Ap.Package aPackage -> new PackageAdapter(aPackage).toPackageElement();
         case Ap.RecordComponent recordComponent -> new RecordComponentAdapter(recordComponent).toRecordComponentElement();
         case Ap.Variable variable -> new VariableAdapter(variable).toVariableElement();
         case null, default -> throw new IllegalArgumentException();
      };
   }
}
