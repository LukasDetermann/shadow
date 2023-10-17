package io.determann.shadow.impl.annotation_processing.shadow;

import io.determann.shadow.api.MirrorAdapter;
import io.determann.shadow.api.annotation_processing.AnnotationProcessingContext;
import io.determann.shadow.api.shadow.Generic;
import io.determann.shadow.api.shadow.Interface;
import io.determann.shadow.api.shadow.Shadow;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import java.util.List;

import static java.util.stream.Collectors.toUnmodifiableList;

public class InterfaceImpl extends DeclaredImpl implements Interface
{
   public InterfaceImpl(AnnotationProcessingContext annotationProcessingContext, DeclaredType declaredTypeMirror)
   {
      super(annotationProcessingContext, declaredTypeMirror);
   }

   public InterfaceImpl(AnnotationProcessingContext annotationProcessingContext, TypeElement typeElement)
   {
      super(annotationProcessingContext, typeElement);
   }

   @Override
   public boolean isFunctional()
   {
      return MirrorAdapter.getProcessingEnv(getApi()).getElementUtils().isFunctionalInterface(getElement());
   }

   @Override
   public List<Shadow> getGenerics()
   {
      return getMirror().getTypeArguments()
                        .stream()
                        .map(typeMirror -> MirrorAdapter.<Shadow>getShadow(getApi(), typeMirror))
                        .collect(toUnmodifiableList());
   }

   @Override
   public List<Generic> getFormalGenerics()// formal
   {
      return getElement().getTypeParameters()
                         .stream()
                         .map(element -> MirrorAdapter.<Generic>getShadow(getApi(), element))
                         .collect(toUnmodifiableList());
   }

   @Override
   public Interface interpolateGenerics()
   {
      return MirrorAdapter.getShadow(getApi(), MirrorAdapter.getProcessingEnv(getApi()).getTypeUtils().capture(getMirror()));
   }

   @Override
   public Interface erasure()
   {
      return MirrorAdapter.getShadow(getApi(), MirrorAdapter.getProcessingEnv(getApi()).getTypeUtils().erasure(getMirror()));
   }
}
