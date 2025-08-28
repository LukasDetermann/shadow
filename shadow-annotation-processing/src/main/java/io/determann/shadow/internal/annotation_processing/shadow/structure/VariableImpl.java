package io.determann.shadow.internal.annotation_processing.shadow.structure;

import io.determann.shadow.api.C;
import io.determann.shadow.api.Modifier;
import io.determann.shadow.api.annotation_processing.AP;
import io.determann.shadow.api.annotation_processing.adapter.Adapters;
import io.determann.shadow.api.query.Implementation;
import io.determann.shadow.internal.annotation_processing.ApContextImpl;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static io.determann.shadow.api.annotation_processing.adapter.Adapters.adapt;
import static io.determann.shadow.api.query.Operations.*;
import static io.determann.shadow.api.query.Provider.requestOrEmpty;
import static io.determann.shadow.api.query.Provider.requestOrThrow;

public abstract class VariableImpl
{
   private final VariableElement variableElement;
   private final AP.Context context;

   protected VariableImpl(AP.Context context, VariableElement variableElement)
   {
      this.context = context;
      this.variableElement = variableElement;
   }

   public Set<Modifier> getModifiers()
   {
      return ApContextImpl.getModifiers(getElement());
   }

   public boolean isSubtypeOf(C.Type type)
   {
      return adapt(getApi()).toTypes().isSubtype(adapt((AP.Declared) type).toDeclaredType(), getMirror());
   }

   public boolean isAssignableFrom(C.Type type)
   {
      return adapt(getApi()).toTypes().isAssignable(adapt((AP.Declared) type).toDeclaredType(), getMirror());
   }

   public AP.VariableType getType()
   {
      return (AP.VariableType) adapt(getApi(), getElement().asType());
   }

   public VariableElement getElement()
   {
      return variableElement;
   }

   public AP.Module getModule()
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

   public List<AP.AnnotationUsage> getAnnotationUsages()
   {
      return Adapters.adapt(getApi(), adapt(getApi()).toElements().getAllAnnotationMirrors(getElement()));
   }

   public List<AP.AnnotationUsage> getDirectAnnotationUsages()
   {
      return adapt(getApi(), getElement().getAnnotationMirrors());
   }

   public AP.Context getApi()
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
