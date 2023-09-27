package io.determann.shadow.api.renderer;

public interface EnumRenderer
{
   /**
    * {@code public enum MyEnum {}}
    */
   String declaration();

   /**
    * <pre>{@code
    *    public enum MyEnum {
    *       //content
    *    }
    * }</pre>
    */
   String declaration(String content);

   /**
    * {@code private final} <b>MyEnum</b> {@code myEnum;}
    */
   String type();
}
