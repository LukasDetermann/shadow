package io.determann.shadow.impl.annotation_processing.shadow;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.annotation_processing.AnnotationProcessingContext;
import io.determann.shadow.api.annotation_processing.MirrorAdapter;
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

   public GenericImpl(AnnotationProcessingContext annotationProcessingContext, TypeParameterElement typeParameterElement)
   {
      super(annotationProcessingContext, ((TypeVariable) typeParameterElement.asType()));
      this.typeParameterElement = typeParameterElement;
   }

   public GenericImpl(AnnotationProcessingContext annotationProcessingContext, TypeVariable typeMirror)
   {
      super(annotationProcessingContext, typeMirror);
      this.typeParameterElement = (TypeParameterElement) MirrorAdapter.getProcessingEnv(getApi()).getTypeUtils().asElement(typeMirror);
   }

   @Override
   public Shadow getExtends()
   {
      return MirrorAdapter.getShadow(getApi(), getMirror().getUpperBound());
   }

   @Override
   public Optional<Shadow> getSuper()
   {
      TypeMirror lowerBound = getMirror().getLowerBound();
      if (lowerBound == null || lowerBound.getKind().equals(javax.lang.model.type.TypeKind.NONE))
      {
         return Optional.empty();
      }
      return Optional.of(MirrorAdapter.getShadow(getApi(), lowerBound));
   }

   @Override
   public Shadow getEnclosing()
   {
      return MirrorAdapter.getShadow(getApi(), getElement().getGenericElement());
   }

   @Override
   public TypeKind getTypeKind()
   {
      return TypeKind.GENERIC;
   }

   @Override
   public Package getPackage()
   {
      return MirrorAdapter
                     .getShadow(getApi(), MirrorAdapter.getProcessingEnv(getApi()).getElementUtils().getPackageOf(getElement()));
   }

   public TypeParameterElement getElement()
   {
      return typeParameterElement;
   }

   @Override
   public String getName()
   {
      return MirrorAdapter.getName(getElement());
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
