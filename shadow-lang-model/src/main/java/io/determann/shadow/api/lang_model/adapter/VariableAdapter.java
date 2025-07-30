package io.determann.shadow.api.lang_model.adapter;

import io.determann.shadow.api.lang_model.LM;
import io.determann.shadow.internal.lang_model.shadow.structure.VariableImpl;

import javax.lang.model.element.VariableElement;

public class VariableAdapter
{
   private final LM.Variable variable;

   VariableAdapter(LM.Variable variable)
   {
      this.variable = variable;
   }

   public VariableElement toVariableElement()
   {
      return ((VariableImpl) variable).getElement();
   }
}
