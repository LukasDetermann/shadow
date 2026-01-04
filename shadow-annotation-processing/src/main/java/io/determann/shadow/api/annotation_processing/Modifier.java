package io.determann.shadow.api.annotation_processing;


import io.determann.shadow.api.annotation_processing.dsl.RenderingContext;

public enum Modifier
{
   PUBLIC("public"),
   PROTECTED("protected"),
   PRIVATE("private"),
   ABSTRACT("abstract"),
   PACKAGE_PRIVATE(""),
   DEFAULT("default"),
   STATIC("static"),
   SEALED("sealed"),
   NON_SEALED("non-sealed"),
   FINAL("final"),
   TRANSIENT("transient"),
   VOLATILE("volatile"),
   SYNCHRONIZED("synchronized"),
   NATIVE("native"),
   STRICTFP("strictfp");

   private final String rendered;

   Modifier(String rendered)
   {
      this.rendered = rendered;
   }

   public String render(RenderingContext renderingContext)
   {
      return rendered;
   }
}
