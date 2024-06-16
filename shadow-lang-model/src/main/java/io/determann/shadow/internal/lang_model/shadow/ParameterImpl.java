package io.determann.shadow.internal.lang_model.shadow;

import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.lang_model.query.ParameterLangModel;
import io.determann.shadow.api.shadow.Executable;
import io.determann.shadow.api.shadow.Parameter;
import io.determann.shadow.api.shadow.Shadow;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.util.List;
import java.util.Objects;

import static io.determann.shadow.api.lang_model.LangModelQueries.query;
import static io.determann.shadow.meta_meta.Operations.*;
import static io.determann.shadow.meta_meta.Provider.request;
import static io.determann.shadow.meta_meta.Provider.requestOrThrow;

public class ParameterImpl extends VariableImpl implements ParameterLangModel
{
   public ParameterImpl(LangModelContext context, VariableElement variableElement)
   {
      super(context, variableElement);
   }

   @Override
   public boolean isVarArgs()
   {
      List<Parameter> parameters = requestOrThrow(getSurrounding(), EXECUTABLE_GET_PARAMETERS);
      return requestOrThrow(getSurrounding(), EXECUTABLE_IS_VAR_ARGS) &&
             query((Shadow) parameters.get(parameters.size() - 1)).representsSameType(this);
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
      return request(otherVariable, NAMEABLE_NAME).map(name -> Objects.equals(getName(), name)).orElse(false) &&
             Objects.equals(getType(), requestOrThrow(otherVariable, VARIABLE_GET_TYPE)) &&
             Objects.equals(getModifiers(), otherVariable.getModifiers()) &&
             Objects.equals(isVarArgs(), requestOrThrow(otherVariable, PARAMETER_IS_VAR_ARGS));
   }
}
