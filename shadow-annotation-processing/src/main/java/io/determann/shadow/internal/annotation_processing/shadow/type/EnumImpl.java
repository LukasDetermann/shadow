package io.determann.shadow.internal.annotation_processing.shadow.type;

import io.determann.shadow.api.C;
import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.adapter.Adapters;
import io.determann.shadow.implementation.support.api.shadow.type.EnumSupport;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import java.util.List;

public class EnumImpl extends DeclaredImpl implements Ap.Enum
{
   public EnumImpl(Ap.Context context, DeclaredType declaredTypeMirror)
   {
      super(context, declaredTypeMirror);
   }

   public EnumImpl(Ap.Context context, TypeElement typeElement)
   {
      super(context, typeElement);
   }

   @Override
   public List<Ap.EnumConstant> getEumConstants()
   {
      return getElement().getEnclosedElements()
                         .stream()
                         .filter(element -> ElementKind.ENUM_CONSTANT.equals(element.getKind()))
                         .map(VariableElement.class::cast)
                         .map(variableElement -> Adapters.adapt(getApi(), variableElement))
                         .map(Ap.EnumConstant.class::cast)
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
