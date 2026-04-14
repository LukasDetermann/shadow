package com.derivandi.api.adapter;

import com.derivandi.api.Ap;
import com.derivandi.internal.shadow.structure.VariableImpl;

import javax.lang.model.element.VariableElement;

public class VariableAdapter
{
   private final Ap.Variable variable;

   VariableAdapter(Ap.Variable variable)
   {
      this.variable = variable;
   }

   public VariableElement toVariableElement()
   {
      return ((VariableImpl) variable).getElement();
   }
}
