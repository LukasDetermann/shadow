package org.determann.shadow.impl.shadow;

import org.determann.shadow.api.ShadowApi;
import org.determann.shadow.api.TypeKind;
import org.determann.shadow.api.shadow.Package;
import org.determann.shadow.api.shadow.Shadow;
import org.determann.shadow.api.shadow.Variable;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.Objects;

public abstract class VariableImpl<SURROUNDING extends Shadow<? extends TypeMirror>> extends ShadowImpl<TypeMirror>
      implements Variable<SURROUNDING>
{
   private final VariableElement variableElement;

   protected VariableImpl(ShadowApi shadowApi, VariableElement variableElement)
   {
      super(shadowApi, variableElement.asType());
      this.variableElement = variableElement;
   }

   @Override
   public boolean isSubtypeOf(Shadow<? extends TypeMirror> shadow)
   {
      return getApi().getJdkApiContext().types().isSubtype(shadow.getMirror(), getMirror());
   }

   @Override
   public boolean isAssignableFrom(Shadow<? extends TypeMirror> shadow)
   {
      return getApi().getJdkApiContext().types().isAssignable(shadow.getMirror(), getMirror());
   }

   @Override
   public Shadow<TypeMirror> getType()
   {
      return getApi().getShadowFactory().shadowFromType(getElement().asType());
   }

   @Override
   public Package getPackage()
   {
      return getApi().getShadowFactory().shadowFromElement(getApi().getJdkApiContext().elements().getPackageOf(getElement()));
   }

   @Override
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
