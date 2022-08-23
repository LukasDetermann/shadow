package org.determann.shadow.api;

public enum TypeKind
{
   //primitives
   BOOLEAN(true, false, false, false, false),
   BYTE(true, false, false, false, false),
   SHORT(true, false, false, false, false),
   INT(true, false, false, false, false),
   LONG(true, false, false, false, false),
   CHAR(true, false, false, false, false),
   FLOAT(true, false, false, false, false),
   DOUBLE(true, false, false, false, false),

   //declared
   CLASS(false, false, true, false, false),
   INTERFACE(false, false, true, false, false),
   ENUM(false, false, true, false, false),
   ANNOTATION(false, false, true, false, false),

   //executable
   METHOD(false, false, false, true, false),
   CONSTRUCTOR(false, false, false, true, false),

   //variables
   ENUM_CONSTANT(false, false, false, false, true),
   FIELD(false, false, false, false, true),
   PARAMETER(false, false, false, false, true),

   //strange stuff
   VOID(false, true, false, false, false),
   PACKAGE(false, true, false, false, false),
   MODULE(false, true, false, false, false),
   NULL(false, false, false, false, false),

   ARRAY(false, false, false, false, false),

   GENERIC_TYPE(false, false, false, false, false),

   UNION(false, false, false, false, false),

   WILDCARD(false, false, false, false, false),

   INTERSECTION(false, false, false, false, false);


   private final boolean primitive;
   private final boolean noType;
   private final boolean declared;
   private final boolean executable;
   private final boolean variable;

   TypeKind(boolean primitive, boolean noType, boolean declared, boolean executable, boolean variable)
   {
      this.primitive = primitive;
      this.noType = noType;
      this.declared = declared;
      this.executable = executable;
      this.variable = variable;
   }

   public boolean isPrimitive()
   {
      return primitive;
   }

   public boolean isNoType()
   {
      return noType;
   }

   public boolean isDeclared()
   {
      return declared;
   }

   public boolean isExecutable()
   {
      return executable;
   }

   public boolean isVariable()
   {
      return variable;
   }
}
