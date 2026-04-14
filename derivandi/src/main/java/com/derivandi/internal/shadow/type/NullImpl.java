package com.derivandi.internal.shadow.type;

import com.derivandi.api.Ap;
import com.derivandi.api.dsl.RenderingContext;
import com.derivandi.api.processor.SimpleContext;

import javax.lang.model.type.NullType;
import java.util.Objects;

public class NullImpl extends TypeImpl<NullType> implements Ap.Null
{
   public NullImpl(SimpleContext context, NullType nullType)
   {
      super(context, nullType);
   }

   @Override
   public boolean isSameType(Ap.Type type)
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
