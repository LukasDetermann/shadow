package io.determann.shadow.api.lang_model.adapter;

import io.determann.shadow.api.lang_model.shadow.LM_Annotationable;
import io.determann.shadow.api.lang_model.shadow.structure.*;
import io.determann.shadow.api.lang_model.shadow.type.LM_Declared;
import io.determann.shadow.api.lang_model.shadow.type.LM_Generic;

import javax.lang.model.element.Element;

public class LM_AnnotationableAdapter
{
   private final LM_Annotationable annotationable;

   LM_AnnotationableAdapter(LM_Annotationable annotationable)
   {
      this.annotationable = annotationable;
   }

   public Element toElement()
   {
      if (annotationable instanceof LM_Declared declared)
      {
         return new LM_DeclaredAdapter(declared).toTypeElement();
      }
      if (annotationable instanceof LM_Executable executable)
      {
         return new LM_ExecutableAdapter(executable).toExecutableElement();
      }
      if (annotationable instanceof LM_Generic generic)
      {
         return new LM_GenericAdapter(generic).toTypeParameterElement();
      }
      if (annotationable instanceof LM_Module module)
      {
         return new LM_ModuleAdapter(module).toModuleElement();
      }
      if (annotationable instanceof LM_Package aPackage)
      {
         return new LM_PackageAdapter(aPackage).toPackageElement();
      }
      if (annotationable instanceof LM_RecordComponent recordComponent)
      {
         return new LM_RecordComponentAdapter(recordComponent).toRecordComponentElement();
      }
      if (annotationable instanceof LM_Variable variable)
      {
         return new LM_VariableAdapter(variable).toVariableElement();
      }
      throw new IllegalArgumentException();
   }
}
