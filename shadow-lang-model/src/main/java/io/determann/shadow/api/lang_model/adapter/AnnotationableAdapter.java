package io.determann.shadow.api.lang_model.adapter;

import io.determann.shadow.api.lang_model.LM;

import javax.lang.model.element.Element;

public class AnnotationableAdapter
{
   private final LM.Annotationable annotationable;

   AnnotationableAdapter(LM.Annotationable annotationable)
   {
      this.annotationable = annotationable;
   }

   public Element toElement()
   {
      if (annotationable instanceof LM.Declared declared)
      {
         return new DeclaredAdapter(declared).toTypeElement();
      }
      if (annotationable instanceof LM.Executable executable)
      {
         return new ExecutableAdapter(executable).toExecutableElement();
      }
      if (annotationable instanceof LM.Generic generic)
      {
         return new GenericAdapter(generic).toTypeParameterElement();
      }
      if (annotationable instanceof LM.Module module)
      {
         return new ModuleAdapter(module).toModuleElement();
      }
      if (annotationable instanceof LM.Package aPackage)
      {
         return new PackageAdapter(aPackage).toPackageElement();
      }
      if (annotationable instanceof LM.RecordComponent recordComponent)
      {
         return new RecordComponentAdapter(recordComponent).toRecordComponentElement();
      }
      if (annotationable instanceof LM.Variable variable)
      {
         return new VariableAdapter(variable).toVariableElement();
      }
      throw new IllegalArgumentException();
   }
}
