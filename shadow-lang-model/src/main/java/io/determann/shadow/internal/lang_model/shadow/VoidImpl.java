package io.determann.shadow.internal.lang_model.shadow;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.shadow.Void;

import javax.lang.model.type.NoType;
import java.util.Objects;

public class VoidImpl extends ShadowImpl<NoType> implements Void
{
   public VoidImpl(LangModelContext context, NoType typeMirror)
   {
      super(context, typeMirror);
   }

   @Override
   public TypeKind getKind()
   {
      return TypeKind.VOID;
   }

   @Override
   public int hashCode()
   {
      return Objects.hashCode(getKind());
   }

   @Override
   public boolean equals(Object other)
   {
      return other instanceof Void;
   }
}