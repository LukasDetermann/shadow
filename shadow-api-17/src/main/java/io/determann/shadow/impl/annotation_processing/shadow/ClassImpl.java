package io.determann.shadow.impl.annotation_processing.shadow;

import io.determann.shadow.api.MirrorAdapter;
import io.determann.shadow.api.annotation_processing.AnnotationProcessingContext;
import io.determann.shadow.api.property.ImmutableProperty;
import io.determann.shadow.api.property.MutableProperty;
import io.determann.shadow.api.property.Property;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.*;
import io.determann.shadow.impl.property.ImmutablePropertyImpl;
import io.determann.shadow.impl.property.MutablePropertyImpl;
import io.determann.shadow.impl.property.PropertyImpl;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Optional;

public class ClassImpl extends DeclaredImpl implements Class
{
   public ClassImpl(AnnotationProcessingContext annotationProcessingContext, DeclaredType declaredTypeMirror)
   {
      super(annotationProcessingContext, declaredTypeMirror);
   }

   public ClassImpl(AnnotationProcessingContext annotationProcessingContext, TypeElement typeElement)
   {
      super(annotationProcessingContext, typeElement);
   }

   @Override
   public boolean isAssignableFrom(Shadow shadow)
   {
      return MirrorAdapter.getProcessingEnv(getApi()).getTypeUtils().isAssignable(getMirror(), MirrorAdapter.getType(shadow));
   }

   @Override
   public Class getSuperClass()
   {
      TypeMirror superclass = getElement().getSuperclass();
      if (javax.lang.model.type.TypeKind.NONE.equals(superclass.getKind()))
      {
         return null;
      }
      return MirrorAdapter.getShadow(getApi(), superclass);
   }

   @Override
   public List<Class> getPermittedSubClasses()
   {
      return getElement().getPermittedSubclasses()
                         .stream()
                         .map(typeMirror -> MirrorAdapter.<Class>getShadow(getApi(), typeMirror))
                         .toList();
   }

   @Override
   public List<Property> getProperties()
   {
      return PropertyImpl.of(this);
   }

   @Override
   public List<MutableProperty> getMutableProperties()
   {
      return MutablePropertyImpl.of(this);
   }

   @Override
   public List<ImmutableProperty> getImmutableProperties()
   {
      return ImmutablePropertyImpl.of(this);
   }

   @Override
   public Optional<Declared> getOuterType()
   {
      TypeMirror enclosingType = getMirror().getEnclosingType();
      if (enclosingType.getKind().equals(javax.lang.model.type.TypeKind.NONE))
      {
         return Optional.empty();
      }
      return Optional.of(MirrorAdapter.getShadow(getApi(), enclosingType));
   }

   @Override
   public List<Shadow> getGenerics()
   {
      return getMirror().getTypeArguments()
                        .stream()
                        .map(typeMirror -> MirrorAdapter.<Shadow>getShadow(getApi(), typeMirror))
                        .toList();
   }

   @Override
   public List<Generic> getFormalGenerics()
   {
      return getElement().getTypeParameters()
                         .stream()
                         .map(element -> MirrorAdapter.<Generic>getShadow(getApi(), element))
                         .toList();
   }

   @Override
   public Class interpolateGenerics()
   {
      return MirrorAdapter.getShadow(getApi(), MirrorAdapter.getProcessingEnv(getApi()).getTypeUtils().capture(getMirror()));
   }

   @Override
   public Class erasure()
   {
      return MirrorAdapter.getShadow(getApi(), MirrorAdapter.getProcessingEnv(getApi()).getTypeUtils().erasure(getMirror()));
   }

   @Override
   public Primitive asUnboxed()
   {
      return MirrorAdapter.getShadow(getApi(), MirrorAdapter.getProcessingEnv(getApi()).getTypeUtils().unboxedType(getMirror()));
   }
}
