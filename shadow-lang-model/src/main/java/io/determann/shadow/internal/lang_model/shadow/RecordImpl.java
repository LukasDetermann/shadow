package io.determann.shadow.internal.lang_model.shadow;

import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.lang_model.shadow.type.RecordLangModel;
import io.determann.shadow.api.shadow.structure.RecordComponent;
import io.determann.shadow.api.shadow.type.Generic;
import io.determann.shadow.api.shadow.type.Shadow;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import java.util.List;

public class RecordImpl extends DeclaredImpl implements RecordLangModel
{
   public RecordImpl(LangModelContext context, DeclaredType declaredTypeMirror)
   {
      super(context, declaredTypeMirror);
   }

   public RecordImpl(LangModelContext context, TypeElement typeElement)
   {
      super(context, typeElement);
   }

   @Override
   public List<RecordComponent> getRecordComponents()
   {
      return getElement().getRecordComponents()
                         .stream()
                         .map(recordComponentElement -> LangModelAdapter.<RecordComponent>generalize(getApi(), recordComponentElement))
                         .toList();
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
