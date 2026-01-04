package io.determann.shadow.internal.annotation_processing.shadow.structure;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.Modifier;
import io.determann.shadow.api.annotation_processing.adapter.Adapters;
import io.determann.shadow.internal.annotation_processing.ApContextImpl;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static io.determann.shadow.api.annotation_processing.adapter.Adapters.adapt;
import static java.util.Optional.ofNullable;

public abstract class VariableImpl
{
   private final VariableElement variableElement;
   private final Ap.Context context;

   protected VariableImpl(Ap.Context context, VariableElement variableElement)
   {
      this.context = context;
      this.variableElement = variableElement;
   }

   public Set<Modifier> getModifiers()
   {
      return ApContextImpl.getModifiers(getElement());
   }

   public boolean isSubtypeOf(Ap.Type type)
   {
      return adapt(getApi()).toTypes().isSubtype(adapt((Ap.Declared) type).toDeclaredType(), getMirror());
   }

   public boolean isAssignableFrom(Ap.Type type)
   {
      return adapt(getApi()).toTypes().isAssignable(adapt((Ap.Declared) type).toDeclaredType(), getMirror());
   }

   public Ap.VariableType getType()
   {
      return (Ap.VariableType) adapt(getApi(), getElement().asType());
   }

   public VariableElement getElement()
   {
      return variableElement;
   }

   public Ap.Module getModule()
   {
      return Adapters.adapt(getApi(), adapt(getApi()).toElements().getModuleOf(getElement()));
   }

   public String getName()
   {
      return getElement().getSimpleName().toString();
   }

   public Optional<String> getJavaDoc()
   {
      return ofNullable(adapt(getApi()).toElements().getDocComment(getElement()));
   }

   public List<Ap.AnnotationUsage> getAnnotationUsages()
   {
      return Adapters.adapt(getApi(), adapt(getApi()).toElements().getAllAnnotationMirrors(getElement()));
   }

   public List<Ap.AnnotationUsage> getDirectAnnotationUsages()
   {
      return adapt(getApi(), getElement().getAnnotationMirrors());
   }

   public Ap.Context getApi()
   {
      return context;
   }

   public TypeMirror getMirror()
   {
      return variableElement.asType();
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
      if (!(other instanceof Ap.Variable otherVariable))
      {
         return false;
      }
      return Objects.equals(getName(), otherVariable.getName()) &&
             Objects.equals(getType(), otherVariable.getType()) &&
             Objects.equals(getModifiers(), otherVariable.getModifiers());
   }
}
