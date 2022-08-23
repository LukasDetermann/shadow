package org.determann.shadow.impl.shadow;

import org.determann.shadow.api.ShadowApi;
import org.determann.shadow.api.shadow.Enum;
import org.determann.shadow.api.shadow.EnumConstant;

import javax.lang.model.element.VariableElement;

public class EnumConstantImpl extends VariableImpl<Enum> implements EnumConstant
{
   public EnumConstantImpl(ShadowApi shadowApi, VariableElement variableElement)
   {
      super(shadowApi, variableElement);
   }
}
