package io.determann.shadow.internal.lang_model.shadow;

import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.shadow.TypeKind;
import io.determann.shadow.api.shadow.type.Null;

import javax.lang.model.type.NullType;
import java.util.Objects;

public class NullImpl extends ShadowImpl<NullType> implements Null
{
   public NullImpl(LangModelContext LangModelContext, NullType nullType)
   {
      super(LangModelContext, nullType);
   }

   @Override
   public TypeKind getKind()
   {
      return TypeKind.NULL;
   }

   @Override
   public int hashCode()
   {
      return Objects.hashCode(getKind());
   }

   @Override
   public boolean equals(Object other)
   {
      return other instanceof Null;
   }
}
