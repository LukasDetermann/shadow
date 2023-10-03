package io.determann.shadow.impl.shadow;

import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.shadow.Generic;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.Shadow;

import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.TypeVariable;
import java.util.Objects;

public class GenericImpl extends ShadowImpl<TypeVariable> implements Generic
{
   private final TypeParameterElement typeParameterElement;

   public GenericImpl(ShadowApi shadowApi, TypeParameterElement typeParameterElement)
   {
      super(shadowApi, ((TypeVariable) typeParameterElement.asType()));
      this.typeParameterElement = typeParameterElement;
   }

   public GenericImpl(ShadowApi shadowApi, TypeVariable typeMirror)
   {
      super(shadowApi, typeMirror);
      this.typeParameterElement = (TypeParameterElement) getApi().getJdkApiContext().getProcessingEnv().getTypeUtils().asElement(typeMirror);
   }

   @Override
   public Shadow getExtends()
   {
      return getApi().getShadowFactory().shadowFromType(getMirror().getUpperBound());
   }

   @Override
   public Shadow getSuper()
   {
      return getApi().getShadowFactory().shadowFromType(getMirror().getLowerBound());
   }

   @Override
   public Shadow getEnclosing()
   {
      return getApi().getShadowFactory().shadowFromElement(getElement().getGenericElement());
   }

   @Override
   public TypeKind getTypeKind()
   {
      return TypeKind.GENERIC;
   }

   @Override
   public Package getPackage()
   {
      return getApi().getShadowFactory()
                     .shadowFromElement(getApi().getJdkApiContext().getProcessingEnv().getElementUtils().getPackageOf(getElement()));
   }

   @Override
   public Shadow erasure()
   {
      return getApi().getShadowFactory().shadowFromType(getApi().getJdkApiContext().getProcessingEnv().getTypeUtils().erasure(getMirror()));
   }

   @Override
   public TypeParameterElement getElement()
   {
      return typeParameterElement;
   }

   @Override
   public String toString()
   {
      return getElement().toString();
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getSimpleName(),
                          getExtends(),
                          getSuper(),
                          getEnclosing());
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
      GenericImpl otherGeneric = (GenericImpl) other;
      return Objects.equals(getSimpleName(), otherGeneric.getSimpleName()) &&
             Objects.equals(getExtends(), otherGeneric.getExtends()) &&
             Objects.equals(getSuper(), otherGeneric.getSuper()) &&
             Objects.equals(getEnclosing(), otherGeneric.getEnclosing());
   }
}
