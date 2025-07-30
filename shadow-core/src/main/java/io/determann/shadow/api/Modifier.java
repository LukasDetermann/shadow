package io.determann.shadow.api;


import io.determann.shadow.api.dsl.Renderable;
import io.determann.shadow.api.renderer.RenderingContext;

public enum Modifier
      implements Renderable
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

   @Override
   public String render(RenderingContext renderingContext)
   {
      return rendered;
   }
}
