package io.determann.shadow.internal.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.lang_model.shadow.type.ClassLangModel;
import io.determann.shadow.api.shadow.property.ImmutableProperty;
import io.determann.shadow.api.shadow.property.MutableProperty;
import io.determann.shadow.api.shadow.property.Property;
import io.determann.shadow.api.shadow.type.Class;
import io.determann.shadow.api.shadow.type.*;
import io.determann.shadow.implementation.support.api.PropertyFactory;
import io.determann.shadow.implementation.support.api.shadow.type.ClassSupport;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Optional;

public class ClassImpl extends DeclaredImpl implements ClassLangModel
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
      return PropertyFactory.propertyOf(this);
   }

   @Override
   public List<MutableProperty> getMutableProperties()
   {
      return PropertyFactory.mutablePropertyOf(this);
   }

   @Override
   public List<ImmutableProperty> getImmutableProperties()
   {
      return PropertyFactory.immutablePropertyOf(this);
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

   @Override
   public boolean equals(Object other)
   {
      return ClassSupport.equals(this, other);
   }

   @Override
   public int hashCode()
   {
      return ClassSupport.hashCode(this);
   }

   @Override
   public String toString()
   {
      return ClassSupport.toString(this);
   }
}
