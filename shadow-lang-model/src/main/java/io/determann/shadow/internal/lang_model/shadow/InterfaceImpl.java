package io.determann.shadow.internal.lang_model.shadow;

import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.shadow.Generic;
import io.determann.shadow.api.shadow.Interface;
import io.determann.shadow.api.shadow.Shadow;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import java.util.List;

public class InterfaceImpl extends DeclaredImpl implements Interface
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
}
