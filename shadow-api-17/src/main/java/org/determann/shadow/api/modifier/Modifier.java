package org.determann.shadow.api.modifier;


public enum Modifier
{
   PUBLIC,
   PROTECTED,
   PRIVATE,
   ABSTRACT,
   DEFAULT,
   STATIC,
   SEALED,
   NON_SEALED,
   FINAL,
   TRANSIENT,
   VOLATILE,
   SYNCHRONIZED,
   NATIVE,
   STRICTFP;

   public static Modifier mapModifier(javax.lang.model.element.Modifier modifier)
   {
      return switch (modifier)
            {
               case PUBLIC -> PUBLIC;
               case PROTECTED -> PROTECTED;
               case PRIVATE -> PRIVATE;
               case ABSTRACT -> ABSTRACT;
               case STATIC -> STATIC;
               case SEALED -> SEALED;
               case NON_SEALED -> NON_SEALED;
               case FINAL -> FINAL;
               case STRICTFP -> STRICTFP;
               case DEFAULT -> DEFAULT;
               case TRANSIENT -> TRANSIENT;
               case VOLATILE -> VOLATILE;
               case SYNCHRONIZED -> SYNCHRONIZED;
               case NATIVE -> NATIVE;
            };
   }
}
