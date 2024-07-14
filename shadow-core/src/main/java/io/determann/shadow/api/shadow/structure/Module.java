package io.determann.shadow.api.shadow.structure;

import io.determann.shadow.api.shadow.*;
import io.determann.shadow.api.shadow.type.Shadow;

public interface Module extends Shadow,
                                Nameable,
                                QualifiedNameable,
                                Annotationable,
                                DeclaredHolder,
                                Documented
{
}
