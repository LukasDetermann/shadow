package io.determann.shadow.internal.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.LM_Adapter;
import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Property;
import io.determann.shadow.api.lang_model.shadow.type.LM_Class;
import io.determann.shadow.api.lang_model.shadow.type.LM_Declared;
import io.determann.shadow.api.lang_model.shadow.type.LM_Generic;
import io.determann.shadow.api.lang_model.shadow.type.LM_Type;
import io.determann.shadow.api.lang_model.shadow.type.primitive.LM_Primitive;
import io.determann.shadow.api.shadow.type.C_Type;
import io.determann.shadow.implementation.support.api.shadow.structure.PropertySupport;
import io.determann.shadow.implementation.support.api.shadow.type.ClassSupport;
import io.determann.shadow.internal.lang_model.shadow.structure.PropertyImpl;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Optional;

import static io.determann.shadow.api.lang_model.LM_Adapter.generalize;
import static io.determann.shadow.api.lang_model.LM_Adapter.getTypes;

public class ClassImpl extends DeclaredImpl implements LM_Class
{
   public ClassImpl(LM_Context context, DeclaredType declaredTypeMirror)
   {
      super(context, declaredTypeMirror);
   }

   public ClassImpl(LM_Context context, TypeElement typeElement)
   {
      super(context, typeElement);
   }

   @Override
   public boolean isAssignableFrom(C_Type type)
   {
      return getTypes(getApi()).isAssignable(getMirror(), LM_Adapter.particularType((LM_Type) type));
   }

   @Override
   public LM_Class getSuperClass()
   {
      TypeMirror superclass = getElement().getSuperclass();
      if (javax.lang.model.type.TypeKind.NONE.equals(superclass.getKind()))
      {
         return null;
      }
      return generalize(getApi(), superclass);
   }

   @Override
   public List<LM_Class> getPermittedSubClasses()
   {
      return getElement().getPermittedSubclasses()
                         .stream()
                         .map(typeMirror -> LM_Adapter.<LM_Class>generalize(getApi(), typeMirror))
                         .toList();
   }

   @Override
   public List<LM_Property> getProperties()
   {
      return PropertySupport.propertiesOf(this)
                            .stream()
                            .map(PropertyImpl::new)
                            .map(LM_Property.class::cast)
                            .toList();
   }

   @Override
   public Optional<LM_Declared> getOuterType()
   {
      TypeMirror enclosingType = getMirror().getEnclosingType();
      if (enclosingType.getKind().equals(javax.lang.model.type.TypeKind.NONE))
      {
         return Optional.empty();
      }
      return Optional.of(generalize(getApi(), enclosingType));
   }

   @Override
   public List<LM_Type> getGenericTypes()
   {
      return getMirror().getTypeArguments()
                        .stream()
                        .map(typeMirror -> LM_Adapter.<LM_Type>generalize(getApi(), typeMirror))
                        .toList();
   }

   @Override
   public List<LM_Generic> getGenerics()
   {
      return getElement().getTypeParameters()
                         .stream()
                         .map(element -> LM_Adapter.<LM_Generic>generalize(getApi(), element))
                         .toList();
   }

   @Override
   public LM_Primitive asUnboxed()
   {
      return generalize(getApi(), getTypes(getApi()).unboxedType(getMirror()));
   }

   @Override
   public LM_Class erasure()
   {
      return generalize(getApi(), getTypes(getApi()).erasure(getMirror()));
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
