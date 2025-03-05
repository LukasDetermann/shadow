package io.determann.shadow.api.renderer;

public interface EnumConstantRenderer
{
   /**
    * {@code MY_CONSTANT}
    */
   String declaration(RenderingContext renderingContext);

   /**
    * {@code MY_CONSTANT(parameters)}
    */
   String declaration(RenderingContext renderingContext, String parameters);

   /**
    * <pre>{@code
    *    MY_CONSTANT(parameters) {
    *       //content
    *    }
    * }</pre>
    */
   String declaration(RenderingContext renderingContext, String parameters, String content);

   /**
    * {@code MyEnum.MY_CONSTANT}
    */
   String invocation(RenderingContext renderingContext);
}
