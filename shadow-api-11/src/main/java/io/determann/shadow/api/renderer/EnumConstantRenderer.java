package io.determann.shadow.api.renderer;

public interface EnumConstantRenderer
{
   /**
    * {@code MY_CONSTANT}
    */
   String declaration();

   /**
    * {@code MY_CONSTANT(parameters)}
    */
   String declaration(String parameters);

   /**
    * <pre>{@code
    *    MY_CONSTANT(parameters) {
    *       //content
    *    }
    * }</pre>
    */
   String declaration(String parameters, String content);

   /**
    * {@code MyEnum.MY_CONSTANT}
    */
   String invocation();
}
