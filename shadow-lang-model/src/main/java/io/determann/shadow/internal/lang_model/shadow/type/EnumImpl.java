package io.determann.shadow.internal.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.lang_model.shadow.type.EnumLangModel;
import io.determann.shadow.api.shadow.structure.EnumConstant;
import io.determann.shadow.implementation.support.api.shadow.type.EnumSupport;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import java.util.List;

public class EnumImpl extends DeclaredImpl implements EnumLangModel
{
   public EnumImpl(LangModelContext context, DeclaredType declaredTypeMirror)
   {
      super(context, declaredTypeMirror);
   }

   public EnumImpl(LangModelContext context, TypeElement typeElement)
   {
      super(context, typeElement);
   }

   @Override
   public List<EnumConstant> getEumConstants()
   {
      return getElement().getEnclosedElements()
                         .stream()
                         .filter(element -> ElementKind.ENUM_CONSTANT.equals(element.getKind()))
                         .map(VariableElement.class::cast)
                         .map(variableElement -> LangModelAdapter.<EnumConstant>generalize(getApi(), variableElement))
                         .toList();
   }

   @Override
   public boolean equals(Object other)
   {
      return EnumSupport.equals(this, other);
   }

   @Override
   public int hashCode()
   {
      return EnumSupport.hashCode(this);
   }

   @Override
   public String toString()
   {
      return EnumSupport.toString(this);
   }
}
