package io.determann.shadow.api.renderer;

public interface EnumRenderer
{
   /**
    * {@code public enum MyEnum {}}
    */
   String declaration(RenderingContext renderingContext);

   /**
    * <pre>{@code
    *    public enum MyEnum {
    *       //content
    *    }
    * }</pre>
    */
   String declaration(RenderingContext renderingContext, String content);

   /**
    * {@code private final} <b>MyEnum</b> {@code myEnum;}
    */
   String type(RenderingContext renderingContext);
}
