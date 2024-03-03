package io.determann.shadow.article.meta_model;

import org.apache.commons.lang3.NotImplementedException;

import javax.lang.model.element.ExecutableElement;
import java.util.Optional;

public class OneApi
{
   //@formatter:off
   //tag::core[]
@interface Supported
{
   Implementation[] value();
}

enum Implementation
{
   REFLECTION,
   ANNOTATION_PROCESSING
}

public interface Method
{
   @Supported({Implementation.ANNOTATION_PROCESSING, Implementation.REFLECTION})
   String getName();

   @Supported(Implementation.REFLECTION)
   Optional<FuncOp> getCodeModel();
}
   //end::core[]

   //tag::annotation_processing[]
class AnnotationProcessingMethodImpl implements Method
{
   private final ExecutableElement executableElement;

   public AnnotationProcessingMethodImpl(ExecutableElement executableElement)
   {
      this.executableElement = executableElement;
   }

   @Override
   public String getName()
   {
      return executableElement.getSimpleName().toString();
   }

   @Override
   public Optional<FuncOp> getCodeModel()
   {
      throw new NotImplementedException();
   }
}
//end::annotation_processing[]
//@formatter:on
}
