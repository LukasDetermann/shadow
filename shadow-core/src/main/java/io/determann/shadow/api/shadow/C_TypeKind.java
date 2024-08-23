package io.determann.shadow.api.shadow;

public enum C_TypeKind
{
   //primitives
   BOOLEAN(true, false),
   BYTE(true, false),
   SHORT(true, false),
   INT(true, false),
   LONG(true, false),
   CHAR(true, false),
   FLOAT(true, false),
   DOUBLE(true, false),

   //declared
   CLASS(false, true),
   INTERFACE(false, true),
   ENUM(false, true),
   ANNOTATION(false, true),
   RECORD(false, true),

   //strange stuff
   VOID(false, false),
   NULL(false, false),

   ARRAY(false, false),

   GENERIC(false, false),

   WILDCARD(false, false),

   INTERSECTION(false, false);

   private final boolean primitive;
   private final boolean declared;

   C_TypeKind(boolean primitive, boolean declared)
   {
      this.primitive = primitive;
      this.declared = declared;
   }

   public boolean isPrimitive()
   {
      return primitive;
   }

   public boolean isDeclared()
   {
      return declared;
   }
}
