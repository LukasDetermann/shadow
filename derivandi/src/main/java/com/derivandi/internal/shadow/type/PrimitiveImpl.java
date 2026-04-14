package com.derivandi.internal.shadow.type;

import com.derivandi.api.Ap;
import com.derivandi.api.dsl.RenderingContext;
import com.derivandi.api.processor.SimpleContext;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.PrimitiveType;
import java.util.Objects;

import static com.derivandi.api.adapter.Adapters.adapt;

public abstract class PrimitiveImpl extends TypeImpl<PrimitiveType>
{
   private final String name;

   public static class LM_booleanImpl extends PrimitiveImpl implements Ap.boolean_
   {
      public LM_booleanImpl(SimpleContext context, PrimitiveType primitiveTypeMirror)
      {
         super(context, primitiveTypeMirror, "boolean");
      }
   }

   public static class LM_byteImpl extends PrimitiveImpl implements Ap.byte_
   {
      public LM_byteImpl(SimpleContext context, PrimitiveType primitiveTypeMirror)
      {
         super(context, primitiveTypeMirror, "byte");
      }
   }

   public static class LM_charImpl extends PrimitiveImpl implements Ap.char_
   {
      public LM_charImpl(SimpleContext context, PrimitiveType primitiveTypeMirror)
      {
         super(context, primitiveTypeMirror, "char");
      }
   }

   public static class LM_doubleImpl extends PrimitiveImpl implements Ap.double_
   {
      public LM_doubleImpl(SimpleContext context, PrimitiveType primitiveTypeMirror)
      {
         super(context, primitiveTypeMirror, "double");
      }
   }

   public static class LM_floatImpl extends PrimitiveImpl implements Ap.float_
   {
      public LM_floatImpl(SimpleContext context, PrimitiveType primitiveTypeMirror)
      {
         super(context, primitiveTypeMirror, "float");
      }
   }

   public static class LM_intImpl extends PrimitiveImpl implements Ap.int_
   {
      public LM_intImpl(SimpleContext context, PrimitiveType primitiveTypeMirror)
      {
         super(context, primitiveTypeMirror, "int");
      }
   }

   public static class LM_longImpl extends PrimitiveImpl implements Ap.long_
   {
      public LM_longImpl(SimpleContext context, PrimitiveType primitiveTypeMirror)
      {
         super(context, primitiveTypeMirror, "long");
      }
   }

   public static class LM_shortImpl extends PrimitiveImpl implements Ap.short_
   {
      public LM_shortImpl(SimpleContext context, PrimitiveType primitiveTypeMirror)
      {
         super(context, primitiveTypeMirror, "short");
      }
   }

   private PrimitiveImpl(SimpleContext context, PrimitiveType primitiveTypeMirror, String name)
   {
      super(context, primitiveTypeMirror);
      this.name = name;
   }

   public boolean isSubtypeOf(Ap.Type type)
   {
      return adapt(getApi()).toTypes().isSubtype(adapt(type).toTypeMirror(), getMirror());
   }

   public boolean isAssignableFrom(Ap.Type type)
   {
      return adapt(getApi()).toTypes().isAssignable(getMirror(), adapt(type).toTypeMirror());
   }

   public Ap.Class asBoxed()
   {
      return (Ap.Class) adapt(getApi(), ((DeclaredType) adapt(getApi()).toTypes().boxedClass(getMirror()).asType()));
   }

   public Ap.Array asArray()
   {
      return adapt(getApi(), adapt(getApi()).toTypes().getArrayType(getMirror()));
   }

   public String getName()
   {
      return name;
   }

   public String renderType(RenderingContext renderingContext)
   {
      return renderName(renderingContext);
   }

   public String renderName(RenderingContext renderingContext)
   {
      return getName();
   }

   @Override
   public boolean equals(Object other)
   {
      return other instanceof Ap.Primitive primitive &&
             Objects.equals(getName(), primitive.getName());
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getName());
   }

   @Override
   public String toString()
   {
      return name;
   }
}
