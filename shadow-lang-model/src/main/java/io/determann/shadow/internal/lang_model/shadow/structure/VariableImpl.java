package io.determann.shadow.internal.lang_model.shadow.structure;

import io.determann.shadow.api.C;
import io.determann.shadow.api.Modifier;
import io.determann.shadow.api.lang_model.LM;
import io.determann.shadow.api.lang_model.adapter.Adapters;
import io.determann.shadow.api.query.Implementation;
import io.determann.shadow.internal.lang_model.LangModelContextImpl;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static io.determann.shadow.api.lang_model.adapter.Adapters.adapt;
import static io.determann.shadow.api.query.Operations.*;
import static io.determann.shadow.api.query.Provider.requestOrEmpty;
import static io.determann.shadow.api.query.Provider.requestOrThrow;

public abstract class VariableImpl
{
   private final VariableElement variableElement;
   private final LM.Context context;

   protected VariableImpl(LM.Context context, VariableElement variableElement)
   {
      this.context = context;
      this.variableElement = variableElement;
   }

   public Set<Modifier> getModifiers()
   {
      return LangModelContextImpl.getModifiers(getElement());
   }

   public boolean isSubtypeOf(C.Type type)
   {
      return Adapters.adapt(getApi()).toTypes().isSubtype(adapt((LM.Declared) type).toDeclaredType(), getMirror());
   }

   public boolean isAssignableFrom(C.Type type)
   {
      return adapt(getApi()).toTypes().isAssignable(adapt((LM.Declared) type).toDeclaredType(), getMirror());
   }

   public LM.VariableType getType()
   {
      return (LM.VariableType) Adapters.adapt(getApi(), getElement().asType());
   }

   public VariableElement getElement()
   {
      return variableElement;
   }

   public LM.Module getModule()
   {
      return Adapters.adapt(getApi(), adapt(getApi()).toElements().getModuleOf(getElement()));
   }

   public String getName()
   {
      return getElement().getSimpleName().toString();
   }

   public String getJavaDoc()
   {
      return adapt(getApi()).toElements().getDocComment(getElement());
   }

   public List<LM.AnnotationUsage> getAnnotationUsages()
   {
      return Adapters.adapt(getApi(), adapt(getApi()).toElements().getAllAnnotationMirrors(getElement()));
   }

   public List<LM.AnnotationUsage> getDirectAnnotationUsages()
   {
      return Adapters.adapt(getApi(), getElement().getAnnotationMirrors());
   }

   public LM.Context getApi()
   {
      return context;
   }

   public TypeMirror getMirror()
   {
      return variableElement.asType();
   }

   public Implementation getImplementation()
   {
      return getApi().getImplementation();
   }

   @Override
   public String toString()
   {
      return getElement().toString();
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getName(),
                          getType(),
                          getModifiers());
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (!(other instanceof C.Variable otherVariable))
      {
         return false;
      }
      return requestOrEmpty(otherVariable, NAMEABLE_GET_NAME).map(name -> Objects.equals(getName(), name)).orElse(false) &&
             Objects.equals(getType(), requestOrThrow(otherVariable, VARIABLE_GET_TYPE)) &&
             requestOrEmpty(otherVariable, MODIFIABLE_GET_MODIFIERS).map(modifiers -> Objects.equals(modifiers, getModifiers())).orElse(false);
   }
}
