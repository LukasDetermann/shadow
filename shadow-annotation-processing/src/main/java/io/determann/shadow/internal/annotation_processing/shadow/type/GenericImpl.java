package io.determann.shadow.internal.annotation_processing.shadow.type;

import io.determann.shadow.api.annotation_processing.AP;
import io.determann.shadow.implementation.support.api.shadow.type.GenericSupport;

import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.IntersectionType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static io.determann.shadow.api.annotation_processing.adapter.Adapters.adapt;

public class GenericImpl extends TypeImpl<TypeVariable> implements AP.Generic
{
   private final TypeParameterElement typeParameterElement;

   public GenericImpl(AP.Context context, TypeParameterElement typeParameterElement)
   {
      super(context, ((TypeVariable) typeParameterElement.asType()));
      this.typeParameterElement = typeParameterElement;
   }

   public GenericImpl(AP.Context context, TypeVariable typeMirror)
   {
      super(context, typeMirror);
      this.typeParameterElement = (TypeParameterElement) adapt(getApi()).toTypes().asElement(typeMirror);
   }

   @Override
   public AP.Type getBound()
   {
      return getBounds().getFirst();
   }

   @Override
   public List<AP.Type> getBounds()
   {
      TypeMirror upperBound = getMirror().getUpperBound();
      if (upperBound instanceof IntersectionType intersectionType)
      {
         return intersectionType.getBounds().stream()
                                .map(typeMirror -> adapt(getApi(), typeMirror))
                                .toList();
      }
      return Collections.singletonList(adapt(getApi(), upperBound));
   }

   @Override
   public List<AP.Interface> getAdditionalBounds()
   {
      List<AP.Type> bounds = getBounds();
      if (bounds.size() <= 1)
      {
         return Collections.emptyList();
      }
      return bounds.stream().skip(1)
                   .map(AP.Interface.class::cast)
                   .toList();
   }

   @Override
   public Optional<AP.Type> getSuper()
   {
      TypeMirror lowerBound = getMirror().getLowerBound();
      if (lowerBound == null || lowerBound.getKind().equals(javax.lang.model.type.TypeKind.NONE))
      {
         return Optional.empty();
      }
      return Optional.of(adapt(getApi(), lowerBound));
   }

   @Override
   public Object getEnclosing()
   {
      return adapt(getApi(), getElement().getGenericElement());
   }

   public TypeParameterElement getElement()
   {
      return typeParameterElement;
   }

   @Override
   public String getName()
   {
      return getElement().getSimpleName().toString();
   }

   @Override
   public List<AP.AnnotationUsage> getAnnotationUsages()
   {
      return adapt(getApi(), adapt(getApi()).toElements().getAllAnnotationMirrors(getElement()));
   }

   @Override
   public List<AP.AnnotationUsage> getDirectAnnotationUsages()
   {
      return adapt(getApi(), getElement().getAnnotationMirrors());
   }

   @Override
   public AP.Generic erasure()
   {
      return (AP.Generic) adapt(getApi(), adapt(getApi()).toTypes().erasure(getMirror()));
   }

   @Override
   public String toString()
   {
      return GenericSupport.toString(this);
   }

   @Override
   public int hashCode()
   {
      return GenericSupport.hashCode(this);
   }

   @Override
   public boolean equals(Object other)
   {
      return GenericSupport.equals(this, other);
   }
}
