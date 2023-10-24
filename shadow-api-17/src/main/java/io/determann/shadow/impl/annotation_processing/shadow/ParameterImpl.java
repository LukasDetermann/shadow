package io.determann.shadow.impl.annotation_processing.shadow;

import io.determann.shadow.api.annotation_processing.AnnotationProcessingContext;
import io.determann.shadow.api.shadow.Executable;
import io.determann.shadow.api.shadow.Parameter;

import javax.lang.model.element.VariableElement;
import java.util.List;

public class ParameterImpl extends VariableImpl<Executable> implements Parameter
{
   public ParameterImpl(AnnotationProcessingContext annotationProcessingContext, VariableElement variableElement)
   {
      super(annotationProcessingContext, variableElement);
   }

   @Override
   public boolean isVarArgs()
   {
      List<Parameter> parameters = getSurrounding().getParameters();
      return getSurrounding().isVarArgs() && parameters.get(parameters.size() - 1).representsSameType(this);
   }
}
