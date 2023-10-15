package io.determann.shadow.impl.annotation_processing.shadow;

import io.determann.shadow.api.MirrorAdapter;
import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.modifier.Modifier;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.*;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public abstract class VariableImpl<SURROUNDING extends Shadow> extends ShadowImpl<TypeMirror>
      implements Variable<SURROUNDING>
{
   private final VariableElement variableElement;

   protected VariableImpl(ShadowApi shadowApi, VariableElement variableElement)
   {
      super(shadowApi, variableElement.asType());
      this.variableElement = variableElement;
   }

   @Override
   public Set<Modifier> getModifiers()
   {
      return MirrorAdapter.getModifiers(getElement());
   }

   @Override
   public boolean isSubtypeOf(Shadow shadow)
   {
      return MirrorAdapter.getProcessingEnv(getApi()).getTypeUtils().isSubtype(MirrorAdapter.getType(shadow), getMirror());
   }

   @Override
   public boolean isAssignableFrom(Shadow shadow)
   {
      return MirrorAdapter.getProcessingEnv(getApi()).getTypeUtils().isAssignable(MirrorAdapter.getType(shadow), getMirror());
   }

   @Override
   public Shadow getType()
   {
      return MirrorAdapter.getShadow(getApi(), getElement().asType());
   }

   @Override
   public Package getPackage()
   {
      return MirrorAdapter
                     .getShadow(getApi(), MirrorAdapter.getProcessingEnv(getApi()).getElementUtils().getPackageOf(getElement()));
   }

   public VariableElement getElement()
   {
      return variableElement;
   }

   @Override
   public TypeKind getTypeKind()
   {
      return switch (getElement().getKind())
      {
         case ENUM_CONSTANT -> TypeKind.ENUM_CONSTANT;
         case FIELD -> TypeKind.FIELD;
         case PARAMETER -> TypeKind.PARAMETER;
         default -> throw new IllegalStateException();
      };
   }

   @Override
   public SURROUNDING getSurrounding()
   {
      return MirrorAdapter.getShadow(getApi(), getElement().getEnclosingElement());
   }

   @Override
   public Module getModule()
   {
      return MirrorAdapter.getModule(getApi(), getElement());
   }

   @Override
   public String getSimpleName()
   {
      return MirrorAdapter.getSimpleName(getElement());
   }

   @Override
   public String getJavaDoc()
   {
      return MirrorAdapter.getJavaDoc(getApi(), getElement());
   }

   @Override
   public List<AnnotationUsage> getAnnotationUsages()
   {
      return MirrorAdapter.getAnnotationUsages(getApi(), getElement());
   }

   @Override
   public List<AnnotationUsage> getDirectAnnotationUsages()
   {
      return MirrorAdapter.getDirectAnnotationUsages(getApi(), getElement());
   }

   @Override
   public String toString()
   {
      return getElement().toString();
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getTypeKind(),
                          getSimpleName(),
                          getSurrounding());
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (other == null || !getClass().equals(other.getClass()))
      {
         return false;
      }
      VariableImpl<?> otherVariable = (VariableImpl<?>) other;
      return Objects.equals(getSimpleName(), otherVariable.getSimpleName()) &&
             Objects.equals(getSurrounding(), otherVariable.getSurrounding()) &&
             Objects.equals(getTypeKind(), otherVariable.getTypeKind());
   }
}