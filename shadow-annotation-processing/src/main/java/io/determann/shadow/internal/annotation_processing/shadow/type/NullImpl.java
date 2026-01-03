package io.determann.shadow.internal.annotation_processing.shadow.type;

import io.determann.shadow.api.C;
import io.determann.shadow.api.annotation_processing.Ap;

import javax.lang.model.type.NullType;
import java.util.Objects;

public class NullImpl extends TypeImpl<NullType> implements Ap.Null
{
   public NullImpl(Ap.Context LangModelContext, NullType nullType)
   {
      super(LangModelContext, nullType);
   }

   @Override
   public boolean representsSameType(C.Type type)
   {
      return type instanceof C.Null;
   }

   @Override
   public boolean equals(Object other)
   {
      return other instanceof Ap.Null;
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(Ap.Null.class);
   }

   @Override
   public String toString()
   {
      return "Null";
   }
}
