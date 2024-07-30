package io.determann.shadow.internal.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.shadow.TypeKind;
import io.determann.shadow.api.shadow.type.Void;
import io.determann.shadow.implementation.support.api.shadow.type.VoidSupport;

import javax.lang.model.type.NoType;

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
      return VoidSupport.hashCode(this);
   }

   @Override
   public boolean equals(Object other)
   {
      return VoidSupport.equals(this, other);
   }

   @Override
   public String toString()
   {
      return VoidSupport.toString(this);
   }
}