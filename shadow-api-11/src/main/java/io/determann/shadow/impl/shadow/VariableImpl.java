package io.determann.shadow.impl.shadow;

import io.determann.shadow.api.MirrorAdapter;
import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.Shadow;
import io.determann.shadow.api.shadow.Variable;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.Objects;

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
      return getApi().getShadowFactory().shadowFromType(getElement().asType());
   }

   @Override
   public Package getPackage()
   {
      return getApi().getShadowFactory()
                     .shadowFromElement(getApi().getJdkApiContext().getProcessingEnv().getElementUtils().getPackageOf(getElement()));
   }

   @Override
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
   public String toString()
   {
      return getElement().toString();
   }

   @Override
   public SURROUNDING getSurrounding()
   {
      return getApi().getShadowFactory().shadowFromElement(getElement().getEnclosingElement());
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
