package io.determann.shadow.api.reflection.shadow.structure;

import io.determann.shadow.api.reflection.shadow.AnnotationableReflection;
import io.determann.shadow.api.shadow.structure.Receiver;
import io.determann.shadow.api.shadow.type.Shadow;

public interface ReceiverReflection extends Receiver,
                                            AnnotationableReflection
{
   Shadow getType();
}
