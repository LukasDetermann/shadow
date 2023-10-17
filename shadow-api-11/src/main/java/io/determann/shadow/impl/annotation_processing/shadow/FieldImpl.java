package io.determann.shadow.impl.annotation_processing.shadow;

import io.determann.shadow.api.MirrorAdapter;
import io.determann.shadow.api.annotation_processing.AnnotationProcessingContext;
import io.determann.shadow.api.shadow.Declared;
import io.determann.shadow.api.shadow.Field;
import io.determann.shadow.api.shadow.Shadow;

import javax.lang.model.element.VariableElement;

public class FieldImpl extends VariableImpl<Declared> implements Field
{
   public FieldImpl(AnnotationProcessingContext annotationProcessingContext, VariableElement variableElement)
   {
      super(annotationProcessingContext, variableElement);
   }

   @Override
   public boolean isConstant()
   {
      return getElement().getConstantValue() != null;
   }

   @Override
   public Object getConstantValue()
   {
      return getElement().getConstantValue();
   }

   @Override
   public Shadow erasure()
   {
      return MirrorAdapter.getShadow(getApi(), MirrorAdapter.getProcessingEnv(getApi()).getTypeUtils().erasure(getMirror()));
   }
}
