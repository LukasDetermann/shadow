package io.determann.shadow.internal.lang_model.shadow.type.primitive;

import io.determann.shadow.api.C;
import io.determann.shadow.api.lang_model.LM;
import io.determann.shadow.implementation.support.api.shadow.type.PrimitiveSupport;
import io.determann.shadow.internal.lang_model.shadow.type.TypeImpl;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.PrimitiveType;

import static io.determann.shadow.api.lang_model.adapter.Adapters.adapt;

public abstract class PrimitiveImpl extends TypeImpl<PrimitiveType>
{
   private final String name;

   public static class LM_booleanImpl extends PrimitiveImpl implements LM.boolean_
   {

      public LM_booleanImpl(LM.Context context, PrimitiveType primitiveTypeMirror)
      {
         super(context, primitiveTypeMirror, "boolean");
      }
   }

   public static class LM_byteImpl extends PrimitiveImpl implements LM.byte_
   {

      public LM_byteImpl(LM.Context context, PrimitiveType primitiveTypeMirror)
      {
         super(context, primitiveTypeMirror, "byte");
      }
   }

   public static class LM_charImpl extends PrimitiveImpl implements LM.char_
   {

      public LM_charImpl(LM.Context context, PrimitiveType primitiveTypeMirror)
      {
         super(context, primitiveTypeMirror, "char");
      }
   }

   public static class LM_doubleImpl extends PrimitiveImpl implements LM.double_
   {

      public LM_doubleImpl(LM.Context context, PrimitiveType primitiveTypeMirror)
      {
         super(context, primitiveTypeMirror, "double");
      }
   }

   public static class LM_floatImpl extends PrimitiveImpl implements LM.float_
   {

      public LM_floatImpl(LM.Context context, PrimitiveType primitiveTypeMirror)
      {
         super(context, primitiveTypeMirror, "float");
      }
   }

   public static class LM_intImpl extends PrimitiveImpl implements LM.int_
   {

      public LM_intImpl(LM.Context context, PrimitiveType primitiveTypeMirror)
      {
         super(context, primitiveTypeMirror, "int");
      }
   }

   public static class LM_longImpl extends PrimitiveImpl implements LM.long_
   {

      public LM_longImpl(LM.Context context, PrimitiveType primitiveTypeMirror)
      {
         super(context, primitiveTypeMirror, "long");
      }
   }

   public static class LM_shortImpl extends PrimitiveImpl implements LM.short_
   {

      public LM_shortImpl(LM.Context context, PrimitiveType primitiveTypeMirror)
      {
         super(context, primitiveTypeMirror, "short");
      }
   }

   protected PrimitiveImpl(LM.Context context, PrimitiveType primitiveTypeMirror, String name)
   {
      super(context, primitiveTypeMirror);
      this.name = name;
   }

   public boolean isSubtypeOf(C.Type type)
   {
      return adapt(getApi()).toTypes().isSubtype(adapt((LM.Type) type).toTypeMirror(), getMirror());
   }

   public boolean isAssignableFrom(C.Type type)
   {
      return adapt(getApi()).toTypes().isAssignable(getMirror(), adapt((LM.Type) type).toTypeMirror());
   }

   public LM.Class asBoxed()
   {
      return (LM.Class) adapt(getApi(), ((DeclaredType) adapt(getApi()).toTypes().boxedClass(getMirror()).asType()));
   }

   public LM.Array asArray()
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
