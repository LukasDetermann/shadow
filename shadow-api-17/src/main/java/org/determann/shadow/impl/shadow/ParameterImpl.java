package org.determann.shadow.impl.shadow;

import org.determann.shadow.api.ShadowApi;
import org.determann.shadow.api.shadow.Executable;
import org.determann.shadow.api.shadow.Parameter;

import javax.lang.model.element.VariableElement;

public class ParameterImpl extends VariableImpl<Executable> implements Parameter
{
   public ParameterImpl(ShadowApi shadowApi, VariableElement variableElement)
   {
      super(shadowApi, variableElement);
   }
}
