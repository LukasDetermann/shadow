package io.determann.shadow.internal.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.lang_model.shadow.type.ArrayLangModel;
import io.determann.shadow.api.lang_model.shadow.type.ShadowLangModel;
import io.determann.shadow.api.shadow.TypeKind;
import io.determann.shadow.api.shadow.type.Shadow;
import io.determann.shadow.implementation.support.api.shadow.type.ArraySupport;

import javax.lang.model.type.ArrayType;
import java.util.List;

public final class ArrayImpl extends ShadowImpl<ArrayType> implements ArrayLangModel
{

   public ArrayImpl(LangModelContext context, ArrayType arrayType)
   {
      super(context, arrayType);
   }

   @Override
   public boolean isSubtypeOf(Shadow shadow)
   {
      return LangModelAdapter.getTypes(getApi()).isSubtype(LangModelAdapter.particularType((ShadowLangModel) shadow), getMirror());
   }

   @Override
   public ShadowLangModel getComponentType()
   {
      return LangModelAdapter.generalize(getApi(), getMirror().getComponentType());
   }

   @Override
   public List<ShadowLangModel> getDirectSuperTypes()
   {
      return LangModelAdapter.getTypes(getApi())
                             .directSupertypes(getMirror())
                             .stream()
                             .map(typeMirror1 -> LangModelAdapter.<ShadowLangModel>generalize(getApi(), typeMirror1))
                             .toList();
   }

   @Override
   public TypeKind getKind()
   {
      return TypeKind.ARRAY;
   }

   @Override
   public boolean representsSameType(Shadow shadow)
   {
      return ArraySupport.representsSameType(this, shadow);
   }

   @Override
   public int hashCode()
   {
      return ArraySupport.hashCode(this);
   }

   @Override
   public boolean equals(Object other)
   {
      return ArraySupport.equals(this, other);
   }

   @Override
   public String toString()
   {
      return ArraySupport.toString(this);
   }
}
