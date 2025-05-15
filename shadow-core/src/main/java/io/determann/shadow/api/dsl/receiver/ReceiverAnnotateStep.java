package io.determann.shadow.api.dsl.receiver;

import io.determann.shadow.api.shadow.type.C_Annotation;

public interface ReceiverAnnotateStep extends ReceiverRenderable
{
   ReceiverAnnotateStep annotate(String... annotation);

   ReceiverAnnotateStep annotate(C_Annotation... annotation);
}
