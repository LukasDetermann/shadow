package io.determann.shadow.internal.lang_model.shadow.type.primitive;

import io.determann.shadow.api.lang_model.LM_Adapter;
import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.lang_model.shadow.type.LM_Array;
import io.determann.shadow.api.lang_model.shadow.type.LM_Class;
import io.determann.shadow.api.lang_model.shadow.type.LM_Type;
import io.determann.shadow.api.lang_model.shadow.type.primitive.*;
import io.determann.shadow.api.shadow.type.C_Type;
import io.determann.shadow.api.shadow.type.primitive.C_Primitive;
import io.determann.shadow.implementation.support.api.shadow.type.PrimitiveSupport;
import io.determann.shadow.internal.lang_model.shadow.type.TypeImpl;

import javax.lang.model.type.PrimitiveType;

public abstract class PrimitiveImpl extends TypeImpl<PrimitiveType>
{
   private final String name;

   public static class LM_booleanImpl extends PrimitiveImpl implements LM_boolean
   {

      public LM_booleanImpl(LM_Context context, PrimitiveType primitiveTypeMirror)
      {
         super(context, primitiveTypeMirror, "boolean");
      }
   }

   public static class LM_byteImpl extends PrimitiveImpl implements LM_byte
   {

      public LM_byteImpl(LM_Context context, PrimitiveType primitiveTypeMirror)
      {
         super(context, primitiveTypeMirror, "byte");
      }
   }

   public static class LM_charImpl extends PrimitiveImpl implements LM_char
   {

      public LM_charImpl(LM_Context context, PrimitiveType primitiveTypeMirror)
      {
         super(context, primitiveTypeMirror, "char");
      }
   }

   public static class LM_doubleImpl extends PrimitiveImpl implements LM_double
   {

      public LM_doubleImpl(LM_Context context, PrimitiveType primitiveTypeMirror)
      {
         super(context, primitiveTypeMirror, "double");
      }
   }

   public static class LM_floatImpl extends PrimitiveImpl implements LM_float
   {

      public LM_floatImpl(LM_Context context, PrimitiveType primitiveTypeMirror)
      {
         super(context, primitiveTypeMirror, "float");
      }
   }

   public static class LM_intImpl extends PrimitiveImpl implements LM_int
   {

      public LM_intImpl(LM_Context context, PrimitiveType primitiveTypeMirror)
      {
         super(context, primitiveTypeMirror, "int");
      }
   }

   public static class LM_longImpl extends PrimitiveImpl implements LM_long
   {

      public LM_longImpl(LM_Context context, PrimitiveType primitiveTypeMirror)
      {
         super(context, primitiveTypeMirror, "long");
      }
   }

   public static class LM_shortImpl extends PrimitiveImpl implements LM_short
   {

      public LM_shortImpl(LM_Context context, PrimitiveType primitiveTypeMirror)
      {
         super(context, primitiveTypeMirror, "short");
      }
   }

   protected PrimitiveImpl(LM_Context context, PrimitiveType primitiveTypeMirror, String name)
   {
      super(context, primitiveTypeMirror);
      this.name = name;
   }

   public boolean isSubtypeOf(C_Type type)
   {
      return LM_Adapter.getTypes(getApi()).isSubtype(LM_Adapter.particularType((LM_Type) type), getMirror());
   }

   public boolean isAssignableFrom(C_Type type)
   {
      return LM_Adapter.getTypes(getApi()).isAssignable(getMirror(), LM_Adapter.particularType((LM_Type) type));
   }

   public LM_Class asBoxed()
   {
      return LM_Adapter.generalize(getApi(), LM_Adapter.getTypes(getApi()).boxedClass(getMirror()).asType());
   }

   public LM_Array asArray()
   {
      return LM_Adapter.generalize(getApi(), LM_Adapter.getTypes(getApi()).getArrayType(getMirror()));
   }

   public String getName()
   {
      return name;
   }

   @Override
   public int hashCode()
   {
      return PrimitiveSupport.hashCode((C_Primitive) this);
   }

   @Override
   public boolean equals(Object other)
   {
      return PrimitiveSupport.equals((C_Primitive) this, other);
   }

   @Override
   public String toString()
   {
      return PrimitiveSupport.toString((C_Primitive) this);
   }
}
