package com.derivandi.api.adapter;

import com.derivandi.api.D;
import com.derivandi.internal.shadow.structure.VariableImpl;

import javax.lang.model.element.VariableElement;

public class VariableAdapter
{
   private final D.Variable variable;

   VariableAdapter(D.Variable variable)
   {
      this.variable = variable;
   }

   public VariableElement toVariableElement()
   {
      return ((VariableImpl) variable).getElement();
   }
}
