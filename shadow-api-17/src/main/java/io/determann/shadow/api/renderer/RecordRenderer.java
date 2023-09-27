package io.determann.shadow.api.renderer;

public interface RecordRenderer
{
   /**
    * {@code public record MyRecord() {}}
    */
   String declaration();

   /**
    * <pre>{@code
    *    public record MyRecord() {
    *       //content
    *    }
    * }</pre>
    */
   String declaration(String content);

   /**
    * {@code private final} <b>MyRecord</b> {@code myCRecord;}
    */
   String type();
}
