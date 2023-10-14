package io.determann.shadow.impl.annotation_processing.shadow;

import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.shadow.Enum;
import io.determann.shadow.api.shadow.EnumConstant;

import javax.lang.model.element.VariableElement;

public class EnumConstantImpl extends VariableImpl<Enum> implements EnumConstant
{
   public EnumConstantImpl(ShadowApi shadowApi, VariableElement variableElement)
   {
      super(shadowApi, variableElement);
   }
}
