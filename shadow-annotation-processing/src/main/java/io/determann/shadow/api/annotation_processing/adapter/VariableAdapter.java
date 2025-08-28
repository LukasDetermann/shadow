package io.determann.shadow.api.annotation_processing.adapter;

import io.determann.shadow.api.annotation_processing.AP;
import io.determann.shadow.internal.annotation_processing.shadow.structure.VariableImpl;

import javax.lang.model.element.VariableElement;

public class VariableAdapter
{
   private final AP.Variable variable;

   VariableAdapter(AP.Variable variable)
   {
      this.variable = variable;
   }

   public VariableElement toVariableElement()
   {
      return ((VariableImpl) variable).getElement();
   }
}
