package io.determann.shadow.internal.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.LM;
import io.determann.shadow.implementation.support.api.shadow.type.VoidSupport;

import javax.lang.model.type.NoType;

public class VoidImpl extends TypeImpl<NoType> implements LM.Void
{
   public VoidImpl(LM.Context context, NoType typeMirror)
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