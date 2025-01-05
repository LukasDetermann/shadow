package io.determann.shadow.api.lang_model.adapter;

import io.determann.shadow.api.lang_model.shadow.structure.LM_Variable;
import io.determann.shadow.internal.lang_model.shadow.structure.VariableImpl;

import javax.lang.model.element.VariableElement;

public class LM_VariableAdapter
{
   private final LM_Variable variable;

   LM_VariableAdapter(LM_Variable variable)
   {
      this.variable = variable;
   }

   public VariableElement toVariableElement()
   {
      return ((VariableImpl) variable).getElement();
   }
}
