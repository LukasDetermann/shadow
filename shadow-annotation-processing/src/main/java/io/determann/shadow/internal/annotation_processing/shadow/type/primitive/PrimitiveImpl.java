package io.determann.shadow.internal.annotation_processing.shadow.type.primitive;

import io.determann.shadow.api.C;
import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.implementation.support.api.shadow.type.PrimitiveSupport;
import io.determann.shadow.internal.annotation_processing.shadow.type.TypeImpl;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.PrimitiveType;

import static io.determann.shadow.api.annotation_processing.adapter.Adapters.adapt;

public abstract class PrimitiveImpl extends TypeImpl<PrimitiveType>
{
   private final String name;

   public static class LM_booleanImpl extends PrimitiveImpl implements Ap.boolean_
   {

      public LM_booleanImpl(Ap.Context context, PrimitiveType primitiveTypeMirror)
      {
         super(context, primitiveTypeMirror, "boolean");
      }
   }

   public static class LM_byteImpl extends PrimitiveImpl implements Ap.byte_
   {

      public LM_byteImpl(Ap.Context context, PrimitiveType primitiveTypeMirror)
      {
         super(context, primitiveTypeMirror, "byte");
      }
   }

   public static class LM_charImpl extends PrimitiveImpl implements Ap.char_
   {

      public LM_charImpl(Ap.Context context, PrimitiveType primitiveTypeMirror)
      {
         super(context, primitiveTypeMirror, "char");
      }
   }

   public static class LM_doubleImpl extends PrimitiveImpl implements Ap.double_
   {

      public LM_doubleImpl(Ap.Context context, PrimitiveType primitiveTypeMirror)
      {
         super(context, primitiveTypeMirror, "double");
      }
   }

   public static class LM_floatImpl extends PrimitiveImpl implements Ap.float_
   {

      public LM_floatImpl(Ap.Context context, PrimitiveType primitiveTypeMirror)
      {
         super(context, primitiveTypeMirror, "float");
      }
   }

   public static class LM_intImpl extends PrimitiveImpl implements Ap.int_
   {

      public LM_intImpl(Ap.Context context, PrimitiveType primitiveTypeMirror)
      {
         super(context, primitiveTypeMirror, "int");
      }
   }

   public static class LM_longImpl extends PrimitiveImpl implements Ap.long_
   {

      public LM_longImpl(Ap.Context context, PrimitiveType primitiveTypeMirror)
      {
         super(context, primitiveTypeMirror, "long");
      }
   }

   public static class LM_shortImpl extends PrimitiveImpl implements Ap.short_
   {

      public LM_shortImpl(Ap.Context context, PrimitiveType primitiveTypeMirror)
      {
         super(context, primitiveTypeMirror, "short");
      }
   }

   protected PrimitiveImpl(Ap.Context context, PrimitiveType primitiveTypeMirror, String name)
   {
      super(context, primitiveTypeMirror);
      this.name = name;
   }

   public boolean isSubtypeOf(C.Type type)
   {
      return adapt(getApi()).toTypes().isSubtype(adapt((Ap.Type) type).toTypeMirror(), getMirror());
   }

   public boolean isAssignableFrom(C.Type type)
   {
      return adapt(getApi()).toTypes().isAssignable(getMirror(), adapt((Ap.Type) type).toTypeMirror());
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

   @Override
   public int hashCode()
   {
      return PrimitiveSupport.hashCode((C.Primitive) this);
   }

   @Override
   public boolean equals(Object other)
   {
      return PrimitiveSupport.equals((C.Primitive) this, other);
   }

   @Override
   public String toString()
   {
      return PrimitiveSupport.toString((C.Primitive) this);
   }
}
