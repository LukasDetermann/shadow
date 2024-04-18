package io.determann.shadow.api.shadow;

import io.determann.shadow.api.Annotationable;
import io.determann.shadow.api.ImplementationDefined;

public interface Return extends Annotationable,
                                ImplementationDefined
{
   Shadow getType();
}
