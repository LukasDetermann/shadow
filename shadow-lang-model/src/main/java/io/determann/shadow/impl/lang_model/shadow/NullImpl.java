package io.determann.shadow.impl.lang_model.shadow;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.shadow.Null;

import javax.lang.model.type.NullType;
import java.util.Objects;

public class NullImpl extends ShadowImpl<NullType> implements Null
{
   public NullImpl(LangModelContext LangModelContext, NullType nullType)
   {
      super(LangModelContext, nullType);
   }

   @Override
   public TypeKind getTypeKind()
   {
      return TypeKind.NULL;
   }

   @Override
   public int hashCode()
   {
      return Objects.hashCode(getTypeKind());
   }

   @Override
   public boolean equals(Object other)
   {
      return other instanceof Null;
   }
}
