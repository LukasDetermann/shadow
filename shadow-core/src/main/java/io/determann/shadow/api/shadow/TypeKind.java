package io.determann.shadow.api.shadow;

public enum TypeKind
{
   //primitives
   BOOLEAN(true, false, false),
   BYTE(true, false, false),
   SHORT(true, false, false),
   INT(true, false, false),
   LONG(true, false, false),
   CHAR(true, false, false),
   FLOAT(true, false, false),
   DOUBLE(true, false, false),

   //declared
   CLASS(false, true, false),
   INTERFACE(false, true, false),
   ENUM(false, true, false),
   ANNOTATION(false, true, false),
   RECORD(false, true, false),

   //variables
   ENUM_CONSTANT(false, false, true),
   FIELD(false, false, true),
   PARAMETER(false, false, true),

   //strange stuff
   VOID(false, false, false),
   MODULE(false, false, false),
   NULL(false, false, false),

   ARRAY(false, false, false),

   GENERIC(false, false, false),

   WILDCARD(false, false, false),

   INTERSECTION(false, false, false),

   RECORD_COMPONENT(false, false, false);


   private final boolean primitive;
   private final boolean declared;
   private final boolean variable;

   TypeKind(boolean primitive, boolean declared, boolean variable)
   {
      this.primitive = primitive;
      this.declared = declared;
      this.variable = variable;
   }

   public boolean isPrimitive()
   {
      return primitive;
   }

   public boolean isDeclared()
   {
      return declared;
   }


   public boolean isVariable()
   {
      return variable;
   }
}
