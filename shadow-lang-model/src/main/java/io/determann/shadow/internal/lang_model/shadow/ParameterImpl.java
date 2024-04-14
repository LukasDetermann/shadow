package io.determann.shadow.internal.lang_model.shadow;

import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.shadow.Executable;
import io.determann.shadow.api.shadow.Parameter;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.util.List;
import java.util.Objects;

public class ParameterImpl extends VariableImpl implements Parameter
{
   public ParameterImpl(LangModelContext context, VariableElement variableElement)
   {
      super(context, variableElement);
   }

   @Override
   public boolean isVarArgs()
   {
      List<Parameter> parameters = getSurrounding().getParameters();
      return getSurrounding().isVarArgs() && parameters.get(parameters.size() - 1).representsSameType(this);
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getKind(),
                          getName(),
                          getSurrounding(),
                          isVarArgs());
   }

   @Override
   public Executable getSurrounding()
   {
      if (getElement().getEnclosingElement() instanceof ExecutableElement executableElement)
      {
         return LangModelAdapter.generalize(getApi(), executableElement);
      }

      return LangModelAdapter.generalize(getApi(), getElement().getEnclosingElement());
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (!(other instanceof Parameter otherVariable))
      {
         return false;
      }
      return Objects.equals(getName(), otherVariable.getName()) &&
             Objects.equals(getType(), otherVariable.getType()) &&
             Objects.equals(getModifiers(), otherVariable.getModifiers()) &&
             Objects.equals(isVarArgs(), otherVariable.isVarArgs());
   }
}
