package io.determann.shadow.internal.annotation_processing.shadow.structure;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.adapter.Adapters;
import io.determann.shadow.api.annotation_processing.dsl.RenderingContext;
import io.determann.shadow.api.annotation_processing.dsl.parameter.ParameterAnnotateStep;
import io.determann.shadow.api.annotation_processing.dsl.parameter.ParameterTypeStep;
import io.determann.shadow.api.annotation_processing.dsl.parameter.ParameterVarargsStep;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.util.List;
import java.util.Objects;

import static io.determann.shadow.api.annotation_processing.dsl.JavaDsl.parameter;

public class ParameterImpl extends VariableImpl implements Ap.Parameter
{
   public ParameterImpl(Ap.Context context, VariableElement variableElement)
   {
      super(context, variableElement);
   }

   @Override
   public boolean isVarArgs()
   {
      List<Ap.Parameter> parameters = getSurrounding().getParameters();
      return getSurrounding().isVarArgs() &&
             parameters.get(parameters.size() - 1).equals(this);
   }

   @Override
   public String renderDeclaration(RenderingContext renderingContext)
   {
      ParameterAnnotateStep annotateStep = parameter()
            .annotate(getDirectAnnotationUsages());

      ParameterTypeStep parameterTypeStep = annotateStep;
      if (isFinal())
      {
         parameterTypeStep = annotateStep.final_();
      }

      ParameterVarargsStep varargsStep = parameterTypeStep.type(getType())
                                                          .name(getName());

      if (!isVarArgs())
      {
         return varargsStep.renderDeclaration(renderingContext);
      }
      return varargsStep.varArgs().renderDeclaration(renderingContext);
   }

   @Override
   public String renderName(RenderingContext renderingContext)
   {
      return getName();
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getName(),
                          getSurrounding());
   }

   @Override
   public Ap.Executable getSurrounding()
   {
      return Adapters.adapt(getApi(), ((ExecutableElement) getElement().getEnclosingElement()));
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (!(other instanceof Ap.Parameter otherVariable))
      {
         return false;
      }
      return Objects.equals(getName(), otherVariable.getName()) &&
             Objects.equals(getType(), otherVariable.getType()) &&
             Objects.equals(getModifiers(), otherVariable.getModifiers());
   }
}
