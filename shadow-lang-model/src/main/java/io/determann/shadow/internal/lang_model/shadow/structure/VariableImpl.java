package io.determann.shadow.internal.lang_model.shadow.structure;

import io.determann.shadow.api.Implementation;
import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.lang_model.adapter.LM_Adapters;
import io.determann.shadow.api.lang_model.shadow.LM_AnnotationUsage;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Module;
import io.determann.shadow.api.lang_model.shadow.type.LM_Declared;
import io.determann.shadow.api.lang_model.shadow.type.LM_Type;
import io.determann.shadow.api.shadow.modifier.C_Modifier;
import io.determann.shadow.api.shadow.structure.C_Variable;
import io.determann.shadow.api.shadow.type.C_Type;
import io.determann.shadow.internal.lang_model.LangModelContextImpl;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrEmpty;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.lang_model.adapter.LM_Adapters.adapt;

public abstract class VariableImpl
{
   private final VariableElement variableElement;
   private final LM_Context context;

   protected VariableImpl(LM_Context context, VariableElement variableElement)
   {
      this.context = context;
      this.variableElement = variableElement;
   }

   public Set<C_Modifier> getModifiers()
   {
      return LangModelContextImpl.getModifiers(getElement());
   }

   public boolean isSubtypeOf(C_Type type)
   {
      return LM_Adapters.adapt(getApi()).toTypes().isSubtype(adapt((LM_Declared) type).toDeclaredType(), getMirror());
   }

   public boolean isAssignableFrom(C_Type type)
   {
      return adapt(getApi()).toTypes().isAssignable(adapt((LM_Declared) type).toDeclaredType(), getMirror());
   }

   public LM_Type getType()
   {
      return LM_Adapters.adapt(getApi(), getElement().asType());
   }

   public VariableElement getElement()
   {
      return variableElement;
   }

   public LM_Module getModule()
   {
      return LM_Adapters.adapt(getApi(), adapt(getApi()).toElements().getModuleOf(getElement()));
   }

   public String getName()
   {
      return getElement().getSimpleName().toString();
   }

   public String getJavaDoc()
   {
      return adapt(getApi()).toElements().getDocComment(getElement());
   }

   public List<LM_AnnotationUsage> getAnnotationUsages()
   {
      return LM_Adapters.adapt(getApi(), adapt(getApi()).toElements().getAllAnnotationMirrors(getElement()));
   }

   public List<LM_AnnotationUsage> getDirectAnnotationUsages()
   {
      return LM_Adapters.adapt(getApi(), getElement().getAnnotationMirrors());
   }

   public LM_Context getApi()
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
      if (!(other instanceof C_Variable otherVariable))
      {
         return false;
      }
      return requestOrEmpty(otherVariable, NAMEABLE_GET_NAME).map(name -> Objects.equals(getName(), name)).orElse(false) &&
             Objects.equals(getType(), requestOrThrow(otherVariable, VARIABLE_GET_TYPE)) &&
             requestOrEmpty(otherVariable, MODIFIABLE_GET_MODIFIERS).map(modifiers -> Objects.equals(modifiers, getModifiers())).orElse(false);
   }
}
