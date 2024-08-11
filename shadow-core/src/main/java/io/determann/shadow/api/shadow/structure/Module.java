package io.determann.shadow.api.shadow.structure;

import io.determann.shadow.api.shadow.Annotationable;
import io.determann.shadow.api.shadow.Documented;
import io.determann.shadow.api.shadow.Nameable;
import io.determann.shadow.api.shadow.QualifiedNameable;
import io.determann.shadow.api.shadow.type.Shadow;

public interface Module extends Shadow,
                                Nameable,
                                QualifiedNameable,
                                Annotationable,
                                Documented
{
}
