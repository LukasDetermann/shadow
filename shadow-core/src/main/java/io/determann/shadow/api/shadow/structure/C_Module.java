package io.determann.shadow.api.shadow.structure;

import io.determann.shadow.api.shadow.C_Annotationable;
import io.determann.shadow.api.shadow.C_Documented;
import io.determann.shadow.api.shadow.C_Nameable;
import io.determann.shadow.api.shadow.C_QualifiedNameable;
import io.determann.shadow.api.shadow.type.C_Shadow;

public interface C_Module extends C_Shadow,
                                  C_Nameable,
                                  C_QualifiedNameable,
                                  C_Annotationable,
                                  C_Documented
{
}
