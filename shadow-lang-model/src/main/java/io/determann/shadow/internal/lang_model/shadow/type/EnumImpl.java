package io.determann.shadow.internal.lang_model.shadow.type;

import io.determann.shadow.api.C;
import io.determann.shadow.api.lang_model.LM;
import io.determann.shadow.api.lang_model.adapter.Adapters;
import io.determann.shadow.implementation.support.api.shadow.type.EnumSupport;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import java.util.List;

public class EnumImpl extends DeclaredImpl implements LM.Enum
{
   public EnumImpl(LM.Context context, DeclaredType declaredTypeMirror)
   {
      super(context, declaredTypeMirror);
   }

   public EnumImpl(LM.Context context, TypeElement typeElement)
   {
      super(context, typeElement);
   }

   @Override
   public List<LM.EnumConstant> getEumConstants()
   {
      return getElement().getEnclosedElements()
                         .stream()
                         .filter(element -> ElementKind.ENUM_CONSTANT.equals(element.getKind()))
                         .map(VariableElement.class::cast)
                         .map(variableElement -> Adapters.adapt(getApi(), variableElement))
                         .map(LM.EnumConstant.class::cast)
                         .toList();
   }

   @Override
   public boolean representsSameType(C.Type type)
   {
      return EnumSupport.representsSameType(this, type);
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
