package io.determann.shadow.api.modifier;


public enum Modifier
{
   PUBLIC,
   PROTECTED,
   PRIVATE,
   ABSTRACT,
   DEFAULT,
   STATIC,
   FINAL,
   TRANSIENT,
   VOLATILE,
   SYNCHRONIZED,
   NATIVE,
   STRICTFP;

   public static Modifier mapModifier(javax.lang.model.element.Modifier modifier)
   {
      switch (modifier)
      {
         case PUBLIC:
            return PUBLIC;
         case PROTECTED:
            return PROTECTED;
         case PRIVATE:
            return PRIVATE;
         case ABSTRACT:
            return ABSTRACT;
         case STATIC:
            return STATIC;
         case FINAL:
            return FINAL;
         case STRICTFP:
            return STRICTFP;
         case DEFAULT:
            return DEFAULT;
         case TRANSIENT:
            return TRANSIENT;
         case VOLATILE:
            return VOLATILE;
         case SYNCHRONIZED:
            return SYNCHRONIZED;
         case NATIVE:
            return NATIVE;
         default:
            throw new IllegalArgumentException();
      }
   }
}
