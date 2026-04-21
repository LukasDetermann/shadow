package com.derivandi.api.adapter;

import com.derivandi.api.D;

import javax.lang.model.element.Element;

public class AnnotationableAdapter
{
   private final D.Annotationable annotationable;

   AnnotationableAdapter(D.Annotationable annotationable)
   {
      this.annotationable = annotationable;
   }

   public Element toElement()
   {
      return switch (annotationable)
      {
         case D.Declared declared -> new DeclaredAdapter(declared).toTypeElement();
         case D.Executable executable -> new ExecutableAdapter(executable).toExecutableElement();
         case D.Generic generic -> new GenericAdapter(generic).toTypeParameterElement();
         case D.Module module -> new ModuleAdapter(module).toModuleElement();
         case D.Package aPackage -> new PackageAdapter(aPackage).toPackageElement();
         case D.RecordComponent recordComponent -> new RecordComponentAdapter(recordComponent).toRecordComponentElement();
         case D.Variable variable -> new VariableAdapter(variable).toVariableElement();
         case null, default -> throw new IllegalArgumentException();
      };
   }
}
