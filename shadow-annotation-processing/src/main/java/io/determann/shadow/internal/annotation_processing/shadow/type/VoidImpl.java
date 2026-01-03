package io.determann.shadow.internal.annotation_processing.shadow.type;

import io.determann.shadow.api.annotation_processing.Ap;

import javax.lang.model.type.NoType;
import java.util.Objects;

public class VoidImpl
      extends TypeImpl<NoType>
      implements Ap.Void
{
   public VoidImpl(Ap.Context context, NoType typeMirror)
   {
      super(context, typeMirror);
   }

   @Override
   public boolean equals(Object other)
   {
      return other instanceof Ap.Void;
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(Ap.Void.class);
   }

   @Override
   public String toString()
   {
      return "void";
   }
}