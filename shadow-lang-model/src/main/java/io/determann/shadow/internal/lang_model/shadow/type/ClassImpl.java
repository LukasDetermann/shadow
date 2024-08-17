package io.determann.shadow.internal.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.lang_model.shadow.structure.PropertyLangModel;
import io.determann.shadow.api.lang_model.shadow.type.*;
import io.determann.shadow.api.shadow.type.Shadow;
import io.determann.shadow.implementation.support.api.shadow.structure.PropertySupport;
import io.determann.shadow.implementation.support.api.shadow.type.ClassSupport;
import io.determann.shadow.internal.lang_model.shadow.structure.PropertyImpl;

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
      return LangModelAdapter.getTypes(getApi()).isAssignable(getMirror(), LangModelAdapter.particularType((ShadowLangModel) shadow));
   }

   @Override
   public ClassLangModel getSuperClass()
   {
      TypeMirror superclass = getElement().getSuperclass();
      if (javax.lang.model.type.TypeKind.NONE.equals(superclass.getKind()))
      {
         return null;
      }
      return LangModelAdapter.generalize(getApi(), superclass);
   }

   @Override
   public List<ClassLangModel> getPermittedSubClasses()
   {
      return getElement().getPermittedSubclasses()
                         .stream()
                         .map(typeMirror -> LangModelAdapter.<ClassLangModel>generalize(getApi(), typeMirror))
                         .toList();
   }

   @Override
   public List<PropertyLangModel> getProperties()
   {
      return PropertySupport.propertiesOf(this)
                            .stream()
                            .map(PropertyImpl::new)
                            .map(PropertyLangModel.class::cast)
                            .toList();
   }

   @Override
   public Optional<DeclaredLangModel> getOuterType()
   {
      TypeMirror enclosingType = getMirror().getEnclosingType();
      if (enclosingType.getKind().equals(javax.lang.model.type.TypeKind.NONE))
      {
         return Optional.empty();
      }
      return Optional.of(LangModelAdapter.generalize(getApi(), enclosingType));
   }

   @Override
   public List<ShadowLangModel> getGenericTypes()
   {
      return getMirror().getTypeArguments()
                        .stream()
                        .map(typeMirror -> LangModelAdapter.<ShadowLangModel>generalize(getApi(), typeMirror))
                        .toList();
   }

   @Override
   public List<GenericLangModel> getGenerics()
   {
      return getElement().getTypeParameters()
                         .stream()
                         .map(element -> LangModelAdapter.<GenericLangModel>generalize(getApi(), element))
                         .toList();
   }

   @Override
   public PrimitiveLangModel asUnboxed()
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
