package io.determann.shadow.impl.shadow;

import io.determann.shadow.api.MirrorAdapter;
import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.modifier.Modifier;
import io.determann.shadow.api.shadow.AnnotationUsage;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.Shadow;
import io.determann.shadow.api.shadow.Variable;

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
      return getApi().getJdkApiContext().getProcessingEnv().getTypeUtils().isSubtype(MirrorAdapter.getType(shadow), getMirror());
   }

   @Override
   public boolean isAssignableFrom(Shadow shadow)
   {
      return getApi().getJdkApiContext().getProcessingEnv().getTypeUtils().isAssignable(MirrorAdapter.getType(shadow), getMirror());
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
                     .getShadow(getApi(), getApi().getJdkApiContext().getProcessingEnv().getElementUtils().getPackageOf(getElement()));
   }

   public VariableElement getElement()
   {
      return variableElement;
   }

   @Override
   public TypeKind getTypeKind()
   {
      switch (getElement().getKind())
      {
         case ENUM_CONSTANT:
            return TypeKind.ENUM_CONSTANT;
         case FIELD:
            return TypeKind.FIELD;
         case PARAMETER:
            return TypeKind.PARAMETER;
         default:
            throw new IllegalStateException();
      }
   }

   @Override
   public SURROUNDING getSurrounding()
   {
      return MirrorAdapter.getShadow(getApi(), getElement().getEnclosingElement());
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
   public void logError(String msg)
   {
      MirrorAdapter.logError(getApi(), getElement(), msg);
   }

   @Override
   public void logInfo(String msg)
   {
      MirrorAdapter.logInfo(getApi(), getElement(), msg);
   }

   @Override
   public void logWarning(String msg)
   {
      MirrorAdapter.logWarning(getApi(), getElement(), msg);
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
