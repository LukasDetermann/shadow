package io.determann.shadow.internal.lang_model.shadow;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.shadow.AnnotationUsage;
import io.determann.shadow.api.shadow.Generic;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.Shadow;

import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class GenericImpl extends ShadowImpl<TypeVariable> implements Generic
{
   private final TypeParameterElement typeParameterElement;

   public GenericImpl(LangModelContext context, TypeParameterElement typeParameterElement)
   {
      super(context, ((TypeVariable) typeParameterElement.asType()));
      this.typeParameterElement = typeParameterElement;
   }

   public GenericImpl(LangModelContext context, TypeVariable typeMirror)
   {
      super(context, typeMirror);
      this.typeParameterElement = (TypeParameterElement) LangModelAdapter.getTypes(getApi()).asElement(typeMirror);
   }

   @Override
   public Shadow getExtends()
   {
      return LangModelAdapter.getShadow(getApi(), getMirror().getUpperBound());
   }

   @Override
   public Optional<Shadow> getSuper()
   {
      TypeMirror lowerBound = getMirror().getLowerBound();
      if (lowerBound == null || lowerBound.getKind().equals(javax.lang.model.type.TypeKind.NONE))
      {
         return Optional.empty();
      }
      return Optional.of(LangModelAdapter.getShadow(getApi(), lowerBound));
   }

   @Override
   public Shadow getEnclosing()
   {
      return LangModelAdapter.getShadow(getApi(), getElement().getGenericElement());
   }

   @Override
   public TypeKind getTypeKind()
   {
      return TypeKind.GENERIC;
   }

   @Override
   public Package getPackage()
   {
      return LangModelAdapter
                     .getShadow(getApi(), LangModelAdapter.getElements(getApi()).getPackageOf(getElement()));
   }

   public TypeParameterElement getElement()
   {
      return typeParameterElement;
   }

   @Override
   public String getName()
   {
      return LangModelAdapter.getName(getElement());
   }

   @Override
   public List<AnnotationUsage> getAnnotationUsages()
   {
      return LangModelAdapter.getAnnotationUsages(getApi(), getElement());
   }

   @Override
   public List<AnnotationUsage> getDirectAnnotationUsages()
   {
      return LangModelAdapter.getDirectAnnotationUsages(getApi(), getElement());
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
                          getExtends(),
                          getSuper());
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (!(other instanceof Generic otherGeneric))
      {
         return false;
      }
      return Objects.equals(getName(), otherGeneric.getName()) &&
             Objects.equals(getExtends(), otherGeneric.getExtends()) &&
             Objects.equals(getSuper(), otherGeneric.getSuper());
   }
}
