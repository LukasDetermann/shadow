package io.determann.shadow.internal.lang_model.shadow;

import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.property.ImmutableProperty;
import io.determann.shadow.api.property.MutableProperty;
import io.determann.shadow.api.property.Property;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.*;
import io.determann.shadow.internal.property.ImmutablePropertyImpl;
import io.determann.shadow.internal.property.MutablePropertyImpl;
import io.determann.shadow.internal.property.PropertyImpl;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Optional;

public class ClassImpl extends DeclaredImpl implements Class
{
   public ClassImpl(LangModelContext context, DeclaredType declaredTypeMirror)
   {
      super(context, declaredTypeMirror);
   }

   public ClassImpl(LangModelContext context, TypeElement typeElement)
   {
      super(context, typeElement);
   }

   @Override
   public boolean isAssignableFrom(Shadow shadow)
   {
      return LangModelAdapter.getTypes(getApi()).isAssignable(getMirror(), LangModelAdapter.particularType(shadow));
   }

   @Override
   public Class getSuperClass()
   {
      TypeMirror superclass = getElement().getSuperclass();
      if (javax.lang.model.type.TypeKind.NONE.equals(superclass.getKind()))
      {
         return null;
      }
      return LangModelAdapter.generalize(getApi(), superclass);
   }

   @Override
   public List<Class> getPermittedSubClasses()
   {
      return getElement().getPermittedSubclasses()
                         .stream()
                         .map(typeMirror -> LangModelAdapter.<Class>generalize(getApi(), typeMirror))
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
      return Optional.of(LangModelAdapter.generalize(getApi(), enclosingType));
   }

   @Override
   public List<Shadow> getGenericTypes()
   {
      return getMirror().getTypeArguments()
                        .stream()
                        .map(typeMirror -> LangModelAdapter.<Shadow>generalize(getApi(), typeMirror))
                        .toList();
   }

   @Override
   public List<Generic> getGenerics()
   {
      return getElement().getTypeParameters()
                         .stream()
                         .map(element -> LangModelAdapter.<Generic>generalize(getApi(), element))
                         .toList();
   }

   @Override
   public Primitive asUnboxed()
   {
      return LangModelAdapter.generalize(getApi(), LangModelAdapter.getTypes(getApi()).unboxedType(getMirror()));
   }
}
