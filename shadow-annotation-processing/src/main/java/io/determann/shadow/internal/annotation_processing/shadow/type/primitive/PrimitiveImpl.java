package io.determann.shadow.internal.annotation_processing.shadow.type.primitive;

import io.determann.shadow.api.C;
import io.determann.shadow.api.annotation_processing.AP;
import io.determann.shadow.implementation.support.api.shadow.type.PrimitiveSupport;
import io.determann.shadow.internal.annotation_processing.shadow.type.TypeImpl;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.PrimitiveType;

import static io.determann.shadow.api.annotation_processing.adapter.Adapters.adapt;

public abstract class PrimitiveImpl extends TypeImpl<PrimitiveType>
{
   private final String name;

   public static class LM_booleanImpl extends PrimitiveImpl implements AP.boolean_
   {

      public LM_booleanImpl(AP.Context context, PrimitiveType primitiveTypeMirror)
      {
         super(context, primitiveTypeMirror, "boolean");
      }
   }

   public static class LM_byteImpl extends PrimitiveImpl implements AP.byte_
   {

      public LM_byteImpl(AP.Context context, PrimitiveType primitiveTypeMirror)
      {
         super(context, primitiveTypeMirror, "byte");
      }
   }

   public static class LM_charImpl extends PrimitiveImpl implements AP.char_
   {

      public LM_charImpl(AP.Context context, PrimitiveType primitiveTypeMirror)
      {
         super(context, primitiveTypeMirror, "char");
      }
   }

   public static class LM_doubleImpl extends PrimitiveImpl implements AP.double_
   {

      public LM_doubleImpl(AP.Context context, PrimitiveType primitiveTypeMirror)
      {
         super(context, primitiveTypeMirror, "double");
      }
   }

   public static class LM_floatImpl extends PrimitiveImpl implements AP.float_
   {

      public LM_floatImpl(AP.Context context, PrimitiveType primitiveTypeMirror)
      {
         super(context, primitiveTypeMirror, "float");
      }
   }

   public static class LM_intImpl extends PrimitiveImpl implements AP.int_
   {

      public LM_intImpl(AP.Context context, PrimitiveType primitiveTypeMirror)
      {
         super(context, primitiveTypeMirror, "int");
      }
   }

   public static class LM_longImpl extends PrimitiveImpl implements AP.long_
   {

      public LM_longImpl(AP.Context context, PrimitiveType primitiveTypeMirror)
      {
         super(context, primitiveTypeMirror, "long");
      }
   }

   public static class LM_shortImpl extends PrimitiveImpl implements AP.short_
   {

      public LM_shortImpl(AP.Context context, PrimitiveType primitiveTypeMirror)
      {
         super(context, primitiveTypeMirror, "short");
      }
   }

   protected PrimitiveImpl(AP.Context context, PrimitiveType primitiveTypeMirror, String name)
   {
      super(context, primitiveTypeMirror);
      this.name = name;
   }

   public boolean isSubtypeOf(C.Type type)
   {
      return adapt(getApi()).toTypes().isSubtype(adapt((AP.Type) type).toTypeMirror(), getMirror());
   }

   public boolean isAssignableFrom(C.Type type)
   {
      return adapt(getApi()).toTypes().isAssignable(getMirror(), adapt((AP.Type) type).toTypeMirror());
   }

   public AP.Class asBoxed()
   {
      return (AP.Class) adapt(getApi(), ((DeclaredType) adapt(getApi()).toTypes().boxedClass(getMirror()).asType()));
   }

   public AP.Array asArray()
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
