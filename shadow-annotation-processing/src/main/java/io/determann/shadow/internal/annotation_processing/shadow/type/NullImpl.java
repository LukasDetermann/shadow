package io.determann.shadow.internal.annotation_processing.shadow.type;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.dsl.RenderingContext;

import javax.lang.model.type.NullType;
import java.util.Objects;

public class NullImpl extends TypeImpl<NullType> implements Ap.Null
{
   public NullImpl(Ap.Context context, NullType nullType)
   {
      super(context, nullType);
   }

   @Override
   public boolean representsSameType(Ap.Type type)
   {
      return type instanceof Ap.Null;
   }

   @Override
   public String renderName(RenderingContext renderingContext)
   {
      return "null";
   }

   @Override
   public String renderType(RenderingContext renderingContext)
   {
      return renderName(renderingContext);
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
