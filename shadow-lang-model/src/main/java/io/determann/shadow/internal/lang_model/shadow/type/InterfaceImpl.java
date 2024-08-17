package io.determann.shadow.internal.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.lang_model.shadow.type.GenericLangModel;
import io.determann.shadow.api.lang_model.shadow.type.InterfaceLangModel;
import io.determann.shadow.api.lang_model.shadow.type.ShadowLangModel;
import io.determann.shadow.implementation.support.api.shadow.type.InterfaceSupport;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import java.util.List;

public class InterfaceImpl extends DeclaredImpl implements InterfaceLangModel
{
   public InterfaceImpl(LangModelContext context, DeclaredType declaredTypeMirror)
   {
      super(context, declaredTypeMirror);
   }

   public InterfaceImpl(LangModelContext context, TypeElement typeElement)
   {
      super(context, typeElement);
   }

   @Override
   public boolean isFunctional()
   {
      return LangModelAdapter.getElements(getApi()).isFunctionalInterface(getElement());
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
   public boolean equals(Object other)
   {
      return InterfaceSupport.equals(this, other);
   }

   @Override
   public int hashCode()
   {
      return InterfaceSupport.hashCode(this);
   }

   @Override
   public String toString()
   {
      return InterfaceSupport.toString(this);
   }
}
