package io.determann.shadow.internal.lang_model.shadow;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.shadow.Array;
import io.determann.shadow.api.shadow.Shadow;

import javax.lang.model.type.ArrayType;
import java.util.List;
import java.util.Objects;

public final class ArrayImpl extends ShadowImpl<ArrayType> implements Array
{

   public ArrayImpl(LangModelContext context, ArrayType arrayType)
   {
      super(context, arrayType);
   }

   @Override
   public boolean isSubtypeOf(Shadow shadow)
   {
      return LangModelAdapter.getTypes(getApi()).isSubtype(LangModelAdapter.getType(shadow), getMirror());
   }

   @Override
   public Shadow getComponentType()
   {
      return LangModelAdapter.getShadow(getApi(), getMirror().getComponentType());
   }

   @Override
   public List<Shadow> getDirectSuperTypes()
   {
      return LangModelAdapter.getTypes(getApi())
                             .directSupertypes(getMirror())
                             .stream()
                             .map(typeMirror1 -> LangModelAdapter.<Shadow>getShadow(getApi(), typeMirror1))
                             .toList();
   }

   @Override
   public TypeKind getTypeKind()
   {
      return TypeKind.ARRAY;
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getComponentType());
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (!(other instanceof Array otherArray))
      {
         return false;
      }
      return Objects.equals(getComponentType(), otherArray.getComponentType());
   }
}
