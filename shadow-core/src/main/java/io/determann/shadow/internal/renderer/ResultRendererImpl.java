package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.Operations;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.renderer.ResultRenderer;
import io.determann.shadow.api.shadow.structure.C_Result;
import io.determann.shadow.api.shadow.type.C_Type;

import static io.determann.shadow.api.Provider.requestOrThrow;

public class ResultRendererImpl
      implements ResultRenderer
{
   private final C_Result result;

   public ResultRendererImpl(C_Result result)
   {
      this.result = result;
   }

   public static String declaration(RenderingContextWrapper context, C_Result result)
   {
      StringBuilder sb = new StringBuilder();

      sb.append(RenderingSupport.annotations(context, result, '\n'));

      C_Type type = requestOrThrow(result, Operations.RESULT_GET_TYPE);
      String renderedType = TypeRendererImpl.type(context, type);
      sb.append(renderedType);

      return sb.toString();
   }

   @Override
   public String declaration(RenderingContext renderingContext)
   {
      return declaration(new RenderingContextWrapper(renderingContext), result);
   }
}
