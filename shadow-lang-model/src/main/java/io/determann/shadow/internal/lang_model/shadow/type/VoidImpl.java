package io.determann.shadow.internal.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.shadow.type.C_Void;
import io.determann.shadow.implementation.support.api.shadow.type.VoidSupport;

import javax.lang.model.type.NoType;

public class VoidImpl extends TypeImpl<NoType> implements C_Void
{
   public VoidImpl(LM_Context context, NoType typeMirror)
   {
      super(context, typeMirror);
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